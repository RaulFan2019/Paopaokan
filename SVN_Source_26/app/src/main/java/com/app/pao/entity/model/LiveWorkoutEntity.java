package com.app.pao.entity.model;

import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.LiveSocial;

import java.util.List;

/**
 * 直播跑步历史对象
 *
 * @author Raul
 */
public class LiveWorkoutEntity {

    public int id;// 跑步历史id
    public String starttime;// 开始时间
    public int status;// 状态
    public long duration;// 耗时
    public float length;// 长度
    public double calorie;// 卡路里
    public List<LiveLap> lap;
    public List<LiveSplite> splits;// 分段信息
    public int thumbupcount;
    public int hasGiveThumbup;
    public List<LiveSocial> social;//评论和点赞列表
    public List<GetPlaybackCameraListResult.VideoEntity> video;

    public LiveWorkoutEntity() {

    }

    public LiveWorkoutEntity(int id, String starttime, int status, long duration, float length, int calorie,
                             List<LiveLap> lap, List<LiveSplite> splits, int thumbupcount, int hasGiveThumbup,
                             List<GetPlaybackCameraListResult.VideoEntity> video) {
        super();
        this.id = id;
        this.starttime = starttime;
        this.status = status;
        this.duration = duration;
        this.length = length;
        this.calorie = calorie;
        this.lap = lap;
        this.splits = splits;
        this.thumbupcount = thumbupcount;
        this.hasGiveThumbup = hasGiveThumbup;
        this.video = video;
    }

    public List<LiveSocial> getSocial() {
        return social;
    }

    public void setSocial(List<LiveSocial> social) {
        this.social = social;
    }

    public int getThumbupcount() {
        return thumbupcount;
    }

    public void setThumbupcount(int thumbupcount) {
        this.thumbupcount = thumbupcount;
    }

    public int getHasGiveThumbup() {
        return hasGiveThumbup;
    }

    public void setHasGiveThumbup(int hasGiveThumbup) {
        this.hasGiveThumbup = hasGiveThumbup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
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

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public List<LiveLap> getLap() {
        return lap;
    }

    public void setLap(List<LiveLap> lap) {
        this.lap = lap;
    }

    public List<LiveSplite> getSplits() {
        return splits;
    }

    public void setSplits(List<LiveSplite> splits) {
        this.splits = splits;
    }

    public List<GetPlaybackCameraListResult.VideoEntity> getVideo() {
        return video;
    }

    public void setVideo(List<GetPlaybackCameraListResult.VideoEntity> video) {
        this.video = video;
    }
}
