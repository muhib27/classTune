package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Tasvir on 12/30/2015.
 */
public class ForgetPasswordActivity extends ChildContainerActivity implements View.OnClickListener{

    private InputMethodManager im;
    private EditText etUsername;
    //private ImageView imgEditCurrentPass;
    private ProgressBar pb;
    //private ImageView imgEditNewPass;

    private Button saveButton;
    private UserHelper userHelper;
    private UIHelper uiHelper;
    private EditText etEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        userHelper=new UserHelper(this, ForgetPasswordActivity.this);
        uiHelper=new UIHelper(ForgetPasswordActivity.this);
        setUpEditProfileView();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
    }

    private void setUpEditProfileView() {
        saveButton = (Button)findViewById(R.id.activity_forget_password_save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
                    uiHelper.showErrorDialog(getString(R.string.java_forgetpasswordactivity_username_cannot_empty));
                } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    uiHelper.showErrorDialog(getString(R.string.java_forgetpasswordactivity_email_cannot_empty));
                } else {
                    /*userHelper.updatePassword(userHelper.getUser(), etNewPass
                                .getText().toString(), etUsername.getText()
                                .toString().trim());*/
                    sendEmail(userHelper.getUser(), etUsername.getText().toString(), etEmail.getText().toString());
                }
            }
        });
        this.etUsername = (EditText)findViewById(R.id.activity_forget_password_username);
        //this.imgEditCurrentPass = (ImageView)findViewById(R.id.imgEditCurrentPass);
        this.etEmail = (EditText)findViewById(R.id.activity_forget_password_email);
    }

    public void sendEmail(User user, String username,
                               String email) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("email", email);
        AppRestClient.post(URLHelper.URL_FORGET_PASSWORD, params,
                new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int i, String s) {
                        super.onSuccess(i, s);
                        Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                                s);
                        if (wrapper.getStatus().getCode() == 200) {
                            Toast.makeText(ForgetPasswordActivity.this, R.string.java_forgetpasswordactivity_check_email,Toast.LENGTH_LONG).show();
                            finish();
                        } else if(wrapper.getStatus().getCode() == 404) {
                            Toast.makeText(ForgetPasswordActivity.this, R.string.java_forgetpasswordactivity_email_incorrect,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
