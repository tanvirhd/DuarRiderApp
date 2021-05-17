package com.duarbd.duarriderapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRiderSalary {
    @SerializedName("riderid")
    @Expose
    private String riderid;
    @SerializedName("riderName")
    @Expose
    private String riderName;
    @SerializedName("riderSalary")
    @Expose
    private String riderSalary;

    @SerializedName("riderPass")
    @Expose
    private String riderPass;

    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("status")
    @Expose
    private String status;

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

    public String getRiderSalary() {
        return riderSalary;
    }

    public void setRiderSalary(String riderSalary) {
        this.riderSalary = riderSalary;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRiderPass() {
        return riderPass;
    }

    public void setRiderPass(String riderPass) {
        this.riderPass = riderPass;
    }
}
