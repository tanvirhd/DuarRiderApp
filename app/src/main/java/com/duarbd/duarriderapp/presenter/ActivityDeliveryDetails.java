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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeliveryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.tvRDConfirmPickupCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPickupCode(binding.etRDPickupCode.getText().toString());
            }
        });

        binding.ivRDCallClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryDetails!=null){
                    makeCall(deliveryDetails.getClientID());
                }else {
                    Toast.makeText(ActivityDeliveryDetails.this, "Please wait. Getting client number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.ivRdCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryDetails!=null){
                    makeCall(deliveryDetails.getCustomerNumber());
                }else {
                    Toast.makeText(ActivityDeliveryDetails.this, "Please wait. Getting customer number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnUpdateDeliveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoading.show();

                switch (binding.btnUpdateDeliveryStatus.getText().toString()){
                    case "On my way to pickup":
                        deliveryDetails.setDeliveryStatus(3);
                        break;
                    case "On my way to delivery":
                        deliveryDetails.setDeliveryStatus(5);
                        break;
                    case "Finish Ride":
                        deliveryDetails.setDeliveryStatus(6);
                        deliveryDetails.setDeliveredAt(Utils.getCurrentDateTime24HRFormat());
                        break;
                }
                viewModelRiderApp.updateDeliveryStatusByRequestId(deliveryDetails).observe(ActivityDeliveryDetails.this,
                        new Observer<ModelResponse>() {
                            @Override
                            public void onChanged(ModelResponse modelResponse) {
                                if(modelResponse!=null&&modelResponse.getResponse()==1){
                                    dialogLoading.dismiss();
                                    updateUI(deliveryDetails);
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


        dialogLoading= Utils.setupLoadingDialog(ActivityDeliveryDetails.this);
        viewModelRiderApp=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModelRiderApp.class);

        deliveryDetails= getIntent().getParcelableExtra(getResources().getString(R.string.parcel));
        if(deliveryDetails!=null) updateUI(deliveryDetails);
    }

    void  updateUI(ModelDeliveryRequest deliveryDetails){
        if(!dialogLoading.isShowing())dialogLoading.show();

        if(deliveryDetails.isOnHold()==1){
            binding.clientInformationBlock.setVisibility(View.GONE);
            binding.paymentInformationBlock.setVisibility(View.GONE);
            binding.pickupCodeBlock.setVisibility(View.GONE);
            binding.btnUpdateDeliveryStatus.setVisibility(View.GONE);
            binding.tvRDOnHold.setVisibility(View.VISIBLE);
        }else {
            binding.tvRDOnHold.setVisibility(View.GONE);

            switch (deliveryDetails.getDeliveryStatus()){
                case 2:
                    binding.tvRDDeliveryStatus.setText("Assigned");
                    binding.clientInformationBlock.setVisibility(View.VISIBLE);

                    binding.customerInformationBlock.setVisibility(View.VISIBLE);
                    binding.ivRdCallCustomer.setVisibility(View.GONE);

                    binding.paymentInformationBlock.setVisibility(View.GONE);
                    binding.pickupCodeBlock.setVisibility(View.GONE);

                    binding.btnUpdateDeliveryStatus.setVisibility(View.VISIBLE);
                    binding.btnUpdateDeliveryStatus.setText("On my way to pickup");

                    binding.tvRDTimeStatusNote.setText("Pickup Time");
                    binding.tvRDTimeStatus.setText(
                       Utils.addMinute(
                          Utils.getTimeFromDeliveryRequestPlacedDate(
                            deliveryDetails.getRequestPlacedAt()),deliveryDetails.getPickupTime()
                       )
                    );
                    break;
                case 3:
                    binding.tvRDDeliveryStatus.setText("On my way to pickup");
                    binding.clientInformationBlock.setVisibility(View.VISIBLE);

                    binding.customerInformationBlock.setVisibility(View.VISIBLE);
                    binding.ivRdCallCustomer.setVisibility(View.GONE);

                    binding.paymentInformationBlock.setVisibility(View.GONE);
                    binding.pickupCodeBlock.setVisibility(View.VISIBLE);

                    binding.btnUpdateDeliveryStatus.setVisibility(View.GONE);

                    binding.tvRDTimeStatusNote.setText("Pickup Time");
                    binding.tvRDTimeStatus.setText(
                            Utils.addMinute(
                                    Utils.getTimeFromDeliveryRequestPlacedDate(
                                            deliveryDetails.getRequestPlacedAt()),deliveryDetails.getPickupTime()
                            )
                    );
                    break;
                case 4:
                    binding.tvRDDeliveryStatus.setText("Received");
                    binding.clientInformationBlock.setVisibility(View.VISIBLE);

                    binding.customerInformationBlock.setVisibility(View.VISIBLE);
                    binding.ivRdCallCustomer.setVisibility(View.VISIBLE);

                    binding.paymentInformationBlock.setVisibility(View.VISIBLE);
                    binding.pickupCodeBlock.setVisibility(View.GONE);

                    binding.btnUpdateDeliveryStatus.setVisibility(View.VISIBLE);
                    binding.btnUpdateDeliveryStatus.setText("On my way to delivery");

                    binding.tvRDTimeStatusNote.setText("Picked-up At");
                    binding.tvRDTimeStatus.setText(Utils.convertTimeTo12HrFormat(deliveryDetails.getPickedUpAt().split(" ")[1]));
                    break;
                case 5:
                    binding.tvRDDeliveryStatus.setText("On my way to Delivery");
                    binding.clientInformationBlock.setVisibility(View.VISIBLE);

                    binding.customerInformationBlock.setVisibility(View.VISIBLE);
                    binding.ivRdCallCustomer.setVisibility(View.VISIBLE);

                    binding.paymentInformationBlock.setVisibility(View.VISIBLE);
                    binding.pickupCodeBlock.setVisibility(View.GONE);

                    binding.btnUpdateDeliveryStatus.setVisibility(View.VISIBLE);
                    binding.btnUpdateDeliveryStatus.setText("Finish Ride");

                    binding.tvRDTimeStatusNote.setText("Picked-up At");
                    binding.tvRDTimeStatus.setText(Utils.convertTimeTo12HrFormat(deliveryDetails.getPickedUpAt().split(" ")[1]));
                    break;
                case 6:
                    binding.tvRDDeliveryStatus.setText("Delivered");
                    binding.clientInformationBlock.setVisibility(View.VISIBLE);

                    binding.customerInformationBlock.setVisibility(View.VISIBLE);
                    binding.ivRdCallCustomer.setVisibility(View.VISIBLE);

                    binding.paymentInformationBlock.setVisibility(View.VISIBLE);
                    binding.pickupCodeBlock.setVisibility(View.GONE);

                    binding.btnUpdateDeliveryStatus.setVisibility(View.GONE);

                    binding.tvRDTimeStatusNote.setText("Delivered At");
                    binding.tvRDTimeStatus.setText(Utils.convertTimeTo12HrFormat(deliveryDetails.getDeliveredAt().split(" ")[1]));
                    break;
            }
        }

        //client info
        binding.tvRDClientName.setText(deliveryDetails.getClientName());
        binding.tvRDDeliveryId.setText("Request ID: "+deliveryDetails.getDeliveryRequestId());
        binding.tvRDProductType.setText(deliveryDetails.getProductType());
        binding.tvRDPickupAddress.setText(deliveryDetails.getPickUpAddress());

        //customer info
        binding.tvRDCustomerName.setText(deliveryDetails.getCustomerName());
        binding.tvRDCustomerNumber.setText(deliveryDetails.getCustomerNumber());

        if(deliveryDetails.getDeliveryAddressExtra().isEmpty())
         binding.tvRDDelievryAddress.setText(deliveryDetails.getDeliveryArea());
        else
         binding.tvRDDelievryAddress.setText(deliveryDetails.getDeliveryArea()+"\n"+deliveryDetails.getDeliveryAddressExtra());

        //payment info
        binding.tvRDPayment.setText((deliveryDetails.getProductPrice()+deliveryDetails.getDeliveryCharge())+" Taka");
        if(dialogLoading.isShowing())dialogLoading.dismiss();
    }


    void makeCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    void verifyPickupCode(String code){
        dialogLoading.show();
        if(deliveryDetails.getPickupCode().equals(code)) {
            deliveryDetails.setDeliveryStatus(4);
            deliveryDetails.setPickedUpAt(Utils.getCurrentDateTime24HRFormat());
            viewModelRiderApp.updateDeliveryStatusByRequestId(deliveryDetails).observe(this,
                    new Observer<ModelResponse>() {
                        @Override
                        public void onChanged(ModelResponse modelResponse) {
                            if(modelResponse!=null&&modelResponse.getResponse()==1){
                                dialogLoading.dismiss();
                                updateUI(deliveryDetails);
                            }else{
                                dialogLoading.dismiss();
                                Toast.makeText(ActivityDeliveryDetails.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            dialogLoading.dismiss();
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
        }
    }

}