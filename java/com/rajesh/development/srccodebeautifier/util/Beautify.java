package com.rajesh.development.srccodebeautifier.util;

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

	private static final String CAF_SCRIPT_TAG_NAME = "xformsscript";
	
	private static final String BEAUTIFY_JS = "beautify.js";
	private static final String BEAUTIFY_CSS = "beautify-css.js";
	private static final String BEAUTIFY_HTML = "beautify-html.js";
	private static final String BEAUTIFY_JS_FUNCTION = "js_beautify";
	private static final String BEAUTIFY_CSS_FUNCTION = "css_beautify";
	private static final String BEAUTIFY_HTML_FUNCTION = "style_html";
	
	private static final String JS_FORMAT_OPTION = "{indent_size:2}";
	
	private static final String CSS_FORMAT_OPTION = new StringBuffer("{")
														.append("'indent_size': 1,")
														.append("'indent_char': '\t'")
														.append("}").toString();
	
	private static final String HTML_FORMAT_OPTION = new StringBuffer("{")
														.append("'brace_style': 'collapse',")
														.append("'break_chained_methods': false,")
														.append("'indent_char':  '  ',")
														.append("'indent_scripts': 'normal',")
														.append("'indent_size': 4,")
														.append("'max_preserve_newlines': 5,")
														.append("'space_after_anon_function': true,")
														.append("'space_before_conditional': true,")
														.append("'unescape_strings': false,")
														.append("'wrap_line_length': '0'")
														.append("}").toString();

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
		String newFileName = new StringBuffer(actualFileName).append("#").append(type).append("beautify").append(extension).toString();
		File newFile = FileUtil.createFile(newFileName);
		if(newFile.exists()){
			String sourceFile = null;
			String functionName = null;
			String option = null;
			switch (type) {
			case SourceFileConstants.JS_FILE_TYPE:
				sourceFile = BEAUTIFY_JS;
				functionName = BEAUTIFY_JS_FUNCTION;
				option = JS_FORMAT_OPTION;
				break;
			case SourceFileConstants.CSS_FILE_TYPE:
				sourceFile = BEAUTIFY_CSS;
				functionName = BEAUTIFY_CSS_FUNCTION;
				option = CSS_FORMAT_OPTION;
				break;
			case SourceFileConstants.HTML_FILE_TYPE:
				sourceFile = BEAUTIFY_HTML;
				functionName = BEAUTIFY_HTML_FUNCTION;
				option = HTML_FORMAT_OPTION;
				break;

			default:
				break;
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

		String jsContent = XMLUtil.getCDATAContentFromFile(fileName,CAF_SCRIPT_TAG_NAME);

		if(jsContent == null)
			return;

		String sourceFile = null;
		String functionName = null;
		String option = null;
		if(SourceFileConstants.CAF_FILE_TYPE.equals(type)){
			sourceFile = BEAUTIFY_JS;
			functionName = BEAUTIFY_JS_FUNCTION;
			option = JS_FORMAT_OPTION;
		}
		String updatedContent = Beautify.beautify(sourceFile, jsContent,functionName, option);
		XMLUtil.writeCDATAContentIntoFile(fileName,CAF_SCRIPT_TAG_NAME,updatedContent);
	}
}