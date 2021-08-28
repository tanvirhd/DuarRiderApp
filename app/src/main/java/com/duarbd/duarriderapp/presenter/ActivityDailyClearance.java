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
import com.duarbd.duarriderapp.adapter.AdapterDailyClearance;
import com.duarbd.duarriderapp.databinding.ActivityDailyClearanceBinding;
import com.duarbd.duarriderapp.interfaces.AdapterDailyClearanceCallback;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityDailyClearance extends AppCompatActivity implements AdapterDailyClearanceCallback {
    private static final String TAG = "ActivityDailyClearance";
    private ActivityDailyClearanceBinding binding;
    private Dialog dialogLoading;

    private List<ModelDeliveryRequest> deliveryHistory;
    private AdapterDailyClearance adapterDailyClearance;
    private ViewModelRiderApp viewModelRiderApp;
    public DecimalFormat twodigits = new DecimalFormat("00");

    private int dailyClearanceAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDailyClearanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.showDeliveryClearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=twodigits.format(binding.datepicker.getSelectedDay())+"-"+
                        twodigits.format( (binding.datepicker.getSelectedMonth()+1))+"-"+
                        twodigits.format(binding.datepicker.getSelectedYear());
                Log.d(TAG, "onClick: "+date);
                getDeliveryClearanceFromServer(date);
            }
        });
    }

    void  init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Daily Clearance");
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        dailyClearanceAmount=0;
        dialogLoading= Utils.setupLoadingDialog(ActivityDailyClearance.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);
        binding.datepicker.setFirstVisibleDate(2021, Calendar.JANUARY, 01);
        binding.datepicker.setLastVisibleDate(2021, Calendar.DECEMBER, 31);
        binding.datepicker.setFollowScroll(true);
        String date[]= Utils.getCustentDateArray();
        binding.datepicker.setSelectedDate(Integer.valueOf(date[2]),Integer.valueOf(date[1])-1,Integer.valueOf(date[0]));

        deliveryHistory=new ArrayList<>();
        adapterDailyClearance=new AdapterDailyClearance(deliveryHistory,ActivityDailyClearance.this,ActivityDailyClearance.this);
        binding.recycDeliveryHistory.setLayoutManager(new LinearLayoutManager(ActivityDailyClearance.this));
        binding.recycDeliveryHistory.setAdapter(adapterDailyClearance);
    }

    @Override
    public void onMoreClicked(ModelDeliveryRequest delivery) {
        startActivity(new Intent(ActivityDailyClearance.this,ActivityDeliveryDetails.class)
                .putExtra(getResources().getString(R.string.parcel),delivery));
    }

    void getDeliveryClearanceFromServer(String date){
        dialogLoading.show();
        viewModelRiderApp.getDeliveryHistoryByRiderId(new ModelRider(Utils.getPref(KEYS.RIDER_ID,""))).observe(ActivityDailyClearance.this,
                new Observer<List<ModelDeliveryRequest>>() {
                    @Override
                    public void onChanged(List<ModelDeliveryRequest> modelDeliveryRequests) {
                        if(modelDeliveryRequests!=null && modelDeliveryRequests.get(0).getResponse().equals("1")){
                            if(modelDeliveryRequests.get(0).getStatus().equals("NothingFound")){
                                dailyClearanceAmount=0;binding.showDeliveryClearance.setText("Clearance amount 0 Tk");
                                deliveryHistory.clear();
                                adapterDailyClearance.notifyDataSetChanged();
                                dialogLoading.dismiss();
                                Toast.makeText(ActivityDailyClearance.this, "0 Ride Found.", Toast.LENGTH_SHORT).show();
                            }else{
                                deliveryHistory.clear();dailyClearanceAmount=0;
                                for(ModelDeliveryRequest delivery:modelDeliveryRequests){
                                    Log.d(TAG, "onChanged: "+delivery.getRequestPlacedAt());
                                    String palcedDate[]=delivery.getRequestPlacedAt().split(" ");
                                    if(date.equals(palcedDate[0])){
                                        deliveryHistory.add(delivery);
                                        if(delivery.getRiderClearance().equals("due"))dailyClearanceAmount=dailyClearanceAmount+delivery.getProductPrice()+delivery.getDeliveryCharge();
                                    }
                                }
                                binding.showDeliveryClearance.setText("Clearance Amount: " +dailyClearanceAmount+" Tk");
                                adapterDailyClearance.notifyDataSetChanged();
                                dialogLoading.dismiss();
                            }
                        }else {
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityDailyClearance.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}