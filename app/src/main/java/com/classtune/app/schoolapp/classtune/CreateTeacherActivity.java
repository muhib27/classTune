package com.classtune.app.schoolapp.classtune;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.classtune.app.schoolapp.model.TeacherInfo;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
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
 * Created by BLACK HAT on 12-Nov-15.
 */
public class CreateTeacherActivity extends FragmentActivity implements UserAuthListener, IPictureCallback {

    private UIHelper uiHelper;

    private int ordinal = -1;
    private String schoolId = "";

    private String gender = "";
    private String dob = "";

    private String firstName = "";
    private String lastName = "";
    private String eMail = "";
    private String password = "";
    private String schoolCode = "";

    private int year;
    private int month;
    private int day;

    private UserHelper userHelper;

    private LinearLayout layoutUserIdHolder;
    private TextView txtUserId;

    private EditText txtEmployeeNumber;
    private DatePicker pickerDob;
    private Spinner spinnerGender;
    private EditText txtContact;
    private EditText txtJobTitle;
    private Spinner spinnerCategory;
    private Spinner spinnerDepartment;
    private Spinner spinnerGrade;

    private DatePicker pickerJoiningDate;
    //private Spinner spinnerBatch;


    private String dateJoining = "";

    //private List<Batch> listBatch;
    //private String batchId = "";
    private List<String> listGender;

    private List<TeacherInfo> listTeacherInfoGrade;
    private List<TeacherInfo> listTeacherInfoDepartment;
    private List<TeacherInfo> listTeacherInfoCategory;
    private List<TeacherInfo> listTeacherInfoPosition;

    private String selectedGrade = "";
    private String selectedDepartment = "";
    private String selectedCategory = "";
    private String selectedPosition = "";

    private TextView txtDob;
    private Spinner spinnerPosition;
    private TextView txtJoiningDate;
    private RelativeLayout layoutDatePicker;
    private RelativeLayout layoutJoiningDatePicker;

    private ActionBar actionBar;
    private ImageButton btnNext;

    private ProgressDialog pd;

    private RelativeLayout layoutGreenPanelPosition;
    private View viewGreenPanelPosition;
    RelativeLayout layoutPositionHolder;

    private TextView txtMessage;



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
        setContentView(R.layout.activity_create_teacher2);

        uiHelper = new UIHelper(CreateTeacherActivity.this);

        userHelper = new UserHelper(this, CreateTeacherActivity.this);

        Bundle extras = getIntent().getExtras();

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



        listTeacherInfoGrade = new ArrayList<TeacherInfo>();
        listTeacherInfoDepartment = new ArrayList<TeacherInfo>();
        listTeacherInfoCategory = new ArrayList<TeacherInfo>();
        listTeacherInfoPosition = new ArrayList<TeacherInfo>();

        initView();
        setUpActionBar();
        initAction();
        initApiGetTeacherInfo();


        updateImagenamePanel(false);

        mCameraGalleryPicker = new CameraGalleryPicker(this, this);

    }

    private void initView()
    {
        layoutUserIdHolder = (LinearLayout)this.findViewById(R.id.layoutUserIdHolder);
        txtUserId = (TextView)this.findViewById(R.id.txtUserId);

        txtEmployeeNumber = (EditText)this.findViewById(R.id.txtEmployeeNumber);
        //pickerDob = (DatePicker)this.findViewById(R.id.pickerDob);
        spinnerGender = (Spinner)this.findViewById(R.id.spinnerGender);
        txtContact = (EditText)this.findViewById(R.id.txtContact);
        txtJobTitle = (EditText)this.findViewById(R.id.txtJobTitle);
        spinnerCategory = (Spinner)this.findViewById(R.id.spinnerCategory);
        spinnerDepartment = (Spinner)this.findViewById(R.id.spinnerDepartment);
        spinnerGrade = (Spinner)this.findViewById(R.id.spinnerGrade);

       // pickerJoiningDate = (DatePicker)this.findViewById(R.id.pickerJoiningDate);


        txtDob = (TextView)this.findViewById(R.id.txtDob);
        spinnerPosition = (Spinner)this.findViewById(R.id.spinnerPosition);
        txtJoiningDate = (TextView)this.findViewById(R.id.txtJoiningDate);
        layoutDatePicker = (RelativeLayout)this.findViewById(R.id.layoutDatePicker);
        layoutJoiningDatePicker = (RelativeLayout)this.findViewById(R.id.layoutJoiningDatePicker);

        layoutUploadPhoto = (RelativeLayout)this.findViewById(R.id.layoutUploadPhoto);
        imageNameContainer = (LinearLayout)this.findViewById(R.id.image_attached_layout);
        tvImageName = (TextView)this.findViewById(R.id.tv_image_name);
        btn_cross_image = (ImageView)this.findViewById(R.id.btn_cross_image);
        txtUploadPhoto = (TextView)this.findViewById(R.id.txtUploadPhoto);


        layoutGreenPanelPosition = (RelativeLayout)this.findViewById(R.id.layoutGreenPanelPosition);
        viewGreenPanelPosition = this.findViewById(R.id.viewGreenPanelPosition);
        layoutPositionHolder = (RelativeLayout)findViewById(R.id.layoutPositionHolder);

        txtMessage = (TextView)this.findViewById(R.id.txtMessage);

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

        txtEmployeeNumber.addTextChangedListener(new TextWatcher() {

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
                    txtUserId.setText(schoolId + "-" + txtEmployeeNumber.getText().toString());
                } else {
                    layoutUserIdHolder.setVisibility(View.GONE);
                }

            }
        });

        listGender = new ArrayList<String>();
        listGender.add(getString(R.string.java_createparentactivity_male));
        listGender.add(getString(R.string.java_createparentactivity_female));

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


        //spinnerCategory.setPrompt("Select a category");
        //spinnerDepartment.setPrompt("Select department");
        //spinnerGrade.setPrompt("Select a grade");




        layoutDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTeacherActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        layoutJoiningDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateTeacherActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkValidForm() == true)
                {
                    initApiCallCreateTeacher();
                }

            }
        });

        btnCreateLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidForm() == true)
                {
                    initApiCallCreateTeacher();
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


        showPositionMessageText(true);

    }


    private void showPositionMessageText(boolean show)
    {
        if(show == true)
        {
            txtMessage.setVisibility(View.VISIBLE);
            spinnerPosition.setVisibility(View.GONE);
        }
        else
        {
            txtMessage.setVisibility(View.GONE);
            spinnerPosition.setVisibility(View.VISIBLE);
        }

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

            CreateTeacherActivity.this.year = myCalendar.get(Calendar.YEAR);
            CreateTeacherActivity.this.month = myCalendar.get(Calendar.MONTH);
            CreateTeacherActivity.this.day = myCalendar.get(Calendar.DAY_OF_MONTH);

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


    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            CreateTeacherActivity.this.year = myCalendar.get(Calendar.YEAR);
            CreateTeacherActivity.this.month = myCalendar.get(Calendar.MONTH);
            CreateTeacherActivity.this.day = myCalendar.get(Calendar.DAY_OF_MONTH);

            StringBuilder sb = new StringBuilder()
                    .append(year).append("-")
                    .append(month + 1).append("-")
                    .append(day).append(" ");

            Log.e("CURRENT_DATE", "is: " + sb.toString());
            dateJoining = sb.toString();


            StringBuilder sbForTextView = new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-")
                    .append(year).append(" ");

            txtJoiningDate.setText(sbForTextView.toString());
            //pickerDob.init(CreateStudentActivity.this.year, CreateStudentActivity.this.month, CreateStudentActivity.this.day, this);
        }

    };




    private void initTeacherInfoSpiners()
    {

        final SpinnerTeacherInfoAdapter adapterGrade = new SpinnerTeacherInfoAdapter(this, android.R.layout.simple_spinner_item, listTeacherInfoGrade);
        adapterGrade.notifyDataSetChanged();
        spinnerGrade.setAdapter(adapterGrade);

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TeacherInfo data = adapterGrade.getItem(position);
                selectedGrade = data.getId();
                Log.e("Spinner info grad click", "id is: " + data.getId() + " name is: " + data.getName());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final SpinnerTeacherInfoAdapter adapterDepartment = new SpinnerTeacherInfoAdapter(this, android.R.layout.simple_spinner_item, listTeacherInfoDepartment);
        adapterDepartment.notifyDataSetChanged();
        spinnerDepartment.setAdapter(adapterDepartment);

        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TeacherInfo data = adapterDepartment.getItem(position);
                selectedDepartment = data.getId();
                Log.e("Spinner info dep click", "id is: " + data.getId() + " name is: " + data.getName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final SpinnerTeacherInfoAdapter adapterCategory = new SpinnerTeacherInfoAdapter(this, android.R.layout.simple_spinner_item, listTeacherInfoCategory);
        adapterCategory.notifyDataSetChanged();
        spinnerCategory.setAdapter(adapterCategory);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TeacherInfo data = adapterCategory.getItem(position);
                selectedCategory = data.getId();
                Log.e("Spinner info cat click", "id is: " + data.getId() + " name is: " + data.getName());

                initApiCallPosition(selectedCategory);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    SpinnerTeacherInfoAdapter adapterPosition = null;
    private void initPositionSpinner()
    {
        if(adapterPosition == null)
            adapterPosition = new SpinnerTeacherInfoAdapter(this, android.R.layout.simple_spinner_item, listTeacherInfoPosition);

        adapterPosition.notifyDataSetChanged();
        spinnerPosition.setAdapter(adapterPosition);

        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TeacherInfo data = adapterPosition.getItem(position);
                selectedPosition = data.getId();
                Log.e("Spinner info pos click", "id is: " + data.getId() + " name is: " + data.getName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Log.e("listPosition size", "is: " + listTeacherInfoPosition.size());


        if(listTeacherInfoPosition.size() > 0)
        {
            showPositionMessageText(false);
        }
        else
        {
            showPositionMessageText(true);
        }
    }


    private void initApiGetTeacherInfo()
    {
        RequestParams params = new RequestParams();

        params.put("school_code", schoolCode);

        AppRestClient.post(URLHelper.URL_TEACHER_INFO, params, teacherInfoHandler);
    }

    AsyncHttpResponseHandler teacherInfoHandler = new AsyncHttpResponseHandler() {


        //UIHelper uiHelper2 = new UIHelper(CreateTeacherActivity.this);
        @Override
        public void onFailure(Throwable arg0, String arg1) {
            /*uiHelper2.showMessage(arg1);
            if (uiHelper2.isDialogActive()) {
                uiHelper2.dismissLoadingDialog();
            }*/
        };

        @Override
        public void onStart() {

            //uiHelper2.showLoadingDialog("Please wait...");


        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            Log.e("SCCCCC", "response: " + responseString);

            //uiHelper2.dismissLoadingDialog();

            listTeacherInfoGrade.clear();
            listTeacherInfoDepartment.clear();
            listTeacherInfoCategory.clear();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");
                JsonArray arrayGrades = modelContainer.getData().get("grades").getAsJsonArray();
                JsonArray arrayDepartment = modelContainer.getData().get("departments").getAsJsonArray();
                JsonArray arrayCategories = modelContainer.getData().get("categories").getAsJsonArray();


                for (int i = 0; i < parseTeacherInfo(arrayGrades.toString()).size(); i++)
                {
                    listTeacherInfoGrade.add(parseTeacherInfo(arrayGrades.toString()).get(i));
                }

                for (int i = 0; i < parseTeacherInfo(arrayDepartment.toString()).size(); i++)
                {
                    listTeacherInfoDepartment.add(parseTeacherInfo(arrayDepartment.toString()).get(i));
                }

                for (int i = 0; i < parseTeacherInfo(arrayCategories.toString()).size(); i++)
                {
                    listTeacherInfoCategory.add(parseTeacherInfo(arrayCategories.toString()).get(i));
                }


                initTeacherInfoSpiners();

                //initApiCallPosition(selectedCategory);


            }

            else if(modelContainer.getStatus().getCode() == 401)
            {
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_NECESSARY_INFO);
            }

            else if(modelContainer.getStatus().getCode() == 400)
            {
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
            }

            else {

            }



        };
    };

    private ArrayList<TeacherInfo> parseTeacherInfo(String object) {
        ArrayList<TeacherInfo> data = new ArrayList<TeacherInfo>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<TeacherInfo>>() {}.getType());
        return data;
    }

    private void initApiCallPosition(String selectedCategory)
    {
        RequestParams params = new RequestParams();

        params.put("school_code", schoolCode);
        params.put("category_id", selectedCategory);

        AppRestClient.post(URLHelper.URL_TEACHER_POSITION, params, teacherPositionHandler);
    }

    AsyncHttpResponseHandler teacherPositionHandler = new AsyncHttpResponseHandler() {


        //UIHelper uiHelper2 = new UIHelper(CreateTeacherActivity.this);


        @Override
        public void onFailure(Throwable arg0, String arg1) {
            /*uiHelper2.showMessage(arg1);
            if (uiHelper2.isDialogActive()) {
                uiHelper2.dismissLoadingDialog();
            }*/
            if(pd.isShowing())
                pd.dismiss();
        };

        @Override
        public void onStart() {

            //uiHelper2.showLoadingDialog("Please wait...");

            pd = ProgressDialog.show(CreateTeacherActivity.this, "", getString(R.string.java_accountsettingsactivity_please_wait), true, false);

        };

        @Override
        public void onSuccess(int arg0, String responseString) {

            pd.dismiss();

            Log.e("SCCCCC", "response: " + responseString);

            //uiHelper2.dismissLoadingDialog();
            listTeacherInfoPosition.clear();
            if(adapterPosition != null)
                adapterPosition.notifyDataSetChanged();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");

                JsonArray arrayPos = modelContainer.getData().get("position").getAsJsonArray();

                for (int i = 0; i < parseTeacherInfo(arrayPos.toString()).size(); i++)
                {
                    listTeacherInfoPosition.add(parseTeacherInfo(arrayPos.toString()).get(i));
                }

                initPositionSpinner();



            }

            else if (modelContainer.getStatus().getCode() == 401)
            {

                Log.e("CODE 401", "code 401");

                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_POSITION);
                selectedPosition = "";

                showPositionMessageText(true);
            }

            else if(modelContainer.getStatus().getCode() == 400)
            {
                Log.e("CODE 400", "code 400");

                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
                selectedPosition = "";

                showPositionMessageText(true);
            }

            else {

            }



        };
    };

    private boolean checkValidForm()
    {
        boolean isValid = true;

        if(txtEmployeeNumber.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_NUMBER);
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

        else if(txtJobTitle.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_JOB_TITLE);
            isValid = false;
        }
        else if(selectedCategory.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_CATEGORY);
            isValid = false;
        }
        else if(selectedDepartment.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_DEPARTMENT);
            isValid = false;
        }

        else if(selectedPosition.matches("")) {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_POSITION_TYPE);
            isValid = false;
        }

        else if(dateJoining.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_JOINING_DATE);
            isValid = false;
        }

        return isValid;
    }


    private void initApiCallCreateTeacher()
    {
        RequestParams params = new RequestParams();

        params.put("first_name", firstName);//
        params.put("last_name", lastName);//
        params.put("email", eMail);//
        params.put("password", password);//
        params.put("school_code", schoolCode);//
        params.put("date_of_birth", dob);//
        params.put("gender", gender);//
        params.put("contact_no", txtContact.getText().toString());//

        params.put("employee_number", txtEmployeeNumber.getText().toString());
        params.put("job_title", txtJobTitle.getText().toString());
        params.put("employee_category_id", selectedCategory);
        params.put("employee_position_id", selectedPosition);
        params.put("employee_department_id", selectedDepartment);
        params.put("employee_grade_id", selectedGrade);
        params.put("joining_date", dateJoining);
        //params.put("batch_id", batchId);


        /*if(!selectedImagePath.equalsIgnoreCase(""))
        {
            File myImage = new File(selectedImagePath);
            try {
                params.put(RequestKeyHelper.PROFILE_IMAGE, myImage);

                Log.e("IMG_FILE", "is: " + myImage);
            } catch(FileNotFoundException e) {}
        }*/

        if(!selectedImagePath.equalsIgnoreCase(""))
        {

            try {
                params.put(RequestKeyHelper.PROFILE_IMAGE, mCameraGalleryFile);

                Log.e("IMG_FILE", "is: " + mCameraGalleryFile);
            }catch(FileNotFoundException e) {}
        }


        userHelper.doClassTuneLogin(URLHelper.URL_PAID_TEACHER, params, ordinal, uiHelper);
        //AppRestClient.post(URLHelper.URL_PAID_TEACHER, params, createTeacherHandler);
    }

    AsyncHttpResponseHandler createTeacherHandler = new AsyncHttpResponseHandler() {

    @Override
    public void onFailure(Throwable arg0, String arg1) {
        uiHelper.showMessage(arg1);
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

            Log.e("CODE 200", "code 200");
        }

        else if (modelContainer.getStatus().getCode() == 401) {

            Log.e("CODE 401", "code 401");
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_EMPLOYEE_EXISTS);
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
                CreateTeacherActivity.this);



        alertDialogBuilder
                .setMessage(AppConstant.CLASSTUNE_MESSAGE_SELECT_SOURCE)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.java_createparentactivity_gallery),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

								/*Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Picture"),
										1);*/

                                //dispatchOpenGelleryApp();
                                mCameraGalleryPicker.openGallery();

                            }
                        })
                .setNegativeButton(getString(R.string.java_createparentactivity_camera),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();

                                //dispatchTakePictureIntent();

								/*Intent takePicture = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(takePicture, 0);*/

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
            tvImageName.setText(getString(R.string.java_createparentactivity_successful));

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
                        Intent intent = new Intent(CreateTeacherActivity.this,
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
                        }else AppUtility.doPaidNavigation(userHelper, CreateTeacherActivity.this);
                        break;

                    default:
                        break;
                }

            } else {
                finish();
                Intent intent = new Intent(CreateTeacherActivity.this,
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

    @Override
    public void onPicturetaken(File path) {

        mCameraGalleryFile = path;
        selectedImagePath = mCameraGalleryFile.getAbsolutePath();

        updateImagenamePanel(true);
    }
}
