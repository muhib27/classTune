package com.classtune.app.freeversion.xmlparsers;

import android.util.Log;

import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.CmartProduct;
import com.classtune.app.schoolapp.model.CmartProductAttribute;
import com.classtune.app.schoolapp.model.CmartProductPrice;
import com.classtune.app.schoolapp.model.PickerType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SingleProductParserHandler extends DefaultHandler {
	public CmartProduct product;

	private CmartProductAttribute keyAttr;
	private List<BaseType> valueAttrs=new ArrayList<BaseType>();
	private HashMap<BaseType, List<BaseType>> attributes=new HashMap<BaseType, List<BaseType>>();
	private Stack<String> elementStack = new Stack<String>();
	private Stack<String> superAttributesStack = new Stack<String>();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String value = new String(ch, start, length).trim();
		if (value.length() == 0)
			return; // ignore white space
		// ****************************Product
		// ****************************//
		if ("entity_id".equals(currentElement())) {
			product.setId((product.getId() != null ? product.getId() : "")
					+ value);
		} else if ("name".equals(currentElement())) {
			product.setName((product.getName() != null ? product.getName() : "")
					+ value);
		} else if ("entity_type".equals(currentElement())) {
			product.setEntityType((product.getEntityType() != null ? product
					.getEntityType() : "") + value);
		} else if ("short_description".equals(currentElement())) {
			product.setShortDescription((product.getShortDescription() != null ? product
					.getShortDescription() : "")
					+ value);
		} else if ("description".equals(currentElement())) {
			product.setDescription((product.getDescription() != null ? product
					.getDescription() : "") + value);
		} else if ("link".equals(currentElement())) {
			product.setLink((product.getLink() != null ? product.getLink() : "")
					+ value);
		} else if ("icon".equals(currentElement())) {
			product.setIcon((product.getIcon() != null ? product.getIcon() : "")
					+ value);
		} else if ("in_stock".equals(currentElement())) {
			product.setInStock((product.getInStock() != null ? product
					.getInStock() : "") + value);
		} else if ("is_salable".equals(currentElement())) {
			product.setIsSalable((product.getIsSalable() != null ? product
					.getIsSalable() : "") + value);
		} else if ("has_gallery".equals(currentElement())) {
			product.setHasGallery((product.getHasGallery() != null ? product
					.getHasGallery() : "") + value);
		} else if ("has_options".equals(currentElement())) {
			product.setHasOptions((product.getHasOptions() != null ? product
					.getHasOptions() : "") + value);
		} else if ("min_sale_qty".equals(currentElement())) {
			product.setMinSaleQty((product.getMinSaleQty() != null ? product
					.getMinSaleQty() : "") + value);
		} else if ("rating_summary".equals(currentElement())) {
			product.setRatingSummary((product.getRatingSummary() != null ? product
					.getRatingSummary() : "")
					+ value);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		//this.elementStack.pop();
		product.setAttributes(attributes);
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		this.elementStack.pop();
		if("relation".equals(qName))
		{
			superAttributesStack.pop();
			attributes.put(keyAttr, valueAttrs);
			//valueAttrs.clear();
		}
		if("option".equals(qName))
		{
			superAttributesStack.pop();
		}
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		Log.e("start element    : ", qName);
		this.elementStack.push(qName);
		if ("mainproduct".equals(qName)) {
			product = new CmartProduct();

		}
		
		int length = attributes.getLength();
		if ("price".equals(qName)) {
			CmartProductPrice price = new CmartProductPrice();
			
			for (int i = 0; i < length; i++) {
				String name = attributes.getQName(i);
				if ("regular".equals(name))
					price.setRegularPrice(attributes.getValue(i));
				else if ("special".equals(name))
					price.setSpecialPrice(attributes.getValue(i));

			}
			product.setPrice(price);

		}
		
		if("option".equals(qName))
		{
			for(int i=0;i<length;i++)
			{
				String name = attributes.getQName(i);
				if("code".equals(name))
				{
					superAttributesStack.push(getCodeFromString(attributes.getValue(i)));
				}
			}
		}
		
		if("value".equals(qName) && "option".equals(goUp(2)))
		{
			keyAttr=new CmartProductAttribute();
			for(int i=0;i<length;i++)
			{
				String name = attributes.getQName(i);
				if("code".equals(name))
					keyAttr.setCode(attributes.getValue(i));
				if("label".equals(name))
					keyAttr.setLabel(attributes.getValue(i));
				keyAttr.setSuperAttributeId(superAttributesStack.peek());
				keyAttr.setType(PickerType.CMART_ATTR_COLOR);
			}
			
		}
		
		if("relation".equals(qName))
		{
			valueAttrs=new ArrayList<BaseType>();
			for(int i=0;i<length;i++)
			{
				String name = attributes.getQName(i);
				if("to".equals(name))
				{
					superAttributesStack.push(getCodeFromString(attributes.getValue(i)));
				}
			}
		}
		
		if("value".equals(qName) && "relation".equals(goUp(2)))
		{
			CmartProductAttribute valueAttr=new CmartProductAttribute();
			for(int i=0;i<length;i++)
			{
				String name = attributes.getQName(i);
				if("code".equals(name))
					valueAttr.setCode(attributes.getValue(i));
				if("label".equals(name))
					valueAttr.setLabel(attributes.getValue(i));
				valueAttr.setSuperAttributeId(superAttributesStack.peek());
				valueAttr.setType(PickerType.CMART_ATTR_SIZE);
			}
			valueAttrs.add(valueAttr);
			
		}

		
	}

	private String getCodeFromString(String fullString)
	{
		return fullString.split("[\\[|\\]]")[1];
	}
	
	private String currentElement() {
		return this.elementStack.peek();
	}

	private String goUp(int level) {
		if (this.elementStack.size() < level)
			return null;
		String temp=this.elementStack.get(this.elementStack.size() - level);
		return temp;
	}

}
