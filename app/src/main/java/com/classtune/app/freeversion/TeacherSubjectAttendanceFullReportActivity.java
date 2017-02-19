package com.classtune.app.freeversion;

import android.view.View;

/**
 * Created by BLACK HAT on 19-Feb-17.
 */

public class TeacherSubjectAttendanceFullReportActivity extends ChildContainerActivity{

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }
}
