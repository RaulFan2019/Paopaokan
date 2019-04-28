package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * lap 信息
 *
 * @author Raul
 */
@Table(name = "lap")
public class DBEntityLap {

    @Id
    @NoAutoIncrement
    @Column(column = "lapId")
    public long lapId;//Lap的编号
    @Column(column = "starttime")
    public String starttime;// lap 名
    @Column(column = "workoutName")
    public String workoutName;// 跑步历史名称
    @Column(column = "duration")
    public long duration;// 耗时
    @Column(column = "lap")
    public long lap;// lap序号
    @Column(column = "length")
    public float length;// 长度
    @Column(column = "status")
    public int status;// 状态

    public DBEntityLap() {

    }

    public DBEntityLap(long lapId, String starttime, String workoutName, long duration, long lap, float length,
                       int status) {
        super();
        this.lapId = lapId;
        this.starttime = starttime;
        this.workoutName = workoutName;
        this.duration = duration;
        this.lap = lap;
        this.length = length;
        this.status = status;
    }

    public long getLapId() {
        return lapId;
    }

    public void setLapId(long lapId) {
        this.lapId = lapId;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLap() {
        return lap;
    }

    public void setLap(long lap) {
        this.lap = lap;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
