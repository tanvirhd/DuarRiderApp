package com.duarbd.duarriderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.interfaces.AdapterDailyClearanceCallback;
import com.duarbd.duarriderapp.model.ModelDeliveryRequest;

import java.util.List;

public class AdapterDailyClearance extends  RecyclerView.Adapter<AdapterDailyClearance.ViewHolderAdapterDailyClearance>{

    List<ModelDeliveryRequest> deliveryList;
    Context context;
    AdapterDailyClearanceCallback callback;

    public AdapterDailyClearance(List<ModelDeliveryRequest> deliveryList, Context context, AdapterDailyClearanceCallback callback) {
        this.deliveryList = deliveryList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolderAdapterDailyClearance onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_delivery_clearance,parent,false);
        ViewHolderAdapterDailyClearance viewHolderAdapterDailyClearance=new ViewHolderAdapterDailyClearance(view);
        return viewHolderAdapterDailyClearance;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapterDailyClearance holder, int position) {
        ModelDeliveryRequest delivery=deliveryList.get(position);
        holder.tvdc_deliveryid.setText("Ride ID: "+delivery.getDeliveryRequestId());
        holder.tvdc_client.setText("Delivery of: "+delivery.getClientName());
        holder.tvdc_price.setText("Bill: "+delivery.getProductPrice()+" Tk");
        holder.tvdc_deliverycharge.setText("Delivery Charge: "+delivery.getDeliveryCharge()+" Tk");

        if(delivery.getRiderClearance().equals("due")) {
            holder.tvoc_clearance.setText("Clearance: due");
            holder.tvoc_clearance.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_red_fill));
        } else {
            holder.tvoc_clearance.setText("Clearance: ok");
            holder.tvoc_clearance.setBackground(context.getResources().getDrawable(R.drawable.bg_cr8_green));
        }

        holder.tvdc_vieworderdetails.setOnClickListener(new View.OnClickListener() {
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

    class ViewHolderAdapterDailyClearance extends RecyclerView.ViewHolder{
        TextView tvdc_deliveryid,tvdc_price,tvdc_deliverycharge,tvdc_client,tvdc_vieworderdetails,tvoc_clearance;
        public ViewHolderAdapterDailyClearance(@NonNull View itemView) {
            super(itemView);
            tvdc_deliveryid=itemView.findViewById(R.id.tvdc_deliveryid);
            tvdc_price=itemView.findViewById(R.id.tvdc_price);
            tvdc_deliverycharge=itemView.findViewById(R.id.tvdc_deliverycharge);
            tvdc_client=itemView.findViewById(R.id.tvdc_client);
            tvdc_vieworderdetails=itemView.findViewById(R.id.tvdc_vieworderdetails);
            tvoc_clearance=itemView.findViewById(R.id.tvoc_clearance);
        }
    }
}
