package com.classtune.app.schoolapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.classtune.app.schoolapp.utils.AppUtility;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private DatePickerOnSetDateListener listener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		listener.onDateSelected(c
				.get(Calendar.MONTH),AppUtility.getMonthFullName(c
				.get(Calendar.MONTH)), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR),
				AppUtility.getFormatedDateString(AppUtility.DATE_FORMAT_SERVER, c),
				AppUtility.getFormatedDateString(AppUtility.DATE_FORMAT_APP, c),
				c.getTime());
	}
	
	public void setCallbacks(DatePickerOnSetDateListener callback)
	{
		this.listener=callback;
	}
	
	public interface DatePickerOnSetDateListener
	{
		public void onDateSelected(int month, String monthName, int day, int year, String dateFormatServer, String dateFormatApp, Date date);
	}
}
