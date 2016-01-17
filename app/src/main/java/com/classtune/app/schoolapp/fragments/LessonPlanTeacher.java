package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomTabButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

import java.util.Calendar;

/**
 * Created by BLACK HAT on 24-Mar-15.
 */
public class LessonPlanTeacher extends Fragment {


    private View view;
    private UIHelper uiHelper;
    private UserHelper userHelper;

    private CustomTabButton btnViewLessonPlan;
    private CustomTabButton btnAddLessonPlan;

    private CustomTabButton currentButton;


    private TextView txtDate;

    private boolean isViewButtonClicked = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lessonplan_teacher, container, false);

        initView(view);


        return view;

    }



    private void initView(View view)
    {
        btnViewLessonPlan = (CustomTabButton)view.findViewById(R.id.btnViewLessonPlan);
        btnAddLessonPlan = (CustomTabButton)view.findViewById(R.id.btnAddLessonPlan);

        currentButton = btnViewLessonPlan;
        btnViewLessonPlan.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.lesson_plan_view_tap);


        loadFragment(new LessonPlanView());



        btnViewLessonPlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
                //horizontalScrollView.smoothScrollTo(scrollX, 0);

                currentButton = btnViewLessonPlan;
                btnViewLessonPlan.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.lesson_plan_view_tap);
                btnAddLessonPlan.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.lesson_plan_add_normal);


                if(isViewButtonClicked == true)
                {
                    loadFragment(new LessonPlanView());
                    isViewButtonClicked = false;
                }





            }
        });


        btnAddLessonPlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
                //horizontalScrollView.smoothScrollTo(scrollX, 0);

                currentButton = btnAddLessonPlan;

                btnViewLessonPlan.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.lesson_plan_view_normal);
                btnAddLessonPlan.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.lesson_plan_add_tap);


                if(isViewButtonClicked == false)
                {
                    loadFragment(new LessonPlanAdd());
                    isViewButtonClicked = true;
                }






            }
        });


        txtDate = (TextView)view.findViewById(R.id.txtDate);
        txtDate.setText(getCurrentDate());


    }


    private void loadFragment(Fragment newFragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutContainer, newFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    private String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        return AppUtility.getFormatedDateString(AppUtility.DATE_FORMAT_APP, c);
    }




}
