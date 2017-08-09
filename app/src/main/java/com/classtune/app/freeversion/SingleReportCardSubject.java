package com.classtune.app.freeversion;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.ClassTestItem;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleReportCardSubject extends ChildContainerActivity {


    private UIHelper uiHelper;

    private TextView txtSubjectName;
    private ImageView imgSubjectIcon;

    private String subjectName = "";
    private String subjectIcon = "";
    private String examCategoryName = "";

    private Gson gson;

    private int position = 0;
    private String subjectId = "";
    private String examId = "";
    private String examGroupId = "";
    private String selectedStudentId = "";


    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtYourMark;
    private TextView txtHighestMark;
    private TextView txtTotalMark;
    private TextView txtGrade;
    private TextView txtAverage;
    private TextView txtPercentile;
    private TextView txtRemarks;
    private LinearLayout layoutRemarksHolder;
    private TextView txtExamCategory;


    private LinearLayout layoutChart;

    private BarChart chart;

    private ClassTestItem data;
    private boolean isFromClassTestFragment = false;
    private LinearLayout layoutNoCommentHolder;
    private LinearLayout layoutDataHolder;
    private TextView txtTitle21;
    private LinearLayout layoutAllHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_report_card_subject);

        uiHelper = new UIHelper(this);
        userHelper = new UserHelper(this);
        gson = new Gson();

        if(getIntent().getExtras()!=null)
        {

            subjectId = getIntent().getExtras().getString("inner_reportcard_item_subject_id");
            position = getIntent().getExtras().getInt("inner_reportcard_item_position");
            examId = getIntent().getExtras().getString("inner_reportcard_item_exam_id", "");
            examGroupId = getIntent().getExtras().getString("inner_reportcard_item_exam_group_id", "");
            selectedStudentId = getIntent().getExtras().getString("inner_reportcard_item_student_id");
            examCategoryName = getIntent().getExtras().getString("inner_reportcard_item_exam_category_name");
            isFromClassTestFragment = getIntent().getExtras().getBoolean("inner_reportcard_item_exam_from_class_test");

        }

       // Log.e("DDDDD", "is: " + data.getCtReportSubjectExam().getClasstestList().get(0).getExamId());
        Log.e("DDDDD", "position is: " + position);

        initView();
        initApiCall();
        //initAction();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }

    private void initView()
    {
        txtSubjectName = (TextView)this.findViewById(R.id.txtSubjectName);
        imgSubjectIcon = (ImageView)this.findViewById(R.id.imgSubjectIcon);
        //listViewReportCard = (ListView)this.findViewById(R.id.listViewReportCard);

        txtTitle = (TextView)this.findViewById(R.id.txtTitle1);
        txtDate = (TextView)this.findViewById(R.id.txtDate);
        txtYourMark = (TextView)this.findViewById(R.id.txtYourMark);
        txtHighestMark = (TextView)this.findViewById(R.id.txtHighestMark);
        txtTotalMark = (TextView)this.findViewById(R.id.txtTotalMark);
        txtPercentile = (TextView)this.findViewById(R.id.txtPercentile);
        txtGrade = (TextView)this.findViewById(R.id.txtGrade);
        txtAverage = (TextView)this.findViewById(R.id.txtAverage);
        txtRemarks = (TextView)this.findViewById(R.id.txtRemarks);
        layoutRemarksHolder = (LinearLayout)this.findViewById(R.id.layoutRemarksHolder);
        txtExamCategory = (TextView)this.findViewById(R.id.txtExamCategory);
        txtExamCategory.setSelected(true);

        layoutChart = (LinearLayout)this.findViewById(R.id.layoutChart);
        chart = (BarChart)this.findViewById(R.id.chart);

        layoutNoCommentHolder = (LinearLayout)this.findViewById(R.id.layoutNoCommentHolder);
        layoutDataHolder = (LinearLayout)this.findViewById(R.id.layoutDataHolder);
        txtTitle21 = (TextView)this.findViewById(R.id.txtTitle21);

        layoutAllHolder = (LinearLayout)this.findViewById(R.id.layoutAllHolder);

        layoutAllHolder.setVisibility(View.GONE);
    }

    private void initAction()
    {

        layoutAllHolder.setVisibility(View.VISIBLE);

        imgSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubjectIcon(), SingleReportCardSubject.this));
        txtSubjectName.setText(data.getSubjectName());
        txtExamCategory.setText(examCategoryName);


        //Log.e("DDDDD", "exam name is: " + data.getExamName());

        txtTitle.setText(data.getExamName());
        txtDate.setText(data.getExamDate());
        txtYourMark.setText(String.valueOf(getDecimalFormatNumber(data.getMark())));
        txtHighestMark.setText(String.valueOf(getDecimalFormatNumber(data.getMaxMark())));
        txtTotalMark.setText(String.valueOf(getDecimalFormatNumber(data.getTotalMark())));
        txtGrade.setText(data.getGrade());
        txtAverage.setText(String.valueOf(getDecimalFormatNumber(data.getAverageMark())));


        /*int percentile = Math.round(data.getPercentile());Log.e("percentile", ""+percentile);
        int percent = Math.round(percentile/5);Log.e("percent", ""+percent);
        int finalPercentile = percent * 5;Log.e("finalPercentile", ""+finalPercentile);

        txtPercentile.setText("Top "+String.valueOf(finalPercentile)+" %");*/


        float percentile = (float)Math.round(data.getPercentile());
        float percent = (float)Math.round(percentile / 5);
        int finalPercentile = (int)percent * 5;

        txtPercentile.setText(getString(R.string.java_singlereportcardsubject_top)+String.valueOf(finalPercentile)+" %");





        if(!TextUtils.isEmpty(data.getRemarks()))
        {
            if(data.getRemarks().equalsIgnoreCase("-"))
            {
                layoutRemarksHolder.setVisibility(View.GONE);
            }
            else
            {
                layoutRemarksHolder.setVisibility(View.VISIBLE);
                txtRemarks.setText(data.getRemarks());
            }

        }
        else
        {
            layoutRemarksHolder.setVisibility(View.GONE);
        }


        if(data.getNoExams().equalsIgnoreCase("1"))
        {
            layoutRemarksHolder.setVisibility(View.VISIBLE);
            layoutNoCommentHolder.setVisibility(View.VISIBLE);
            txtTitle21.setText(data.getExamName());

            layoutChart.setVisibility(View.GONE);
            layoutDataHolder.setVisibility(View.GONE);
        }
        else
        {
            layoutRemarksHolder.setVisibility(View.GONE);
            layoutNoCommentHolder.setVisibility(View.GONE);

            layoutChart.setVisibility(View.VISIBLE);
            layoutDataHolder.setVisibility(View.VISIBLE);
        }

        showChart();

    }

    private void showChart()
    {
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.animateXY(2000, 2000);
        chart.setDescription("");



        chart.invalidate();
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
        Log.e("DDD_DATA", ""+data.getMark());
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(Float.parseFloat(getDecimalFormatNumber(data.getMark())), 0); // your mark
        valueSet1.add(v1e1);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(Float.parseFloat(getDecimalFormatNumber(data.getAverageMark())), 1); //average mark
        valueSet2.add(v2e1);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v3e1 = new BarEntry(Float.parseFloat(getDecimalFormatNumber(data.getMaxMark())), 2); // highest mark
        valueSet3.add(v3e1);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.row_classtest_report_txt_your_mark));
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, getString(R.string.java_singlereportcardsubject_Average_mark));
        barDataSet2.setColor(Color.rgb(155, 0, 0));

        BarDataSet barDataSet3 = new BarDataSet(valueSet3, getString(R.string.row_classtest_report_txt_highest_mark));
        barDataSet3.setColor(Color.rgb(0, 0, 155));

        barDataSet1.setBarSpacePercent(-50f);
        barDataSet2.setBarSpacePercent(-50f);
        barDataSet3.setBarSpacePercent(-50f);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);


        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        return xAxis;
    }


    private void initApiCall()
    {

        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put(RequestKeyHelper.SUBJECT_ID, subjectId);
        params.put("no_exams", "1");

        if(isFromClassTestFragment == true) {
            params.put("exam_id", examId);
        }
        else{
            params.put("exam_group_id", examGroupId);
        }

        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS) {
            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

        }else if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.TEACHER){

            params.put(RequestKeyHelper.STUDENT_ID, selectedStudentId);
        }

        else if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.STUDENT){

            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getPaidInfo().getProfileId());
        }


        //AppRestClient.post(URLHelper.URL_GET_SINGLE_REPORT_CARD, params, singleReportCardHandler);
        singleReportCard(params);

    }

    private void singleReportCard(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().getSingleReportCard(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.e("###RESPONSE", "is: "+response.body());

                        uiHelper.dismissLoadingDialog();


                        Wrapper modelContainer = GsonParser.getInstance()
                                .parseServerResponse2(response.body());

                        if (modelContainer.getStatus().getCode() == 200) {


                            JsonObject object = modelContainer.getData().get("result").getAsJsonObject();
                            data = gson.fromJson(object.toString(), ClassTestItem.class);

                            initAction();

                        }


                        else {

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
    AsyncHttpResponseHandler singleReportCardHandler = new AsyncHttpResponseHandler() {

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

            Log.e("###RESPONSE", "is: "+responseString);

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {


                JsonObject object = modelContainer.getData().get("result").getAsJsonObject();
                data = gson.fromJson(object.toString(), ClassTestItem.class);

                initAction();

            }


            else {

            }



        };
    };


    private String getDecimalFormatNumber(float number)
    {
        String value = "";

        Locale locale  = new Locale("en", "UK");
        String pattern = "#.##";

        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);


        value = decimalFormat.format(number);

        return value;
    }

    private String getDecimalFormatNumber(String numberString)
    {
        String value = "";
        float number = 0;

        try {
            number = Float.parseFloat(numberString);
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }

        Locale locale  = new Locale("en", "UK");
        String pattern = "#.##";

        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        value = decimalFormat.format(number);

        return value;
    }



}
