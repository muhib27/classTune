package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;

import java.util.HashMap;

public class RoutineFragment extends Fragment implements MyFragmentTabHost.OnTabChangeListener{



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}


	private MyFragmentTabHost mTabHostRoutine;
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
		rootView = inflater.inflate(R.layout.routine_layout,container, false);
		mTabHostRoutine = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_routine);

		initialiseTabHost(savedInstanceState);
	    return rootView;
	}


	/** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHostRoutine.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostRoutine.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_routine);
        TabInfo tabInfo = null;
        MyFragmentTabHost.TabSpec spec   =   mTabHostRoutine.newTabSpec(AppConstant.TAB_CLASS_ROUTINE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_class_routine), R.drawable.tab_events));
        addTab(this.mTabHostRoutine, spec, ( tabInfo = new TabInfo(AppConstant.TAB_CLASS_ROUTINE, StudentClassRoutineFragment.class, args)));

        spec   =   mTabHostRoutine.newTabSpec(AppConstant.TAB_WEEKLY_ROUTINE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_weekly_routine), R.drawable.tab_club_news));
        addTab(this.mTabHostRoutine, spec, ( tabInfo = new TabInfo(AppConstant.TAB_WEEKLY_ROUTINE, WeeklyRoutineFragment.class, args)));

        spec   =   mTabHostRoutine.newTabSpec(AppConstant.TAB_EXAM_ROUTINE);
        spec.setIndicator(getIndicatorView(getString(R.string.title_exam_routine), R.drawable.tab_archieve));
        addTab(this.mTabHostRoutine, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EXAM_ROUTINE, ExamRoutineFragment.class, args)));

        // Default to first tab
        this.onTabChanged(AppConstant.TAB_CLASS_ROUTINE);
        mTabHostRoutine.setOnTabChangedListener(this);
	}

	private View getIndicatorView(String text,int drawableId)
	{
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, this.mTabHostRoutine.getTabWidget(), false);
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
/*	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		   
         *  Navigation stacks for each tab gets created.. 
         *  tab identifier is used as key to get respective stack for each tab
         
        mStacks  =   new HashMap<String, Stack<Fragment>>();
        mStacks.put(AppConstant.TAB_CLASS_ROUTINE, new Stack<Fragment>());
        mStacks.put(AppConstant.TAB_WEEKLY_ROUTINE, new Stack<Fragment>());
        mStacks.put(AppConstant.TAB_EXAM_ROUTINE, new Stack<Fragment>());
	}


	MyFragmentTabHost mTabHost;
	View rootView;
	 A HashMap of stacks, where we use tab identifier as keys..
    private HashMap<String, Stack<Fragment>> mStacks;

    Save current tabs identifier in this..
    private String mCurrentTab;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}


	    private View getIndicatorView(String text,int drawableId)
		{
			View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, this.mTabHost.getTabWidget(), false);
			TextView title = (TextView) tabIndicator.findViewById(R.id.title);
			title.setText(text);
			ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
			icon.setImageResource(drawableId);
			return tabIndicator;
		}
	    

	    public void initializeTabs() {
	         Setup your tab icons and content views.. Nothing special in this..
	    	MyFragmentTabHost.TabSpec spec    =   mTabHost.newTabSpec(AppConstant.TAB_CLASS_ROUTINE);
	        mTabHost.setCurrentTab(-3);
	        spec.setContent(new MyFragmentTabHost.TabContentFactory() {
	            public View createTabContent(String tag) {
	                return rootView.findViewById(R.id.realtabcontent_routine);
	            }
	        });
	        spec.setIndicator(getIndicatorView(getString(R.string.title_class_routine), R.drawable.tab_events));
	        mTabHost.addTab(spec,ClassRoutineFragment.class,null);


	        spec                    =   mTabHost.newTabSpec(AppConstant.TAB_WEEKLY_ROUTINE);
	        spec.setContent(new MyFragmentTabHost.TabContentFactory() {
	            public View createTabContent(String tag) {
	                return rootView.findViewById(R.id.realtabcontent_routine);
	            }
	        });
	        spec.setIndicator(getIndicatorView(getString(R.string.title_weekly_routine), R.drawable.tab_club_news));
	        mTabHost.addTab(spec,WeeklyRoutineFragment.class,null);
	        
	        
	        spec                    =   mTabHost.newTabSpec(AppConstant.TAB_EXAM_ROUTINE);
	        spec.setContent(new MyFragmentTabHost.TabContentFactory() {
	            public View createTabContent(String tag) {
	                return rootView.findViewById(R.id.realtabcontent_routine);
	            }
	        });
	        spec.setIndicator(getIndicatorView(getString(R.string.title_exam_routine), R.drawable.tab_archieve));
	        mTabHost.addTab(spec,ExamRoutineFragment.class,null);
	    }
	    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.routine_layout,container, false);.,


	        mTabHost = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_routine);
	        mTabHost.setOnTabChangedListener(listener);
	        
	        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_routine);
	        initializeTabs();
	        
	        
	        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Fragment B"),
	                UpcomingEventsFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Fragment C"),
	                ClubNewsFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("Fragment D"),
	        		UpcomingEventsFragment.class, null);


	        return rootView;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}


	Comes here when user switch tab, or we do programmatically
	MyFragmentTabHost.OnTabChangeListener listener    =   new MyFragmentTabHost.OnTabChangeListener() {
      public void onTabChanged(String tabId) {
        Set current tab..
        mCurrentTab                     =   tabId;

        if(mStacks.get(tabId).size() == 0){
          
           *    First time this tab is selected. So add first fragment of that tab.
           *    Dont need animation, so that argument is false.
           *    We are adding a new fragment which is not present in stack. So add to stack is true.
           
          if(tabId.equals(AppConstant.TAB_CLASS_ROUTINE)){
            pushFragments(tabId, new ClassRoutineFragment(), false,true);
          }else if(tabId.equals(AppConstant.TAB_WEEKLY_ROUTINE)){
            pushFragments(tabId, new WeeklyRoutineFragment(), false,true);
          }
          else if(tabId.equals(AppConstant.TAB_EXAM_ROUTINE)){
              pushFragments(tabId, new ExamRoutineFragment(), false,true);
            }
        }else {
          
           *    We are switching tabs, and target tab is already has atleast one fragment. 
           *    No need of animation, no need of stack pushing. Just show the target fragment
           
          pushFragments(tabId, mStacks.get(tabId).lastElement(), false,false);
        }
      }
    };


     Might be useful if we want to switch tab programmatically, from inside any of the fragment.
    public void setCurrentTab(int val){
          mTabHost.setCurrentTab(val);
    }


     
     *      To add fragment to a tab. 
     *  tag             ->  Tab identifier
     *  fragment        ->  Fragment to show, in tab identified by tag
     *  shouldAnimate   ->  should animate transaction. false when we switch tabs, or adding first fragment to a tab
     *                      true when when we are pushing more fragment into navigation stack. 
     *  shouldAdd       ->  Should add to fragment navigation stack (mStacks.get(tag)). false when we are switching tabs (except for the first time)
     *                      true in all other cases.
     
    public void pushFragments(String tag, Fragment fragment,boolean shouldAnimate, boolean shouldAdd){
      if(shouldAdd)
          mStacks.get(tag).push(fragment);
      FragmentManager   manager         =   getActivity().getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      if(shouldAnimate)
          ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
      ft.replace(R.id.realtabcontent_routine, fragment);
      ft.commit();
    }


    public void popFragments(){
          
       *    Select the second last fragment in current tab's stack.. 
       *    which will be shown after the fragment transaction given below 
       
      Fragment fragment =   mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

      pop current fragment from stack.. 
      mStacks.get(mCurrentTab).pop();

       We have the target fragment in hand.. Just show it.. Show a standard navigation animation
      FragmentManager   manager         =   getActivity().getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
      ft.replace(R.id.realtabcontent_routine, fragment);
      //ft.attach(fragment);
      ft.commit();
    } 
*/
}
