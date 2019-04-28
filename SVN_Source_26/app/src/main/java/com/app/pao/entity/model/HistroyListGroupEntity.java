package com.app.pao.entity.model;

/**
 * Created by Raul on 2015/11/29.
 * 动态列表父对象
 */
public class HistroyListGroupEntity {
    private String timeStr;// 时间内容

    public HistroyListGroupEntity(){

    }
    public HistroyListGroupEntity(String timeStr) {
        super();
        this.timeStr = timeStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
