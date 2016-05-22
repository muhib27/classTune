 package com.classtune.app.schoolapp.fragments;

 import android.annotation.SuppressLint;
 import android.content.Context;
 import android.content.Intent;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.support.v4.app.Fragment;
 import android.text.format.DateUtils;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.AdapterView;
 import android.widget.ListView;
 import android.widget.ProgressBar;
 import android.widget.TextView;

 import com.classtune.app.R;
 import com.classtune.app.freeversion.SingleEventActivity;
 import com.classtune.app.schoolapp.GcmIntentService;
 import com.classtune.app.schoolapp.adapters.UpcomingEventListAdapter;
 import com.classtune.app.schoolapp.model.SchoolEvent;
 import com.classtune.app.schoolapp.model.SchoolEventWrapper;
 import com.classtune.app.schoolapp.model.UserAuthListener;
 import com.classtune.app.schoolapp.model.Wrapper;
 import com.classtune.app.schoolapp.networking.AppRestClient;
 import com.classtune.app.schoolapp.utils.AppConstant;
 import com.classtune.app.schoolapp.utils.AppUtility;
 import com.classtune.app.schoolapp.utils.GsonParser;
 import com.classtune.app.schoolapp.utils.RequestKeyHelper;
 import com.classtune.app.schoolapp.utils.URLHelper;
 import com.classtune.app.schoolapp.utils.UserHelper;
 import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
 import com.classtune.app.schoolapp.viewhelpers.UIHelper;
 import com.handmark.pulltorefresh.library.PullToRefreshBase;
 import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
 import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
 import com.handmark.pulltorefresh.library.PullToRefreshListView;
 import com.loopj.android.http.AsyncHttpResponseHandler;
 import com.loopj.android.http.RequestParams;

 import java.util.ArrayList;
 import java.util.List;

 @SuppressLint("ValidFragment")
 public class UpcomingEventsFragment extends Fragment implements UserAuthListener{


     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         // TODO Auto-generated method stub
         super.onActivityCreated(savedInstanceState);
     }

     private Context context;
     private PullToRefreshListView eventListView;
     private UpcomingEventListAdapter adapter;
     private View view;
     //private TicketListAdapter adapter;
     private List<SchoolEvent> items;
     private UIHelper uiHelper;
     private UserHelper userHelper;
     private int pageNumber=1;
     private int pageSize=10;
     private boolean isRefreshing=false;
     private boolean loading = false;
     private boolean stopLoadingData=false;


     private ProgressBar progressBar;


     private TextView txtMessage;


     @Override
     public void onCreate(Bundle savedInstanceState) {
         // TODO Auto-generated method stub
         super.onCreate(savedInstanceState);
         init();
     }

     private void initializePageing()
     {
         pageNumber=1;
         isRefreshing=false;
         loading= false;
         stopLoadingData=false;
     }

     private void init() {
         context=getActivity();
         items=new ArrayList<SchoolEvent>();
         uiHelper=new UIHelper(getActivity());
         userHelper=new UserHelper(this, context);
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         // TODO Auto-generated method stub
         super.onCreateView(inflater, container, savedInstanceState);
         view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
         eventListView = (PullToRefreshListView) view.findViewById(R.id.event_listview);
         setUpList();
         loadDataInToList();

         progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

         txtMessage = (TextView)view.findViewById(R.id.txtMessage);


         return view;
     }



     private void setUpList() {

         initializePageing();
         eventListView.setMode(Mode.BOTH);
         // Set a listener to be invoked when the list should be refreshed.
         eventListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
             @Override
             public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                 String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),
                         DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                 // Update the LastUpdatedLabel
                 refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);


                 Mode m=eventListView.getCurrentMode();
                 if(m==Mode.PULL_FROM_START)
                 {
                     stopLoadingData=false;
                     isRefreshing=true;
                     pageNumber=1;
                     loading = true;
                     loadDataInToList();
                 }
                 else if(!stopLoadingData)
                 {
                     pageNumber++;
                     loading = true;
                     loadDataInToList();
                 }
                 else
                 {
                     new NoDataTask().execute();
                 }
             }


         });
         items.clear();
         adapter=new UpcomingEventListAdapter(context, items,uiHelper);
         eventListView.setAdapter(adapter);

         eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 SchoolEvent data = (SchoolEvent)adapter.getItem(position-1);
                 Intent intent = new Intent(getActivity(), SingleEventActivity.class);
                 intent.putExtra(AppConstant.ID_SINGLE_EVENT, data.getEventId());
                 context.startActivity(intent);
             }
         });

     }


     private void loadDataInToList() {
         if(AppUtility.isInternetConnected())
         {
             fetchDataFromServer();
         }
         else
             uiHelper.showMessage(context.getString(R.string.internet_error_text));
     }

     private void fetchDataFromServer() {

             RequestParams params=new RequestParams();
             params.put("user_secret",UserHelper.getUserSecret());

             if (userHelper.getUser().getType() == UserTypeEnum.STUDENT) {
                 params.put("school",userHelper.getUser().getPaidInfo().getSchoolId());
             }


             if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
                 params.put("school",userHelper.getUser().getPaidInfo().getSchoolId());
             }



             if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {


                 if(getActivity().getIntent().getExtras()!=null)
                 {
                     if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                     {
                         String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                         String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                         params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                         params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


                         GcmIntentService.initApiCall(rid, rtype);
                     }
                     else
                     {
                         params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                         params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                     }
                 }
                 else
                 {
                     params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                     params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                 }

                 //params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                 //params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                 //params.put("school",userHelper.getUser().getSelectedChild().getSchoolId());
             }

             params.put("page_number", pageNumber+"");
             params.put("page_size", pageSize+"");
             //params.put("category", pageSize+"");

             AppRestClient.post(URLHelper.URL_GET_EVENT_LIST, params, getEventsHandler);

     }

     AsyncHttpResponseHandler getEventsHandler=new AsyncHttpResponseHandler()
     {
         @Override
         public void onFailure(Throwable arg0, String arg1) {
             super.onFailure(arg0, arg1);

         }

         @Override
         public void onStart() {
             super.onStart();
             if(pageNumber==0 && !isRefreshing){
             if(!uiHelper.isDialogActive())
                 uiHelper.showLoadingDialog(getString(R.string.loading_text));
             else
                 uiHelper.updateLoadingDialog(getString(R.string.loading_text));
             }
         }

         @Override
         public void onSuccess(int arg0, String responseString) {
             super.onSuccess(arg0, responseString);
             Log.e("EVENT RESPONSE", responseString);
             uiHelper.dismissLoadingDialog();
             Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
             if(wrapper.getStatus().getCode()==200)
             {
                 SchoolEventWrapper schoolEventData=GsonParser.getInstance().parseEventWrapper(wrapper.getData().toString());
                 if(!schoolEventData.isHasnext())
                     stopLoadingData=true;
                 if(pageNumber==1)
                     items.clear();
                 items.addAll(schoolEventData.getEvents());
                 adapter.notifyDataSetChanged();
                 if(pageNumber!=0 || isRefreshing)
                 {
                     eventListView.onRefreshComplete();
                     loading=false;
                 }
             }
             else
             {

             }


             if(items.size() > 0)
             {
                 progressBar.setVisibility(View.GONE);

                 txtMessage.setVisibility(View.GONE);
             }
             else
             {
                 progressBar.setVisibility(View.GONE);

                 txtMessage.setVisibility(View.VISIBLE);
             }

             Log.e("Events", responseString);
         }
     };

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
             eventListView.onRefreshComplete();

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
