package com.wolftechnica.google.exceptions;

/**
 * The WtStorageException wraps all checked standard Java exception and enriches
 * them with a custom error code. You can use this code to retrieve localized
 * error messages and to link to our online documentation.
 * 
 * @author Wolftechnica services
 */
public class WtCloudException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718828512143293558L;

	private WtCloudExceptionCodes exceptionCodes;

	public WtCloudException(String string) {
		super();

	}

	public WtCloudException(WtCloudExceptionCodes exceptionCodes) {
		super();
		this.setExceptionCodes(exceptionCodes);
	} 

	public WtCloudExceptionCodes getExceptionCodes() {
		return exceptionCodes;
	}

	public void setExceptionCodes(WtCloudExceptionCodes exceptionCodes) {
		this.exceptionCodes = exceptionCodes;
	}

}
