package com.wolftechnica.google.constants;

/**
 * FileExtenion : various supported extension type of file.
 *
 * @author Wolftechnica services
 *
 */
public enum FileExtenion {

	IMG_JPEG("jpeg"), IMG_PNG("png"), IMG_JPG("jpg"), IMG_GIF("gif"), AUDIO_MP3("mp3"), AUDIO_WMA("png"), AUDIO_WAV(
			"wav"), VIDEO_MKV("mkv"), VIDEO_FLV("flv"), VIDEO_AVI("avi"), VIDEO_VOB("vob");

	/**
	 * @param extension
	 */
	private FileExtenion(String extension) {
		this.extension = extension;
	}

	/** The extension. */
	private final String extension;

	public String getExtension() {
		return extension;
	}

}
