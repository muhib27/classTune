package com.classtune.app.freeversion.xmlparsers;

import android.util.Log;

import com.classtune.app.schoolapp.model.CmartCategory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class IndexParserHandler extends DefaultHandler {
	public List<CmartCategory> categories=new ArrayList<CmartCategory>();
	private Stack<String> elementStack = new Stack<String>();
	private Stack<Object> objectStack  = new Stack<Object>();
	
	private String homeBannerUrl="";
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		Log.e("characters : ",
				new String(ch, start, length));
		String value = new String(ch, start, length).trim();
        if(value.length() == 0) return; // ignore white space

        if("label".equals(currentElement())){
            CmartCategory cat = (CmartCategory) this.objectStack.peek();
            cat.setLabel((cat.getLabel() != null ?
            		cat.getLabel()  : "") + value);
        } else if("entity_id"  .equals(currentElement())){

        	CmartCategory cat = (CmartCategory) this.objectStack.peek();
        	cat.setEntity_id((cat.getEntity_id() != null ?
            		cat.getEntity_id()  : "") + value);

        } 
        else if("content_type"  .equals(currentElement())){

        	CmartCategory cat = (CmartCategory) this.objectStack.peek();
        	cat.setContent_type((cat.getContent_type() != null ?
            		cat.getContent_type()  : "") + value);

        } 
        else if("icon"  .equals(currentElement())){

        	CmartCategory cat = (CmartCategory) this.objectStack.peek();
        	cat.setIcon((cat.getIcon() != null ?
            		cat.getIcon()  : "") + value);
        } 
        
        else if("home_banner"  .equals(currentElement())){
        	homeBannerUrl=value;
        }
        
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		Log.e("end document   : ", "");
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		Log.e("end element    : ", qName);
		
		
		this.elementStack.pop();

        if("item".equals(qName)){
            Object object = this.objectStack .pop();
            
        }
		
	}
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		Log.e("start document   : ", "");
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		Log.e("start element    : ", qName);
		
		this.elementStack.push(qName);

        if("item".equals(qName)){
            CmartCategory cat = new CmartCategory();
            this.objectStack.push(cat);
            this.categories.add(cat);
        } 
		
		for(int i=0;i<attributes.getLength();i++)
		{
			Log.e(attributes.getQName(i), attributes.getValue(i));
		}
	}
	
	 private String currentElement() {
	        return this.elementStack.peek();
	    }

	    private String currentElementParent() {
	        if(this.elementStack.size() < 2) return null;
	        return this.elementStack.get(this.elementStack.size()-2);
	    }
}
