package com.wolftechnica.google.exceptions;

/**
 * The WtStorageException wraps all checked standard Java exception and enriches
 * them with a custom error code. You can use this code to retrieve localized
 * error messages and to link to our online documentation.
 * 
 * @author Wolftechnica services
 */
public class WtStorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718828512143293558L;

	private WtStorageExceptionCodes exceptionCodes;

	public WtStorageException() {
		super();

	}

	public WtStorageException(WtStorageExceptionCodes exceptionCodes) {
		super();
		this.setExceptionCodes(exceptionCodes);
	} 

	public WtStorageExceptionCodes getExceptionCodes() {
		return exceptionCodes;
	}

	public void setExceptionCodes(WtStorageExceptionCodes exceptionCodes) {
		this.exceptionCodes = exceptionCodes;
	}

}
