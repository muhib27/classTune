package com.classtune.app.schoolapp.callbacks;

import com.classtune.app.schoolapp.viewhelpers.GradeDialog;

import java.util.List;

public interface onGradeDialogButtonClickListener {

	public void onDoneBtnClick(GradeDialog gradeDialog, String gradeStr, List<Integer> grades);
	
}
