package com.classtune.app.schoolapp.classtune;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.classtune.app.schoolapp.model.TeacherInfo;

import java.util.List;

/**
 * Created by BLACK HAT on 12-Nov-15.
 */
public class SpinnerTeacherInfoAdapter extends ArrayAdapter<TeacherInfo> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<TeacherInfo> values;

    public SpinnerTeacherInfoAdapter(Context context, int textViewResourceId,
                               List<TeacherInfo> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public TeacherInfo getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtName = new TextView(context);
        txtName.setTextColor(Color.BLACK);
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


        txtName.setText(values.get(position).getName());
        txtName.setGravity(Gravity.CENTER);


        return txtName;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView txtName = new TextView(context);
        txtName.setTextColor(Color.BLACK);
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        txtName.setText(values.get(position).getName());
        txtName.setGravity(Gravity.CENTER);
        txtName.setPadding(0, 10, 0, 10);

        return txtName;
    }

}
