package com.classtune.app.schoolapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.CompleteProfileActivityContainer;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.callbacks.onGradeDialogButtonClickListener;
import com.classtune.app.schoolapp.fragments.DatePickerFragment.DatePickerOnSetDateListener;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.City;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.UserWrapper;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.CustomDateTimePicker;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.ActionItem;
import com.classtune.app.schoolapp.viewhelpers.GradeDialog;
import com.classtune.app.schoolapp.viewhelpers.QuickAction;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileFragmentTeacher extends Fragment implements
		OnFocusChangeListener, OnClickListener, UserAuthListener {

	private RadioButton radioFirstName, radioMiddleName, radioLastName;
	private EditText etFirstName, etMiddleName, etLastName, etMobile, etSchool,etTeachingForYears,etTeachingForMonth;
	private TextView tvCountryCode, tvCountry, tvCity, tvMonth, tvday,
			tvYear, tvMedium;
	private Button btnBirthday, btnMedium, btnSelectGrade, btnSave, btnCity;
	private RadioGroup rGGender;
	private RadioButton rBMale, rBFemale;

	private static final int ID_BANGLA = 1;
	private static final int ID_ENGLISH = 2;

	ApplicationSingleton app;
	UIHelper uiHelper;
	UserHelper userHelper;
	String country = "", state = "", city = "", countryCode = "",
			mobileNumber = "", monthStr = "", dateStr = "", yearStr = "";
	QuickAction mQuickAction;
	private User user;
	private View view;
	private String gradeStr = "";
	private List<Integer> selectedGrades;
	private int selectedMediumId = 0;

	private List<BaseType> cities;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cities = new ArrayList<BaseType>();
		String[] myResArray = getResources().getStringArray(R.array.city_list);
		for (String string : myResArray) {
			City city = new City();
			city.setName(string);
			cities.add(city);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/** Inflating the layout for this fragment **/
		view = inflater.inflate(R.layout.complete_profile_teacher_fragment,
				null);
		radioFirstName = (RadioButton) view.findViewById(R.id.radio_first);
		radioMiddleName = (RadioButton) view.findViewById(R.id.radio_middle);
		radioLastName = (RadioButton) view.findViewById(R.id.radio_last);
		etFirstName = (EditText) view.findViewById(R.id.et_first);
		etMiddleName = (EditText) view.findViewById(R.id.et_middle);
		etLastName = (EditText) view.findViewById(R.id.et_last);
		etMobile = (EditText) view.findViewById(R.id.et_mobile);
		etSchool = (EditText) view.findViewById(R.id.et_school_name);
		tvCountryCode = (TextView) view.findViewById(R.id.tv_country_code);
		tvCountry = (TextView) view.findViewById(R.id.tv_country);
		tvCity = (TextView) view.findViewById(R.id.tv_city);
		tvMonth = (TextView) view.findViewById(R.id.tv_month);
		tvday = (TextView) view.findViewById(R.id.tv_day);
		tvYear = (TextView) view.findViewById(R.id.tv_year);
		tvMedium = (TextView) view.findViewById(R.id.tv_medium);
		btnMedium = (Button) view.findViewById(R.id.btn_medium);
		btnSelectGrade = (Button) view.findViewById(R.id.btn_select_grade);
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnCity = (Button) view.findViewById(R.id.btn_city);
		btnBirthday = (Button) view.findViewById(R.id.btn_birthday);
		rGGender = (RadioGroup) view.findViewById(R.id.radioGroup1);
		rBMale = (RadioButton) view.findViewById(R.id.radio0);
		rBFemale = (RadioButton) view.findViewById(R.id.radio1);
		etTeachingForYears=(EditText)view.findViewById(R.id.et_teaching_years);
		etTeachingForMonth=(EditText)view.findViewById(R.id.et_teaching_months);
		return view;
	}

	private void getCurrentUser() {

		if (((CompleteProfileActivityContainer) getActivity())
				.isFirstTimeUpdate()) {
			user = userHelper.getUser();
			updateUI();
		} else {
			fetchUserInfo();
		}

	}

	private void fetchUserInfo() {
		HashMap<String,String> params = new HashMap<>();
		params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		//AppRestClient.post(URLHelper.URL_FREE_VERSION_GET_USER, params, getUserInfo);
		freeVersionGetUser(params);
	}

	private void freeVersionGetUser(HashMap<String,String> params){
		uiHelper.showLoadingDialog("Please wait...");
		ApplicationSingleton.getInstance().getNetworkCallInterface().freeVersionGetUser(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}

						Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
								response.body());
						if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
							UserWrapper userWrapper = GsonParser.getInstance()
									.parseUserWrapper(wrapper.getData().toString());
							user=userWrapper.getUser();
							//user.setId(UserHelper.getUserFreeId());
						}
						updateUI();
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}
						uiHelper.showMessage(getString(R.string.internet_error_text));
					}
				}
		);
	}
	AsyncHttpResponseHandler getUserInfo = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			uiHelper.showMessage(getString(R.string.internet_error_text));

		}

		@Override
		public void onStart() {

			uiHelper.showLoadingDialog("Please wait...");
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				UserWrapper userWrapper = GsonParser.getInstance()
						.parseUserWrapper(wrapper.getData().toString());
				user=userWrapper.getUser();
				//user.setId(UserHelper.getUserFreeId());
			}
			updateUI();
		}

	};

	private void updateUI() {
		etFirstName.setText(user.getFirstName());
		etMiddleName.setText(user.getMiddleName());
		etLastName.setText(user.getLastName());
		// etSchool.setText(user.gets);
		etMobile.setText(user.getMobileNum());
		if (user.getDob() != null
				&& !user.getDob().equalsIgnoreCase("0000-00-00"))
			updateBirthDatePanel(user.getDob());
		etSchool.setText(user.getSchoolName());

		if (TextUtils.isEmpty(user.getMedium().toString().trim()))
			user.setMedium("0");
		if (user.getMedium().equalsIgnoreCase("0")) {
			tvMedium.setText(mQuickAction.getActionItem(0).getTitle());
		} else
			tvMedium.setText(mQuickAction.getActionItem(1).getTitle());
		Log.e("USER GENDER", "" + user.getGender());
		if (user.getGender() != 0)
			((RadioButton) rGGender.getChildAt(user.getGender() - 1))
					.setChecked(true);
		else
			((RadioButton) rGGender.getChildAt(user.getGender()))
					.setChecked(true);
		if (!user.getGradeIds().equals(""))
			btnSelectGrade.setText("Grades: " + user.getGradeIds());

		String temp = user.getCity();
		if (!TextUtils.isEmpty(temp)) {
			tvCity.setText(temp);
		}
		temp=user.getTeachinFor();
		if(!TextUtils.isEmpty(temp))
		{
			String[] tokens=temp.split("-");
			etTeachingForYears.setText(tokens[0]);
			//etTeachingForMonth.setText(tokens[1]);
		}
				
		

	}

	private void updateBirthDatePanel(String dob) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				AppUtility.DATE_FORMAT_SERVER, java.util.Locale.getDefault());
		Date date;
		try {
			date = formatter.parse(dob);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			tvMonth.setText(AppUtility.getMonthFullName(calendar
					.get(Calendar.MONTH)));
			tvday.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
			tvYear.setText(calendar.get(Calendar.YEAR) + "");

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		radioFirstName.setOnClickListener(this);
		radioMiddleName.setOnClickListener(this);
		radioLastName.setOnClickListener(this);

		app = (ApplicationSingleton) getActivity().getApplicationContext();

		app.setupUI(view.findViewById(R.id.layout_parent), getActivity());

		country = tvCountry.getText().toString();
		city = tvCity.getText().toString();
		countryCode = tvCountryCode.getText().toString();
		mobileNumber = countryCode + etMobile.getText().toString();

		btnBirthday.setOnClickListener(this);
		btnMedium.setOnClickListener(this);
		btnSelectGrade.setOnClickListener(this);
		userHelper = new UserHelper(this, getActivity());
		uiHelper = new UIHelper(getActivity());
		setQuickAction();

		btnSave.setOnClickListener(this);
		btnCity.setOnClickListener(this);
		tvCity.setOnClickListener(this);
		selectedGrades = new ArrayList<Integer>();
	}

	private void setQuickAction() {
		// TODO Auto-generated method stub
		ActionItem banglaItem = new ActionItem(ID_BANGLA, "Bangla", null);
		ActionItem englishItem = new ActionItem(ID_ENGLISH, "English", null);

		// use setSticky(true) to disable QuickAction dialog being dismissed
		// after an item is clicked

		mQuickAction = new QuickAction(getActivity());

		mQuickAction.addActionItem(banglaItem);
		mQuickAction.addActionItem(englishItem);

		// setup the action item click listener
		mQuickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction quickAction, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						tvMedium.setText(actionItem.getTitle());
						selectedMediumId = actionItem.getActionId();
						// if (actionId == ID_BANGLA) {
						// tvMedium.setText(actionItem.getTitle());
						// } else {
						// tvMedium.setText(actionItem.getTitle());
						// }
					}
				});

		mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		getCurrentUser();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_birthday:
			//showBirthDayPicker();
			showDatepicker();
			break;

		case R.id.btn_medium:
			mQuickAction.show(v);
			mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_RIGHT);
			break;

		case R.id.btn_select_grade:

			selectGrades();

			break;
		case R.id.btn_save:

			updateUser();

			break;
		
		case R.id.radio_first:
			radioFirstName.setChecked(true);
			radioMiddleName.setChecked(false);
			radioLastName.setChecked(false);
			break;

		case R.id.radio_middle:
			radioFirstName.setChecked(false);
			radioMiddleName.setChecked(true);
			radioLastName.setChecked(false);
			break;

		case R.id.radio_last:
			radioFirstName.setChecked(false);
			radioMiddleName.setChecked(false);
			radioLastName.setChecked(true);
			break;
		case R.id.btn_city:
			showCityPicker();
			break;
		case R.id.tv_city:
			showCityPicker();
			break;
		default:
			break;
		}
	}

	private void showDatepicker() {
		DatePickerFragment picker = new DatePickerFragment();
		picker.setCallbacks(datePickerCallback);
		picker.show(getFragmentManager(), "datePicker");
	}
	
	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case CITY:
				City city = (City) item;
				tvCity.setText(city.getText());
				break;
			default:
				break;
			}

		}
	};

	DatePickerOnSetDateListener datePickerCallback=new DatePickerOnSetDateListener() {
		
		

		@Override
		public void onDateSelected(int month, String monthName, int day,
				int year, String dateFormatServer, String dateFormatApp,
				Date date) {
			// TODO Auto-generated method stub
			tvMonth.setText(monthName);
			tvday.setText(day + "");
			tvYear.setText(year + "");

			monthStr = monthName;
			dateStr = day + "";
			yearStr = year + "";
		}
	};
	
	
	
	private void showCityPicker() {
		Picker picker = Picker.newInstance(0);
		picker.setData(PickerType.CITY, cities, PickerCallback,
				"Choose your city");
		picker.show(getChildFragmentManager(), null);
	}
	
	private void updateUser() {

		String temp = etFirstName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setFirstName(temp);
		}
		temp = etMiddleName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setMiddleName(temp);
		}
		temp = etLastName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setLastName(temp);
		}
		temp = etSchool.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setSchoolName(temp);
		}
		
		temp="";
		String tempYear=etTeachingForYears.getText().toString().trim();
		String tempMonth=etTeachingForMonth.getText().toString().trim();
		if (!TextUtils.isEmpty(tempYear))
			temp=temp+tempYear;
		else
			temp=temp+"0";
		
		if(!TextUtils.isEmpty(tempMonth))
			temp=temp+"-"+tempMonth;
		else
			temp=temp+"0";
		
		if(!TextUtils.isEmpty(temp))
			user.setTeachinFor(temp);
		temp=tvYear.getText().toString()+tvMonth.getText().toString()+tvday.getText().toString();
		if(!TextUtils.isEmpty(temp))
		{
		user.setDob(tvYear.getText()
				+ "-"
				+ AppUtility.getMonthNumberFromName(tvMonth.getText()
						.toString().trim()) + "-" + tvday.getText());
		}
		user.setMedium(selectedMediumId + "");
		user.setGradeIds(gradeStr);

		if (radioFirstName.isChecked()) {
			user.setNickName("1");
		} else if (radioMiddleName.isChecked()) {
			user.setNickName("2");
		} else if (radioLastName.isChecked()) {
			user.setNickName("3");
		}

		switch (rGGender.getCheckedRadioButtonId()) {
		case R.id.radio0:
			user.setGender(1);
			user.setGender("Male");
			break;
		case R.id.radio1:
			user.setGender(2);
			user.setGender("Female");
			break;
		default:
			break;
		}

		String mobileNum = etMobile.getText().toString().trim();
		if(!TextUtils.isEmpty(mobileNum))
			user.setMobileNum(mobileNum);
		
		userHelper.updateUser(user);

		// params.put(RequestKeyHelper.DOB, user.getDob());
	}

	private void selectGrades() {
		// TODO Auto-generated method stub
		new GradeDialog(getActivity(), new onGradeDialogButtonClickListener() {

			@Override
			public void onDoneBtnClick(GradeDialog gradeDialog,
					String gradeString, List<Integer> grades) {

				gradeDialog.dismiss();

				gradeStr = new String(gradeString);
				selectedGrades.clear();
				selectedGrades.addAll(grades);

				btnSelectGrade.setText("Grades: "
						+ getFormattedGradeString(grades));

			}
		}, gradeStr).show();
	}

	public String getFormattedGradeString(List<Integer> data) {
		String temp = "";
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) == -1) {
				temp = temp + "Playgroup";
			} else if (data.get(i) == 0) {
				temp = temp + "KG";
			} else {
				temp = temp + data.get(i);
			}

			if (i != data.size() - 1) {
				temp = temp + ",";
			}
		}
		return temp;
	}

	private void showBirthDayPicker() {
		// TODO Auto-generated method stub
		CustomDateTimePicker custom = new CustomDateTimePicker(getActivity(),
				new CustomDateTimePicker.ICustomDateTimeListener() {

					@Override
					public void onCancel() {

					}

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						// TODO Auto-generated method stub

						// SimpleDateFormat format = new
						// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						//
						// String dateStr = format.format(dateSelected);
						//
						// Log.e("Date Selected", dateStr);

						tvMonth.setText(monthFullName);
						tvday.setText(date + "");
						tvYear.setText(year + "");

						monthStr = monthFullName;
						dateStr = date + "";
						yearStr = year + "";
					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		custom.set24HourFormat(false);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		custom.setDate(Calendar.getInstance());
		custom.showDialog();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus)
			if (v.getId() == R.id.et_first || v.getId() == R.id.et_middle
					|| v.getId() == R.id.et_last) {
				switch (v.getId()) {
				case R.id.et_first:
					radioFirstName.setChecked(hasFocus);
					radioMiddleName.setChecked(!hasFocus);
					radioLastName.setChecked(!hasFocus);
					break;

				case R.id.et_middle:
					radioFirstName.setChecked(!hasFocus);
					radioMiddleName.setChecked(hasFocus);
					radioLastName.setChecked(!hasFocus);
					break;

				case R.id.et_last:
					radioFirstName.setChecked(!hasFocus);
					radioMiddleName.setChecked(!hasFocus);
					radioLastName.setChecked(hasFocus);
					break;

				default:
					break;
				}
			}
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
        if(((CompleteProfileActivityContainer)getActivity()).isFirstLogin){
            ((CompleteProfileActivityContainer)getActivity()).backToLogin();
        }else {
            Intent intent = new Intent(getActivity(),
                    HomePageFreeVersion.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all
            // activities
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        getActivity().finish();

	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}

}
 /*extends Fragment implements
		OnFocusChangeListener, OnClickListener, UserAuthListener {

	private RadioButton radioFirstName, radioMiddleName, radioLastName;
	private EditText etFirstName, etMiddleName, etLastName, etMobile,etTeachingForYears,etTeachingForMonth;
	private TextView tvCountryCode, tvCountry, tvState, tvCity, tvMonth, tvday,
			tvYear;
	private Button btnBirthday, btnSelectGrade, btnSave, btnSkip;
	private RadioGroup rGGender;
	private RadioButton rBMale, rBFemale;

	ApplicationSingleton app;
	UIHelper uiHelper;
	UserHelper userHelper;
	String country = "", state = "", city = "", countryCode = "",
			mobileNumber = "", monthStr = "", dateStr = "", yearStr = "";
	private User user;
	private View view;
	private String gradeStr = "";
	private List<Integer> selectedGrades;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		*//** Inflating the layout for this fragment **//*
		view = inflater.inflate(R.layout.complete_profile_teacher_fragment,
				null);
		radioFirstName = (RadioButton) view.findViewById(R.id.radio_first);
		radioMiddleName = (RadioButton) view.findViewById(R.id.radio_middle);
		radioLastName = (RadioButton) view.findViewById(R.id.radio_last);
		etFirstName = (EditText) view.findViewById(R.id.et_first);
		etMiddleName = (EditText) view.findViewById(R.id.et_middle);
		etLastName = (EditText) view.findViewById(R.id.et_last);
		etMobile = (EditText) view.findViewById(R.id.et_mobile);
		tvCountryCode = (TextView) view.findViewById(R.id.tv_country_code);
		tvCountry = (TextView) view.findViewById(R.id.tv_country);
		tvState = (TextView) view.findViewById(R.id.tv_state);
		tvCity = (TextView) view.findViewById(R.id.tv_city);
		tvMonth = (TextView) view.findViewById(R.id.tv_month);
		tvday = (TextView) view.findViewById(R.id.tv_day);
		tvYear = (TextView) view.findViewById(R.id.tv_year);
		btnSelectGrade = (Button) view.findViewById(R.id.btn_select_grade);
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnSkip = (Button) view.findViewById(R.id.btn_skip);
		btnBirthday = (Button) view.findViewById(R.id.btn_birthday);
		rGGender = (RadioGroup) view.findViewById(R.id.radioGroup1);
		rBMale = (RadioButton) view.findViewById(R.id.radio0);
		rBFemale = (RadioButton) view.findViewById(R.id.radio1);
		etTeachingForYears=(EditText)view.findViewById(R.id.et_teaching_years);
		etTeachingForMonth=(EditText)view.findViewById(R.id.et_teaching_months);
		return view;
	}

	private void getCurrentUser() {

		if (((CompleteProfileActivityContainer) getActivity())
				.isFirstTimeUpdate()) {
			user = userHelper.getUser();
			updateUI();
		} else {
			fetchUserInfo();
		}

	}

	private void fetchUserInfo() {
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		AppRestClient.post(URLHelper.URL_FREE_VERSION_GET_USER, params,
				getUserInfo);
	}

	AsyncHttpResponseHandler getUserInfo = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			uiHelper.showMessage(arg1);

		}

		@Override
		public void onStart() {

			uiHelper.showLoadingDialog("Please wait...");
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				user = GsonParser.getInstance()
						.parseUserWrapper(wrapper.getData().toString())
						.getUser();
			}
			updateUI();
		}

	};

	private void updateUI() {
		etFirstName.setText(user.getFirstName());
		etMiddleName.setText(user.getMiddleName());
		etLastName.setText(user.getLastName());
		// etSchool.setText(user.gets);
		etMobile.setText(user.getMobileNum());
		if (user.getDob() != null)
			updateBirthDatePanel(user.getDob());

	}

	private void updateBirthDatePanel(String dob) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				AppUtility.DATE_FORMAT_SERVER, java.util.Locale.getDefault());
		Date date;
		try {
			date = formatter.parse(dob);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			tvMonth.setText(AppUtility.getMonthFullName(calendar
					.get(Calendar.MONTH)));
			tvday.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
			tvYear.setText(calendar.get(Calendar.YEAR) + "");

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		etFirstName.setOnFocusChangeListener(this);
		etMiddleName.setOnFocusChangeListener(this);
		etLastName.setOnFocusChangeListener(this);

		radioFirstName.setEnabled(false);
		radioMiddleName.setEnabled(false);
		radioLastName.setEnabled(false);

		app = (ApplicationSingleton) getActivity().getApplicationContext();

		app.setupUI(view.findViewById(R.id.layout_parent), getActivity());

		country = tvCountry.getText().toString();
		city = tvCity.getText().toString();
		countryCode = tvCountryCode.getText().toString();
		mobileNumber = countryCode + etMobile.getText().toString();

		btnBirthday.setOnClickListener(this);
		btnSelectGrade.setOnClickListener(this);
		userHelper = new UserHelper(this, getActivity());
		uiHelper = new UIHelper(getActivity());

		btnSave.setOnClickListener(this);
		btnSkip.setOnClickListener(this);
		selectedGrades = new ArrayList<Integer>();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		getCurrentUser();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_birthday:
			showBirthDayPicker();
			break;

		case R.id.btn_select_grade:

			selectGrades();

			break;
		case R.id.btn_save:

			updateUser();

			break;
		case R.id.btn_skip:
			userHelper.setLoggedIn(true);
			Intent intent = new Intent(getActivity(),
					FreeVersionLoggedInActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up
			// all activities
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			getActivity().finish();
			break;

		default:
			break;
		}
	}

	private void updateUser() {

		String temp = etFirstName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setFirstName(temp);
		}
		temp = etMiddleName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setMiddleName(temp);
		}
		temp = etLastName.getText().toString().trim();
		if (!TextUtils.isEmpty(temp)) {
			user.setLastName(temp);
		}
		
		temp="";
		String tempYear=etTeachingForYears.getText().toString().trim();
		String tempMonth=etTeachingForMonth.getText().toString().trim();
		if (!TextUtils.isEmpty(tempYear)) 
		{
			temp=temp+tempYear;
		}
		else
			temp=temp+"0";
		
		if(!TextUtils.isEmpty(tempMonth)) 
		{
			temp=temp+"-"+tempMonth;
		}
		else
			temp=temp+"0";
		
		if(!TextUtils.isEmpty(temp))
			user.setTeachinFor(temp);
		
		
		user.setDob(tvYear.getText()
				+ "-"
				+ AppUtility.getMonthNumberFromName(tvMonth.getText()
						.toString().trim()) + "-" + tvday.getText());
		user.setGradeIds(gradeStr);

		if (radioFirstName.isChecked()) {
			user.setNickName("1");
		} else if (radioMiddleName.isChecked()) {
			user.setNickName("2");
		} else if (radioLastName.isChecked()) {
			user.setNickName("3");
		}

		switch (rGGender.getCheckedRadioButtonId()) {
		case R.id.radio0:
			user.setGender(1);
			user.setGender("Male");
			break;
		case R.id.radio1:
			user.setGender(2);
			user.setGender("Female");
			break;
		default:
			break;
		}

		userHelper.updateUser(user);

		// params.put(RequestKeyHelper.DOB, user.getDob());
	}

	private void selectGrades() {
		// TODO Auto-generated method stub
		new GradeDialog(getActivity(), new onGradeDialogButtonClickListener() {

			@Override
			public void onDoneBtnClick(GradeDialog gradeDialog,
					String gradeString, List<Integer> grades) {

				gradeDialog.dismiss();

				gradeStr = new String(gradeString);
				selectedGrades.clear();
				selectedGrades.addAll(grades);

				btnSelectGrade.setText("Grades: "
						+ getFormattedGradeString(grades));

			}
		}, gradeStr).show();
	}

	public String getFormattedGradeString(List<Integer> data) {
		String temp = "";
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) == -1) {
				temp = temp + "Playgroup";
			} else if (data.get(i) == 0) {
				temp = temp + "KG";
			} else {
				temp = temp + data.get(i);
			}

			if (i != data.size() - 1) {
				temp = temp + ",";
			}
		}
		return temp;
	}

	private void showBirthDayPicker() {
		// TODO Auto-generated method stub
		CustomDateTimePicker custom = new CustomDateTimePicker(getActivity(),
				new CustomDateTimePicker.ICustomDateTimeListener() {

					@Override
					public void onCancel() {

					}

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						// TODO Auto-generated method stub

						// SimpleDateFormat format = new
						// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						//
						// String dateStr = format.format(dateSelected);
						//
						// Log.e("Date Selected", dateStr);

						tvMonth.setText(monthFullName);
						tvday.setText(date + "");
						tvYear.setText(year + "");

						monthStr = monthFullName;
						dateStr = date + "";
						yearStr = year + "";
					}
				});
		*//**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 *//*
		custom.set24HourFormat(false);
		*//**
		 * Pass Directly current data and time to show when it pop up
		 *//*
		custom.setDate(Calendar.getInstance());
		custom.showDialog();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus)
			if (v.getId() == R.id.et_first || v.getId() == R.id.et_middle
					|| v.getId() == R.id.et_last) {
				switch (v.getId()) {
				case R.id.et_first:
					radioFirstName.setChecked(hasFocus);
					radioMiddleName.setChecked(!hasFocus);
					radioLastName.setChecked(!hasFocus);
					break;

				case R.id.et_middle:
					radioFirstName.setChecked(!hasFocus);
					radioMiddleName.setChecked(hasFocus);
					radioLastName.setChecked(!hasFocus);
					break;

				case R.id.et_last:
					radioFirstName.setChecked(!hasFocus);
					radioMiddleName.setChecked(!hasFocus);
					radioLastName.setChecked(hasFocus);
					break;

				default:
					break;
				}
			}
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
		Intent intent = new Intent(getActivity(),
				FreeVersionLoggedInActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all
		// activities
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		getActivity().finish();

	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
	}

}
*/