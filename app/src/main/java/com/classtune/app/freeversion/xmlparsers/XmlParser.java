package com.classtune.app.freeversion.xmlparsers;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlParser {

	

	private static XmlParser instance = null;
	private static SAXParserFactory factory;
	private static SAXParser saxParser;

	
	public XmlParser() {
		factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized XmlParser getInstance() {
		if (instance == null) {
			instance = new XmlParser();
		}
		return instance;
	}

	public static void destroyInstance() {
		instance = null;
	}

	public IndexParserHandler parseCmartIndex(String xmlData){
		IndexParserHandler handler   = new IndexParserHandler();
		try {
        	saxParser.parse(new InputSource(new StringReader(xmlData)), handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handler;
	}
	
	public CatelogParserHandler parseCmartCatelogData(String xmlData){
		CatelogParserHandler handler   = new CatelogParserHandler();
		try {
        	saxParser.parse(new InputSource(new StringReader(xmlData)), handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handler;
	}
	
	public SingleProductParserHandler parseSingleProduct(String xmlData){
		SingleProductParserHandler handler   = new SingleProductParserHandler();
		try {
        	saxParser.parse(new InputSource(new StringReader(xmlData)), handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handler;
	}
	

}
