package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.TabContentFactory;

import com.classtune.app.R;
import com.classtune.app.schoolapp.viewhelpers.CustomTabButton;


public class ParentEventFragment extends Fragment {

	
	
		private CustomTabButton btnUpcomingEvents;
		private CustomTabButton btnArchivePage;
		


		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);


            /*if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");


                    GcmIntentService.initApiCall(rid, rtype);
                }
            }*/
			
		}


		//private MyFragmentTabHost mTabHostEvts;
		//private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ParentEventFragment.TabInfo>();
		//private TabInfo mLastTab = null;
		
		private View rootView;
		/**
		 * 
		 */
		/*private class TabInfo {
			 private String tag;
	         private Class<?> clss;
	         private Bundle args;
	         private Fragment fragment;
	         TabInfo(String tag, Class<?> clazz, Bundle args) {
	        	 this.tag = tag;
	        	 this.clss = clazz;
	        	 this.args = args;
	         }
	         
		}*/
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
			rootView = inflater.inflate(R.layout.parent_event_layout_custom,container, false);
			//mTabHostEvts = (MyFragmentTabHost)rootView.findViewById(R.id.tabhost_events);

			//initialiseTabHost(savedInstanceState);

			initView(rootView);

		    return rootView;
		}



		private void initView(View view)
		{
			//init first tab
			this.btnUpcomingEvents = (CustomTabButton)view.findViewById(R.id.tab_upcoming);
			this.btnUpcomingEvents.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.events_tap);
			Fragment fragment = new UpcomingEventsFragment();
			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.commit();


			this.btnUpcomingEvents.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btnUpcomingEvents.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.events_tap);
					btnArchivePage.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.archive_normal);


					Fragment fragment = new UpcomingEventsFragment();
                    if(!fragment.isAdded())
                        fragment.setArguments(getArguments());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.fragmentContainer, fragment);
		            fragmentTransaction.commit();



				}
			});


			this.btnArchivePage = (CustomTabButton)view.findViewById(R.id.tab_archive);

			this.btnArchivePage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btnArchivePage.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.archive_tap_black);
					btnUpcomingEvents.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.events_normal);


					Fragment fragment = new ArchievePageFragment();
                    if(!fragment.isAdded())
                        fragment.setArguments(getArguments());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.fragmentContainer, fragment);
		            fragmentTransaction.commit();
				}
			});


		}



		/** (non-Javadoc)
	     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	     */
	    public void onSaveInstanceState(Bundle outState) {
	        //outState.putString("tab", mTabHostEvts.getCurrentTabTag()); //save the tab selected
	        super.onSaveInstanceState(outState);
	    }
		/**
		 * Initialise the Tab Host
		 */
		/*private void initialiseTabHost(Bundle args) {
			mTabHostEvts.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent_events);
	        TabInfo tabInfo = null;
	        MyFragmentTabHost.TabSpec spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_EVENTS);
	        spec.setIndicator(getIndicatorView(getString(R.string.title_events_tab), R.drawable.tab_events));
	        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_EVENTS, UpcomingEventsFragment.class, args)));

	        spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_CLUB_NEWS);
	        spec.setIndicator(getIndicatorView(getString(R.string.title_club_news_tab), R.drawable.tab_club_news));
	        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_CLUB_NEWS, ClubNewsFragment.class, args)));

	        spec   =   mTabHostEvts.newTabSpec(AppConstant.TAB_ARCHIEVE);
	        spec.setIndicator(getIndicatorView(getString(R.string.title_archieve_tab), R.drawable.tab_archieve));
	        addTab(this.mTabHostEvts, spec, ( tabInfo = new TabInfo(AppConstant.TAB_ARCHIEVE, ArchievePageFragment.class, args)));



	        // Default to first tab
	        this.onTabChanged(AppConstant.TAB_EVENTS);
	        //
	        mTabHostEvts.setOnTabChangedListener(this);

		}*/

		/*private View getIndicatorView(String text,int drawableId)
		{
			View tabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator, this.mTabHostEvts.getTabWidget(), false);
			TextView title = (TextView) tabIndicator.findViewById(R.id.title);
			title.setText(text);
			ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
			icon.setImageResource(drawableId);
			return tabIndicator;
		}*/

		/**
		 * @param activity
		 * @param tabHost
		 * @param tabSpec
		 * @param clss
		 * @param args
		 */
		/*private void addTab(MyFragmentTabHost tabHost, MyFragmentTabHost.TabSpec tabSpec, TabInfo tabInfo) {
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
		}*/


		/** (non-Javadoc)
		 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
		 */
		/*public void onTabChanged(String tag) {
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
	                    ft.add(R.id.realtabcontent_events, newTab.fragment, newTab.tag);
	                } else {
	                    ft.attach(newTab.fragment);
	                }
	            }

	            mLastTab = newTab;
	            ft.commit();
	            this.getChildFragmentManager().executePendingTransactions();
			}
	    }*/
	
	
	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  
		 *  Navigation stacks for each tab gets created.. 
		 *  tab identifier is used as key to get respective stack for each tab
		 
		mStacks  =   new HashMap<String, Stack<Fragment>>();
		mStacks.put(AppConstant.TAB_EVENTS, new Stack<Fragment>());
		mStacks.put(AppConstant.TAB_CLUB_NEWS, new Stack<Fragment>());
		mStacks.put(AppConstant.TAB_ARCHIEVE, new Stack<Fragment>());
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


	public void initializeTabs(){
		 Setup your tab icons and content views.. Nothing special in this..
		MyFragmentTabHost.TabSpec spec    =   mTabHost.newTabSpec(AppConstant.TAB_EVENTS);
		mTabHost.setCurrentTab(-3);
		spec.setContent(new MyFragmentTabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return rootView.findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(getIndicatorView(getString(R.string.title_events_tab), R.drawable.tab_events));
		mTabHost.addTab(spec,UpcomingEventsFragment.class,null);


		spec                    =   mTabHost.newTabSpec(AppConstant.TAB_CLUB_NEWS);
		spec.setContent(new MyFragmentTabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return rootView.findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(getIndicatorView(getString(R.string.title_club_news_tab), R.drawable.tab_club_news));
		mTabHost.addTab(spec,ClubNewsFragment.class,null);


		spec                    =   mTabHost.newTabSpec(AppConstant.TAB_ARCHIEVE);
		spec.setContent(new MyFragmentTabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return rootView.findViewById(R.id.realtabcontent);
			}
		});
		spec.setIndicator(getIndicatorView(getString(R.string.title_archieve_tab), R.drawable.tab_archieve));
		mTabHost.addTab(spec,ArchievePageFragment.class,null);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.parent_tabhost_layout, container, false);


		mTabHost = (MyFragmentTabHost)rootView.findViewById(android.R.id.tabhost);
		mTabHost.setOnTabChangedListener(listener);
		
		CustomButton headerParent = (CustomButton) rootView.findViewById(R.id.header_parent);
		headerParent.setImage(R.drawable.icon_report_card);
		headerParent.setTitleText("Events");

		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
		initializeTabs();


		mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Fragment B"),
	                UpcomingEventsFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Fragment C"),
	                ClubNewsFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("Fragment D"),
	        		UpcomingEventsFragment.class, null);


		return rootView;
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
				 
				if(tabId.equals(AppConstant.TAB_EVENTS)){
					pushFragments(tabId, new UpcomingEventsFragment(), false,true);
				}else if(tabId.equals(AppConstant.TAB_CLUB_NEWS)){
					pushFragments(tabId, new ClubNewsFragment(), false,true);
				}
				else if(tabId.equals(AppConstant.TAB_ARCHIEVE)){
					pushFragments(tabId, new ArchievePageFragment(), false,true);
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
		ft.replace(R.id.realtabcontent, fragment);
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
		ft.replace(R.id.realtabcontent, fragment);
		//ft.attach(fragment);
		ft.commit();
	} */


}
