/**
 * 
 */
package com.classtune.app.schoolapp.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.classtune.app.R;

import roboguice.activity.RoboFragmentActivity;

/**
 * @author Amit
 *
 */
public class SimpleDialogFragment extends DialogFragment {
	
	private Context context;
	private String title;
	private String message;
	private int resId;
	private Class<RoboFragmentActivity> targetContext;
	private Intent intent;
	
	public SimpleDialogFragment() {
		// TODO Auto-generated constructor stub
		context = getActivity();
	}
	
	public void setParams(String Title, String message, int resId, Class<RoboFragmentActivity> targetContext, Intent intent) {
		this.title = Title;
		this.message = Html.fromHtml(message).toString();;
		this.resId = resId;
		this.targetContext =targetContext;
		this.intent = intent;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(this.resId, container);
        getDialog().setTitle(this.title);
        
        switch (resId) {
		case R.layout.dialog_one_button:
			processOneButtonDialog(view);
			break;

		default:
			break;
		}

        return view;
	}

	private void processOneButtonDialog(View view) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) view.findViewById(R.id.tv_message);
		tv.setText(this.message);
		
		Button buttonOk = (Button) view.findViewById(R.id.btn_ok);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (targetContext != null) {
					SimpleDialogFragment.this.dismiss();
					startActivity(new Intent(context, targetContext));
				} else {
					SimpleDialogFragment.this.dismiss();
					
					if (intent != null) {
						startActivity(intent);
					}
				}
			}
		});
	}
}
