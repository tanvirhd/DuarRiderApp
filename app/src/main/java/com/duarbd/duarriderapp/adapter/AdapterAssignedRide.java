package com.duarbd.duarriderapp.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.interfaces.AdapterAssignedRideCallbacks;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.tools.Utils;

import java.util.List;

public class AdapterAssignedRide extends RecyclerView.Adapter<AdapterAssignedRide.ViewHolderAdapterAssignedRide>{

    private static final String TAG = "AdapterAssignedRide";
    List<ModelDeliveryRequest> assignedRideList;
    Context context;
    AdapterAssignedRideCallbacks callbacks;

    public AdapterAssignedRide(List<ModelDeliveryRequest> assignedRideList, Context context, AdapterAssignedRideCallbacks callbacks) {
        this.assignedRideList = assignedRideList;
        this.context = context;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolderAdapterAssignedRide onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_assigned_ride,parent,false);
        ViewHolderAdapterAssignedRide viewHolderAdapterAssignedRide=new ViewHolderAdapterAssignedRide(view);
        return viewHolderAdapterAssignedRide;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapterAssignedRide holder, int position) {
        ModelDeliveryRequest assignedRide=assignedRideList.get(position);

        holder.tvAS_pickupTime.setText("Pickup Time"+"\n"+
        Utils.addMinute(Utils.getTimeFromDeliveryRequestPlacedDate(assignedRide.getRequestPlacedTime()),assignedRide.getPickupTime()));

        holder.tvAS_pickupFrom.setText("PICKUP FROM:"+"\n"+assignedRide.getPickUpAddress());
        holder.tvAS_deliverTo.setText("DELIVER TO:"+"\n"+assignedRide.getDeliveryArea());

        switch (assignedRide.getDeliveryStatus()){
            case 2:
                holder.onMyWayToPickup.setVisibility(View.VISIBLE);
                holder.tvAS_status.setText("Assigned");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_red_fill,null));
                else holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_red_fill));

                holder.onMyWayToPickup.setVisibility(View.VISIBLE);
                holder.onMyWayToDelivery.setVisibility(View.GONE);
                break;
            case 3:
                holder.onMyWayToPickup.setVisibility(View.GONE);
                holder.onMyWayToDelivery.setVisibility(View.GONE);

                holder.tvAS_status.setText("Picking up");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green,null));
                else holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green));
                break;
            case 4:
                holder.tvAS_status.setText("Received");
                holder.onMyWayToPickup.setVisibility(View.GONE);
                holder.onMyWayToDelivery.setVisibility(View.VISIBLE);
                holder.tvAS_pickupTime.setVisibility(View.GONE);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green,null));
                else holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green));
                break;
            case 5:
                holder.onMyWayToPickup.setVisibility(View.GONE);
                holder.tvAS_status.setText("On my way to delivery");
                holder.onMyWayToPickup.setVisibility(View.GONE);
                holder.onMyWayToDelivery.setVisibility(View.GONE);
                holder.tvAS_pickupTime.setVisibility(View.GONE);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green,null));
                else holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green));
                break;
            case 6:
                holder.onMyWayToPickup.setVisibility(View.GONE);
                holder.tvAS_pickupTime.setVisibility(View.GONE);
                holder.tvAS_status.setText("Delivered");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green,null));
                else holder.tvAS_status.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green));
                break;
        }

        holder.onMyWayToPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onMyWayToPickUpClicked(assignedRideList.get(position));
            }
        });
        holder.onMyWayToDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onMyWayToDeliveryClicked(assignedRideList.get(position));
            }
        });

        holder.conatiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onAssignedRideClicked(assignedRide);
            }
        });


    }

    @Override
    public int getItemCount() {
        return assignedRideList.size();
    }

    class ViewHolderAdapterAssignedRide extends RecyclerView.ViewHolder{
        TextView tvAS_status,tvAS_pickupTime,tvAS_pickupFrom,tvAS_deliverTo,onMyWayToPickup,onMyWayToDelivery;
        ConstraintLayout conatiner;

        public ViewHolderAdapterAssignedRide(@NonNull View itemView) {
            super(itemView);
            tvAS_status=itemView.findViewById(R.id.tvAS_status);
            tvAS_pickupTime=itemView.findViewById(R.id.tvAS_pickupTime);
            tvAS_pickupFrom=itemView.findViewById(R.id.tvAS_pickupFrom);
            tvAS_deliverTo=itemView.findViewById(R.id.tvAS_deliverTo);
            onMyWayToPickup=itemView.findViewById(R.id.onMyWayToPickup);
            onMyWayToDelivery=itemView.findViewById(R.id.onMyWayToDelivery);
            conatiner=itemView.findViewById(R.id.as_Container);
        }
    }
}
