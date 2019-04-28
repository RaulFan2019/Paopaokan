package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul.Fan on 2016/3/8.
 * 解析复制剪贴板返回的问题
 */
public class GetParseInviteTextResult implements Serializable{


    /**
     * invitecode : RiAp
     * type : 1
     * data : 10060
     * ownerid : 10060
     * ownernickname : 阿木木得得得阿快uu
     * name : 阿木木得得得阿快uu
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2016-01-07_568e516fd1d2f.
     * comment : 河南省 鹤壁市
     */

    private String invitecode;
    private int type;
    private int data;
    private int ownerid;
    private String ownernickname;
    private String name;
    private String avatar;
    private String comment;

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public void setOwnernickname(String ownernickname) {
        this.ownernickname = ownernickname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public int getType() {
        return type;
    }

    public int getData() {
        return data;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public String getOwnernickname() {
        return ownernickname;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getComment() {
        return comment;
    }
}
