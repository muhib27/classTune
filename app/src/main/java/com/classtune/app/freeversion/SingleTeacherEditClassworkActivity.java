package com.classtune.app.freeversion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.DatePickerFragment;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Subject;
import com.classtune.app.schoolapp.model.TeacherClassWork;
import com.classtune.app.schoolapp.model.TypeHomeWork;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

public class SingleTeacherEditClassworkActivity extends ChildContainerActivity {
    private TeacherClassWork data;
    private Gson gson;

    private String id;
    private UIHelper uiHelper;
    EditText subjectEditText, classworkDescriptionEditText;
    private List<BaseType> subjectCats;
    private List<BaseType> classworkTypeCats;
    TextView subjectNameTextView, classWorkTypeTextView, choosenFileTextView;
    private String subjectId="", homeworkTypeId="1";
    private String selectedFilePath = "";
    private TextView choosenDateTextView;
    private final static int REQUEST_CODE_FILE_CHOOSER = 101;
    private String dateFormatServerString = "";

    private LinearLayout layoutDate;
    private LinearLayout layoutAttachmentHolder;
    UserHelper userHelper;


    private ImageButton btnSubjectName;
    private ImageButton btnclassworkType;
    private CustomButton btnAttachmentFile;
    private CustomButton btnDueDate;

    private Button btnCancel;
    private Button btnSave;

    private LinearLayout layoutSelectSubject;
    private LinearLayout layoutSelectType;
    private String mimeType = "";
    private String fileSize = "";

    private LinearLayout layoutFileAttachRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_teacher_edit_classwork);
        gson = new Gson();

        subjectCats = new ArrayList<BaseType>();
        uiHelper = new UIHelper(this);
        userHelper=new UserHelper(this);

        if(getIntent().getExtras() != null)
            this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_CLASSWORK);

        intiviews();
        createHomeworkTypeCats();
        fetchSubject();

        initApiCall();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStoragePermissionGranted();
        }
    }


    private void initApiCall()
    {
        RequestParams params = new RequestParams();

        //app.showLog("adfsdfs", app.getUserSecret());

        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", this.id);



        AppRestClient.post(URLHelper.URL_SINGLE_TEACHER_CLASSWORK, params, singleTeacherHomeWorkHandler);
    }

    AsyncHttpResponseHandler singleTeacherHomeWorkHandler = new AsyncHttpResponseHandler() {

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


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

                JsonObject objHomework = modelContainer.getData().get("classwork").getAsJsonObject();
                data = gson.fromJson(objHomework.toString(), TeacherClassWork.class);

                Log.e("HHH", "data: " + data.getClasswork_name());

                initialDataPopulate();

            }

            else {

            }



        };
    };


    private void initialDataPopulate()
    {
        subjectId = data.getSubjects_id();
        homeworkTypeId = data.getClasswork_type();

        subjectNameTextView.setText(data.getSubjects());

        if(data.getClasswork_type().equalsIgnoreCase("1"))
        {
            classWorkTypeTextView.setText(R.string.java_singleteacheredithomeworkactivity_regular);
        }
        else if(data.getClasswork_type().equalsIgnoreCase("2"))
        {
            classWorkTypeTextView.setText(R.string.java_singleteacheredithomeworkactivity_project);
        }

        subjectEditText.setText(data.getClasswork_name());

        classworkDescriptionEditText.setText(data.getContent());

       // dateFormatServerString = data.getDuedate();

        if(!TextUtils.isEmpty(data.getAttachment_file_name()))
            choosenFileTextView.setText(data.getAttachment_file_name());

        //choosenDateTextView.setText(data.getDuedate());

    }





    private boolean isFormValid() {
        if(subjectId.equals("")){
            Toast.makeText(this, R.string.java_singleteacheredithomeworkactivity_choose_subject, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(subjectEditText.getText().toString().trim().equals("")){
            Toast.makeText(this, R.string.java_singleteacheredithomeworkactivity_subject_title_cannot_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(classworkDescriptionEditText.getText().toString().trim().equals("")){
            Toast.makeText(this, R.string.java_singleteacheredithomeworkactivity_homework_description_cannot_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void PublishHomeWork() {

        RequestParams params = new RequestParams();

        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put(RequestKeyHelper.SUBJECT_ID, subjectId);
        params.put(RequestKeyHelper.CONTENT, classworkDescriptionEditText
                .getText().toString());
        params.put(RequestKeyHelper.SUBJECT_TITLE, subjectEditText.getText()
                .toString());
        params.put(RequestKeyHelper.TYPE, homeworkTypeId);
       // params.put(RequestKeyHelper.HOMEWORK_DUEDATE, dateFormatServerString);
        params.put("id", id);

        if(!selectedFilePath.equalsIgnoreCase(""))
        {
            File myFile= new File(selectedFilePath);
            try {
                params.put("attachment_file_name", myFile);

                Log.e("FILE_NAME", "is: " + myFile.toString());
            } catch(FileNotFoundException e) {}
        }

        if(!TextUtils.isEmpty(mimeType)){
            params.put("mime_type", mimeType);
        }
        if(!TextUtils.isEmpty(fileSize)){
            params.put("file_size", fileSize);
        }


        AppRestClient.post(URLHelper.URL_TEACHER_ADD_CLASSWORK, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        if (!uiHelper.isDialogActive())
                            uiHelper.showLoadingDialog(getString(R.string.loading_text));
                        super.onStart();
                    }

                    @Override
                    public void onFailure(Throwable arg0, String response) {
                        if (uiHelper.isDialogActive())
                            uiHelper.dismissLoadingDialog();
                        Log.e("POST HOMEWORK FAILED", response);
                        super.onFailure(arg0, response);
                    }

                    @Override
                    public void onSuccess(int arg0, String responseString) {
                        if (uiHelper.isDialogActive())
                            uiHelper.dismissLoadingDialog();

                        Log.e("SERVERRESPONSE", responseString);
                        Wrapper wrapper = GsonParser.getInstance()
                                .parseServerResponse(responseString);
                        if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                            Toast.makeText(SingleTeacherEditClassworkActivity.this,
                                    R.string.java_singleteacheredithomeworkactivity_saved_as_draft,
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            clearDataFields();
                            finish();
                        } else
                            Toast.makeText(
                                    SingleTeacherEditClassworkActivity.this,
                                    R.string.java_singleteacheredithomeworkactivity_failed_post,
                                    Toast.LENGTH_SHORT).show();
                        super.onSuccess(arg0, responseString);
                    }
                });
    }

    private void clearDataFields()
    {
        subjectNameTextView.setText("");
        classWorkTypeTextView.setText("");
        subjectEditText.setText("");
        classworkDescriptionEditText.setText("");
        choosenFileTextView.setText(R.string.java_singleteacheredithomeworkactivity_no_file_attached);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        choosenDateTextView.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

    }



    private void createHomeworkTypeCats() {
        classworkTypeCats = new ArrayList<BaseType>();
        classworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_regular), "1"));
        classworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_project), "2"));
    }

    private void fetchSubject() {

        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        AppRestClient.post(URLHelper.URL_TEACHER_CLASSWORK_GET_SUBJECT, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable arg0, String response) {
                        super.onFailure(arg0, response);
                        Log.e("GET_SUBJECT_FAIL", response);
                    }

                    @Override
                    public void onSuccess(int arg0, String response) {
                        super.onSuccess(arg0, response);
                        Log.e("GET_SUBJECT_SUCCESS", response);
                        Wrapper wrapper = GsonParser.getInstance()
                                .parseServerResponse(response);
                        if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                            subjectCats.clear();
                            subjectCats.addAll(GsonParser.getInstance()
                                    .parseSubject(
                                            wrapper.getData().get("subjects")
                                                    .toString()));
                        }
                    }
                });
    }

    private void intiviews() {
        subjectEditText = (EditText) this
                .findViewById(R.id.et_teacher_ah_subject_name);


        layoutAttachmentHolder = (LinearLayout)this.findViewById(R.id.layoutAttachmentHolder);

        classworkDescriptionEditText = (EditText) this
                .findViewById(R.id.et_teacher_ah_homework_description);
        subjectNameTextView = (TextView) this
                .findViewById(R.id.tv_teacher_ah_subject_name);
        classWorkTypeTextView = (TextView) this
                .findViewById(R.id.tv_teacher_ah_homework_type);
        choosenFileTextView = (TextView) this
                .findViewById(R.id.tv_teacher_ah_choosen_file_name);
        choosenDateTextView = (TextView) this
                .findViewById(R.id.tv_teacher_ah_date);


        btnSubjectName = ((ImageButton) this.findViewById(R.id.btn_subject_name));
        btnSubjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(PickerType.TEACHER_SUBJECT, subjectCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_subject));
            }
        });


        btnclassworkType = ((ImageButton) this.findViewById(R.id.btn_classwork_type));
        btnclassworkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(PickerType.TEACHER_HOMEWORKTYPE, classworkTypeCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
            }
        });



        if(userHelper.getUser().getPaidInfo().getSchoolType() == 0)
        {
            layoutAttachmentHolder.setAlpha(.5f);
        }
        else
        {
            layoutAttachmentHolder.setAlpha(1f);
            btnAttachmentFile = ((CustomButton) this.findViewById(R.id.btn_teacher_ah_attach_file));
            btnAttachmentFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChooser();
                }
            });

            layoutFileAttachRight = (LinearLayout)this.findViewById(R.id.layoutFileAttachRight);
            layoutFileAttachRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChooser();
                }
            });
        }

		/*((CustomButton) view.findViewById(R.id.btn_teacher_ah_attach_file))
				.setOnClickListener(this);*/




   /*     btnDueDate = ((CustomButton) this.findViewById(R.id.btn_teacher_ah_due_date));
        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
            }
        });*/


     /*   layoutDate = (LinearLayout)this.findViewById(R.id.layoutDate);
        layoutDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatepicker();
            }
        });*/

/*
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        choosenDateTextView.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));*/


        btnCancel = (Button)this.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnSave = (Button)this.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    PublishHomeWork();
                }
            }
        });

        layoutSelectSubject = (LinearLayout)this.findViewById(R.id.layoutSelectSubject);
        layoutSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(PickerType.TEACHER_SUBJECT, subjectCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_subject));


            }
        });

        layoutSelectType = (LinearLayout)this.findViewById(R.id.layoutSelectType);
        layoutSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(PickerType.TEACHER_HOMEWORKTYPE, classworkTypeCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
            }
        });
    }

    public void showPicker(PickerType type, List<BaseType> cats, String title) {

        Picker picker = Picker.newInstance(0);
        picker.setData(type, cats, new Picker.PickerItemSelectedListener() {

            @Override
            public void onPickerItemSelected(BaseType item) {

                switch (item.getType()) {
                    case TEACHER_SUBJECT:
                        Subject mdata = (Subject) item;
                        subjectNameTextView.setText(mdata.getName());
                        subjectId = mdata.getId();
                        break;
                    case TEACHER_HOMEWORKTYPE:
                        TypeHomeWork data = (TypeHomeWork) item;
                        classWorkTypeTextView.setText(data.getTypeName());
                        homeworkTypeId = data.getTypeId();
                        break;
                    default:
                        break;
                }

            }
        }, title);
        picker.show(getSupportFragmentManager(), null);
    }

    /*@Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_publish_homework:
                if (isFormValid()) {
                    PublishHomeWork(false);
                }
                break;

            case R.id.btn_save_draft_homework:
                if (isFormValid()) {
                    PublishHomeWork(true);
                }
                break;

            default:
                break;
        }
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FILE_CHOOSER:
                if (resultCode == SingleTeacherEditHomeworkActivity.this.RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        *//*if (uri.getLastPathSegment().endsWith("doc")
                                || uri.getLastPathSegment().endsWith("docx")
                                || uri.getLastPathSegment().endsWith("pdf")) {
                            try {
                                // Get the file path from the URI
                                final String path = FileUtils.getPath(
                                        SingleTeacherEditHomeworkActivity.this, uri);
                                selectedFilePath = path;
                                choosenFileTextView
                                        .setText(getFileNameFromPath(selectedFilePath));

                                mimeType = SchoolApp.getInstance().getMimeType(selectedFilePath);
                                File myFile= new File(selectedFilePath);
                                fileSize = String.valueOf(myFile.length());

                                Log.e("MIME_TYPE", "is: "+SchoolApp.getInstance().getMimeType(selectedFilePath));
                                Log.e("FILE_SIZE", "is: "+fileSize);

                            } catch (Exception e) {
                                Log.e("FileSelectorTestAtivity",
                                        "File select error", e);
                            }
                        } else {
                            Toast.makeText(SingleTeacherEditHomeworkActivity.this, R.string.java_singleteacheredithomeworkactivity_invalid_file_type,
                                    Toast.LENGTH_SHORT).show();
                        }*//*


                            try {
                                // Get the file path from the URI
                                final String path = FileUtils.getPath(
                                        SingleTeacherEditHomeworkActivity.this, uri);
                                selectedFilePath = path;
                                choosenFileTextView
                                        .setText(getFileNameFromPath(selectedFilePath));

                                mimeType = SchoolApp.getInstance().getMimeType(selectedFilePath);
                                File myFile= new File(selectedFilePath);
                                fileSize = String.valueOf(myFile.length());

                                Log.e("MIME_TYPE", "is: "+SchoolApp.getInstance().getMimeType(selectedFilePath));
                                Log.e("FILE_SIZE", "is: "+fileSize);

                            } catch (Exception e) {
                                Log.e("FileSelectorTestAtivity",
                                        "File select error", e);
                            }
                        Log.e("File", "Uri = " + uri.toString());
                    }
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 119) {

            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    // Here is object contains selected files names and path
                    selectedFilePath = object.path+object.names.get(0);


                    mimeType = SchoolApp.getInstance().getMimeType(selectedFilePath);
                    File myFile= new File(selectedFilePath);
                    fileSize = String.valueOf(myFile.length());

                    Log.e("MIME_TYPE", "is: "+SchoolApp.getInstance().getMimeType(selectedFilePath));
                    Log.e("FILE_SIZE", "is: "+fileSize);

                    long fileSizeInKB = myFile.length() / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;

                    if(fileSizeInMB <= 5) {
                        choosenFileTextView.setText(object.names.get(0));
                    }
                    else {
                        selectedFilePath = "";
                        mimeType = "";
                        fileSize = "";
                        Toast.makeText(this, R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showChooser() {
        /*Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,
                getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }*/

        Intent intent = new Intent(this, ru.bartwell.exfilepicker.ExFilePickerActivity.class);
        intent.putExtra(ExFilePicker.SET_START_DIRECTORY, "/");
        intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
        intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);
        intent.putExtra(ExFilePicker.DISABLE_SORT_BUTTON, true);
        intent.putExtra(ExFilePicker.ENABLE_QUIT_BUTTON, true);
        startActivityForResult(intent, 119);
    }

    private String getFileNameFromPath(String path) {
        String[] tokens = path.split("/");
        return tokens[tokens.length - 1];
    }

    private void showDatepicker() {
        DatePickerFragment picker = new DatePickerFragment();
        picker.setCallbacks(datePickerCallback);
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    DatePickerFragment.DatePickerOnSetDateListener datePickerCallback = new DatePickerFragment.DatePickerOnSetDateListener() {

        @Override
        public void onDateSelected(int month, String monthName, int day,
                                   int year, String dateFormatServer, String dateFormatApp,
                                   Date date) {
            // TODO Auto-generated method stub
            choosenDateTextView.setText(dateFormatApp);
            dateFormatServerString = dateFormatServer;
        }

		/*
		 * @Override public void onDateSelected(String monthName, int day, int
		 * year) { // TODO Auto-generated method stub Date date; try { date =
		 * new SimpleDateFormat("MMMM").parse(monthName); Calendar cal =
		 * Calendar.getInstance(); cal.setTime(date); String dateString = day +
		 * "-" + cal.get(Calendar.MONTH) + "-" + year;
		 * choosenDateTextView.setText(dateString); } catch (ParseException e) {
		 * // TODO Auto-generated catch block Log.e("ERROR", e.toString()); } }
		 */
    };

    /*@Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }*/

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION","Permission is granted");
                return true;
            } else {

                Log.v("PERMISSION","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSION","Permission is granted");
            return true;
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("PERMISSION","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission

        }
    }
}
