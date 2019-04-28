package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/11/24.
 * 上传跑步历史返回的信息
 */
public class GetUploadWorkoutResult {


    /**
     * id : 3884689
     * newWorkout : 0,old, 1,new
     * needNotifyBeginRunning : 0
     */

    private int id;
    private int newWorkout;//是否是新的workout
    private int needNotifyBeginRunning;

    public void setId(int id) {
        this.id = id;
    }

    public void setNewWorkout(int newWorkout) {
        this.newWorkout = newWorkout;
    }

    public void setNeedNotifyBeginRunning(int needNotifyBeginRunning) {
        this.needNotifyBeginRunning = needNotifyBeginRunning;
    }

    public int getId() {
        return id;
    }

    public int getNewWorkout() {
        return newWorkout;
    }

    public int getNeedNotifyBeginRunning() {
        return needNotifyBeginRunning;
    }
}
