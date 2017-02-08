package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.HomeworkData;
import com.classtune.app.schoolapp.model.TeacherClassWork;
import com.classtune.app.schoolapp.model.TeacherHomeworkData;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.MyTagHandler;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.ExpandableTextView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SingleTeacherClassworkActivity extends ChildContainerActivity {
    private UIHelper uiHelper;
    private String id;
    private TextView tvLesson;
    private TextView tvShift;
    private TextView tvCourse;
    private TextView tvSection;
    private TextView tvDueDate;
    private TextView tvAssignDate;
    private LinearLayout mLinearLayout;
    //private WebView webViewContent;

    private ExpandableTextView txtContent;
    private TextView tvSubject;
    private CustomButton btnDone;
    private ImageView ivSubjectIcon;

    private LinearLayout bottmlay;

    private TeacherClassWork data;
    private Gson gson;

    private Button btnDownload;
    private LinearLayout layoutDownloadHolder;

    private LinearLayout layoutHorizontalBar;
    private Button btnEdit;
    private static final int REQUEST_EDIT_HOMEWORK = 56;
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
        setContentView(R.layout.activity_single_teacher_classwork);


        gson = new Gson();

        uiHelper = new UIHelper(SingleTeacherClassworkActivity.this);

        if(getIntent().getExtras() != null)
            this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_CLASSWORK);

        initView();
        initApicall();
    }
    private void initView()
    {
        //this.webViewContent = (WebView)this.findViewById(R.id.webViewContent);
        this.txtContent = (ExpandableTextView)this.findViewById(R.id.txtContent);
        this.tvLesson = (TextView) this.findViewById(R.id.tv_homework_content);
        this.tvSubject = (TextView) this.findViewById(R.id.tv_teacher_feed_subject_name);
        this.tvShift = (TextView)this.findViewById(R.id.tv_teacher_homewrok_feed_shift);
        this.tvCourse = (TextView)this.findViewById(R.id.tv_teacher_homework_feed_course);
        this.tvSection = (TextView)this.findViewById(R.id.tv_teacher_homework_feed_section);
        this.tvAssignDate = (TextView)this.findViewById(R.id.txtAssignDate);
        this.mLinearLayout = (LinearLayout)this.findViewById(R.id.single_teacher_homework_detail_visibility);

        this.btnDone = (CustomButton) this.findViewById(R.id.btn_done);
        this.ivSubjectIcon = (ImageView) this.findViewById(R.id.imgViewCategoryMenuIcon);
        this.bottmlay = (LinearLayout)this.findViewById(R.id.bottmlay);
        this.btnDownload = (Button)this.findViewById(R.id.btnDownload);
        this.layoutDownloadHolder = (LinearLayout)this.findViewById(R.id.layoutDownloadHolder);

        this.layoutHorizontalBar = (LinearLayout)this.findViewById(R.id.layoutHorizontalBar);
        this.btnEdit = (Button)this.findViewById(R.id.btnEdit);

    }



    private void initAction()
    {
        mLinearLayout.setVisibility(View.VISIBLE);
        this.tvLesson.setText(data.getClasswork_name());
        this.txtContent.setText(Html.fromHtml(data.getContent(), null, new MyTagHandler()));


       /* btnDone.setTitleText(getString(R.string.java_singleteacherhomeworkactivity_done_by)+data.getDone());
        btnDone.setTextSize(16);*/

        this.tvSubject.setText(data.getSubjects());
        this.tvShift.setText(data.getBatch());
        this.tvCourse.setText(data.getCourse());
        this.tvSection.setText(data.getSection());
        this.tvAssignDate.setText(data.getAssign_date());




        this.ivSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubjects_icon(), this));




        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SingleTeacherClassworkActivity.this, TeacherHomeworkDoneActivity.class);
                intent.putExtra(AppConstant.ID_TEACHER_HOMEWORK_DONE, data.getId());
                startActivity(intent);
            }
        });




        if(!TextUtils.isEmpty(data.getAttachment_file_name()))
        {
            layoutDownloadHolder.setVisibility(View.VISIBLE);
        }
        else
        {
            layoutDownloadHolder.setVisibility(View.GONE);
        }



        btnDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //http://api.champs21.com/api/freeuser/downloadattachment?id=47


                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://api.champs21.com/api/freeuser/downloadclasswork?id=" + data.getId())));
            }
        });


		/*if(data.getIsEditable() == true)
		{
			btnEdit.setVisibility(View.VISIBLE);
			layoutHorizontalBar.setVisibility(View.VISIBLE);
		}
		else
		{
			btnEdit.setVisibility(View.GONE);
			layoutHorizontalBar.setVisibility(View.GONE);
		}*/

        this.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.is_editable() == true)
                {
                    Intent intent = new Intent(SingleTeacherClassworkActivity.this, SingleTeacherEditClassworkActivity.class);
                    intent.putExtra(AppConstant.ID_SINGLE_CLASSWORK, data.getId());
                    startActivityForResult(intent, REQUEST_EDIT_HOMEWORK);
                }
                else
                {
                    Toast.makeText(SingleTeacherClassworkActivity.this, getString(R.string.java_singleteacherdrafthomeworkactivity_edit_from_website), Toast.LENGTH_SHORT).show();
                }


            }
        });





    }





    @SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
    private void showWebViewContent(String text, WebView webView) {

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        webView.loadDataWithBaseURL("", text, mimeType, encoding, null);
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());


    }



    private List<HomeworkData> parseHomeworkData(String object) {

        List<HomeworkData> tags = new ArrayList<HomeworkData>();
        Type listType = new TypeToken<List<HomeworkData>>() {
        }.getType();
        tags = (List<HomeworkData>) new Gson().fromJson(object, listType);
        return tags;
    }


    private void initApicall()
    {
        RequestParams params = new RequestParams();

        //app.showLog("adfsdfs", app.getUserSecret());

        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", this.id);



        AppRestClient.post(URLHelper.URL_SINGLE_TEACHER_CLASSWORK, params, singleTeacherHomeWorkHandler);
    }

    AsyncHttpResponseHandler singleTeacherHomeWorkHandler = new AsyncHttpResponseHandler() {

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

                JsonObject objHomework = modelContainer.getData().get("classwork").getAsJsonObject();
                data = gson.fromJson(objHomework.toString(), TeacherClassWork.class);

                Log.e("HHH", "data: " + data.getClasswork_name());

                initAction();

            }

            else {

            }



        };
    };

	/*@Override
	protected void onDestroy()
	{
		super.onDestroy();
		webViewContent.destroy();
	};*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {

            if(requestCode == REQUEST_EDIT_HOMEWORK)
            {
                initApicall();
            }
        }


    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }
}
