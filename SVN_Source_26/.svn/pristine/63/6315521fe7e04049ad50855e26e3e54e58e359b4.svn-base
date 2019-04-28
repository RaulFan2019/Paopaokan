package com.app.pao.entity.model;

import java.io.Serializable;
import java.util.List;

/**
 * 历史信息(显示使用)
 *
 * @author Raul
 */
public class HistoryInfoEntity implements Serializable {

    public int workoutid;
    public int userId;//用户Id
    public String name;//跑步历史名称
    public int avgPace;// 平均配速
    public float length;// 跑步距离
    public long duration;// 跑步所花时间
    public float calorie;// 消耗卡路里
    public String avatar;//头像
    public int avgHeartrate;//平均心率

    public List<HistoryLocEntity> locList;// 点的集合
    public List<HistoryHeartrateEntity> heartrateList;// 心率集合
    public List<HistorySpliteEntity> spliteList;// 分段集合
    public List<HistoryLapEntity> lapList;

    public HistoryInfoEntity(int workoutid, String name, int avgPace, float length, long duration, float calorie,
                             String avatar,
                             List<HistoryLocEntity> locList,
                             List<HistoryHeartrateEntity> heartrateList, List<HistorySpliteEntity> spliteList, int
                                     avgHeartrate,List<HistoryLapEntity> lapList) {
        super();
        this.workoutid = workoutid;
        this.avgPace = avgPace;
        this.length = length;
        this.duration = duration;
        this.calorie = calorie;
        this.locList = locList;
        this.heartrateList = heartrateList;
        this.spliteList = spliteList;
        this.name = name;
        this.avatar = avatar;
        this.avgHeartrate = avgHeartrate;
        this.lapList = lapList;
    }

    public List<HistoryLapEntity> getLapList() {
        return lapList;
    }

    public void setLapList(List<HistoryLapEntity> lapList) {
        this.lapList = lapList;
    }

    public int getAvgHeartrate() {
        return avgHeartrate;
    }

    public void setAvgHeartrate(int avgHeartrate) {
        this.avgHeartrate = avgHeartrate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public List<HistorySpliteEntity> getSpliteList() {
        return spliteList;
    }

    public void setSpliteList(List<HistorySpliteEntity> spliteList) {
        this.spliteList = spliteList;
    }

    public List<HistoryHeartrateEntity> getHeartrateList() {
        return heartrateList;
    }

    public void setHeartrateList(List<HistoryHeartrateEntity> heartrateList) {
        this.heartrateList = heartrateList;
    }

    public List<HistoryLocEntity> getLocList() {
        return locList;
    }

    public void setLocList(List<HistoryLocEntity> locList) {
        this.locList = locList;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
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

    public int getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(int avgPace) {
        this.avgPace = avgPace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
