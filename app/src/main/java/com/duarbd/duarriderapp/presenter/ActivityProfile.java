package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.databinding.ActivityProfileBinding;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ActivityProfile extends AppCompatActivity {
    private static final String TAG = "ActivityProfile";
    private ActivityProfileBinding binding;

    private Dialog dialogLoading;
    private ViewModelRiderApp viewModelRiderApp;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.btnShowUpdatePassWordBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        binding.updatePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.newPass.getText().toString().equals(binding.retypedPass.getText().toString())){
                    updateRiderPassword(binding.newPass.getText().toString());
                }else {
                    Toast.makeText(ActivityProfile.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        bottomSheetBehavior=BottomSheetBehavior.from(binding.bottomsheet);

        if(Utils.getPrefBoolean(KEYS.IS_PASSWORD_UPDATED,false)){
            binding.group.setVisibility(View.GONE);
        }else  binding.group.setVisibility(View.VISIBLE);

        dialogLoading= Utils.setupLoadingDialog(ActivityProfile.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);
    }

    void getRiderInformationFromServer(){
        dialogLoading.show();
        viewModelRiderApp.getRiderInformation(new ModelRider(Utils.getPref(KEYS.RIDER_ID,""))).observe(ActivityProfile.this,
                new Observer<ModelRider>() {
                    @Override
                    public void onChanged(ModelRider rider) {
                        if(rider!=null&&rider.getResponse()==1){
                            updateUI(rider);
                            dialogLoading.dismiss();
                        }else {
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityProfile.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void updateUI(ModelRider rider){
        binding.tvInitial.setText(rider.getRiderName().charAt(0)+"");
        binding.tvRiderName.setText(rider.getRiderName());
        binding.tvVehicleType.setText(rider.getVehicleType());
        binding.tvContactNumber.setText(rider.getRiderContactNumber());
        binding.tvRiderOf.setText(rider.getVehicleType()+" Rider");
    }

    void  updateRiderPassword(String updatedPassword){
        dialogLoading.show();
        ModelRider rider=new ModelRider(Utils.getPref(KEYS.RIDER_ID,""),updatedPassword);
        viewModelRiderApp.updateRiderPassword(rider).observe(ActivityProfile.this,
                new Observer<ModelResponse>() {
                    @Override
                    public void onChanged(ModelResponse modelResponse) {
                        if(modelResponse!=null&&modelResponse.getResponse()==1){
                            binding.group.setVisibility(View.GONE);
                            Utils.savePrefBoolean(KEYS.IS_PASSWORD_UPDATED,true);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityProfile.this, "Password Updated.", Toast.LENGTH_SHORT).show();
                        }else {
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityProfile.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRiderInformationFromServer();
    }
}