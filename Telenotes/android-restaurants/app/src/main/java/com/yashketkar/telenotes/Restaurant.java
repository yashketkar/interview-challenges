package com.yashketkar.telenotes;

import java.io.Serializable;

/**
 * Created by yashketkar on 4/23/2016.
 */

public class Restaurant implements Serializable {
    public String mName;
    public String mAddress;
    public String mRating;
    public String mType;
    public String mtel;
    public String mwebsite;
    public String memail;

    public double getMlatitude() {
        return mlatitude;
    }

    public void setMlatitude(double mlatitude) {
        this.mlatitude = mlatitude;
    }

    public double getMlongitude() {
        return mlongitude;
    }

    public void setMlongitude(double mlongitude) {
        this.mlongitude = mlongitude;
    }

    public double mlatitude;
    public double mlongitude;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getMtel() {
        return mtel;
    }

    public void setMtel(String mtel) {
        this.mtel = mtel;
    }

    public String getMwebsite() {
        return mwebsite;
    }

    public void setMwebsite(String mwebsite) {
        this.mwebsite = mwebsite;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }
}
