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
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;

import java.util.HashMap;


public class ParentAttendenceFragment extends Fragment implements MyFragmentTabHost.OnTabChangeListener{


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	private MyFragmentTabHost mTabHostAtt;
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
		rootView = inflater.inflate(R.layout.parent_attendance_layout,container, false);
		mTabHostAtt = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_att);
		initialiseTabHost(savedInstanceState);
	    return rootView;
	}


	/** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHostAtt.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostAtt.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_attendance);
        TabInfo tabInfo = null;
        MyFragmentTabHost.TabSpec spec   =   mTabHostAtt.newTabSpec(AppConstant.TAB_MONTHLY_ATTENDANCE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_monthly_attendance_tab)));
        addTab(this.mTabHostAtt, spec, ( tabInfo = new TabInfo(AppConstant.TAB_MONTHLY_ATTENDANCE, AttendenceFragment.class, args)));

        spec   =   mTabHostAtt.newTabSpec(AppConstant.TAB_YEARLY_ATTENDANCE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_yearly_attendance_tab)));
        addTab(this.mTabHostAtt, spec, ( tabInfo = new TabInfo(AppConstant.TAB_YEARLY_ATTENDANCE, YearlyAttendanceReportFragment.class, args)));

		spec   =   mTabHostAtt.newTabSpec(AppConstant.TAB_SUBJECT_ATTENDANCE);
		spec.setIndicator(getIndicatorView(getString(R.string.title_subject_attendance_tab)));
		addTab(this.mTabHostAtt, spec, ( tabInfo = new TabInfo(AppConstant.TAB_SUBJECT_ATTENDANCE, StudentParentSubjectReport.class, args)));


        // Default to first tab
        this.onTabChanged(AppConstant.TAB_MONTHLY_ATTENDANCE);
        //
        mTabHostAtt.setOnTabChangedListener(this);

	}

	private View getIndicatorView(String text)
	{
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator_attendance, this.mTabHostAtt.getTabWidget(), false);
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

            if(!tabInfo.fragment.isAdded())
                tabInfo.fragment.setArguments(getArguments());

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
	
	
	
	
}
