package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.classtune.app.R;
import com.classtune.app.schoolapp.SocialBaseActivity;

public class ChildContainerActivity extends SocialBaseActivity implements
		OnQueryTextListener, OnClickListener, SearchView.OnCloseListener {

    
	private static final String TAG = ChildContainerActivity.class.getSimpleName();
	private ActionBar actionBar;
	public ImageView homeBtn,logo;

	@Override
	protected void onResume() {
		super.onResume();
		invalidateOptionsMenu();
	}

	
	
	
	@Override
	public void setContentView(final int layoutResID) {
		FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(
				R.layout.activity_child_container_layout, null); // Your base layout here
		
		getLayoutInflater().inflate(layoutResID, layout, true); // Setting
																	// the
																	// content
																	// of layout
																	// your
																	// provided
																	// to the
																	// act_content
																	// frame
		super.setContentView(layout);
		doBaseTask();
		// here you can get your drawer buttons and define how they should
		// behave and what must they do, so you won't be needing to repeat it in
		// every activity class
	}


	private void doBaseTask() {
		setUpActionBar();
		
	}

	
	private void setUpActionBar(){
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		View cView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
		homeBtn = (ImageView) cView.findViewById(R.id.back_btn_home);
		homeBtn.setOnClickListener(this);
		homeBtn.setVisibility(View.VISIBLE);
		logo = (ImageView) cView.findViewById(R.id.logo);
		logo.setVisibility(View.GONE);
		actionBar.setCustomView(cView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_child, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
		
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);

	}

	
	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.back_btn_home:
			finish();
			break;
		default:
			break;
		}
		super.onClick(view);
	}




	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAuthenticationSuccessful() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}



}