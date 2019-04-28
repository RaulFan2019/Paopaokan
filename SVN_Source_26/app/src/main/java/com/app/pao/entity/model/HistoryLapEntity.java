package com.app.pao.entity.model;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Raul on 2016/2/4.
 */
public class HistoryLapEntity implements Serializable{

    private String LapStartTime;
    private String startLatLng;
    private String endLatLng;

    public HistoryLapEntity() {

    }

    public HistoryLapEntity(String lapStartTime) {
        this.LapStartTime = lapStartTime;
        this.startLatLng = null;
        this.endLatLng = null;
    }


    public HistoryLapEntity(String lapStartTime, String startLatLng, String endLatLng) {
        super();
        LapStartTime = lapStartTime;
        this.startLatLng = startLatLng;
        this.endLatLng = endLatLng;
    }

    public String getLapStartTime() {
        return LapStartTime;
    }

    public void setLapStartTime(String lapStartTime) {
        LapStartTime = lapStartTime;
    }

    public String getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(String startLatLng) {
        this.startLatLng = startLatLng;
    }

    public String getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(String endLatLng) {
        this.endLatLng = endLatLng;
    }
}
