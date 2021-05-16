package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.databinding.ActivityMyWalletBinding;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.model.ModelRiderSalary;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ActivityMyWallet extends AppCompatActivity {
    private static final String TAG = "ActivityMyWallet";
    private ActivityMyWalletBinding binding;
    private ViewModelRiderApp viewModelRiderApp;
    private Dialog dialogLoading;
    private ModelRiderSalary rider;

    private BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.collectWages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        binding.processToCollectWaiges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.password.getText().toString().equals("")){
                    Toast.makeText(ActivityMyWallet.this, "Enter Your Password!!", Toast.LENGTH_SHORT).show();
                }else if(binding.tvCurrentWages.getText().toString().equals("0 Tk")){
                    Toast.makeText(ActivityMyWallet.this, "Your wallet is empty :(", Toast.LENGTH_SHORT).show();
                    binding.password.setText("");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else{
                    if(binding.password.getText().toString().equals(rider.getRiderPass())){
                        collectRiderSalary();
                    }else {
                        Toast.makeText(ActivityMyWallet.this, "Wrong Credential!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    void getWagesFromServer(){
        dialogLoading.show();
        viewModelRiderApp.getRiderSalary(new ModelRider(Utils.getPref(KEYS.RIDER_ID,""))).observe(ActivityMyWallet.this,
                new Observer<ModelRiderSalary>() {
                    @Override
                    public void onChanged(ModelRiderSalary modelRiderSalary) {
                        if(modelRiderSalary!=null && modelRiderSalary.getResponse()==1){
                            rider=modelRiderSalary;
                            binding.tvCurrentWages.setText(rider.getRiderSalary()+" Tk");
                            dialogLoading.dismiss();
                        }else {
                            dialogLoading.dismiss();
                            binding.tvCurrentWages.setText("0 Tk");
                            Toast.makeText(ActivityMyWallet.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void  collectRiderSalary(){
        dialogLoading.show();
        viewModelRiderApp.collectRiderSalary(new ModelRider(Utils.getPref(KEYS.RIDER_ID,""))).observe(ActivityMyWallet.this,
                new Observer<ModelResponse>() {
                    @Override
                    public void onChanged(ModelResponse modelResponse) {
                        if(modelResponse!=null&&modelResponse.getResponse()==1){
                            dialogLoading.dismiss();
                            binding.tvCurrentWages.setText("0 Tk");
                            binding.password.setText("");
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }else {
                            dialogLoading.dismiss();
                            binding.password.setText("");
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            Toast.makeText(ActivityMyWallet.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void init(){
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomSheetBehavior=BottomSheetBehavior.from(binding.bottomsheet);
        dialogLoading= Utils.setupLoadingDialog(ActivityMyWallet.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWagesFromServer();
    }
}