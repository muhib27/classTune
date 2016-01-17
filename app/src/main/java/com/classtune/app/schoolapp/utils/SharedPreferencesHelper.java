package com.classtune.app.schoolapp.utils;

/* this class is for save or retrieving data in/from shared preference*/


import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


public class SharedPreferencesHelper {
	
    private static SharedPreferencesHelper instance;
 
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
 
    private SharedPreferencesHelper() {
    	
        settings = SchoolApp.getInstance()
                .getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }
 
    public static SharedPreferencesHelper getInstance() {
        if (instance==null)
            instance=new SharedPreferencesHelper();
        return instance;
    }
 
    public String getString(String key, String defValue) {
        return settings.getString(key, defValue);
    }
 
    public boolean hasKey(String key){
    	return settings.contains(key);
    }
    public SharedPreferencesHelper setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
 
        return this;
    }
 
    public int getInt(String key, int defValue) {
        return settings.getInt(key, defValue);
    }
 
    public SharedPreferencesHelper setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
 
        return this;
    }
 
    public boolean getBoolean(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }
 
    public SharedPreferencesHelper setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }
    
   /* public void setUser(User user) {
    	getInstance().setString(SPKeyHelper.TOKEN, user.getPaidInfo().getSecret());
		getInstance().setString(SPKeyHelper.BATCH_ID, user.getPaidInfo().getBatchId());
		getInstance().setBoolean(SPKeyHelper.IS_ADMIN, user.getPaidInfo().isAdmin());
		getInstance().setBoolean(SPKeyHelper.IS_PARENT, user.getPaidInfo().isParent());
		getInstance().setBoolean(SPKeyHelper.IS_STUDENT, user.getPaidInfo().isStudent());
		getInstance().setBoolean(SPKeyHelper.IS_TEACHER, user.getPaidInfo().isTeacher());
		getInstance().setString(SPKeyHelper.SCHOOL_ID, user.getPaidInfo().getSchoolId());
		getInstance().setString(SPKeyHelper.USERNAME, user.getUsername());
		getInstance().setString(SPKeyHelper.PASSWORD, user.getPassword());
		getInstance().setString(SPKeyHelper.PROFILE_ID, user.getPaidInfo().getProfileId());
		getInstance().setInt(SPKeyHelper.TERMS, user.getPaidInfo().getTermList().size());
    }
    
    public User getUser1() {
    	User user = new User();
    	
    	user.setAdmin(getInstance().getBoolean(SPKeyHelper.IS_ADMIN, false));
    	user.setParent(getInstance().getBoolean(SPKeyHelper.IS_PARENT, false));
    	user.setStudent(getInstance().getBoolean(SPKeyHelper.IS_STUDENT, false));
    	user.setTeacher(getInstance().getBoolean(SPKeyHelper.IS_TEACHER, false));
    	user.setBatchId(getInstance().getString(SPKeyHelper.BATCH_ID, ""));
    	user.setSecret(getInstance().getString(SPKeyHelper.TOKEN, ""));
    	user.setSchoolId(getInstance().getString(SPKeyHelper.SCHOOL_ID, ""));
    	
    	user.setUsername(getInstance().getString(SPKeyHelper.USERNAME, ""));
    	user.setPassword(getInstance().getString(SPKeyHelper.PASSWORD, ""));
    	user.setPassword(getInstance().getString(SPKeyHelper.PROFILE_ID, ""));
    	user.setTotalTerms(getInstance().getInt(SPKeyHelper.TERMS, 0));
    	
    	return user;
    }
    */
    public void saveWeekends(ArrayList<Integer> weekends) {
    	getInstance().setInt(SPKeyHelper.WEEKEND_1, weekends.get(0));
		getInstance().setInt(SPKeyHelper.WEEKEND_2, weekends.get(1));
    }
    public ArrayList<Integer> getWeekends() {
    	ArrayList<Integer> weekendList = new ArrayList<Integer>();
    	
    	weekendList.add(getInstance().getInt(SPKeyHelper.WEEKEND_1, 0));
    	weekendList.add(getInstance().getInt(SPKeyHelper.WEEKEND_2, 0));
    	
    	return weekendList;
    }
 
}


