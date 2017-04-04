package com.classtune.app.freeversion;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.DatePickerFragment;
import com.classtune.app.schoolapp.model.LessonPlan;
import com.classtune.app.schoolapp.model.LessonPlanCategory;
import com.classtune.app.schoolapp.model.Subject;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by BLACK HAT on 05-Apr-15.
 */
public class EditLessonPlanActivity extends ChildContainerActivity{


    private UIHelper uiHelper;
    private UserHelper userHelper;


    private Gson gson;
    private LessonPlan data;

    private String id;

    private Button btnSave;
    private Button btnSaveAndAssign;

    private TextView txtDate;

    private EditText txtTitle;

    private TextView txtSelectCategory;
    private ImageButton btnSelectCategory;
    private LinearLayout layoutSelectCategoryActionHolder;

    private TextView txtSubjectClass;
    private ImageButton btnSubjectClass;
    private LinearLayout layoutSubjectClassActionHolder;

    private TextView txtLectureDate;
    private ImageButton btnLectureDate;
    private LinearLayout layoutLectureDateActionHolder;

    private EditText txtDescription;

    private List<LessonPlanCategory> listCategory;

    private String selectedCategoryId = null;

    private String selectedDate = null;

    private List<LessonPlan> listLessonPlan;

    private boolean  isSubjectLayoutClicked = true;
    private LinearLayout layoutSelectMultipleSubject;

    private List<Subject> listSubject;

    private List<String> listSubjectId;
    private List<String> listSubjectName;


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_lessonplan2);

        uiHelper = new UIHelper(this);
        userHelper = new UserHelper(this);

        if(getIntent().getExtras() != null)
            this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_LESSON_PLAN);

        Log.e("IDIDIDI", "is: " + id);

        gson = new Gson();


        listCategory = new ArrayList<LessonPlanCategory>();

        listLessonPlan = new ArrayList<LessonPlan>();
        listLessonPlan.clear();

        listSubject = new ArrayList<Subject>();

        listSubjectId = new ArrayList<String>();

        listSubjectName = new ArrayList<String>();


        initView();

        initApiCallGetEditData();
        //initApiCallSubject();

        initAction();
    }


    private void initView()
    {
        btnSave = (Button)this.findViewById(R.id.btnSave);
        btnSaveAndAssign = (Button)this.findViewById(R.id.btnSaveAndAssign);

        //btnSave.setButtonSelected(true, R.color.black);
        //btnSaveAndAssign.setButtonSelected(true, R.color.black);

        txtDate = (TextView)this.findViewById(R.id.txtDate);

        txtTitle = (EditText)this.findViewById(R.id.txtTitleOfLesson);

        txtSelectCategory = (TextView)this.findViewById(R.id.txtSelectCategory);
        btnSelectCategory = (ImageButton)this.findViewById(R.id.btnSelectCategory);
        layoutSelectCategoryActionHolder = (LinearLayout)this.findViewById(R.id.layoutSelectCategoryActionHolder);

        txtSubjectClass = (TextView)this.findViewById(R.id.txtSubjectClass);
        btnSubjectClass = (ImageButton)this.findViewById(R.id.btnSubjectClass);
        layoutSubjectClassActionHolder = (LinearLayout)this.findViewById(R.id.layoutSubjectClassActionHolder);

        txtLectureDate = (TextView)this.findViewById(R.id.txtLectureDate);
        btnLectureDate = (ImageButton)this.findViewById(R.id.btnLectureDate);
        layoutLectureDateActionHolder = (LinearLayout)this.findViewById(R.id.layoutLectureDateActionHolder);

        txtDescription = (EditText)this.findViewById(R.id.txtDescription);

        layoutSelectMultipleSubject = (LinearLayout)this.findViewById(R.id.layoutSelectMultipleSubject);
        layoutSelectMultipleSubject.setBackgroundColor(Color.parseColor("#eff0f4"));
    }

    private void initAction()
    {
        txtDate.setText(getCurrentDate());

        btnSelectCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallCategory();
            }
        });

        layoutSelectCategoryActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallCategory();
            }
        });

        btnLectureDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showDatepicker();
            }
        });

        layoutLectureDateActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showDatepicker();
            }
        });


        layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
        layoutSelectMultipleSubject.setVisibility(View.VISIBLE);


        btnSubjectClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);

                    if(listSubject.size() <=0)
                        initApiCallSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);


                }

                //initApiCallSubject();

            }
        });

        layoutSubjectClassActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                isSubjectLayoutClicked = !isSubjectLayoutClicked;

                if(isSubjectLayoutClicked)
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.parseColor("#eff0f4"));
                    layoutSelectMultipleSubject.setVisibility(View.VISIBLE);


                    if(listSubject.size() <=0)
                        initApiCallSubject();
                }
                else
                {
                    layoutSubjectClassActionHolder.setBackgroundColor(Color.WHITE);
                    //layoutSelectMultipleSubject.removeAllViews();
                    layoutSelectMultipleSubject.setVisibility(View.GONE);


                }



            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isValidData())
                    initApiCallAdd(false);
            }
        });

        btnSaveAndAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidData())
                    initApiCallAdd(true);
            }
        });


    }

    private String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        return AppUtility.getFormatedDateString(AppUtility.DATE_FORMAT_APP, c);
    }

    private void initApiCallCategory()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        AppRestClient.post(URLHelper.URL_LESSON_CATEGORY, params, lessonCategoryHandler);
    }


    AsyncHttpResponseHandler lessonCategoryHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listCategory.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayCategory = modelContainer.getData().get("category").getAsJsonArray();
                for (int i = 0; i < parseCategory(arrayCategory.toString()).size(); i++)
                {
                    listCategory.add(parseCategory(arrayCategory.toString()).get(i));
                }

                showCategoryPopup(btnSelectCategory);


            } else {

            }


        }

        ;
    };

    private ArrayList<LessonPlanCategory> parseCategory(String object) {
        ArrayList<LessonPlanCategory> data = new ArrayList<LessonPlanCategory>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlanCategory>>() {}.getType());
        return data;
    }

    private void showCategoryPopup(View view)
    {
        PopupMenu popup = new PopupMenu(this, view);
        //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());

        //popup.getMenu().add("All");

        for(int i=0;i<listCategory.size();i++)
            popup.getMenu().add(listCategory.get(i).getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                for(LessonPlanCategory b : listCategory)
                {
                    if(item.getTitle().toString().equalsIgnoreCase(b.getName()))
                        selectedCategoryId = b.getId();
                }

                Log.e("ITEM_ID", "id: " + selectedCategoryId);

                txtSelectCategory.setText(item.getTitle());

                return true;
            }
        });

        popup.show();
    }

    private void showDatepicker() {
        DatePickerFragment picker = new DatePickerFragment();
        picker.setCallbacks(datePickerCallback);
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    DatePickerFragment.DatePickerOnSetDateListener datePickerCallback = new DatePickerFragment.DatePickerOnSetDateListener() {

        @Override
        public void onDateSelected(int month, String monthName, int day,
                                   int year, String dateFormatServer, String dateFormatApp,
                                   Date date) {
            // TODO Auto-generated method stub
            txtLectureDate.setText(dateFormatApp);
            selectedDate = dateFormatServer;
            Log.e("DATE", "selecetd: " + selectedDate);
            //dateFormatServerString = dateFormatServer;
        }

    };


    private void initApiCallGetEditData()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", this.id);
        Log.e("Testing", "initApiCallGetEditData: "+id );

        AppRestClient.post(URLHelper.URL_GET_LESSONPLAN_EDIT_DATA, params, lessonGetEditDataHandler);
    }

    AsyncHttpResponseHandler lessonGetEditDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listCategory.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);


            initApiCallSubject();

            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayCategory = modelContainer.getData().get("category").getAsJsonArray();

                JsonArray arraySubject = modelContainer.getData().get("subjects").getAsJsonArray();

                txtSelectCategory.setText(arrayCategory.get(0).getAsJsonObject().get("name").getAsString());
                selectedCategoryId = arrayCategory.get(0).getAsJsonObject().get("id").getAsString();


                JsonObject objLessonPlan = modelContainer.getData().get("lessonplan").getAsJsonObject();
                data = gson.fromJson(objLessonPlan.toString(), LessonPlan.class);

                populateData(data);


            } else {

            }


        }

        ;
    };

    private ArrayList<LessonPlan> parseLessonPlan(String object) {
        ArrayList<LessonPlan> data = new ArrayList<LessonPlan>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlan>>() {}.getType());
        return data;
    }


    private void populateData(LessonPlan data)
    {

        txtTitle.setText(data.getTitle());

        if(!TextUtils.isEmpty(data.getPublishDate()))
            txtLectureDate.setText(AppUtility.getDateString(data.getPublishDate(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));


        txtDescription.setText(Html.fromHtml(data.getDescription()));
    }

    private void initApiCallSubject()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", this.id);

        AppRestClient.post(URLHelper.URL_LESSON_SUBJECT, params, lessonSubjectHandler);
    }


    AsyncHttpResponseHandler lessonSubjectHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            //listSubject.clear();


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arraySubject = modelContainer.getData().get("subjects").getAsJsonArray();
                for (int i = 0; i < parseSubject(arraySubject.toString()).size(); i++)
                {
                    listSubject.add(parseSubject(arraySubject.toString()).get(i));
                }

                //showSubjectPopup(btnSubjectClass);
                generateSubjectChooserLayout(layoutSelectMultipleSubject);






            } else {

            }


        }

        ;
    };

    private ArrayList<Subject> parseSubject(String object) {
        ArrayList<Subject> data = new ArrayList<Subject>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<Subject>>() {}.getType());
        return data;
    }


    private void generateSubjectChooserLayout(LinearLayout layout)
    {

        for (int i=0;i<listSubject.size();i++)
        {
            CheckBox cb = new CheckBox(this);
            cb.setPadding(5, 5, 5, 5);
            cb.setTag(i);
            cb.setButtonDrawable(R.drawable.check_btn);
            cb.setText(listSubject.get(i).getName());
            cb.setTextColor(Color.BLACK);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox btn = (CheckBox)buttonView;
                    int tag = (Integer)btn.getTag();

                    if(isChecked)
                    {
                        listSubjectId.add(listSubject.get(tag).getId());
                        refreshData(listSubjectId);
                        getIdWithComma();


                        listSubjectName.add(listSubject.get(tag).getName());
                        refreshData(listSubjectName);
                        getNameWithComma();
                    }
                    else
                    {
                        listSubjectId.remove(listSubject.get(tag).getId());
                        getIdWithComma();

                        listSubjectName.remove(listSubject.get(tag).getName());
                        getNameWithComma();
                    }

                    txtSubjectClass.setText(getNameWithComma());

                    Log.e("SUB_ID", "is: " + getIdWithComma());

                }


            });


            //if(i == listSubjectName.indexOf(""))


            if(listSubject.get(i).getSelected() == 1)
            {
                cb.setChecked(true);

                listSubjectId.add(listSubject.get(i).getId());
                refreshData(listSubjectId);
                getIdWithComma();


                listSubjectName.add(listSubject.get(i).getName());
                refreshData(listSubjectName);
                getNameWithComma();

                txtSubjectClass.setText(getNameWithComma());

            }

            layout.addView(cb);
        }

    }

    private String getIdWithComma()
    {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectId )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }

    private String getNameWithComma()
    {
        StringBuilder result = new StringBuilder();
        for ( String p : listSubjectName )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }

    private void refreshData(List<String> list)
    {
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
    }

    private boolean isValidData()
    {
        boolean result = true;

        if(TextUtils.isEmpty(txtTitle.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_insert_lesson_plan_title));
        }

        else if(TextUtils.isEmpty(txtSelectCategory.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_select_lesson_plan_category));
        }

        else if(TextUtils.isEmpty(txtDescription.getText().toString()))
        {
            result = false;
            showToast(getString(R.string.java_editLessonplanactivity_insert_lesson_plan_description));
        }



        return result;
    }
    private void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void initApiCallAdd(boolean isShow)
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", this.id);

        if(!TextUtils.isEmpty(getIdWithComma()))
            params.put("subject_ids", getIdWithComma());

        params.put("lessonplan_category_id", selectedCategoryId);
        params.put("title", txtTitle.getText().toString());

        if(!TextUtils.isEmpty(txtLectureDate.getText().toString()))
            params.put("publish_date", selectedDate);

        params.put("content", txtDescription.getText().toString());

        if(isShow)
        {
            params.put("is_show", "1");
        }
        else
        {
            params.put("is_show", "0");
        }



        AppRestClient.post(URLHelper.URL_LESSON_ADD, params, lessonAddHandler);
    }

    AsyncHttpResponseHandler lessonAddHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listSubject.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {

                Toast.makeText(EditLessonPlanActivity.this, R.string.java_editLessonplanactivity_successfully_edited_lesson_plan, Toast.LENGTH_SHORT).show();

                finish();


            } else {

            }


        }

        ;
    };


}
