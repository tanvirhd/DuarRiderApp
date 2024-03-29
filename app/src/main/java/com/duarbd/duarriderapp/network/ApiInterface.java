package com.duarbd.duarriderapp.network;

import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelResponseRider;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.model.ModelRiderSalary;
import com.duarbd.duarriderapp.model.ModelTokenFCM;


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

    @POST("getDeliveryHistoryByRiderid.php")//ready
    Observable<List<ModelDeliveryRequest>> getDeliveryHistoryByRiderId(@Body ModelRider rider);

    @POST("getRiderPayment.php")
    Observable<ModelRiderSalary> getRiderSalary(@Body ModelRider rider);

    @POST("collectRiderPayment.php")
    Observable<ModelResponse> collectRiderSalary(@Body ModelRider rider);

    @POST("updateRiderPassword.php")
    Observable<ModelResponse> updateRiderPassword(@Body ModelRider rider);

    @POST("getRiderInformationById.php")
    Observable<ModelRider> getRiderInformation(@Body ModelRider rider);

    @POST("storeFCMToken.php")
    Observable<ModelResponse> updateTokenFCM(@Body ModelTokenFCM token);

    @POST("getClientNumber.php")
    Observable<ModelResponse> getClientContactNumber(@Body ModelDeliveryRequest deliveryRequest);
}
