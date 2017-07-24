/**
 * 
 */
package com.classtune.app.schoolapp.utils;

public class URLHelper {
	public final static String URL_BASE = "http://apimaster.classtune.com/api/";

	public final static String URL_LOGIN = "user/auth";
	
	public final static String URL_GCM_REGISTER = "freeuser/addgcm";

	public final static String URL_NOTICE = "notice";
	
	public final static String URL_GET_TEACHER_BATCH = "calender/getbatch";
	
	public final static String URL_POST_ADD_ATTENDANCE = "calender/AddAttendence";
	
	public final static String URL_GET_STUDENT_INFO = "calender/getstudentinfo";
	
	public final static String URL_GET_STUDENTS_ATTENDANCE = "calender/getbatchstudentattendence";

    public final static String URL_GET_GRAPH_SUBJECTS = "report/getsubject";

    public final static String URL_GET_REPORT_PROGRESS = "report/progress";

    public final static String URL_GET_REPORT_PROGRESS_ALL = "report/progressall";
	
	public final static String URL_GET_STUDENT_LEAVE_LIST = "event/studentleaves";
	
	public final static String URL_GET_TEACHER_LEAVE_LIST = "event/teacherleaves";
	
	public final static String URL_GET_PARENT_LEAVE_LIST = "event/studentleavesparent";
	
	public final static String URL_GET_LEAVE_TYPE = "event/leavetype";
	
	public final static String URL_GET_ACADEMIC_CALENDAR_EVENTS = "calender/academic";
	
	public final static String URL_GET_BATCH_CLASS_REPORT = "calender/studentAttendenceReport";

	public final static String URL_HOMEWORK = "homework";
	public final static String URL_CLASSWORK = "classwork";

	public final static String URL_PROJECT = "homework/getproject";

	public final static String URL_NOTICE_EVENT = "event";

	public final static String URL_GET_ATTENDENCE_EVENTS = "calender/getattendence";

	public final static String URL_HOMEWORK_DONE = "homework/done";

	public final static String URL_NOTICE_ACKNOWLEDGE = "notice/acknowledge";

	public final static String URL_SYLLABUS_TERM = "syllabus/terms";

	public final static String URL_SYLLABUS = "syllabus";

	public final static String URL_GET_EVENT_LIST = "event";
	
	public final static String URL_TEACHER_GET_SUBJECT = "homework/getsubject";
	public final static String URL_TEACHER_CLASSWORK_GET_SUBJECT = "classwork/getsubject";
	
	public final static String URL_TEACHER_ADD_HOMEWORK = "homework/addhomework";
	public final static String URL_TEACHER_ADD_CLASSWORK = "classwork/addclasswork";
	
	public final static String URL_TEACHER_HOMEWORK_FEED = "homework/teacherhomework";
	public final static String URL_TEACHER_CLASSWORK_FEED = "classwork/teacherclasswork";

	public final static String URL_TEACHER_LEAVE = "event/addleaveteacher";
	
	public final static String URL_PARENT_LEAVE = "event/addleavestudent";
	
	public final static String URL_REPORT_CARD = "report/classtestreport";//"report/getfullreport";

	public final static String URL_POST_ACK = "event/acknowledge";

	public final static String URL_CLUB_LIST = "club";

	public final static String URL_NOTIFICATION = "event/getuserreminder";
	
	public final static String URL_POST_ACK_EVENT = "event/acknowledge";

	public final static String URL_POST_ACK_CLUB = "club/acknowledge";

	public final static String URL_ROUTINE = "routine";

	public final static String URL_ROUTINE_EXAM = "routine/exam";

	public final static String URL_TRANSPORT = "transport";

	public final static String STRING = "calender/academic";
	
	public final static String URL_GET_EXAM_ROUTINE_STUDENT_PARENT = "Routine/AllExam";
	
	public final static String URL_GET_RESULT_REPORT = "Report/allexam";
	public final static String URL_GET_RESULT_GROUP_REPORT = "report/getConnectExam";
	public final static String URL_GET_RESULT_GROUP_AUTH_DOWNLOAD = "report/addauthforexam";

	public final static String URL_GET_NEXT_CLASS_DATA = "routine/nextclass";
	
	public final static String URL_GET_NEXT_CLASS_STUDENT ="Routine/NextClassStudent";
	
	public final static String URL_GET_WEEK_DAY_CLASSES = "routine/Teacher";
	
	public final static String URL_GET_WEEK_DAY_STUDENT_CLASSES = "Routine/getdateroutine";
	
	public final static String FREE_USER_CREATE = "freeuser/create";

	public final static String URL_FORGET_PASSWORD = "user/forgotpassword";

	public final static String URL_FREE_VERSION_HOME = "freeuser";

	public final static String URL_FREE_VERSION_SINGLENEWS = "freeuser/getsinglenews";
	
	public final static String URL_FREE_VERSION_RELATED_NEWS = "freeuser/relatednews";

	public final static String URL_FREE_VERSION_CATEGORY = "freeuser/getcategorypost";
	
	public final static String URL_FREE_VERSION_COMMENT = "freeuser/getcomments";
	
	public final static String URL_FREE_VERSION_COMMENT_POST = "freeuser/AddComments";
	
	public final static String URL_PAID_VERSION_SCHOOL_FEED = "dashboard/gethome";//"freeuser/getSchoolTeacherBylinePost";

	public final static String URL_PAID_VERSION_CLASSTUNE_FEED = "dashboard/getuserFeed";

	public final static String URL_FREE_VERSION_SCHOOL_FEED ="freeuser/getSchoolTeacherBylinePost";
	
	public final static String URL_FREE_VERSION_SEARCH = "freeuser/search";

	public final static String URL_FREE_VERSION_GET_USER = "freeuser/getuserinfo";

	public final static String URL_FREE_VERSION_READLATER = "freeuser/readlater";
	
	public final static String URL_FREE_VERSION_ADDWOW = "freeuser/addwow";
	
	public final static String URL_FREE_VERSION_GOODREAD_ALL = "freeuser/goodreadall";
	
	public final static String URL_FREE_VERSION_GOODREAD_FOLDER = "freeuser/goodreadfolder";
	
	public final static String URL_FREE_VERSION_FOLDERIZE_GOODREAD = "freeuser/goodread";
	
	public final static String URL_FREE_VERSION_REMOVE_GOODREAD = "freeuser/removeGoodRead";
	
	public final static String URL_FREE_VERSION_REMOVE_GOODREAD_FOLDER = "freeuser/folderDelete";
	
	public final static String URL_FREE_VERSION_GET_MENU = "freeuser/getmenu";
	
	public final static String URL_FREE_VERSION_SHARE_TO_MY_SCHOOL = "freeuser/shareschoolfeed";
	
	public final static String URL_FREE_VERSION_POST_CANDLE = "freeuser/candle";
	
	public final static String URL_FREE_VERSION_POST_SCHOOL_CANDLE = "freeuser/candleschool";
	
	public final static String URL_FREE_VERSION_SCHOOL_SEARCH = "freeuser/schoolsearch";
	
	public final static String URL_FREE_VERSION_SCHOOL = "freeuser/school";
	
	public final static String URL_FREE_VERSION_SCHOOL_PAGE = "freeuser/schoolpage";
	
	public final static String URL_FREE_VERSION_SCHOOL_INFO = "freeuser/getschoolinfo";
	
	public final static String URL_PREFERENCE_SETTINGS_GET = "freeuser/get_preference";
	
	public final static String URL_PREFERENCE_SETTINGS_SET = "freeuser/set_preference";
	
	public final static String URL_MEETING_REQUEST = "event/meetingrequest";
	
	public final static String URL_MEETING_STATUS = "event/MeetingStatus";
	
	public final static String URL_MEETING_GETSTUDENTPARENT = "event/getstudentparent";
	
	public final static String URL_MEETING_SEND_REQUEST = "event/addmeetingrequest";
	
	public final static String URL_GET_SINGLE_TERM_REPORT = "Report/getexamreport";
	
	public final static String URL_MEETING_GETTEACHERPARENT = "event/getteacherparent";
	
	public final static String URL_MEETING_SEND_REQUEST_PARENT = "event/addmeetingparent";
	
	public final static String URL_GET_ASSESSMENT= "freeuser/getassesment";
	
	public final static String URL_ASSESSMENT_ADDMARK= "freeuser/addmark";
	
	public final static String URL_ASSESSMENT_UPDATE_PLAY= "freeuser/updateplayed";
	
	public final static String URL_ASSESSMENT_HISTORY= "freeuser/assesmenthistory";
	
	public final static String URL_ASSESSMENT_LEADERBOARD= "freeuser/assesmenttopscore";
	
	public final static String URL_HOMEWORK_ASSESSMENT_LIST= "homework/assessment";
	
	public final static String URL_HOMEWORK_ASSESSMENT= "homework/getassessment";
	
	public final static String URL_HOMEWORK_ASSESSMENT_ADDMARK= "homework/saveassessment";
	
	public final static String URL_SINGLE_HOMEWORK = "homework/singlehomework";
	public final static String URL_SINGLE_CLASSWORK = "classwork/singleclasswork";

	public final static String URL_HOMEWORK_ASSESSMENT_RESULT= "homework/assessmentscore";
	
	public final static String URL_SINGLE_CALENDAR_EVENT= "event/getsingleevent";
	
	public final static String URL_SINGLE_SYLLABUS= "syllabus/single";
	
	public final static String URL_FEES = "event/fees";
	
	public final static String URL_HOMEWORK_STATUS= "homework/homeworkstatus";
	
	public final static String URL_SINGLE_TEACHER_HOMEWORK = "homework/singleteacher";
	public final static String URL_SINGLE_TEACHER_CLASSWORK = "classwork/singleteacher";

	public final static String URL_SINGLE_TEACHER_PUBLISH_HOMEWORK = "homework/publishhomework";
	public final static String URL_SINGLE_TEACHER_PUBLISH_CLASSWORK = "classwork/publishclasswork";
	
	public final static String URL_HOMEWORK_SUBJECT = "homework/subjects";
	public final static String URL_CLASSWORK_SUBJECT = "classwork/subjects";
	
	public final static String URL_HOMEWORK_SUBJECT_TEACHER = "homework/getsubject";
	public final static String URL_CLASSWORK_SUBJECT_TEACHER = "classwork/getSubject";
	
	public final static String URL_APPROVE_LEAVE = "calender/approveLeave";
	
	public final static String URL_GET_NOTICE = "notice/getnotice";
	
	public final static String URL_SINGLE_NOTICE = "notice/getsinglenotice";
	
	public final static String URL_EVENT_REMINDER = "event/readreminder";
	
	public final static String URL_SINGLE_MEETING_REQUEST = "event/meetingrequestsingle";
	
	
	public final static String URL_LOGOUT = "user/logout";


    public final static String URL_LESSONPLAN = "syllabus/lessonplans";

    public final static String URL_LESSON_CATEGORY = "syllabus/lessoncategory";

    public final static String URL_LESSON_DELETE = "syllabus/lessonplandelete";

    public final static String URL_LESSON_ASSIGN = "syllabus/assignlesson";

    public final static String URL_LESSON_SUBJECT = "syllabus/getsubject";

    public final static String URL_LESSON_ADD = "syllabus/addlessonplan";

    public final static String URL_SINGLE_LESSON_PLAN = "syllabus/singlelessonplans";

    public final static String URL_GET_LESSONPLAN_EDIT_DATA = "syllabus/lessonplanedit";

    public final static String URL_GET_LESSONPLAN_SUBJECT_STUDENT_PARENT = "syllabus/lsubjects";

    public final static String URL_GET_LESSONPLAN_SUBJECT_DETAILS = "syllabus/lessonplansstd";

    public final static String URL_EXAM_ROUTINE_TEACHER = "routine/teacherexam";


	//&&&&&&&&&&&&&&&&&&&&&&&&& SPELLING BEE STARTS &&&&&&&&&&&&&&&&&&&&&

	public final static String SPELLINGBEE_INIT = "spelltv";

	public final static String SPELLINGBEE_SAVESCORE = "freeuser/savespellingbee";
	public final static String SPELLINGBEE_SAVESCORE_NEW = "spelltv/savespellingtv";

	public final static String SPELLINGBEE_LEADERBOARD = "freeuser/getleaderboard";
	public final static String SPELLINGBEE_LEADERBOARD_NEW = "spelltv/getleaderboard";


	//&&&&&&&&&&&&&&&&&&&&&&&&& SPELLING BEE ENDS &&&&&&&&&&&&&&&&&&&&&




	//&&&&&&&&&&&&&&&&&&&&&&&&&C_MART&&&&&&&&&&&&&&&&&&&&&
		public final static String BASE_URL_CMART = "http://www.champs21.com/market/xmlconnect/";
		public final static String URL_FREE_VERSION_C_MART_INIT = "configuration/index/app_code/defand1/updated_at/1408969783/screen_size/1280x800";
		public final static String URL_FREE_VERSION_C_MART_INDEX = BASE_URL_CMART+"index";
		public final static String URL_FREE_VERSION_C_MART_CATELOG = "catalog/category/id/4/offset/0/count/3";
		
	public final static String URL_FREE_VERSION_SCHOOL_ACTIVITIES = "freeuser/schoolactivity";
	
	
	public final static String URL_FREE_VERSION_SCHOOL_CREATE = "freeuser/createschool";
	public final static String URL_FREE_VERSION_SCHOOL_JOIN = "freeuser/joinschool";
	public final static String URL_FREE_VERSION_SCHOOL_LEAVE = "freeuser/leaveschool";
	

	//###################### CLASS TUNE APIS ###############################

	//&&&&&&&&&&&&&&& RegistrationFirstPhaseActivity &&&&&&&&&&&&&&&&&&&&&&&&
	public final static String URL_PAID_USERCHECK = "paid/usercheck";
	public final static String URL_PAID_BATCH = "paid/getbatch";
	public final static String URL_PAID_STUDENT = "paid/student";
	public final static String URL_PAID_PARENT = "paid/parent";
	public final static String URL_CHECK_STUDENT = "paid/checkstudent";
	public final static String URL_PAID_TEACHER = "paid/teacher";
	public final static String URL_TEACHER_INFO = "paid/getteacherinfo";
	public final static String URL_TEACHER_POSITION = "paid/getcategoryposition";
	public final static String URL_GET_INFO = "dashboard/getstudentinfo";

	public final static String URL_GET_TEACHER_INFO = "dashboard/getemployeeinfo";

	public final static String URL_GET_SINGLE_EVENT = "event/getsingleevent";
	public final static String URL_GET_SINGLE_REPORT_CARD = "report/subjectreport";

	public final static String URL_GET_APP_VERSION = "user/checkversion";
	public final static String URL_TEACHER_IMPORTANCE_SWAP = "event/checkimportantnotice";
	public final static String URL_TEACHER_ASSOCIATED_SUBJECT = "attendance/associatesubject";
	public final static String URL_TEACHER_ASSOCIATED_GET_STUDENT = "attendance/getstudents";
	public final static String URL_TEACHER_SUBJECT_ATTENDANCE_ADD = "attendance/addattendence";
	public final static String URL_TEACHER_SUBJECT_REPORT = "attendance/reportteacher";
	public final static String URL_TEACHER_SUBJECT_REPORT_ALL = "attendance/reportallteacher";
	public final static String URL_STD_PARENT_SUBJECT = "attendance/getsubject";
	public final static String URL_STD_PARENT_SUBJECT_REPORT = "attendance/report";
	public final static String URL_HAS_ACADEMIC_CALENDAR = "acacal";
	public final static String URL_DOWNLOAD_ACADEMIC_CALENDAR = "acacal/downloadattachment/id/";
	
}
