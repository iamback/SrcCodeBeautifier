package com.rajesh.development.javascript.util;
 
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.rajesh.development.util.FileUtil;
import com.rajesh.development.util.XMLUtil;
 
public class Beautify {
 
  public static final String BEAUTIFY_JS = "beautify.js";
  public static final String BEAUTIFY_CSS = "beautify-css.js";
  public static final String BEAUTIFY_HTML = "beautify-html.js";
 
  public static String beautify(String fileName, String fileContent, String functionName, String option) {
    Context cx = Context.enter();
    Scriptable scope = cx.initStandardObjects();
    InputStream resourceAsStream = Beautify.class.getResourceAsStream(fileName);
 
    try {
      Reader reader = new InputStreamReader(resourceAsStream);
      cx.evaluateReader(scope, reader, "__"+fileName, 1, null);
      reader.close();
    } catch (IOException e) {
      throw new Error("Error reading " + fileName);
    }
    scope.put("content", scope, fileContent);
    return (String) cx.evaluateString(scope, functionName+"(content, "+option+")",
        "inline", 1, null);
  }
 
  public static void updateFile(File fileName, String type) {
	  String fileContent = FileUtil.readFileContent(fileName);
      String absolutePath = fileName.getAbsolutePath();
	  int fileExtensionIndex = absolutePath.lastIndexOf(".");
      String actualFileName = absolutePath.substring(0, fileExtensionIndex);
      String extension = absolutePath.substring(fileExtensionIndex);
      String newFileName = actualFileName+"#"+type+"beautify"+extension;
      File newFile = FileUtil.createFile(newFileName);
      if(newFile.exists()){
    	  String sourceFile = null;
    	  String functionName = null;
    	  String option = null;
    	  if("js".equals(type)){
    		  sourceFile = BEAUTIFY_JS;
    		  functionName = "js_beautify";
    		  option = "{indent_size:2}";
    	  }else if("css".equals(type)){
    		  sourceFile = BEAUTIFY_CSS;
    		  functionName = "css_beautify";
    		  option = "{"+
	    			      "'indent_size': 1,"+
	    			      "'indent_char': '\t'"+
    			      "}";
    	  }else if("html".equals(type)){
    		  sourceFile = BEAUTIFY_HTML;
    		  functionName = "style_html";
    		  option = "{"+
	    				  "'brace_style': 'collapse',"+
	    				  "'break_chained_methods': false,"+
	    				  "'indent_char':  '  ',"+
	    				  "'indent_scripts': 'normal',"+
	    				  "'indent_size': 4,"+
	    				  "'keep_array_indentation': false,"+
	    				  "'max_preserve_newlines': 5,"+
	    				  "'preserve_newlines': true,"+
	    				  "'space_after_anon_function': true,"+
	    				  "'space_before_conditional': true,"+
	    				  "'unescape_strings': false,"+
	    				  "'wrap_line_length': '0'"+
					    "}";
    	  }
    	  FileUtil.writeIntoFile(newFile, Beautify.beautify(sourceFile, fileContent,functionName, option));
    	  String actualFilePath = fileName.getAbsolutePath();
    	  if(!FileUtil.deleteFile(fileName)){
    		  throw new RuntimeException("Error occured while deleting the original file");		  
    	  }
    	  FileUtil.renameFile(newFile.getAbsolutePath(), actualFilePath);
      }	 
  }
  public static void updateXMLFile(File fileName, String type) {
	  
	  String jsContent = XMLUtil.getCDATAContentFromFile(fileName,"xformsscript");

	  if(jsContent == null)
		  return;
	  
    	  String sourceFile = null;
    	  String functionName = null;
    	  String option = null;
    	  if("caf".equals(type)){
    		  sourceFile = BEAUTIFY_JS;
    		  functionName = "js_beautify";
    		  option = "{indent_size:2}";
    	  }
    	  String updatedContent = Beautify.beautify(sourceFile, jsContent,functionName, option);
    	  XMLUtil.writeCDATAContentIntoFile(fileName,"xformsscript",updatedContent);
  }
}