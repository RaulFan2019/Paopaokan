package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Raul.Fan on 2016/3/15.
 * 原始定位数据
 */
@Table(name = "originalPosition")
public class DBEntityOriginalPosition {

    @Id
    @NoAutoIncrement
    @Column(column = "location_id")
    private long id;//位置id
    @Column(column = "starttime")
    private String starttime;//跑步历史开始时间
    @Column(column = "latitude")
    private double latitude;//纬度
    @Column(column = "longitude")
    private double longitude;//经度
    @Column(column = "haccuracy")
    private float haccuracy;// 精度
    @Column(column = "timeoffset")
    private long timeoffset;//相对于跑步开始的时间
    @Column(column = "errorcode")
    private int errorcode;//定位错误码
    @Column(column = "uploadStatus")
    public int uploadStatus;//上传状态


    public DBEntityOriginalPosition() {

    }

    public DBEntityOriginalPosition(long id, String starttime, double latitude, double longitude, float haccuracy, long timeoffset, int errorcode, int uploadStatus) {
        this.id = id;
        this.starttime = starttime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.haccuracy = haccuracy;
        this.timeoffset = timeoffset;
        this.errorcode = errorcode;
        this.uploadStatus = uploadStatus;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
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

    public long getTimeoffset() {
        return timeoffset;
    }

    public void setTimeoffset(long timeoffset) {
        this.timeoffset = timeoffset;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
