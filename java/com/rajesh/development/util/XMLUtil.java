package com.rajesh.development.util;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {

	public static Document readXMLFile(File file) {

		try {	    
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new FileInputStream(file));			
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			throw new RuntimeException("Error occured while reading xmlfile");
		}
	}

	public static Node getNode(Document doc, String tagName){
		NodeList scriptNodes = doc.getElementsByTagName(tagName);		
		return scriptNodes.item(0);
	}

	public static String getCDATAContent(Node node){
		NodeList nodeList = node.getChildNodes();
		if (nodeList != null) {
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node child = nodeList.item(i);
		        if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
		        	CDATASection cdata = (CDATASection)child;
		        	return cdata.getData();
		        }
		    }
		}
		return null;
	}
	
	public static void setCDATAContent(Node node, String value){
		NodeList nodeList = node.getChildNodes();
		if (nodeList != null) {
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node child = nodeList.item(i);
		        if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
		        	CDATASection cdata = (CDATASection)child;
		        	cdata.setData(value);
		        }
		    }
		}
	}
	
	public static String getCDATAContentFromFile(File file,String tagName){
		Document doc = readXMLFile(file);
		Node node = getNode(doc, tagName);
		return getCDATAContent(node);
	}

	public static void writeCDATAContentIntoFile(File file, String tagName,	String updatedContent) {
		Document doc = readXMLFile(file);
		Node node = getNode(doc, tagName);
		setCDATAContent(node, updatedContent);
		writeContentIntoXMLFile(doc,file);
	}
	
	public static void writeContentIntoXMLFile(Document doc,File file){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file.getAbsolutePath());
			transformer.transform(source, result);
		}catch(Exception e){
			throw new RuntimeException("Error occured while writing content into XML file");
		}
		
	}

}

