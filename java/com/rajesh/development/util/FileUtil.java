package com.rajesh.development.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileUtil {
	public static String readFileContent(File filePath){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error occured while opening the file",e);
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				line = br.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
			} catch (IOException e) {
				throw new RuntimeException("Error occured while reading the file",e);
			}
			return sb.toString();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException("Error occured while closing the file",e);
			}
		}
	}

	public static boolean deleteFile(File filePath){
		return filePath.delete();
	}

	public static File createFile(String fullFilePath)
	{	
		try {
			File file = new File(fullFilePath);
			boolean fileNotExists = true;
			if(file.exists()){
				fileNotExists = deleteFile(file);
			}
			if(fileNotExists){
				 if(file.createNewFile()){
					 return file;
				 }else{
					 throw new RuntimeException("Error occured while creating the file"); 
				 }
			}
			else
				throw new RuntimeException("Error occured while deleting the file before file creation");
		} catch (IOException e) {
			throw new RuntimeException("Error occured while creating the file",e);
		}
	}

	public static void writeIntoFile(File file, String fileContent) {
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fileContent);
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException("Error occured while writing the file",e);
		}
	}
	
	public static boolean renameFile(String oldFileName, String newFileName){	 
		File oldfile =new File(oldFileName);
		File newfile =new File(newFileName); 
		return oldfile.renameTo(newfile); 
    }
}
