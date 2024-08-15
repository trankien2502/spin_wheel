package com.tkt.spin_wheel.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    public static final String SHARED_PREFS_NAME = "GHOST_DETECTOR";
    public static String CAMERA = "CAMERA";
    public static String MICRO = "MICRO";
    public static String LANGUAGE = "LANGUAGE";
    public static String SOUND_GHOST_SCAN = "SOUND_GHOST_SCAN";
    public static String SOUND_CHALLENGE = "SOUND_CHALLENGE";
    public static String GHOST_TYPE = "GHOST_TYPE";
    public static String SCARY_STORIES = "SCARY_STORIES";
    public static String GHOST_ID = "GHOST_ID";
    public static String SOUND_POSITION = "SOUND_POSITION";
    public static String SOUND_CHECK = "SOUND_CHECK";
    public static String RATE_STAR = "RATE_STAR";
    public static int MORE_EDIT_ID = 1;
    public static int MORE_DUPLICATE_ID = 2;
    public static int MORE_TOP_ID = 3;
    public static int MORE_DELETE_ID = 4;



    public static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(str, str2);
    }

    public static void setLong(Context context, String str, long i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putLong(str, i);
        edit.apply();
    }

    public static long getLong(Context context, String str, long i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getLong(str, i);
    }

    public static void setInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt(str, i);
    }

    public static void setFloat(Context context, String str, float i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putFloat(str, i);
        edit.apply();
    }

    public static float getFloat(Context context, String str, float i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getFloat(str, i);
    }

    public static void setBoolean(Context context, String str, boolean b) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putBoolean(str, b);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean b) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getBoolean(str, b);
    }
}
