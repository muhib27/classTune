package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.classtune.app.freeversion.DefaulterRegistrationActivity;
import com.classtune.app.schoolapp.model.DefaulterModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RR on 14-Nov-17.
 */

public class DefaulterRegistrationAdapter extends BaseAdapter {
    private Context context;
    private List<DefaulterModel> studentList;
    private boolean isClickable = false;
    private boolean isUpdate = false;
    private boolean isAllPresent = false;
    private Set<String> listStudentStatusNew;
    private List<String> mList;
    private List<String> dTempList;

    public DefaulterRegistrationAdapter(Context context, List<DefaulterModel> studentList) {
        this.context = context;
        this.studentList = studentList;

        this.listStudentStatusNew = new HashSet<>();
        this.mList = new ArrayList<>();
        this.dTempList = new ArrayList<>();
    }

    public DefaulterRegistrationAdapter() {
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
            rowView = inflater.inflate(R.layout.defaulter_row_item, null);

            holder.studentName = (TextView) rowView.findViewById(R.id.studentName);
            holder.studentRoll = (TextView) rowView.findViewById(R.id.studentRoll);
            //holder.studentStatus =  (TextView) rowView.findViewById(R.id.studentStatus);
            holder.defaulterSet = (CheckBox) rowView.findViewById(R.id.defaulterSet);
            holder.defaulterLayout = (LinearLayout) rowView.findViewById(R.id.defaulterLayout);
            holder.checkboxLayout = (RelativeLayout) rowView.findViewById(R.id.checkboxLayout);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        holder.defaulterSet.setTag(index);
        if (studentList.get(index).getStudent_name().isEmpty() || studentList.get(index).getStudent_name() == null)
            holder.studentName.setText("-");
        else
            holder.studentName.setText(studentList.get(index).getStudent_name());
        if (studentList.get(index).getClass_roll_no() == null || studentList.get(index).getClass_roll_no().isEmpty())
            holder.studentRoll.setText("-");
        else
            holder.studentRoll.setText(studentList.get(index).getClass_roll_no());
        if (studentList.get(index).getDefaulter().equals("1") || studentList.get(index).isSelected()) {
//            if(dTempList.contains(studentList.get(index).getStudent_id()))
//            dTempList.add(studentList.get(index).getStudent_id());
            holder.defaulterSet.setChecked(true);
            studentList.get(index).setSelected(true);
        } else {
            studentList.get(index).setSelected(false);
            holder.defaulterSet.setChecked(false);
        }

//        if (!DefaulterRegistrationActivity.checkBoxRegistered.isChecked())
//            holder.defaulterSet.setEnabled(false);
//        else
//            holder.defaulterSet.setEnabled(true);
//        holder.defaulterSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!DefaulterRegistrationActivity.checkBoxRegistered.isChecked()) {
//                    holder.defaulterSet.setEnabled(false);
//
////                    if(holder.defaulterSet.isChecked())
////                        holder.defaulterSet.setChecked(true);
////                    else
////                        holder.defaulterSet.setChecked(false);
////
////                    holder.defaulterSet.setClickable(true);
//                } else {
//                    holder.defaulterSet.setEnabled(true);
////                    if(holder.defaulterSet.isChecked())
////                        holder.defaulterSet.setChecked(false);
////                    else
////                        holder.defaulterSet.setChecked(true);
//                }
//            }
//        });

        if(!isClickable) {
//            holder.checkboxLayout.setOnClickListener(null);
            holder.checkboxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    holder.defaulterLayout.setEnabled(false);
//                    holder.defaulterSet.setEnabled(false);

//                    ((LinearLayout)view).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    Toast.makeText(context, R.string.message_defaulter_register, Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
        else {
            holder.defaulterSet.setClickable(true);
            holder.defaulterSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    studentList.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    if(buttonView.isChecked())
                        dTempList.add(studentList.get(getPosition).getStudent_id());
                    else
                        if(dTempList.contains(studentList.get(getPosition).getStudent_id()))
                            dTempList.remove(studentList.get(getPosition).getStudent_id());
//                    if(!isChecked) {
//                        holder.defaulterSet.setChecked(false);
//                        studentList.get(index).setDefaulter("0");
////                        if(dTempList.contains(studentList.get(index).getStudent_id()))
////                            dTempList.remove(studentList.get(index).getStudent_id());
//
//                    }
//                    else {
//                        holder.defaulterSet.setChecked(true);
//                        studentList.get(index).setDefaulter("1");
//                        //dTempList.add(studentList.get(index).getStudent_id());
//                    }
                }
            });
            //Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
        }


//        }else{
//            holder.checkboxLayout.setActivated(true);
//            holder.defaulterSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView,
//                                             boolean isChecked) {
//                    if(isClickable){
//                        holder.defaulterSet.setEnabled(false);
//                        // Code to display your message.
//                    }
//                    else {
//                        holder.defaulterSet.setEnabled(true);
//                    }
//                }
//            });

//            holder.defaulterSet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
////                    validateCheckBoxSelected(holder.lisCheckBox, (LinearLayout)view, R.color.leave);
////                    student.setClickedPresent(true);
////
////                    holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//
////                    if(listStudentStatusNew.contains(student.getStudentId()+"A")){
////                        listStudentStatusNew.remove(student.getStudentId()+"A");
////                    }
////                    if(listStudentStatusNew.contains(student.getStudentId()+"L")){
////                        listStudentStatusNew.remove(student.getStudentId()+"L");
////                    }
//
//
//                }
//            });

//        }
        holder.defaulterSet.setChecked(studentList.get(index).isSelected());
        return rowView;
    }


    private static class ViewHolder {
        private TextView studentName;
        private TextView studentRoll;
        private TextView studentStatus;
        private CheckBox defaulterSet;
        private LinearLayout defaulterLayout;
        private RelativeLayout checkboxLayout;
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
