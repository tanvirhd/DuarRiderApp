package com.duarbd.duarriderapp.network.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.duarbd.duarriderapp.model.ModelDeliveryRequest;
import com.duarbd.duarriderapp.model.ModelResponse;
import com.duarbd.duarriderapp.model.ModelResponseRider;
import com.duarbd.duarriderapp.model.ModelRider;
import com.duarbd.duarriderapp.model.ModelRiderSalary;
import com.duarbd.duarriderapp.model.ModelTokenFCM;
import com.duarbd.duarriderapp.network.repository.Repository;

import java.util.List;

public class ViewModelRiderApp extends AndroidViewModel {
    private Repository repository;
    public ViewModelRiderApp(@NonNull Application application) {
        super(application);
        repository=new Repository();
    }

    public LiveData<ModelResponseRider> riderLogin(ModelRider rider) {
        return repository.riderLogin(rider);
    }

    public LiveData<List<ModelDeliveryRequest>> getAssignedRide(ModelRider rider){
        return repository.getAssignedRide(rider);
    }

    public LiveData<ModelResponse> updateDeliveryStatusByRequestId(ModelDeliveryRequest request){
        return repository.updateDeliveryStatusByRequestId(request);
    }

    public LiveData<ModelRiderSalary> getRiderSalary(ModelRider rider){
        return repository.getRiderSalary(rider);
    }

    public  LiveData<ModelResponse> collectRiderSalary (ModelRider rider){
        return repository.collectRiderSalary(rider);
    }

    public LiveData<List<ModelDeliveryRequest>> getDeliveryHistoryByRiderId(ModelRider rider){
        return repository.getDeliveryHistoryByRiderId(rider);
    }
    public  LiveData<ModelResponse> updateRiderPassword(ModelRider rider){
        return repository.updateRiderPassword(rider);
    }
    public LiveData<ModelRider> getRiderInformation(ModelRider rider){
        return repository.getRiderInformation(rider);
    }
    public LiveData<ModelResponse> updateTokenFCM(ModelTokenFCM token){
        return repository.updateTokenFCM(token);
    }
    public LiveData<ModelResponse> getClientContactNumber(ModelDeliveryRequest deliveryRequest){
        return repository.getClientContactNumber(deliveryRequest);
    }
}
