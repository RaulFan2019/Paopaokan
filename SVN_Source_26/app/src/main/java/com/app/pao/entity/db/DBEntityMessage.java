package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Raul on 2015/11/30.
 * <p/>
 * 消息存储模型
 */
@Table(name = "message")
public class DBEntityMessage {

    @Id
    @Column(column = "mid")
    private int mid;//本地id
    @Column(column = "id")
    private long id;//messageid
    @Column(column = "fromuserid")
    private int fromuserid;//来自user
    @Column(column = "fromusernickname")
    private String fromusernickname;//来自user昵称
    @Column(column = "fromuseravatar")
    public String fromuseravatar;//来自user头像
    @Column(column = "fromusergender")
    private int fromusergender;//来自user性别
    @Column(column = "sendtime")
    private String sendtime;//发送时间
    @Column(column = "status")
    private int status;//读取状态
    @Column(column = "type")
    private int type;//类型
    @Column(column = "extra")
    private String extra;//附加信息
    @Column(column = "message")
    private String message;//消息内容
    @Column(column = "userid")
    private int userid;//用户id
    @Column(column = "showstatus")
    private int showStatus;
    @Column(column = "workoutid")
    private int workoutId;
    @Column(column = "workoutstarttime")
    private String workoutStarttime;
    @Column(column = "groupid")
    public int groupid;

    public DBEntityMessage() {

    }

    public DBEntityMessage(long id, int fromuserid, String fromusernickname, String fromuseravatar, int
            fromusergender, String sendtime, int status, int type, String extra, String message,
                           int showStatus, int workoutId, String workoutStarttime,int groupid) {
        super();
        this.id = id;
        this.fromuserid = fromuserid;
        this.fromusernickname = fromusernickname;
        this.fromuseravatar = fromuseravatar;
        this.fromusergender = fromusergender;
        this.sendtime = sendtime;
        this.status = status;
        this.type = type;
        this.extra = extra;
        this.message = message;
        this.showStatus = showStatus;
        this.workoutId = workoutId;
        this.workoutStarttime = workoutStarttime;
        this.groupid = groupid;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(int fromuserid) {
        this.fromuserid = fromuserid;
    }

    public String getFromusernickname() {
        return fromusernickname;
    }

    public void setFromusernickname(String fromusernickname) {
        this.fromusernickname = fromusernickname;
    }

    public String getFromuseravatar() {
        return fromuseravatar;
    }

    public void setFromuseravatar(String fromuseravatar) {
        this.fromuseravatar = fromuseravatar;
    }

    public int getFromusergender() {
        return fromusergender;
    }

    public void setFromusergender(int fromusergender) {
        this.fromusergender = fromusergender;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutStarttime() {
        return workoutStarttime;
    }

    public void setWorkoutStarttime(String workoutStarttime) {
        this.workoutStarttime = workoutStarttime;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
}
