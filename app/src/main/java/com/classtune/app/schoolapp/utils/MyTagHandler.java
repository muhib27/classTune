/**
 * 
 */
package com.classtune.app.schoolapp.utils;

import android.text.Editable;
import android.text.Html.TagHandler;

import org.xml.sax.XMLReader;

/**
 * @author Amit
 *
 */
public class MyTagHandler implements TagHandler {

	boolean first= true;
	String parent=null;
	int index=1;

	/* (non-Javadoc)
	 * @see android.text.Html.TagHandler#handleTag(boolean, java.lang.String, android.text.Editable, org.xml.sax.XMLReader)
	 */
	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		// TODO Auto-generated method stub
		if(tag.equals("ul")) parent="ul";
		else if(tag.equals("ol")) parent="ol";
		if(tag.equals("li")){
			if(parent.equals("ul")){
				if(first){
					output.append("\n\t  ");
					first= false;
				}else{
					first = true;
				}
			}
			else{
				if(first){
					output.append("\n\t"+index+". ");
					first= false;
					index++;
				}else{
					first = true;
				}
			}   
		}
	}

}
