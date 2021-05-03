package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(Utils.getPrefBoolean(KEYS.IS_LOGGED_IN,false)){
            startActivity(new Intent(ActivitySplash.this,ActivityHome.class));finish();
        }else {
            startActivity(new Intent(ActivitySplash.this,ActivityLogin.class));finish();
        }
    }
}