package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 *获取跑团活动中的配速分档
 */
public class GetGroupPartyPaceLevelResult {

    List<String> pace;

    public List<String> getPace() {
        return pace;
    }

    public void setPace(List<String> pace) {
        this.pace = pace;
    }
}
