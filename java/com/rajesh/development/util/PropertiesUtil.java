package com.rajesh.development.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties properties = new Properties();
	static {		
		FileReader output = null;
		try { 
			output =new FileReader(ConfigurationConstants.PROPERTIES_FILE_PATH);
			properties.load(output);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getProperty(String propertyName, String defaultValue){
		String propValue = (String) properties.get(propertyName);
		if(propValue != null)
			return propValue;
		return defaultValue;
	}
	
}