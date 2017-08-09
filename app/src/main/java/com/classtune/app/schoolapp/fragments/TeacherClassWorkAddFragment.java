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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.IAttachFile;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Subject;
import com.classtune.app.schoolapp.model.TypeHomeWork;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.ArrayList;
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
 * A simple {@link Fragment} subclass.
 */
public class TeacherClassWorkAddFragment extends Fragment implements View.OnClickListener, IAttachFile {
    View rootView;
    private UIHelper uiHelper;
    EditText subjectEditText, classworkDescriptionEditText;
    private List<Subject> subjectCats;
    private List<BaseType> classworkTypeCats;
    TextView subjectNameTextView, classWorkTypeTextView, choosenFileTextView;
    private String subjectId="", classworkTypeId ="1";
    private String selectedFilePath = "";
    private final static int REQUEST_CODE_FILE_CHOOSER = 101;


    private LinearLayout layoutDate;
    private LinearLayout layoutAttachmentHolder;
    UserHelper userHelper;

    private LinearLayout layoutSelectSubject;
    private LinearLayout layoutSelectType;
    private String mimeType = "";
    private String fileSize = "";
    private LinearLayout layoutSelectMultipleSubject;
    private LinearLayout layoutSubjectClassActionHolder;
    private List<String> listSubjectId;
    private List<String> listSubjectName;
    private boolean  isSubjectLayoutClicked = false;
    public static TeacherClassWorkAddFragment instance;
    private List<CheckBox> listCheckSubject;
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
        subjectCats = new ArrayList<>();
        uiHelper = new UIHelper(getActivity());
        userHelper=new UserHelper(getActivity());
        listSubjectName = new ArrayList<>();
        listSubjectId = new ArrayList<>();
        listCheckSubject = new ArrayList<>();
        listFiles = new ArrayList<>();

    }

    private boolean isFormValid() {
        if(subjectId.equals("")){
            Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_choose_subject), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(subjectEditText.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_subject_title_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(classworkDescriptionEditText.getText().toString().trim().equals("")){
            Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_homework_description_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    File myFile;
    public void PublishHomeWork(final boolean isForDraft) {
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
            RequestBody subject_id = RequestBody.create(MediaType.parse("multipart/form-data"), subjectId);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), classworkDescriptionEditText
                    .getText().toString());
            RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), subjectEditText.getText()
                    .toString());
            RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), classworkTypeId);

            RequestBody mime_type;
            if(!TextUtils.isEmpty(mimeType)){
                mime_type = RequestBody.create(MediaType.parse("multipart/form-data"), mimeType);
            }else {
                mime_type = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            }
            RequestBody file_size = RequestBody.create(MediaType.parse("multipart/form-data"), fileSize);

            RequestBody is_draft;
            if(isForDraft == true){
                is_draft = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
            }else {
                is_draft = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
            }

            // create RequestBody instance from file
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("attachment_file_name", myFile.getName(), requestFile);


            ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddClasswork(user_secret, subject_id, content, title, type, body, mime_type, file_size, is_draft).enqueue(
                    new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (uiHelper.isDialogActive())
                                uiHelper.dismissLoadingDialog();

                            Log.e("SERVERRESPONSE", ""+ response.body());
                            Wrapper wrapper = GsonParser.getInstance()
                                    .parseServerResponse2(response.body());
                            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                                if(isForDraft == true)
                                {
                                    Toast.makeText(getActivity(),
                                            getString(R.string.java_singleteacheredithomeworkactivity_saved_as_draft),
                                            Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getActivity(),
                                            R.string.java_teacherhomeworkaddfragment_successfully_posted_homework,
                                            Toast.LENGTH_SHORT).show();
                                }



                                clearDataFields();
                            } else
                                Toast.makeText(
                                        getActivity(),
                                        getString(R.string.java_singleteacheredithomeworkactivity_failed_post),
                                        Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            if (uiHelper.isDialogActive())
                                uiHelper.dismissLoadingDialog();
                        }
                    }
            );
        }else {
            if(isForDraft == true)
            {
                ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddClasswork(UserHelper.getUserSecret(), subjectId, classworkDescriptionEditText
                        .getText().toString(), subjectEditText.getText().toString(), classworkTypeId, "1").enqueue(
                        new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                if (uiHelper.isDialogActive())
                                    uiHelper.dismissLoadingDialog();

                                Log.e("SERVERRESPONSE", ""+ response.body());
                                Wrapper wrapper = GsonParser.getInstance()
                                        .parseServerResponse2(response.body());
                                if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                                    if(isForDraft == true)
                                    {
                                        Toast.makeText(getActivity(),
                                                getString(R.string.java_singleteacheredithomeworkactivity_saved_as_draft),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),
                                                R.string.java_teacherhomeworkaddfragment_successfully_posted_homework,
                                                Toast.LENGTH_SHORT).show();
                                    }



                                    clearDataFields();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.java_singleteacheredithomeworkactivity_failed_post),
                                            Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                if (uiHelper.isDialogActive())
                                    uiHelper.dismissLoadingDialog();
                            }
                        }
                );

            }else {
                ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddClasswork(UserHelper.getUserSecret(), subjectId, classworkDescriptionEditText
                        .getText().toString(), subjectEditText.getText().toString(), classworkTypeId, "0").enqueue(
                        new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                if (uiHelper.isDialogActive())
                                    uiHelper.dismissLoadingDialog();

                                Log.e("SERVERRESPONSE", ""+ response.body());
                                Wrapper wrapper = GsonParser.getInstance()
                                        .parseServerResponse2(response.body());
                                if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                                    if(isForDraft == true)
                                    {
                                        Toast.makeText(getActivity(),
                                                getString(R.string.java_singleteacheredithomeworkactivity_saved_as_draft),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),
                                                R.string.java_teacherhomeworkaddfragment_successfully_posted_homework,
                                                Toast.LENGTH_SHORT).show();
                                    }



                                    clearDataFields();
                                } else
                                    Toast.makeText(
                                            getActivity(),
                                            getString(R.string.java_singleteacheredithomeworkactivity_failed_post),
                                            Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                if (uiHelper.isDialogActive())
                                    uiHelper.dismissLoadingDialog();
                            }
                        }
                );
            }

        }

      /*  RequestParams params = new RequestParams();

        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        Log.e("Subject id with coma", "PublishHomeWork: "+subjectId );
        params.put(RequestKeyHelper.SUBJECT_ID, subjectId);
        params.put(RequestKeyHelper.CONTENT, classworkDescriptionEditText
                .getText().toString());
        params.put(RequestKeyHelper.SUBJECT_TITLE, subjectEditText.getText()
                .toString());
        params.put(RequestKeyHelper.TYPE, classworkTypeId);


        Log.e("addhomwork", "PublishHomeWork: "+ subjectId+ " "+ classworkDescriptionEditText.getText().toString() +" "+ subjectEditText.getText().toString() + " "+ classworkTypeId);
        if(isForDraft == true)
        {
            params.put("is_draft", "1");

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

                            if(isForDraft == true)
                            {
                                Toast.makeText(getActivity(),
                                        getString(R.string.java_singleteacheredithomeworkactivity_saved_as_draft),
                                        Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getActivity(),
                                        R.string.java_teacherclassworkaddfragment_successfully_posted_classwork,
                                        Toast.LENGTH_SHORT).show();
                            }
                            clearDataFields();
                        } else
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.java_singleteachereditclassworkactivity_failed_post),
                                    Toast.LENGTH_SHORT).show();
                        super.onSuccess(arg0, responseString);
                    }
                });*/

    }



    private void clearDataFields() {
        subjectNameTextView.setText("");
        classWorkTypeTextView.setText("");
        subjectEditText.setText("");
        classworkDescriptionEditText.setText("");
        choosenFileTextView.setText(getString(R.string.java_singleteacheredithomeworkactivity_no_file_attached));

        for(int i=0;i<listCheckSubject.size();i++){
            listCheckSubject.get(i).setChecked(false);
        }

    }


    public TeacherClassWorkAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_teacher_class_work_add, container, false);
        intiviews(rootView);
        createHomeworkTypeCats();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isStoragePermissionGranted();
        }
        isSubjectLayoutClicked = false;
        if(subjectCats!=null && subjectCats.size()>0)
            subjectCats.clear();
        return rootView;
    }
    private void createHomeworkTypeCats() {
        classworkTypeCats = new ArrayList<>();
        classworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_regular), "1"));
        classworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_project), "2"));
    }

    private void fetchSubject() {

        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        ApplicationSingleton.getInstance().getNetworkCallInterface().teacherClassworkSubject(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.e("GET_SUBJECT_SUCCESS", ""+ response.body());
                        Wrapper wrapper = GsonParser.getInstance()
                                .parseServerResponse2(response.body());
                        if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                            subjectCats.clear();
                            subjectCats.addAll(GsonParser.getInstance()
                                    .parseSubject(
                                            wrapper.getData().get("subjects")
                                                    .toString()));
                            generateSubjectChooserLayout(layoutSelectMultipleSubject);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                }
        );

   /*     AppRestClient.post(URLHelper.URL_TEACHER_CLASSWORK_GET_SUBJECT, params,
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
                            generateSubjectChooserLayout(layoutSelectMultipleSubject);
                        }
                    }
                });*/
    }

    private void intiviews(View view) {
        subjectEditText = (EditText) view
                .findViewById(R.id.et_teacher_ah_subject_name);

        layoutSubjectClassActionHolder = (LinearLayout)view.findViewById(R.id.layoutSelectSubject);


        layoutAttachmentHolder = (LinearLayout)view.findViewById(R.id.layoutAttachmentHolder);

        classworkDescriptionEditText = (EditText) view
                .findViewById(R.id.et_teacher_ah_classwork_description);
        subjectNameTextView = (TextView) view
                .findViewById(R.id.tv_teacher_ah_subject_name);
        classWorkTypeTextView = (TextView) view
                .findViewById(R.id.et_teacher_ah_subject_name);
        choosenFileTextView = (TextView) view
                .findViewById(R.id.tv_teacher_ah_choosen_file_name);
        ((ImageButton) view.findViewById(R.id.btn_subject_name))
                .setOnClickListener(this);
       /* ((ImageButton) view.findViewById(R.id.btn_classwork_type))
                .setOnClickListener(this);*/
        layoutSelectMultipleSubject = (LinearLayout)view.findViewById(R.id.layoutSelectMultipleSubject);
        layoutSelectMultipleSubject.setBackgroundColor(Color.parseColor("#eff0f4"));

        if(userHelper.getUser().getPaidInfo().getSchoolType() == 0)
        {
            layoutAttachmentHolder.setAlpha(.5f);
        }
        else
        {
            layoutAttachmentHolder.setAlpha(1f);
            ((CustomButton) view.findViewById(R.id.btn_teacher_ah_attach_file))
                    .setOnClickListener(this);
            ((LinearLayout)view.findViewById(R.id.layoutFileAttachRight)).setOnClickListener(this);
        }

		/*((CustomButton) view.findViewById(R.id.btn_teacher_ah_attach_file))
				.setOnClickListener(this);*/


        ((Button) view.findViewById(R.id.btn_save_draft_homework))
                .setOnClickListener(this);

        ((Button) view.findViewById(R.id.btn_publish_homework))
                .setOnClickListener(this);

        ((Button) view.findViewById(R.id.btn_save_draft_homework))
                .setOnClickListener(this);


        layoutSelectSubject = (LinearLayout)view.findViewById(R.id.layoutSelectSubject);
        layoutSelectSubject.setOnClickListener(this);

        layoutSelectSubject = (LinearLayout)view.findViewById(R.id.layoutSelectSubject);

    }
    private void generateSubjectChooserLayout(LinearLayout layout)
    {
        listCheckSubject.clear();
        for (int i=0;i<subjectCats.size();i++)
        {
            CheckBox cb = new CheckBox(getActivity());
            cb.setPadding(5, 5, 5, 5);
            cb.setTag(i);
            cb.setButtonDrawable(R.drawable.check_btn);
            cb.setText(subjectCats.get(i).getText());
            cb.setTextColor(Color.BLACK);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox btn = (CheckBox)buttonView;
                    int tag = (Integer)btn.getTag();

                    if(isChecked)
                    {
                        listSubjectId.add(subjectCats.get(tag).getId());
                        refreshData(listSubjectId);
                        getIdWithComma();
                        listSubjectName.add(subjectCats.get(tag).getText());
                        refreshData(listSubjectName);
                        getNameWithComma();
                    }
                    else
                    {
                        listSubjectId.remove(subjectCats.get(tag).getId());
                        getIdWithComma();

                        listSubjectName.remove(subjectCats.get(tag).getText());
                        getNameWithComma();
                    }

                    subjectNameTextView.setText(getNameWithComma());
                    subjectId = getIdWithComma();
                    Log.e("SUB_ID", "is: " + getIdWithComma());

                }


            });

            //if(i == listSubjectName.indexOf(""))

            layout.addView(cb);
            listCheckSubject.add(cb);
        }

    }

    private void refreshData(List<String> list)
    {
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
    }
    private String getIdWithComma()
    {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectId )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }

    private String getNameWithComma()
    {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectName )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
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
                        classworkTypeId = data.getTypeId();
                        break;
                    default:
                        break;
                }

            }
        }, title);
        picker.show(getChildFragmentManager(), null);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_subject_name:
			/*showPicker(PickerType.TEACHER_SUBJECT, subjectCats,
					getString(R.string.java_singleteacheredithomeworkactivity_select_subject));*/
                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);

                    if(subjectCats.size() <=0)
                        fetchSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);


                }
                break;
            case R.id.layoutSelectSubject:
			/*showPicker(PickerType.TEACHER_SUBJECT, subjectCats,
					getString(R.string.java_singleteacheredithomeworkactivity_select_subject));*/
                //generateSubjectChooserLayout(layoutSelectMultipleSubject);
                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);

                    if(subjectCats.size() <=0)
                        fetchSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_classwork_type:
                showPicker(PickerType.TEACHER_HOMEWORKTYPE, classworkTypeCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
                break;
            case R.id.layoutSelectType:
                showPicker(PickerType.TEACHER_HOMEWORKTYPE, classworkTypeCats,
                        getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
                break;

            case R.id.btn_teacher_ah_attach_file:
                showChooser();
                break;
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
            case R.id.layoutFileAttachRight:
                showChooser();

            default:
                break;
        }
    }

	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CODE_FILE_CHOOSER:
				if (resultCode == getActivity().RESULT_OK) {
					if (data != null) {
						// Get the URI of the selected file
						final Uri uri = data.getData();
					*//*if (uri.getLastPathSegment().endsWith("doc")
							|| uri.getLastPathSegment().endsWith("docx")
							|| uri.getLastPathSegment().endsWith("pdf")) {
						try {
							// Get the file path from the URI
							final String path = FileUtils.getPath(
									getActivity(), uri);
							selectedFilePath = path;
							choosenFileTextView
									.setText(getFileNameFromPath(selectedFilePath));
							mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
							File myFile= new File(selectedFilePath);
							fileSize = String.valueOf(myFile.length());
							Log.e("MIME_TYPE", "is: "+ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
							Log.e("FILE_SIZE", "is: "+fileSize);
						} catch (Exception e) {
							Log.e("FileSelectorTestAtivity",
									"File select error", e);
						}
					} else {
						Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_invalid_file_type),
								Toast.LENGTH_SHORT).show();
					}*//*


						try {
							// Get the file path from the URI
							final String path = FileUtils.getPath(
									getActivity(), uri);
							selectedFilePath = path;


							mimeType = ApplicationSingleton.getInstance().getMimeType(selectedFilePath);
							File myFile= new File(selectedFilePath);
							fileSize = String.valueOf(myFile.length());

							Log.e("MIME_TYPE", "is: "+ApplicationSingleton.getInstance().getMimeType(selectedFilePath));
							Log.e("FILE_SIZE", "is: "+fileSize);

							long fileSizeInKB = myFile.length() / 1024;
							long fileSizeInMB = fileSizeInKB / 1024;

							if(fileSizeInMB <= 5) {
								choosenFileTextView.setText(getFileNameFromPath(selectedFilePath));
							}
							else {
								selectedFilePath = "";
								mimeType = "";
								fileSize = "";
								Toast.makeText(getActivity(), R.string.java_teacherhomeworkaddfragment_file_size_message, Toast.LENGTH_SHORT).show();
							}



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
        /*Intent intent = new Intent(getActivity(), ru.bartwell.exfilepicker.ExFilePickerActivity.class);
        intent.putExtra(ExFilePicker.SET_START_DIRECTORY, "/");
        intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
        intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);
        intent.putExtra(ExFilePicker.DISABLE_SORT_BUTTON, true);
        intent.putExtra(ExFilePicker.ENABLE_QUIT_BUTTON, true);
        getActivity().startActivityForResult(intent, AppConstant.REQUEST_CODE_TEACHER_CLASSWORK_ATTACH_FILE);
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

    private String getFileNameFromPath(String path) {
        String[] tokens = path.split("/");
        return tokens[tokens.length - 1];
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

    @Override
    public void onAttachCallBack(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == AppConstant.REQUEST_CODE_TEACHER_CLASSWORK_ATTACH_FILE) {

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
