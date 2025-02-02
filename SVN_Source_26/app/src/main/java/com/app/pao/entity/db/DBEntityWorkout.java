package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 跑步历史信息
 *
 * @author Raul
 */
@Table(name = "workout")
public class DBEntityWorkout implements Serializable{

    @Id
    @Column(column = "dbid")
    private String DBID;
    @Column(column = "name")
    public String name;// 跑步历史名
    @Column(column = "starttime")
    public String starttime;// 开始时间
    @Column(column = "workoutId")
    public int workoutId;// 跑步历史id
    @Column(column = "status")
    public int status;// 跑步状态 1. 正在跑 , 2. 跑步结束
    @Column(column = "duration")
    public long duration;// 跑步所花时间
    @Column(column = "length")
    public float length;// 跑步距离
    @Column(column = "calorie")
    public float calorie;// 消耗卡路里
    @Column(column = "maxHeight")
    public double maxHeight;// 最高海拔
    @Column(column = "minHeight")
    public double minHeight;// 最低海拔
    @Column(column = "maxPace")
    public int maxPace;// 最大配速
    @Column(column = "minPace")
    public int minPace;// 最小配速
    @Column(column = "avgPace")
    public int avgPace;// 平均配速
    @Column(column = "maxSpeed")
    public int maxSpeed;// 最大配速
    @Column(column = "avgSpeed")
    public int avgSpeed;// 平均配速(秒/公里)
    @Column(column = "avgheartrate")
    public int avgHeartrate;// 平均心率
    @Column(column = "maxheartrate")
    public int maxHeartrate;// 最大心率
    @Column(column = "minheartrate")
    public int minHeartrate;// 最小心率
    @Column(column = "minSpeed")
    public int minSpeed;// 最小配速
    @Column(column = "userid")
    public int userId;//用户ID
    @Column(column = "startStep")
    public int startStep;
    @Column(column = "endStep")
    public int endStep;


    public DBEntityWorkout() {

    }

    public DBEntityWorkout(long DBIDStr, String name, String starttime, int workoutId,
                           int status, long duration, float length, float calorie,
                           double maxHeight, double minHeight, int maxPace, int minPace, int avgPace,
						   int maxSpeed, int avgSpeed, int avgHeartrate, int maxHeartrate,
                           int minHeartrate, int minSpeed, int userId,int startStep , int endStep) {
        super();
        this.DBID = String.valueOf(DBIDStr);
        this.name = name;
        this.starttime = starttime;
        this.workoutId = workoutId;
        this.status = status;
        this.duration = duration;
        this.length = length;
        this.calorie = calorie;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxPace = maxPace;
        this.minPace = minPace;
        this.avgPace = avgPace;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
        this.avgHeartrate = avgHeartrate;
        this.maxHeartrate = maxHeartrate;
        this.minHeartrate = minHeartrate;
        this.minSpeed = minSpeed;
        this.userId = userId;
        this.startStep = startStep;
        this.endStep = endStep;
    }

    public String getDBID() {
        return DBID;
    }

    public void setDBID(String DBID) {
        this.DBID = DBID;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(int maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public int getMinHeartrate() {
        return minHeartrate;
    }

    public void setMinHeartrate(int minHeartrate) {
        this.minHeartrate = minHeartrate;
    }

    public int getAvgHeartrate() {
        return avgHeartrate;
    }

    public void setAvgHeartrate(int avgHeartrate) {
        this.avgHeartrate = avgHeartrate;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(int avgPace) {
        this.avgPace = avgPace;
    }

    public int getMaxPace() {
        return maxPace;
    }

    public void setMaxPace(int maxPace) {
        this.maxPace = maxPace;
    }

    public int getMinPace() {
        return minPace;
    }

    public void setMinPace(int minPace) {
        this.minPace = minPace;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStartStep() {
        return startStep;
    }

    public void setStartStep(int startStep) {
        this.startStep = startStep;
    }

    public int getEndStep() {
        return endStep;
    }

    public void setEndStep(int endStep) {
        this.endStep = endStep;
    }
}
