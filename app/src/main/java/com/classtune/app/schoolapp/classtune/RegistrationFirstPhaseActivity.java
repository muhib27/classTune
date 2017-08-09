package com.classtune.app.schoolapp.classtune;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BLACK HAT on 08-Nov-15.
 */
public class RegistrationFirstPhaseActivity extends Activity {


    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtRetypePassword;
    private EditText txtSchoolCode;
    private ImageButton btnNext;

    private  UIHelper uiHelper;

    private int ordinal = -1;

    private TextWatcher txtWatcher = null;

    private String schoolId = "";
    private ActionBar actionBar;

    private Button btnCreateLower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_registration_firstphase2);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            ordinal = extras.getInt(AppConstant.USER_TYPE_CLASSTUNE);
        }

        uiHelper = new UIHelper(RegistrationFirstPhaseActivity.this);

        initView();
        setUpActionBar();
        initAction();




        Log.e("ORDINAL", "is: " + ordinal);

    }

    private void setUpActionBar() {
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#219439")));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        View cView = getLayoutInflater().inflate(R.layout.actionbar_view_classtune, null);

        btnNext = (ImageButton) cView.findViewById(R.id.btnNext);

        actionBar.setCustomView(cView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }


    private void initView()
    {
        txtFirstName = (EditText)this.findViewById(R.id.txtFirstName);
        txtLastName = (EditText)this.findViewById(R.id.txtLastName);
        txtEmail = (EditText)this.findViewById(R.id.txtEmail);
        txtPassword = (EditText)this.findViewById(R.id.txtPassword);
        txtRetypePassword = (EditText)this.findViewById(R.id.txtRetypePassword);
        txtSchoolCode = (EditText)this.findViewById(R.id.txtSchoolCode);
        //btnNext = (ImageButton)this.findViewById(R.id.btnNext);


        txtPassword.setTypeface(Typeface.DEFAULT);
        txtPassword.setTransformationMethod(new PasswordTransformationMethod());

        txtRetypePassword.setTypeface(Typeface.DEFAULT);
        txtRetypePassword.setTransformationMethod(new PasswordTransformationMethod());

        btnCreateLower = (Button)this.findViewById(R.id.btnCreateLower);
    }

    private void initAction()
    {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkValidForm() == true && checkValidEmail() == true)
                    initApicall();

            }
        });

        btnCreateLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkValidForm() == true && checkValidEmail() == true)
                    initApicall();
            }
        });

    }


    private boolean checkValidEmail()
    {
        if(isValidEmail(txtEmail.getText().toString()))
        {
            return true;
        }

        else
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_INVALID_EMAIL);
            return false;
        }

    }


    private boolean checkValidForm()
    {
        boolean isValid = true;

        if(txtFirstName.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_FIRST_NAME);
            isValid = false;
        }

        else if(txtLastName.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_LAST_NAME);
            isValid = false;
        }

        else if(txtEmail.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMAIL);
            isValid = false;
        }

        else if(txtPassword.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_PASSWORD);
            isValid = false;
        }

        else if(txtPassword.getText().toString().length() < 6)
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_PASSWORD_CHAR_LENGTH);
            isValid = false;
        }

        else if(txtRetypePassword.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_PASSWORD_RETYPE);
            isValid = false;
        }

        else if(!txtPassword.getText().toString().equals(txtRetypePassword.getText().toString()))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_PASSWORD_MATCH);
            isValid = false;
        }


        else if(txtSchoolCode.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SCHOOL_CODE_EMPTY);
            isValid = false;
        }


        return isValid;
    }


    private final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void initApicall()
    {
        HashMap<String,String> params = new HashMap<>();

        params.put("first_name", txtFirstName.getText().toString());
        params.put("last_name", txtLastName.getText().toString());
        params.put("email", txtEmail.getText().toString());
        params.put("password", txtPassword.getText().toString());
        params.put("school_code", txtSchoolCode.getText().toString());



        //AppRestClient.post(URLHelper.URL_PAID_USERCHECK, params, checkUserHandler);
        paidUserCheck(params);
    }

    private void paidUserCheck(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().paidUserCheck(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.e("SCCCCC", "response: " + response.body());

                        uiHelper.dismissLoadingDialog();


                        Wrapper modelContainer = GsonParser.getInstance()
                                .parseServerResponse2(response.body());

                        if (modelContainer.getStatus().getCode() == 200) {

                            String sId = modelContainer.getData().get("school_id").getAsString();
                            String schoolId;



                            int admissionCode = Integer.parseInt(sId);
                            if(admissionCode <= 9)
                            {
                                schoolId = "0"+sId;
                            }
                            else
                            {
                                schoolId = sId;
                            }


                            if(ordinal == 2) //type student
                            {
                                Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateStudentActivity.class);
                                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                                intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                                intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());



                                startActivity(intent);
                            }

                            else if(ordinal == 4) //type parent
                            {
                                Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateParentActivity.class);
                                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                                intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                                intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());

                                startActivity(intent);
                            }
                            else if(ordinal == 3) //type teacher
                            {
                                Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateTeacherActivity.class);
                                intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                                intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                                intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                                intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());

                                startActivity(intent);
                            }



                            Log.e("CODE 200", "code 200");
                        }

                        else if (modelContainer.getStatus().getCode() == 401) {

                            Log.e("CODE 401", "code 401");
                            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SCHOOL_CODE_VALID);
                        }

                        else if (modelContainer.getStatus().getCode() == 400) {

                            Log.e("CODE 400", "code 400");
                            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
                        }


                        else {

                        }

                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        uiHelper.showMessage(getString(R.string.internet_error_text));
                        if (uiHelper.isDialogActive()) {
                            uiHelper.dismissLoadingDialog();
                        }
                    }
                }
        );
    }
    AsyncHttpResponseHandler checkUserHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            Log.e("SCCCCC", "response: " + responseString);

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                String sId = modelContainer.getData().get("school_id").getAsString();
                String schoolId;



                int admissionCode = Integer.parseInt(sId);
                if(admissionCode <= 9)
                {
                    schoolId = "0"+sId;
                }
                else
                {
                    schoolId = sId;
                }


                if(ordinal == 2) //type student
                {
                    Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateStudentActivity.class);
                    intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                    intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                    intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());



                    startActivity(intent);
                }

                else if(ordinal == 4) //type parent
                {
                    Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateParentActivity.class);
                    intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                    intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                    intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());

                    startActivity(intent);
                }
                else if(ordinal == 3) //type teacher
                {
                    Intent intent = new Intent(RegistrationFirstPhaseActivity.this, CreateTeacherActivity.class);
                    intent.putExtra(AppConstant.USER_TYPE_CLASSTUNE, ordinal);
                    intent.putExtra(AppConstant.SCHOOL_ID_CLASSTUNE, schoolId);

                    intent.putExtra(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE, txtFirstName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_LAST_NAME_CLASSTUNE, txtLastName.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_EMAIL_CLASSTUNE, txtEmail.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_PASSWORD_CLASSTUNE, txtPassword.getText().toString());
                    intent.putExtra(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE, txtSchoolCode.getText().toString());

                    startActivity(intent);
                }



                Log.e("CODE 200", "code 200");
            }

            else if (modelContainer.getStatus().getCode() == 401) {

                Log.e("CODE 401", "code 401");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SCHOOL_CODE_VALID);
            }

            else if (modelContainer.getStatus().getCode() == 400) {

                Log.e("CODE 400", "code 400");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
            }


            else {

            }



        };
    };
}
