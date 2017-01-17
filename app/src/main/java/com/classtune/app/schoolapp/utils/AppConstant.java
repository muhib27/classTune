/**
 * 
 */
package com.classtune.app.schoolapp.utils;

import com.classtune.app.R;
import com.google.android.gms.common.Scopes;

public class AppConstant {
	public static final String[] SCOPES = { Scopes.PLUS_LOGIN, Scopes.PROFILE };


	//public static final String[] SCOPES = { Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE };


	public static final String[] VISIBLE_ACTIVITIES = {
			"http://schemas.google.com/AddActivity",
			"http://schemas.google.com/ReviewActivity",
			"http://schemas.google.com/BuyActivity" };

	public final static int RESPONSE_CODE_SUCCESS = 200;
	public final static int RESPONSE_CODE_SESSION_EXPIRED = 406;

	public final static String SHARED_PREF_NAME = "SchoolApp";

	public final static String NORMAL = "NORMAL";
	public final static String SELECTED = "SELECTED";

	// ######################################### UI Strings
	// #########################################
	public final static String KEY_HOMEWORK = "HOMEWORK";
	public final static String KEY_PROJECT = "PROJECT";

	// ######################################### UI Strings
	// #########################################
	public final static String STR_OK = "Ok";
	public final static String STR_INTERNET_PROBLEM_TITLE = "Internet Problem";
	public final static String STR_INTERNET_PROBLEM_MESSAGE = "No internet connection. Please connect to a network first.";
	public final static String STR_ERROR_TITLE = "Error";
	public final static String STR_EMPTY_USERNAME = "Username field is empty!";
	public final static String STR_EMPTY_PASSWORD = "Password field is empty!";
	public final static String STR_MATCHING_FAILED = "Username and password did not match!";
	public final static String STR_LOADER_TITLE = "Loading";
	public final static String STR_LOADER_MSG = "Please wait";

	// ######################################### Homework Is Done
	// #########################################
	public final static String ACCEPTED = "ACCEPTED";
	public final static String NOT_DONE = "NOT DONE";
	public final static String SUBMITTED = "SUBMITTED";

	// ######################################### Events
	// #########################################
	public static final String TAB_EVENTS = "tab_event_identifier";
	public static final String TAB_CLUB_NEWS = "tab_club_news_identifier";
	public static final String TAB_ARCHIEVE = "tab_archieve_identifier";
	// ######################################### Leave
			// #########################################
	public static final String TAB_LEAVE_APPLICATION = "tab_leave_application_identifier";
	public static final String TAB_STUDENT_LEAVE = "tab_student_leave_identifier";
	public static final String TAB_MY_LEAVE = "tab_my_leave_identifier";
	// ######################################### HomeWork
		// #########################################
	public static final String TAB_DRAFT = "tab_draft_identifier";
	public static final String TAB_HOMEWORK_ADD = "tab_homework_add_identifier";
	public static final String TAB_HOMEWORK_DRAFT = "tab_homework_draft_identifier";
	public static final String TAB_HOMEWORK_FEED = "tab_homework_feed_identifier";
	public static final String TAB_ATTENDANCE_TEACHER = "tab_attendance_teacher_identifier";
	// ######################################### ClassWork
	// #########################################
	public static final String TAB_CLASSWORK_ADD = "tab_homework_add_identifier";
	public static final String TAB_CLASSWORK_DRAFT = "tab_homework_draft_identifier";
	public static final String TAB_CLASSWORK_FEED = "tab_homework_feed_identifier";
	// ######################################### Report Card
	// #########################################
	public static final String TAB_CLASSTEST = "tab_classtest_identifier";
	public static final String TAB_TERM_REPORT = "tab_term_report_identifier";
	public static final String TAB_PROJECT = "tab_result_project_identifier";
    public static final String TAB_PROGRESS_GRAPH = "tab_progress_graph_identifier";

	// ######################################### REMINDER
	// #########################################
	public final static String REMINDER_KEY = "reminder_all";
	public final static String REMINDER_TITLE = "reminder_title";
	public final static String REMINDER_TEXT = "reminder_txt";
	public final static String REMINDER_TIME = "reminder_time";

	// ######################################### ROUTINE
	// #########################################
	public final static String TAB_CLASS_ROUTINE = "class_routine_identifier";
	public final static String TAB_WEEKLY_ROUTINE = "weekly_routine_identifier";
	public final static String TAB_EXAM_ROUTINE = "exam_routine_identifier";

	// ######################################### Notification Strings
	// #########################################

	public final static String NOTIFICATION_HOMEWORK = SchoolApp.getInstance().getString(R.string.java_appconstant_new_homework);
	public final static int PAGE_SIZE = 10;

	// ######################################### Attendence
	// #########################################
	public static final String TAB_MONTHLY_ATTENDANCE = "tab_monthly_attendance_identifier";
	public static final String TAB_YEARLY_ATTENDANCE = "tab_yearly_attendance_identifier";
	public static final String TAB_ROLLCALL_TEACHER = "tab_roll_call_identifier";
	public static final String TAB_CLASS_REPORT_TEACHER = "tab_class_report_identifier";
	public static final String TAB_STUDENT_REPORT_TEACHER = "tab_student_report_teacher_identifier";

	// ######################################### TRANSPORT
	// #########################################
	public static final String LAST_UPDATE = SchoolApp.getInstance().getString(R.string.java_appconstant_last_update);

	// ######################################### Accademic Calendar
	// #########################################
	public static final String TAB_EXAM_CALENDAR = "tab_exam_calendar_identifier";
	public static final String TAB_EVENTS_CALENDAR = "tab_events_calendar_identifier";
	public static final String TAB_HOLIDAYS_CALENDAR = "tab_holidays_calendar_identifier";
	public static final String TAB_OTHERS_CALENDAR = "tab_other_calendar_identifier";

	public static final String TAB_EMPTY = "tab_empty_identifier";

	// ######################################### FreeVersionHomeFragment
	// #########################################
	public static final String ITEM_ID = "item_id";
	public static final String ITEM_CAT_ID = "item_cat_id";
	
	
	//#########################################AllSchoolListFragment
	public static final String SCHOOL_DATA = "school_data";
	public static final String SCHOOL_PAGE_DATA = "school_page_data";
	
	
	public static final String GOING_GOODREAD = "going_goodread";
	public static final String TAB_FEES_DUE = "tab_fees_due_identifier";
	public static final String TAB_FEES_HISTORY = "tab_fees_history_identifier";
	
	
	
	//######################################### SchoolDetailsActivity
	public static final String SCHOOL_ID = "school_id";
		
	//######################################### SchoolAllActivities
	public static final String ACTIVITY_SINGLE = "activity_single";
	
	
	
	//######################################### SchoolSearchFragment
	public static final String LIST_SCHOOL = "list_school";
	public static final String ARRAY_SCHOOL = "array_school";
	
	
	//######################################### VideoFragment
	public static final String VIDEO_ROOT_OBJECT = "video_root_object";
	public static final String VIDEO_URL = "video_url";
	public static final String VIDEO_POST_ID = "video_post_id";
	
	
	//######################################## SingleItemShowFragmentActivity
	public static final String POST_FRAG_OBJECT = "post_frag_object";
	public static final String POST_FRAG_OBJECT2 = "post_frag_object2";
	public static final String POST_FRAG_OBJECT_TOTAL = "post_frag_object_total";
	
	//GCM
		public static final String GCM_REGISTRATION_SERVER="gcm_registration_server";
		public static final String DEVICE_REGISTRATION_URL="add_device";
	    public static final String NOTIFICATION_TYPE_TAG = "notification_type";
	
	public static final long TOTAL_ASSESSMENT_TIME = 90000;
	
	
	//######################################## HomeworkFragment
	public static final String ID_SINGLE_HOMEWORK = "id_single_homework";


	//######################################## SingleEventActivity
	public static final String ID_SINGLE_EVENT = "id_single_event";
	
	
	//######################################## SingleCalendarEvent
	public static final String ID_SINGLE_CALENDAR_EVENT = "id_single_calendar_event";
	
	
	//######################################## SingleSyllabus
	public static final String ID_SINGLE_SYLLABUS = "id_single_syllabus";
	
	
	//######################################## SyllabusFragment
	public static final String ID_TERM = "id_term";
	public static final String ID_BATCH = "id_batch";
	
	
	//######################################## TeacherHomeworkDoneActivity
	public static final String ID_TEACHER_HOMEWORK_DONE = "id_teacher_homework_done";
	
	
	//######################################## NoticeFragmentNew
	public static final String ID_SINGLE_NOTICE = "id_single_notice";
	
	
	//######################################## MeetingFragment
	public static final String ID_SINGLE_MEETING_REQUEST = "id_single_meeting_request";


    //######################################## LessonPlanView
    public static final String ID_SINGLE_LESSON_PLAN = "id_single_lesson_plan";

    //######################################## LessonPlanStudentParent
    public static final String DATA_LESSON_PLAN_SUBJECT = "data_lesson_plan_subject";


	//######################################## CALLS TUNE CONSTANTS ############################
	public static final String USER_TYPE_CLASSTUNE = "user_type_classtune";
	public static final String SCHOOL_ID_CLASSTUNE = "school_id_classtune";
	public static final String STUDENT_FIRST_NAME_CLASSTUNE = "student_first_name_classtune";
	public static final String STUDENT_LAST_NAME_CLASSTUNE = "student_last_name_classtune";
	public static final String STUDENT_EMAIL_CLASSTUNE = "student_email_name_classtune";
	public static final String STUDENT_PASSWORD_CLASSTUNE = "student_password_classtune";
	public static final String STUDENT_SCHOOL_CODE_CLASSTUNE = "student_school_code_classtune";


	public static final String CLASSTUNE_FONT_NAME = "breeserif_regular.otf";


	public static final String CLASSTUNE_MESSAGE_INVALID_EMAIL = SchoolApp.getInstance().getString(R.string.java_classtune_registration_valid_email);
	public static final String CLASSTUNE_MESSAGE_FIRST_NAME = SchoolApp.getInstance().getString(R.string.java_classtune_registration_first_name);
	public static final String CLASSTUNE_MESSAGE_LAST_NAME = SchoolApp.getInstance().getString(R.string.java_classtune_registration_last_name);
	public static final String CLASSTUNE_MESSAGE_EMAIL = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_email_address);
	public static final String CLASSTUNE_MESSAGE_PASSWORD = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_password);
	public static final String CLASSTUNE_MESSAGE_PASSWORD_CHAR_LENGTH = SchoolApp.getInstance().getString(R.string.java_classtune_registration_password_length);
	public static final String CLASSTUNE_MESSAGE_PASSWORD_RETYPE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_retype_password);
	public static final String CLASSTUNE_MESSAGE_PASSWORD_MATCH = SchoolApp.getInstance().getString(R.string.java_classtune_registration_password_didnt_match);
	public static final String CLASSTUNE_MESSAGE_SCHOOL_CODE_EMPTY = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_school_code);
	public static final String CLASSTUNE_MESSAGE_SCHOOL_CODE_VALID = SchoolApp.getInstance().getString(R.string.java_classtune_registration_schoolcode_not_valid);

	public static final String CLASSTUNE_MESSAGE_SOMETHING_WENT_WRONG = SchoolApp.getInstance().getString(R.string.java_classtune_registration_something_went_wrong);

	public static final String CLASSTUNE_MESSAGE_CLASS_YET = SchoolApp.getInstance().getString(R.string.java_classtune_registration_school_didnt_create_class_yet);
	public static final String CLASSTUNE_MESSAGE_ADMISSION_NUMBER = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_admission_no);
	public static final String CLASSTUNE_MESSAGE_BATCH_SELECT = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_batch);
	public static final String CLASSTUNE_MESSAGE_DOB_SELECT = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_dob);
	public static final String CLASSTUNE_MESSAGE_GENDER_SELECT = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_gender);
	public static final String CLASSTUNE_MESSAGE_CONTACT_NUMBER = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_contact_no);
	public static final String CLASSTUNE_MESSAGE_ADMISSION_NUMBER_EXISTS = SchoolApp.getInstance().getString(R.string.java_classtune_registration_already_exists); //need improved text

	public static final String CLASSTUNE_MESSAGE_SELECT_SOURCE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_source);

	public static final String CLASSTUNE_MESSAGE_ADD_CHILD_MORE_THAN = SchoolApp.getInstance().getString(R.string.java_classtune_registration_add_more_child);
	public static final String CLASSTUNE_MESSAGE_USER_ID = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_user_id);
	public static final String CLASSTUNE_MESSAGE_ADD_ONE_CHILD = SchoolApp.getInstance().getString(R.string.java_classtune_registration_add_one_child);
	public static final String CLASSTUNE_MESSAGE_USER_NAME = SchoolApp.getInstance().getString(R.string.java_classtune_registration_already_exists); //need improved text
	public static final String CLASSTUNE_MESSAGE_INVALID_STUDENT_ID = SchoolApp.getInstance().getString(R.string.java_classtune_registration_student_id_not_valid);
	public static final String CLASSTUNE_MESSAGE_DUPLICATE_STUDENT_ID = SchoolApp.getInstance().getString(R.string.java_classtune_registration_duplicate_student_id);
	public static final String CLASSTUNE_MESSAGE_DIALOG_TITLE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_enter_children_id);
	public static final String CLASSTUNE_MESSAGE_DIALOG_CHILD_ID = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_child_id);
	public static final String CLASSTUNE_MESSAGE_DIALOG_RELATION = SchoolApp.getInstance().getString(R.string.java_classtune_registration_field_cannot_be_empty); //need improved text
	public static final String CLASSTUNE_MESSAGE_STUDENT_NOT_EXISTS = SchoolApp.getInstance().getString(R.string.java_classtune_registration_id_does_not_exist); //need improved text
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_NECESSARY_INFO = SchoolApp.getInstance().getString(R.string.java_classtune_registration_employee_necessaty_information_yet);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_POSITION = SchoolApp.getInstance().getString(R.string.java_classtune_registration_employee_position_yet);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_NUMBER = SchoolApp.getInstance().getString(R.string.java_classtune_registration_employee_no);
	public static final String CLASSTUNE_MESSAGE_JOB_TITLE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_provide_job_title);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_CATEGORY = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_category);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_DEPARTMENT = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_department);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_POSITION_TYPE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_position);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_JOINING_DATE = SchoolApp.getInstance().getString(R.string.java_classtune_registration_select_joining_date);
	public static final String CLASSTUNE_MESSAGE_EMPLOYEE_EXISTS = SchoolApp.getInstance().getString(R.string.java_classtune_registration_number_already_exists);

	public static final long BANNER_POPUP_SHOW_TIME = 12000;
	public static final long BANNER_POPUP_DISMISS_TIME = 5000;


	//################### key for getting batch from global broadcast
	public static final String KEY_BATCH_FROM_TEACHERATTENDANCE_TAB = "key_batch_from_teacherattendance_tab";


}
