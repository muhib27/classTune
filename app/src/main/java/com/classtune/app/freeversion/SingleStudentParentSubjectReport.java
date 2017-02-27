package com.classtune.app.freeversion;

import android.os.Bundle;
import android.view.View;

import com.classtune.app.R;

/**
 * Created by BLACK HAT on 27-Feb-17.
 */

public class SingleStudentParentSubjectReport extends ChildContainerActivity {


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
        setContentView(R.layout.activity_single_student_parent_subject_report);

    }
}
