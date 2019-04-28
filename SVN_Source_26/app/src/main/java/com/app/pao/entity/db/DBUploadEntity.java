package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Raul on 2015/10/30.
 * 需要上传给服务器的数据
 */
@Table(name = "DBUploadEntity")
public class DBUploadEntity extends DBEntityBase {

    public static final int TYPE_UPLOAD_WORKOUT = 1;
    public static final int TYPE_DELETE_WORTOUT = 2;
    public static final int TYPE_THUMB_UP = 3;
    public static final int TYPE_READ_MESSAGE = 4;//收到消息
    public static final int TYPE_THUMB_UP_YES = 5;//点赞
    public static final int TYPE_THUMB_UP_NO = 6;//取消点赞
    public static final int TYPE_COMMENTS = 7;//评论
    public static final int TYPE_UPLOAD_ORIGINAL_POSITION = 8;

    @Column(column = "uploadType")
    public int uploadType;//上传内容类型
    @Column(column = "url")
    public String url;//上传给服务器的url地址
    @Column(column = "info")
    public String info;//上传数据的内容
    @Column(column = "workoutName")
    public String workoutName;//若上传的是跑步历史,跑步历史名称
    @Column(column = "userId")
    public int userId;

    public DBUploadEntity() {

    }

    public DBUploadEntity(int uploadType, String url, String info, String workoutName, int userId) {
        super();
        this.uploadType = uploadType;
        this.url = url;
        this.info = info;
        this.workoutName = workoutName;
        this.userId = userId;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
