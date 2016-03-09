package com.classtune.app.schoolapp.viewhelpers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.classtune.app.schoolapp.LoginActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.utils.UserHelper;

/**
 * Created by Tasvir on 3/31/2015.
 */
public class PopupDialogChangePassword extends DialogFragment implements UserAuthListener{

    UserHelper userHelper;
    UIHelper uiHelper;
    private TextView passwordEmainTextView;
    private Button saveButton;
    private EditText etCurrentPass;
    private EditText etNewPass;
    private EditText etRePass;
    private Button skipButton;
    private ImageButton closeButton;

    public interface PassChangeCallBack{
        public void onPassChangeDialogDismiss();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userHelper = new UserHelper(this, getActivity());
        uiHelper = new UIHelper(getActivity());//((LoginActivity)getActivity()).uiHelper;
        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.popup_layout_change_pass, container,
                false);
        setUpEditProfileView(view);
        return view;
    }

    @Override
    public void onAuthenticationStart() {
        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
        }
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));

    }

    @Override
    public void onAuthenticationSuccessful() {
        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
        }
        uiHelper.dismissLoadingDialog();
     }

    @Override
    public void onAuthenticationFailed(String msg) {

       // if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
        //}
        uiHelper.showMessage(msg);
    }

    @Override
    public void onPaswordChanged() {
        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
        }
        uiHelper.showMessage(getString(R.string.java_accountsettingsactivity_password_changes_success));
        ((LoginActivity)getActivity()).onPassChangeDialogDismiss();
        PopupDialogChangePassword.this.dismiss();
    }
    private void setUpEditProfileView(View view) {
        passwordEmainTextView = (TextView) view.findViewById(R.id.tv_pass_email);
        //passwordEmainTextView.setText(userHelper.getUser().getEmail());
        closeButton = (ImageButton) view.findViewById(R.id.popup_btn_close);
        saveButton = (Button) view.findViewById(R.id.save_btn);
        skipButton = (Button) view.findViewById(R.id.btn_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).onPassChangeDialogDismiss();
                PopupDialogChangePassword.this.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).onPassChangeDialogDismiss();
                PopupDialogChangePassword.this.dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etCurrentPass.getText().toString().trim())) {
                    uiHelper.showErrorDialog(getActivity().getString(R.string.java_popupdialogchangepassword_password_cannot_empty));
                } else if (TextUtils.isEmpty(etNewPass.getText().toString().trim())) {
                    uiHelper.showErrorDialog(getString(R.string.java_popupdialogchangepassword_password_cannot_empty));
                } else if (TextUtils.isEmpty(etRePass.getText().toString().trim())) {
                    uiHelper.showErrorDialog(getString(R.string.java_popupdialogchangepassword_password_cannot_empty));
                } else {
                    if (!etNewPass.getText().toString().trim()
                            .equals(etRePass.getText().toString().trim())) {
                        uiHelper.showErrorDialog(getString(R.string.java_classtune_registration_password_didnt_match));
                    } else {
                        userHelper.updatePassword(userHelper.getUser(), etNewPass
                                .getText().toString(), etCurrentPass.getText()
                                .toString().trim());
                    }
                }
            }
        });
        this.etCurrentPass = (EditText) view.findViewById(R.id.et_current_pass);
        //this.imgEditCurrentPass = (ImageView)findViewById(R.id.imgEditCurrentPass);

        this.etNewPass = (EditText) view.findViewById(R.id.et_new_pass);
        //this.imgEditNewPass = (ImageView)findViewById(R.id.imgEditNewPass);

        this.etRePass = (EditText) view.findViewById(R.id.et_re_pass);
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
}
