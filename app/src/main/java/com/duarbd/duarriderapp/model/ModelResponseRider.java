package com.duarbd.duarriderapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelResponseRider {
    @SerializedName("riderid")
    @Expose
    private  String riderid;

    @SerializedName("riderName")
    @Expose
    private String riderName;

    @SerializedName("riderContactNumber")
    @Expose
    private String riderContactNumber;

    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    @SerializedName("response")
    @Expose
    private Integer response;

    @SerializedName("status")
    @Expose
    private Integer status;

    public ModelResponseRider(String riderid, String riderName, String riderContactNumber, String vehicleType, Integer response, Integer status) {
        this.riderid = riderid;
        this.riderName = riderName;
        this.riderContactNumber = riderContactNumber;
        this.vehicleType = vehicleType;
        this.response = response;
        this.status = status;
    }

    public String getRiderid() {
        return riderid;
    }

    public void setRiderid(String riderid) {
        this.riderid = riderid;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderContactNumber() {
        return riderContactNumber;
    }

    public void setRiderContactNumber(String riderContactNumber) {
        this.riderContactNumber = riderContactNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
