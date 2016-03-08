/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogChangePassword;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

public class AccountSettingsActivity extends ChildContainerActivity implements OnClickListener,UserAuthListener,PopupDialogChangePassword.PassChangeCallBack{

	private InputMethodManager im;
	private EditText etCurrentPass;
	//private ImageView imgEditCurrentPass;
	private ProgressBar pb;
	private EditText etNewPass;
	//private ImageView imgEditNewPass;
	private EditText etRePass;
	//private ImageView imgEditRePass;
	private ImageButton saveButton;
	private TextView passwordEmainTextView;
	private UserHelper userHelper;
	private UIHelper uiHelper;

	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub
		super.onAuthenticationStart();
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
	}
	@Override
	public void onAuthenticationSuccessful() {
		// TODO Auto-generated method stub
		super.onAuthenticationSuccessful();
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		super.onAuthenticationFailed(msg);
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		uiHelper.showMessage(msg);
	}

	@Override
	public void onPaswordChanged() {
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		uiHelper.showMessage(getString(R.string.java_accountsettingsactivity_password_changes_success));
		finish();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_settings_layout2);
		userHelper=new UserHelper(this, AccountSettingsActivity.this);
		uiHelper=new UIHelper(AccountSettingsActivity.this);
		setUpEditProfileView();
		
	}

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
    }

    private void setUpEditProfileView() {
		passwordEmainTextView = (TextView) findViewById(R.id.tv_pass_email);
		passwordEmainTextView.setText(userHelper.getUser().getEmail());
		saveButton = (ImageButton)findViewById(R.id.save_btn);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(etCurrentPass.getText().toString().trim())) {
					uiHelper.showErrorDialog(getString(R.string.java_accountsettingsactivity_password_cannot_empty));
				} else if (TextUtils.isEmpty(etNewPass.getText().toString().trim())) {
					uiHelper.showErrorDialog(getString(R.string.java_accountsettingsactivity_password_cannot_empty));
				} else if (TextUtils.isEmpty(etRePass.getText().toString().trim())) {
					uiHelper.showErrorDialog(getString(R.string.java_accountsettingsactivity_password_cannot_empty));
				} else {
					if (!etNewPass.getText().toString().trim()
							.equals(etRePass.getText().toString().trim())) {
						uiHelper.showErrorDialog(getString(R.string.java_accountsettingsactivity_password_didnt_match));
					} else {
						userHelper.updatePassword(userHelper.getUser(), etNewPass
								.getText().toString(), etCurrentPass.getText()
								.toString().trim());
					}
				}
			}
		});
		this.etCurrentPass = (EditText)findViewById(R.id.et_current_pass);
		//this.imgEditCurrentPass = (ImageView)findViewById(R.id.imgEditCurrentPass);

		this.etNewPass = (EditText)findViewById(R.id.et_new_pass);
		//this.imgEditNewPass = (ImageView)findViewById(R.id.imgEditNewPass);

		this.etRePass = (EditText)findViewById(R.id.et_re_pass);
		//this.imgEditRePass = (ImageView)findViewById(R.id.imgEditRePass);

		/*im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(etCurrentPass.getWindowToken(), 0);
		im.hideSoftInputFromWindow(etNewPass.getWindowToken(), 0);
		im.hideSoftInputFromWindow(etRePass.getWindowToken(), 0);

		if (Build.VERSION.SDK_INT >= 11) {
			etCurrentPass.setRawInputType(InputType.TYPE_CLASS_TEXT);
			etCurrentPass.setTextIsSelectable(true);

			etNewPass.setRawInputType(InputType.TYPE_CLASS_TEXT);
			etNewPass.setTextIsSelectable(true);

			etRePass.setRawInputType(InputType.TYPE_CLASS_TEXT);
			etRePass.setTextIsSelectable(true);

		} else {
			etCurrentPass.setRawInputType(InputType.TYPE_NULL);
			etCurrentPass.setFocusable(true);

			etNewPass.setRawInputType(InputType.TYPE_NULL);
			etNewPass.setFocusable(true);

			etRePass.setRawInputType(InputType.TYPE_NULL);
			etRePass.setFocusable(true);
		}

		etCurrentPass.setFocusable(false);
		etNewPass.setFocusable(false);
		etRePass.setFocusable(false);*/

		//showSoftKeyboard(im, etCurrentPass, imgEditCurrentPass);
		//showSoftKeyboard(im, etNewPass, imgEditNewPass);
		//showSoftKeyboard(im, etRePass, imgEditRePass);

		etRePass.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// doSomething();

				if (etRePass.getText().toString().length() >= etNewPass
						.getText().toString().length()
						&& !etRePass
								.getText()
								.toString()
								.equalsIgnoreCase(
										etNewPass.getText().toString()))
					etRePass.setError(getString(R.string.java_accountsettingsaactivity_enter_correct_password));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// doSomething();
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				// doSomething();
				etRePass.setError(null);

			}

		});
	}

	private void showSoftKeyboard(final InputMethodManager im,
			final EditText et, ImageView editIcon) {
		editIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				im.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		et.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				et.setFocusableInTouchMode(true);
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);
				return false;
			}
		});
	}


    @Override
    public void onPassChangeDialogDismiss() {

    }
}