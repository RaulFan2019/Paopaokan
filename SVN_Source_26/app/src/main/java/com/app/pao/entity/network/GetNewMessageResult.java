package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/12/3.
 * 一条新的消息结构
 */
public class GetNewMessageResult {

    /**
     * id : 1
     * fromuserid : 10443
     * fromusernickname : 俊男
     * fromuseravatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-10-31_5634be6ed179f.jpg
     * fromusergender : 1
     * sendtime : 2015-11-17 22:21:31
     * status : 1
     * type : 1
     * extra :
     * message : 俊男申请添加你为好友
     */

    private long id;
    private int fromuserid;
    private String fromusernickname;
    private String fromuseravatar;
    private int fromusergender;
    private String sendtime;
    private int status;
    private int type;
    private String extra;
    private String message;
    

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
}
