package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.DrawerChildBase;
import com.classtune.app.schoolapp.model.DrawerChildMenu;
import com.classtune.app.schoolapp.model.DrawerChildMenuDiary;
import com.classtune.app.schoolapp.model.DrawerChildMySchool;
import com.classtune.app.schoolapp.model.DrawerChildSettings;
import com.classtune.app.schoolapp.model.DrawerGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawerExpandableListViewAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<DrawerGroup> _listDataHeader; // header titles
	// child data in format of header title, child title
	private Map<String, List<DrawerChildBase>> _listDataChild;
	private Map<String, List<Boolean>> _listDataChildStates;
	public DrawerExpandableListViewAdapter(Context context,
			List<DrawerGroup> listDataHeader,
			Map<String, List<DrawerChildBase>> listChildData,Map<String, List<Boolean>> listChildStates) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		this._listDataChildStates=listChildStates;
		//initializeStates();
		
		restorePrevStates();
		
	}

	public void restorePrevStates()
	{
		for(int i = 0;i<_listDataHeader.size();i++)
		{
			List<DrawerChildBase> childItems = _listDataChild.get(_listDataHeader.get(i).getText());
			List<Boolean> childStates=_listDataChildStates.get(_listDataHeader.get(i).getText());
			if(childStates==null)
			{
				childStates=new ArrayList<Boolean>();
				for(int j=0;j<childItems.size();j++)
				{
					childStates.add(false);
				}
			}
			_listDataChildStates.put(_listDataHeader.get(i).getText(), childStates);
		}
	}
	
	
	public void initializeStates()
	{
		for(int i = 0;i<_listDataHeader.size();i++)
		{
			List<DrawerChildBase> childItems = _listDataChild.get(_listDataHeader.get(i).getText());
			List<Boolean> childStates=_listDataChildStates.get(_listDataHeader.get(i).getText());
			if(childStates==null)
			{
				childStates=new ArrayList<Boolean>();
			}
			childStates.clear();
			for(int j=0;j<childItems.size();j++)
			{
				childStates.add(false);
			}
			_listDataChildStates.put(_listDataHeader.get(i).getText(), childStates);
		}
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(
				this._listDataHeader.get(groupPosition).getText()).get(
				childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		ImageView image = (ImageView) convertView.findViewById(R.id.image);

		if (getChild(groupPosition, childPosition) instanceof DrawerChildMenu) {
			final DrawerChildMenu child = (DrawerChildMenu) getChild(
					groupPosition, childPosition);
			
			boolean childState = _listDataChildStates.get(_listDataHeader.get(groupPosition).getText()).get(childPosition);
			 if(childState)
			 {
			         // Set your child item as selected
				 image.setImageResource(_context.getResources().getIdentifier(
							child.getImageName() + "_tap", "drawable",
							_context.getPackageName()));
			 }else
			 {
			        // Set your child item as not selected
				 image.setImageResource(_context.getResources().getIdentifier(
							child.getImageName() + "_normal", "drawable",
							_context.getPackageName()));
			 }
			
			txtListChild.setText(child.getText());
		}
		else if(getChild(groupPosition, childPosition) instanceof DrawerChildSettings) {
			final DrawerChildSettings child = (DrawerChildSettings) getChild(
					groupPosition, childPosition);
			image.setImageResource(_context.getResources().getIdentifier(
					child.getImageName() + "_normal", "drawable",
					_context.getPackageName()));
			txtListChild.setText(child.getText());
		}
		else if(getChild(groupPosition, childPosition) instanceof DrawerChildMySchool) {
			final DrawerChildMySchool child = (DrawerChildMySchool) getChild(
					groupPosition, childPosition);
			image.setImageResource(_context.getResources().getIdentifier(
					child.getImageName() + "_normal", "drawable",
					_context.getPackageName()));
			txtListChild.setText(child.getText());
		}
		else if (getChild(groupPosition, childPosition) instanceof DrawerChildMenuDiary) {
			final DrawerChildMenuDiary child = (DrawerChildMenuDiary) getChild(
					groupPosition, childPosition);
			
			boolean childState = _listDataChildStates.get(_listDataHeader.get(groupPosition).getText()).get(childPosition);
			 if(childState)
			 {
			         // Set your child item as selected
				 image.setImageResource(_context.getResources().getIdentifier(
							child.getImageName() + "_tap", "drawable",
							_context.getPackageName()));
			 }else
			 {
			        // Set your child item as not selected
				 image.setImageResource(_context.getResources().getIdentifier(
							child.getImageName() + "_normal", "drawable",
							_context.getPackageName()));
			 }
			
			txtListChild.setText(child.getText());
		}
		else
		{
			
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(
				this._listDataHeader.get(groupPosition).getText()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		DrawerGroup headerTitle = (DrawerGroup) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		CheckedTextView lblListHeader = (CheckedTextView) convertView
				.findViewById(R.id.lblListHeader);
		ImageView lblimage = (ImageView) convertView.findViewById(R.id.image);
		lblListHeader.setText(headerTitle.getText());
		if (getChildrenCount(groupPosition) == 0)
			lblListHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		else {
			lblListHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.updown, 0);
			lblListHeader.setChecked(isExpanded);
		}
		lblimage.setImageResource(_context.getResources().getIdentifier(
				headerTitle.getImageName(), "drawable",
				_context.getPackageName()));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
