package com.classtune.app.schoolapp.classtune;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.ChildSelectionActivity;
import com.classtune.app.schoolapp.camera.CameraGalleryPicker;
import com.classtune.app.schoolapp.camera.IPictureCallback;
import com.classtune.app.schoolapp.fragments.UserTypeSelectionDialog;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogChangePassword;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by BLACK HAT on 10-Nov-15.
 */
public class CreateParentActivity extends FragmentActivity implements IDialogSelectChildrenDoneListener, UserAuthListener, IPictureCallback {


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
    private String batchId = "";

    private EditText txtUserId;
    private DatePicker pickerDob;
    private Spinner spinnerGender;
    private EditText txtContact;

    private UserHelper userHelper;

    //private Button btnCreate;

    private int year;
    private int month;
    private int day;

    private List<String> listGender;

    //private Button btnAddChild;

    private DialogSelectChildren dialog;

    private String childParam = "";

    private List<String> listChildParam;

    private LinearLayout layoutChildViewHolder;

    private RelativeLayout layoutAddChild;

    private List<ChildrenModel> listChildrenModel;
    private TextView txtChildId;
    private List<String> usedNames;
    private RelativeLayout layoutAddMoreChild;

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
        setContentView(R.layout.activity_create_parent2);

        Bundle extras = getIntent().getExtras();
        userHelper = new UserHelper(this, CreateParentActivity.this);

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

        uiHelper = new UIHelper(CreateParentActivity.this);

        initView();
        setUpActionBar();
        initAction();

        listChildrenModel = new ArrayList<ChildrenModel>();
        usedNames = new ArrayList<String>();


        updateImagenamePanel(false);

        mCameraGalleryPicker = new CameraGalleryPicker(this, this);
    }

    private void initView()
    {
        txtUserId = (EditText)this.findViewById(R.id.txtUserId);
        pickerDob = (DatePicker)this.findViewById(R.id.pickerDob);
        spinnerGender = (Spinner)this.findViewById(R.id.spinnerGender);
        txtContact = (EditText)this.findViewById(R.id.txtContact);


        //btnCreate = (Button)this.findViewById(R.id.btnCreate);
        //btnAddChild = (Button)this.findViewById(R.id.btnAddChild);

        dialog = new DialogSelectChildren(this, schoolCode, this);
        layoutChildViewHolder = (LinearLayout)this.findViewById(R.id.layoutChildViewHolder);

        layoutUploadPhoto = (RelativeLayout)this.findViewById(R.id.layoutUploadPhoto);

        txtDob = (TextView)this.findViewById(R.id.txtDob);
        layoutDatePicker = (RelativeLayout)this.findViewById(R.id.layoutDatePicker);

        txtUploadPhoto = (TextView)this.findViewById(R.id.txtUploadPhoto);

        imageNameContainer = (LinearLayout)this.findViewById(R.id.image_attached_layout);
        tvImageName = (TextView)this.findViewById(R.id.tv_image_name);
        btn_cross_image = (ImageView)this.findViewById(R.id.btn_cross_image);

        layoutAddChild = (RelativeLayout)this.findViewById(R.id.layoutAddChild);
        txtChildId = (TextView)this.findViewById(R.id.txtChildId);

        layoutAddMoreChild = (RelativeLayout)this.findViewById(R.id.layoutAddMoreChild);

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
                    initApiCall();
                }
            }
        });*/


        /*btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.show();

            }
        });*/

        listChildParam = new ArrayList<String>();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidForm() == true) {
                    initApiCall();
                }

            }
        });

        btnCreateLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidForm() == true) {
                    initApiCall();
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


                new DatePickerDialog(CreateParentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        layoutAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listChildrenModel.size()<3)
                    dialog.show();
                else
                    uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_ADD_CHILD_MORE_THAN);
            }
        });


        layoutAddMoreChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listChildrenModel.size()<3)
                    dialog.show();
                else
                    uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_ADD_CHILD_MORE_THAN);
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

            CreateParentActivity.this.year = myCalendar.get(Calendar.YEAR);
            CreateParentActivity.this.month = myCalendar.get(Calendar.MONTH);
            CreateParentActivity.this.day = myCalendar.get(Calendar.DAY_OF_MONTH);

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


    /*private void addViewMoreChild()
    {
        TextView tv = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.65f);

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setHighlightColor(Color.BLACK);
        tv.setLayoutParams(params);
        tv.setText("Child Id");


        EditText et = new EditText(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.35f);
        et.setLayoutParams(params2);
        et.setSingleLine(true);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsLayout.setMargins((int) AppUtility.getDeviceIndependentDpFromPixel(this, 10), (int) AppUtility.getDeviceIndependentDpFromPixel(this, 10), (int) AppUtility.getDeviceIndependentDpFromPixel(this, 10),
                (int) AppUtility.getDeviceIndependentDpFromPixel(this, 10));
        layout.setLayoutParams(paramsLayout);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setWeightSum(2f);

        layout.addView(tv);
        layout.addView(et);



        layoutChildIdHolder.addView(layout);




    }*/

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

        pickerDob.init(year, month, day, this);

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

        if(txtUserId.getText().toString().matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_USER_ID);
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

        else if(childParam.matches(""))
        {
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_ADD_ONE_CHILD);
            isValid = false;
        }




        return isValid;
    }

    private void initApiCall()
    {
        RequestParams params = new RequestParams();

        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", eMail);
        params.put("password", password);
        params.put("school_code", schoolCode);
        params.put("user_id", txtUserId.getText().toString());

        params.put("date_of_birth", dob);

        params.put("gender", gender);
        params.put("contact_no", txtContact.getText().toString());


        Log.e("CHILD_PARAMS", "is: " + childParam);

        params.put("childrens", childParam);

        if(!selectedImagePath.equalsIgnoreCase(""))
        {
            File myImage = new File(selectedImagePath);
            try {
                params.put(RequestKeyHelper.PROFILE_IMAGE, myImage);

                Log.e("IMG_FILE", "is: " + myImage);
            } catch(FileNotFoundException e) {}
        }



        userHelper.doClassTuneLogin(URLHelper.URL_PAID_PARENT, params, ordinal, uiHelper);
        //AppRestClient.post(URLHelper.URL_PAID_PARENT, params, createParentHandler);
    }

    AsyncHttpResponseHandler createParentHandler = new AsyncHttpResponseHandler() {

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


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                Log.e("CODE 200", "code 200");
            }

            else if (modelContainer.getStatus().getCode() == 401) {

                Log.e("CODE 401", "code 401");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_USER_NAME);
            }

            else if (modelContainer.getStatus().getCode() == 400) {

                Log.e("CODE 400", "code 400");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG);
            }

            else if (modelContainer.getStatus().getCode() == 402) {

                Log.e("CODE 402", "code 402");
                uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_INVALID_STUDENT_ID);
            }


            else {

            }



        };
    };


    @Override
    public void onDoneSelection(ChildrenModel childrenModel) {

        final String name = childrenModel.getChildId();
        if (usedNames.contains(name))
        {
            //true
            uiHelper.showErrorDialog(AppConstant.CLASSTUNE_MESSAGE_DUPLICATE_STUDENT_ID);
        }
        else
        {
            usedNames.add(name);
            //false
            listChildrenModel.add(childrenModel);

        }
        for(int i=0; i<listChildrenModel.size(); i++)
        {
            Log.e("@@C_ID", listChildrenModel.get(i).getChildId());
            Log.e("@@C_RELATION", listChildrenModel.get(i).getRelationName());

        }
        Log.e("@@C_SIZE", "" + listChildrenModel.size());
        if(listChildrenModel.size()<1)
        {
            layoutAddMoreChild.setVisibility(View.GONE);
        }
        else
        {
            layoutAddMoreChild.setVisibility(View.VISIBLE);
        }



        String data = "";
        for(int i=0; i<listChildrenModel.size(); i++)
        {
            data = listChildrenModel.get(i).getChildId()+","+listChildrenModel.get(i).getRelationName();
        }

        listChildParam.add(data);

        Set<String> hs = new LinkedHashSet<String>(listChildParam);
        hs.addAll(listChildParam);
        listChildParam.clear();
        listChildParam.addAll(hs);



        childParam= concatStringsWSep(listChildParam, "|");
        Log.e("@@C_PARAM", childParam);
        List<String> listName = new ArrayList<String>();
        for(int i=0;i<listChildrenModel.size();i++)
        {
            listName.add(listChildrenModel.get(i).getChildName());
        }
        txtChildId.setText(concatStringsWSep(listName, ","));


    }

    private static String concatStringsWSep(Iterable<String> strings, String separator)
    {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }
        return sb.toString();
    }

    private boolean hasDuplicates(List<ChildrenModel> data)
    {
        final List<String> usedNames = new ArrayList<String>();
        for (ChildrenModel obj : data) {
            final String name = obj.getChildId();

            if (usedNames.contains(name)) {
                return true;
            }

            usedNames.add(name);
        }

        return false;
    }

    //upload photo
    private void showPicChooserDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CreateParentActivity.this);



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

								/*Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(intent, "Select Picture"),
										1);*/
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
        if(requestCode == SchoolApp.REQUEST_CODE_CHILD_SELECTION){
            if (data == null) {
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(CreateParentActivity.this,
                            HomePageFreeVersion.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
            }
        }
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
                        Intent intent = new Intent(CreateParentActivity.this,
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
                        } else {
                            startActivityForResult(new Intent(this,
                                            ChildSelectionActivity.class),
                                    SchoolApp.REQUEST_CODE_CHILD_SELECTION);
                            //AppUtility.doPaidNavigation(userHelper, CreateParentActivity.this);
                        }
                        break;

                    default:
                        break;
                }

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
