package com.duarbd.duarriderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.interfaces.AdapterRideHistoryCallback;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;

import java.util.List;

public class AdapterRideHistory extends RecyclerView.Adapter<AdapterRideHistory.ViewHolderAdapterRideHistory>{

    List<ModelDeliveryRequest> deliveryList;
    Context context;
    AdapterRideHistoryCallback callback;

    public AdapterRideHistory(List<ModelDeliveryRequest> deliveryList, Context context, AdapterRideHistoryCallback callback) {
        this.deliveryList = deliveryList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolderAdapterRideHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_delivery_history,parent,false);
        ViewHolderAdapterRideHistory viewHolderAdapterRideHistory=new ViewHolderAdapterRideHistory(view);
        return  viewHolderAdapterRideHistory;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapterRideHistory holder, int position) {
        ModelDeliveryRequest delivery=deliveryList.get(position);
        holder.tvdh_deliveryid.setText("Ride ID: "+delivery.getDeliveryRequestId());
        holder.tvdh_client.setText("Delivery of: "+delivery.getClientName());
        holder.tvdh_price.setText("Bill: "+delivery.getProductPrice()+" Tk");
        holder.tvdh_deliverycharge.setText("Delivery Charge: "+delivery.getDeliveryCharge()+" Tk");

        holder.tvdh_vieworderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMoreClicked(delivery);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    class  ViewHolderAdapterRideHistory extends RecyclerView.ViewHolder{
        TextView tvdh_deliveryid,tvdh_price,tvdh_deliverycharge,tvdh_client,tvdh_vieworderdetails;
        public ViewHolderAdapterRideHistory(@NonNull View itemView) {
            super(itemView);
            tvdh_deliveryid=itemView.findViewById(R.id.tvdh_deliveryid);
            tvdh_price=itemView.findViewById(R.id.tvdh_price);
            tvdh_deliverycharge=itemView.findViewById(R.id.tvdh_deliverycharge);
            tvdh_client=itemView.findViewById(R.id.tvdh_client);
            tvdh_vieworderdetails=itemView.findViewById(R.id.tvdh_vieworderdetails);
        }
    }
}
