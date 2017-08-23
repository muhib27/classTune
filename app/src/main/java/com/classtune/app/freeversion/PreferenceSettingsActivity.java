package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.MainCategory;
import com.classtune.app.schoolapp.model.SubCategory;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomTextView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferenceSettingsActivity extends ChildContainerActivity implements OnCheckedChangeListener {

	private LinearLayout scrollLinearLayout;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	private ArrayList<MainCategory> mainCategories;
	private HashMap<String, SubCategory> subCategoriesMap;
	private String[] selectedSubCateogryIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preference_settings);
		initViews();
		uiHelper = new UIHelper(this);
		userHelper = new UserHelper(this);
		mainCategories = new ArrayList<MainCategory>();
		subCategoriesMap = new HashMap<String, SubCategory>();
		fetchPreferenceData();
	}

	private void fetchPreferenceData() {
		HashMap<String,String> params = new HashMap<>();
		// UserHelper.getUserFreeId()
		params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		//AppRestClient.post(URLHelper.URL_PREFERENCE_SETTINGS_GET, params, preferenceHandler);
		preferenceSettingGet(params);
	}

	private void setPreferenceData(String category_ids) {
		HashMap<String,String> params = new HashMap<>();
		// UserHelper.getUserFreeId()
		params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		params.put(RequestKeyHelper.CATEGORY_IDS, category_ids);

		//AppRestClient.post(URLHelper.URL_PREFERENCE_SETTINGS_SET, params, set_preferenceHandler);
		preferenceSettingSet(params);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		((Button) findViewById(R.id.pref_btn_save)).setOnClickListener(this);
		scrollLinearLayout = (LinearLayout) findViewById(R.id.pref_ll_scroll);
	}

	private void preferenceSettingGet(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().preferenceSettingGet(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}

						if (response.body() != null){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());

							if (wrapper.getStatus().getCode() == 200
									|| wrapper.getStatus().getCode() == 202) {

								Log.e("PREFERENCE DATA", wrapper.getData().toString());
								Log.e("STATUS CODE", "  " + wrapper.getStatus().getCode());
								// postList = GsonParser.getInstance().parseGoodreadPostAll(
								// wrapper.getData().toString());
								mainCategories = GsonParser.getInstance().parseMainCategories(
										wrapper.getData().getAsJsonArray("all_categories")
												.toString());
								selectedSubCateogryIds = wrapper.getData()
										.get("preferred_categories").getAsString().split(",");
								Log.e("PREFFERED CATEGORY STRING ",
										wrapper.getData().get("preferred_categories")
												.toString());
								for (MainCategory mcat : mainCategories) {
									View mainCatView = LayoutInflater.from(
											PreferenceSettingsActivity.this).inflate(
											R.layout.item_pref_settings, null);
									CustomTextView ctvm = (CustomTextView) mainCatView
											.findViewById(R.id.tv_main_cat_name);
									ImageView icon = (ImageView) mainCatView
											.findViewById(R.id.pref_cat_icon);
									icon.setImageResource(AppUtility.getResourceImageId(
											Integer.parseInt(mcat.getId()), true, true));
									ctvm.setText(mcat.getName());
									LinearLayout subView = (LinearLayout) mainCatView
											.findViewById(R.id.pref_ll_subcat);
									for (SubCategory sub : mcat.getSub_categories()) {
										View subViewItem = LayoutInflater.from(
												PreferenceSettingsActivity.this).inflate(
												R.layout.item_pref_settings_subcat, null);
										CheckBox cb = (CheckBox) subViewItem
												.findViewById(R.id.cb_pref_subcat);

										CustomTextView ctvs = (CustomTextView) subViewItem
												.findViewById(R.id.tv_pref_subcat_name);
										cb.setTag(sub.getId());
										cb.setOnCheckedChangeListener(PreferenceSettingsActivity.this);
										ctvs.setText(sub.getName());
										subCategoriesMap.put(sub.getId(), sub);
										// if(wrapper.getStatus().getCode()==202){

						/*cb.setChecked(true);
						// }else {
						cb.setChecked(false);*/
										if(wrapper.getStatus().getCode()==202){
											cb.setChecked(true);
										}else
											for (String catId : selectedSubCateogryIds) {
												Log.e("MATCHED SUBCATEGORY SELECTED", "" + catId);
												if (sub.getId().equals(catId)) {
													cb.setChecked(true);
													break;
												}
											}
										// }

										subView.addView(subViewItem);
									}
									if (mcat.getSub_categories().size() > 0)
										scrollLinearLayout.addView(mainCatView);
								}
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
	AsyncHttpResponseHandler preferenceHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);

			if (wrapper.getStatus().getCode() == 200
					|| wrapper.getStatus().getCode() == 202) {

				Log.e("PREFERENCE DATA", wrapper.getData().toString());
				Log.e("STATUS CODE", "  " + wrapper.getStatus().getCode());
				// postList = GsonParser.getInstance().parseGoodreadPostAll(
				// wrapper.getData().toString());
				mainCategories = GsonParser.getInstance().parseMainCategories(
						wrapper.getData().getAsJsonArray("all_categories")
								.toString());
				selectedSubCateogryIds = wrapper.getData()
						.get("preferred_categories").getAsString().split(",");
				Log.e("PREFFERED CATEGORY STRING ",
						wrapper.getData().get("preferred_categories")
								.toString());
				for (MainCategory mcat : mainCategories) {
					View mainCatView = LayoutInflater.from(
							PreferenceSettingsActivity.this).inflate(
							R.layout.item_pref_settings, null);
					CustomTextView ctvm = (CustomTextView) mainCatView
							.findViewById(R.id.tv_main_cat_name);
					ImageView icon = (ImageView) mainCatView
							.findViewById(R.id.pref_cat_icon);
					icon.setImageResource(AppUtility.getResourceImageId(
							Integer.parseInt(mcat.getId()), true, true));
					ctvm.setText(mcat.getName());
					LinearLayout subView = (LinearLayout) mainCatView
							.findViewById(R.id.pref_ll_subcat);
					for (SubCategory sub : mcat.getSub_categories()) {
						View subViewItem = LayoutInflater.from(
								PreferenceSettingsActivity.this).inflate(
								R.layout.item_pref_settings_subcat, null);
						CheckBox cb = (CheckBox) subViewItem
								.findViewById(R.id.cb_pref_subcat);

						CustomTextView ctvs = (CustomTextView) subViewItem
								.findViewById(R.id.tv_pref_subcat_name);
						cb.setTag(sub.getId());
						cb.setOnCheckedChangeListener(PreferenceSettingsActivity.this);
						ctvs.setText(sub.getName());
						subCategoriesMap.put(sub.getId(), sub);
						// if(wrapper.getStatus().getCode()==202){
						
						/*cb.setChecked(true);
						// }else {
						cb.setChecked(false);*/
						if(wrapper.getStatus().getCode()==202){
							cb.setChecked(true);
						}else 
						for (String catId : selectedSubCateogryIds) {
							Log.e("MATCHED SUBCATEGORY SELECTED", "" + catId);
							if (sub.getId().equals(catId)) {
								cb.setChecked(true);
								break;
							}
						}
						// }

						subView.addView(subViewItem);
					}
					if (mcat.getSub_categories().size() > 0)
						scrollLinearLayout.addView(mainCatView);
				}
			}
		};
	};

	private void preferenceSettingSet(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().preferenceSettingSet(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}

						if (response.body() != null){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());

							if (wrapper.getStatus().getCode() == 200) {

								Log.e("SET_PREFERENCE DATA", wrapper.getData().toString());
								finish();
								Intent main = new Intent(PreferenceSettingsActivity.this,HomePageFreeVersion.class);
								main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
										| Intent.FLAG_ACTIVITY_NEW_TASK
										| Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(main);

				/*
				 * mainCategories =
				 * GsonParser.getInstance().parseMainCategories(
				 * wrapper.getData(
				 * ).getAsJsonArray("all_categories").toString());
				 */
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
	AsyncHttpResponseHandler set_preferenceHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);

			if (wrapper.getStatus().getCode() == 200) {
				
				Log.e("SET_PREFERENCE DATA", wrapper.getData().toString());
				finish();	
				Intent main = new Intent(PreferenceSettingsActivity.this,HomePageFreeVersion.class);
				main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
						| Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(main);
				
				/*
				 * mainCategories =
				 * GsonParser.getInstance().parseMainCategories(
				 * wrapper.getData(
				 * ).getAsJsonArray("all_categories").toString());
				 */
			}
		};
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.pref_btn_save:
			String commaSeparatedString = "";

			for (SubCategory sub : subCategoriesMap.values()) {
				if (sub.isChecked()) {
					commaSeparatedString += sub.getId();
					commaSeparatedString += ",";
				}
			}
			setPreferenceData(commaSeparatedString);
			Log.e("Selected COmma Separeted SubCategories",
					commaSeparatedString);
			break;
		default:
			
			break;
		}
		super.onClick(v);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.e("button tag id", buttonView.getTag().toString());
		subCategoriesMap.get(buttonView.getTag().toString()).setChecked(
				isChecked);
	}
}
