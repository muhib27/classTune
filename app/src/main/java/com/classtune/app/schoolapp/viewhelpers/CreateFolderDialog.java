package com.classtune.app.schoolapp.viewhelpers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.onCreateFolderDialogButtonClickListener;

public class CreateFolderDialog extends Dialog implements android.view.View.OnClickListener {

	Button btnCreate, btnCancel;
	TextView tvError;

	private onCreateFolderDialogButtonClickListener listener;

	String folderName = "";

	public CreateFolderDialog(Context context,onCreateFolderDialogButtonClickListener mListener) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_folder_name);
		this.setCancelable(false);
		this.listener=mListener;

		btnCreate = (Button) findViewById(R.id.btn_create);
		btnCreate.setOnClickListener(this);

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		tvError = (TextView) findViewById(R.id.tv_error);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create:

			EditText etFolderName = (EditText) findViewById(R.id.editText_foldername);

			folderName = etFolderName.getText().toString();
			
			if (folderName.equalsIgnoreCase("")) {
				tvError.setVisibility(View.VISIBLE);
			} else {
				listener.onCreateBtnClick(this, folderName, tvError);
			}
			
			break;

		case R.id.btn_cancel:
			listener.onCancelBtnClick(this);
			break;

		default:
			break;
		}
	}
}
