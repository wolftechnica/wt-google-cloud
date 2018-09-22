package com.wolftechnica.google.constants;

/**
 *GoogleConstant : contain all the final keys for fetching property from property files
 * 
 * @author Wolftechnica services
 *
 */
public class GoogleConstant {

	public static final String CONFIG_GC_STORAGE_API_KEY = "com.wolftechnica.google.api.key";
	public static final String CONFIG_GC_PROJECT_ID = "com.wolftechnica.google.api.project.id";
	public static final String CONFIG_GC_PROJECT_BUCKET_NAME = "com.wolftechnica.google.api.project.bucketname";

	public static final String CONFIG_GC_FOLDER_PROFILE = "com.wolftechnica.google.api.folder.profile";
	public static final String CONFIG_GC_FOLDER_MEDIA = "com.wolftechnica.google.api.folder.media";
	public static final String CONFIG_GC_FOLDER_AUDIO = "com.wolftechnica.google.api.folder.audio";
	public static final String CONFIG_GC_FOLDER_VIDEO = "com.wolftechnica.google.api.folder.video";
	public static final String CONFIG_GC_FOLDER_DOCUMENTS = "com.wolftechnica.google.api.folder.documents";
	public static final String CONFIG_GC_FOLDER_PICTURES = "com.wolftechnica.google.api.folder.pictures";

	private GoogleConstant() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

}
