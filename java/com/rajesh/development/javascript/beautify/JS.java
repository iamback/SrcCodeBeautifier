package com.rajesh.development.javascript.beautify;

import java.io.File;

import com.rajesh.development.javascript.util.Beautify;

public class JS {

	public static void main(String[] args) {
		updateDirSourceFile(new File("C:\\Cordys\\defaultInst\\cws\\sync\\330072\\AROPSale_NewDev\\AROP Sale Process"));
	}
	
	public static void updateDirSourceFile(File directoryFile) {
	    for (File fileEntry : directoryFile.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	if(".svn".equals(fileEntry.getName()))
	        		continue;
	        	updateDirSourceFile(fileEntry);
	        } else {
	        	String absolutePath = fileEntry.getAbsolutePath();
	            try{	            	
		            if(absolutePath.endsWith(".js"))   
		            	Beautify.updateFile(fileEntry,"js");
		            if(absolutePath.endsWith(".css"))   
		            	Beautify.updateFile(fileEntry,"css");
		            if(absolutePath.endsWith(".htm") || absolutePath.endsWith(".html"))   
		            	Beautify.updateFile(fileEntry,"html");
		            if(absolutePath.endsWith("#cws-xform#.cws"))   
		            	Beautify.updateXMLFile(fileEntry,"caf");
	            }catch(Exception e){
	            	System.out.println(absolutePath);
	            }
	        	
	        }
	    }
	}
}
