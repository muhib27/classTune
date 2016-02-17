package com.classtune.app.schoolapp.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.GraphSubjectType;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.ProgressExam;
import com.classtune.app.schoolapp.model.SubjectSeries;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tasvir on 4/8/2015.
 */
public class ProgressGraphFragment extends Fragment implements View.OnClickListener {

    private TextView subjectTextView;
    private ImageButton btnSubjectSelect;
    private ProgressBar pbGraph;
    private LinearLayout graphView;
    private UserHelper userHelper;
    private GraphicalView mChartView;
    private String subjectTextString = "";
    private String subjectIdString ="";
    private RelativeLayout layoutSelectSubject;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userHelper = new UserHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_graph, container, false);
        subjectTextView = (TextView) view.findViewById(R.id.tv_prog_selected_subject);
        btnSubjectSelect = (ImageButton) view.findViewById(R.id.btn_prog_select_subject);
        btnSubjectSelect.setOnClickListener(this);
        pbGraph = (ProgressBar) view.findViewById(R.id.pb_graph);
        graphView = (LinearLayout) view.findViewById(R.id.graph_view);
        layoutSelectSubject = (RelativeLayout)view.findViewById(R.id.layoutSelectSubject);
        layoutSelectSubject.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_prog_select_subject:
                showSubjectPicker();
                break;
            case R.id.layoutSelectSubject:
                showSubjectPicker();
                break;
            default:
                break;
        }
    }

    private void setGraphVisibility(boolean status) {
        graphView.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        pbGraph.setVisibility(!status ? View.VISIBLE : View.INVISIBLE);
    }
    public void showSubjectPicker() {

        CustomPickerWithLoadData picker = CustomPickerWithLoadData.newInstance(0);
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        if(userHelper.getUser().getType()== UserHelper.UserTypeEnum.PARENTS){

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


            //params.put(RequestKeyHelper.BATCH_ID,userHelper.getUser().getSelectedChild().getBatchId());
            //params.put(RequestKeyHelper.STUDENT_ID,userHelper.getUser().getSelectedChild().getProfileId());
        }


        picker.setData(PickerType.GRAPH,params, URLHelper.URL_GET_GRAPH_SUBJECTS, PickerCallback , "Select Subject");
        picker.show(getChildFragmentManager(), null);
    }
    Picker.PickerItemSelectedListener PickerCallback = new Picker.PickerItemSelectedListener() {

        @Override
        public void onPickerItemSelected(BaseType item) {

            switch (item.getType()) {
                case GRAPH:
                    subjectTextString = item.getText();
                    subjectIdString = ((GraphSubjectType)item).getId();
                    showPicker(PickerType.TEACHER_BATCH);
                    break;
                default:
                    break;
            }
        }
    };

    public void showPicker(PickerType type) {

        Picker picker = Picker.newInstance(0);
        List<BaseType> examTypes = new ArrayList<BaseType>(2);
        examTypes.add(new Batch("1","Class Test"));
        examTypes.add(new Batch("3","Term Test"));
        picker.setData(type, examTypes, PickerCallbackExamType , "Select Exam Type");
        picker.show(getChildFragmentManager(), null);
    }
    Picker.PickerItemSelectedListener PickerCallbackExamType = new Picker.PickerItemSelectedListener() {

        @Override
        public void onPickerItemSelected(BaseType item) {

            switch (item.getType()) {
                case TEACHER_BATCH:
                    subjectTextView.setText(subjectTextString);
                    if(subjectTextString.equalsIgnoreCase("All")){
                            fetchAllGraphData(((Batch)item).getId());
                    }else {
                           fetchGraphData(subjectIdString,((Batch)item).getId());
                    }

                    break;
                default:
                    break;
            }

        }
    };
    private void fetchGraphData(String subjectId,String examType){
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put(RequestKeyHelper.SUBJECT_ID, subjectId);
        params.put(RequestKeyHelper.EXAM_CATEGORY,examType);
        if(userHelper.getUser().getType()== UserHelper.UserTypeEnum.PARENTS){

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

            //params.put(RequestKeyHelper.BATCH_ID,userHelper.getUser().getSelectedChild().getBatchId());
            //params.put(RequestKeyHelper.STUDENT_ID,userHelper.getUser().getSelectedChild().getProfileId());
        }
        AppRestClient.post(URLHelper.URL_GET_REPORT_PROGRESS, params,  new AsyncHttpResponseHandler(){
            public void onFailure(Throwable arg0, String arg1) {
                setGraphVisibility(true);
//                Log.e("error", arg1);
            };

            public void onStart() {
                setGraphVisibility(false);

            };

            public void onSuccess(int arg0, String responseString) {

                setGraphVisibility(true);
                Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                        responseString);
                JsonObject progress = wrapper.getData().getAsJsonObject("progress");
                Log.e("response graph", (progress.get("exam")).toString());
                openChart(GsonParser.getInstance().parseGraphDataList(
                        (progress.get("exam")).toString()));
                //adapter.notifyDataSetChanged();
            };
        });

    }

    private void fetchAllGraphData(String examType){
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put(RequestKeyHelper.EXAM_CATEGORY,examType);
        if(userHelper.getUser().getType()== UserHelper.UserTypeEnum.PARENTS){

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

            //params.put(RequestKeyHelper.BATCH_ID,userHelper.getUser().getSelectedChild().getBatchId());
            //params.put(RequestKeyHelper.STUDENT_ID,userHelper.getUser().getSelectedChild().getProfileId());
        }
        AppRestClient.post(URLHelper.URL_GET_REPORT_PROGRESS_ALL, params,  new AsyncHttpResponseHandler(){
            public void onFailure(Throwable arg0, String arg1) {
                setGraphVisibility(true);
//                Log.e("error", arg1);
            };

            public void onStart() {
                setGraphVisibility(false);

            };

            public void onSuccess(int arg0, String responseString) {

                setGraphVisibility(true);
                Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                        responseString);
                JsonObject progress = wrapper.getData().getAsJsonObject("progress");
               // Log.e("response graph",(progress.get("exam")).toString());
                openChartAll(GsonParser.getInstance().parseGraphDataListAll(
                        (progress.get("subject")).toString()));
                //adapter.notifyDataSetChanged();
            };
        });
    }

    private void openChartAll(List<SubjectSeries> exam){
        String[] titles = new String[exam.size()] ;//{ "Crete Air Temperature", "Skiathos Air Temperature" };
        List<double[]> x = new ArrayList<double[]>();
        List<double[]> values = new ArrayList<double[]>();
        int[] colors = new int[exam.size()];
        PointStyle[] styles = new PointStyle[exam.size()]; //{ PointStyle.CIRCLE, PointStyle.DIAMOND };
        String[] types = new String[exam.size()];
        for (int i = 0; i < exam.size(); i++) {
            titles[i]=exam.get(i).getName();
            double [] allDouble = new double[exam.get(i).getAllExam().size()];
            double [] valuesDouble = new double[exam.get(i).getAllExam().size()];
            colors[i]= Color.parseColor(exam.get(i).getColor());
            styles[i]=PointStyle.CIRCLE;
            types[i]= LineChart.TYPE;
            for(int j=0;j<exam.get(i).getAllExam().size();j++){
                    allDouble[j]=j;
                    valuesDouble[j]= Double.parseDouble(exam.get(i).getAllExam().get(j).getPoint());
            }
            x.add(allDouble);
            values.add(valuesDouble);
        }

        /*values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
                13.9 });
        values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });*/
        //int[] colors = new int[] { Color.GREEN, Color.rgb(200, 150, 0) };
        //PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        renderer.setPointSize(5.5f);
        int length = renderer.getSeriesRendererCount();

        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            r.setLineWidth(5);
            r.setFillPoints(true);
            renderer.setXLabels(0);
           // renderer.addXTextLabel(i,exam.get(i).getName());
        }
        SubjectSeries ss = getHighestExam(exam);
        for(int i=0;i<ss.getAllExam().size();i++){
            renderer.addXTextLabel(i,ss.getAllExam().get(i).getName());
        }
        setChartSettings(renderer, "All Subject Comparison Graph", "Exam Name", "Percentage", 0.5, 12.5, 0, 110,
                Color.BLACK, Color.BLACK);

        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        //renderer.setXLabels(0);
        renderer.setXAxisMax(5);
        renderer.setYLabels(10);
        renderer.setXAxisMin(-.1);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setShowGrid(false);
        renderer.setXLabelsAlign(Paint.Align.RIGHT);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
        renderer.setShowLegend(true);
        //multiRenderer.setLegendHeight(150);
        renderer.setFitLegend(true);
        renderer.setMargins(new int[] { 50, 50, 50, 22 });
        renderer.setMarginsColor(Color.WHITE);



        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);

        if(mChartView!=null)graphView.removeView(mChartView);
        mChartView = ChartFactory.getCombinedXYChartView(getActivity(),dataset,renderer,types);

        graphView.addView(mChartView  , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
    }
    private SubjectSeries getHighestExam(List<SubjectSeries> exam){
        int max = 0;
        SubjectSeries m = null;
        for(SubjectSeries s:exam){
            if(s.getAllExam().size()>max){
                max = s.getAllExam().size();
                m = s;
            }
        }
        return m;

    }
    private void openChart(List<ProgressExam> exam){
        int[] x = { 0,1,2,3,4,5,6,7 };
        int[] income = { 2000,2500,2700,3000,2800,3500,3700,3800};
        int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400 };




        // Creating an  XYSeries for Income
        //CategorySeries incomeSeries = new CategorySeries("Income");
        XYSeries incomeSeries = new XYSeries("Your Percentage");
        // Creating an  XYSeries for Income
        XYSeries expenseSeries = new XYSeries("Highest Percentage");
        // Adding data to Income and Expense Series

        XYSeries averageSeries = new XYSeries("Average Percentage");
        for(int i=0;i<exam.size();i++){
            incomeSeries.add(i,exam.get(i).getYour_percent());//income[i]
            expenseSeries.add(i,exam.get(i).getMax_mark_percent());//expense[i]
            averageSeries.add(i,exam.get(i).getAvg_mark_percent());
        }


        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        dataset.addSeries(averageSeries);


        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.rgb(130, 130, 230));
        incomeRenderer.setFillPoints(false);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setChartValuesTextSize(val);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.rgb(220, 80, 80));
        expenseRenderer.setFillPoints(false);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setChartValuesTextSize(val);


        XYSeriesRenderer averageRenderer = new XYSeriesRenderer();
        averageRenderer.setColor(Color.rgb(102, 0, 102));
        averageRenderer.setFillPoints(false);
        averageRenderer.setLineWidth(2);
        averageRenderer.setDisplayChartValues(true);
        averageRenderer.setChartValuesTextSize(val);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        //multiRenderer.setChartTitle("Income vs Expense Chart");
        //multiRenderer.setXTitle("Year 2012");
        //multiRenderer.setYTitle("Amount in Dollars");
        for(int i=0; i< exam.size();i++){
            multiRenderer.addXTextLabel(i, exam.get(i).getExam_name());//mMonth[i]
        }


        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.removeAllRenderers();
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);
        multiRenderer.addSeriesRenderer(averageRenderer);

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setDisplayValues(true);
        multiRenderer.setShowLegend(true);
        //multiRenderer.setLegendHeight(150);
        multiRenderer.setFitLegend(true);
        multiRenderer.setMargins(new int[] { 50, 50, 50, 22 });
        //multiRenderer.setPanEnabled(false);

        multiRenderer.setLabelsTextSize(val);
        multiRenderer.setLegendTextSize(val);
        multiRenderer.setLabelsColor(Color.WHITE);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setXAxisMax(3);
        multiRenderer.setXAxisMin(-1);
        multiRenderer.setYAxisMax(120);
        multiRenderer.setYAxisMin(0);
        float barwidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, metrics);
        multiRenderer.setBarWidth(barwidth);



        // Creating an intent to plot bar chart using dataset and multipleRenderer
        //Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        // Start Activity
        //startActivity(new CombinedTemperatureChart().execute(this));
        if(mChartView!=null)graphView.removeView(mChartView);
        mChartView = ChartFactory.getBarChartView(getActivity(),dataset,multiRenderer,BarChart.Type.DEFAULT);

        graphView.addView(mChartView  , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

    }
    private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                                   List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i]);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 0 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

}
