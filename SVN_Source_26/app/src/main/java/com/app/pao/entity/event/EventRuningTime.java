package com.app.pao.entity.event;

import java.io.Serializable;

/**
 * EventBus 传跑步时间
 */
public class EventRuningTime implements Serializable{

    private long time;//跑步时间
    private String workoutName;//跑步名称

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public EventRuningTime(long time) {
        this.time = time;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public EventRuningTime(long time, String workoutName) {
        this.workoutName = workoutName;
        this.time = time;
    }
}
