package com.duarbd.duarriderapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRider {
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

    @SerializedName("password")
    @Expose
    private String password;

    public ModelRider(String riderid, String riderName, String riderContactNumber, String vehicleType, String password) {
        this.riderid = riderid;
        this.riderName = riderName;
        this.riderContactNumber = riderContactNumber;
        this.vehicleType = vehicleType;
        this.password = password;
    }

    public ModelRider(String riderid, String password) {
        this.riderid = riderid;
        this.password = password;
    }

    public ModelRider(String riderid) {
        this.riderid = riderid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
