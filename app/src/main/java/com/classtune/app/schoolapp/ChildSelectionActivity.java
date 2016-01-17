package com.classtune.app.schoolapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.UserPaidInfo;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomIndicator;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

import java.util.ArrayList;

import roboguice.activity.RoboFragmentActivity;


public class ChildSelectionActivity extends RoboFragmentActivity{

	SchoolApp app;
	UIHelper uiHelper;
	UserHelper userHelper;
	//ListView listView;
	//EfficientAdapter adapter;
	ArrayList<UserPaidInfo> childrenList;
	
	
	
	//recreating
	private ImageView imgViewProfile;
	private ImageButton btnPrevious;
	private ImageButton btnNext;
	private TextView txtChildName;
	private TextView txtSchoolName;
	private TextView txtBatchSection;
	private ProgressBar progressBar;
	
	private int currentPosition = 0;
	
	private  float density;
	
	private CustomIndicator customIndicator;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_children_selection3);
		
		density = getResources().getDisplayMetrics().density;
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		app = (SchoolApp) getApplicationContext();
		userHelper = new UserHelper(this);
		uiHelper = new UIHelper(this);


		childrenList = userHelper.getUser().getChildren();
		
		//Log.e("list size", userHelper.getUser().getChildren().size()+"");

		//listView = (ListView) findViewById(R.id.listView_children);
		//adapter = new EfficientAdapter(this, childrenList);
		
		//listView.setAdapter(adapter);
		/*listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				userHelper.storeCurrentChildInfo(childrenList.get(position));
				Intent paidIntent = new Intent(ChildSelectionActivity.this,
						HomePageFreeVersion.class);
				paidIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(paidIntent);
				setResult(RESULT_OK);
				ChildSelectionActivity.this.finish();
			}
		});*/
		
		initView();
		initAction();

        Log.e("COVER", "is: " + UserHelper.getSchoolCover());

	}

	
	
	private void initView()
	{
		imgViewProfile = (ImageView)this.findViewById(R.id.imgViewProfile);
		btnPrevious = (ImageButton)this.findViewById(R.id.btnPrevious);
		btnNext = (ImageButton)this.findViewById(R.id.btnNext);
		
		txtChildName = (TextView)this.findViewById(R.id.txtChildName);
		txtSchoolName = (TextView)this.findViewById(R.id.txtSchoolName);
		txtBatchSection = (TextView)this.findViewById(R.id.txtBatchSection);
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		
		
		imgViewProfile.setScaleType(ScaleType.FIT_XY);
		imgViewProfile.setAdjustViewBounds(true);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) AppUtility.getDeviceIndependentDpFromPixel(this, 125), (int) AppUtility.getDeviceIndependentDpFromPixel(this, 125));
		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(135 * density), (int)(135 * density));
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		imgViewProfile.setLayoutParams(params);
		imgViewProfile.requestLayout();
		
		
		
		Log.e("DENSITY", "is: " + density);
		
		
		customIndicator = (CustomIndicator)this.findViewById(R.id.customIndicator);


	}
	
	private void initAction()
	{

            refreshData(currentPosition);
		btnPrevious.setVisibility(View.INVISIBLE);
		
		
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentPosition--;
				refreshData(currentPosition);
				customIndicator.setSelectedIndicator(currentPosition);
			}
		});
		
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentPosition++;
				refreshData(currentPosition);
				customIndicator.setSelectedIndicator(currentPosition);
				
			}
		});
		
		
		imgViewProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userHelper.storeCurrentChildInfo(childrenList.get(currentPosition));
				/*Intent paidIntent = new Intent(ChildSelectionActivity.this,
						HomePageFreeVersion.class);
				paidIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(paidIntent);*/
				setResult(RESULT_OK);
				ChildSelectionActivity.this.finish();
				
				
			}
		});
		
		
		customIndicator.initView(this, childrenList.size());
		customIndicator.setSelectedIndicator(currentPosition);
		
		if(childrenList.size() <=1)
		{
			customIndicator.setVisibility(View.GONE);
		}
		else
		{
			customIndicator.setVisibility(View.VISIBLE);
		}


	}
	
	
	private void refreshData(int currentPosition)
	{
		if(!TextUtils.isEmpty(childrenList.get(currentPosition).getProfile_image()))
		{
			SchoolApp.getInstance().displayUniversalImage(childrenList.get(currentPosition).getProfile_image(), imgViewProfile, progressBar);
		}
		else
		{
			imgViewProfile.setImageResource(R.drawable.user_avatar);
			progressBar.setVisibility(View.GONE);
		}
		
		
		txtChildName.setText(childrenList.get(currentPosition).getFullName());
		txtSchoolName.setText(childrenList.get(currentPosition).getSchool_name());
		txtBatchSection.setText(childrenList.get(currentPosition).getBatch_name()+"  "+childrenList.get(currentPosition).getCourse_name()+",  Section "+childrenList.get(currentPosition).getSection_name());
	
		
		if(currentPosition <= 0)
		{
			btnPrevious.setVisibility(View.INVISIBLE);
		}
		else
		{
			btnPrevious.setVisibility(View.VISIBLE);
		}
		
		if(currentPosition == childrenList.size()-1)
		{
			btnNext.setVisibility(View.INVISIBLE);
		}
		else
		{
			btnNext.setVisibility(View.VISIBLE);
		}
		
		
		if(childrenList.size() <= 0)
		{
			btnPrevious.setVisibility(View.INVISIBLE);
			btnNext.setVisibility(View.INVISIBLE);
		}
		else
		{
			
		}
		
		
	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/*private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		//        ViewHolder holder;

		private ArrayList<UserPaidInfo> list;

		public EfficientAdapter(Context context, ArrayList<UserPaidInfo> list) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		*//**
		 * The number of items in the list is determined by the number of speeches
		 * in our array.
		 *
		 * @see android.widget.ListAdapter#getCount()
		 *//*
		public int getCount() {
			return list.size();
		}

		*//**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 *
		 * @see android.widget.ListAdapter#getItem(int)
		 *//*
		public Object getItem(int position) {
			return position;
		}

		*//**
		 * Use the array index as a unique id.
		 *
		 * @see android.widget.ListAdapter#getItemId(int)
		 *//*
		public long getItemId(int position) {
			return position;
			//dfsdf
		}

		*//**
		 * Make a view to hold each row.
		 *
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 *//*
		//		public View getView(int position, View convertView, ViewGroup parent) {
		//
		//
		//		}


		class ViewHolder {
			TextView tvName;
			TextView tvSchool;
			ImageView imgStudent;
			TextView tvclassandSection;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;
			// When convertView is not null, we can reuse it directly, there is no need
			// to reinflate it. We only inflate a new View when the convertView supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_children_selection, null);

				// Creates a ViewHolder and store references to the two children views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tvSchool = (TextView) convertView.findViewById(R.id.tv_school);
				holder.imgStudent = (ImageView) convertView.findViewById(R.id.imageView_photo);
				holder.tvclassandSection  = (TextView)convertView.findViewById(R.id.tv_class);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			
			// Bind the data efficiently with the holder.
			UserPaidInfo childInfo = list.get(position);
			holder.tvName.setText(childInfo.getFullName());
			holder.tvSchool.setText(childInfo.getSchool_name());
			holder.tvclassandSection.setText(childInfo.getBatch_name()+"  "+childInfo.getCourse_name()+"  Section "+childInfo.getSection_name());
			if(!TextUtils.isEmpty(childInfo.getProfile_image()))
				SchoolApp.getInstance().displayUniversalImage(childInfo.getProfile_image(), holder.imgStudent);
			

			return convertView;
		}

		public ArrayList<UserPaidInfo> getList() {
			return list;
		}
	}*/

	@Override
	public void onBackPressed() {

		userHelper.storeCurrentChildInfo(childrenList.get(0));

		setResult(RESULT_OK);
		super.onBackPressed();
	}
}
