package com.wolftechnica.google.constants;

/**
 * FileExtenion : various supported extension type of file.
 *
 * @author Wolftechnica services
 *
 */
public enum FileExtenion {

	IMG_JPEG("jpeg","image/jpeg"), IMG_PNG("png","image/png"), IMG_JPG("jpg","image/jpg"), IMG_GIF("gif","image/gif"), 
	AUDIO_MP3("mp3","audio/mp3"), AUDIO_WMA("wma","audio/wma"), AUDIO_WAV("wav","audio/wav"), 
	VIDEO_MKV("mkv","video/mkv"), VIDEO_FLV("flv","video/mkv"), VIDEO_AVI("avi","video/msvideo"), VIDEO_VOB("vob","video/vob")
	, VIDEO_MP4("mp4","video/mp4") , FILE_ZIP("zip","application/zip");

	/**
	 * @param extension
	 */
	private FileExtenion(String extension, String contentType) {
		this.extension = extension;
		this.contentType = contentType;
	}

	/** The extension. */
	private final String extension;
	
	/** The extension. */
	private final String contentType;

	public String getExtension() {
		return extension;
	}

	public String getContentType() {
		return contentType;
	}
	
	/*public static FileExtenion getFileExtenion(String contentType){
		switch (contentType) {
		case "audio/mp3":
			return FileExtenion.AUDIO_MP3;
		case "image/jpeg":
			return FileExtenion.IMG_JPEG;
		case "image/png":
			return FileExtenion.IMG_PNG;
		case "image/gif":
			return FileExtenion.IMG_GIF;
		case "video/vob":
			return FileExtenion.VIDEO_VOB;
		case "image/jpg":
			return FileExtenion.IMG_JPG;
		default:
			break;
		}
		return null;
		
		
	}*/
	
	
	

}
