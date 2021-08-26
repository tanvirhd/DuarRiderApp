package com.duarbd.duarriderapp.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.databinding.ActivityDeliveryDetailsBinding;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.network.viewmodel.ViewModelRiderApp;
import com.duarbd.duarriderapp.tools.KEYS;
import com.duarbd.duarriderapp.tools.Utils;

public class ActivityDeliveryDetails extends AppCompatActivity {
    private static final String TAG = "ActivityDeliveryDetails";
    private ActivityDeliveryDetailsBinding binding;
    private ModelDeliveryRequest deliveryDetails;
    private ViewModelRiderApp viewModelRiderApp;
    private Dialog dialogLoading;
    private String clientContactNumber;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeliveryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.btnVerifyPickUpCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPickupCode(binding.etPickupCode.getText().toString());
            }
        });

        binding.btnFinishRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryDetails.setDeliveryStatus(6);
                deliveryDetails.setRiderid(Utils.getPref(KEYS.RIDER_ID,""));
                viewModelRiderApp.updateDeliveryStatusByRequestId(deliveryDetails).observe(ActivityDeliveryDetails.this,
                        new Observer<ModelResponse>() {
                            @Override
                            public void onChanged(ModelResponse modelResponse) {
                                if(modelResponse!=null&&modelResponse.getResponse()==1){
                                    Toast.makeText(ActivityDeliveryDetails.this, "Ride Complete.", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }else{
                                    dialogLoading.dismiss();
                                    //Log.d(TAG, "onChanged: response status="+modelResponse.getStatus());
                                    Toast.makeText(ActivityDeliveryDetails.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    void  init(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Ride Details");
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.tvCallClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clientContactNumber!=null){
                    makeCall(clientContactNumber);
                }else {
                    Toast.makeText(ActivityDeliveryDetails.this, "Please wait. Getting client number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogLoading= Utils.setupLoadingDialog(ActivityDeliveryDetails.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);

        deliveryDetails= getIntent().getParcelableExtra(getResources().getString(R.string.parcel));
        if(deliveryDetails!=null) updateUI(deliveryDetails);
    }

    void  updateUI(ModelDeliveryRequest deliveryDetails){
        Log.d(TAG, "updateUI: client id"+deliveryDetails.getClientID());


        binding.tvDeliveryId.setText("Delivery id: "+deliveryDetails.getDeliveryRequestId());
        binding.tvDeliveyOf.setText(deliveryDetails.getClientName());

        binding.tvCustomerName.setText("Customer Name: "+deliveryDetails.getCustomerName());
        binding.tvCustomerNumber.setText("Customer Number: "+ deliveryDetails.getCustomerNumber());
        binding.tvCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(deliveryDetails.getCustomerNumber());
            }
        });

        binding.tvCallClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(deliveryDetails.getClientID());
            }
        });

        binding.tvProductType.setText("Product Type: "+deliveryDetails.getProductType());
        binding.tvPIckupAddress.setText("Pickup address: "+deliveryDetails.getPickUpAddress());
        binding.tvDeliveryArea.setText("Delivery address (Area): "+deliveryDetails.getDeliveryArea());
        binding.tvDeliveryAddressNote.setText("Delivery address note:"+deliveryDetails.getDeliveryAddressExtra());

        binding.tvCOD.setText("COD Amount:" +(deliveryDetails.getProductPrice()+deliveryDetails.getDeliveryCharge())+" taka");

        switch (deliveryDetails.getDeliveryStatus()){
            case 2:
                binding.tvCallClient.setVisibility(View.VISIBLE);
                binding.tvCallCustomer.setVisibility(View.GONE);
                binding.tvCOD.setVisibility(View.GONE);
                binding.groupClearance.setVisibility(View.GONE);
                binding.btnFinishRide.setVisibility(View.GONE);
                break;
            case 3:
                binding.tvCallClient.setVisibility(View.VISIBLE);
                binding.tvCallCustomer.setVisibility(View.GONE);
                binding.tvCOD.setVisibility(View.GONE);
                binding.groupClearance.setVisibility(View.VISIBLE);
                binding.btnFinishRide.setVisibility(View.GONE);
                break;
            case 4:
                binding.tvCallClient.setVisibility(View.VISIBLE);
                binding.tvCallCustomer.setVisibility(View.VISIBLE);
                binding.tvCOD.setVisibility(View.VISIBLE);
                binding.groupClearance.setVisibility(View.GONE);
                binding.btnFinishRide.setVisibility(View.GONE);
                break;
            case 5:
                binding.tvCallClient.setVisibility(View.VISIBLE);
                binding.tvCallCustomer.setVisibility(View.VISIBLE);
                binding.tvCOD.setVisibility(View.VISIBLE);
                binding.groupClearance.setVisibility(View.GONE);
                binding.btnFinishRide.setVisibility(View.VISIBLE);
                break;
            case 6:
                binding.tvCallClient.setVisibility(View.VISIBLE);
                binding.tvCallCustomer.setVisibility(View.VISIBLE);
                binding.tvCOD.setVisibility(View.GONE);
                binding.groupClearance.setVisibility(View.GONE);
                binding.btnFinishRide.setVisibility(View.GONE);
                break;
        }

    }

    void makeCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    void verifyPickupCode(String code){
        Log.d(TAG, "verifyPickupCode: code"+code);
        if(deliveryDetails.getPickupCode().equals(code)) {
            deliveryDetails.setDeliveryStatus(4);
            viewModelRiderApp.updateDeliveryStatusByRequestId(deliveryDetails).observe(this,
                    new Observer<ModelResponse>() {
                        @Override
                        public void onChanged(ModelResponse modelResponse) {
                            if(modelResponse!=null&&modelResponse.getResponse()==1){
                                deliveryDetails.setDeliveryStatus(4);
                                updateUI(deliveryDetails);
                            }else{
                                dialogLoading.dismiss();
                                Toast.makeText(ActivityDeliveryDetails.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
        }
    }

    void getClientContactNumber(ModelDeliveryRequest deliveryRequest){
      dialogLoading.show();
      viewModelRiderApp.getClientContactNumber(deliveryRequest).observe(ActivityDeliveryDetails.this,
              new Observer<ModelResponse>() {
                  @Override
                  public void onChanged(ModelResponse modelResponse) {
                      if(modelResponse!=null&&modelResponse.getResponse()==1){
                          clientContactNumber=modelResponse.getClientContactNumber();
                          dialogLoading.dismiss();
                      }else {
                          Toast.makeText(ActivityDeliveryDetails.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                          dialogLoading.dismiss();
                      }
                  }
              });
    }*/
}