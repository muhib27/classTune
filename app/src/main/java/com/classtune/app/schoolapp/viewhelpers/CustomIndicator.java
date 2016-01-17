package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.classtune.app.R;

import java.util.ArrayList;
import java.util.List;

public class CustomIndicator extends LinearLayout {
	
	private int numOfIndicators = 0;
	
	private Context context;
	
	
	private List<LinearLayout> listIndicator = new ArrayList<LinearLayout>();
	

	 public CustomIndicator(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	       
	    }

	    public CustomIndicator(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        
	    }

	    public CustomIndicator(Context context, int numOfIndicators) {
	        super(context);
	        
	        this.context = context;
	        this.numOfIndicators = numOfIndicators;
	        
	       
	    }
	    
	    
	    public void initView(Context context, int numOfIndicators) {
	        //View view = inflate(getContext(), R.layout.layout_custom_indicator, null);
	    	this.context = context;
	        initIndicator(numOfIndicators);
	        this.numOfIndicators = numOfIndicators;
	        
	    }
	    
	    
	    private void initIndicator(int numOfIndicators)
	    {
	    	for(int i=0;i<numOfIndicators;i++)
	    	{
	    		LinearLayout ib = new LinearLayout(this.context);
	    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    		params.setMargins(5, 5, 5, 5);
	    		ib.setLayoutParams(params);
	    		ib.setBackgroundColor(Color.TRANSPARENT);
	    		ib.setBackgroundResource(R.drawable.indicator_normal);
	    		addView(ib);
	    		
	    		listIndicator.add(ib);
	    	}
	    }

	    public void setSelectedIndicator(int currentPosition)
	    {
	    	for(int i =0; i<listIndicator.size(); i++)
	    	{
	    		if(i == currentPosition)
	    		{
	    			listIndicator.get(i).setBackgroundResource(R.drawable.indicator_tap);
	    		}
	    		else
	    		{
	    			listIndicator.get(i).setBackgroundResource(R.drawable.indicator_normal);
	    		}
	    	}
	    }
	    
	    

}
