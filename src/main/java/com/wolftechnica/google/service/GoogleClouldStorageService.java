package com.wolftechnica.google.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.wolftechnica.google.exceptions.WtCloudException;
import com.wolftechnica.google.exceptions.WtCloudExceptionCodes;
import com.wolftechnica.google.helpers.DtoMapper;
import com.wolftechnica.google.helpers.PropertyFileHelper;
import com.wolftechnica.google.helpers.WtHelper;
import com.wolftechnica.google.service.core.GCSCoreService;

/**
 * GoogleClouldStorageService provides the various method for 
 * easy implementation of google storage service  
 * 
 * @author Wolftechnica
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
	//Dont initialize this here. read properties in constructor or init method
	private String bucketName;// = PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_PROJECT_BUCKET_NAME);
	
	
	/**
	 *  GoogleClouldStorageService : default constructor for initializing GoogleClouldStorageService and provide GCSCoreService object   
	 */
	public GoogleClouldStorageService() {
		try {
			LOG.info("initializing GCSCoreService...");
			clouldStorageCoreService = GCSCoreService.getGCSCoreService();
			LOG.info("GCSCoreService initialized");
		} catch (WtCloudException e) {
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
	 * @throws WtCloudException  : exception while retrieving object from storage
	 */
	public GoogleClouldStorageService(String apiKey, String projectId, String bucketName) throws WtCloudException {
		if(apiKey == null || projectId == null ||bucketName == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		this.clouldStorageCoreService = GCSCoreService.getGCSCoreService(apiKey, projectId);
		this.bucketName = bucketName;
	}




	/**
	 * The method uploadProfilePic  is to upload profile pic.
	 * @param file is the file to upload
	 * @param fileName is the name of the file 
	 * @throws WtCloudException : exception while retrieving object from storage 
	 */
	public void uploadProfilePic(byte[] file, String fileName) throws WtCloudException {
		if(file == null || fileName == null ) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
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
	 * @throws WtCloudException : exception while retrieving object from storage 
	 *
	 */
	public void uploadUserMedia(byte[] file,MediaType mediatype, String fileName) throws WtCloudException {
		if(file == null ||  mediatype == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		clouldStorageCoreService.addFileToBucket(file, bucketName,
				getRootFolder(mediatype) + System.currentTimeMillis() + ((fileName == null) ? WtHelper.generateRandomString(7) : fileName));
	}
 

	/**
	 * Method addFileInBucket : is used for uploading file to the storage
	 * 
	 * @param file - file to be upload. This will auto generate a unique file name.
	 * @param fileExtension - various file extension is supported from {@link FileExtenion} enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @return WtBlobId : generated info for uploaded file. Same can be used to retrieve stored file.
	 * @throws WtCloudException : exception while retrieving object from storage
	 */
	public WtBlobId uploadFileInBucket(byte[] file, FileExtenion fileExtension) throws WtCloudException {
		if(file == null || fileExtension == null ) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		return uploadFileInBucket( file, fileExtension, null, bucketName);
	}
	
	/**
	 * 
	 * @param file - file to be upload
	 * @param fileExtension - various file extension is supported from FileExtenion enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @param fileName - file name is optional, if it is provided this method will append current time to the filename
	 * 	fileName must not contain personal information
	 * @return WtBlobId : generated info for uploaded file, this can be used to retrieve the file using wt-util or google storage api
	 * @throws WtCloudException : exception while retrieving object from storage
	 */
	public WtBlobId uploadFileInBucket(byte[] file, FileExtenion fileExtension, String fileName) throws WtCloudException {
		return uploadFileInBucket( file, fileExtension, fileName, bucketName);
	}

	/**
	 * This Overloaded form of addFileInBucket method it for specify the bucket name to which we are going to add file
	 * 
	 * @param file : file to be upload 
	 * @param bucketName : specify the name of the bucket where one need to store uploaded file 
	 * @param fileExtension - various file extension is supported from FileExtenion enum.
	 *            like FileExtenion.IMG_JPEG, FileExtenion.IMG_GIF ..etc
	 * @param fileName : file name must be consist with 
	 * @return WTBlobId : generated info for uploaded file.
	 * @throws WtCloudException : exception while retrieving object from storage 
	 */
	public WtBlobId uploadFileInBucket(byte[] file, FileExtenion fileExtension, String fileName, String bucketName)
			throws WtCloudException {
		if (file == null || fileExtension == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
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
	 * @param fileExtenion : enum to declare the file extension
	 * @param subDirectoryPath : path of the subdirectory where file need to be stored 
	 * @return : generated info for uploaded file.
	 * @throws WtCloudException : exception if unable to load file in cloud storage
	 */
	public WtBlobId uploadFileInFolder(byte[] file, FileExtenion fileExtenion, String subDirectoryPath) throws WtCloudException {
		if (file == null || fileExtenion == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
		}
		return uploadFileInFolder(file, bucketName, fileExtenion, null, subDirectoryPath);
	}
	
	/**
	 * uploadFileInFolder :  This Method can used for uploading file in specified directory, 
	 * 		example, suppose one need to add a file in the bucket demobucket under folder structure /data/profile/image/,
	 * 		so he/she will pass /data/profile/image/ as a subdirectory parameter 	
	 * @param file : file to be upload 
	 * @param bucketName : specify the name of the bucket where one need to store uploaded file
	 * @param fileExtenion : enum to declare the file extension
	 * @param fileName : filename is optional 
	 * @param subDirectory : sub directory of parent bucket 
	 * @return : generated info for uploaded file.
	 * @throws WtCloudException  : exception if unable to load file in cloud storage
	 */
	public WtBlobId uploadFileInFolder(byte[] file, String bucketName, FileExtenion fileExtenion, String fileName, String subDirectory) throws WtCloudException {
		if(file == null || fileExtenion == null || subDirectory == null )
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
		fileName = (fileName == null) ? WtHelper.generateRandomString(17) : fileName;
		String fileNameOrigninal = fileName.concat("." + fileExtenion.getExtension());
		WtBlobId wtBlobId = new WtBlobId();
		BlobInfo blobInfo = clouldStorageCoreService.addFileToBucket(file, fileExtenion.getContentType(),bucketName, subDirectory, fileNameOrigninal);
		if (Arrays.asList(FileExtenion.IMG_JPEG, FileExtenion.IMG_JPG, FileExtenion.IMG_PNG).contains(fileExtenion)) {
			try {
				String fileNameTumbnailed = fileName.concat("-thumbnailed-75x75." + fileExtenion.getExtension());
				LOG.log(Level.INFO, "Processing for resizing for fileExtenion : {0}", fileExtenion);
				BlobInfo resizedFileblobInfo = clouldStorageCoreService.addFileToBucket(
						WtHelper.resizeImage(file, 75, 75, fileExtenion.getExtension()), fileExtenion.getContentType(),
						bucketName, subDirectory, fileNameTumbnailed);
				wtBlobId.setMediaLinkThumbailed(resizedFileblobInfo.getMediaLink());
				LOG.log(Level.INFO, "thumbnailed has been generated  : {0}", resizedFileblobInfo.getBlobId());
			} catch (Exception e) {
				LOG.log(Level.SEVERE, e.getMessage(), e);
				throw new WtCloudException(WtCloudExceptionCodes.STORAGE_UNABLE_TO_SAVE_THUMBNAILED);
			}
		}
		if (blobInfo != null) {
			wtBlobId.setName(blobInfo.getName());
			wtBlobId.setGeneration(blobInfo.getGeneration());
			wtBlobId.setBucket(blobInfo.getBucket());
			wtBlobId.setMediaLink(blobInfo.getMediaLink());
		}
		return wtBlobId;
	}

	/**
	 * getObjectMediaLinks : method can be used to fetch all the blobdto in a particular bucket
	 * @param bucketName : name of bucket 
	 * @return : List of BlobDTOs 
	 * @throws WtCloudException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public List<WtBlob> getObjectMediaLinks(String bucketName) throws WtCloudException {
		bucketName = (bucketName == null ) ? this.bucketName : bucketName;
		Page<com.google.cloud.storage.Blob> blobs = clouldStorageCoreService.getObjectsFromBucket(bucketName);
		if(blobs == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_NO_BUCKET_FOUND);
		}
		List<WtBlob> list = new ArrayList<>();
		for (com.google.cloud.storage.Blob blob : blobs.iterateAll()) {
			list.add(DtoMapper.objectToDto(blob));
		}
		return list;
	}
	

	/** 
	 * Method getFileFromBucket for fetching file object from the bucket 
	 * @param wtBlobId of type {@link WtBlobId} : this should conatin the generation id, bucket name and file name
	 * @return {@link WtBlob} consisting of all required attributes
	 * @throws WtCloudException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public WtBlob getFileFromBucket(WtBlobId wtBlobId ) throws WtCloudException {
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
	 * @throws WtCloudException : In case of blob not present in the bucket it throws GoogleException.
	 */
	public WtBlob getFileFromBucket(String bucketName, String blobName, long generation) throws WtCloudException {
		if (bucketName == null || blobName == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND);
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
	 * @throws WtCloudException : exception while retrieving object from storage
	 */
	public WtBlob getFileFromBucket(String blobName, long generation) throws WtCloudException {
		com.google.cloud.storage.Blob blob = clouldStorageCoreService.getObjectFromBucket(bucketName, blobName,
				generation);
		if(blob == null) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_NO_BUCKET_OBJECT_FOUND);
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
