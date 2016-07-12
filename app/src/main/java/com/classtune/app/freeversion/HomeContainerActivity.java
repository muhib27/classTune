package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.NotificationActivity;
import com.classtune.app.schoolapp.SocialBaseActivity;
import com.classtune.app.schoolapp.adapters.DrawerExpandableListViewAdapter;
import com.classtune.app.schoolapp.camera.CameraGalleryPicker;
import com.classtune.app.schoolapp.camera.IPictureCallback;
import com.classtune.app.schoolapp.model.DrawerChildBase;
import com.classtune.app.schoolapp.model.DrawerChildSettings;
import com.classtune.app.schoolapp.model.DrawerGroup;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.model.WrapAllData;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserAccessType;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.CustomRhombusIcon;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeContainerActivity extends SocialBaseActivity implements
		OnQueryTextListener, OnClickListener, SearchView.OnCloseListener, GcmIntentService.INotificationCount, NotificationActivity.INotificationCountChangedFromActivity, IPictureCallback {


    public DrawerExpandableListViewAdapter listAdapter;
	public ExpandableListView expListView;

	private static final String TAG = HomeContainerActivity.class
			.getSimpleName();
	private boolean mState = false;
	public DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ActionBar actionBar;
	ScheduleTask r = null;
	ReschedulableTimer rescheduleTimer = null;
	private UIHelper uiHelper;

	private SearchView mSearchView;
	LinearLayout layoutSearch;
	private PopupWindow popup;

	private int keyHeight = 0;

	private View popupView;
	private WrapAllData dataWrap;
	private static int positionPager = 0;
	private LinearLayout basicInfoPanel;
	// private LinearLayout basidInfoTitlePanel;
	private User user;
	private ImageView profilePic;

	private ProgressBar pb;
	private TextView userNameTextView;
	private TextView userTypeTextView;
	public ImageView homeBtn, logo;
	private TextView txtTitle;

	public List<DrawerGroup> groupItem;
	public Map<String, List<DrawerChildBase>> childList;
	public Map<String, List<Boolean>> childSelectionStates;

	//upload photo
	private String selectedImagePath = "";
	private CameraGalleryPicker mCameraGalleryPicker;




    private TextView txtNotificationCount;

	private void prepareListData() {
		groupItem = new ArrayList<DrawerGroup>();
		childList = new HashMap<String, List<DrawerChildBase>>();
		DrawerGroup group;
		int count = 0;

		group = new DrawerGroup();
		/*group.setImageName("menu_right_drawer");
		group.setText("Champs21");
		group.setId("0");
		groupItem.add(group);
		String[] myMenuArrayText = getResources().getStringArray(
				R.array.free_menus_text);
		String[] myMenuArrayImages = getResources().getStringArray(
				R.array.free_menus_images);
		String[] myMenuArrayIds = getResources().getStringArray(
				R.array.free_menus_id);
		List<DrawerChildBase> menus = new ArrayList<DrawerChildBase>();

		for (int i = 0; i < myMenuArrayText.length; i++) {
			DrawerChildMenu child = new DrawerChildMenu();
			child.setText(myMenuArrayText[i]);
			child.setId(myMenuArrayIds[i]);
			child.setImageName(myMenuArrayImages[i]);
			menus.add(child);
		}
		childList.put(groupItem.get(count++).getText(), menus);*/
		/*
		 * List<Boolean> menuStatelist=new ArrayList<Boolean>(Arrays.asList(new
		 * Boolean[myMenuArrayText.length])); Collections.fill(menuStatelist,
		 * new Boolean(false));
		 * childSelectionStates.put(groupItem.get(count).getText(),
		 * menuStatelist); count++;
		 */

		if (UserHelper.isLoggedIn()
				&& UserHelper.getUserAccessType() == UserAccessType.PAID) {
            if (userHelper.getUser().getType()==UserTypeEnum.PARENTS){
                group = new DrawerGroup();
                group.setImageName("student_selection_icon_white");
                group.setText(getString(R.string.java_homecontaineractivity_select_your_child));
                group.setId("1");
                groupItem.add(group);
                childList.put(groupItem.get(count++).getText(),
                        new ArrayList<DrawerChildBase>());
            }

			/*String[] myMySchoolArrayText={userHelper.getUser().getPaidInfo().getSchool_name()};
			String[] myMySchoolArrayImages={"account_settings"};
			
			List<DrawerChildBase> myschool = new ArrayList<DrawerChildBase>();

			for (int i = 0; i < myMySchoolArrayText.length; i++) {
				DrawerChildMySchool child = new DrawerChildMySchool();
				child.setText(myMySchoolArrayText[i]);
				child.setImageName(myMySchoolArrayImages[i]);
				child.setId("" + i);
				myschool.add(child);
			}
			childList.put(groupItem.get(count++).getText(), myschool);*/
		}
		/*if (UserHelper.isLoggedIn()
				&& UserHelper.getUserAccessType() == UserAccessType.PAID) {
			group = new DrawerGroup();
			group.setImageName("diary_white");
			group.setText("Diary21");
			group.setId("1");
			groupItem.add(group);
			String[] myDiaryArrayText;
			String[] myDiaryArrayImages;
			String[] myDiaryArrayClazz;

			switch (userHelper.getUser().getType()) {
			case STUDENT:
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_student);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_student);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_student);
				List<DrawerChildBase> diaryMenuStudent = new ArrayList<DrawerChildBase>();
				for (int i = 0; i < myDiaryArrayText.length; i++) {
					DrawerChildMenuDiary child = new DrawerChildMenuDiary();
					child.setText(myDiaryArrayText[i]);
					child.setId(i + "");
					child.setImageName(myDiaryArrayImages[i]);
					child.setClazzName("com.champs21.schoolapp.fragments."
							+ myDiaryArrayClazz[i]);
					diaryMenuStudent.add(child);
				}
				childList.put(groupItem.get(count++).getText(),
						diaryMenuStudent);
				break;

			case PARENTS:
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_parents);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_parents);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_parents);
				List<DrawerChildBase> diaryMenuParents = new ArrayList<DrawerChildBase>();
				for (int i = 0; i < myDiaryArrayText.length; i++) {
					DrawerChildMenuDiary child = new DrawerChildMenuDiary();
					child.setText(myDiaryArrayText[i]);
					child.setId(i + "");
					child.setImageName(myDiaryArrayImages[i]);
					child.setClazzName("com.champs21.schoolapp.fragments."
							+ myDiaryArrayClazz[i]);
					diaryMenuParents.add(child);
				}
				childList.put(groupItem.get(count++).getText(),
						diaryMenuParents);
				break;

			case TEACHER:
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_teacher);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_teacher);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_teacher);
				List<DrawerChildBase> diaryMenuTeacher = new ArrayList<DrawerChildBase>();
				for (int i = 0; i < myDiaryArrayText.length; i++) {
					DrawerChildMenuDiary child = new DrawerChildMenuDiary();
					child.setText(myDiaryArrayText[i]);
					child.setId(i + "");
					child.setImageName(myDiaryArrayImages[i]);
					child.setClazzName("com.champs21.schoolapp.fragments."
							+ myDiaryArrayClazz[i]);
					diaryMenuTeacher.add(child);
				}
				childList.put(groupItem.get(count++).getText(),
						diaryMenuTeacher);
				break;
			default:
				break;
			}

		}*/

		group = new DrawerGroup();
		group.setImageName("settings_right_drawer");
		group.setText(getString(R.string.java_homecontaineractivity_settings));
		group.setId("2");
		groupItem.add(group);
		String[] mySettingsArrayText;
		String[] mySettingsArrayImages;
		if (UserHelper.isLoggedIn()) {
			mySettingsArrayText = getResources().getStringArray(
					R.array.free_settings_text_log_in);
			mySettingsArrayImages = getResources().getStringArray(
					R.array.free_settings_images_log_in);
			List<DrawerChildBase> settings = new ArrayList<DrawerChildBase>();

			for (int i = 0; i < mySettingsArrayText.length; i++) {
				DrawerChildSettings child = new DrawerChildSettings();
				child.setText(mySettingsArrayText[i]);
				child.setImageName(mySettingsArrayImages[i]);
				child.setId("" + i);
				settings.add(child);
			}
			childList.put(groupItem.get(count++).getText(), settings);
			/*
			 * List<Boolean> settingsStatelist=new
			 * ArrayList<Boolean>(Arrays.asList(new
			 * Boolean[mySettingsArrayText.length]));
			 * Collections.fill(settingsStatelist, new Boolean(false));
			 * childSelectionStates.put(groupItem.get(count).getText(),
			 * settingsStatelist); count++;
			 */

			/*group = new DrawerGroup();
			group.setImageName("score");
			group.setText("Quiz Scores");
			group.setId("3");
			groupItem.add(group);
			childList.put(groupItem.get(count++).getText(),
					new ArrayList<DrawerChildBase>());*/

			group = new DrawerGroup();
			group.setImageName("log_out");
			group.setText(getString(R.string.java_homecontaineractivity_log_out));
			group.setId("4");
			groupItem.add(group);
			childList.put(groupItem.get(count++).getText(),
					new ArrayList<DrawerChildBase>());
		} else {
			mySettingsArrayText = getResources().getStringArray(
					R.array.free_settings_text);
			mySettingsArrayImages = getResources().getStringArray(
					R.array.free_settings_images);
			List<DrawerChildBase> settings = new ArrayList<DrawerChildBase>();

			for (int i = 0; i < mySettingsArrayText.length; i++) {
				DrawerChildSettings child = new DrawerChildSettings();
				child.setText(mySettingsArrayText[i]);
				child.setImageName(mySettingsArrayImages[i]);
				child.setId("" + i);
				settings.add(child);
			}
			childList.put(groupItem.get(count++).getText(), settings);
		}

	}

	private void updateUI() {
		if (UserHelper.isLoggedIn()) {
			mState = true;
			user = userHelper.getUser();

			ImageLoader.getInstance().displayImage(user.getProfilePicsUrl(),
					profilePic, AppUtility.getOptionForUserImage(),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							pb.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// spinner.setVisibility(View.GONE);
							pb.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							pb.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});

			basicInfoPanel.setVisibility(View.VISIBLE);
			// basidInfoTitlePanel.setVisibility(View.VISIBLE);

			if (!TextUtils.isEmpty(userHelper.getUser().getNickName())) {
				switch (Integer.parseInt(userHelper.getUser().getNickName())) {
				case 1:
					setUserName(userHelper.getUser().getFirstName());
					break;
				case 2:
					setUserName(userHelper.getUser().getMiddleName());
					break;
				case 3:
					setUserName(userHelper.getUser().getLastName());
					break;
				default:
					break;
				}
			} else {
				userNameTextView.setText(userHelper.getUser().getEmail());
			}
			
			if(userHelper.getUser().getType()==UserTypeEnum.PARENTS){
				userTypeTextView.setText(R.string.java_homecontaineractivity_parent);
			}else{
				userTypeTextView.setText(userHelper.getUser().getType().toString());
			} 
			

		} else {

			basicInfoPanel.setVisibility(View.GONE);
			// basidInfoTitlePanel.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		updateUI();
		invalidateOptionsMenu();
		prepareListData();
		listAdapter = new DrawerExpandableListViewAdapter(this, groupItem,
				childList, childSelectionStates);
		expListView.setAdapter(listAdapter);
		expListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
	}

	public void setUserName(String name) {
		if (!TextUtils.isEmpty(name))
			userNameTextView.setText(name);
		else
			userNameTextView.setText(userHelper.getUser().getEmail());
	}

	@Override
	public void setContentView(final int layoutResID) {
		DrawerLayout layout = (DrawerLayout) getLayoutInflater().inflate(
				R.layout.activity_main_container_layout, null); // Your base
																// layout here
		FrameLayout actContent = (FrameLayout) layout
				.findViewById(R.id.content_frame);
		getLayoutInflater().inflate(layoutResID, actContent, true); // Setting
																	// the
																	// content
																	// of layout
																	// your
																	// provided
																	// to the
																	// act_content
																	// frame
		super.setContentView(layout);
		childSelectionStates = new HashMap<String, List<Boolean>>();
		doBaseTask();
		// here you can get your drawer buttons and define how they should
		// behave and what must they do, so you won't be needing to repeat it in
		// every activity class
	}

	private void doBaseTask() {
		uiHelper = new UIHelper(HomeContainerActivity.this);
		setUpActionBar();
		setUpDialog();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (RelativeLayout) findViewById(R.id.right_drawer);
		expListView = (ExpandableListView) mDrawerList.findViewById(R.id.lvExp);
		userNameTextView = (TextView) mDrawerList.findViewById(R.id.tv_name);
		basicInfoPanel = (LinearLayout) findViewById(R.id.basic_info_panel);
		// basidInfoTitlePanel=(LinearLayout)findViewById(R.id.basic_info_title);
		userTypeTextView = (TextView) mDrawerList.findViewById(R.id.tv_class);
		profilePic = (ImageView) findViewById(R.id.profile_image);
		profilePic.setOnClickListener(this);

		pb = (ProgressBar) findViewById(R.id.profile_pics_spinner);
		pb.setVisibility(View.GONE);
		// set a custom shadow that overlays the main content when the drawer
		// oepns
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.END);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				if (slideOffset == 0) {
					// invalidateOptionsMenu();
					Log.e("Drawer", "Bondho hoise");
				} else if (slideOffset != 0) {
					// invalidateOptionsMenu();
					Log.e("Drawer", "Open hoise");
				}
				super.onDrawerSlide(drawerView, slideOffset);
			}

			public void onDrawerClosed(View view) {
				expListView.expandGroup(0);
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu

			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	private void setUpDialog() {

		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		popupView = layoutInflater.inflate(R.layout.layout_dialog, null);

		popup = new PopupWindow(popupView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutSearch = (LinearLayout) popupView.findViewById(R.id.layoutSearch);

		keyHeight = popupView.getHeight();

		// adjustPopupHeight();
		final View root = getWindow().getDecorView().findViewById(
				android.R.id.content);
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						Rect r = new Rect();
						root.getWindowVisibleDisplayFrame(r);

						int screenHeight = root.getRootView().getHeight();
						int heightDifference = screenHeight
								- (r.bottom - r.top);
						//Log.e("Keyboard Size", "Size: " + heightDifference);
						// Log.e("Keyboard Size popup", "height: " +
						// popup.getHeight());

						// popup.setHeight(heightDifference);

						if (heightDifference > 100) {

							popup.setHeight((int) (screenHeight
									- heightDifference - (int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 85,
											getResources().getDisplayMetrics())));
						}

						else {
							popup.setHeight(screenHeight);
						}
					}
				});
	}

	private void adjustPopupHeight() {
		final View root = getWindow().getDecorView().findViewById(
				android.R.id.content);

		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						Rect r = new Rect();
						root.getWindowVisibleDisplayFrame(r);

						int screenHeight = root.getRootView().getHeight();
						int heightDifference = screenHeight
								- (r.bottom - r.top);
						//Log.e("Keyboard Size", "Size: " + heightDifference);
						// Log.e("Keyboard Size popup", "height: " +
						// popup.getHeight());

						// popup.setHeight(heightDifference);

						if (heightDifference > 100) {

							WindowManager.LayoutParams params = (WindowManager.LayoutParams) popupView
									.getLayoutParams();
							popupView.getLayoutParams();
							params.height = (int) (screenHeight
									- heightDifference - (int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 85,
											getResources().getDisplayMetrics()));
							popupView.setLayoutParams(params);
						}

						else {
							WindowManager.LayoutParams params = (WindowManager.LayoutParams) popupView
									.getLayoutParams();
							popupView.getLayoutParams();
							params.height = screenHeight;
							popupView.setLayoutParams(params);
						}

						// boolean visible = heightDiff > screenHeight / 3;
					}
				});
	}

	private void setUpActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		View cView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
		homeBtn = (ImageView) cView.findViewById(R.id.back_btn_home);
		homeBtn.setOnClickListener(this);
		logo = (ImageView) cView.findViewById(R.id.logo);
		actionBar.setCustomView(cView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		txtTitle = (TextView) cView.findViewById(R.id.txtTitle);
	}

	public void setActionBarTitle(String title) {
		logo.setVisibility(View.GONE);
		actionBar.setDisplayShowTitleEnabled(false);
		// actionBar.setTitle(Html.fromHtml("<html><body><marquee><font color='#312E2F'><b>"+title+"</b></font></marquee></body></html>"));
		// actionBar.setTitle(title);
		txtTitle.setVisibility(View.VISIBLE);
		txtTitle.setText(title);
		txtTitle.setSelected(true);
	}

	public void setActionBarNormal() {
		logo.setVisibility(View.VISIBLE);
		txtTitle.setVisibility(View.GONE);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);

		// Associate searchable configuration with the SearchView
		/*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) menu.findItem(
				R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
		if (mState) {
			menu.getItem(1).setVisible(false);
			if (userHelper.getUser().getAccessType() == UserAccessType.PAID) {
				menu.getItem(2).setVisible(true);
			} else
				menu.getItem(2).setVisible(false);
		} else
			menu.getItem(2).setVisible(false);

		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlate = searchView.findViewById(searchPlateId);
		searchPlate.setBackgroundColor(Color.parseColor("#4b5459"));

		// search_close_btn
		int closeBtnId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_close_btn", null, null);
		ImageView closeButton = (ImageView) searchView.findViewById(closeBtnId);
		closeButton.setImageResource(R.drawable.btn_search_close);

		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mSearchView.isIconified()) {
					mSearchView.setIconified(true);

					if (popup != null) {
						popup.dismiss();
						layoutSearch.removeAllViews();
					}
				}
			}
		});

		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText editText = (EditText) searchView.findViewById(id);

		searchManager.setOnCancelListener(new SearchManager.OnCancelListener() {

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				if (popup != null) {
					popup.dismiss();
				}
				layoutSearch.removeAllViews();

			}
		});

		MenuItem menuItem = menu.findItem(R.id.action_search);
		menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				Log.e("SHOWING_SEARCH", "yes");

				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				Log.e("SHOWING_SEARCH", "no");

				if (popup != null) {
					popup.dismiss();
				}
				layoutSearch.removeAllViews();

				return true;
			}
		});*/


        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_notification_new) {

                item.getActionView().setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(HomeContainerActivity.this,
                                NotificationActivity.class));

                    }
                });

                ImageButton ib = (ImageButton)item.getActionView().findViewById(R.id.btnNotification);
                ib.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeContainerActivity.this,
                                NotificationActivity.class));

                    }
                });

                txtNotificationCount = (TextView)item.getActionView().findViewById(R.id.txtNotificationCount);

                txtNotificationCount.setText(UserHelper.getTotalUnreadNotification());

                if(TextUtils.isEmpty(UserHelper.getTotalUnreadNotification()) || Integer.parseInt(UserHelper.getTotalUnreadNotification()) <= 0)
                {
                    txtNotificationCount.setVisibility(View.GONE);
                }
                else
                {
                    txtNotificationCount.setVisibility(View.VISIBLE);
                }


            }
        }




		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_drawer) {
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				mDrawerLayout.openDrawer(Gravity.RIGHT);
				expListView.expandGroup(0);
			}
		} /*else if (item.getItemId() == R.id.action_login) {
			startActivity(new Intent(HomeContainerActivity.this,
					LoginActivity.class));
		}*/


        /*else if (item.getItemId() == R.id.action_notification) {
			startActivity(new Intent(HomeContainerActivity.this,
					NotificationActivity.class));
		}*/




		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		/*MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchViewMenuItem.getActionView();
		int searchImgId = getResources().getIdentifier(
				"android:id/search_button", null, null);
		ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
		v.setImageResource(R.drawable.ic_action_search);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnCloseListener(this);

		int id = mSearchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) mSearchView.findViewById(id);
		textView.setTextColor(Color.WHITE);*/

		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub

		Log.e("TEXT_CHANGE_QUERY", "is: " + newText);

		if (rescheduleTimer == null) {
			r = new ScheduleTask();
			rescheduleTimer = new ReschedulableTimer();

			r.setTerm(newText);

			rescheduleTimer.schedule(r, 2 * 1000);

		}

		else {
			r.setTerm(newText);

			rescheduleTimer.reschedule(2 * 1000);
		}

		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub

		Log.e("SUBMITTED_QUERY", "is: " + query);

		return false;
	}

	private void initApiCall(String term) {

		RequestParams params = new RequestParams();
		params.put("term", term);
		AppRestClient.post(URLHelper.URL_FREE_VERSION_SEARCH, params,
				searchHandler);
	}

	private AsyncHttpResponseHandler searchHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			if (uiHelper != null)
				uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		@Override
		public void onSuccess(String responseString) {
			// Log.e("FREE_HOME", "data: "+responseString);
			layoutSearch.removeAllViews();

			if (popup != null)
				popup.dismiss();

			if (mSearchView.isIconified()) {
				if (popup != null) {
					popup.dismiss();
					layoutSearch.removeAllViews();
				}
			}

			if (uiHelper != null)
				uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {

				Log.e("SEARCH", "data: "
						+ modelContainer.getData().getAsJsonObject().toString());

				JsonArray arrayPost = modelContainer.getData()
						.getAsJsonObject().get("post").getAsJsonArray();

				dataWrap = new WrapAllData(arrayPost.toString());

				for (int i = 0; i < dataWrap.getListPost().size(); i++) {

					createSearchRow(i, dataWrap);
				}

				if (popup != null) {
					popup.showAsDropDown(mSearchView);
					adjustPopupHeight();
				}
			}

			else {

			}

		};

	};



    @Override
    public void onNotificationCountChanged(int count) {

        updateTextField(count);


    }



    @Override
    public void onNotificationCountChangedFromActivity(int count) {

        updateTextField(count);

    }


    private void updateTextField(final int value)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                txtNotificationCount.setText(String.valueOf(value));

                if(value <= 0)
                {
                    txtNotificationCount.setVisibility(View.GONE);
                }
                else
                {
                    txtNotificationCount.setVisibility(View.VISIBLE);
                }

            }
        });
    }





    public class ScheduleTask implements Runnable {

		private String term = "";

		public void run() {
			// Do schecule task
			Log.e("TIMER", "run finished called");

			if (!TextUtils.isEmpty(getTerm()))
				initApiCall(getTerm());

		}

		public String getTerm() {
			return term;
		}

		public void setTerm(String term) {
			this.term = term;
		}

		public ScheduleTask() {

		}

	}

	class ReschedulableTimer extends Timer {
		private Runnable task;
		private TimerTask timerTask;

		public void schedule(Runnable runnable, long delay) {
			task = runnable;
			timerTask = new TimerTask() {
				public void run() {
					task.run();
				}
			};

			this.schedule(timerTask, delay);
		}

		public void reschedule(long delay) {

			timerTask.cancel();
			timerTask = new TimerTask() {
				public void run() {
					task.run();
				}
			};
			this.schedule(timerTask, delay);
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.profile_image:

			// gallery/camera chooser here

			showPicChooserDialog();

			break;
		case R.id.back_btn_home:
			finish();
			break;
		default:
			break;
		}
		super.onClick(view);
	}

	private void showPicChooserDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HomeContainerActivity.this);

		alertDialogBuilder
				.setMessage(R.string.java_homecontaineractivity_change_profile_picture)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.java_createparentactivity_gallery),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

								mCameraGalleryPicker.openGallery();

								/*
								 * Intent intent = new Intent();
								 * intent.setType("image/*");
								 * intent.setAction(Intent.ACTION_GET_CONTENT);
								 * startActivityForResult
								 * (Intent.createChooser(intent,
								 * "Select Picture"), 1);
								 */



							}
						})
				.setNegativeButton(getString(R.string.java_createparentactivity_camera),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

								mCameraGalleryPicker.openCamere();

								/*
								 * Intent takePicture = new Intent(
								 * MediaStore.ACTION_IMAGE_CAPTURE);
								 * startActivityForResult(takePicture, 0);
								 */
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCancelable(true);

		alertDialog.show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		mCameraGalleryPicker.activityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

        GcmIntentService.listener = this;
        NotificationActivity.listenerActivity = this;

		mCameraGalleryPicker = new CameraGalleryPicker(this, this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);

	}

	/*
	 * private class DrawerItemClickListener implements OnItemClickListener {
	 *
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { Log.v(TAG, "ponies"); navigateTo(position); } }
	 */

	/*
	 * private void navigateTo(int position) { Log.v(TAG, "List View Item: " +
	 * position);
	 *
	 * switch (position) { case 0: getSupportFragmentManager()
	 * .beginTransaction() .replace(R.id.content_frame,
	 * HomePageFreeVersionNew.newInstance(),
	 * HomePageFreeVersionNew.TAG).commit(); break; case 1: break; } }
	 */

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	private View createSearchRow(final int position, final WrapAllData dataWrap) {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.row_search_single_item, null);

		LinearLayout layoutSearchContainer = (LinearLayout) view
				.findViewById(R.id.layoutSearchContainer);
		// layoutSearchContainer.setBackgroundResource(R.drawable.tap_search_container);
		TextView txtTitleName = (TextView) view.findViewById(R.id.txtTitleName);
		txtTitleName.setText(dataWrap.getListPost().get(position).getTitle());

		TextView txtCategoryName = (TextView) view
				.findViewById(R.id.txtCategoryName);
		txtCategoryName.setText(dataWrap.getListPost().get(position)
				.getCategoryName());

		ImageView imageViewImageLogo = (ImageView) view
				.findViewById(R.id.imageViewImageLogo);
		CustomRhombusIcon imageViewImageLogoRombus = (CustomRhombusIcon) view
				.findViewById(R.id.imageViewImageLogoRombus);

		if (!TextUtils.isEmpty(dataWrap.getListPost().get(position).getImage())) {

			imageViewImageLogoRombus.setVisibility(View.GONE);
			imageViewImageLogo.setVisibility(View.VISIBLE);

			SchoolApp.getInstance().displayUniversalImage(
					dataWrap.getListPost().get(position).getImage(),
					imageViewImageLogo);

		} else {

			imageViewImageLogoRombus.setVisibility(View.VISIBLE);
			imageViewImageLogo.setVisibility(View.GONE);

			imageViewImageLogoRombus.setIconImage(AppUtility
					.getResourceImageId(
							Integer.parseInt(dataWrap.getListPost()
									.get(position).getCategoryId()), true,
							false));

		}

		// imageViewImageLogo.setImageResource(AppUtility.getResourceImageId(Integer.parseInt(dataWrap.getListPost().get(position).getCategoryId()),
		// true));
		// SchoolApp.getInstance().displayUniversalImage(dataWrap.getListPost().get(position).getCategoryIconUrl(),
		// imageViewImageLogo);

		layoutSearch.addView(view);

		final LinearLayout layoutRightRedBar = (LinearLayout) view
				.findViewById(R.id.layoutRightRedBar);

		/*layoutSearchContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// layoutRightRedBar.setVisibility(View.VISIBLE);

				Log.e("TXT_VIEW", "click tv post");

				Intent intent = new Intent(HomeContainerActivity.this,
						SingleItemShowActivity.class);
				intent.putExtra(AppConstant.ITEM_ID, dataWrap.getListPost()
						.get(position).getId());
				intent.putExtra(AppConstant.ITEM_CAT_ID, dataWrap.getListPost()
						.get(position).getCategoryId());
				startActivity(intent);

				if (popup != null) {
					popup.dismiss();
					layoutSearch.removeAllViews();
				}

			}
		});*/

		layoutSearchContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					layoutRightRedBar.setVisibility(View.VISIBLE);

				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					layoutRightRedBar.setVisibility(View.VISIBLE);

				}

				else // if (event.getAction() == MotionEvent.ACTION_UP)
				{
					layoutRightRedBar.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});

		return view;

	}

	private InputMethodManager im;

	private void showSoftKeyboard(final InputMethodManager im,
			final EditText et, ImageView editIcon) {
		editIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				im.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
			}
		});

		et.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				et.setFocusableInTouchMode(true);
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);
				return false;
			}
		});
	}

	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
	}

	@Override
	public void onAuthenticationSuccessful() {
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		updateUI();

		// Toast.makeText(this, "Successfully updated Password",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		if (uiHelper.isDialogActive()) {
			uiHelper.dismissLoadingDialog();
		}
		uiHelper.showMessage(msg);
	}

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onBackPressed() {
		if (mSearchView!= null && !mSearchView.isIconified()) {
			mSearchView.setIconified(true);

			if (popup != null) {
				popup.dismiss();
				layoutSearch.removeAllViews();
			}

		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (popupView != null) {
			popupView = null;

		}
		if (popup.isShowing()) {
			popup.dismiss();
			popup = null;
		}

		if (uiHelper != null) {
			uiHelper.dismissLoadingDialog();
			uiHelper = null;
		}
	}

	@Override
	public void onPaswordChanged() {

	}

	@Override
	public void onPicturetaken(File path) {

		selectedImagePath = path.getAbsolutePath();

		if (!selectedImagePath.equalsIgnoreCase("")) {

			File myPicFile = new File(selectedImagePath);
			UserHelper userHelper = new UserHelper(
					HomeContainerActivity.this, HomeContainerActivity.this);
			userHelper.updateProfilePicture(myPicFile);

		}
	}
}