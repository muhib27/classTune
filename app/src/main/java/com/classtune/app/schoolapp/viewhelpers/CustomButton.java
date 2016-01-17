/**
 * 
 */
package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;

/**
 * @author Amit
 * 
 */
public class CustomButton extends LinearLayout {
	
	ImageView image;
	TextView title;
	
	boolean btnSelected = false;

	public CustomButton(Context context) {
		this(context, null);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initStyleButton(attrs, context);
	}

	private void initStyleButton(AttributeSet attrs, Context context) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomButton);
		String titleText = a.getString(R.styleable.CustomButton_iconText);
		Drawable iconImage = a.getDrawable(R.styleable.CustomButton_iconImage);
		int textSize = a.getInt(R.styleable.CustomButton_textSize, 12);
		int color = a.getColor(R.styleable.CustomButton_textColor, R.color.gray_1);
		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.btn_layout, this, true);

		
		image = (ImageView) getChildAt(0);
		image.setImageDrawable(iconImage);

		title = (TextView) getChildAt(1);
		title.setText(titleText);
		title.setTextSize(textSize);
		title.setTextColor(color);
		
	}
	
	
	public void setTextSize(int textSize)
	{
		title.setTextSize(textSize);
	}
	
	

	public void setButtonSelected(boolean bol) {
		if (bol) {
			title.setTextColor(Color.BLACK);
			this.setBackgroundResource(R.drawable.white_btn);
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.gray_btn);
		}
	}
	
	public void setReportButtonSelected(boolean bol) {
		if (bol) {
			title.setTextColor(Color.BLACK);
			this.setBackgroundResource(R.drawable.white_btn);
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.gray_btn);
		}
	}
	
	public void setButtonBGSelected(boolean bol) {
		if (bol) {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.color.black);
		} else {
			title.setTextColor(getResources().getColor(R.color.gray_1));
			this.setBackgroundResource(R.drawable.general_btn);
		}
		
		setBtnSelected(bol);
	}
	
	public void setImageGone(){
		image.setVisibility(View.GONE);
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
	
	
	public void setBtnSelected(boolean btnSelected) {
		this.btnSelected = btnSelected;
	}
	public boolean isBtnSelected() {
		return btnSelected;
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
}
