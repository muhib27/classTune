package com.classtune.app.freeversion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.YearlyAttendanceReportFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AnyFragmentLoadActivity extends ChildContainerActivity {

	public static String PACKAGE_NAME = "com.classtune.app.schoolapp.fragments";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		homeBtn.setVisibility(View.VISIBLE);
		logo.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anyfragmentload);

		if (getIntent().getExtras() != null) {
			String batchId = getIntent().getExtras().getString("batch_id");
			if (batchId != null) {
				getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.pager_frame,
								YearlyAttendanceReportFragment.newInstance(
										batchId, getIntent().getExtras()
												.getString("student_id")), "")
						.commit();
			}

            else {

                if(getIntent().getExtras().containsKey("class_name_school_candle"))
                {
                    String value = getIntent().getExtras().getString("class_name_school_candle");
                    loadFragmentSchoolCandle(value);
                }
                else
                {
                    String value = getIntent().getExtras().getString("class_name");
                    loadFragment(value);
                }
			}

		}

        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getExtras().containsKey("total_unread_extras"))
            {
                String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                GcmIntentService.initApiCall(rid, rtype);
            }
        }

	}

	private void loadFragment(String name) {
        String className = PACKAGE_NAME + "." + name;
        try {
            // Object xyz = Class.forName(className).newInstance();

            Constructor<?> ctor = Class.forName(className).getConstructor();
            Fragment object = (Fragment) ctor.newInstance(new Object[] {});

            object.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pager_frame, object, "").commit();

        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadFragmentSchoolCandle(String name) {
        String className = PACKAGE_NAME + "." + name;
        try {
            // Object xyz = Class.forName(className).newInstance();

            Constructor<?> ctor = Class.forName(className).getConstructor();
            Fragment object = (Fragment) ctor.newInstance(new Object[] {});

            Bundle bundle_new = new Bundle();
            bundle_new.putInt("key_school_id", Integer.parseInt(userHelper.getJoinedSchool().getSchool_id()));
            object.setArguments(bundle_new);



            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pager_frame, object, "").commit();

        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




}
