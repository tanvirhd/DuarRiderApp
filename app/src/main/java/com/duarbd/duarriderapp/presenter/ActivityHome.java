package com.duarbd.duarriderapp.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.adapter.AdapterAssignedRide;
import com.duarbd.duarriderapp.databinding.ActivityHomeBinding;
import com.duarbd.duarriderapp.interfaces.AdapterAssignedRideCallbacks;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, AdapterAssignedRideCallbacks {
    private static final String TAG = "ActivityHome";
    private ActivityHomeBinding binding;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ViewModelRiderApp viewModelRiderApp;
    private Dialog dialogLoading;
    private List<ModelDeliveryRequest> assignedRideList;
    private AdapterAssignedRide adapterAssignedRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavDrawer();
        init();
    }


    void init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(Utils.getPref(KEYS.RIDER_NAME,"Rider Application"));

        dialogLoading=Utils.setupLoadingDialog(ActivityHome.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);

        assignedRideList=new ArrayList<>();
        adapterAssignedRide=new AdapterAssignedRide(assignedRideList,ActivityHome.this,ActivityHome.this);
        binding.recyc.setLayoutManager(new LinearLayoutManager(ActivityHome.this));
        binding.recyc.setAdapter(adapterAssignedRide);

    }

    void initNavDrawer(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerlayout, binding.toolbar, R.string.open, R.string.close);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black0, null));
        else actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black0));
        binding.drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        binding.homeNav.setNavigationItemSelectedListener(this);
    }

    void getAssignedRidesFromServer(){
        dialogLoading.show();
        ModelRider rider=new ModelRider(Utils.getPref(KEYS.RIDER_ID,""));
        viewModelRiderApp.getAssignedRide(rider).observe(ActivityHome.this,
                new Observer<List<ModelDeliveryRequest>>() {
                    @Override
                    public void onChanged(List<ModelDeliveryRequest> modelDeliveryRequests) {
                        if(modelDeliveryRequests!=null&&modelDeliveryRequests.get(0).getResponse().equals("1")){
                            if(!modelDeliveryRequests.get(0).getStatus().equals("NothingFound")){
                                assignedRideList.clear();
                                assignedRideList.addAll(modelDeliveryRequests);
                                adapterAssignedRide.notifyDataSetChanged();
                                dialogLoading.dismiss();
                            }else {
                                assignedRideList.clear();
                                adapterAssignedRide.notifyDataSetChanged();
                                dialogLoading.dismiss();
                            }
                        }else {
                            Toast.makeText(ActivityHome.this, "No ride assigned yet", Toast.LENGTH_SHORT).show();
                            dialogLoading.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        getAssignedRidesFromServer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.deliveryHistory:
                Toast.makeText(this, "Delivery History", Toast.LENGTH_SHORT).show();
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.dailyClearance:
                Toast.makeText(this, "Daily Clearance", Toast.LENGTH_SHORT).show();
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myWallet:
                Toast.makeText(this, "My Wallet", Toast.LENGTH_SHORT).show();
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onMyWayToPickUpClicked(ModelDeliveryRequest assignedDelivery) {
        dialogLoading.show();
        assignedDelivery.setDeliveryStatus(3);
        viewModelRiderApp.updateDeliveryStatusByRequestId(assignedDelivery).observe(ActivityHome.this,
                new Observer<ModelResponse>() {
                    @Override
                    public void onChanged(ModelResponse modelResponse) {
                        if(modelResponse!=null&&modelResponse.getResponse()==1){
                            getAssignedRidesFromServer();
                        }else{
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityHome.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onMyWayToDeliveryClicked(ModelDeliveryRequest assignedDelivery) {
        dialogLoading.show();
        assignedDelivery.setDeliveryStatus(5);
        viewModelRiderApp.updateDeliveryStatusByRequestId(assignedDelivery).observe(ActivityHome.this,
                new Observer<ModelResponse>() {
                    @Override
                    public void onChanged(ModelResponse modelResponse) {
                        if(modelResponse!=null&&modelResponse.getResponse()==1){
                            getAssignedRidesFromServer();
                        }else{
                            dialogLoading.dismiss();
                            Toast.makeText(ActivityHome.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onAssignedRideClicked(ModelDeliveryRequest delivery) {

        startActivity(new Intent(ActivityHome.this,ActivityDeliveryDetails.class)
          .putExtra(getResources().getString(R.string.parcel),delivery));
    }
}