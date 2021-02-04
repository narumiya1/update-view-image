package com.example.uploadandviewimage.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Sesion {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "app";

    private static final String IS_LOGIN = "IsLoged";
    private static final String IS_FIRST = "IsFisrt";
    private static final String IS_NOT_ALARMT = "IsAlarm";

    public static final String IS_REGISTER="IsRegistered";
    public static final String KEY_USER = "user";
    public static final String KEY_ALARM = "alarm";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASSWORD = "passowrd";
    public static final String KEY_API_JWT = "jwt";


    public Sesion(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }




    public void clearAlarm() {
        pref.edit().remove(KEY_ALARM).commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_LOGIN, false);

        Intent i = new Intent(_context, LoginNumber.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void setIsLogin(Boolean v) {
        editor.putBoolean(IS_LOGIN, v);
//        editor.putBoolean(IS_LOGIN, v);
        editor.commit();
    }

    //23 01 21
    public void setRegisterUser(Boolean v){
        editor.putBoolean(IS_REGISTER,v);
        editor.commit();
    }
    public boolean isFirst() {
        return pref.getBoolean(IS_FIRST, false);
    }

    public boolean isAlarm() {
        return pref.getBoolean(IS_NOT_ALARMT, false);
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "");
    }
    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }
    public String getKeyApiJwt() {
        return pref.getString(KEY_API_JWT, "");
    }
    public void setIsFisrt(Boolean v) {
        editor.putBoolean(IS_FIRST, v);
        editor.commit();
    }

    public void setAlarm(Boolean v) {
        editor.putBoolean(IS_NOT_ALARMT, v);
        editor.commit();
    }

    public void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }
    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }
    public void setKeyApiJwt(String apiJwt) {
        editor.putString(KEY_API_JWT, apiJwt);
        editor.commit();
    }

    public UserResponse getUser() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER, "");
        UserResponse obj = gson.fromJson(json, UserResponse.class);
        return obj;
    }

    public void createLoginSession(UserResponse user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString(KEY_USER, json);

        editor.putBoolean(IS_LOGIN, true);

        editor.commit();

    }
}