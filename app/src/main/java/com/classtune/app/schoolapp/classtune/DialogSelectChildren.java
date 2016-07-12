package com.classtune.app.schoolapp.classtune;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by BLACK HAT on 11-Nov-15.
 */
public class DialogSelectChildren extends Dialog {

    private Activity activity;
    private String schoolCode;

    private EditText txtChildId;
    private EditText txtRelation;
    private Button btnDone;

    private UIHelper uiHelper;

    private IDialogSelectChildrenDoneListener doneListener;

    //private LinearLayout layoutChildInfoHolder;
    //private TextView txtChildInfo;
    private String studentId = "";


    public DialogSelectChildren(Activity activity, String schoolCode, IDialogSelectChildrenDoneListener doneListener) {
        super(activity);
        this.activity = activity;
        this.schoolCode = schoolCode;
        this.doneListener = doneListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_children);

        uiHelper = new UIHelper(activity);

        initView();
        initAction();

        this.setTitle(AppConstant.CLASSTUNE_MESSAGE_DIALOG_TITLE);
    }

    private void initView()
    {
        btnDone = (Button)this.findViewById(R.id.btnDone);

        txtChildId = (EditText)this.findViewById(R.id.txtChildId);
        txtRelation = (EditText)this.findViewById(R.id.txtRelation);

        //layoutChildInfoHolder = (LinearLayout)this.findViewById(R.id.layoutChildInfoHolder);
        //txtChildInfo = (TextView)this.findViewById(R.id.txtChildInfo);
    }

    private void initAction()
    {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidForm() == true) {
                    initApiCall(studentId);
                }


            }
        });

        textChangeDetection(txtChildId);


    }

    private String getChildrenParam()
    {
        String data = "";

        if(!txtChildId.getText().toString().matches(""))
        {
            data = data+txtChildId.getText().toString()+","+txtRelation.getText().toString();
        }



        return data;
    }


    private boolean checkValidForm()
    {
        boolean isValid = true;



        if(txtChildId.getText().toString().matches(""))
        {

            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_DIALOG_CHILD_ID);
            isValid = false;

        }

        if(!txtChildId.getText().toString().matches(""))
        {
            if (txtRelation.getText().toString().matches(""))
            {
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_DIALOG_RELATION);
                isValid = false;
            }
        }





        return  isValid;
    }

    private void textChangeDetection(EditText txt)
    {
        txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() != 0)
                {

                    studentId = s.toString();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {


                }
            }
        });

    }


    private void initApiCall(String studentId)
    {
        RequestParams params = new RequestParams();

        params.put("school_code", schoolCode);
        params.put("student_id", studentId);


        AppRestClient.post(URLHelper.URL_CHECK_STUDENT, params, checkStudentHandler);
    }

    AsyncHttpResponseHandler checkStudentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(activity.getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(activity.getString(R.string.java_accountsettingsactivity_please_wait));


        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            Log.e("SCCCCC", "response: " + responseString);

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");
                String fullName = modelContainer.getData().get("full_name").getAsString();

                //layoutChildInfoHolder.setVisibility(View.VISIBLE);
                //txtChildInfo.setText("Student Id: " + fullName);

                //String childrenParam = getChildrenParam();
                //doneListener.onDoneSelection(childrenParam);
                doneListener.onDoneSelection(new ChildrenModel(txtChildId.getText().toString(), txtRelation.getText().toString(), fullName));
                DialogSelectChildren.this.dismiss();

            }
            else
            {
                //layoutChildInfoHolder.setVisibility(View.GONE);
            }

            if (modelContainer.getStatus().getCode() == 401) {

                Log.e("CODE 401", "code 401");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_STUDENT_NOT_EXISTS);
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
