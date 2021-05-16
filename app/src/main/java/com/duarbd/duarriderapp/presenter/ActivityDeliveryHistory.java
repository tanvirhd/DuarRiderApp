package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.adapter.AdapterRideHistory;
import com.duarbd.duarriderapp.databinding.ActivityDeliveryHistoryBinding;
import com.duarbd.duarriderapp.interfaces.AdapterRideHistoryCallback;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityDeliveryHistory extends AppCompatActivity implements AdapterRideHistoryCallback {
    private static final String TAG = "ActivityDeliveryHistory";
    private ActivityDeliveryHistoryBinding binding;
    private Dialog dialogLoading;
    private ViewModelRiderApp viewModelRiderApp;

    private List<ModelDeliveryRequest> deliveryHistory;
    private AdapterRideHistory adapterRideHistory;


    public  DecimalFormat twodigits = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeliveryHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        init();

        binding.showDEliveryHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=twodigits.format(binding.datepicker.getSelectedDay())+"-"+
                        twodigits.format( (binding.datepicker.getSelectedMonth()+1))+"-"+
                        twodigits.format(binding.datepicker.getSelectedYear());
                Log.d(TAG, "onClick: "+date);
                getRideHistoryFromServer(date);
            }
        });
    }

    void init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Ride History");
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        dialogLoading= Utils.setupLoadingDialog(ActivityDeliveryHistory.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);
        binding.datepicker.setFirstVisibleDate(2021, Calendar.JANUARY, 01);
        binding.datepicker.setLastVisibleDate(2021, Calendar.DECEMBER, 31);
        binding.datepicker.setFollowScroll(true);
        String date[]= Utils.getCustentDateArray();
        binding.datepicker.setSelectedDate(Integer.valueOf(date[2]),Integer.valueOf(date[1])-1,Integer.valueOf(date[0]));

        deliveryHistory=new ArrayList<>();
        adapterRideHistory=new AdapterRideHistory(deliveryHistory,ActivityDeliveryHistory.this,ActivityDeliveryHistory.this);
        binding.recycDeliveryHistory.setLayoutManager(new LinearLayoutManager(ActivityDeliveryHistory.this));
        binding.recycDeliveryHistory.setAdapter(adapterRideHistory);

    }

    @Override
    public void onMoreClicked(ModelDeliveryRequest delivery) {
        startActivity(new Intent(ActivityDeliveryHistory.this,ActivityDeliveryDetails.class).putExtra(getResources().getString(R.string.parcel),delivery));
    }

    void getRideHistoryFromServer(String date){
        dialogLoading.show();
        viewModelRiderApp.getDeliveryHistoryByRiderId(new ModelRider(Utils.getPref(KEYS.RIDER_ID,""))).observe(ActivityDeliveryHistory.this,
                new Observer<List<ModelDeliveryRequest>>() {
                    @Override
                    public void onChanged(List<ModelDeliveryRequest> modelDeliveryRequests) {
                        if(modelDeliveryRequests!=null && modelDeliveryRequests.get(0).getResponse().equals("1")){
                            if(modelDeliveryRequests.get(0).getStatus().equals("NothingFound")){
                                deliveryHistory.clear();
                                adapterRideHistory.notifyDataSetChanged();
                                dialogLoading.dismiss();
                                Toast.makeText(ActivityDeliveryHistory.this, "0 Ride Found.", Toast.LENGTH_SHORT).show();
                            }else{

                                deliveryHistory.clear();
                                for(ModelDeliveryRequest delivery:modelDeliveryRequests){
                                    String palceddate[]=delivery.getRequestPlacedTime().split(" ");
                                    if(date.equals(palceddate[0])){
                                        deliveryHistory.add(delivery);
                                    }
                                }
                                adapterRideHistory.notifyDataSetChanged();
                                dialogLoading.dismiss();
                            }
                        }else {
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityDeliveryHistory.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}