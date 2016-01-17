package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;

public class CustomTabButton extends LinearLayout {

	ImageView image;
	TextView title;
	

	public CustomTabButton(Context context) {
		this(context, null);
	}

	public CustomTabButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initStyleButton(attrs, context);
	}

	private void initStyleButton(AttributeSet attrs, Context context) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomTabButton);
		String titleText = a.getString(R.styleable.CustomTabButton_iconTextTab);
		Drawable iconImage = a.getDrawable(R.styleable.CustomTabButton_iconImageTab);
		
		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_btn_layout, this, true);

		
		image = (ImageView) getChildAt(0);
		image.setImageDrawable(iconImage);

		title = (TextView) getChildAt(1);
		title.setText(titleText);

		
	}

	public void setButtonSelected(boolean bol, int colorId, int imageId) {
		if (bol) {
			title.setTextColor(colorId);
			this.setBackgroundResource(R.drawable.tab_selected_btn);
			
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.tab_general_btn);
			
		}
		
		image.setImageResource(imageId);
	}
	
	
	
	public void setButtonSelected(boolean bol, int colorId) {
		if (bol) {
			title.setTextColor(colorId);
			this.setBackgroundResource(R.drawable.tab_selected_btn);
			
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.tab_general_btn);
			
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
	
	public void setTitleTextSize(float size){
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}
}
