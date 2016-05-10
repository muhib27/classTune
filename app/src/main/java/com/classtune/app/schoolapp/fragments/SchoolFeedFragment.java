package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.AnyFragmentLoadActivity;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.freeversion.SingleEventActivity;
import com.classtune.app.freeversion.SingleExamRoutine;
import com.classtune.app.freeversion.SingleHomeworkActivity;
import com.classtune.app.freeversion.SingleMeetingRequestActivity;
import com.classtune.app.freeversion.SingleNoticeActivity;
import com.classtune.app.schoolapp.model.FreeFeed;
import com.classtune.app.schoolapp.model.FreeVersionPost;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserAccessType;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.PagerContainer;
import com.classtune.app.schoolapp.viewhelpers.PopupDialog;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SchoolFeedFragment extends Fragment implements UserAuthListener {

    UIHelper uiHelper;
    UserHelper userHelper;
    private PullToRefreshListView listGoodread;
    private GoodReadAdapter adapter;
    private ArrayList<FreeFeed> allGooadReadPost = new ArrayList<FreeFeed>();
    private ProgressBar spinner;

    boolean hasNext = false;
    private int pageNumber = 1;
    private int pageSize = 30;
    private boolean isRefreshing = false;
    private boolean loading = false;
    private boolean stopLoadingData = false;
    private boolean isPaid;
    private boolean isTeacher;
    private String schoolName;
    private String username;
    private String userdetails;
    private String profileurl;
    private int attendance_status;
    private FreeVersionPost.LastVisited lastvisited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UIHelper(getActivity());
        adapter = new GoodReadAdapter(getActivity(), allGooadReadPost);
        userHelper = new UserHelper(this, getActivity());
        allGooadReadPost.clear();
        //Log.e("SIZE OF ALLGOODREADPOST:", "" + allGooadReadPost.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_feed, container,
                false);
        //some code goes here
        spinner = (ProgressBar) view.findViewById(R.id.loading);
        isPaid = userHelper.getUser().getPaidInfo().getSchoolType() == 1 ? true : false;
        isTeacher = userHelper.getUser().getType() == UserTypeEnum.TEACHER ? true : false;

        listGoodread = (PullToRefreshListView) view
                .findViewById(R.id.listView_category);
        int footerHeight = getActivity().getResources().getDimensionPixelSize(
                R.dimen.footer_height);
        //some code hello there
        listGoodread.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position - 1 != 0){
                    FreeFeed feed = adapter.getList().get(position - 1);
                    invokeClasses(feed.getRtype(), feed.getRid(), feed.getIs_read());
                    adapter.getList().get(position - 1).setIs_read("1");
                }
            }
        });
        listGoodread.setAdapter(adapter);
        setUpList();
        loadDataInToList();
        return view;
    }

    private void loadDataInToList() {
        if (AppUtility.isInternetConnected()) {
            processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY, "");
        } else {
            uiHelper.showMessage(getActivity().getString(
                    R.string.internet_error_text));
        }
    }

    private void setUpList() {
        initializePageing();
        listGoodread.setMode(Mode.PULL_FROM_END);
        listGoodread.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                Mode m = listGoodread.getCurrentMode();
                if (m == Mode.PULL_FROM_START) {
                    stopLoadingData = false;
                    isRefreshing = true;
                    pageNumber = 1;
                    loading = true;
                    processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY, "");
                } else if (!stopLoadingData) {
                    pageNumber++;
                    loading = true;
                    processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY, "");
                } else {
                    new NoDataTask().execute();
                }
            }
        });

        adapter = new GoodReadAdapter(getActivity(), allGooadReadPost);
        listGoodread.setAdapter(adapter);
    }

    private void processFetchPost(String url, String categoryIndex) {

        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
        params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
        //params.put(RequestKeyHelper.SCHOOL_ID, getArguments().getInt("school_id") + "");
        // getArguments().getInt("school_id") + "");
        //Log.e("SCHOOL_ID_FEED", getArguments().getInt("school_id") + "");
        //params.put(RequestKeyHelper.TARGET, "school");
        if (UserHelper.isLoggedIn()) {
            if (userHelper.getUser().getAccessType() == UserAccessType.PAID) {
                params.put(RequestKeyHelper.USER_SECRET,
                        UserHelper.getUserSecret());
                if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
                    params.put("batch_id", userHelper.getUser()
                            .getSelectedChild().getBatchId());
                    params.put("student_id", userHelper.getUser()
                            .getSelectedChild().getProfileId());
                }
                AppRestClient.post(URLHelper.URL_PAID_VERSION_CLASSTUNE_FEED,
                        params, fitnessHandler);

            } else {
                AppRestClient.post(URLHelper.URL_FREE_VERSION_SCHOOL_FEED,
                        params, fitnessHandler);
            }

        }

        // Log.e("Params", params.toString());
        // if(getArguments().getInt("school_id")==-5)return;

    }

    AsyncHttpResponseHandler fitnessHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

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
        }



        @Override
        public void onSuccess(int arg0, String responseString) {

            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
            Log.d("@@@****@@@",responseString);
            /*
			 * if (fitnessAdapter.getPageNumber() == 1) {
			 * fitnessAdapter.getList().clear(); // setupPoppyView(); }
			 */
            //Log.e("Response CATEGORY", responseString);
            // app.showLog("Response", responseString);
            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {
                hasNext = modelContainer.getData().get("has_next")
                        .getAsBoolean();

                schoolName = modelContainer.getData().get("school_name").getAsString();
                username = modelContainer.getData().get("user_name").getAsString();
                profileurl = modelContainer.getData().get("profile_picture").getAsString();
                attendance_status = modelContainer.getData().get("attandence").getAsInt();
                userdetails = modelContainer.getData().get("user_details").getAsString();
                lastvisited = GsonParser.getInstance().parseLastVisited(modelContainer.getData().getAsJsonObject("last_visited").toString());

                if (pageNumber == 1) {
                    adapter.clearList();
                    adapter.addSeparatorItem(new FreeFeed());
                }

                spinner.setVisibility(View.GONE);
                if (!hasNext) {
                    // fitnessAdapter.setStopLoadingData(true);
                    stopLoadingData = true;
                }
                // fitnessAdapter.getList().addAll();
                ArrayList<FreeFeed> allpost = GsonParser.getInstance()
                        .parsePost(
                                modelContainer.getData().getAsJsonArray("feeds")
                                        .toString());

                // if (pageNumber == 1)
                for (int i = 0; i < allpost.size(); i++) {
                     adapter.addItem(allpost.get(i));
                }
                adapter.notifyDataSetChanged();

                if (pageNumber != 0 || isRefreshing) {
                    listGoodread.onRefreshComplete();
                    loading = false;
                }
            }
        }
    };

    private void initializePageing() {
        pageNumber = 1;
        isRefreshing = false;
        loading = false;
        stopLoadingData = false;
    }

    public static SchoolFeedFragment newInstance(int schoolId) {
        SchoolFeedFragment f = new SchoolFeedFragment();
        Bundle args = new Bundle();
        args.putInt("school_id", schoolId);
        f.setArguments(args);
        return f;
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
            listGoodread.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    public void showCustomDialog(String headerText, int imgResId,
                                 String descriptionText) {
        PopupDialog picker = PopupDialog.newInstance(0);
        picker.setData(headerText, descriptionText, imgResId, getActivity());
        picker.show(getChildFragmentManager(), null);
    }

    public class GoodReadAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<FreeFeed> list;
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SUMMERY = 1;

        private static final int TYPE_MAX_COUNT = 2;
        private TreeSet<Integer> mSeparatorSet = new TreeSet<Integer>();
        private TreeSet<Integer> mSingleSeparatorSet = new TreeSet<Integer>();
        private LayoutInflater mInflater;

        public GoodReadAdapter(Context context, ArrayList<FreeFeed> list) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
            this.list = new ArrayList<FreeFeed>();
        }

        public void clearList() {
            this.list.clear();
            this.mSeparatorSet.clear();
            this.mSingleSeparatorSet.clear();
        }

        public void addItem(final FreeFeed item) {
            list.add(item);
            // The notification is not necessary since the items are not added
            // dynamically
            // notifyDataSetChanged();
        }

        public void addSeparatorItem(final FreeFeed item) {
            list.add(item);
            // Save separator position
            // This is used to check whether the element is a separator or an
            // item
            mSeparatorSet.add(list.size() - 1);
            // The notification is not necessary since the separators are not
            // added dynamically
            // notifyDataSetChanged();
        }

        public ArrayList<FreeFeed> getList(){
            return list;
        }

        public GoodReadAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (mSeparatorSet.contains(position)) {
                return TYPE_SUMMERY;
            }
            return TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final int i = position;

            ViewHolder holder = null;
            int type = getItemViewType(position);
            FreeFeed mpost = list.get(position);

            if (convertView == null) {
                holder = new ViewHolder();

                switch (type) {
                    case TYPE_SUMMERY:
                        convertView = mInflater.inflate(
                                R.layout.feed_header_new, parent,
                                false);
                        holder.schoolName = (TextView) convertView.findViewById(R.id.sum_tv_school_name_new);
                        holder.currentDate = (TextView) convertView.findViewById(R.id.sum_tv_date_new);
                        holder.studentName = (TextView) convertView.findViewById(R.id.sum_tv_name);
                        holder.todayTextView = (TextView) convertView.findViewById(R.id.sum_tv_today_new);
                        holder.profilePicture = (ImageView) convertView.findViewById(R.id.sum_iv_profile_photo_new);
                        holder.parentOfLabel =(TextView) convertView.findViewById(R.id.summary_student_parent_of_label_new);
                        holder.statusImage = (ImageView) convertView.findViewById(R.id.status_image);
                        holder.statusText = (TextView) convertView.findViewById(R.id.status_attendance);
                        holder.tomorrowLay = (LinearLayout) convertView.findViewById(R.id.tomorrow_class_lay);

                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(
                                R.layout.feed_item,
                                parent, false);
                        holder.date = (TextView) convertView.findViewById(R.id.main_feed_date);
                        holder.headerTitle = (TextView) convertView.findViewById(R.id.main_feed_header_title);
                        holder.titleTextVie = (TextView) convertView.findViewById(R.id.main_feed_title);
                        holder.newTextView = (TextView) convertView.findViewById(R.id.newTextView);
                        holder.iconbg = (RelativeLayout) convertView.findViewById(R.id.iconbg);
                        holder.icon = (ImageView) convertView.findViewById(R.id.main_feed_icon);
                        holder.body1 = (TextView) convertView.findViewById(R.id.main_feed_body1);
                        holder.body2 = (TextView) convertView.findViewById(R.id.main_feed_body2);
                        holder.body3 = (TextView) convertView.findViewById(R.id.main_feed_body3);

                        break;

                    default:
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            /*holder.profilePicture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userHelper.getUser().getType() != UserTypeEnum.TEACHER)
                    {
                        Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
                        intent.putExtra("key_from_feed", 1);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), TeacherInfoActivity.class);
                        startActivity(intent);
                    }
                }
            });

            holder.studentNameHeader.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userHelper.getUser().getType() != UserTypeEnum.TEACHER)
                    {
                        Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
                        intent.putExtra("key_from_feed", 1);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), TeacherInfoActivity.class);
                        startActivity(intent);
                    }
                }
            });*/


            if (list.size() > 0) {
               switch (type) {
                    case TYPE_SUMMERY:
                        FreeFeed summary = list.get(position);
                        holder.schoolName.setText(schoolName);


                        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
                            holder.studentName.setText(userHelper.getUser().getSelectedChild().getFullName());
                        }
                        else{
                            holder.studentName.setText(username);
                        }

                        //holder.studentNameHeader.setText(username);
                        holder.currentDate.setText(userdetails);

                        switch (attendance_status){
                            case 1:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_holiday);
                                holder.statusImage.setImageResource(R.drawable.group4);
                                break;
                            case 2:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_weekend);
                                holder.statusImage.setImageResource(R.drawable.group5);
                                break;
                            case 3:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_leave);
                                holder.statusImage.setImageResource(R.drawable.group3);
                                break;
                            case 4:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_late);
                                holder.statusImage.setImageResource(R.drawable.group2);
                                break;
                            case 5:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_absent_today);
                                holder.statusImage.setImageResource(R.drawable.group8);
                                break;
                            case 6:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_present_today);
                                holder.statusImage.setImageResource(R.drawable.group7);
                                break;
                            case 7:
                                holder.statusText.setText(R.string.java_schoolfeedfragment_class_yet_to_start);
                                holder.statusImage.setImageResource(R.drawable.group1);
                                break;
                        }
                        if (!TextUtils.isEmpty(lastvisited.getType())) {
                            holder.todayTextView.setText(lastvisited
                                    .getFirst()
                                    + "\n"
                                    + lastvisited.getNumber()
                                    + "\n"
                                    + lastvisited.getType());
                        } else {
                            holder.todayTextView.setText(lastvisited.getNumber());
                        }
                        holder.tomorrowLay.setTag("8");
                        disableBlock(holder.tomorrowLay, true, 8);
                        if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
                            holder.tomorrowLay.setTag("4");
                            disableBlock(holder.tomorrowLay,true, 4);
                            holder.statusImage.setVisibility(View.GONE);
                            holder.statusText.setVisibility(View.GONE);
                            if (!TextUtils.isEmpty(userHelper.getUser().getNickName())) {
                                switch (Integer.parseInt(userHelper.getUser().getNickName())) {
                                    case 1:
                                        setUserName(holder.studentName,userHelper.getUser().getFirstName());
                                        break;
                                    case 2:
                                        setUserName(holder.studentName, userHelper.getUser().getMiddleName());
                                        break;
                                    case 3:
                                        setUserName(holder.studentName, userHelper.getUser().getLastName());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                holder.studentName.setText(userHelper.getUser().getEmail());
                            }
                            //holder.studentNameHeader.setText(userHelper.getUser().getFullName());
                        }

                        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
                            holder.parentOfLabel.setVisibility(View.VISIBLE);
                        }

                        if (!TextUtils.isEmpty(profileurl)) {
                            if(userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
                                SchoolApp.getInstance().displayUniversalImage(
                                        userHelper.getUser().getSelectedChild().getProfile_image(),
                                        holder.profilePicture);
                            } else {
                                SchoolApp.getInstance().displayUniversalImage(
                                        profileurl,
                                        holder.profilePicture);
                            }
                        }
                        break;
                    case TYPE_ITEM:

                        holder.headerTitle.setText(getcategoryName(list.get(position).getRtype()));

                        if(!TextUtils.isEmpty(list.get(position).getCreated())) {
                            holder.date.setText(list.get(position).getCreated());
                        }else{
                            holder.date.setText("");
                        }

                        if(!TextUtils.isEmpty(list.get(position).getTitle())) {
                            holder.titleTextVie.setText(Html.fromHtml(list.get(position).getTitle()));
                        }else{
                            holder.titleTextVie.setText("");
                        }

                        setImgViewIcon(holder.icon, list.get(position).getRtype());
                        if(position%2==0){
                            holder.iconbg.setBackgroundColor(getResources().getColor(R.color.gray_4));
                            holder.icon.setColorFilter(null);
                        } else {
                            holder.iconbg.setBackgroundColor(getResources().getColor(R.color.classtune_green_color));
                            holder.icon.setColorFilter(Color.argb(255, 255, 255, 255));
                        }

                        if(list.get(position).getIs_read().equals("0")){
                            holder.newTextView.setVisibility(View.VISIBLE);
                        } else {
                            holder.newTextView.setVisibility(View.GONE);
                        }

                        if(!TextUtils.isEmpty(list.get(position).getBody1())) {
                            holder.body1.setText(Html.fromHtml( "\u2022 " + list.get(position).getBody1()));
                        }else{
                            holder.body1.setText("");
                        }

                        if(!TextUtils.isEmpty(list.get(position).getBody2())) {
                            holder.body2.setText(Html.fromHtml( "\u2022 " + list.get(position).getBody2()));
                        }else{
                            holder.body2.setText("");
                        }

                        if(!TextUtils.isEmpty(list.get(position).getBody3())) {
                            holder.body3.setText(Html.fromHtml( "\u2022 " + list.get(position).getBody3()));
                        }else{
                            holder.body3.setText("");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                Log.e("FREE_HOME_API", "array is empty!");
            }

            return convertView;
        }

        private String capitalize(final String line) {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }




        private int getVisibility(boolean isVisible) {
            if (isVisible)
                return View.VISIBLE;
            else
                return View.INVISIBLE;
        }

        private void disableBlock(View view, boolean state, int pos) {

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(v.getTag() == null) return;
                    int pos = Integer.parseInt(v.getTag().toString());
                    ((HomePageFreeVersion) getActivity())
                            .loadPaidFragment(PaidVersionHomeFragment
                                    .newInstance(pos-1));
                }
            });

            if (state) {
                view.setVisibility(View.VISIBLE);
                view.setTag("" + pos);
            } else {
                view.setVisibility(View.GONE);
                view.setBackgroundColor(getResources().getColor(R.color.bg_disable));
                //view.findViewById(R.id.sum_iv_disable).setBackgroundColor(getActivity().getResources().getColor(R.color.red_disable));
            }
            /*switch (view.getId()) {
                case R.id.sum_lay_event:
                case R.id.sum_lay_has_class_tomorrow:
                case R.id.sum_lay_homework:
                case R.id.sum_lay_report_card:
                case R.id.sum_lay_quiz:
                case R.id.sum_lay_leave:
                case R.id.sum_lay_tution:
                case R.id.sum_lay_attendance:
                case R.id.sum_lay_meeting:
                    if (state) {

                        view.setVisibility(View.VISIBLE);
                        view.setTag("" + pos);

                    } else {
                        view.setVisibility(View.GONE);
                        view.setBackgroundColor(getResources().getColor(R.color.bg_disable));
                    }
                     break;
                case R.id.sum_lay_exam:
                case R.id.sum_lay_notice:
                case R.id.sum_lay_exam_routine_publish:
                    if (state) {
                        view.setVisibility(View.VISIBLE);
                        view.setTag("" + pos);
                    } else {
                        view.setVisibility(View.GONE);
                        view.setBackgroundColor(getResources().getColor(R.color.bg_disable));
                        //view.findViewById(R.id.sum_iv_disable).setBackgroundColor(getActivity().getResources().getColor(R.color.red_disable));
                    }
                break;

                default:
                    break;
            }*/

        }
    }

    AsyncHttpResponseHandler wowHandler = new AsyncHttpResponseHandler() {
        public void onStart() {
            // uiHelper.showLoadingDialog("Please wait...");
        }

        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
            Log.e("WOW HOYNAI", "HEHEHEHE" + arg1);
        }


        public void onSuccess(int arg0, String responseString) {
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }

            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                    responseString);

            if (wrapper.getStatus().getCode() == 200) {
                // uiHelper.showMessage("Successfully added wow");
                Log.e("WOW HOISE", "HEHEHEHE");
            } else {
                Log.e("WOW HONAI ONNO CODE", " " + responseString);
            }
        }

    };

    AsyncHttpResponseHandler readLaterHandler = new AsyncHttpResponseHandler() {
        public void onStart() {
            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        }


        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        public void onSuccess(int arg0, String responseString) {
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }

            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                    responseString);

            if (wrapper.getStatus().getCode() == 200) {
                uiHelper.showMessage("Post has been added to GoodRead");
            }
        }

    };

    class ViewHolder {

        PagerContainer container;

        // SUMMERY LAYOUT
        TextView schoolName, currentDate, studentName;
        TextView todayTextView;
        ImageView icon;
        RelativeLayout iconbg;

        TextView date;
        ImageView profilePicture;
        TextView studentNameHeader, parentOfLabel;
        ImageView statusImage;
        LinearLayout tomorrowLay;
        TextView statusText;
        TextView headerTitle, body1, body2, body3, dateTextView, titleTextVie, newTextView;
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<String> images;
        private LayoutInflater inflater;

        ImagePagerAdapter(List<String> images) {
            this.images = images;
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image,
                    view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout
                    .findViewById(R.id.loading);
            SchoolApp.getInstance().displayUniversalImage(images.get(position),
                    imageView);
            // imageLoader.displayImage(images.get(position), imageView,
            // options,
            // new SimpleImageLoadingListener() {
            // @Override
            // public void onLoadingStarted(String imageUri, View view) {
            // spinner.setVisibility(View.VISIBLE);
            // }
            //
            // @Override
            // public void onLoadingFailed(String imageUri, View view,
            // FailReason failReason) {
            // String message = null;
            // switch (failReason.getType()) {
            // case IO_ERROR:
            // message = "Input/Output error";
            // break;
            // case DECODING_ERROR:
            // message = "Image can't be decoded";
            // break;
            // case NETWORK_DENIED:
            // message = "Downloads are denied";
            // break;
            // case OUT_OF_MEMORY:
            // message = "Out Of Memory error";
            // break;
            // case UNKNOWN:
            // message = "Unknown error";
            // break;
            // }
            // Toast.makeText(getActivity(), message,
            // Toast.LENGTH_SHORT).show();
            //
            // spinner.setVisibility(View.GONE);
            // }
            //
            // @Override
            // public void onLoadingComplete(String imageUri,
            // View view, Bitmap loadedImage) {
            // spinner.setVisibility(View.GONE);
            // }
            // });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public void onAuthenticationStart() {

    }

    @Override
    public void onAuthenticationSuccessful() {

    }

    @Override
    public void onAuthenticationFailed(String msg) {

    }

    @Override
    public void onPaswordChanged() {

    }

    public void setUserName(TextView userNameTextView ,String name) {
        if (!TextUtils.isEmpty(name))
            userNameTextView.setText(name);
        else
            userNameTextView.setText(userHelper.getUser().getEmail());
    }

    private void setImgViewIcon(ImageView imgView, String rType)
    {
        int type = Integer.parseInt(rType);

        switch (type) {
            case 1:
                imgView.setImageResource(R.drawable.notice_event);
                break;

            case 2:
                imgView.setImageResource(R.drawable.notice_exam_schadule);
                break;

            case 3:
                imgView.setImageResource(R.drawable.notice_exam_report);
                break;

            case 4:
                imgView.setImageResource(R.drawable.notice_homework);
                break;

            case 5:
                imgView.setImageResource(R.drawable.notice_notice);
                break;

            case 6:
                imgView.setImageResource(R.drawable.notice_attendance);
                break;

            case 7:
                imgView.setImageResource(R.drawable.notice_leave);
                break;

            case 8:
                imgView.setImageResource(R.drawable.notice_leave);
                break;

            case 9:
                imgView.setImageResource(R.drawable.notice_leave);
                break;

            case 10:
                imgView.setImageResource(R.drawable.notice_leave);
                break;

            case 11:
                imgView.setImageResource(R.drawable.notice_meeting_request);
                break;

            case 12:
                imgView.setImageResource(R.drawable.notice_meeting_request);
                break;

            case 13:
                imgView.setImageResource(R.drawable.notice_meeting_request);
                break;

            case 14:
                imgView.setImageResource(R.drawable.notice_meeting_request);
                break;

            default:
                imgView.setImageResource(R.drawable.notice_default);

                break;
        }


    }
    private String getcategoryName(String rType)
    {
        int type = Integer.parseInt(rType);

        switch (type) {
            case 1:
                return getActivity().getString(R.string.java_schoolfeedfragment_event);

            case 2:
                return getActivity().getString(R.string.java_schoolfeedfragment_exam_routine);

            case 3:
                return getActivity().getString(R.string.java_schoolfeedfragment_exam_report);

            case 4:
                return getActivity().getString(R.string.java_schoolfeedfragment_homework);

            case 5:
                return getActivity().getString(R.string.java_schoolfeedfragment_notice);

            case 6:
                return getActivity().getString(R.string.java_schoolfeedfragment_attendance);

            case 7:
                return getActivity().getString(R.string.java_schoolfeedfragment_leave);

            case 8:
                return getActivity().getString(R.string.java_schoolfeedfragment_leave);

            case 9:
                return getActivity().getString(R.string.java_schoolfeedfragment_leave);

            case 10:
                return getActivity().getString(R.string.java_schoolfeedfragment_leave);

            case 11:
                return getActivity().getString(R.string.java_schoolfeedfragment_meeting_request);

            case 12:
                return getActivity().getString(R.string.java_schoolfeedfragment_meeting_request);

            case 13:
                return getActivity().getString(R.string.java_schoolfeedfragment_meeting_request);

            case 14:
                return getActivity().getString(R.string.java_schoolfeedfragment_meeting_request);

            default:
                return getActivity().getString(R.string.java_schoolfeedfragment_notice);
        }


    }
    private void invokeClasses(String rType, String rid, String is_read)
    {
        int type = Integer.parseInt(rType);
        Intent intent = null;
        switch (type) {
            case 1:

                /*intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentEventFragment");*/
                intent = new Intent(getActivity(), SingleEventActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_EVENT, rid);

                break;

            case 2:

                intent = new Intent(getActivity(), SingleExamRoutine.class);
                intent.putExtra(AppConstant.ID_SINGLE_CALENDAR_EVENT, rid);

                break;

            case 3:

                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentReportCardFragment");

                break;


            case 4:
                intent = new Intent(getActivity(), SingleHomeworkActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, rid);

                break;

            case 5:
                intent = new Intent(getActivity(), SingleNoticeActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_NOTICE, rid);

                break;

            case 6:
                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentAttendenceFragment");

                break;

            case 7:


                break;

            case 8:
                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "MyLeaveFragment");

                break;

            case 9:

                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "StudentLeaveFragment");

                break;

            case 10:
                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "MyLeaveFragment");

                break;

            case 11:
                intent = new Intent(getActivity(), SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rid);

                break;

            case 12:
                intent = new Intent(getActivity(), SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rid);

                break;

            case 13:
                intent = new Intent(getActivity(), SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rid);

                break;

            case 14:
                intent = new Intent(getActivity(), SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rid);

                break;

            case 15:
                intent = new Intent(getActivity(), AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "QuizFragment");

                break;

            default:
                break;
        }

        if(intent != null) {
            startActivity(intent);
        }

        if(is_read.equalsIgnoreCase("0")){
            initApiCall(rid, rType);
        }

    }
    private void initApiCall(String rId, String rTtype)
    {

        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


        if(rId != null)
        {
            params.put("rid", rId);
        }

        if(rTtype != null)
        {
            params.put("rtype", rTtype);
        }


        AppRestClient.post(URLHelper.URL_EVENT_REMINDER, params, reminderHandler);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    AsyncHttpResponseHandler reminderHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
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

                //fetchNotification();

               /* modelContainer.getData().get("unread_total").getAsString();

                SharedPreferencesHelper.getInstance().setString("total_unread", modelContainer.getData().get("unread_total").getAsString());

                userHelper.saveTotalUnreadNotification( modelContainer.getData().get("unread_total").getAsString());

                listenerActivity.onNotificationCountChangedFromActivity(Integer.parseInt(modelContainer.getData().get("unread_total").getAsString()));*/

            }

            else {

            }
        };
    };

}
