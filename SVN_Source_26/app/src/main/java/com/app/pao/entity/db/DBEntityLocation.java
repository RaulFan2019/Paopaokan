package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 地址信息保存
 *
 * @author Raul
 */
@Table(name = "location")
public class DBEntityLocation implements Serializable {

    @Id
    @NoAutoIncrement
    @Column(column = "location_id")
    public long id;//位置id
    @Column(column = "workoutName")
    public String workoutName;//跑步历史名称
    @Column(column = "lapStartTime")
    public String lapStartTime;//Lap名称
    @Column(column = "latitude")
    public double latitude;//纬度
    @Column(column = "longitude")
    public double longitude;//经度
    @Column(column = "haccuracy")
    public float haccuracy;// 水平精度
    @Column(column = "altitude")
    public double altitude;// 海拔
    @Column(column = "vaccuracy")
    public float vaccuracy;// 垂直精度
    @Column(column = "timeoffset")
    public long timeoffset;//相对时间
    @Column(column = "speed")
    public int speed;// 秒/公里
    @Column(column = "uploadStatus")
    public int uploadStatus;//上传状态
    @Column(column = "originallatitude")
    public double originallatitude;//原始纬度
    @Column(column = "originallongitude")
    public double originallongitude;//原始经度

    public DBEntityLocation() {

    }

    public DBEntityLocation(long id, String workoutName, String lapStartTime, double latitude, double longitude,
                            double altitude, float haccuracy, float vaccuracy, long timeoffset, int speed, int
                                    uploadStatus
            , double originallatitude, double originallongitude
    ) {
        super();
        this.id = id;
        this.workoutName = workoutName;
        this.lapStartTime = lapStartTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.haccuracy = haccuracy;
        this.altitude = altitude;
        this.vaccuracy = vaccuracy;
        this.timeoffset = timeoffset;
        this.speed = speed;
        this.uploadStatus = uploadStatus;
        this.originallatitude = originallatitude;
        this.originallongitude = originallongitude;
    }

    public double getOriginallatitude() {
        return originallatitude;
    }

    public void setOriginallatitude(double originallatitude) {
        this.originallatitude = originallatitude;
    }

    public double getOriginallongitude() {
        return originallongitude;
    }

    public void setOriginallongitude(double originallongitude) {
        this.originallongitude = originallongitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getLapStartTime() {
        return lapStartTime;
    }

    public void setLapStartTime(String lapStartTime) {
        this.lapStartTime = lapStartTime;
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

    public float getHaccuracy() {
        return haccuracy;
    }

    public void setHaccuracy(float haccuracy) {
        this.haccuracy = haccuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getVaccuracy() {
        return vaccuracy;
    }

    public void setVaccuracy(float vaccuracy) {
        this.vaccuracy = vaccuracy;
    }

    public long getTimeoffset() {
        return timeoffset;
    }

    public void setTimeoffset(long timeoffset) {
        this.timeoffset = timeoffset;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
