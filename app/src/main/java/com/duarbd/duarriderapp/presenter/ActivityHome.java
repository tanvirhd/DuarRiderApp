package com.duarbd.duarriderapp.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.adapter.AdapterAssignedRide;
import com.duarbd.duarriderapp.databinding.ActivityHomeBinding;
import com.duarbd.duarriderapp.interfaces.AdapterAssignedRideCallbacks;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.model.ModelTokenFCM;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

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

        init();
        initNavDrawer();
        updateFCMToken();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAssignedRidesFromServer("swipeRefresh");
            }
        });
    }


    void init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(Utils.getPref(KEYS.RIDER_NAME,"Rider Application"));
        initHeader();

        dialogLoading=Utils.setupLoadingDialog(ActivityHome.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);

        assignedRideList=new ArrayList<>();
        adapterAssignedRide=new AdapterAssignedRide(assignedRideList,ActivityHome.this,ActivityHome.this);
        binding.recyc.setLayoutManager(new LinearLayoutManager(ActivityHome.this));
        binding.recyc.setAdapter(adapterAssignedRide);

    }

    void initHeader(){
        setSupportActionBar(binding.toolbar);
        View header =  binding.homeNav.getHeaderView(0);
        TextView riderName = header.findViewById(R.id.tvHeaderRiderName);
        TextView tvHeaderInitial=header.findViewById(R.id.tvHeaderInitial);

        String ridername=Utils.getPref(KEYS.RIDER_NAME,"Rider");
        riderName.setText(ridername);
        tvHeaderInitial.setText(ridername.charAt(0)+"");
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
                                Toast.makeText(ActivityHome.this, "No ride assigned yet", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ActivityHome.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            dialogLoading.dismiss();
                        }
                    }
                });
    }

    void getAssignedRidesFromServer(String swipeRefresh){
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
                                binding.swipeRefresh.setRefreshing(false);
                            }else {
                                assignedRideList.clear();
                                adapterAssignedRide.notifyDataSetChanged();
                                binding.swipeRefresh.setRefreshing(false);
                                Toast.makeText(ActivityHome.this, "No ride assigned yet", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ActivityHome.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            binding.swipeRefresh.setRefreshing(false);
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
                startActivity(new Intent(ActivityHome.this,ActivityProfile.class));
                break;
            case R.id.rideHistory:
                startActivity(new Intent(ActivityHome.this,ActivityDeliveryHistory.class));
                break;
            case R.id.dailyClearance:
                startActivity(new Intent(ActivityHome.this,ActivityDailyClearance.class));;
                break;
            //todo note: myWallet is off temporary
            /*case R.id.myWallet:
                startActivity(new Intent(ActivityHome.this,ActivityMyWallet.class));
                break;*/
            case R.id.logout:
                Utils.savePrefBoolean(KEYS.IS_LOGGED_IN,false);
                startActivity(new Intent(ActivityHome.this,ActivityLogin.class));
                finish();
                break;
        }
        return true;
    }


    @Override
    public void onAssignedRideClicked(ModelDeliveryRequest delivery) {
        startActivity(new Intent(ActivityHome.this,ActivityDeliveryDetails.class)
          .putExtra(getResources().getString(R.string.parcel),delivery));
    }

    private  void updateFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                ModelTokenFCM tokenFCM=new ModelTokenFCM(Utils.getPref(KEYS.RIDER_ID,""),"rider",s);
                viewModelRiderApp.updateTokenFCM(tokenFCM).observe(ActivityHome.this,
                        new Observer<ModelResponse>() {
                            @Override
                            public void onChanged(ModelResponse modelResponse) {
                                if(modelResponse!=null&&modelResponse.getResponse()==1){
                                    Toast.makeText(ActivityHome.this, "Token Updated", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ActivityHome.this, "Token update failed!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Token update failed");
            }
        });

    }
}