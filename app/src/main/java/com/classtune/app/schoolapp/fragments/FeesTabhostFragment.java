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

public class FeesTabhostFragment extends UserVisibleHintFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private MyFragmentTabHost mTabHostFees;
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

		/**
		 * (non-Javadoc)
		 *
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
		rootView = inflater
				.inflate(R.layout.fees_tabhost_layout, container, false);
		mTabHostFees = (MyFragmentTabHost) rootView
				.findViewById(R.id.tabhost_fees);

		initialiseTabHost(savedInstanceState);
		return rootView;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHostFees.getCurrentTabTag()); // save the
																	// tab
																	// selected
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostFees.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent_fees);
		TabInfo tabInfo = null;
		MyFragmentTabHost.TabSpec spec = mTabHostFees
				.newTabSpec(AppConstant.TAB_FEES_DUE);
		spec.setIndicator(getIndicatorView(
				getString(R.string.tab_fees_due),
				R.drawable.tab_fees_due));
		
		
		
		
		addTab(this.mTabHostFees, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_FEES_DUE,
				FeesDueFragment.class, args)));

		
		
		
		spec = mTabHostFees.newTabSpec(AppConstant.TAB_FEES_HISTORY);
		spec.setIndicator(getIndicatorView(
				getString(R.string.tab_fees_history),
				R.drawable.tab_fees_history));
		
		
		
		
		
		addTab(this.mTabHostFees, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_FEES_HISTORY, FeesHistoryFragment.class,
				args)));

		

	}

	private View getIndicatorView(String text, int drawableId) {
		View tabIndicator = LayoutInflater.from(getActivity())
				.inflate(R.layout.tab_indicator,
						this.mTabHostFees.getTabWidget(), false);
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
	private void addTab(MyFragmentTabHost tabHost,
			MyFragmentTabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(getActivity()));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = getChildFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			getChildFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec, tabInfo.clss, null);
	}

	@Override
	protected void onVisible() {

	}

	@Override
	protected void onInvisible() {

	}

}
