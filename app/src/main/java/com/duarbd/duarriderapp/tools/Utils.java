package com.duarbd.duarriderapp.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Window;

import com.duarbd.duarriderapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        DecimalFormat twodigits = new DecimalFormat("00");
        String ampm="";
        String[] separatedTime=time.split(":");
        int hour=Integer.valueOf(separatedTime[0]);
        int min=Integer.valueOf(separatedTime[1]);

        min=min+pickupWithin;
        if(min>=60){
            hour=hour+1;
            min=min-60;
        }

        //todo 00:00 time exception not checked
        if(hour<12){
            return hour+":"+twodigits.format(min)+" am";
        }else if(hour==12){
            return hour+":"+twodigits.format(min)+" pm";
        }else {
            hour=hour-12;
            return hour+":"+twodigits.format(min)+" pm";
        }
    }

    public static String[] getCustentDateArray(){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormatDate = new SimpleDateFormat("dd-MM-yyyy");
        String date=dateFormatDate.format(calendar.getTime());
        String[] seperated=date.split("-");
        return seperated;
    }

    public static String convertTimeTo12HrFormat(String time){ //arg example 13:30
        DecimalFormat twodigits = new DecimalFormat("00");
        String ampm="";
        String[] separatedTime=time.split(":");
        int hour=Integer.valueOf(separatedTime[0]);
        int min=Integer.valueOf(separatedTime[1]);

        //todo 00:00 time exception not checked
        if(hour<12){
            return hour+":"+twodigits.format(min)+" am";
        }else if(hour==12){
            return hour+":"+twodigits.format(min)+" pm";
        }else {
            hour=hour-12;
            return hour+":"+twodigits.format(min)+" pm";
        }
    }

    public static boolean isNetworkAvailable(Context activity) {

        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Network[] networks = connectivity.getAllNetworks();
            NetworkInfo networkInfo;

            for (Network mNetwork : networks) {

                networkInfo = connectivity.getNetworkInfo(mNetwork);

                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }

        } else {
            if (connectivity != null) {

                NetworkInfo[] info = connectivity.getAllNetworkInfo();

                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Uri getDefaultRingtoneUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

    public static String getCurrentDateTime24HRFormat(){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormatDate = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
        return dateFormatDate.format(calendar.getTime())+" "+dateFormatTime.format(calendar.getTime());
    }
}
