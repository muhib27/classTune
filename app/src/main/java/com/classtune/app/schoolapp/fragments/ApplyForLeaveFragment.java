package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

import java.util.HashMap;

public class ApplyForLeaveFragment extends Fragment {


	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(getActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}


	private MyFragmentTabHost mTabHostEvts;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;

	private View rootView;
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
		rootView = inflater.inflate(R.layout.teacher_leave_main,container, false);
		mTabHostEvts = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_leave_teacher);
		/*CustomButton headerParent = (CustomButton) rootView.findViewById(R.id.header_parent);
		headerParent.setImage(R.drawable.reportcard_rombus);
		headerParent.setTitleText("Events");*/
		initialiseTabHost(savedInstanceState);
	    return rootView;
	}


	/** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHostEvts.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostEvts.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_events);
        TabInfo tabInfo = null;
        
        
        
        MyFragmentTabHost.TabSpec spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_LEAVE_APPLICATION);
        spec.setIndicator(getIndicatorView(getString(R.string.title_leave_application)));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_LEAVE_APPLICATION, LeaveApplicationFragment.class, args)));
        
        
        if (userHelper.getUser().getType() != UserTypeEnum.PARENTS) 
		{
        	spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_STUDENT_LEAVE);
            spec.setIndicator(getIndicatorView(getString(R.string.title_student_leave)));
            addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_STUDENT_LEAVE, StudentLeaveFragment.class, args)));
		}
        
        /*spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_STUDENT_LEAVE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_student_leave)));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_STUDENT_LEAVE, StudentLeaveFragment.class, args)));*/
        
        spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_MY_LEAVE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_my_leave)));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_MY_LEAVE, MyLeaveFragment.class, args)));
        
        
        
        
	}
	private View getIndicatorView(String text) {
		View tabIndicator = LayoutInflater.from(getActivity()).inflate( R.layout.tab_indicator_attendance, this.mTabHostEvts.getTabWidget(), false);
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
	

	

}

