package com.wolftechnica.google.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.BlobInfo;
import com.wolftechnica.google.constants.FileExtenion;
import com.wolftechnica.google.constants.GoogleConstant;
import com.wolftechnica.google.constants.MediaType;
import com.wolftechnica.google.dto.WtBlob;
import com.wolftechnica.google.dto.WtBlobId;
import com.wolftechnica.google.exceptions.WtStorageException;
import com.wolftechnica.google.exceptions.WtStorageExceptionCodes;
import com.wolftechnica.google.helpers.DtoMapper;
import com.wolftechnica.google.helpers.PropertyFileHelper;
import com.wolftechnica.google.helpers.WtHelper;
import com.wolftechnica.google.service.core.GCSCoreService;

/**
 * GoogleClouldStorageService provides the various method for 
 * easy implementation of google storage service  
 * 
 * @author Wolftechnica services
 * @version 1.0
 */
public class GoogleClouldStorageService {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());
	
	private GCSCoreService clouldStorageCoreService;

	/**
	 * bucketName : configured bucket name with property
	 * com.wolftechnica.google.api.project.bucketname in property file
	 * wf-google.properties
	 */
	private String bucketName = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_PROJECT_BUCKET_NAME);
	
	
	/**
	 *  GoogleClouldStorageService : default constructor for initializing GoogleClouldStorageService and provide GCSCoreService object   
	 */
	public GoogleClouldStorageService() {
		try {
			LOG.info("initializing GCSCoreService...");
			clouldStorageCoreService = GCSCoreService.getGCSCoreService();
			LOG.info("GCSCoreService initialized");
		} catch (IllegalAccessException e) {
			LOG.severe(e.getLocalizedMessage());
		}
	}
	
	/**
	 * GoogleClouldStorageService : parameterized constructor for initializing GoogleClouldStorageService and provide GCSCoreService object 
	 *  
	 * @param bucketName : specify the name of the bucket where one need to store uploaded file  
	 * @param apiKey : path of the API key generated from the google cloud storage
	 * @param projectId : name of the project ID
	 * 
	 * @throws IllegalAccessException : exception while retrieving object from storage
	 * @throws WtStorageException  : exception while retrieving object from storage
	 */
	public GoogleClouldStorageService(String apiKey, String projectId, String bucketName) throws IllegalAccessException, WtStorageException {
		if(apiKey == null || projectId == null ||bucketName == null) {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		this.clouldStorageCoreService = GCSCoreService.getGCSCoreService(apiKey, projectId);
		this.bucketName = bucketName;
	}




	/**
	 * The method uploadProfilePic  is to upload profile pic.
	 * @param file is the file to upload
	 * @param fileName is the name of the file 
	 * @throws WtStorageException : exception while retrieving object from storage 
	 */
	public void uploadProfilePic(byte[] file, String fileName) throws WtStorageException {
		if(file == null || fileName == null ) {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		String prefix = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_FOLDER_PROFILE)
				+ System.currentTimeMillis();
		clouldStorageCoreService.addFileToBucket(file, bucketName, prefix + fileName);

	}

	/**
	 * uploadUserMedia : method can be used for storing user's file in bucket under media folder 
	 * @param file : file to be uploaded
	 * @param mediatype : supported media types AUDIO, VIDEO, DOCUMENTS, PICTURES
	 * @param fileName : file name is optional when it will be provided it will append to the timestamp 
	 * otherwise random generated string is concatenated with timestamp.
	 * @throws WtStorageException : exception while retrieving object from storage 
	 *
	 */
	public void uploadUserMedia(byte[] file,MediaType mediatype, String fileName) throws WtStorageException {
		if(file == null || fileName == null || mediatype == null) {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		clouldStorageCoreService.addFileToBucket(file, bucketName,
				getRootFolder(mediatype) + System.currentTimeMillis() + ((fileName == null) ? WtHelper.generateRandomString(7) : fileName));
	}
 

	/**
	 * Method addFileToBucket : is used for uploading file to the storage
	 * 
	 * @param file - file to be upload. This will auto generate a unique file name.
	 * @param fileExtension - various file extension is supported from {@link FileExtenion} enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @return WtBlobId : generated info for uploaded file. Same can be used to retrieve stored file.
	 * @throws WtStorageException : exception while retrieving object from storage
	 */
	public WtBlobId addFileToBucket(byte[] file, FileExtenion fileExtension) throws WtStorageException {
		if(file == null || fileExtension == null ) {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		return addFileToBucket(bucketName, file, fileExtension, null);
	}
	
	/**
	 * 
	 * @param file - file to be upload
	 * @param fileExtension - various file extension is supported from FileExtenion enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @param fileName - file name is optional, if it is provided this method will append current time to the filename
	 * 	fileName must not contain personal information
	 * @return WtBlobId : generated info for uploaded file, this can be used to retrieve the file using wt-util or google storage api
	 * @throws WtStorageException : exception while retrieving object from storage
	 */
	public WtBlobId addFileToBucket(byte[] file, FileExtenion fileExtension, String fileName) throws WtStorageException {
		return addFileToBucket(bucketName, file, fileExtension, fileName);
	}

	/**
	 * This Overloaded form of addFileToBucket method it for specify the bucket name to which we are going to add file
	 * 
	 * @param file : file to be upload 
	 * @param bucketName : specify the name of the bucket where one need to store uploaded file 
	 * @param fileExtension - various file extension is supported from FileExtenion enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @param fileName : file name must be consist with 
	 * @return WTBlobId : generated info for uploaded file.
	 * @throws WtStorageException : exception while retrieving object from storage 
	 */
	public WtBlobId addFileToBucket(String bucketName, byte[] file, FileExtenion fileExtension, String fileName)
			throws WtStorageException {
		if (file == null || fileExtension == null) {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		fileName = (fileName == null) ? WtHelper.generateRandomString(7) : fileName;
		fileName = fileName.concat("." + fileExtension.getExtension());
		LOG.log(Level.INFO, "File name : {0}", fileName);
		WtBlobId wtBlobId = new WtBlobId();
		BlobInfo blobInfo = clouldStorageCoreService.addFileToBucket(file, bucketName, fileName);
		if (blobInfo != null) {
			wtBlobId.setName(blobInfo.getName());
			wtBlobId.setGeneration(blobInfo.getGeneration());
			wtBlobId.setBucket(blobInfo.getBucket());
		}
		return wtBlobId;
	}

	/**
	 * @param file : file to be upload 
	 * @param bucketName : specify the name of the bucket where one need to store uploaded file
	 * @param fileName : file name must be consist with
	 * @param subDirectory : sub directory of parent bucket
	 */
	public void uploadFileInFolder(byte[] file, String bucketName, String fileName, String subDirectory) {
		clouldStorageCoreService.addFileToBucket(file, bucketName, subDirectory, fileName);
	}

	/**
	 * getObjectMediaLinks : method can be used to fetch all the blobdto in a particular bucket
	 * @param bucketName : name of bucket 
	 * @return : List of BlobDTOs 
	 * @throws WtStorageException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public List<WtBlob> getObjectMediaLinks(String bucketName) throws WtStorageException {
		bucketName = (bucketName == null ) ? this.bucketName : bucketName;
		Page<com.google.cloud.storage.Blob> blobs = clouldStorageCoreService.getObjectsFromBucket(bucketName);
		if(blobs == null) {
			throw new WtStorageException(WtStorageExceptionCodes.NO_BUCKET_OBJECT_FOUND);
		}
		List<WtBlob> list = new ArrayList<>();
		for (com.google.cloud.storage.Blob blob : blobs.iterateAll()) {
			list.add(DtoMapper.objectToDto(blob));
		}
		return list;
	}
	

	/** 
	 * Method getFileFromBucket for fetching file object from the bucket 
	 * @return {@link WtBlob} consisting of all required attributes
	 * @throws WtStorageException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public WtBlob getFileFromBucket(WtBlobId wtBlobId ) throws WtStorageException {
		com.google.cloud.storage.Blob blob = clouldStorageCoreService.getObjectFromBucket(wtBlobId.getBucket(), wtBlobId.getName(),
				wtBlobId.getGeneration());
		return DtoMapper.objectToDto(blob);
	}
	
	/**
	 * getFileFromBucket : Method can be used for get a specific file from bucket
	 * @param bucketName : bucket name  where file is present
	 * @param blobName : name of the file
	 * @param generation : generation which is generated for every blob while uploading in bucket
	 * @return : BlobDTO [blobId-Blob id consist of bucketname/name/generation ,mediaLink-URL of the particular blob,content-Array of byte for file]
	 * @throws WtStorageException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public WtBlob getFileFromBucket(String bucketName, String blobName, long generation) throws WtStorageException {
		if (bucketName == null || blobName == null)  {
			throw new WtStorageException(WtStorageExceptionCodes.MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		com.google.cloud.storage.Blob blob = clouldStorageCoreService.getObjectFromBucket(bucketName, blobName,
				generation);
		return DtoMapper.objectToDto(blob);
	}
	
	/**
	 * getFileFromBucket : This overloaded method can be used to get a specific file from initialized bucket
	 * 
	 * @param blobName : name of the file
	 * @param generation : generation which is generated for every blob while uploading in bucket
	 * @return : BlobDTO [blobId-Blob id consist of bucketname/name/generation ,mediaLink-URL of the particular blob,content-Array of byte for file]
	 * @throws WtStorageException : exception while retrieving object from storage
	 */
	public WtBlob getFileFromBucket(String blobName, long generation) throws WtStorageException {
		com.google.cloud.storage.Blob blob = clouldStorageCoreService.getObjectFromBucket(bucketName, blobName,
				generation);
		if(blob == null) {
			throw new WtStorageException(WtStorageExceptionCodes.NO_BUCKET_OBJECT_FOUND);
		}
		return DtoMapper.objectToDto(blob);
	} 
	
	

	/**
	 * getRootFolder : Method is used for locating folder according to their content type.
	 * 
	 * @param mediatype : : supported media types AUDIO, VIDEO, DOCUMENTS, PICTURES
	 * @return path of the folder
	 */
	private String getRootFolder(MediaType mediatype) {
		String rootFolder = "";
		switch (mediatype) {
		case AUDIO:
			rootFolder = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_FOLDER_AUDIO);
			break;
		case VIDEO:
			rootFolder = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_FOLDER_VIDEO);
			break;
		case DOCUMENTS:
			rootFolder = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_FOLDER_DOCUMENTS);
			break;
		case PICTURES:
			rootFolder = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_FOLDER_PICTURES);
			break;
		default:
			break;
		}
		return rootFolder;
	}

}
