package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.viewhelpers.CustomRhombusIcon;
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;

import java.util.HashMap;

public class TeacherHomeWorkFragment extends Fragment {

	
	
	private LinearLayout layoutFilter;

	public static IFilterClicked lsitener; 
	public static IFilterInsideClicked lsitenerInside; 
	
	private boolean isFilterClicked = false;
	private ImageView imgFilter;
	private LinearLayout layoutMidPanel;
	
	private LinearLayout layoutSubject;
	private LinearLayout layoutDate;
	

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
		rootView = inflater.inflate(R.layout.teacher_homework,container, false);

		layoutFilter = (LinearLayout)rootView.findViewById(R.id.layoutFilter);
		imgFilter = (ImageView)rootView.findViewById(R.id.imgFilter);
		layoutMidPanel = (LinearLayout)rootView.findViewById(R.id.layoutMidPanel);

		layoutSubject = (LinearLayout)rootView.findViewById(R.id.layoutSubject);
		layoutDate = (LinearLayout)rootView.findViewById(R.id.layoutDate);



		mTabHostEvts = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_homework_teacher);
		LinearLayout headerParent = (LinearLayout) rootView.findViewById(R.id.header_parent);
		CustomRhombusIcon icon = new CustomRhombusIcon(getActivity());
		icon.setIconImage(R.drawable.homework_tap);
		headerParent.addView(icon,0);
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
        MyFragmentTabHost.TabSpec spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_HOMEWORK_FEED);
        spec.setIndicator(getIndicatorView(getString(R.string.title_homework_feed), R.drawable.tab_homework_feed));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_HOMEWORK_FEED, TeacherHomeWorkFeedFragment.class, args)));
        
        spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_HOMEWORK_ADD);
        spec.setIndicator(getIndicatorView(getString(R.string.title_add_homework), R.drawable.tab_homework_add));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_HOMEWORK_ADD, TeacherHomeWorkAddFragment.class, args)));
        
       /* spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_DRAFT);
        spec.setIndicator(getIndicatorView(getString(R.string.title_draft), R.drawable.tab_draft));
        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_DRAFT, TeacherHomeWorkDraftFragment.class, args)));*/
        
        
        mTabHostEvts.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if(mTabHostEvts.getCurrentTab() == 0)
				{
					layoutFilter.setVisibility(View.VISIBLE);
				}
				else
				{
					layoutFilter.setVisibility(View.GONE);
				}
				
				
				
				
			}
		});
        
        
        
        layoutFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				isFilterClicked = !isFilterClicked;
				
				lsitener.onFilterClicked(isFilterClicked);
				
				if(isFilterClicked)
				{
					layoutFilter.setBackgroundColor(Color.parseColor("#b1b8ba"));
					imgFilter.setImageResource(R.drawable.filter_tap);
					
					layoutMidPanel.setVisibility(View.VISIBLE);
					
					//layoutMidPanel.animate().translationY(layoutMidPanel.getHeight());
				}
				
				else
				{
					layoutFilter.setBackgroundColor(Color.WHITE);
					imgFilter.setImageResource(R.drawable.filter_normal);
					
					layoutMidPanel.setVisibility(View.GONE);
				}
				
			}
		});
        
        
        
        layoutSubject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lsitenerInside.onFilterSubjectClicked();
			}
		});
		
		layoutDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lsitenerInside.onFilterDateClicked();
			}
		});
        
	}
	
	private View getIndicatorView(String text,int drawableId)
	{
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, this.mTabHostEvts.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(text);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
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
	

	
	public interface IFilterClicked{
		
		public void onFilterClicked(boolean isClicked);
		
	}
	
	
	public interface IFilterInsideClicked{
		
		public void onFilterSubjectClicked();
		public void onFilterDateClicked();
		
	}
	

}

