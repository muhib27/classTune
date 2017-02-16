package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppConstant;

/**
 * Created by BLACK HAT on 16-Feb-17.
 */

public class TeacherSubjectAttendanceTakeActivity extends ChildContainerActivity{

    private String subjectId = "";
    private EditText txtSearch;
    private ListView listView;


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
        setContentView(R.layout.activity_teacher_subject_attendance);

        if(getIntent().getExtras() != null){
            subjectId = getIntent().getExtras().getString(AppConstant.KEY_ASSOCIATED_SUBJECT_ID);
        }

        Log.e("SUBJECT_ID", "is: "+subjectId);

    }

    private void initView(){

    }
}
