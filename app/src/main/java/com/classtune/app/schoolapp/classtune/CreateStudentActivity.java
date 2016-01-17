package com.classtune.app.schoolapp.classtune;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.CompleteProfileActivityContainer;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.camera.CameraGalleryPicker;
import com.classtune.app.schoolapp.camera.IPictureCallback;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SPKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogChangePassword;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by BLACK HAT on 09-Nov-15.
 */
public class CreateStudentActivity extends FragmentActivity implements UserAuthListener, IPictureCallback {


    private int ordinal = -1;
    private String schoolId = "";

    private Spinner spinnerBatch;
    //private DatePicker pickerDob;
    private Spinner spinnerGender;
    private EditText txtContact;
    private EditText txtAdmission;
    private Button btnCreate;

    private UIHelper uiHelper;

    private List<String> listGender;

    private String gender = "";
    private String dob = "";

    private int year;
    private int month;
    private int day;

    private String firstName = "";
    private String lastName = "";
    private String eMail = "";
    private String password = "";
    private String schoolCode = "";
    private String batchId = "";

    private List<Batch> listBatch;

    private LinearLayout layoutUserIdHolder;
    private TextView txtUserId;

    private UserHelper userHelper;

    private ActionBar actionBar;
    private ImageButton btnNext;

    private TextView txtDob;
    private RelativeLayout layoutDatePicker;


    //upload photo
    private RelativeLayout layoutUploadPhoto;
    private LinearLayout imageNameContainer;
    private TextView tvImageName;
    private String selectedImagePath = "";
    private ImageView btn_cross_image;
    private TextView txtUploadPhoto;

    private CameraGalleryPicker mCameraGalleryPicker;
    private File mCameraGalleryFile;

    private Button btnCreateLower;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student2);

        Bundle extras = getIntent().getExtras();
        userHelper = new UserHelper(this, CreateStudentActivity.this);
        if(extras != null)
        {
            ordinal = extras.getInt(AppConstant.USER_TYPE_CLASSTUNE);
            schoolId = extras.getString(AppConstant.SCHOOL_ID_CLASSTUNE);

            firstName = extras.getString(AppConstant.STUDENT_FIRST_NAME_CLASSTUNE);
            lastName = extras.getString(AppConstant.STUDENT_LAST_NAME_CLASSTUNE);
            eMail = extras.getString(AppConstant.STUDENT_EMAIL_CLASSTUNE);
            password = extras.getString(AppConstant.STUDENT_PASSWORD_CLASSTUNE);
            schoolCode = extras.getString(AppConstant.STUDENT_SCHOOL_CODE_CLASSTUNE);
        }

        Log.e("ORDINAL AND SCHOOL ID", "ordinal: " + ordinal + " school id: " + schoolId);

        uiHelper = new UIHelper(CreateStudentActivity.this);
        listBatch = new ArrayList<Batch>();

        initView();
        setUpActionBar();
        initAction();
        initApiGetBatch();



        updateImagenamePanel(false);

        mCameraGalleryPicker = new CameraGalleryPicker(this, this);
    }

    private void initApiGetBatch()
    {
        RequestParams params = new RequestParams();

        params.put("school_code", schoolCode);

        AppRestClient.post(URLHelper.URL_PAID_BATCH, params, batchHandler);
    }

    AsyncHttpResponseHandler batchHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog("Please wait...");


        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            Log.e("SCCCCC", "response: " + responseString);

            uiHelper.dismissLoadingDialog();

            listBatch.clear();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");
                JsonArray arrayBatch = modelContainer.getData().get("batches").getAsJsonArray();
                for (int i = 0; i < parseBatch(arrayBatch.toString()).size(); i++)
                {
                    listBatch.add(parseBatch(arrayBatch.toString()).get(i));
                }

                //batchId = listBatch.get(0).getId();
                initBatchSpinner();


            }

            else if(modelContainer.getStatus().getCode() == 401)
            {
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_CLASS_YET);
            }

            else if(modelContainer.getStatus().getCode() == 400)
            {
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
            }

            else {

            }



        };
    };


    private ArrayList<Batch> parseBatch(String object) {
        ArrayList<Batch> data = new ArrayList<Batch>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<Batch>>() {}.getType());
        return data;
    }

    private void initBatchSpinner()
    {
        final SpinnerBatchAdapter adapter = new SpinnerBatchAdapter(this, android.R.layout.simple_spinner_item, listBatch);
        adapter.notifyDataSetChanged();
        spinnerBatch.setAdapter(adapter);

        spinnerBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Batch batch = adapter.getItem(position);
                batchId = batch.getId();
                Log.e("Spinner batch click", "id is: " + batchId + " name is: " + batch.getName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView()
    {
        spinnerBatch = (Spinner)this.findViewById(R.id.spinnerBatch);
        //pickerDob = (DatePicker)this.findViewById(R.id.pickerDob);
        spinnerGender = (Spinner)this.findViewById(R.id.spinnerGender);
        txtContact = (EditText)this.findViewById(R.id.txtContact);
        txtAdmission = (EditText)this.findViewById(R.id.txtAdmission);
        btnCreate = (Button)this.findViewById(R.id.btnCreate);

        layoutUserIdHolder = (LinearLayout)this.findViewById(R.id.layoutUserIdHolder);
        txtUserId = (TextView)this.findViewById(R.id.txtUserId);

        layoutUploadPhoto = (RelativeLayout)this.findViewById(R.id.layoutUploadPhoto);

        imageNameContainer = (LinearLayout)this.findViewById(R.id.image_attached_layout);
        tvImageName = (TextView)this.findViewById(R.id.tv_image_name);
        btn_cross_image = (ImageView)this.findViewById(R.id.btn_cross_image);

        txtDob = (TextView)this.findViewById(R.id.txtDob);
        layoutDatePicker = (RelativeLayout)this.findViewById(R.id.layoutDatePicker);

        txtUploadPhoto = (TextView)this.findViewById(R.id.txtUploadPhoto);

        btnCreateLower = (Button)this.findViewById(R.id.btnCreateLower);
    }

    private void setUpActionBar() {
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.classtune_green_color)));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        View cView = getLayoutInflater().inflate(R.layout.actionbar_view_classtune, null);

        btnNext = (ImageButton) cView.findViewById(R.id.btnNext);

        actionBar.setCustomView(cView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void initAction()
    {
        listGender = new ArrayList<String>();
        listGender.add("Male");
        listGender.add("Female");

        SpinnerGenderAdapter genderAdapter = new SpinnerGenderAdapter(this, android.R.layout.simple_spinner_item, listGender);
        spinnerGender.setAdapter(genderAdapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Spinner item position", "is: " + position);
                if (position == 0) {
                    gender = "1";
                } else {
                    gender = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setCurrentDateOnView();

        /*btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidForm() == true) {
                    initApicall();
                }
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidForm() == true) {
                    initApicall();
                }

            }
        });

        btnCreateLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidForm() == true) {
                    initApicall();
                }
            }
        });


        txtAdmission.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    layoutUserIdHolder.setVisibility(View.VISIBLE);
                    txtUserId.setText(schoolId + "-" + txtAdmission.getText().toString());
                } else {
                    layoutUserIdHolder.setVisibility(View.GONE);
                }

            }
        });

        layoutUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPicChooserDialog();
            }
        });

        btn_cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImagePath = "";
                updateImagenamePanel(false);
            }
        });

        layoutDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateStudentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            CreateStudentActivity.this.year = myCalendar.get(Calendar.YEAR);
            CreateStudentActivity.this.month = myCalendar.get(Calendar.MONTH);
            CreateStudentActivity.this.day = myCalendar.get(Calendar.DAY_OF_MONTH);

            StringBuilder sb = new StringBuilder()
                    .append(year).append("-")
                    .append(month + 1).append("-")
                    .append(day).append(" ");

            Log.e("CURRENT_DATE", "is: " + sb.toString());
            dob = sb.toString();

            StringBuilder sbForTextView = new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-")
                    .append(year).append(" ");

            txtDob.setText(sbForTextView.toString());

            //pickerDob.init(CreateStudentActivity.this.year, CreateStudentActivity.this.month, CreateStudentActivity.this.day, this);
        }

    };

    /*public void setCurrentDateOnView() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder()
                .append(year).append("-")
                .append(month + 1).append("-")
                .append(day).append(" ");

        Log.e("CURRENT_DATE", "is: " + sb.toString());
        dob = sb.toString();

        //pickerDob.init(year, month, day, this);

    }*/

    /*@Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        StringBuilder sb = new StringBuilder()
                .append(year).append("-")
                .append(monthOfYear + 1).append("-")
                .append(dayOfMonth).append(" ");

        Log.e("CHANGED_DATE", "is: " + sb.toString());
        dob = sb.toString();
    }*/


    private boolean checkValidForm()
    {
        boolean isValid = true;

        if(txtAdmission.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_ADMISSION_NUMBER);
            isValid = false;
        }
        else if(batchId.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_BATCH_SELECT);
            isValid = false;
        }
        else if(dob.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_DOB_SELECT);
            isValid = false;
        }
        else if(gender.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_GENDER_SELECT);
            isValid = false;
        }
        else if(txtContact.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_CONTACT_NUMBER);
            isValid = false;
        }
        return isValid;
    }

    private void initApicall()
    {
        RequestParams params = new RequestParams();

        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", eMail);
        params.put("password", password);
        params.put("school_code", schoolCode);
        params.put("batch_id", batchId);

        params.put("date_of_birth", dob);

        params.put("gender", gender);
        params.put("contact_no", txtContact.getText().toString());
        params.put("admission", txtAdmission.getText().toString());

        if(!selectedImagePath.equalsIgnoreCase(""))
        {
            File myImage = new File(selectedImagePath);
            try {
                params.put(RequestKeyHelper.PROFILE_IMAGE, myImage);

                Log.e("IMG_FILE", "is: " + myImage);
            } catch(FileNotFoundException e) {}
        }


        userHelper.doClassTuneLogin(URLHelper.URL_PAID_STUDENT, params, ordinal, uiHelper);
        //AppRestClient.post(URLHelper.URL_PAID_STUDENT, params, createStudentHandler);
    }

    AsyncHttpResponseHandler createStudentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog("Please wait...");


        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            Log.d("TOTAL RESPONSE", "response: " + responseString);

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");
            }

            else if (modelContainer.getStatus().getCode() == 401) {

                Log.e("CODE 401", "code 401");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_ADMISSION_NUMBER_EXISTS);
            }

            else if (modelContainer.getStatus().getCode() == 400) {

                Log.e("CODE 400", "code 400");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
            }


            else {

            }



        };
    };




    private void showPicChooserDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CreateStudentActivity.this);



        alertDialogBuilder
                .setMessage(AppConstant.CLASSTUNE_MESSAGE_SELECT_SOURCE)
                .setCancelable(false)
                .setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                                mCameraGalleryPicker.openGallery();

                            }
                        })
                .setNegativeButton("Camera",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                                mCameraGalleryPicker.openCamere();

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);

        alertDialog.show();

    }




    private void updateImagenamePanel(boolean isVisible) {
        if (isVisible) {
            imageNameContainer.setVisibility(View.VISIBLE);
            //tvImageName.setText(getFileNameFromPath(selectedImagePath));
            tvImageName.setText("Successful");

            txtUploadPhoto.setVisibility(View.GONE);

        } else {
            imageNameContainer.setVisibility(View.GONE);

            txtUploadPhoto.setVisibility(View.VISIBLE);
        }
    }
    private String getFileNameFromPath(String path) {
        String[] tokens = path.split("/");
        return tokens[tokens.length - 1];
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraGalleryPicker.activityResult(requestCode, resultCode, data);
    }



    @Override
    public void onAuthenticationStart() {
        uiHelper.showLoadingDialog(getString(R.string.loading_text));

    }

    @Override
    public void onAuthenticationSuccessful() {
        if (uiHelper.isDialogActive()) {
            uiHelper.dismissLoadingDialog();

        }
        if (UserHelper.isRegistered()) {
            if (UserHelper.isLoggedIn()) {




                switch (UserHelper.getUserAccessType()) {
                    case FREE:
                        Intent intent = new Intent(CreateStudentActivity.this,
                                HomePageFreeVersion.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                    case PAID:
                        if ( UserHelper.isFirstLogin() ){
                            PopupDialogChangePassword picker = new PopupDialogChangePassword();
                            picker.show(getSupportFragmentManager(), null);
                        }else doPaidNavigation();
                        break;

                    default:
                        break;
                }

            } else {
                finish();
                Intent intent = new Intent(CreateStudentActivity.this,
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
    public void onAuthenticationFailed(String msg) {

    }

    @Override
    public void onPaswordChanged() {

    }

    public void doPaidNavigation(){
        switch (userHelper.getUser().getType()) {

            case PARENTS:
                if (userHelper.getUser().getChildren() == null) {
                    Log.e("Userhelper", "null");
                }
                if (userHelper.getUser().getChildren().size() > 0) {
                    Intent paidIntent = new Intent(CreateStudentActivity.this,
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
                Intent paidIntent = new Intent(CreateStudentActivity.this,
                        HomePageFreeVersion.class);
                paidIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(paidIntent);
                finish();
                break;
        }

    }

    @Override
    public void onPicturetaken(File path) {

        mCameraGalleryFile = path;
        selectedImagePath = mCameraGalleryFile.getAbsolutePath();

        updateImagenamePanel(true);
    }
}
