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

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_attendance, "Attendance", "Taking class attendance is now easier than ever! " +
                "Teachers can take attendance in few clicks, and parents get real time notification. Also, " +
                "it helps school to keep records."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_academic_calendar, "Academic Calendar", "School can share the academic calendar in " +
                "the beginning of the year with the authority " +
                "of modification when required. " +
                "It helps parents to plan family holidays."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_lesson_plan, "Lesson Plan", "Teachers can upload weekly, " +
                "monthly and yearly lesson plans. It allows students and " +
                "parents to plan ahead of time."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_teachers_routinepng, "Teacher’s Routine", "Now teachers can see their day " +
                "to day schedule and make plans accordingly. Surely " +
                "accelerate better outputs."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_homework, "Homework", "This feature is convenient for everyone! " +
                "Teachers can assign homework to whole class " +
                "or individual student. Students now can find homework easily. Set reminder and mark " +
                "done once completed and parents can keep track of homework assigned to child."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_fees, "Fees", "Parents can pay fees online using credit " +
                "card or mobile money. Great tool for monitoring " +
                "payment and keeping school’s accounts."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_froms, "Forms", "All school forms are now available " +
                "from mobile and web. Fill it up and submit online. " +
                "Zero crowd in office room thus higher productivity."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_meeting_request, "Meeting Request", "School admin can organize parents " +
                "meets and notify parents quickly. Parents can " +
                "request to meet teachers. Even teachers can request an individual parent for meeting."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_report_card, "Report Card", "Teacher can upload the report card of class tests, " +
                "exams, projects and students can see it " +
                "online. Students can place it for parents view and after reviewing parents can send " +
                "acknowledgement to school."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_event, "Events", "Events page keeps everyone updated " +
                "about date, timing and venue of any event coming " +
                "up in the school. It also sends necessary reminders."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_transport, "Transport", "Pick up and drop off time of your " +
                "child’s school transport now can be checked online. " +
                "Any changes in the transport schedule can reach parents in minutes."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_routine, "Routine", "Teachers and Management can " +
                "modify routine when necessary and send instant " +
                "notification."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_sylabus, "Syllabus", "Schools are enabled to upload yearly syllabus. " +
                "Students, Parents and Teachers can refer " +
                "to syllabus on the go."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_notice, "Notice", "Any notice without any word limitation can be sent. " +
                "Its real time and interactive. Instant " +
                "notification to parents."));

        listData.add(new InfoFeatureModel(R.drawable.classtune_feature_leave, "Leave", "Applying for leave is now hassle free! " +
                "Parents can apply for Student’s leave and get " +
                "notification once granted. Teachers can approve Students leave and can apply for their " +
                "own leaves also."));





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
