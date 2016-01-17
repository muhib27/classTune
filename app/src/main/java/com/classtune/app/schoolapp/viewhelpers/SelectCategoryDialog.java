package com.classtune.app.schoolapp.viewhelpers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.onGradeDialogButtonClickListener;
import com.classtune.app.schoolapp.model.MenuData;

import java.util.ArrayList;

public class SelectCategoryDialog extends Dialog implements android.view.View.OnClickListener{

	
	ArrayList<MenuData> cats;;

	private onGradeDialogButtonClickListener listener;


	public SelectCategoryDialog(Context context,onGradeDialogButtonClickListener mListener, String selectedGrades) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_select_grade);
		this.setCancelable(false);
		this.listener=mListener;





	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_done:

			
		default:
			break;
		}
	}

	

}
