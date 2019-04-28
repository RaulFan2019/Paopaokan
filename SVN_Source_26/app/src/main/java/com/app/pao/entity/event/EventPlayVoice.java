package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/3/8.
 * 播放语音事件
 */
public class EventPlayVoice {

    public int posstion;

    public EventPlayVoice() {

    }

    public EventPlayVoice(int posstion) {
        this.posstion = posstion;
    }

    public int getPosstion() {
        return posstion;
    }

    public void setPosstion(int posstion) {
        this.posstion = posstion;
    }
}
