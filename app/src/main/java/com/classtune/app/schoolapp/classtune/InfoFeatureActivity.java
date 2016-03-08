package com.classtune.app.schoolapp.classtune;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.classtune.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLACK HAT on 07-Dec-15.
 */
public class InfoFeatureActivity extends Activity{

    private List<InfoFeatureModel> listData;
    private ListView listViewData;
    private FeatureAdapter adapter;
    private ActionBar actionBar;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classtune_info_feature);
        setUpActionBar();

        listData = new ArrayList<InfoFeatureModel>();

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_attendance, getString(R.string.java_infofeatureactivity_title_attendance), getString(R.string.java_infofeatureactivity_content_attendance)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_academic_calendar, getString(R.string.java_infofeatureactivity_title_academic_calendar), getString(R.string.java_infofeatureactivity_content_academic_calendar)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_lesson_plan, getString(R.string.java_infofeatureactivity_title_lesson_plan), getString(R.string.java_infofeatureactivity_content_lesson_plan)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_teachers_routinepng, getString(R.string.java_infofeatureactivity_title_teachers_routine), getString(R.string.java_infofeatureactivity_content_teachers_routine)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_homework, getString(R.string.java_infofeatureactivity_title_homework), getString(R.string.java_infofeatureactivity_content_homework)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_fees, getString(R.string.java_infofeatureactivity_title_fees), getString(R.string.java_infofeatureactivity_content_fees)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_froms, getString(R.string.java_infofeatureactivity_title_forms), getString(R.string.java_infofeatureactivity_content_forms)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_meeting_request, getString(R.string.java_infofeatureactivity_title_meeting_request), getString(R.string.java_infofeatureactivity_content_meeting_request)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_report_card, getString(R.string.java_infofeatureactivity_title_report_card), getString(R.string.java_infofeatureactivity_content_report_card)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_event, getString(R.string.java_infofeatureactivity_title_events), getString(R.string.java_infofeatureactivity_content_events)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_transport, getString(R.string.java_infofeatureactivity_title_transport), getString(R.string.java_infofeatureactivity_content_transport)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_routine, getString(R.string.java_infofeatureactivity_title_routine), getString(R.string.java_infofeatureactivity_content_routine)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_sylabus, getString(R.string.java_infofeatureactivity_title_syllabus), getString(R.string.java_infofeatureactivity_content_syllabus)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_notice, getString(R.string.java_infofeatureactivity_title_notice), getString(R.string.java_infofeatureactivity_content_notice)));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_leave, getString(R.string.java_infofeatureactivity_title_leave), getString(R.string.java_infofeatureactivity_content_leave)));





        adapter = new FeatureAdapter();
        listViewData = (ListView)this.findViewById(R.id.listViewData);
        listViewData.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setUpActionBar(){
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        View cView = getLayoutInflater().inflate(R.layout.actionbar_view_info_feature, null);
        logo = (ImageView) cView.findViewById(R.id.logo);
        actionBar.setCustomView(cView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }


    private class FeatureAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(InfoFeatureActivity.this).inflate(R.layout.row_classtune_info_feature, parent, false);

                holder.layoutHolder = (LinearLayout)convertView.findViewById(R.id.layoutHolder);
                holder.featureIcon = (ImageView)convertView.findViewById(R.id.featureIcon);
                holder.txtTitle = (TextViewPlus)convertView.findViewById(R.id.txtTitle);
                holder.txtDetail = (TextViewPlus)convertView.findViewById(R.id.txtDetail);


                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.featureIcon.setImageResource(listData.get(position).getIconResId());
            holder.txtTitle.setText(listData.get(position).getTitle());
            holder.txtDetail.setText(listData.get(position).getDetails());

            if(position%2 == 0)
            {
                holder.layoutHolder.setBackgroundColor(Color.parseColor("#e9f0f4"));
            }
            else
            {
                holder.layoutHolder.setBackgroundColor(Color.WHITE);
            }


            return convertView;
        }


        class ViewHolder {

            LinearLayout layoutHolder;
            ImageView featureIcon;
            TextViewPlus txtTitle;
            TextViewPlus txtDetail;


        }
    }
}
