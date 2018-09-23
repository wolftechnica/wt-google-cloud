package com.wolftechnica.google.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wolftechnica.google.exceptions.WtCloudException;
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

	public static byte[] fileToByteArray(File file) throws WtCloudException {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			throw new WtCloudException(e.getMessage());
		}
	}
	
}
