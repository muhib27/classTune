package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.model.ClassworkData;
import com.classtune.app.schoolapp.model.HomeworkData;
import com.classtune.app.schoolapp.model.ModelContainer;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.MyTagHandler;
import com.classtune.app.schoolapp.utils.ReminderHelper;
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

public class SingleClassworkActivity extends ChildContainerActivity {


        private UIHelper uiHelper;
        private String id;
        private TextView tvLesson;
        //private WebView webViewContent;

        private ExpandableTextView txtContent;
        private TextView tvSubject;
        private TextView tvDueDate;
        private TextView tvAssignDate;
        private TextView section;
        private CustomButton btnDone;
        private CustomButton btnReminder;
        private ImageView ivSubjectIcon;

        private LinearLayout bottmlay;

        private ClassworkData data;
        private Gson gson;

        private Button btnDownload;
        private LinearLayout layoutDownloadHolder;

        private RelativeLayout layoutMessage;
        private LinearLayout layoutDataContainer;


        @Override
        protected void onResume() {
            // TODO Auto-generated method stub
            super.onResume();
            homeBtn.setVisibility(View.VISIBLE);
            logo.setVisibility(View.GONE);
        }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_single_classwork);

            gson = new Gson();

            uiHelper = new UIHelper(SingleClassworkActivity.this);

            if(getIntent().getExtras() != null)
                this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_CLASSWORK);

            initView();
            initApicall();



        /*if(getIntent().getExtras()!=null)
        {
            Log.e("EXTRAS", "is: "+getIntent().getExtras().getBundle("total_unread_extras").getString("rid"));
        }*/

            if(getIntent().getExtras()!=null)
            {
                if(getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");


                    GcmIntentService.initApiCall(rid, rtype);
                }
            }





        }


        private void initView()
        {
            //this.webViewContent = (WebView)this.findViewById(R.id.webViewContent);
            this.txtContent = (ExpandableTextView)this.findViewById(R.id.txtContent);
            this.tvLesson = (TextView) this.findViewById(R.id.tv_homework_content);
            this.tvSubject = (TextView) this.findViewById(R.id.tv_teacher_feed_subject_name);
            //this.tvDueDate = (TextView) this.findViewById(R.id.tv_teacher_homewrok_feed_date);
            this.tvAssignDate = (TextView) this.findViewById(R.id.tv_teacher_homewrok_feed_date);
            this.section = (TextView) this.findViewById(R.id.tv_teavher_homework_feed_section);
            this.btnDone = (CustomButton) this.findViewById(R.id.btn_done);
            this.btnReminder = (CustomButton) this.findViewById(R.id.btn_reminder);
            this.ivSubjectIcon = (ImageView) this.findViewById(R.id.imgViewCategoryMenuIcon);
            this.bottmlay = (LinearLayout)this.findViewById(R.id.bottmlay);
            this.btnDownload = (Button)this.findViewById(R.id.btnDownload);
            this.layoutDownloadHolder = (LinearLayout)this.findViewById(R.id.layoutDownloadHolder);

            layoutMessage = (RelativeLayout)this.findViewById(R.id.layoutMessage);
            layoutDataContainer = (LinearLayout)this.findViewById(R.id.layoutDataContainer);


        }



        private void initAction()
        {
            this.tvLesson.setText(data.getName());
            this.txtContent.setText(Html.fromHtml(data.getContent(), null, new MyTagHandler()));

/*
            if ( data.getIsDone().equalsIgnoreCase(AppConstant.ACCEPTED) ||
                    data.getIsDone().equalsIgnoreCase(AppConstant.SUBMITTED)) {
                btnDone.setImage(R.drawable.done_tap);
                btnDone.setTitleColor(this.getResources().getColor(R.color.classtune_green_color));

                btnDone.setEnabled(false);
            } else {

                btnDone.setImage(R.drawable.done_normal);
                btnDone.setTitleColor(this.getResources().getColor(R.color.gray_1));

                if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.STUDENT) {
                    btnDone.setEnabled(true);
                }
                if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS) {
                    btnDone.setEnabled(false);
                }

                btnDone.setTag( data.getId());
            }*/


            this.tvSubject.setText(data.getSubjects());

            //String[] parts = data.getDueDate().split(" ");
           // String part1 = parts[0];
            //this.tvDueDate.setText(part1);
            this.tvAssignDate.setText(data.getAssign_date());

      /*      this.section.setText(getString(R.string.java_singlehomeworkactivity_by)+data.getTeacherName());

            this.ivSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubject_icon_name(), this));

            if(data.getTimeOver() == 0)
            {
                bottmlay.setVisibility(View.VISIBLE);
            }
            else if(data.getTimeOver() == 1)
            {
                bottmlay.setVisibility(View.GONE);
            }
*/

            btnDone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    processDoneButton((CustomButton) v);
                }
            });

            btnReminder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final CustomButton btn = (CustomButton) v;
                    setButtonState(btn, R.drawable.btn_reminder_tap, false, getString(R.string.btn_reminder));


                    AppUtility.listenerDatePickerCancel = new AppUtility.IDatePickerCancel() {
                        @Override
                        public void onCancelCalled() {

                            Log.e("CCCCC", "cancel called");
                            btn.setImage(R.drawable.btn_reminder_normal);
                            btn.setTitleColor(SingleClassworkActivity.this.getResources().getColor(R.color.gray_1));
                            btn.setEnabled(true);
                        }
                    };



                   // AppUtility.showDateTimePicker(AppConstant.KEY_HOMEWORK+data.getId(), data.getSubject()+ ": " + AppConstant.NOTIFICATION_HOMEWORK, data.getName(), SingleClassworkActivity.this);
                }
            });



          /*  if(!TextUtils.isEmpty(data.getAttachmentFileName()))
            {
                layoutDownloadHolder.setVisibility(View.VISIBLE);
            }
            else
            {
                layoutDownloadHolder.setVisibility(View.GONE);
            }*/



            btnDownload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //http://api.champs21.com/api/freeuser/downloadattachment?id=47


                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("http://api.champs21.com/api/freeuser/downloadattachment?id=" + data.getId())));
                }
            });


            if (ReminderHelper.getInstance().reminder_map.containsKey(AppConstant.KEY_HOMEWORK+data.getId())){
                setButtonState(btnReminder, R.drawable.btn_reminder_tap, false, getString(R.string.btn_reminder));

            }else {
                setButtonState(btnReminder, R.drawable.btn_reminder_normal, true, getString(R.string.btn_reminder));
            }


        }

        @SuppressLint("ResourceAsColor")
        private void setButtonState(CustomButton btn, int imgResId, boolean enable , String btnText) {

            btn.setImage(imgResId);
            btn.setTitleText(btnText);
            btn.setEnabled(enable);
            if(enable) {
                setBtnTitleColor(btn, R.color.gray_1);
            } else {
                setBtnTitleColor(btn, R.color.classtune_green_color);
            }
        }
        private void setBtnTitleColor(CustomButton btn, int colorId) {
            btn.setTitleColor(this.getResources().getColor(colorId));
        }

        protected void processDoneButton(CustomButton button) {
            // TODO Auto-generated method stub
            RequestParams params = new RequestParams();

            Log.e("User secret", UserHelper.getUserSecret());
            Log.e("Ass_ID", button.getTag().toString());

            params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
            params.put(RequestKeyHelper.ASSIGNMENT_ID, button.getTag().toString());

            AppRestClient.post(URLHelper.URL_HOMEWORK_DONE, params, doneBtnHandler);
        }

        AsyncHttpResponseHandler doneBtnHandler = new AsyncHttpResponseHandler() {
            public void onFailure(Throwable arg0, String arg1) {
                Log.e("button", "failed");
                uiHelper.showMessage(getString(R.string.internet_error_text));
                uiHelper.dismissLoadingDialog();
            };

            public void onStart() {
                Log.e("button", "onstart");
                uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
            };

            public void onSuccess(int arg0, String response) {
                Log.e("response", response);
                Log.e("button", "success");
                uiHelper.dismissLoadingDialog();

                ModelContainer modelContainer = GsonParser.getInstance().parseGson(response);

                /*if (modelContainer.getStatus().getCode() == 200) {
                    data.setIsDone(AppConstant.ACCEPTED);

                    btnDone.setImage(R.drawable.done_tap);
                    btnDone.setTitleColor(SingleClassworkActivity.this.getResources().getColor(R.color.classtune_green_color));

                    btnDone.setEnabled(false);

                } else {
                    uiHelper.showMessage(getString(R.string.java_singlehomeworkactivity_error_in_operation));
                }*/



                Log.e("status code", modelContainer.getStatus().getCode() + "");
            };
        };



        @SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
        private void showWebViewContent(String text, WebView webView) {

            final String mimeType = "text/html";
            final String encoding = "UTF-8";

            webView.loadDataWithBaseURL("", text, mimeType, encoding, null);
            WebSettings webViewSettings = webView.getSettings();
            webViewSettings.setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());


        }



        private List<ClassworkData> parseHomeworkData(String object) {

            List<ClassworkData> tags = new ArrayList<ClassworkData>();
            Type listType = new TypeToken<List<ClassworkData>>() {
            }.getType();
            tags = (List<ClassworkData>) new Gson().fromJson(object, listType);
            return tags;
        }


        private void initApicall()
        {
            RequestParams params = new RequestParams();

            //app.showLog("adfsdfs", app.getUserSecret());

            params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
            params.put("id", this.id);
            Log.e("SINGLE_HOMEWORK", "id: "+this.id);

            if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                if(getIntent().getExtras()!=null)
                {
                    if(getIntent().getExtras().containsKey("total_unread_extras"))
                    {
                        String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                        String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                        params.put(RequestKeyHelper.STUDENT_ID, getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                        params.put(RequestKeyHelper.BATCH_ID, getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


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

                //Log.e("STU_ID", userHelper.getUser().getSelectedChild().getId());
                //Log.e("STU_PROFILE_ID", userHelper.getUser().getSelectedChild().getProfileId());
                //Log.e("STU_BATCH_ID", userHelper.getUser().getSelectedChild().getBatchId());
            }


            AppRestClient.post(URLHelper.URL_SINGLE_CLASSWORK, params, singleHomeWorkHandler);
        }

        AsyncHttpResponseHandler singleHomeWorkHandler = new AsyncHttpResponseHandler() {

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

                    layoutDataContainer.setVisibility(View.VISIBLE);
                    layoutMessage.setVisibility(View.GONE);

                    JsonObject objHomework = modelContainer.getData().get("classwork").getAsJsonObject();
                    data = gson.fromJson(objHomework.toString(), ClassworkData.class);

                    Log.e("HHH", "data: " + data.getName());

                    initAction();

                }

                else if(modelContainer.getStatus().getCode() == 400 && modelContainer.getStatus().getCode() != 404)
                {
                    layoutDataContainer.setVisibility(View.GONE);
                    layoutMessage.setVisibility(View.VISIBLE);
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


    }