package com.duarbd.duarriderapp.network;

import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelResponseRider;
import com.duarbd.duarriderapp.model.ModelRider;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("riderLogin.php")
    Observable<ModelResponseRider> riderLogin(@Body  ModelRider rider);

    @POST("getAssignedRideByRiderId.php")
    Observable<List<ModelDeliveryRequest>> getAssignedRide(@Body ModelRider rider);

    @POST("updateAssignedRideStatusById.php")
    Observable<ModelResponse> updateDeliveryStatusByRequestId(@Body  ModelDeliveryRequest request);
}
