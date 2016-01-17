package com.classtune.app.schoolapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.CompleteProfileActivityContainer;
import com.classtune.app.freeversion.ForgetPasswordActivity;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog.UserTypeListener;
import com.classtune.app.schoolapp.utils.SPKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogChangePassword;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

public class LoginActivity extends SocialBaseActivity implements
        OnClickListener, UserTypeListener, PopupDialogChangePassword.PassChangeCallBack {

    EditText etUserName;
    EditText etPassword;
    Button btnLogin;

    private final int DIALOG_FRAGMENT = 101;
    SchoolApp app;
    boolean isFirstTime;
    public UIHelper uiHelper;
    public UserHelper userHelper;
    public String username = "", password = "";
    public static int REQUEST_COMPLETE_PROFILE = 567;

    private String fromAssessment = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 125;
    private TextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userHelper = new UserHelper(this, LoginActivity.this);
        uiHelper = new UIHelper(LoginActivity.this);
        etUserName = (EditText) findViewById(R.id.et_username);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(this);
        //etUserName.setText("ovi@gmail.com");
        etPassword = (EditText) findViewById(R.id.et_password);
        //etPassword.setText("123456");
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        app = (SchoolApp) getApplicationContext();
        app.setupUI(findViewById(R.id.layout_parent), this);

        if (getIntent() != null && getIntent().getExtras() != null)
            fromAssessment = getIntent().getExtras().getString("assessment_score");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                validateFieldAndCallLogIn();
                break;
            case R.id.tv_forget_password:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void validateFieldAndCallLogIn() {
        username = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {

            etUserName.setError("Please enter UserName!!");
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter Password!!");
        } else {
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                userHelper.doLogIn(username, password);
            }
        }

    }

    @Override
    public void onAuthenticationStart() {
        uiHelper.showLoadingDialog(getString(R.string.loading_text));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    userHelper.doLogIn(username, password);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onAuthenticationSuccessful() {

        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
        }
        if (UserHelper.isRegistered()) {
            if (UserHelper.isLoggedIn()) {

                if (fromAssessment != null && fromAssessment.length() > 0) {
                    //Intent intent = new Intent(LoginActivity.this, AssesmentActivity.class);
                    finish();
                    return;
                }

                switch (UserHelper.getUserAccessType()) {
                    case FREE:
                        Intent intent = new Intent(LoginActivity.this,
                                HomePageFreeVersion.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                    case PAID:
                   /* if ( UserHelper.isFirstLogin() ){
                        PopupDialogChangePassword picker = new PopupDialogChangePassword();
                        picker.show(getSupportFragmentManager(), null);
                    } else {*/
                        if (userHelper.getUser().getType() != UserTypeEnum.PARENTS) {
                            doPaidNavigation();
                        } else {
                            startActivityForResult(new Intent(this,
                                            ChildSelectionActivity.class),
                                    SchoolApp.REQUEST_CODE_CHILD_SELECTION);
                        }
                        //}
                        break;

                    default:
                        break;
                }

            } else {
                finish();
                Intent intent = new Intent(LoginActivity.this,
                        CompleteProfileActivityContainer.class);
                intent.putExtra(SPKeyHelper.USER_TYPE, userHelper.getUser()
                        .getType().ordinal());
                intent.putExtra("FIRST_TIME", true);
                startActivity(intent);

            }
        } else {
            Log.e("TypeSelection!", "GOOOOOOOOOOOOOOOO");
            UserTypeSelectionDialog dialogFrag = UserTypeSelectionDialog
                    .newInstance();
            dialogFrag.show(getSupportFragmentManager().beginTransaction(),
                    "dialog");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_COMPLETE_PROFILE) {
            doPaidNavigation();
        }
        if (requestCode == SchoolApp.REQUEST_CODE_CHILD_SELECTION) {
            doPaidNavigation();
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    private void gotoCompleteProfile() {
        Intent i = new Intent(LoginActivity.this,
                CompleteProfileActivityContainer.class);
        i.putExtra(SPKeyHelper.USER_TYPE, userHelper
                .getUser().getType().ordinal());
        i.putExtra("first_login", true);
        startActivityForResult(i, REQUEST_COMPLETE_PROFILE);
    }

    private void doPaidNavigation() {
        switch (userHelper.getUser().getType()) {

            case PARENTS:
                if (userHelper.getUser().getChildren() == null) {
                    Log.e("Userhelper", "null");
                }
                if (userHelper.getUser().getChildren().size() > 0) {
                    Intent paidIntent = new Intent(LoginActivity.this,
                            HomePageFreeVersion.class);
                    paidIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(paidIntent);
                    finish();
                }
                break;
            default:

                /*if(userHelper.getUserAccessType()== UserHelper.UserAccessType.PAID ){//&& UserHelper.isFirstLogin()
                    PopupDialogChangePassword picker = new PopupDialogChangePassword();
                    picker.show(getSupportFragmentManager(), null);
                }*/
                Intent paidIntent = new Intent(LoginActivity.this,
                        HomePageFreeVersion.class);
                paidIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(paidIntent);
                finish();
                break;
        }
    }

    @Override
    public void onAuthenticationFailed(String msg) {
        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();
            uiHelper.showMessage(msg);
        }
    }

    @Override
    public void onTypeSelected(int ordinal) {

        Log.e("Type", UserTypeEnum.values()[ordinal].toString());
        userHelper.updateUserWithType(ordinal);
    }

    @Override
    public void onPaswordChanged() {

    }

    @Override
    public void onPassChangeDialogDismiss() {
        gotoCompleteProfile();
        uiHelper.dismissLoadingDialog();
    }
}
