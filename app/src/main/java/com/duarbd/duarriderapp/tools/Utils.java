package com.duarbd.duarriderapp.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.duarbd.duarriderapp.R;

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

    public static Dialog setupLoadingDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //this prevents dimming effect
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static String getTimeFromDeliveryRequestPlacedDate(String timeDate){
        String[] separated=timeDate.split(" ");
        return separated[1];
    }

    public static String addMinute(String time,int pickupWithin){
        String ampm="";
        String[] separatedTime=time.split(":");
        int hour=Integer.valueOf(separatedTime[0]);
        int min=Integer.valueOf(separatedTime[1]);

        min=min+pickupWithin;
        if(min>60){
            hour=hour+1;
            min=min-60;
        }

        return hour+":"+min;
    }
}
