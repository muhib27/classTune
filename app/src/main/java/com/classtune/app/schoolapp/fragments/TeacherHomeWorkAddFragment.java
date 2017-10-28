package com.classtune.app.schoolapp.fragments;

import android.Manifest;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.IAttachFile;
import com.classtune.app.schoolapp.fragments.DatePickerFragment.DatePickerOnSetDateListener;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.StudentAssociated;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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

import static android.app.Activity.RESULT_OK;

public class TeacherHomeWorkAddFragment extends Fragment implements
		OnClickListener, IAttachFile {

	View rootView;
	private UIHelper uiHelper;
	EditText subjectEditText, homeworkDescriptionEditText;
	private List<Subject> subjectCats;
	private List<BaseType> homeworkTypeCats;
	TextView subjectNameTextView, homeWorkTypeTextView, choosenFileTextView;
	private String subjectId="", homeworkTypeId="1";
	private String selectedFilePath = "";
	private TextView choosenDateTextView;
	private final static int REQUEST_CODE_FILE_CHOOSER = 5;
	private String dateFormatServerString = "";
	
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
	public static TeacherHomeWorkAddFragment instance;
	private List<CheckBox> listCheckBoxSubject;
	private ArrayList<String> listFiles;
	private Switch switchToggle;
	private String switchStatus = "NO";
	private CheckBox lastSelectedButton = null;
	private List<StudentAssociated> listStudent;
	private List<String> listStudentIds;

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
		listCheckBoxSubject = new ArrayList<>();
		listFiles = new ArrayList<>();
		listStudent = new ArrayList<>();
        listStudentIds = new ArrayList<>();
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
		if(homeworkDescriptionEditText.getText().toString().trim().equals("")){
			Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_homework_description_cannot_empty), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(dateFormatServerString.equals("")){
			Toast.makeText(getActivity(), getString(R.string.java_singleteacheredithomeworkactivity_choose_due_date), Toast.LENGTH_SHORT).show();
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
			RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), homeworkDescriptionEditText
					.getText().toString());
			RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), subjectEditText.getText()
					.toString());
			RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), homeworkTypeId);
			RequestBody duedate = RequestBody.create(MediaType.parse("multipart/form-data"), dateFormatServerString);
			RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), "id");
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

			RequestBody studentIds = RequestBody.create(MediaType.parse("multipart/form-data"), getStudentIdWithComma());


				ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddHomework(user_secret, subject_id, content, title, type, duedate, body, mime_type, file_size, is_draft, studentIds).enqueue(
						new Callback<JsonElement>() {
							@Override
							public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
								if (uiHelper.isDialogActive())
									uiHelper.dismissLoadingDialog();

								if (response.body() != null){
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
				ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddHomework(UserHelper.getUserSecret(), subjectId, homeworkDescriptionEditText
						.getText().toString(), subjectEditText.getText().toString(), homeworkTypeId, dateFormatServerString, "1").enqueue(
						new Callback<JsonElement>() {
							@Override
							public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
								if (uiHelper.isDialogActive())
									uiHelper.dismissLoadingDialog();
								if (response.body() != null){
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

							}

							@Override
							public void onFailure(Call<JsonElement> call, Throwable t) {
								if (uiHelper.isDialogActive())
									uiHelper.dismissLoadingDialog();
							}
						}
				);

			}else {
				ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAddHomework(UserHelper.getUserSecret(), subjectId, homeworkDescriptionEditText
						.getText().toString(), subjectEditText.getText().toString(), homeworkTypeId, dateFormatServerString, "0").enqueue(
						new Callback<JsonElement>() {
							@Override
							public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
								if (uiHelper.isDialogActive())
									uiHelper.dismissLoadingDialog();

								if (response.body() != null){
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
		/*RequestParams params = new RequestParams();

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		Log.e("Subject id with coma", "PublishHomeWork: "+subjectId );
		params.put(RequestKeyHelper.SUBJECT_ID, subjectId);
		params.put(RequestKeyHelper.CONTENT, homeworkDescriptionEditText
				.getText().toString());
		params.put(RequestKeyHelper.SUBJECT_TITLE, subjectEditText.getText()
				.toString());
		params.put(RequestKeyHelper.TYPE, homeworkTypeId);
		params.put(RequestKeyHelper.HOMEWORK_DUEDATE, dateFormatServerString);

		//Log.e("addhomwork", "PublishHomeWork: "+ subjectId+ " "+classworkDescriptionEditText.getText() +" "+ subjectEditText + " "+homeworkTypeId + " "+ dateFormatServerString);
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



		AppRestClient.post(URLHelper.URL_TEACHER_ADD_HOMEWORK, params,
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
										R.string.java_teacherhomeworkaddfragment_successfully_posted_homework,
										Toast.LENGTH_SHORT).show();
							}



							clearDataFields();
						} else
							Toast.makeText(
									getActivity(),
									getString(R.string.java_singleteacheredithomeworkactivity_failed_post),
									Toast.LENGTH_SHORT).show();
						super.onSuccess(arg0, responseString);
					}
				});*/

	}




	private void clearDataFields()
    {
        subjectNameTextView.setText("");
        homeWorkTypeTextView.setText("");
        subjectEditText.setText("");
        homeworkDescriptionEditText.setText("");
        choosenFileTextView.setText(getString(R.string.java_singleteacheredithomeworkactivity_no_file_attached));

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        choosenDateTextView.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

		for(int i=0;i<listCheckBoxSubject.size();i++){
			listCheckBoxSubject.get(i).setChecked(false);
		}
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_teacher_add_homework,
				container, false);
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
		homeworkTypeCats = new ArrayList<>();
		homeworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_regular), "1"));
		homeworkTypeCats.add(new TypeHomeWork(getString(R.string.java_singleteacheredithomeworkactivity_project), "2"));
	}

	private void fetchSubject() {

		HashMap<String,String> params = new HashMap<>();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if(!uiHelper.isDialogActive())
			uiHelper.showLoadingDialog(getString(R.string.loading_text));
		ApplicationSingleton.getInstance().getNetworkCallInterface().teacherHomeworkSubject(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

							uiHelper.dismissLoadingDialog();
						if (response.body() != null){
							Log.e("GET_SUBJECT_SUCCESS", ""+response.body());
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

					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {

					}
				}
		);

		/*AppRestClient.post(URLHelper.URL_TEACHER_GET_SUBJECT, params,
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


		layoutAttachmentHolder = (LinearLayout)view.findViewById(R.id.layoutAttachmentHolder);

		homeworkDescriptionEditText = (EditText) view
				.findViewById(R.id.et_teacher_ah_homework_description);
		subjectNameTextView = (TextView) view
				.findViewById(R.id.tv_teacher_ah_subject_name);
		homeWorkTypeTextView = (TextView) view
				.findViewById(R.id.tv_teacher_ah_homework_type);
		choosenFileTextView = (TextView) view
				.findViewById(R.id.tv_teacher_ah_choosen_file_name);
		choosenDateTextView = (TextView) view
				.findViewById(R.id.tv_teacher_ah_date);
		((ImageButton) view.findViewById(R.id.btn_subject_name))
				.setOnClickListener(this);
		((ImageButton) view.findViewById(R.id.btn_classwork_type))
				.setOnClickListener(this);
		layoutSelectMultipleSubject = (LinearLayout)view.findViewById(R.id.layoutSelectMultipleSubject);
		layoutSubjectClassActionHolder = (LinearLayout)view.findViewById(R.id.layoutSelectType);
		layoutSelectMultipleSubject.setBackgroundColor(Color.parseColor("#eff0f4"));
		layoutSubjectClassActionHolder.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

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



			}
		});

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




		((CustomButton) view.findViewById(R.id.btn_teacher_ah_due_date))
				.setOnClickListener(this);
		((Button) view.findViewById(R.id.btn_publish_homework))
				.setOnClickListener(this);

		((Button) view.findViewById(R.id.btn_save_draft_homework))
				.setOnClickListener(this);

		layoutDate = (LinearLayout)view.findViewById(R.id.layoutDate);
		layoutDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatepicker();
			}
		});
		
		
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		
		choosenDateTextView.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

		layoutSelectSubject = (LinearLayout)view.findViewById(R.id.layoutSelectSubject);
		layoutSelectSubject.setOnClickListener(this);

		layoutSelectType = (LinearLayout)view.findViewById(R.id.layoutSelectType);
		layoutSelectType.setOnClickListener(this);

		layoutSelectSubject = (LinearLayout)view.findViewById(R.id.layoutSelectSubject);

		switchToggle = (Switch) view.findViewById(R.id.switchToggle);
		toggleManagment();

	}
	private void generateSubjectChooserLayout(LinearLayout layout)
	{

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

						lastSelectedButton = btn;

						if(switchStatus.equalsIgnoreCase("YES")){
							for(int i=0;i<listCheckBoxSubject.size();i++){
								listCheckBoxSubject.get(i).setChecked(false);
							}

							lastSelectedButton.setChecked(true);

							showMultipleStudentDialog(subjectCats.get(tag).getId());
						}



					}
					else
					{
						listSubjectId.remove(subjectCats.get(tag).getId());
						getIdWithComma();

						listSubjectName.remove(subjectCats.get(tag).getText());
						getNameWithComma();

						//lastSelectedButton = null;
					}

					subjectNameTextView.setText(getNameWithComma());
					subjectId = getIdWithComma();
					Log.e("SUB_ID", "is: " + getIdWithComma());



				}


			});

			//if(i == listSubjectName.indexOf(""))

			listCheckBoxSubject.add(cb);
			layout.addView(cb);
		}



		//workign with toggling
		//toggleManagement();

	}

	private void toggleManagment(){
		switchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(switchStatus.equals("NO")){
					switchStatus = "YES";
				}else {
					switchStatus = "NO";
				}

				Log.e("TOGGLE", "is: "+switchStatus);

				toggleSelection();
			}


		});


	}


	private void toggleSelection(){

		for (int i = 0; i < listCheckBoxSubject.size(); i++) {
			listCheckBoxSubject.get(i).setChecked(false);
		}

	}

	private void showMultipleStudentDialog(final String studentId){

		initApiCallStudentSelection(studentId);
	}

	private void initApiCallStudentSelection(final String subjectId){
		listStudent.clear();
        listStudentIds.clear();

		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().showStudentList(UserHelper.getUserSecret(), subjectId).enqueue(new Callback<JsonElement>() {
			@Override
			public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

				uiHelper.dismissLoadingDialog();
				Log.e("***RESPONSE***", "is: "+new Gson().toJson(response));
				if(response.body()!=null && response.isSuccessful()){


					String strObject = ApplicationSingleton.getInstance().convertJsonElementToString(response.body());
					try {
						JSONObject jsonObject = new JSONObject(strObject);

						JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("students");
						listStudent.addAll(convertStudentArrayList(jsonArray));
						if(listStudent.size() > 0)
							showDialog(listStudent);
						else
							Toast.makeText(getActivity(), R.string.fragment_archieved_events_txt_no_data_found, Toast.LENGTH_SHORT).show();

					} catch (JSONException e) {
						e.printStackTrace();
					}



				}
			}

			@Override
			public void onFailure(Call<JsonElement> call, Throwable t) {
				t.printStackTrace();
				uiHelper.dismissLoadingDialog();
			}
		});
	}

	private List<StudentAssociated> convertStudentArrayList(JSONArray jsonArray){
		Gson gson = new Gson();
		Type type = new TypeToken<List<StudentAssociated>>(){}.getType();
		List<StudentAssociated> listStudent = gson.fromJson(jsonArray.toString(), type);
		return listStudent;
	}


	private void showDialog(final List<StudentAssociated> listStudent){
		final ArrayList seletedItems = new ArrayList();

		for(int i=0; i<listStudent.size(); i++){
			String data = "";
			if(TextUtils.isEmpty(listStudent.get(i).getRollNo())){
				data = listStudent.get(i).getStudentName();
			}else {
				data = listStudent.get(i).getStudentName()+"\n"+listStudent.get(i).getRollNo();
			}

			seletedItems.add(data);

		}




		/*seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");
		seletedItems.add("Easy\n"+"Roll 121231325545");*/


		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle("Select The Difficulty Level")
				.setMultiChoiceItems((CharSequence[])seletedItems.toArray(new CharSequence[0]), null, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
						if (isChecked) {
							// If the user checked the item, add it to the selected items
							seletedItems.add(indexSelected);
							listStudentIds.add(listStudent.get(indexSelected).getStudentId());
						} else if (seletedItems.contains(indexSelected)) {
							// Else, if the item is already in the array, remove it
							seletedItems.remove(Integer.valueOf(indexSelected));
							listStudentIds.remove(Integer.valueOf(indexSelected));
						}
					}
				}).setPositiveButton(R.string.java_uihelper_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//  Your code when user clicked on OK
						//  You can write the code  to save the selected item here
					}
				}).setNegativeButton(R.string.attachment_dialog_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//  Your code when user clicked on Cancel
					}
				}).create();
		dialog.show();
	}


	private String getStudentIdWithComma()
	{
		StringBuilder result = new StringBuilder();
		for ( String p : listStudentIds )
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
		picker.setData(type, cats, new PickerItemSelectedListener() {

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
					homeWorkTypeTextView.setText(data.getTypeName());
					homeworkTypeId = data.getTypeId();
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
			showPicker(PickerType.TEACHER_HOMEWORKTYPE, homeworkTypeCats,
					getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
			break;
			case R.id.layoutSelectType:
				showPicker(PickerType.TEACHER_HOMEWORKTYPE, homeworkTypeCats,
						getString(R.string.java_singleteacheredithomeworkactivity_select_homework_type));
				break;

		case R.id.btn_teacher_ah_attach_file:
			showChooser();
			break;
		case R.id.btn_teacher_ah_due_date:
			showDatepicker();
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
				break;

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
		Log.e("ACT_RES", ""+requestCode+" "+requestCode+" "+data);
		super.onActivityResult(requestCode, resultCode, data);

		/*if (requestCode == REQUEST_CODE_FILE_CHOOSER) {

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
				if(resultCode== RESULT_OK && data!=null) {
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
				if(resultCode== RESULT_OK && data!=null) {
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
		getActivity().startActivityForResult(intent, AppConstant.REQUEST_CODE_TEACHER_HOMEWORK_ATTACH_FILE);
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

	private void showDatepicker() {
		DatePickerFragment picker = new DatePickerFragment();
		picker.setCallbacks(datePickerCallback);
		picker.show(getFragmentManager(), "datePicker");
	}

	DatePickerOnSetDateListener datePickerCallback = new DatePickerOnSetDateListener() {

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
		Log.e("ACT_RES_INTERFACE", ""+requestCode+" "+requestCode+" "+data);
		/*if (requestCode == AppConstant.REQUEST_CODE_TEACHER_HOMEWORK_ATTACH_FILE) {

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
				if(resultCode== RESULT_OK && data!=null) {
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
				if(resultCode== RESULT_OK && data!=null) {
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
