package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/11/30.
 * 获取消息列表结果
 */
public class GetMessageListResult {


    /**
     * message : [{"id":"1","fromuserid":"10443","fromusernickname":"俊男","fromuseravatar":"http://7xk0si.com1.z0.glb
     * .clouddn.com/2015-10-31_5634be6ed179f.jpg","fromusergender":"1","sendtime":"2015-11-17 22:21:31","status":"1",
     * "type":"1","extra":"","message":"俊男申请添加你为好友"}]
     * newmessagecount : 1
     */

    private int newmessagecount;//新消息数量
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

    private List<MessageEntity> message;//消息列表

    public void setNewmessagecount(int newmessagecount) {
        this.newmessagecount = newmessagecount;
    }

    public void setMessage(List<MessageEntity> message) {
        this.message = message;
    }

    public int getNewmessagecount() {
        return newmessagecount;
    }

    public List<MessageEntity> getMessage() {
        return message;
    }

    public static class MessageEntity {
        private int id;
        private int fromuserid;//来源userid
        private String fromusernickname;//来源用户昵称
        private String fromuseravatar;//来源用户头像
        private int fromusergender;//来源用户性别
        private String sendtime;//发送时间
        private int status;//状态
        private int type;//类型
        private String extra;
        private String message;//消息内容

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
}
