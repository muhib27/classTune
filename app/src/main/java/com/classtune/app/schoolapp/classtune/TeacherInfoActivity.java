package com.classtune.app.schoolapp.classtune;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by BLACK HAT on 27-Dec-15.
 */
public class TeacherInfoActivity extends Activity{

    //private UIHelper uiHelper;
    private UserHelper userHelper;

    private LinearLayout pbLayout;
    private ImageView profileImage;
    private ProgressBar progress;

    private TextView txtTeacherName;
    private TextView txtPosition;
    private TextView txtEmployeeNumber;
    private TextView txtDepartment;
    private TextView txtCategory;
    private TextView txtPhoneNumber;
    private TextView txtDob;
    private TextView txtJoiningDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);

        initView();

        initApiCall();

    }

    private void initView()
    {
        pbLayout = (LinearLayout)findViewById(R.id.pb);
        profileImage = (ImageView)findViewById(R.id.iv_profile_picture);
        progress = (ProgressBar)findViewById(R.id.progress);

        txtTeacherName = (TextView)this.findViewById(R.id.txtTeacherName);
        txtPosition = (TextView)this.findViewById(R.id.txtPosition);
        txtEmployeeNumber = (TextView)this.findViewById(R.id.txtEmployeeNumber);
        txtDepartment = (TextView)this.findViewById(R.id.txtDepartment);
        txtCategory = (TextView)this.findViewById(R.id.txtCategory);
        txtPhoneNumber = (TextView)this.findViewById(R.id.txtPhoneNumber);
        txtDob = (TextView)this.findViewById(R.id.txtDob);
        txtJoiningDate = (TextView)this.findViewById(R.id.txtJoiningDate);

    }

    private void initAction(String photoUrl, String name, String position, String employeeNumber,
                            String department, String category, String phone, String dob, String joiningDate)
    {
        ImageLoader.getInstance().displayImage(photoUrl,
                profileImage, AppUtility.getOptionForUserImage(),
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progress.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // spinner.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri,
                                                  View view, Bitmap loadedImage) {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri,
                                                   View view) {

                    }
                });

        txtTeacherName.setText(name);
        txtPosition.setText(position);
        txtEmployeeNumber.setText(employeeNumber);
        txtDepartment.setText(department);
        txtCategory.setText(category);
        txtPhoneNumber.setText(phone);
        //txtDob.setText(dob);
        //txtJoiningDate.setText(joiningDate);

        //AppUtility.getDateString(data.getPublishDate(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER);
        txtDob.setText(AppUtility.getDateString(dob, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
        txtJoiningDate.setText(AppUtility.getDateString(joiningDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));


        if(TextUtils.isEmpty(phone))
        {
            txtPhoneNumber.setText("---");
        }
    }

    private void initApiCall()
    {


        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        AppRestClient.post(URLHelper.URL_GET_TEACHER_INFO, params, teacherInfoHandler);


    }

    AsyncHttpResponseHandler teacherInfoHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            super.onFailure(arg0, arg1);
            pbLayout.setVisibility(View.GONE);
        };

        @Override
        public void onStart() {

            super.onStart();
            pbLayout.setVisibility(View.VISIBLE);


        };

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            pbLayout.setVisibility(View.GONE);


            Log.e("RES", "response string: " + responseString);


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                JsonObject objectEmployee = modelContainer.getData().get("employee").getAsJsonObject();

                String photoUrl = objectEmployee.get("user_image").getAsString();

                String name = objectEmployee.get("name").getAsString();
                String position = objectEmployee.get("position").getAsString();
                String employeeNumber = objectEmployee.get("employee_number").getAsString();
                String department = objectEmployee.get("department").getAsString();
                String category = objectEmployee.get("category").getAsString();
                String phone = objectEmployee.get("phone").getAsString();
                String dob = objectEmployee.get("date_of_birth").getAsString();
                String joiningDate = objectEmployee.get("joining_date").getAsString();

                initAction(photoUrl, name, position, employeeNumber, department, category, phone, dob, joiningDate);

            }

            else {

            }



        }


    };
}
