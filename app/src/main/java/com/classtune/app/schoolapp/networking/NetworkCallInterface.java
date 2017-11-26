package com.classtune.app.schoolapp.networking;

import com.classtune.app.schoolapp.utils.URLHelper;
import com.google.gson.JsonElement;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Extreme_Piash on 7/31/2017.
 */

public interface NetworkCallInterface {

    @FormUrlEncoded
    @POST(URLHelper.URL_LOGIN)
    Call<JsonElement> userLogin(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GCM_REGISTER)
    Call<JsonElement> gcmRegister(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_NOTICE)
    Call<JsonElement> notice(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_TEACHER_BATCH)
    Call<JsonElement> getBatch(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PAID_VERSION_CLASSTUNE_FEED)
    Call<JsonElement> getUserFeed(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_POST_ADD_ATTENDANCE)
    Call<JsonElement> addAttendence(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_STUDENT_INFO)
    Call<JsonElement> getStudentInfo(@FieldMap HashMap<String, String> params);

    //****************for Random Picker*****
    @FormUrlEncoded
    @POST
    Call<JsonElement> getPickerTypeLeave(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST
    Call<JsonElement> getPickerTypeGraphSubject(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST
    Call<JsonElement> getPickerTypeStudent(@FieldMap HashMap<String, String> params, @Url String url);

    //********
    @FormUrlEncoded
    @POST(URLHelper.URL_GET_STUDENTS_ATTENDANCE)
    Call<JsonElement> getStudentAttendence(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_REPORT_PROGRESS)
    Call<JsonElement> getReportProgress(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_REPORT_PROGRESS_ALL)
    Call<JsonElement> getReportProgressAll(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_STUDENT_LEAVE_LIST)
    Call<JsonElement> studentLeaveList(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_TEACHER_LEAVE_LIST)
    Call<JsonElement> teacherLeaveList(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_ACADEMIC_CALENDAR_EVENTS)
    Call<JsonElement> academicClender(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_BATCH_CLASS_REPORT)
    Call<JsonElement> getBatchClassReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST
    Call<JsonElement> homework(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST
    Call<JsonElement> classwork(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_ATTENDENCE_EVENTS)
    Call<JsonElement> getAttendenceEvent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_DONE)
    Call<JsonElement> homeworkDone(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_NOTICE_ACKNOWLEDGE)
    Call<JsonElement> noticeAcknowledge(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SYLLABUS_TERM)
    Call<JsonElement> syllabusTerm(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SYLLABUS)
    Call<JsonElement> syllabus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_EVENT_LIST)
    Call<JsonElement> eventList(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_GET_SUBJECT)
    Call<JsonElement> teacherHomeworkSubject(@FieldMap HashMap<String, String> params);

    //********to do add homework and add classwork
    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_CLASSWORK_GET_SUBJECT)
    Call<JsonElement> teacherClassworkSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_HOMEWORK_FEED)
    Call<JsonElement> teacherHomeworkFeed(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_CLASSWORK_FEED)
    Call<JsonElement> teacherClassworkFeed(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_LEAVE)
    Call<JsonElement> teacherLeave(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PARENT_LEAVE)
    Call<JsonElement> parentLeave(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_REPORT_CARD)
    Call<JsonElement> reportCard(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_NOTIFICATION)
    Call<JsonElement> notification(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_POST_ACK_EVENT)
    Call<JsonElement> eventAcknowledge(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_ROUTINE)
    Call<JsonElement> routine(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_ROUTINE_EXAM)
    Call<JsonElement> routineExam(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TRANSPORT)
    Call<JsonElement> transports(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_EXAM_ROUTINE_STUDENT_PARENT)
    Call<JsonElement> examRoutine(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_RESULT_REPORT)
    Call<JsonElement> getResultReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_RESULT_GROUP_REPORT)
    Call<JsonElement> getResultGroupReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_RESULT_GROUP_AUTH_DOWNLOAD)
    Call<JsonElement> resultGrouptAuthDounload(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_NEXT_CLASS_DATA)
    Call<JsonElement> getNextClassData(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_NEXT_CLASS_STUDENT)
    Call<JsonElement> getNextClassStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_WEEK_DAY_CLASSES)
    Call<JsonElement> getWeekDayClasses(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_WEEK_DAY_STUDENT_CLASSES)
    Call<JsonElement> getWeekDayStudentClasses(@FieldMap HashMap<String, String> params);

    // multipath have to implement letter
    @FormUrlEncoded
    @POST(URLHelper.FREE_USER_CREATE)
    Call<JsonElement> createUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FORGET_PASSWORD)
    Call<JsonElement> forgetPassword(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_RELATED_NEWS)
    Call<JsonElement> freeVersionRelatedNews(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PAID_VERSION_CLASSTUNE_FEED)
    Call<JsonElement> freeVersionClassTuneFeed(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_SCHOOL_FEED)
    Call<JsonElement> freeVersionSchoolFeed(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_SEARCH)
    Call<JsonElement> freeVersionSchoolSearch(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_GET_USER)
    Call<JsonElement> freeVersionGetUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_SHARE_TO_MY_SCHOOL)
    Call<JsonElement> freeVersionShareMySchool(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PREFERENCE_SETTINGS_GET)
    Call<JsonElement> preferenceSettingGet(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PREFERENCE_SETTINGS_SET)
    Call<JsonElement> preferenceSettingSet(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_MEETING_REQUEST)
    Call<JsonElement> mettingRequest(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_MEETING_STATUS)
    Call<JsonElement> mettingStatus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST
    Call<JsonElement> mettingStudentParent(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST
    Call<JsonElement> mettingSendRequest(@FieldMap HashMap<String, String> params, @Url String url);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_SINGLE_TERM_REPORT)
    Call<JsonElement> singleTermReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_ASSESSMENT)
    Call<JsonElement> getAccessment(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_ASSESSMENT_ADDMARK)
    Call<JsonElement> accessMentAddMark(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_ASSESSMENT_UPDATE_PLAY)
    Call<JsonElement> accessMentUpdatePlay(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_ASSESSMENT_LEADERBOARD)
    Call<JsonElement> accessmentLeaderBoad(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_ASSESSMENT_LIST)
    Call<JsonElement> homeworkAccessmentList(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_ASSESSMENT)
    Call<JsonElement> homeworkAccessment(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_ASSESSMENT_ADDMARK)
    Call<JsonElement> homeworkAccessmentAddmark(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_HOMEWORK)
    Call<JsonElement> singleHomework(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_CLASSWORK)
    Call<JsonElement> singleClasswork(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_ASSESSMENT_RESULT)
    Call<JsonElement> homeworkAccessmentResult(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_CALENDAR_EVENT)
    Call<JsonElement> singleCalendarEvent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_SYLLABUS)
    Call<JsonElement> singleSyllabus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FEES)
    Call<JsonElement> fees(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_STATUS)
    Call<JsonElement> homeworkStatus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_TEACHER_HOMEWORK)
    Call<JsonElement> singleTeacherHomework(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_TEACHER_CLASSWORK)
    Call<JsonElement> singleTeacherClasswork(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_TEACHER_PUBLISH_HOMEWORK)
    Call<JsonElement> singleTeacherHomeworkPublish(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_TEACHER_PUBLISH_CLASSWORK)
    Call<JsonElement> singleTeacherClassworkPublish(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_SUBJECT)
    Call<JsonElement> homeworkSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_CLASSWORK_SUBJECT)
    Call<JsonElement> classworkSubject(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST(URLHelper.URL_HOMEWORK_SUBJECT_TEACHER)
    Call<JsonElement> homeworkSubjectTeacher(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_CLASSWORK_SUBJECT_TEACHER)
    Call<JsonElement> classworkSubjectTeacher(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_APPROVE_LEAVE)
    Call<JsonElement> approveLeave(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_NOTICE)
    Call<JsonElement> getNotice(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_NOTICE)
    Call<JsonElement> getSingleNotice(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_EVENT_REMINDER)
    Call<JsonElement> eventReminder(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_MEETING_REQUEST)
    Call<JsonElement> singleMeetingRequest(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LOGOUT)
    Call<JsonElement> logOut(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSONPLAN)
    Call<JsonElement> lessonPlan(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_CATEGORY)
    Call<JsonElement> lessonCategory(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_DELETE)
    Call<JsonElement> lessonDelete(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_ASSIGN)
    Call<JsonElement> lessonAssign(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_SUBJECT)
    Call<JsonElement> lessonSubject(@FieldMap HashMap<String, String> params);

    //have to implement add lesson plan

    @FormUrlEncoded
    @POST(URLHelper.URL_SINGLE_LESSON_PLAN)
    Call<JsonElement> singleLessonPlan(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_LESSONPLAN_EDIT_DATA)
    Call<JsonElement> lessonplanEditData(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_LESSONPLAN_SUBJECT_STUDENT_PARENT)
    Call<JsonElement> lessonplanSubjectStudentParent(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST(URLHelper.URL_GET_LESSONPLAN_SUBJECT_DETAILS)
    Call<JsonElement> lessonplanSubjectDetail(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_EXAM_ROUTINE_TEACHER)
    Call<JsonElement> examRoutineTeacher(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_FREE_VERSION_C_MART_INDEX)
    Call<JsonElement> cMartIndex(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PAID_USERCHECK)
    Call<JsonElement> paidUserCheck(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_PAID_BATCH)
    Call<JsonElement> paidBatch(@FieldMap HashMap<String, String> params);

    /*@FormUrlEncoded
    @POST(URLHelper.URL_PAID_STUDENT)
    Call<JsonElement> paidStudent(@FieldMap HashMap<String, String> params);*/

    @FormUrlEncoded
    @POST(URLHelper.URL_CHECK_STUDENT)
    Call<JsonElement> checkStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_INFO)
    Call<JsonElement> teacherInfo(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_POSITION)
    Call<JsonElement> teacherPosition(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_TEACHER_INFO)
    Call<JsonElement> getTeacherInfo(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_SINGLE_EVENT)
    Call<JsonElement> getSingleEvent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_SINGLE_REPORT_CARD)
    Call<JsonElement> getSingleReportCard(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_GET_APP_VERSION)
    Call<JsonElement> getAppVersion(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_IMPORTANCE_SWAP)
    Call<JsonElement> teacherImportantSwap(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ASSOCIATED_SUBJECT)
    Call<JsonElement> teacherAssociatedSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ASSOCIATED_GET_STUDENT)
    Call<JsonElement> teacherAssociatedGetStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_SUBJECT_ATTENDANCE_ADD)
    Call<JsonElement> teacherSubjectAttendanceAdd(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_SUBJECT_REPORT)
    Call<JsonElement> teacherSubjectReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_SUBJECT_REPORT_ALL)
    Call<JsonElement> teacherSubjectReportAll(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_STD_PARENT_SUBJECT)
    Call<JsonElement> stdParentSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_STD_PARENT_SUBJECT_REPORT)
    Call<JsonElement> stdParentSubjectReport(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_HAS_ACADEMIC_CALENDAR)
    Call<JsonElement> hasAcademicCalendar(@FieldMap HashMap<String, String> params);

    // ################################ Multipart ###################################
    @Multipart
    @POST(URLHelper.URL_TEACHER_ADD_HOMEWORK)
    Call<JsonElement> teacherAddHomework(@Part("user_secret") RequestBody user_secret, @Part("subject_id")RequestBody subject_id, @Part("content")RequestBody content, @Part("title")RequestBody title, @Part("type")RequestBody type,
                                         @Part("duedate") RequestBody duedate,
                                         @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size, @Part("is_draft") RequestBody is_draft, @Part("students")RequestBody student);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ADD_HOMEWORK)
    Call<JsonElement> teacherAddHomework(@Field("user_secret") String user_secret, @Field("subject_id")String subject_id, @Field("content")String content, @Field("title")String title, @Field("type")String type,
                                         @Field("duedate") String duedate,@Field("is_draft") String is_draft);

    @Multipart
    @POST(URLHelper.URL_TEACHER_ADD_HOMEWORK)
    Call<JsonElement> teacherAddHomeworkEdit(@Part("user_secret") RequestBody user_secret, @Part("subject_id")RequestBody subject_id, @Part("content")RequestBody content, @Part("title")RequestBody title, @Part("type")RequestBody type,
                                         @Part("duedate") RequestBody duedate, @Part("id") RequestBody id,
                                         @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ADD_HOMEWORK)
    Call<JsonElement> teacherAddHomeworkEdit(@Field("user_secret") String user_secret, @Field("subject_id")String subject_id, @Field("content")String content, @Field("title")String title, @Field("type")String type,
                                         @Field("duedate") String duedate, @Field("id") String id);


    @Multipart
    @POST(URLHelper.URL_TEACHER_ADD_CLASSWORK)
    Call<JsonElement> teacherAddClasswork(@Part("user_secret") RequestBody user_secret, @Part("subject_id")RequestBody subject_id, @Part("content")RequestBody content, @Part("title")RequestBody title, @Part("type")RequestBody type,
                                         @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size, @Part("is_draft") RequestBody is_draft);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ADD_CLASSWORK)
    Call<JsonElement> teacherAddClasswork(@Field("user_secret") String user_secret, @Field("subject_id")String subject_id, @Field("content")String content, @Field("title")String title, @Field("type")String type, @Field("duedate") String is_draft);

    @Multipart
    @POST(URLHelper.URL_TEACHER_ADD_CLASSWORK)
    Call<JsonElement> teacherAddClassworkEdit(@Part("user_secret") RequestBody user_secret, @Part("subject_id")RequestBody subject_id, @Part("content")RequestBody content, @Part("title")RequestBody title, @Part("type")RequestBody type,
                                              @Part("id") RequestBody id,
                                             @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_ADD_CLASSWORK)
    Call<JsonElement> teacherAddClassworkEdit(@Field("user_secret") String user_secret, @Field("subject_id")String subject_id, @Field("content")String content, @Field("title")String title, @Field("type")String type, @Field("id") String id);



    @Multipart
    @POST(URLHelper.URL_LESSON_ADD)
    Call<JsonElement> teacherAddLessonplan(@Part("user_secret") RequestBody user_secret, @Part("subject_ids")RequestBody subject_ids, @Part("lessonplan_category_id")RequestBody categoryId, @Part("title")RequestBody title, @Part("publish_date")RequestBody publishdate,
                                           @Part("content")RequestBody content,@Part("is_show")RequestBody ishow,
                                           @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_ADD)
    Call<JsonElement> teacherAddLessonplan(@Field("user_secret") String user_secret, @Field("subject_ids") String subject_ids, @Field("lessonplan_category_id") String categoryId, @Field("title") String title, @Field("publish_date") String publishdate,
                                           @Field("content") String content,@Field("is_show") String ishow);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_ADD)
    Call<JsonElement> teacherAddLessonplanEdit(@Field("user_secret") String user_secret, @Field("subject_ids") String subject_ids, @Field("lessonplan_category_id") String categoryId, @Field("title") String title, @Field("publish_date") String publishdate,
                                           @Field("content") String content,@Field("is_show") String ishow, @Field("id") String id);

/*

    @Multipart
    @POST(URLHelper.URL_LESSON_ADD)
    Call<JsonElement> teacherAddLessonplanEdit(@Part("user_secret") RequestBody user_secret, @Part("subject_id")RequestBody subject_id, @Part("content")RequestBody content, @Part("title")RequestBody title, @Part("type")RequestBody type,
                                              @Part("id") RequestBody id,
                                              @Part MultipartBody.Part attachment_file_name, @Part("mime_type") RequestBody mime_type, @Part("file_size")RequestBody file_size);

    @FormUrlEncoded
    @POST(URLHelper.URL_LESSON_ADD)
    Call<JsonElement> teacherAddLessonplankEdit(@Field("user_secret") String user_secret, @Field("subject_id")String subject_id, @Field("content")String content, @Field("title")String title, @Field("type")String type, @Field("id") String id);
*/


    @FormUrlEncoded
    @POST(URLHelper.URL_SELECT_STUDENT_HW_ADD)
    Call<JsonElement> showStudentList(@Field("user_secret") String user_secret, @Field("subject_id") String subject_id);


    //muhib
    @FormUrlEncoded
    @POST(URLHelper.URL_DEFAULTER_STUDENT_LIST)
    Call<JsonElement> getDefaulterStudentList(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_TEACHER_DEFAULTER_ADD)
    Call<JsonElement> teacherDefaulterAdd(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(URLHelper.URL_DEFAULTER_LIST)
    Call<JsonElement> getDefaultertList(@FieldMap HashMap<String, String> params);

}
