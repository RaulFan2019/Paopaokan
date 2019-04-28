package com.app.pao.entity.event;

/**
 * Created by Raul on 2016/1/12.
 * 评论中用户点击事件
 */
public class EventCommentUserClick {
    public int userId;

    public EventCommentUserClick(int userId) {
        this.userId = userId;
    }
}
