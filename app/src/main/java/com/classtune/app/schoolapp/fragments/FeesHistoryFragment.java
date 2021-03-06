package com.classtune.app.schoolapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.FeesHistory;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeesHistoryFragment extends Fragment {
	

	
	private View view;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	private ListView listViewFeesDue;
	private FeesHistoryAdapter adapter;
	
	
	private List<FeesHistory> listDue;
	
	private TextView txtMessage;
	
	
	
	private TextView txtDueDateHeader;
	private TextView txtDescriptionHeader;
	private TextView txtAmountHeader;
	private TextView txtStatusHeader;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(getActivity());
		
		listDue = new ArrayList<FeesHistory>();
		
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initApiCall();
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_feeshistoryfragment,
				container, false);
		
		initView(view);
		
		return view;
	}
	
	
	
	private void initView(View view)
	{
		txtDueDateHeader = (TextView)view.findViewById(R.id.txtDueDateHeader);
		txtDueDateHeader.setSelected(true);
		txtDescriptionHeader = (TextView)view.findViewById(R.id.txtDescriptionHeader);
		txtDescriptionHeader.setSelected(true);
		txtAmountHeader = (TextView)view.findViewById(R.id.txtAmountHeader);
		txtAmountHeader.setSelected(true);
		txtStatusHeader = (TextView)view.findViewById(R.id.txtStatusHeader);
		txtStatusHeader.setSelected(true);
		
		
		
		listViewFeesDue = (ListView)view.findViewById(R.id.listViewFeesDue);
		adapter = new FeesHistoryAdapter();
		listViewFeesDue.setAdapter(adapter);
		
		txtMessage = (TextView)view.findViewById(R.id.txtMessage);
	}
	
	
	private void initApiCall()
	{

		HashMap<String,String> params = new HashMap<>();

	
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		
		//AppRestClient.post(URLHelper.URL_FEES, params, feesDueHandler);
		fees(params);
	
	}
	
	private void fees(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().fees(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();
						if (response.body() != null){
							Wrapper modelContainer = GsonParser.getInstance()
									.parseServerResponse2(response.body());

							if (modelContainer.getStatus().getCode() == 200) {


								JsonArray  arrayHistory = modelContainer.getData().get("history").getAsJsonArray();
								listDue = parseFeesHistoryList(arrayHistory.toString());

								adapter.notifyDataSetChanged();

								if(listDue.size() <= 0)
								{
									txtMessage.setVisibility(View.VISIBLE);
								}
								else
								{
									txtMessage.setVisibility(View.GONE);
								}

							}
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}
					}
				}
		);
	}
	AsyncHttpResponseHandler feesDueHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			
				uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
			

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			

			uiHelper.dismissLoadingDialog();


			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				
				JsonArray  arrayHistory = modelContainer.getData().get("history").getAsJsonArray();
				listDue = parseFeesHistoryList(arrayHistory.toString());
				
				adapter.notifyDataSetChanged();
				
				if(listDue.size() <= 0)
				{
					txtMessage.setVisibility(View.VISIBLE);
				}
				else
				{
					txtMessage.setVisibility(View.GONE);
				}
				
			}
			
			else {

			}
			
			

		};
	};
	
	
	
	private List<FeesHistory> parseFeesHistoryList(String object) {

		List<FeesHistory> tags = new ArrayList<FeesHistory>();
		Type listType = new TypeToken<List<FeesHistory>>() {
		}.getType();
		tags = (List<FeesHistory>) new Gson().fromJson(object, listType);
		return tags;
	}
	
	
	
	private class FeesHistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listDue.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listDue.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_feeshistory,null,false);
                
                holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
                holder.txtBalance = (TextView)convertView.findViewById(R.id.txtBalance);
                holder.txtDueDate = (TextView)convertView.findViewById(R.id.txtDueDate);
                holder.layoutStatus = (LinearLayout)convertView.findViewById(R.id.layoutStatus);
                holder.txtStatus = (TextView)convertView.findViewById(R.id.txtStatus);
               
                convertView.setTag(holder);
            }
            
            else
            {
                
            	holder = (ViewHolder)convertView.getTag();
                
            }
			
			
            holder.txtName.setText(listDue.get(position).getName());
            holder.txtBalance.setText(listDue.get(position).getBalance());
            holder.txtDueDate.setText(listDue.get(position).getDuedate());
            
            if(listDue.get(position).getIsPaid().equalsIgnoreCase("0"))
            {
            	holder.layoutStatus.setBackgroundColor(Color.BLACK);
            	holder.txtStatus.setText(R.string.java_feeshistoryfragment_not_paid);
            	holder.txtStatus.setTextColor(Color.RED);
            }
            else
            {
            	holder.layoutStatus.setBackgroundColor(Color.WHITE);
            	holder.txtStatus.setText(R.string.java_feeshistoryfragment_paid);
            	holder.txtStatus.setTextColor(Color.BLACK);
            }
            
			
			return convertView;
		}
		
	}
	
	
	class ViewHolder{
		
		TextView txtName;
		TextView txtBalance;
		TextView txtDueDate;
		LinearLayout layoutStatus;
		TextView txtStatus;
        
    }
	



}
