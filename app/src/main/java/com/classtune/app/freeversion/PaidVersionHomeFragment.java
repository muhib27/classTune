package com.classtune.app.freeversion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.SchoolFeedFragment;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.DrawerChildBase;
import com.classtune.app.schoolapp.model.DrawerChildMenuDiary;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PaidVersionHomeFragment extends Fragment implements
		OnClickListener {
	public static List<BaseType> batches = new ArrayList<BaseType>();
	public static boolean isBatchLoaded = false;
	public static Batch selectedBatch = null;
	public int currentPos = 0;
	private UserHelper userHelper = new UserHelper(getActivity());
	private boolean ispaid;

	@Override
	public void onDestroy() {
		super.onDestroy();
		batches.clear();
		isBatchLoaded = false;
		selectedBatch = null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setUp();
		// tabsFirst.setViewPager(pager, tabsFirst);
	}

	public static PaidVersionHomeFragment newInstance(int index) {
		PaidVersionHomeFragment f = new PaidVersionHomeFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("position", index);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		getView().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sv_main.scrollTo(currentPos, 0);
			}
		});
		//sv_main.scrollTo(currentPos, 0);
	}

	public static final String TAG = PaidVersionHomeFragment.class
			.getSimpleName();
	private View view;
	private HorizontalScrollView sv_main;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentPos = getArguments().getInt("position");
		view = inflater.inflate(R.layout.fragment_paid_main, container, false);
		/*
		 * tabsFirst=(PagerSlidingTabStrip)view.findViewById(R.id.tab);
		 * pager=(ViewPager)view.findViewById(R.id.pager);
		 */
		sv_main = (HorizontalScrollView) view.findViewById(R.id.sv_paid_scroll);
		menuScrollView = (LinearLayout) view.findViewById(R.id.sv_paid);
		container = (FrameLayout) view.findViewById(R.id.container_paid);

		return view;
	}

	/*
	 * private PagerSlidingTabStrip tabsFirst; private
	 * TabPagerAdapterPaidVersion adapter; private ViewPager pager;
	 */
	private LinearLayout menuScrollView;
	private FrameLayout container;
	private static int positionPager = 0;

	public static PaidVersionHomeFragment newInstance() {
		return new PaidVersionHomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	private void setUp() {
		/*
		 * int[] firstAdapterIconArrayStudent={ R.drawable.icon_homework,
		 * R.drawable.icon_routine, R.drawable.icon_attendance,
		 * R.drawable.icon_academic_calendar, R.drawable.icon_syllabus,
		 * R.drawable.icon_reportcard, R.drawable.icon_event,
		 * R.drawable.icon_notice, R.drawable.icon_transport, }; int[]
		 * firstAdapterIconTapArrayStudent={ R.drawable.homework_tap,
		 * R.drawable.routine_tap, R.drawable.attendance_tap,
		 * R.drawable.academic_calendar_tap, R.drawable.syllabus_tap,
		 * R.drawable.reportcard_tap, R.drawable.event_tap,
		 * R.drawable.notice_tap, R.drawable.transport_tap };
		 * 
		 * int[] firstAdapterIconArrayParents={ R.drawable.icon_homework,
		 * R.drawable.icon_fees, R.drawable.icon_notice,
		 * R.drawable.icon_attendance, R.drawable.icon_routine,
		 * R.drawable.icon_syllabus, R.drawable.icon_reportcard,
		 * R.drawable.icon_forms, R.drawable.icon_meeting,
		 * R.drawable.icon_leave, R.drawable.icon_event,
		 * R.drawable.icon_transport, }; int[] firstAdapterIconTapArrayParents={
		 * R.drawable.homework_tap, R.drawable.fees_tap, R.drawable.notice_tap,
		 * R.drawable.attendance_tap, R.drawable.routine_tap,
		 * R.drawable.syllabus_tap, R.drawable.reportcard_tap,
		 * R.drawable.forms_tap, R.drawable.meetingrequest_tap,
		 * R.drawable.leaveapplication_tap, R.drawable.event_tap,
		 * R.drawable.transport_tap, };
		 * 
		 * int[] firstAdapterIconArrayTeacher={ R.drawable.icon_attendance,
		 * R.drawable.icon_homework, R.drawable.icon_routine,
		 * R.drawable.icon_notice, R.drawable.icon_syllabus,
		 * R.drawable.icon_student_list, R.drawable.icon_leave,
		 * R.drawable.icon_meeting, R.drawable.icon_event,
		 * R.drawable.icon_transport, R.drawable.icon_reportcard,
		 * R.drawable.icon_academic_calendar, }; int[]
		 * firstAdapterIconTapArrayTeacher={ R.drawable.attendance_tap,
		 * R.drawable.homework_tap, R.drawable.routine_tap,
		 * R.drawable.notice_tap, R.drawable.syllabus_tap,
		 * R.drawable.student_list_tap, R.drawable.leaveapplication_tap,
		 * R.drawable.meetingrequest_tap, R.drawable.event_tap,
		 * R.drawable.transport_tap, R.drawable.reportcard_tap,
		 * R.drawable.academic_calendar_tap, };
		 */
		String[] myDiaryArrayText;
		String[] myDiaryArrayImages;
		String[] myDiaryArrayClazz;
		switch (userHelper.getUser().getType()) {
		case STUDENT:
			/*
			 * adapter = new
			 * TabPagerAdapterPaidVersion(getChildFragmentManager(),
			 * firstAdapterIconArrayStudent
			 * ,firstAdapterIconTapArrayStudent,userHelper.getUser().getType());
			 */
			if(userHelper.getUser().getPaidInfo().getSchoolType() == 1) {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_student);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_student);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_student);
			} else {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_student_free);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_student_free);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_student_free);
			}


			break;
		case PARENTS:
			/*
			 * adapter = new
			 * TabPagerAdapterPaidVersion(getChildFragmentManager(),
			 * firstAdapterIconArrayParents
			 * ,firstAdapterIconTapArrayParents,userHelper.getUser().getType());
			 */
			if(userHelper.getUser().getPaidInfo().getSchoolType() == 1) {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_parents);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_parents);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_parents);
			} else {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_parents_free);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_parents_free);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_parents_free);
			}

			break;
		case TEACHER:
			/*
			 * adapter = new
			 * TabPagerAdapterPaidVersion(getChildFragmentManager(),
			 * firstAdapterIconArrayTeacher
			 * ,firstAdapterIconTapArrayTeacher,userHelper.getUser().getType());
			 */
			if(userHelper.getUser().getPaidInfo().getSchoolType() == 1){
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_teacher);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_teacher);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_teacher);
			} else {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_teacher_free);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_teacher_free);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_teacher_free);
			}

			break;
		default:
			if(userHelper.getUser().getPaidInfo().getSchoolType() == 1){
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_student);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_student);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_student);

			} else {
				myDiaryArrayText = getResources().getStringArray(
						R.array.diary_text_student_free);
				myDiaryArrayImages = getResources().getStringArray(
						R.array.diary_images_student_free);
				myDiaryArrayClazz = getResources().getStringArray(
						R.array.diary_class_student_free);

			}
			break;
		}
		List<DrawerChildBase> diaryMenuStudent = new ArrayList<DrawerChildBase>();
		ispaid = userHelper.getUser().getPaidInfo().getSchoolType() == 1 ? true: false;
		for (int i = 0; i < myDiaryArrayText.length; i++) {
			DrawerChildMenuDiary child = new DrawerChildMenuDiary();
			child.setText(myDiaryArrayText[i]);
			child.setId(i + "");
			child.setImageName(myDiaryArrayImages[i]);
			child.setClazzName("com.classtune.app.schoolapp.fragments."
					+ myDiaryArrayClazz[i]);
			diaryMenuStudent.add(child);

			ImageButton iv = new ImageButton(getActivity());
			child.setPressed(false);
			iv.setTag(child);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(
					AppUtility.convertDipToPixels(getActivity(), 10f), 5,
					AppUtility.convertDipToPixels(getActivity(), 10f), 5);
			iv.setLayoutParams(params);
			iv.setBackgroundResource(getActivity().getResources()
					.getIdentifier(child.getImageName() + "_normal",
							"drawable", getActivity().getPackageName()));
			if (i == currentPos) {
				iv.setBackgroundResource(getActivity().getResources()
						.getIdentifier(child.getImageName() + "_tap",
								"drawable", getActivity().getPackageName()));
				child.setPressed(true);
				loadDiaryItem(child);
			}
			if(!ispaid && i > 2) {
				iv.getBackground().setAlpha(45);
			}else {
				iv.getBackground().setAlpha(255);
			}
			iv.setOnClickListener(this);
			menuScrollView.addView(iv);
		}

		/*
		 * pager.setOffscreenPageLimit(1); pager.setAdapter(adapter);
		 */

		// pager.setCurrentItem(positionPager);
	}

	public void showCustomDialog(String headerText, int imgResId,
								 String descriptionText) {

		PopupDialog picker = PopupDialog.newInstance(0);
		picker.setData(headerText, descriptionText, imgResId, getActivity());
		picker.show(getChildFragmentManager(), null);
	}

	@Override
	public void onClick(View v) {
		DrawerChildMenuDiary child = (DrawerChildMenuDiary) v.getTag();
		ImageButton btn = (ImageButton) v;
		if (child.isPressed())
			return;
		if(!ispaid && Integer.parseInt(child.getId()) > 2){
			showCustomDialog(child.getText(), R.drawable.premium_upgrade_icon, getActivity().getString(R.string.java_paidversionhomefragment_contact_school));
			return;
		}
		for (int i = 0; i < menuScrollView.getChildCount(); i++) {
			ImageButton ib = (ImageButton) menuScrollView.getChildAt(i);
			DrawerChildMenuDiary dcmd = (DrawerChildMenuDiary) ib.getTag();
			dcmd.setPressed(false);
			ib.setBackgroundResource(getActivity().getResources()
					.getIdentifier(dcmd.getImageName() + "_normal", "drawable",
							getActivity().getPackageName()));
			if(!ispaid && i>2) {
				ib.getBackground().setAlpha(45);
			}
		}
		child.setPressed(true);
		btn.setBackgroundResource(getActivity().getResources().getIdentifier(
				child.getImageName() + "_tap", "drawable",
				getActivity().getPackageName()));
		loadDiaryItem(child);
	}

	private void loadDiaryItem(DrawerChildMenuDiary item) {
		String className = item.getClazzName();
		currentPos = Integer.parseInt(item.getId());
		try {
			// Object xyz = Class.forName(className).newInstance();
			if (className.equals("com.classtune.app.schoolapp.fragments.home")) {
				((HomePageFreeVersion) getActivity()).loadHome();
				return;
			}
			if (className
					.equals("com.classtune.app.schoolapp.fragments.SchoolFeedFragment")) {
				if (userHelper.getJoinedSchool() != null) {
					getChildFragmentManager()
							.beginTransaction()
							.replace(
									R.id.container_paid,
									SchoolFeedFragment.newInstance(Integer
											.parseInt(userHelper
													.getJoinedSchool()
													.getSchool_id())), TAG)
							.commit();
				} else {
					getChildFragmentManager()
							.beginTransaction()
							.replace(R.id.container_paid,
									SchoolFeedFragment.newInstance(-5), TAG)
							.commit();
				}

				return;
			}
			Constructor<?> ctor = Class.forName(className).getConstructor();
			Fragment object = (Fragment) ctor.newInstance(new Object[] {});
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_paid, object, TAG).commit();

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
		} catch (java.lang.InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
