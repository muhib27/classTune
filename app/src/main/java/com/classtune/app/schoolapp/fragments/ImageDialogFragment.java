package com.classtune.app.schoolapp.fragments;

/*This Dialog fragment is for selecting the image source*/

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.ImageCaptureSourceListener;

public class ImageDialogFragment extends DialogFragment {

	
	
private ImageCaptureSourceListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (ImageCaptureSourceListener) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement AuthenticationListener");
	    }
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	}
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 final View view = inflater.inflate(R.layout.add_attachment_dialog_layout, container, false);
	        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        
	        ((Button) view.findViewById(R.id.camera_btn)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dismiss();
					listener.onCameraBtnClicked();
				}
			});
	        ((Button) view.findViewById(R.id.gallery_btn)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dismiss();
					listener.onGalleryBtnClicked();
				}
			});
	        
	        ((Button) view.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dismiss();
					//listener.onGalleryBtnClicked();
				}
			});
	        
	        
	        return view;
	        
	 }



	    public static ImageDialogFragment newInstance() {
	    	ImageDialogFragment frag = new ImageDialogFragment();
	    	return frag;
	    }
	    
	   

	
}
