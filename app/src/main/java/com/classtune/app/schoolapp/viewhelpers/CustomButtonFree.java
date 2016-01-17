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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;

/**
 * @author Amit
 * 
 */
public class CustomButtonFree extends LinearLayout {
	
	ImageView image;
	TextView title;
	
	boolean btnSelected = false;

	public CustomButtonFree(Context context) {
		this(context, null);
	}

	public CustomButtonFree(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initStyleButton(attrs, context);
	}

	private void initStyleButton(AttributeSet attrs, Context context) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomButtonFree);
		String titleText = a.getString(R.styleable.CustomButtonFree_iconTextFree);
		Drawable iconImage = a.getDrawable(R.styleable.CustomButtonFree_iconImageFree);
		
		a.recycle();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.btn_layout_free, this, true);

		
		LinearLayout l = (LinearLayout)getChildAt(0);
		image = (ImageView) l.getChildAt(1);
		image.setImageDrawable(iconImage);

		title = (TextView) l.getChildAt(0);
		title.setText(titleText);
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
}
