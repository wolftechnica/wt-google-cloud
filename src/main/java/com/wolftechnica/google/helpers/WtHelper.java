package com.wolftechnica.google.helpers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wolftechnica.google.service.core.GCSCoreService;

public class WtHelper {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());
 
	private WtHelper() throws IllegalAccessException {
		throw new IllegalAccessException("Static helper.");
	}
	
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

}
