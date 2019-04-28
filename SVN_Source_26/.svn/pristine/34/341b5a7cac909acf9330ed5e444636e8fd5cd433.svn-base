package com.app.pao.entity.model;

import java.io.Serializable;

/**
 * 历史位置(显示用)
 *
 * @author Raul
 */
public class HistoryLocEntity implements Serializable {

    public double latitude;// 纬度
    public double longitude;// 经度
    public int speed;// 秒/公里
    public long timeofset;//时间偏移
    public String lapStartTime;//分段开始时间

    public HistoryLocEntity(double latitude, double longitude, int speed, long timeofset,String lapStartTime) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.timeofset = timeofset;
        this.lapStartTime = lapStartTime;
    }

    public String getLapStartTime() {
        return lapStartTime;
    }

    public void setLapStartTime(String lapStartTime) {
        this.lapStartTime = lapStartTime;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimeofset() {
        return timeofset;
    }

    public void setTimeofset(long timeofset) {
        this.timeofset = timeofset;
    }
}
