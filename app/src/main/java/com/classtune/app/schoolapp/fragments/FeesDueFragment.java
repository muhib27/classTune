package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.FeesDue;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FeesDueFragment extends Fragment {
	
	private View view;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	private ListView listViewFeesDue;
	private FeesDueAdapter adapter;
	
	
	private List<FeesDue> listDue;
	
	private TextView txtMessage;
	
	
	private TextView txtDueDateHeader;
	private TextView txtDescriptionHeader;
	private TextView txtAmountHeader;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(getActivity());
		
		listDue = new ArrayList<FeesDue>();
		
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
		view = inflater.inflate(R.layout.fragment_feesduefragment,
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
		
		
		listViewFeesDue = (ListView)view.findViewById(R.id.listViewFeesDue);
		adapter = new FeesDueAdapter();
		listViewFeesDue.setAdapter(adapter);
		
		txtMessage = (TextView)view.findViewById(R.id.txtMessage);
	}
	
	
	private void initApiCall()
	{

		RequestParams params = new RequestParams();

	
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		
		AppRestClient.post(URLHelper.URL_FEES, params, feesDueHandler);
	
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
				
				
				JsonArray  arrayDue = modelContainer.getData().get("due").getAsJsonArray();
				listDue = parseFeesDueList(arrayDue.toString());
				
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
	
	
	
	private List<FeesDue> parseFeesDueList(String object) {

		List<FeesDue> tags = new ArrayList<FeesDue>();
		Type listType = new TypeToken<List<FeesDue>>() {
		}.getType();
		tags = (List<FeesDue>) new Gson().fromJson(object, listType);
		return tags;
	}
	
	
	
	private class FeesDueAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_feesdue,null,false);
                
                holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
                holder.txtBalance = (TextView)convertView.findViewById(R.id.txtBalance);
                holder.txtDueDate = (TextView)convertView.findViewById(R.id.txtDueDate);
               
                convertView.setTag(holder);
            }
            
            else
            {
                
            	holder = (ViewHolder)convertView.getTag();
                
            }
			
			
            holder.txtName.setText(listDue.get(position).getName());
            holder.txtBalance.setText(listDue.get(position).getBalance());
            holder.txtDueDate.setText(listDue.get(position).getDuedate());
            
            
			
			return convertView;
		}
		
	}
	
	
	class ViewHolder{
		
		TextView txtName;
		TextView txtBalance;
		TextView txtDueDate;
        
    }
	

}
