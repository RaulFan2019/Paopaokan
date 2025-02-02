package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/12/2.
 * 获取申请好友请求结果
 */
public class GetApplyFriendListResult {

    /**
     * applyid : 1393
     * id : 10072
     * username : 13002179931
     * nickname : duang~
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-20_564f205554815.jpg
     * gender : 1
     * status : 3
     * applydate : 2015-12-02 19:30:13
     * approvedate :
     * type : 2
     */

    public int applyid;//申请id
    public int id;//user id
    public String username;//用户名
    public String nickname;//昵称
    public String avatar;//头像
    public int gender;//性别
    public int status;//1.同意,2.拒绝 3.等待
    public String applydate;//申请日期
    public String approvedate;//同意日期
    public int totallength;//总里程
    public int type;//1.发出请求. 2 收到请求

    public void setApplyid(int applyid) {
        this.applyid = applyid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public void setApprovedate(String approvedate) {
        this.approvedate = approvedate;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getApplyid() {
        return applyid;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getGender() {
        return gender;
    }

    public int getStatus() {
        return status;
    }

    public String getApplydate() {
        return applydate;
    }

    public String getApprovedate() {
        return approvedate;
    }

    public int getType() {
        return type;
    }

    public int getTotallength() {
        return totallength;
    }

    public void setTotallength(int totallength) {
        this.totallength = totallength;
    }
}
