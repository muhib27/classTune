package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class UninterceptableViewPager extends ViewPager {

	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	public UninterceptableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		boolean ret = super.onInterceptTouchEvent(ev);
//		return super.onInterceptTouchEvent(ev)
//				&& mGestureDetector.onTouchEvent(ev);
//		// if (ret)
//		
//		// getParent().requestDisallowInterceptTouchEvent(true);
//		// return ret;
//	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(arg0);
	}
	
	
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// // boolean ret = super.onTouchEvent(ev);
	// return super.onInterceptTouchEvent(ev) &&
	// mGestureDetector.onTouchEvent(ev);
	// // if (ret)
	// // getParent().requestDisallowInterceptTouchEvent(true);
	// // return ret;
	// }
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				Log.e("Action Down", "Aise");
				return true;
			}
			
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return super.onSingleTapConfirmed(e);
		}
	}

	
}