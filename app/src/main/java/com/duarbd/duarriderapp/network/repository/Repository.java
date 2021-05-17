package com.duarbd.duarriderapp.network.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelResponseRider;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.model.ModelRiderSalary;
import com.duarbd.duarriderapp.network.ApiClient;
import com.duarbd.duarriderapp.network.ApiInterface;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class Repository {
    private static final String TAG = "Repository";
    private  ApiInterface apiRequest;

    public Repository() {
        apiRequest=ApiClient.getApiInterface();
    }

    public LiveData<ModelResponseRider> riderLogin(ModelRider rider) {
        MutableLiveData<ModelResponseRider> result = new MutableLiveData<>();
        apiRequest.riderLogin(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelResponseRider>() {
                    @Override
                    public void accept(ModelResponseRider modelResponseRider) throws Exception {
                        result.postValue(modelResponseRider);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "riderLogin: error:" + throwable.getMessage());
                    }
                });
        return  result;
    }

    public LiveData<List<ModelDeliveryRequest>> getAssignedRide(ModelRider rider){
        MutableLiveData<List<ModelDeliveryRequest>> result=new MutableLiveData<>();
        apiRequest.getAssignedRide(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ModelDeliveryRequest>>() {
                    @Override
                    public void accept(List<ModelDeliveryRequest> modelDeliveryRequests) throws Exception {
                        result.postValue(modelDeliveryRequests);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "getAssignedRide: error"+throwable.getMessage());
                    }
                });
        return result;
    }

    public LiveData<ModelResponse> updateDeliveryStatusByRequestId(ModelDeliveryRequest request){
        MutableLiveData<ModelResponse> result=new MutableLiveData<>();
        apiRequest.updateDeliveryStatusByRequestId(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelResponse>() {
                    @Override
                    public void accept(ModelResponse modelResponse) throws Exception {
                        result.postValue(modelResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "updateDeliveryStatusByRequestId: error:"+throwable.getMessage());
                    }
                });
        return result;
    }

    public LiveData<ModelRiderSalary> getRiderSalary(ModelRider rider){
        MutableLiveData<ModelRiderSalary> result =new MutableLiveData<>();
        apiRequest.getRiderSalary(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelRiderSalary>() {
                    @Override
                    public void accept(ModelRiderSalary modelRiderSalary) throws Exception {
                        result.postValue(modelRiderSalary);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "getRiderSalary: error:"+throwable.getMessage());
                    }
                });
        return  result;
    }

    public  LiveData<ModelResponse> collectRiderSalary (ModelRider rider){
        MutableLiveData<ModelResponse> result =new MutableLiveData<>();
        apiRequest.collectRiderSalary(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelResponse>() {
                    @Override
                    public void accept(ModelResponse modelResponse) throws Exception {
                        result.postValue(modelResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "collectRiderSalary: error:"+throwable.getMessage());
                    }
                });
        return result;
    }

    public LiveData<List<ModelDeliveryRequest>> getDeliveryHistoryByRiderId(ModelRider rider){
        MutableLiveData<List<ModelDeliveryRequest>> result=new MutableLiveData<>();
        apiRequest.getDeliveryHistoryByRiderId(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ModelDeliveryRequest>>() {
                    @Override
                    public void accept(List<ModelDeliveryRequest> modelDeliveryRequests) throws Exception {
                        result.postValue(modelDeliveryRequests);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "getDeliveryHistoryByRiderId: error:"+throwable.getMessage());
                    }
                });
        return  result;
    }

    public  LiveData<ModelResponse> updateRiderPassword(ModelRider rider){
        MutableLiveData<ModelResponse> result=new MutableLiveData<>();
        apiRequest.updateRiderPassword(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelResponse>() {
                    @Override
                    public void accept(ModelResponse modelResponse) throws Exception {
                        result.postValue(modelResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "updateRiderPassword: error:"+throwable.getMessage());
                    }
                });
        return result;
    }

    public LiveData<ModelRider> getRiderInformation(ModelRider rider){
        MutableLiveData<ModelRider> result=new MutableLiveData<>();
        apiRequest.getRiderInformation(rider).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModelRider>() {
                    @Override
                    public void accept(ModelRider rider) throws Exception {
                        result.postValue(rider);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "getRiderInformation: error:"+throwable.getMessage());
                    }
                });
        return result;
    }
}
