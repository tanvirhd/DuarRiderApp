package com.duarbd.duarriderapp.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    // save data to sharedPreference
    public static void savePref(String name, String value) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, value);
        editor.apply();
    } // save data to sharedPreference

    public static void savePref(String name, int value) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static void savePrefBoolean(String name, boolean value) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static void clearPref() {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().apply();
    }

    // get data EventFrom shared preference
    public static String getPref(String name, String defaultValue) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        return pref.getString(name, defaultValue);
    }

    public static int getPref(String name, int defaultValue) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        return pref.getInt(name, defaultValue);
    }

    public static Boolean getPrefBoolean(String name, boolean defaultValue) {
        SharedPreferences pref = App.getInstance().getContext().getSharedPreferences(App.getInstance().getPackageName(), Context.MODE_PRIVATE);
        return pref.getBoolean(name, defaultValue);
    }
}
