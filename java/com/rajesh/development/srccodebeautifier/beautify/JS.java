package com.rajesh.development.srccodebeautifier.beautify;

import java.io.File;

import com.rajesh.development.srccodebeautifier.util.Beautify;
import com.rajesh.development.srccodebeautifier.util.SourceFileConstants;
import com.rajesh.development.util.PropertiesUtil;

public class JS {

	private static final String SOURCE_DIR_PATH_PROPERTY_NAME = "SourceDirectoryPath";
	private static final String SOURCE_DIR_PATH_DEFAULT_VALUE = "C:\\Cordys\\defaultInst\\cws\\sync\\330072\\AROPSale_NewDev\\AROP Sale Process";

	public static void main(String[] args) {
		updateDirSourceFile(new File(PropertiesUtil.getProperty(SOURCE_DIR_PATH_PROPERTY_NAME, SOURCE_DIR_PATH_DEFAULT_VALUE)));
	}

	public static void updateDirSourceFile(File directoryFile) {
		for (File fileEntry : directoryFile.listFiles()) {
			if (fileEntry.isDirectory()) {
				if(SourceFileConstants.SVN_FOLDER_NAME.equals(fileEntry.getName()))
					continue;
				updateDirSourceFile(fileEntry);
			} else {
				String absolutePath = fileEntry.getAbsolutePath();
				try{	            	
					if(absolutePath.endsWith(SourceFileConstants.JS_FILE_EXTENSION))   
						Beautify.updateFile(fileEntry,SourceFileConstants.JS_FILE_TYPE);
					if(absolutePath.endsWith(SourceFileConstants.CSS_FILE_EXTENSION))   
						Beautify.updateFile(fileEntry,SourceFileConstants.CSS_FILE_TYPE);
					if(absolutePath.endsWith(SourceFileConstants.HTM_FILE_EXTENSION) || absolutePath.endsWith(SourceFileConstants.HTML_FILE_EXTENSION))   
						Beautify.updateFile(fileEntry,SourceFileConstants.HTML_FILE_TYPE);
					if(absolutePath.endsWith(SourceFileConstants.CAF_FILE_EXTENSION))
						Beautify.updateXMLFile(fileEntry,SourceFileConstants.CAF_FILE_TYPE);
				}catch(Exception e){
					System.out.println(absolutePath);
				}

			}
		}
	}
}
