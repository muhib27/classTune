package com.classtune.app.freeversion.xmlparsers;

import android.util.Log;

import com.classtune.app.schoolapp.model.CmartCategory;
import com.classtune.app.schoolapp.model.CmartProduct;
import com.classtune.app.schoolapp.model.CmartProductPrice;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CatelogParserHandler extends DefaultHandler {
	public String parentTitle;
	public String parentId;
	public boolean hasMoreItems;
	public List<CmartCategory> subCategories = new ArrayList<CmartCategory>();
	public List<CmartProduct> products = new ArrayList<CmartProduct>();

	private Stack<String> elementStack = new Stack<String>();
	private Stack<Object> objectStack = new Stack<Object>();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		Log.e("characters : ", new String(ch, start, length));
		String value = new String(ch, start, length).trim();
		if (value.length() == 0)
			return; // ignore white space
		// ****************************Category
		// info****************************//
		if ("parent_title".equals(currentElement())
				&& "category_info".equals(goUp(2)))
			parentTitle = value;
		else if ("parent_id".equals(currentElement())
				&& "category_info".equals(goUp(2)))
			parentId = value;
		else if ("has_more_items".equals(currentElement())
				&& "category_info".equals(goUp(2)))
			hasMoreItems = "1".equals(value) ? true : false;

		// ****************************Subcategory
		// ****************************//

		else if ("label".equals(currentElement()) && "item".equals(goUp(2))) {
			CmartCategory cat = (CmartCategory) this.objectStack.peek();
			cat.setLabel((cat.getLabel() != null ? cat.getLabel() : "") + value);
		} else if ("entity_id".equals(currentElement())
				&& "items".equals(goUp(3))) {

			CmartCategory cat = (CmartCategory) this.objectStack.peek();
			cat.setEntity_id((cat.getEntity_id() != null ? cat.getEntity_id()
					: "") + value);

		} else if ("content_type".equals(currentElement())
				&& "item".equals(goUp(2))) {

			CmartCategory cat = (CmartCategory) this.objectStack.peek();
			cat.setContent_type((cat.getContent_type() != null ? cat
					.getContent_type() : "") + value);

		} else if ("icon".equals(currentElement()) && "items".equals(goUp(3))) {

			CmartCategory cat = (CmartCategory) this.objectStack.peek();
			cat.setIcon((cat.getIcon() != null ? cat.getIcon() : "") + value);
		}
		// ****************************Product
		// ****************************//
		else if ("entity_id".equals(currentElement()) && "products".equals(goUp(3))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setId((product.getId() != null ? product.getId() : "")
					+ value);
		} else if ("name".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setName((product.getName() != null ? product.getName() : "")
					+ value);
		}
		else if ("entity_type".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setEntityType((product.getEntityType() != null ? product.getEntityType() : "")
					+ value);
		} else if ("short_description".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setShortDescription((product.getShortDescription() != null ? product.getShortDescription() : "")
					+ value);
		}
		else if ("description".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setDescription((product.getDescription() != null ? product.getDescription() : "")
					+ value);
		} else if ("link".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setLink((product.getLink() != null ? product.getLink() : "")
					+ value);
		}
		else if ("icon".equals(currentElement()) && "products".equals(goUp(3))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setIcon((product.getIcon() != null ? product.getIcon() : "")
					+ value);
		} else if ("in_stock".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setInStock((product.getInStock() != null ? product.getInStock() : "")
					+ value);
		}
		else if ("is_salable".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setIsSalable((product.getIsSalable() != null ? product.getIsSalable() : "")
					+ value);
		} else if ("has_gallery".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setHasGallery((product.getHasGallery() != null ? product.getHasGallery() : "")
					+ value);
		}
		else if ("has_options".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setHasOptions((product.getHasOptions() != null ? product.getHasOptions() : "")
					+ value);
		} else if ("min_sale_qty".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setMinSaleQty((product.getMinSaleQty() != null ? product.getMinSaleQty() : "")
					+ value);
		}
		else if ("rating_summary".equals(currentElement()) && "item".equals(goUp(2))) {

			CmartProduct product = (CmartProduct) this.objectStack.peek();
			product.setRatingSummary((product.getRatingSummary() != null ? product.getRatingSummary() : "")
					+ value);
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
            this.objectStack .pop();
            
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
		if ("item".equals(qName))
		{
			if ("items".equals(goUp(2))) {
				CmartCategory subCat = new CmartCategory();
				this.objectStack.push(subCat);
				this.subCategories.add(subCat);
			} else if ("products".equals(goUp(2))) {
				CmartProduct product = new CmartProduct();
				this.objectStack.push(product);
				this.products.add(product);
			}
		}
		if("price".equals(qName))
		{
			CmartProduct product = (CmartProduct) this.objectStack.peek();
			CmartProductPrice price=new CmartProductPrice();
			int length = attributes.getLength();
			for (int i=0; i<length; i++) {
				String name = attributes.getQName(i);
				if("regular".equals(name))
					price.setRegularPrice(attributes.getValue(i));
				else if("special".equals(name))
					price.setSpecialPrice(attributes.getValue(i));
				
			}
			product.setPrice(price);
			
		}
			

	}

	private String currentElement() {
		return this.elementStack.peek();
	}

	private String goUp(int level) {
		if (this.elementStack.size() < level)
			return null;
		return this.elementStack.get(this.elementStack.size() - level);
	}

}
