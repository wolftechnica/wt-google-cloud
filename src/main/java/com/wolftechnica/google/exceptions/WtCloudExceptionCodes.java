package com.wolftechnica.google.exceptions;

/**
 * Enum WtStorageExceptionCodes has all the expected situations in which {@link WtCloudException} can be thrown
 * @author Wolftechnica services
 *
 */
public enum WtCloudExceptionCodes {

	STORAGE_UNABLE_TO_CONNECT(70001, "unable to connect to google cloud storage."),
	STORAGE_NO_BUCKET_FOUND(70002, "No bucket found with given name."),
	STORAGE_NO_BUCKET_OBJECT_FOUND(70003, "No bucket object found with given name."),
	STORAGE_MANDATORY_ARGUMENTS_NOT_FOUND(70004, "Mandatory argument not provided to the method."),
	STORAGE_SINGLETON_CLASS_ACCESS_EXCEPTION(70005, "Singleton class can instantiated once only."),
	STORAGE_SINGLETON_FILE_NOT_EXCEPTION(70006, "File not found."),
	STORAGE_SINGLETON_IO_EXCEPTION(70007, "IO exception occured."),
	STORAGE_UNABLE_TO_SAVE_THUMBNAILED(70008, "Unable to save tumbnailed")

	;

	private WtCloudExceptionCodes(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	/** The exception code. */
	private final Integer code;

	/** The exception description. */
	private final String description;

	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
