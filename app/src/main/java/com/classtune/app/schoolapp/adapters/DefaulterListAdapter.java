package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.DefaulterModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RR on 14-Nov-17.
 */

public class DefaulterListAdapter extends BaseAdapter {
    private Context context;
    private List<DefaulterModel> studentList;
    private boolean isClickable = false;
    private boolean isUpdate = false;
    private boolean isAllPresent = false;
    private Set<String> listStudentStatusNew;
    private List<String> mList;
    private List<String> dTempList;

    public DefaulterListAdapter(Context context, List<DefaulterModel> studentList) {
        this.context = context;
        this.studentList = studentList;

        this.listStudentStatusNew = new HashSet<>();
        this.mList = new ArrayList<>();
        this.dTempList = new ArrayList<>();
    }

    public DefaulterListAdapter() {
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public boolean getClickable() {
        return this.isClickable;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isAllPresent() {
        return isAllPresent;
    }

    public void setAllPresent(boolean allPresent) {
        isAllPresent = allPresent;
    }
    ViewHolder holder;

    @Override
    public View getView(final int index, final View convertView, ViewGroup viewGroup) {

        View rowView = convertView;
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        dTempList.clear();
        //final ViewHolder holder;
        if (rowView == null) {

            holder = new ViewHolder();
            rowView = inflater.inflate(R.layout.defaulter_list_row_item, null);

            holder.studentName = (TextView) rowView.findViewById(R.id.studentName);
            holder.studentRoll = (TextView) rowView.findViewById(R.id.studentRoll);


            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        if (studentList.get(index).getStudent_name().isEmpty() || studentList.get(index).getStudent_name() == null)
            holder.studentName.setText("-");
        else
            holder.studentName.setText(studentList.get(index).getStudent_name());
        if (studentList.get(index).getClass_roll_no() == null || studentList.get(index).getClass_roll_no().isEmpty())
            holder.studentRoll.setText("-");
        else
            holder.studentRoll.setText(studentList.get(index).getClass_roll_no());

        return rowView;
    }


    private static class ViewHolder {
        private TextView studentName;
        private TextView studentRoll;
    }

    public List<String> getListStudentDataId() {
        //int d = dTempList.size();

        List<String> mSet = new ArrayList<>();
        for(int i = 0; i<studentList.size(); i++)
        {
            if(studentList.get(i).isSelected())
                mSet.add(studentList.get(i).getStudent_id());
        }
//        for(String str : listStudentStatusNew){
//            str = str.substring(0, str.length()-1);
//            mSet.add(str);
//        }
        return mSet;
    }
}
