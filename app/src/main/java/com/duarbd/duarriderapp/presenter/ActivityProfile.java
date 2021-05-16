package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.databinding.ActivityProfileBinding;

public class ActivityProfile extends AppCompatActivity {
    private static final String TAG = "ActivityProfile";
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    void init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Profile");
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}