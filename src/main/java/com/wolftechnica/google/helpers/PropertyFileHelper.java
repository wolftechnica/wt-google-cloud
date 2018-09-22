package com.wolftechnica.google.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wolftechnica.google.service.core.GCSCoreService;

public class PropertyFileHelper {

	private static final Logger LOG = Logger.getLogger(GCSCoreService.class.getName());

	private static Properties appProps;

	static {
		appProps = new Properties();
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "wf-google-ext.properties";
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (Exception e) {
			appConfigPath = rootPath+ "wf-google.properties";
			try {
				appProps.load(new FileInputStream(appConfigPath));
			} catch (IOException e1) {
				LOG.log(Level.SEVERE, "Exception occured while loading default file : IOException", e1);
			}

			LOG.log(Level.INFO, "Exception occured while loading ext file");
		}
	}

	public static String getProperty(String key) {
		return appProps.getProperty(key);
	}

	private PropertyFileHelper() throws IllegalAccessException {
		throw new IllegalAccessException("Static helper.");
	} 

}
