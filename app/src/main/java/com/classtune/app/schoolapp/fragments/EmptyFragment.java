/**
 * 
 */
package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classtune.app.R;

/**
 *
 */
public class EmptyFragment extends UserVisibleHintFragment{
	
	private static final String TAG="Empty Fragment";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		return inflater.inflate(R.layout.fragment_academic_calendar_exam, container, false);
	}
	
	

	@Override
	protected void onVisible() {
		Log.e(TAG, "Visible hoise ar Content loaded");
	}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub
		
	}
}