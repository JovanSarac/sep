package com.example.mobilebank2.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobilebank2.dto.MobileBankUserDto;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER = "user_data";

    public static void saveUser(Context context, MobileBankUserDto user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        editor.putString(KEY_USER, gson.toJson(user));
        editor.apply();
    }

    public static MobileBankUserDto getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, MobileBankUserDto.class);
        }
        return null;
    }

    public static void logout(Context context) {
        SharedPreferences preferences  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences .edit().clear().apply();
    }
}
