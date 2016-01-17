package com.classtune.app.schoolapp.viewhelpers;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Comment;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class CommentDialog extends DialogFragment implements UserAuthListener{

	UIHelper uiHelper;
	PullToRefreshListView commentListView;
	CommentAdapter adapter;
	
	boolean hasNext = false;
	private int pageNumber = 1;
	private int pageSize = 10;
	private boolean isRefreshing = false;
	private boolean loading = false;
	private boolean stopLoadingData = false;
	private ProgressBar spinner;
	private ArrayList<Comment> allComments = new ArrayList<Comment>();
	private Button commentPostBtn;
	private EditText commentEditText;
	private RelativeLayout commentLayout;
	private UserHelper userHelper;
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(this, getActivity());
		adapter = new CommentAdapter(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		 View view = inflater.inflate(R.layout.dialog_comment, container,
					false);
		 commentListView = (PullToRefreshListView)view.findViewById(R.id.listview_comment);
		 ((TextView)view.findViewById(R.id.wow_header)).setText(getArguments().getString("wow_string"));
		 spinner = (ProgressBar) view.findViewById(R.id.loading);
		 initViews(view);
		 setUpList();
		 loadDataInToList();
		return view;
	}
	private void initViews(View view) {
		commentLayout = (RelativeLayout)view.findViewById(R.id.lay_comment_post);
		commentPostBtn = (Button)view.findViewById(R.id.btn_comment_post);
		commentEditText = (EditText)view.findViewById(R.id.et_comment);
		commentPostBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(commentEditText.getText())){
					if(UserHelper.isLoggedIn() && getArguments().getBoolean("can_comment")){
						postComment();
					}
				}else {
					uiHelper.showErrorDialog("Comment Cannot be Empty!!");
				}
			}
		});
		
		if(UserHelper.isLoggedIn() && getArguments().getBoolean("can_comment")){
			commentLayout.setVisibility(View.VISIBLE);
		}else  commentLayout.setVisibility(View.GONE);
	}
	
	
	private void postComment(){

		RequestParams params = new RequestParams();
		
		if (UserHelper.isLoggedIn()) {
			params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		}

		
		params.put(RequestKeyHelper.POST_ID, getArguments().getInt("post_id")+"");
		params.put(RequestKeyHelper.POST_DETAILS, commentEditText.getText().toString());
		Comment comment = new Comment();
		comment.setUsername(userHelper.getUser().getUsername());
		comment.setDetails(commentEditText.getText().toString());
		allComments.add(comment);
		adapter.notifyDataSetChanged();
		commentEditText.setText("");

		// Log.e("Params", params.toString());

			
			AppRestClient.post(URLHelper.URL_FREE_VERSION_COMMENT_POST, params,
					commentPostHandler);
	}
	private void loadDataInToList() {
		if (AppUtility.isInternetConnected()) {
			processFetchPost(URLHelper.URL_FREE_VERSION_COMMENT,
					"");
		} else
			uiHelper.showMessage(getActivity().getString(
					R.string.internet_error_text));
	}
	public static CommentDialog newInstance(int postId,boolean cancomment, String wowString) {
		CommentDialog frag = new CommentDialog();
		Bundle args = new Bundle();
		args.putInt("post_id", postId);
		args.putBoolean("can_comment", cancomment);
		args.putString("wow_string", wowString);
		frag.setArguments(args);
		return frag;
	}
	private void setUpList() {

		commentListView.setMode(Mode.PULL_FROM_END);
		// Set a listener to be invoked when the list should be refreshed.
		commentListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				Mode m = commentListView.getCurrentMode();
				if (m == Mode.PULL_FROM_START) {
					stopLoadingData = false;
					isRefreshing = true;
					pageNumber = 1;
					loading = true;
					processFetchPost(URLHelper.URL_FREE_VERSION_COMMENT,
							"");
				} else if (!stopLoadingData) {
					pageNumber++;
					loading = true;
					processFetchPost(URLHelper.URL_FREE_VERSION_COMMENT,
							"");
				} else {
					new NoDataTask().execute();
				}
			}
		});

		adapter = new CommentAdapter(getActivity());
		
		commentListView.setAdapter(adapter);
	}
	
	private void processFetchPost(String url, String categoryIndex) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		
		/*if (UserHelper.isLoggedIn()) {
			params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		}*/

		params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
		params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
		params.put(RequestKeyHelper.POST_ID, getArguments().getInt("post_id")+"");
		

		// Log.e("Params", params.toString());

			
			AppRestClient.post(URLHelper.URL_FREE_VERSION_COMMENT, params,
					commentHandler);
		
	}
	
	AsyncHttpResponseHandler commentPostHandler = new AsyncHttpResponseHandler(){
		public void onFailure(Throwable arg0, String arg1) {
			Log.e("Comment not posted RESPONSE", arg1);
		};
		
		public void onSuccess(int arg0, String responseString) {
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			if (modelContainer.getStatus().getCode() != 200) {
				Log.e("STATUS CODE", modelContainer.getStatus().getCode() + "");
			}
		};
	};
	AsyncHttpResponseHandler commentHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			if (pageNumber == 0 && !isRefreshing) {
				if (!uiHelper.isDialogActive())
					uiHelper.showLoadingDialog(getString(R.string.loading_text));
				else
					uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			}
			if (pageNumber == 1) {
				spinner.setVisibility(View.VISIBLE);
			}
		};

		@Override
		public void onSuccess(int arg0, String responseString) {

			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			/*
			 * if (fitnessAdapter.getPageNumber() == 1) {
			 * fitnessAdapter.getList().clear(); // setupPoppyView(); }
			 */
			Log.e("Response CATEGORY", responseString);
			// app.showLog("Response", responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {

				hasNext = modelContainer.getData().get("has_next")
						.getAsBoolean();

				if (pageNumber == 1)
					allComments.clear();
				spinner.setVisibility(View.GONE);
				if (!hasNext) {
					// fitnessAdapter.setStopLoadingData(true);
					stopLoadingData = true;
				}

				// fitnessAdapter.getList().addAll();
				ArrayList<Comment> allCom = GsonParser.getInstance()
						.parseFreeVersionComment(
								modelContainer.getData().getAsJsonArray("comments")
										.toString());
				if (pageNumber == 1)
				for (int i = 0; i < allCom.size(); i++) {
					allComments.add(allCom.get(i));
				}
				adapter.notifyDataSetChanged();

				if (pageNumber != 0 || isRefreshing) {
					commentListView.onRefreshComplete();
					loading = false;
				}
			}

		}

		
	};
	
	public class CommentAdapter extends BaseAdapter {

		private Context context;
		
		public CommentAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return allComments.size();
		}

		@Override
		public Object getItem(int position) {
			return allComments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final int i = position;

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(this.context).inflate(
						R.layout.comment_item, parent,
						false);
				
				holder.author = (TextView) convertView.findViewById(R.id.tv_comment_author);
				holder.content = (TextView) convertView.findViewById(R.id.tv_comment_content);
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (allComments.size() > 0) {
				holder.content.setText(allComments.get(position).getDetails());
				holder.author.setText(allComments.get(position).getUsername());
			} else {
				Log.e("FREE_HOME_API", "array is empty!");
			}

			return convertView;
		}
	}

	class ViewHolder {
		
		TextView author, content;
		
	}
	
	private class NoDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			commentListView.onRefreshComplete();

			super.onPostExecute(result);
		}
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

}
