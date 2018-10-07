package com.wolftechnica.google.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
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
	public static String generateRandomString(int length){
		Random random = new Random();
		String randomString = random.ints(48,122)
                .filter(i-> (i<57 || i>65) && (i <90 || i>97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
		LOG.log(Level.INFO, "random genrated string : {0} ", randomString);
        return randomString;
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
	
}
