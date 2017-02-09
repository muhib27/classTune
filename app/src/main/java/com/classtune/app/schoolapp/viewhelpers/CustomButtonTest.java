/**
 * 
 */
package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;

public class CustomButtonTest extends LinearLayout {
	
	boolean isselected;
	
	ImageView image;
	TextView title;
	Drawable iconImageNormal,iconImageTap;
	Context context;

	public CustomButtonTest(Context context) {
		this(context, null);
	}

	public CustomButtonTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initStyleButton(attrs, context);
	}

	private void initStyleButton(AttributeSet attrs, Context context) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomButtonTest);
		String titleText = a.getString(R.styleable.CustomButtonTest_buttonText);
		iconImageNormal = a.getDrawable(R.styleable.CustomButtonTest_iconImageNormal);
		iconImageTap = a.getDrawable(R.styleable.CustomButtonTest_iconImageTap);
		int textSize = a.getInt(R.styleable.CustomButtonTest_buttonTextSize, 12);
		isselected=a.getBoolean(R.styleable.CustomButtonTest_buttonSelected, false);
		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.btn_layout, this, true);

		
		image = (ImageView) getChildAt(0);

		title = (TextView) getChildAt(1);
		title.setText(titleText);
		title.setTextSize(textSize);
		setButtonSelected(isselected);
		
	}

	public void toggleButtonState()
	{
		if(isselected)
			setButtonSelected(false);
		else
			setButtonSelected(true);
	}
	
	public boolean getSelectedState()
	{
		return isselected;
	}
	
	public void setButtonSelected(boolean bol) {
		if (bol) {
			title.setTextColor(getResources().getColor(R.color.classtune_green_color));
			this.setBackgroundResource(R.drawable.white_btn);
			image.setImageDrawable(iconImageTap);
			isselected=true;
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.gray_btn);
			image.setImageDrawable(iconImageNormal);
			isselected=false;
		}
	}
	
	public void setImage(int resId) {
		image.setImageResource(resId);
	}
	
	public void setTitleColor(int resId) {
		title.setTextColor(resId);
	}
	
	public void setTitleText(String newText){
		title.setText(newText);
	}
}
