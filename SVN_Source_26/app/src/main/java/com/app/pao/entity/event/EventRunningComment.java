package com.app.pao.entity.event;

import com.app.pao.entity.network.GetRunningUploadResult;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/23.
 * 正在跑步的人收到的点赞或语音地址
 */
public class EventRunningComment {

    public List<GetRunningUploadResult.CommentsEntity> commentsEntities;

    public List<GetRunningUploadResult.CommentsEntity> getCommentsEntities() {
        return commentsEntities;
    }

    public void setCommentsEntities(List<GetRunningUploadResult.CommentsEntity> commentsEntities) {
        this.commentsEntities = commentsEntities;
    }

    public EventRunningComment(List<GetRunningUploadResult.CommentsEntity> commentsEntities) {
        this.commentsEntities = commentsEntities;
    }

    public EventRunningComment() {

    }
}
