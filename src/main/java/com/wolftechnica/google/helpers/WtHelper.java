package com.wolftechnica.google.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import com.wolftechnica.google.exceptions.WtCloudException;
import com.wolftechnica.google.exceptions.WtCloudExceptionCodes;
import com.wolftechnica.google.service.core.GCSCoreService;

/**
 * Class {@link WtHelper} provides the various utility that can make your integration easy.
 * @author Wolftechnica
 *
 */
public class WtHelper {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());
 
	private WtHelper() throws IllegalAccessException {
		throw new IllegalAccessException("Static helper.");
	}
	
	/**
	 * create a random string value that can be used for various purpose.
	 * @param length : is the length of the the string value you that you need to generate 
	 * @return random string
	 */
	public static String generateRandomString(int length) {
		StringBuilder builder = new StringBuilder();
		while (length-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	


	/**
	 * 
	 * @param file
	 * @return
	 * @throws WtCloudException
	 */
	public static byte[] fileToByteArray(File file) throws WtCloudException {
		byte[] bytesArray = new byte[(int) file.length()];
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.read(bytesArray);
		} catch (FileNotFoundException e) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_SINGLETON_FILE_NOT_EXCEPTION);
		} catch (IOException e) {
			throw new WtCloudException(WtCloudExceptionCodes.STORAGE_SINGLETON_IO_EXCEPTION);
		}
		return bytesArray;

	}
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
 
}
