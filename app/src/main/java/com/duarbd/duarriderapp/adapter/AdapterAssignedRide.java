package com.duarbd.duarriderapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
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
        Log.d(TAG, "onBindViewHolder:"+Utils.addMinute(
                Utils.getTimeFromDeliveryRequestPlacedDate(assignedRide.getRequestPlacedAt()),
                assignedRide.getPickupTime()
        ));

        holder.tvAs_ClientName.setText(assignedRide.getClientName());
        holder.tvAS_productType.setText(assignedRide.getProductType());
        holder.tvAS_pickupFrom.setText(assignedRide.getPickUpAddress());

        if(assignedRide.getDeliveryAddressExtra().isEmpty()){
            holder.tvAS_deliverTo.setText(assignedRide.getDeliveryArea());
        }else {
            holder.tvAS_deliverTo.setText(assignedRide.getDeliveryArea()+"\n"+assignedRide.getDeliveryAddressExtra());
        }

        if(assignedRide.isOnHold()==1){
            holder.tvAS_infoBlock.setVisibility(View.GONE);
            holder.tvAS_timeBlock.setVisibility(View.GONE);
            holder.tvAS_onHold.setVisibility(View.VISIBLE);
        }else {
            holder.tvAS_infoBlock.setVisibility(View.VISIBLE);
            holder.tvAS_onHold.setVisibility(View.GONE);
            switch (assignedRide.getDeliveryStatus()){
                case 2:
                    holder.tvAS_status.setText("New Ride Assigned");

                    holder.tvAS_timeBlock.setVisibility(View.VISIBLE);
                    holder.tvAS_timeStatusNote.setText("Pick-up Time");
                    holder.tvAS_timeStatus.setText(
                            Utils.addMinute(
                                    Utils.getTimeFromDeliveryRequestPlacedDate(assignedRide.getRequestPlacedAt()),
                                    assignedRide.getPickupTime()
                            )
                    );
                    break;
                case 3:

                    holder.tvAS_status.setText("Picking up");

                    holder.tvAS_timeBlock.setVisibility(View.VISIBLE);
                    holder.tvAS_timeStatusNote.setText("Pick-up Time");
                    holder.tvAS_timeStatus.setText(
                            Utils.addMinute(
                                    Utils.getTimeFromDeliveryRequestPlacedDate(assignedRide.getRequestPlacedAt()),
                                    assignedRide.getPickupTime()
                            )
                    );
                    break;
                case 4:
                    holder.tvAS_status.setText("Received");

                    holder.tvAS_timeBlock.setVisibility(View.GONE);
                    //holder.tvAS_timeStatusNote.setText("Picked-up At");
                    //holder.tvAS_timeStatus.setText(Utils.convertTimeTo12HrFormat(assignedRide.getPickedUpAt().split(" ")[1]));
                    break;
                case 5:
                    holder.tvAS_status.setText("On my way to delivery");

                    holder.tvAS_timeBlock.setVisibility(View.GONE);
                    //holder.tvAS_timeStatusNote.setText("Picked-up At");
                    //holder.tvAS_timeStatus.setText(Utils.convertTimeTo12HrFormat(assignedRide.getPickedUpAt().split(" ")[1]));
                    break;
                case 6:
                    holder.tvAS_status.setText("Delivered");

                    holder.tvAS_timeBlock.setVisibility(View.GONE);
                    //holder.tvAS_timeStatusNote.setText("Delivered At");
                    //holder.tvAS_timeStatus.setText(Utils.convertTimeTo12HrFormat(assignedRide.getDeliveredAt().split(" ")[1]));
                    break;
            }
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
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
        TextView tvAS_status,tvAS_timeStatus,tvAS_timeStatusNote,tvAS_pickupFrom,
                 tvAS_deliverTo,tvAs_ClientName,tvAS_productType,tvAS_onHold;

        CardView container;
        Group tvAS_timeBlock,tvAS_infoBlock;

        public ViewHolderAdapterAssignedRide(@NonNull View itemView) {
            super(itemView);
            tvAS_status=itemView.findViewById(R.id.tvAS_status);
            tvAS_timeStatus=itemView.findViewById(R.id.tvAS_timeStatus);
            tvAS_pickupFrom=itemView.findViewById(R.id.tvAS_pickupFrom);
            tvAS_deliverTo=itemView.findViewById(R.id.tvAS_deliverTo);
            tvAS_timeStatusNote=itemView.findViewById(R.id.tvAS_timeStatusNote);
            container =itemView.findViewById(R.id.as_Container);
            tvAs_ClientName=itemView.findViewById(R.id.tvAS_clientName);
            tvAS_productType=itemView.findViewById(R.id.tvAS_productType);
            tvAS_onHold=itemView.findViewById(R.id.tvAS_onHold);
            tvAS_timeBlock=itemView.findViewById(R.id.tvAS_timeBlock);
            tvAS_infoBlock=itemView.findViewById(R.id.tvAS_infoBlock);
        }
    }
}
