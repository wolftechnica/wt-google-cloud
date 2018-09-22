package com.wolftechnica.google.service.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.StorageOptions;
import com.wolftechnica.google.constants.GoogleConstant;
import com.wolftechnica.google.exceptions.WtStorageException;
import com.wolftechnica.google.exceptions.WtStorageExceptionCodes;
import com.wolftechnica.google.helpers.PropertyFileHelper;

/**
 * GCSCoreService : Below class provide the core implementation of the methods and operations provided by google storage
 * 
 * @author Wolftechnica services
 * @version 1.0
 */
public class GCSCoreService {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());
	private static com.google.cloud.storage.Storage storage;
	private static GCSCoreService clouldStorageCoreService;

	private GCSCoreService() throws IllegalAccessException {
		if(clouldStorageCoreService != null ) {
			throw new IllegalAccessException("GoogleClouldStorageCoreService is singletion class");
		}
		
	}

	/**
	 * Method getGCSCoreService is provide the singleton object of core service class.
	 * @return GCSCoreService
	 * @throws IllegalAccessException if no property file found
	 */
	public static GCSCoreService getGCSCoreService() throws IllegalAccessException {
		if (clouldStorageCoreService == null) {
			clouldStorageCoreService = new GCSCoreService();
			try {
				init(PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_STORAGE_API_KEY),PropertyFileHelper.getProperty(GoogleConstant.CONFIG_GC_PROJECT_ID) );
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Unable to read properties", e);
			}
		}
		return clouldStorageCoreService;
	}
	
	/**
	 * Method getGCSCoreService is provide the singleton object of core service class with the provided apikey and projectId.
	 * @param apiKey : path of the API key generated from the google cloud storage
	 * @param projectId : name of the project ID
	 * @return Singleton instance of GCSCoreService
	 * @throws IllegalAccessException if no key passed
	 */
	public static GCSCoreService getGCSCoreService(String apiKey, String projectId) throws IllegalAccessException {
		if (clouldStorageCoreService == null) {
			clouldStorageCoreService = new GCSCoreService();
			try {
				init(apiKey, projectId);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Unable to read properties", e);
			}
		}
		return clouldStorageCoreService;
	}
	

	/**
	 * Below statement create credentials object by reading json api key generated by google could,
	 *  which is mentions against key com.wolftechnica.google.api.key in property file.
	 *  example : com.wolftechnica.google.api.key=/Users/A121qae/springboot-tempemp3-72e44c75e603.json
	 *  after creating credentials object, it creates a storage object by using it.
	 *  
	 * @param apiKey google api key
	 * @param projectId google project ids
	 * @throws IOException if couldnt get the apikey
	 */
	public static void init(String apiKey, String projectId) throws IOException {
		Credentials credentials = GoogleCredentials.fromStream(
				new FileInputStream(apiKey));
		storage = StorageOptions.newBuilder().setCredentials(credentials)
				.setProjectId(projectId).build().getService();
	}
	

	/**
	 * Method createBucket(String bucketName) :  provides facility to create the bucket with specified name
	 * @param bucketName : name of the bucket that one want to create under the given storage.
	 */
	public void createBucket(String bucketName) {
		try {
			Bucket bucket = storage.create(BucketInfo.of(bucketName));
			LOG.info("Bucktet created succefully. : " + bucket.getName());
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception occur", e);
		}
	}

	/**
	 * Method getBucket(String bucketName) : fetch the bucket with the given name
	 * @param bucketName : name of bucket the one wants to fetch form storage.
	 * @return if bucket is present oject of Bucket is provided otherwise null is returned to the callee
	 */
	public Bucket getBucket(String bucketName) {
		Bucket bucket = null;
		try {
			bucket = storage.get(bucketName);
			LOG.log(Level.INFO, "bucket : {0} ", bucket);
			return bucket;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception occur", e);
		}
		return bucket;
	}

	/**
	 * Method addFileToBucket : provide control to add a file to the bucket. 
	 * @param content : array of bytes is that needs to be saved in the bucket
	 * @param bucketName : name of the bucket where we need to add file
	 * @param fileName : name of the file with extension i.e. demo.jpeg
	 * @return return blob info object
	 */
	public BlobInfo addFileToBucket(byte[] content, String bucketName, String fileName) {
		final String file = System.currentTimeMillis() + "-" + fileName;
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, file)
				.setAcl(new ArrayList<>(
						Arrays.asList(Acl.of(User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER))))
				.build(), content);
		LOG.log(Level.INFO, " blobInfo : ", blobInfo);
		return blobInfo;
	}

	/**
	 * @param content : array of bytes is that needs to be saved in the bucket
	 * @param bucketName : name of the bucket where we need to add file
	 * @param subDirectory : sub directory of parent bucket
	 * @param fileName : name of the file with extension i.e. demo.jpeg
	 */
	public void addFileToBucket(byte[] content, String bucketName, String subDirectory, String fileName) {
		final String file = System.currentTimeMillis() + "-" + fileName;
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, subDirectory + file)
				.setAcl(new ArrayList<>(
						Arrays.asList(Acl.of(User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER))))
				.build(), content);
		LOG.log(Level.INFO, " blobInfo : ", blobInfo);
	}

	/**
	 * @param bucketName : name of the bucket where we need to add file
	 * @return returns blob page
	 * @throws WtStorageException : exception while retrieving object from storage
	 */
	public Page<Blob> getObjectsFromBucket(String bucketName) throws WtStorageException {
		Bucket bucket = getBucket(bucketName);
		if (bucket == null) {
			LOG.log(Level.WARNING, "No bucket exist : ", bucketName);
			throw new WtStorageException(WtStorageExceptionCodes.NO_BUCKET_FOUND);
		}
		return bucket.list();
	}

	/**
	 * @param bucketName : name of the bucket where we need to add file
	 * @param blobName blobName
	 * @param generation generation name
	 * @return Blob : returns google blob object
	 * @throws WtStorageException exception while retrieving data from google storage
	 */
	public Blob getObjectFromBucket(String bucketName, String blobName, long generation) throws WtStorageException {
		LOG.log(Level.INFO, "params : {0}  ", Arrays.asList(blobName, generation));
		Blob blob = null;
		try {
			blob = getBucket(bucketName).get(blobName, BlobGetOption.generationMatch(generation));
			if (blob == null) {
				LOG.log(Level.WARNING, "No bucket exist : ", bucketName);
				throw new WtStorageException(WtStorageExceptionCodes.NO_BUCKET_OBJECT_FOUND);
			}
		} catch (Exception e) {
			LOG.severe(e.getMessage());
		}
		if (blob != null)
			LOG.log(Level.INFO, " blob : ", blob.getGeneratedId());
		return blob;
	}
}
