package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 心率信息存储
 *
 * @author Raul
 */
@Table(name = "heartrate")
public class DBEntityHeartrate extends DBEntityBase {

    @Id
    @NoAutoIncrement
    @Column(column = "heartrateId")
    public long heartrateId;
    @Column(column = "workoutName")
    public String workoutName;// 跑步历史名称
    @Column(column = "lapId")
    public long lapId;// lap id
    @Column(column = "spliteId")
    public long spliteId; // splite id
    @Column(column = "timeofset")
    public long timeofset;// 相对于整个跑步历史的时间偏移
    @Column(column = "bmp")
    public int bmp;// 心率
    @Column(column = "uploadStatus")
    public int uploadStatus;// 上传状态
    @Column(column = "cadence")
    public int cadence;

    public DBEntityHeartrate() {

    }

    public DBEntityHeartrate(long heartrateId, String workoutName, long lapId, long spliteId, long timeofset, int bmp,
                             int uploadStatus,int cadence) {
        super();
        this.heartrateId = heartrateId;
        this.workoutName = workoutName;
        this.timeofset = timeofset;
        this.bmp = bmp;
        this.lapId = lapId;
        this.spliteId = spliteId;
        this.uploadStatus = uploadStatus;
        this.cadence = cadence;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public long getHeartrateId() {
        return heartrateId;
    }

    public void setHeartrateId(long heartrateId) {
        this.heartrateId = heartrateId;
    }

    public long getLapId() {
        return lapId;
    }

    public void setLapId(long lapId) {
        this.lapId = lapId;
    }

    public long getSpliteId() {
        return spliteId;
    }

    public void setSpliteId(long spliteId) {
        this.spliteId = spliteId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public long getTimeofset() {
        return timeofset;
    }

    public void setTimeofset(long timeofset) {
        this.timeofset = timeofset;
    }

    public int getBmp() {
        return bmp;
    }

    public void setBmp(int bmp) {
        this.bmp = bmp;
    }

    public int getCadence() {
        return cadence;
    }

    public void setCadence(int cadence) {
        this.cadence = cadence;
    }
}
