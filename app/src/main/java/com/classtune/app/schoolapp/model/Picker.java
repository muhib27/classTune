package com.classtune.app.schoolapp.model;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.PickerAdapter;

import java.util.ArrayList;
import java.util.List;


public class Picker extends DialogFragment {

	
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		//getDataFromDb();
		titleTextView.setText(titleText);
		adapter=new PickerAdapter(getActivity(), 0, items,null);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Picker.this.dismiss();
				listener.onPickerItemSelected(items.get(position));
				
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//items=new ArrayList<BaseType>();
	}

	private String titleText;
	private TextView titleTextView;
	private Button cancelBtn;
	private ListView list;
	private List<BaseType> items;
	private PickerAdapter adapter;
	private PickerType type;
	private PickerItemSelectedListener listener;
	
	public Picker() {

	}

	public static Picker newInstance(int title) {
		Picker frag = new Picker();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.picker, container, false);
		list = (ListView) view.findViewById(R.id.list);
		titleTextView = (TextView) view.findViewById(R.id.dialog_tv_title);
		cancelBtn = (Button) view.findViewById(R.id.dialog_btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
			}
		});
		return view;
	}

	
	public void setData(PickerType type,List<BaseType> items, PickerItemSelectedListener lis, String titleText)
	{
		this.listener=lis;
		this.type=type;
		this.items = new ArrayList<BaseType>();
		this.items.addAll(items);
		this.titleText = titleText;
	}
	
	public interface PickerItemSelectedListener
	{
		public void onPickerItemSelected(BaseType item);
	}
	
	
}
