package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.android.gms.tasks.TaskExecutors;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ParentAcademicCalender extends UserVisibleHintFragment implements MyFragmentTabHost.OnTabChangeListener{

	private Button btnDownload;
	private UserHelper userHelper;
	private UIHelper uiHelper;
	private String attachmentId = "";
	private LinearLayout layoutDownloadHolder;

	public void clearAllTabsData(){
		/*FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.hide(mLastTab.fragment);
		ft.detach(mLastTab.fragment);
		ft.remove(mLastTab.fragment);*/

		//mTabHostAC.getTabWidget().removeAllViews();
		mTabHostAC.clearAllTabs();
		mapTabInfo.clear();
		mLastTab=null;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userHelper = new UserHelper(getActivity());
		uiHelper = new UIHelper(getActivity());
	}

	private void initEmptyTab(Bundle args) {
		mTabHostAC.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_ac);
		TabInfo tabInfo = null;
		MyFragmentTabHost.TabSpec spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_EMPTY);
		spec.setIndicator(getIndicatorView(getString(R.string.title_exam_calendar_tab)));
		addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EMPTY, EmptyFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		this.onTabChanged(AppConstant.TAB_EMPTY);
		//
		mTabHostAC.setOnTabChangedListener(this);
	}

	/*@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if(isVisibleToUser)
		{
			Log.e(TAG, "dekhaise");
			clearAllTabsData();
			initialiseTabHost(bundle);
			isLoadedData=true;

		}


	}*/

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String text = String.format(
				getResources().getString(R.string.academic_calendar_title_text),
				AppUtility.getCurrentYear());
		//screenTitleText.setText(text);
		//currentDateText.setText(AppUtility.getCurrentDate(AppUtility.DATE_FORMAT_APP));


	}


	private MyFragmentTabHost mTabHostAC;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;

	private View rootView;
	//private TextView screenTitleText,currentDateText;
	private static String TAG="Parent Academic Calendar!!!!!";
	private boolean isLoadedData=false;

	private Bundle bundle;
	/**
	 *
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;
		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}
	/**
	 *
	 *
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/** (non-Javadoc)
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.parent_academic_calender_layout,container, false);
		mTabHostAC = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_ac);
		//screenTitleText=(TextView) rootView.findViewById(R.id.academic_calendar_title);
		//currentDateText=(TextView) rootView.findViewById(R.id.current_date_text);
		bundle=savedInstanceState;
		//initialiseTabHost(bundle);
		initEmptyTab(bundle);

		btnDownload = (Button)rootView.findViewById(R.id.btnDownload);
		btnDownload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("CLICKED", "ok");
				initDownload();
			}
		});

		layoutDownloadHolder = (LinearLayout)rootView.findViewById(R.id.layoutDownloadHolder);

		initApiCallHasCalendar();

		return rootView;
	}


	private void initApiCallHasCalendar(){
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if(userHelper.getUser().getType()==UserTypeEnum.PARENTS) {
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
		}

		AppRestClient.post(URLHelper.URL_HAS_ACADEMIC_CALENDAR, params,
				hasDownloadHandler);
	}

	AsyncHttpResponseHandler hasDownloadHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			uiHelper.showMessage(getString(R.string.internet_error_text));
			uiHelper.dismissLoadingDialog();
		}

		@Override
		public void onStart() {
			super.onStart();
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();


			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {

				boolean hasId = modelContainer.getData().get("acacal").getAsJsonObject().has("id");

				if(hasId){
					layoutDownloadHolder.setVisibility(View.VISIBLE);
					attachmentId = modelContainer.getData().get("acacal").getAsJsonObject().get("id").getAsString();
				}else {
					layoutDownloadHolder.setVisibility(View.GONE);
				}



			} else {

			}
		}
	};


	private void initDownload() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse("http://api.champs21.com/api/acacal/downloadattachment/id/" + attachmentId)));
	}



	/** (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHostAC.getCurrentTabTag()); //save the tab selected
		super.onSaveInstanceState(outState);
	}
	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostAC.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_ac);
		TabInfo tabInfo = null;
		MyFragmentTabHost.TabSpec spec;
		if((new UserHelper(getActivity()).getUser().getType()==UserTypeEnum.STUDENT)){
			spec =  mTabHostAC.newTabSpec(AppConstant.TAB_EXAM_CALENDAR);
			spec.setIndicator(getIndicatorView(getString(R.string.title_exam_calendar_tab)));
			addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EXAM_CALENDAR, AcademicCalendarExam.class, args)));
		}

		spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_EVENTS_CALENDAR);
		spec.setIndicator(getIndicatorView(getString(R.string.title_events_calendar_tab)));
		addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EVENTS_CALENDAR, AcademicCalendarEvents.class, args)));

		spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_HOLIDAYS_CALENDAR);
		spec.setIndicator(getIndicatorView(getString(R.string.title_holidays_calendar_tab)));
		addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_HOLIDAYS_CALENDAR, AcademicCalendarHolidays.class, args)));

		spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_OTHERS_CALENDAR);
		spec.setIndicator(getIndicatorView(getString(R.string.title_others_calendar_tab)));
		addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_OTHERS_CALENDAR, AcademicCalendarOthers.class, args)));


		// Default to first tab
		this.onTabChanged(AppConstant.TAB_EXAM_CALENDAR);
		//
		mTabHostAC.setOnTabChangedListener(this);

	}

	private View getIndicatorView(String text)
	{
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator_attendance, this.mTabHostAC.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(text);
		return tabIndicator;
	}

	/**
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private void addTab(MyFragmentTabHost tabHost, MyFragmentTabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(getActivity()));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state.  If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = getChildFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = getChildFragmentManager().beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			getChildFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec,tabInfo.clss,null);
	}


	/** (non-Javadoc)
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(getActivity(),
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent_ac, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			this.getChildFragmentManager().executePendingTransactions();
		}
	}



	@Override
	protected void onVisible() {
		// TODO Auto-generated method stub
		clearAllTabsData();
		initialiseTabHost(bundle);
		isLoadedData=true;
	}


	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub


	}


	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		String text = String.format(
				getResources().getString(R.string.academic_calendar_title_text),
				AppUtility.getCurrentYear());
		screenTitleText.setText(text);
		currentDateText.setText(AppUtility.getCurrentDate());
	}


	private MyFragmentTabHost mTabHostAC;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ParentAcademicCalender.TabInfo>();
	private TabInfo mLastTab = null;

	private View rootView;
	private TextView screenTitleText,currentDateText;
	private Bundle bundle;
	 *//**
	 *
	 *//*
	private class TabInfo {
		 private String tag;
         private Class<?> clss;
         private Bundle args;
         private Fragment fragment;
         TabInfo(String tag, Class<?> clazz, Bundle args) {
        	 this.tag = tag;
        	 this.clss = clazz;
        	 this.args = args;
         }

	}
	  *//**
	  *
	  *
	  *//*
	class TabFactory implements TabContentFactory {

		private final Context mContext;

	   *//**
	   * @param context
	   *//*
	    public TabFactory(Context context) {
	        mContext = context;
	    }

	    *//** (non-Javadoc)
	    * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	    *//*
	    public View createTabContent(String tag) {
	        View v = new View(mContext);
	        v.setMinimumWidth(0);
	        v.setMinimumHeight(0);
	        return v;
	    }

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.parent_academic_calender_layout,container, false);
		mTabHostAC = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_ac);
		screenTitleText=(TextView) rootView.findViewById(R.id.academic_calendar_title);
		currentDateText=(TextView) rootView.findViewById(R.id.current_date_text);
		//initialiseTabHost(savedInstanceState);
		bundle=savedInstanceState;

		//initEmptyTab(bundle);
		initialiseTabHost(savedInstanceState);

	    return rootView;
	}


	private void initEmptyTab(Bundle args) {

        TabInfo tabInfo = null;
        MyFragmentTabHost.TabSpec spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_EMPTY);
        spec.setIndicator(getIndicatorView(getString(R.string.title_exam_calendar_tab)));
        addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EMPTY, EmptyFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
     // Default to first tab
        this.onTabChanged(AppConstant.TAB_EMPTY);
        //
        mTabHostAC.setOnTabChangedListener(this);
	}


	     *//** (non-Javadoc)
	     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	     *//*
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHostAC.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
	      *//**
	      * Initialise the Tab Host
	      *//*
	private void initialiseTabHost(Bundle args) {
		mTabHostAC.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_ac);
        TabInfo tabInfo = null;
        MyFragmentTabHost.TabSpec spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_EXAM_CALENDAR);
        spec.setIndicator(getIndicatorView(getString(R.string.title_exam_calendar_tab)));
        addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EXAM_CALENDAR, AcademicCalendarExam.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_EVENTS_CALENDAR);
        spec.setIndicator(getIndicatorView(getString(R.string.title_events_calendar_tab)));
        addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EVENTS_CALENDAR, AcademicCalendarEvents.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_HOLIDAYS_CALENDAR);
        spec.setIndicator(getIndicatorView(getString(R.string.title_holidays_calendar_tab)));
        addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_HOLIDAYS_CALENDAR, AcademicCalendarHolidays.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        spec   =   mTabHostAC.newTabSpec(AppConstant.TAB_OTHERS_CALENDAR);
        spec.setIndicator(getIndicatorView(getString(R.string.title_others_calendar_tab)));
        addTab(this.mTabHostAC, spec, ( tabInfo = new TabInfo(AppConstant.TAB_OTHERS_CALENDAR, AcademicCalendarOthers.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        // Default to first tab
        this.onTabChanged(AppConstant.TAB_EXAM_CALENDAR);
        //
        mTabHostAC.setOnTabChangedListener(this);

	}

	private View getIndicatorView(String text)
	{
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator_attendance, this.mTabHostAC.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(text);
		return tabIndicator;
	}

	       *//**
	       * @param activity
	       * @param tabHost
	       * @param tabSpec
	       * @param clss
	       * @param args
	       *//*
	private void addTab(MyFragmentTabHost tabHost, MyFragmentTabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(getActivity()));
        String tag = tabSpec.getTag();

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        tabInfo.fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.detach(tabInfo.fragment);
            ft.commit();
            getChildFragmentManager().executePendingTransactions();
        }

        tabHost.addTab(tabSpec,tabInfo.clss,null);
	}


	        *//** (non-Javadoc)
	        * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	        *//*
	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
            		ft.detach(mLastTab.fragment);

                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(getActivity(),
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.realtabcontent_ac, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
            ft.commit();
            this.getChildFragmentManager().executePendingTransactions();
		}
    }


	public void clearAllTabsData(){
		FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
		ft.hide(mLastTab.fragment);
		ft.detach(mLastTab.fragment);
		ft.remove(mLastTab.fragment);
		mTabHostAC.getTabWidget().removeAllViews();
		mTabHostAC.clearAllTabs();
		mapTabInfo.clear();
		mLastTab=null;
		}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub

	}*/


}
