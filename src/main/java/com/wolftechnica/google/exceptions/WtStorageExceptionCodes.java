package com.wolftechnica.google.exceptions;

/**
 * Enum WtStorageExceptionCodes has all the expected situations in which {@link WtStorageException} can be thrown
 * @author Wolftechnica services
 *
 */
public enum WtStorageExceptionCodes {

	UNABLE_TO_CONNECT(70001, "unable to connect to google cloud storage."),
	NO_BUCKET_FOUND(70002, "No bucket found with given name."),
	NO_BUCKET_OBJECT_FOUND(70003, "No bucket object found with given name."),
	MANDATORY_ARGUMENTS_NOT_FOUND(70004, "Mandatory argument not provided to the method.")

	;

	private WtStorageExceptionCodes(Integer code, String description) {
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
