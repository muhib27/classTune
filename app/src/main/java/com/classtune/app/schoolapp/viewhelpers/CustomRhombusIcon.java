/**
 * 
 */
package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.classtune.app.R;

public class CustomRhombusIcon extends FrameLayout {
	
	
	ImageView image;
	Drawable iconImage;
	Context context;

	public CustomRhombusIcon(Context context) {
		this(context, null);
	}

	public CustomRhombusIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initStyleButton(attrs, context);
	}

	private void initStyleButton(AttributeSet attrs, Context context) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.CustomRhombus);
		iconImage = a.getDrawable(R.styleable.CustomRhombus_iconImageRhombus);
		a.recycle();
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.rhombus_icon, this, true);

		
		image = (ImageView) findViewById(R.id.icon);
		image.setImageDrawable(iconImage);
		
	}
	
	public void setIconImage(int id)
	{
		image.setImageResource(id);
	}

	
}
