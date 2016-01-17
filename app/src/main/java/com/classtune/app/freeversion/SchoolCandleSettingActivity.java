package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.SchoolCandleSetting;

import java.util.ArrayList;
import java.util.List;

public class SchoolCandleSettingActivity extends ChildContainerActivity {

	
	private CheckBox cBStudent;
	private CheckBox cBParents;
	private CheckBox cBTeacher;
	private CheckBox cBAlumni;
	
	private RadioGroup radioGroupCanComment;
	private RadioGroup radioShowHide;
	
	private RadioButton btnYes;
	private RadioButton btnNo;
	
	private RadioButton btnShow;
	private RadioButton btnHide;
	
	private ImageButton btnSave;
	
	
	private LinearLayout layoutShowHide;
	
	private int canComment = 0;
	private int isShow = 0;
	private List<Integer> typeList;
	
	private SchoolCandleSetting myParcelableObject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(com.classtune.app.R.layout.activity_school_candle_setting);
		
		initView();
		myParcelableObject = (SchoolCandleSetting)getIntent().getParcelableExtra("settings");
		initAction();
		
		updateUI();
	}
	
	private void updateUI()
	{
		RadioButton btn=(RadioButton)radioGroupCanComment.getChildAt(myParcelableObject.getCanComment());
		btn.setChecked(true);
		RadioButton btnShowHide=(RadioButton)radioShowHide.getChildAt(myParcelableObject.getIsShow());
		btnShowHide.setChecked(true);
		
		/*if(myParcelableObject.getCanComment() == 0)
		{
			btnNo.setChecked(true);
		}
		else
		{
			btnYes.setChecked(true);
		}
		
		if(myParcelableObject.getIsShow() == 0)
		{
			btnShow.setChecked(true);
		}
		else
		{
			btnHide.setChecked(true);
		}*/
		
		
		
		for(int i=0; i< myParcelableObject.getTypeList().size();i++)
		{
			switch (myParcelableObject.getTypeList().get(i)) {
			
			case 1:
				cBAlumni.setChecked(true);
			break;
			
			case 2:
				cBStudent.setChecked(true);
			break;
			
			case 3:
				cBParents.setChecked(true);
			break;
			
			case 4:
				cBTeacher.setChecked(true);
			break;
			

			
			default:
				break;
			}
		}
			
		
	}
	
	
	private void initView()
	{
		
		typeList = new ArrayList<Integer>();
		
		
		
		this.cBStudent = (CheckBox)this.findViewById(R.id.cb_candle_student);
		this.cBParents = (CheckBox)this.findViewById(R.id.cb_candle_parents);
		this.cBTeacher = (CheckBox)this.findViewById(R.id.cb_candle_teacher);
		this.cBAlumni = (CheckBox)this.findViewById(R.id.cb_candle_alumni);
		
		this.radioGroupCanComment = (RadioGroup)this.findViewById(R.id.radioGroupCanComment);
		this.radioShowHide = (RadioGroup)this.findViewById(R.id.radioShowHide);
		
		this.btnYes = (RadioButton)this.findViewById(R.id.btnYes);
		this.btnNo = (RadioButton)this.findViewById(R.id.btnNo);
		
		this.btnShow = (RadioButton)this.findViewById(R.id.btnShow);
		this.btnHide = (RadioButton)this.findViewById(R.id.btnHide);
		
		this.btnSave = (ImageButton)this.findViewById(R.id.btnSave);
		
		this.layoutShowHide = (LinearLayout)this.findViewById(R.id.layoutShowHide);
		
		
		
		
		
	}
	
	private void initAction()
	{
		this.btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				typeList.clear();
				
				
				int radioButtonYesId = radioGroupCanComment.getCheckedRadioButtonId();
				if(R.id.btnYes == radioButtonYesId)
				{
					canComment = 1;
				}
				else
				{
					canComment = 0;
				}
				
				int radioButtonShowId = radioShowHide.getCheckedRadioButtonId();
				if(R.id.btnShow == radioButtonShowId)
				{
					isShow = 1;
				}
				else
				{
					isShow = 0;
				}
				
				if(cBStudent.isChecked())
				{
					typeList.add(2);
				}
				if(cBParents.isChecked())
				{
					typeList.add(3);
				}
				if(cBTeacher.isChecked())
				{
					typeList.add(4);
				}
				if(cBAlumni.isChecked())
				{
					typeList.add(1);
				}
				
				
				
				SchoolCandleSetting data = new SchoolCandleSetting(canComment, isShow, typeList);
				
				Intent intent = new Intent();
				intent.putExtra("settings", data);
				
				setResult(SchoolCandleSettingActivity.RESULT_OK, intent);
				
				finish();
			}
		});
		
		
		if(btnNo.isChecked())
		{
			layoutShowHide.setVisibility(View.GONE);
		}
		else
		{
			layoutShowHide.setVisibility(View.VISIBLE);
		}
		
		radioGroupCanComment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(btnNo.isChecked())
				{
					layoutShowHide.setVisibility(View.GONE);
				}
				else
				{
					layoutShowHide.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
}
