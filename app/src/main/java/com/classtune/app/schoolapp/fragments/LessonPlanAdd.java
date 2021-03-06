package com.classtune.app.schoolapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.IAttachFile;
import com.classtune.app.schoolapp.model.LessonPlanCategory;
import com.classtune.app.schoolapp.model.Subject;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BLACK HAT on 24-Mar-15.
 */
public class LessonPlanAdd extends Fragment implements IAttachFile {


    private View view;
    private UIHelper uiHelper;
    private UserHelper userHelper;

    private EditText txtTitle;

    private TextView txtSelectCategory;
    private ImageButton btnSelectCategory;
    private LinearLayout layoutSelectCategoryActionHolder;

    private TextView txtSubjectClass;
    private ImageButton btnSubjectClass;
    private LinearLayout layoutSubjectClassActionHolder;

    private TextView txtLectureDate;
    private ImageButton btnLectureDate;
    private LinearLayout layoutLectureDateActionHolder;

    private EditText txtDescription;

    private Button btnSave;
    private Button btnSaveAndAssign;


    private List<LessonPlanCategory> listCategory;
    private String selectedCategoryId = null;


    private List<Subject> listSubject;
    private String selectedSubjectId = null;

    private String selectedDate = null;

    private LinearLayout layoutSelectMultipleSubject;


    private List<String> listSubjectId;
    private List<String> listSubjectName;



    private boolean  isSubjectLayoutClicked = false;
    private LinearLayout layoutAttachmentHolder, layoutFileAttachRight;
    private CustomButton btnTeacherAttachFile;

    private String selectedFilePath = "";
    private String mimeType = "";
    private String fileSize = "";
    private TextView choosenFileTextView;
    private File myFile;
    public static LessonPlanAdd instance;
    private ArrayList<String> listFiles;

    @Override
    public void onResume() {
        super.onResume();
        if(AppUtility.isInternetConnected() == false){
            Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());

        listFiles = new ArrayList<>();

        listCategory = new ArrayList<LessonPlanCategory>();
        listSubject = new ArrayList<Subject>();

        listSubjectId = new ArrayList<String>();

        listSubjectName = new ArrayList<String>();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lessonplan_add2, container, false);

        initView(view);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStoragePermissionGranted();
        }

        return view;

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION","Permission is granted");
                return true;
            } else {

                Log.v("PERMISSION","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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


    private void initView(View view)
    {

        txtTitle = (EditText)view.findViewById(R.id.txtTitle);

        txtSelectCategory = (TextView)view.findViewById(R.id.txtSelectCategory);
        btnSelectCategory = (ImageButton)view.findViewById(R.id.btnSelectCategory);
        layoutSelectCategoryActionHolder = (LinearLayout)view.findViewById(R.id.layoutSelectCategoryActionHolder);

        txtSubjectClass = (TextView)view.findViewById(R.id.txtSubjectClass);
        btnSubjectClass = (ImageButton)view.findViewById(R.id.btnSubjectClass);
        layoutSubjectClassActionHolder = (LinearLayout)view.findViewById(R.id.layoutSubjectClassActionHolder);

        txtLectureDate = (TextView)view.findViewById(R.id.txtLectureDate);
        btnLectureDate = (ImageButton)view.findViewById(R.id.btnLectureDate);
        layoutLectureDateActionHolder = (LinearLayout)view.findViewById(R.id.layoutLectureDateActionHolder);

        txtDescription = (EditText)view.findViewById(R.id.txtDescription);


        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSaveAndAssign = (Button)view.findViewById(R.id.btnSaveAndAssign);

        //btnSave.setButtonSelected(true, R.color.black);
        //btnSaveAndAssign.setButtonSelected(true, R.color.black);


        btnSelectCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallCategory();
            }
        });

        layoutSelectCategoryActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallCategory();
            }
        });


        btnSubjectClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);

                    if(listSubject.size() <=0)
                        initApiCallSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);


                }

                //initApiCallSubject();

            }
        });

        layoutSubjectClassActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);


                    if(listSubject.size() <=0)
                        initApiCallSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);


                }



            }
        });


        btnLectureDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showDatepicker();
            }
        });

        layoutLectureDateActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showDatepicker();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isValidData())
                    initApiCallAdd(false);
            }
        });

        btnSaveAndAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidData())
                    initApiCallAdd(true);
            }
        });



        layoutSelectMultipleSubject = (LinearLayout)view.findViewById(R.id.layoutSelectMultipleSubject);
        layoutSelectMultipleSubject.setBackgroundColor(Color.parseColor("#eff0f4"));


        choosenFileTextView = (TextView) view
                .findViewById(R.id.tv_teacher_ah_choosen_file_name);
        choosenFileTextView.setText(getString(R.string.java_singleteacheredithomeworkactivity_no_file_attached));


        btnTeacherAttachFile = (CustomButton)view.findViewById(R.id.btn_teacher_ah_attach_file);
        layoutAttachmentHolder = (LinearLayout)view.findViewById(R.id.layoutAttachmentHolder);
        layoutFileAttachRight = (LinearLayout)view.findViewById(R.id.layoutFileAttachRight);

        btnTeacherAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showChooser();
            }
        });
        layoutAttachmentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooser();
            }
        });

        layoutFileAttachRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooser();
            }
        });


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
        instance = this;
        showFileDialog();
        getActivity().setResult(Activity.RESULT_OK);


        /*Intent intent = new Intent(getActivity(), ru.bartwell.exfilepicker.ExFilePickerActivity.class);
        intent.putExtra(ExFilePicker.SET_START_DIRECTORY, "/");
        intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
        intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);
        intent.putExtra(ExFilePicker.DISABLE_SORT_BUTTON, true);
        intent.putExtra(ExFilePicker.ENABLE_QUIT_BUTTON, true);
        getActivity().startActivityForResult(intent, AppConstant.REQUEST_CODE_TEACHER_ADD_LESSON_PLAN);
        getActivity().setResult(Activity.RESULT_OK);*/

    }

    private void showFileDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(getString(R.string.file_chooser_message));
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.file_chooser_type_photo),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        listFiles.clear();
                        FilePickerBuilder.getInstance().setMaxCount(1)
                                .setSelectedFiles(listFiles)
                                .setActivityTheme(R.style.CustomAppCompatTheme)
                                .pickPhoto(getActivity());
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.file_chooser_type_doc),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        listFiles.clear();
                        FilePickerBuilder.getInstance().setMaxCount(1)
                                .setSelectedFiles(listFiles)
                                .setActivityTheme(R.style.CustomAppCompatTheme)
                                .pickFile(getActivity());
                    }
                });



        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 116) {

            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    // Here is object contains selected files names and path
                    selectedFilePath = object.path+object.names.get(0);


                    mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                    File myFile= new File(selectedFilePath);
                    fileSize = String.valueOf(myFile.length());

                    Log.e("MIME_TYPE", "is: "+ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
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
                        Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }*/

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    listFiles.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if(listFiles.size() > 0){
                        String fileNamePath = listFiles.get(0);
                        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/")+1);

                        selectedFilePath = fileNamePath;


                        mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                        File myFile= new File(selectedFilePath);
                        fileSize = String.valueOf(myFile.length());

                        Log.e("MIME_TYPE", "is: "+ ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
                        Log.e("FILE_SIZE", "is: "+fileSize);

                        long fileSizeInKB = myFile.length() / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if(fileSizeInMB <= 5) {
                            choosenFileTextView.setText(fileName);
                        }
                        else {
                            selectedFilePath = "";
                            mimeType = "";
                            fileSize = "";
                            Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    listFiles.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    if(listFiles.size() > 0){
                        String fileNamePath = listFiles.get(0);
                        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/")+1);

                        selectedFilePath = fileNamePath;


                        mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                        File myFile= new File(selectedFilePath);
                        fileSize = String.valueOf(myFile.length());

                        Log.e("MIME_TYPE", "is: "+ ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
                        Log.e("FILE_SIZE", "is: "+fileSize);

                        long fileSizeInKB = myFile.length() / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if(fileSizeInMB <= 5) {
                            choosenFileTextView.setText(fileName);
                        }
                        else {
                            selectedFilePath = "";
                            mimeType = "";
                            fileSize = "";
                            Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }

    }

    private void initAction() {

    }


    private void initApiCallCategory()
    {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        //AppRestClient.post(URLHelper.URL_LESSON_CATEGORY, params, lessonCategoryHandler);
        lessonCategory(params);
    }


    private void lessonCategory(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().lessonCategory(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        listCategory.clear();
                        uiHelper.dismissLoadingDialog();
                        if (response.body() != null){
                            Wrapper modelContainer = GsonParser.getInstance()
                                    .parseServerResponse2(response.body());



                            if (modelContainer.getStatus().getCode() == 200) {


                                JsonArray arrayCategory = modelContainer.getData().get("category").getAsJsonArray();
                                for (int i = 0; i < parseCategory(arrayCategory.toString()).size(); i++)
                                {
                                    listCategory.add(parseCategory(arrayCategory.toString()).get(i));
                                }

                                showCategoryPopup(btnSelectCategory);


                            }
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
    AsyncHttpResponseHandler lessonCategoryHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listCategory.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayCategory = modelContainer.getData().get("category").getAsJsonArray();
                for (int i = 0; i < parseCategory(arrayCategory.toString()).size(); i++)
                {
                    listCategory.add(parseCategory(arrayCategory.toString()).get(i));
                }

                showCategoryPopup(btnSelectCategory);


            } else {

            }


        }

        ;
    };


    private ArrayList<LessonPlanCategory> parseCategory(String object) {
        ArrayList<LessonPlanCategory> data = new ArrayList<LessonPlanCategory>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlanCategory>>() {}.getType());
        return data;
    }

    private void showCategoryPopup(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());

        //popup.getMenu().add("All");

        for(int i=0;i<listCategory.size();i++)
            popup.getMenu().add(listCategory.get(i).getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                for(LessonPlanCategory b : listCategory)
                {
                    if(item.getTitle().toString().equalsIgnoreCase(b.getName()))
                        selectedCategoryId = b.getId();
                }

                Log.e("ITEM_ID", "id: " + selectedCategoryId);

                txtSelectCategory.setText(item.getTitle());

                return true;
            }
        });

        popup.show();
    }



    private void initApiCallSubject()
    {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        //AppRestClient.post(URLHelper.URL_LESSON_SUBJECT, params, lessonSubjectHandler);
        lessonSubject(params);
    }


    private void lessonSubject(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().lessonSubject(params).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //listSubject.clear();
                uiHelper.dismissLoadingDialog();
                if (response.body() != null){
                    Wrapper modelContainer = GsonParser.getInstance()
                            .parseServerResponse2(response.body());



                    if (modelContainer.getStatus().getCode() == 200) {


                        JsonArray arraySubject = modelContainer.getData().get("subjects").getAsJsonArray();
                        for (int i = 0; i < parseSubject(arraySubject.toString()).size(); i++)
                        {
                            listSubject.add(parseSubject(arraySubject.toString()).get(i));
                        }

                        //showSubjectPopup(btnSubjectClass);
                        generateSubjectChooserLayout(layoutSelectMultipleSubject);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                uiHelper.showMessage(getString(R.string.internet_error_text));
                if (uiHelper.isDialogActive()) {
                    uiHelper.dismissLoadingDialog();
                }
            }
        });
    }
    AsyncHttpResponseHandler lessonSubjectHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            //listSubject.clear();


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arraySubject = modelContainer.getData().get("subjects").getAsJsonArray();
                for (int i = 0; i < parseSubject(arraySubject.toString()).size(); i++)
                {
                    listSubject.add(parseSubject(arraySubject.toString()).get(i));
                }

                //showSubjectPopup(btnSubjectClass);
                generateSubjectChooserLayout(layoutSelectMultipleSubject);




            } else {

            }


        }

        ;
    };

    private ArrayList<Subject> parseSubject(String object) {
        ArrayList<Subject> data = new ArrayList<Subject>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<Subject>>() {}.getType());
        return data;
    }

    private void showSubjectPopup(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());

        //popup.getMenu().add("All");

        for(int i=0;i<listSubject.size();i++)
            popup.getMenu().add(listSubject.get(i).getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                for(Subject b : listSubject)
                {
                    if(item.getTitle().toString().equalsIgnoreCase(b.getName()))
                        selectedSubjectId = b.getId();
                }

                Log.e("ITEM_ID", "id: " + selectedSubjectId);

                txtSubjectClass.setText(item.getTitle());

                return true;
            }
        });

        popup.show();
    }



    private void showDatepicker() {
        DatePickerFragment picker = new DatePickerFragment();
        picker.setCallbacks(datePickerCallback);
        picker.show(getFragmentManager(), "datePicker");
    }

    DatePickerFragment.DatePickerOnSetDateListener datePickerCallback = new DatePickerFragment.DatePickerOnSetDateListener() {

        @Override
        public void onDateSelected(int month, String monthName, int day,
                                   int year, String dateFormatServer, String dateFormatApp,
                                   Date date) {
            // TODO Auto-generated method stub
            txtLectureDate.setText(dateFormatApp);
            selectedDate = dateFormatServer;
            Log.e("DATE", "selecetd: " + selectedDate);
            //dateFormatServerString = dateFormatServer;
        }

    };


    private void initApiCallAdd(boolean isShow)
    {
        if (!uiHelper.isDialogActive())
            uiHelper.showLoadingDialog(getString(R.string.loading_text));

        if(!TextUtils.isEmpty(selectedFilePath)){

            File myFile= new File(selectedFilePath);
            /*me.addPart("attachment_file_name", myFile, false);
            Log.e("FILE_NAME", "is: " + myFile.toString());
            if(!TextUtils.isEmpty(mimeType)){
                me.addPart("mime_type", mimeType, false);
            }
            if(!TextUtils.isEmpty(fileSize)){
                me.addPart("file_size", fileSize, false);
            }*/


            RequestBody user_secret = RequestBody.create(MediaType.parse("multipart/form-data"), UserHelper.getUserSecret());
            RequestBody subject_ids = RequestBody.create(MediaType.parse("multipart/form-data"), getIdWithComma());
            RequestBody categoryId = RequestBody.create(MediaType.parse("multipart/form-data"), selectedCategoryId);
            RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"),  txtTitle.getText().toString());
            RequestBody publisDate = RequestBody.create(MediaType.parse("multipart/form-data"), selectedDate);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), txtDescription.getText().toString());
            RequestBody mime_type;
            if(!TextUtils.isEmpty(mimeType)){
                mime_type = RequestBody.create(MediaType.parse("multipart/form-data"), mimeType);
            }else {
                mime_type = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            }
            RequestBody file_size = RequestBody.create(MediaType.parse("multipart/form-data"), fileSize);

            RequestBody is_Show;
            if(isShow == true){
                is_Show = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
            }else {
                is_Show = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
            }

            // create RequestBody instance from file
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("attachment_file_name", myFile.getName(), requestFile);


            ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddLessonplan(user_secret, subject_ids, categoryId, title, publisDate, content, is_Show, body, mime_type, file_size).enqueue(
                    new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            listSubject.clear();
                            uiHelper.dismissLoadingDialog();
                            if (response.body() != null){
                                Wrapper modelContainer = GsonParser.getInstance()
                                        .parseServerResponse2(response.body());



                                if (modelContainer.getStatus().getCode() == 200) {

                                    Toast.makeText(getActivity(), R.string.java_leaveapplicationfragment_successfully_added_lesson_plan, Toast.LENGTH_SHORT).show();
                                    clearDataFields();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            if (uiHelper.isDialogActive())
                                uiHelper.dismissLoadingDialog();
                        }
                    }
            );

        } else {

            String is_Show;
            if(isShow == true){
                is_Show = "1";
            } else {
                is_Show = "0";
            }
            ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddLessonplan(UserHelper.getUserSecret(), getIdWithComma(), selectedCategoryId, txtTitle.getText().toString(), selectedDate, txtDescription.getText().toString(), is_Show).enqueue(
                    new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            listSubject.clear();

                            uiHelper.dismissLoadingDialog();
                            if (response.body() != null){
                                Wrapper modelContainer = GsonParser.getInstance()
                                        .parseServerResponse2(response.body());



                                if (modelContainer.getStatus().getCode() == 200) {

                                    Toast.makeText(getActivity(), R.string.java_leaveapplicationfragment_successfully_added_lesson_plan, Toast.LENGTH_SHORT).show();
                                    clearDataFields();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            if (uiHelper.isDialogActive())
                                uiHelper.dismissLoadingDialog();
                        }
                    }
            );
        }
/*        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        if(!TextUtils.isEmpty(getIdWithComma()))
            params.put("subject_ids", getIdWithComma());

        params.put("lessonplan_category_id", selectedCategoryId);
        params.put("title", txtTitle.getText().toString());

        if(!TextUtils.isEmpty(txtLectureDate.getText().toString()))
            params.put("publish_date", selectedDate);

        params.put("content", txtDescription.getText().toString());

        if(isShow)
        {
            params.put("is_show", "1");
        }
        else
        {
            params.put("is_show", "0");
        }

        if(!TextUtils.isEmpty(selectedFilePath))
        {
            myFile= new File(selectedFilePath);
            try {
                params.put("attachment_file_name", myFile);

                Log.e("FILE_NAME", "is: " + myFile.toString());
            } catch(FileNotFoundException e) {e.printStackTrace();}
        }

        if(!TextUtils.isEmpty(mimeType)){
            params.put("mime_type", mimeType);
        }
        if(!TextUtils.isEmpty(fileSize)){
            params.put("file_size", fileSize);
        }



        AppRestClient.post(URLHelper.URL_LESSON_ADD, params, lessonAddHandler);*/
    }



    private boolean isValidData()
    {
        boolean result = true;

        if(TextUtils.isEmpty(txtTitle.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_insert_lesson_plan_title));
        }

        else if(TextUtils.isEmpty(txtSelectCategory.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_select_lesson_plan_category));
        }

        else if(TextUtils.isEmpty(txtDescription.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_insert_lesson_plan_description));
        }



        return result;
    }

    private void showToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    AsyncHttpResponseHandler lessonAddHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listSubject.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {

                Toast.makeText(getActivity(), R.string.java_leaveapplicationfragment_successfully_added_lesson_plan, Toast.LENGTH_SHORT).show();
                clearDataFields();


            } else {

            }


        }

        ;
    };


    private void generateSubjectChooserLayout(LinearLayout layout)
    {

        for (int i=0;i<listSubject.size();i++)
        {
            CheckBox cb = new CheckBox(getActivity());
            cb.setPadding(5, 5, 5, 5);
            cb.setTag(i);
            cb.setButtonDrawable(R.drawable.check_btn);
            cb.setText(listSubject.get(i).getName());
            cb.setTextColor(Color.BLACK);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox btn = (CheckBox)buttonView;
                    int tag = (Integer)btn.getTag();

                    if(isChecked)
                    {
                        listSubjectId.add(listSubject.get(tag).getId());
                        refreshData(listSubjectId);
                        getIdWithComma();


                        Log.e("LessonPlan", "onCheckedChanged: "+listSubject.get(tag).getName() );
                        listSubjectName.add(listSubject.get(tag).getName());
                        refreshData(listSubjectName);
                        getNameWithComma();
                    }
                    else
                    {
                        listSubjectId.remove(listSubject.get(tag).getId());
                        getIdWithComma();

                        listSubjectName.remove(listSubject.get(tag).getName());
                        getNameWithComma();
                    }

                    txtSubjectClass.setText(getNameWithComma());

                    Log.e("SUB_ID", "is: " + getIdWithComma());

                }


            });


           //if(i == listSubjectName.indexOf(""))
            
           layout.addView(cb);
        }

    }

    private String getIdWithComma() {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectId )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }

    private String getNameWithComma() {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectName )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }

    private void refreshData(List<String> list)
    {
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
    }

    private void clearDataFields(){
        txtTitle.setText("");
        txtSelectCategory.setText(getString(R.string.fragment_lessonplan_view_txt_select_category));
        txtSubjectClass.setText(getString(R.string.fragment_lessonplan_add_txt_hint_select_subject_and_class));
        txtSubjectClass.setText(getString(R.string.fragment_lessonplan_add_txt_hint_select_subject_and_class));
        txtLectureDate.setText(getString(R.string.fragment_lessonplan_add_txt_hint_select_lecture_date));
        choosenFileTextView.setText(getString(R.string.java_singleteacheredithomeworkactivity_no_file_attached));
        txtDescription.setText("");

    }

    @Override
    public void onAttachCallBack(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == AppConstant.REQUEST_CODE_TEACHER_ADD_LESSON_PLAN) {
            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    // Here is object contains selected files names and path
                    selectedFilePath = object.path+object.names.get(0);


                    mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                    File myFile= new File(selectedFilePath);
                    fileSize = String.valueOf(myFile.length());

                    Log.e("MIME_TYPE", "is: "+ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
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
                        Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }*/

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    listFiles.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if(listFiles.size() > 0){
                        String fileNamePath = listFiles.get(0);
                        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/")+1);

                        selectedFilePath = fileNamePath;


                        mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                        File myFile= new File(selectedFilePath);
                        fileSize = String.valueOf(myFile.length());

                        Log.e("MIME_TYPE", "is: "+ ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
                        Log.e("FILE_SIZE", "is: "+fileSize);

                        long fileSizeInKB = myFile.length() / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if(fileSizeInMB <= 5) {
                            choosenFileTextView.setText(fileName);
                        }
                        else {
                            selectedFilePath = "";
                            mimeType = "";
                            fileSize = "";
                            Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    listFiles.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    if(listFiles.size() > 0){
                        String fileNamePath = listFiles.get(0);
                        String fileName = fileNamePath.substring(fileNamePath.lastIndexOf("/")+1);

                        selectedFilePath = fileNamePath;


                        mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
                        File myFile= new File(selectedFilePath);
                        fileSize = String.valueOf(myFile.length());

                        Log.e("MIME_TYPE", "is: "+ ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
                        Log.e("FILE_SIZE", "is: "+fileSize);

                        long fileSizeInKB = myFile.length() / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if(fileSizeInMB <= 5) {
                            choosenFileTextView.setText(fileName);
                        }
                        else {
                            selectedFilePath = "";
                            mimeType = "";
                            fileSize = "";
                            Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }

        instance = null;
    }
}
