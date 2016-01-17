package com.classtune.app.schoolapp.viewhelpers;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.classtune.app.R;
import com.classtune.app.schoolapp.callbacks.onGradeDialogButtonClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class GradeDialog extends Dialog implements android.view.View.OnClickListener, OnCheckedChangeListener {
	

	CheckBox cbPg, cbKg, cbOne, cbTwo, cbThree, cbFour, cbFive, cbSix, cbSeven, cbEight, cbNine, cbTen;

	Button btnDone;

	ArrayList<Integer> gradeNumbers;

	private onGradeDialogButtonClickListener listener;

	String grades = "";

	public GradeDialog(Context context,onGradeDialogButtonClickListener mListener, String selectedGrades) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_select_grade);
		this.setCancelable(false);
		this.listener=mListener;


		btnDone = (Button) findViewById(R.id.btn_done);
		btnDone.setOnClickListener(this);

		cbPg = (CheckBox) findViewById(R.id.checkBox_playgroup);
		cbPg.setOnCheckedChangeListener(this);

		cbKg = (CheckBox) findViewById(R.id.checkBox_kg);
		cbKg.setOnCheckedChangeListener(this);

		cbOne = (CheckBox) findViewById(R.id.checkBox_one);
		cbOne.setOnCheckedChangeListener(this);

		cbTwo = (CheckBox) findViewById(R.id.checkBox_two);
		cbTwo.setOnCheckedChangeListener(this);

		cbThree = (CheckBox) findViewById(R.id.checkBox_three);
		cbThree.setOnCheckedChangeListener(this);

		cbFour = (CheckBox) findViewById(R.id.checkBox_four);
		cbFour.setOnCheckedChangeListener(this);

		cbFive = (CheckBox) findViewById(R.id.checkBox_five);
		cbFive.setOnCheckedChangeListener(this);

		cbSix = (CheckBox) findViewById(R.id.checkBox_six);
		cbSix.setOnCheckedChangeListener(this);

		cbSeven = (CheckBox) findViewById(R.id.checkBox_seven);
		cbSeven.setOnCheckedChangeListener(this);

		cbEight = (CheckBox) findViewById(R.id.checkBox_eight);
		cbEight.setOnCheckedChangeListener(this);

		cbNine = (CheckBox) findViewById(R.id.checkBox_nine);
		cbNine.setOnCheckedChangeListener(this);

		cbTen = (CheckBox) findViewById(R.id.checkBox_ten);
		cbTen.setOnCheckedChangeListener(this);


		gradeNumbers = new ArrayList<Integer>();

		String[] temp = selectedGrades.split(",");
		
		for (String s : temp) {
			if (!s.equalsIgnoreCase("")) {
				Integer i;
//				if (s.equalsIgnoreCase("Playgroup")) {
//					i = -1;
//				} else if (s.equalsIgnoreCase("KG")) {
//					i = 0;
//				} else {
					i = Integer.parseInt(s);
//				}

//				gradeNumbers.add(i);

				doCheckSelectedNumbers(i);
			}
			
		}



	}

	private void doCheckSelectedNumbers(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case -1:
			cbPg.setChecked(true);
			break;

		case 0:
			cbKg.setChecked(true);
			break;

		case 1:
			cbOne.setChecked(true);
			break;

		case 2:
			cbTwo.setChecked(true);
			break;

		case 3:
			cbThree.setChecked(true);
			break;

		case 4:
			cbFour.setChecked(true);
			break;

		case 5:
			cbFive.setChecked(true);
			break;

		case 6:
			cbSix.setChecked(true);
			break;

		case 7:
			cbSeven.setChecked(true);
			break;

		case 8:
			cbEight.setChecked(true);
			break;

		case 9:
			cbNine.setChecked(true);
			break;

		case 10:
			cbTen.setChecked(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_done:

			//			if (!grades.equalsIgnoreCase("")) {
			//				grades = grades.substring(0, grades.length() - 1);
			//			}

			Collections.sort(gradeNumbers);

			for (int i = 0; i < gradeNumbers.size(); i++) {
				if (i == gradeNumbers.size() - 1) {
					grades = grades + gradeNumbers.get(i) + "";
				} else {
					grades = grades + gradeNumbers.get(i) + ",";
				}
			}

			Log.e("Grades", grades);

			listener.onDoneBtnClick(this,grades, gradeNumbers);
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		switch (buttonView.getId()) {
		case R.id.checkBox_playgroup:

			checkNumber(isChecked, -1);

			break;

		case R.id.checkBox_kg:

			checkNumber(isChecked, 0);

			break;

		case R.id.checkBox_one:

			checkNumber(isChecked, 1);

			break;

		case R.id.checkBox_two:

			checkNumber(isChecked, 2);

			break;

		case R.id.checkBox_three:

			checkNumber(isChecked, 3);

			break;

		case R.id.checkBox_four:

			checkNumber(isChecked, 4);

			break;

		case R.id.checkBox_five:

			checkNumber(isChecked, 5);

			break;

		case R.id.checkBox_six:

			checkNumber(isChecked, 6);

			break;

		case R.id.checkBox_seven:

			checkNumber(isChecked, 7);

			break;

		case R.id.checkBox_eight:

			checkNumber(isChecked, 8);

			break;

		case R.id.checkBox_nine:

			checkNumber(isChecked, 9);

			break;

		case R.id.checkBox_ten:

			checkNumber(isChecked, 10);

			break;

		default:
			break;
		}
	}
	
	

	private void checkNumber(boolean isChecked, Integer n) {
		// TODO Auto-generated method stub
		if (isChecked) {
			gradeNumbers.add(n);
		} else {
			
			for (int i = 0; i < gradeNumbers.size(); i++) {
				if (gradeNumbers.get(i).equals(n)) {
					gradeNumbers.remove(n);
					i--;
				}
			}

		}
	}

}
