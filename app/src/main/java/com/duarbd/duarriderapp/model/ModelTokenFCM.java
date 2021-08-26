package com.duarbd.duarriderapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelTokenFCM {
    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("tokencategory")
    @Expose
    private String tokencategory;

    @SerializedName("token")
    @Expose
    private String token;

    public ModelTokenFCM(String token) {
        this.token = token;
    }

    public ModelTokenFCM(String uid, String tokencategory, String token) {
        this.uid = uid;
        this.tokencategory = tokencategory;
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTokencategory() {
        return tokencategory;
    }

    public void setTokencategory(String tokencategory) {
        this.tokencategory = tokencategory;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
