package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2015/12/15.
 */
public class GetJpushMessageResult implements Serializable{


    /**
     334         $extras= array();
     335         $extras['userid']=$userid;// dbid for user
     336         $extras['username']=$username; //name for user, cell phone number
     337         $extras['usernickname']=$usernickname;
     338         $extras['groupid']=$groupid;
     339         $extras['groupname']=$groupname;
     340         $extras['groupavatar']=$groupavatar;
     341         $extras['gender']=$gender;
     342         $extras['avatar']=$avatar;
     343         $extras['messagetype']=JPUSHAPPLYJOINRUNGROUP;
     344         $extras['messageid']=$messageid;
     345
     346         $extras['id'] = $messageid;
     347         $extras['fromuserid']=$userid;
     348         $extras['fromusernickname']= $usernickname;
     349         $extras['fromuseravatar']= $avatar;
     350         $extras['fromusergender']= $gender;
     351
     352         $extras['sendtime']= date("Y-m-d H:i:s");
     353         $extras['status']= 1;
     354         $extras['type']= USERMESSAGEAPPLYJOINRUNGROUP;
     355         $extras['extra']= $groupid;
     */

    public String username;
    public int fromuserid;
    public String extra;
    public int gender;
    public String fromuseravatar;
    public int userid;
    public String usernickname;
    public int messagetype;
    public int fromusergender;
    public String fromusernickname;
    public int status;
    public String avatar;
    public long messageid;
    public int type;
    public long id;
    public String sendtime;
    public String message;
    public int groupid;
    public String groupname;
    public String groupavatar;
    public String location;
    public int partyid;
    public String partyname;
    public String workoutstarttime;
    public int workoutid;
    public String approvernickname;

    public String getApprovernickname() {
        return approvernickname;
    }

    public void setApprovernickname(String approvernickname) {
        this.approvernickname = approvernickname;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public int getPartyid() {
        return partyid;
    }

    public void setPartyid(int partyid) {
        this.partyid = partyid;
    }

    public String getWorkoutstarttime() {
        return workoutstarttime;
    }

    public void setWorkoutstarttime(String workoutstarttime) {
        this.workoutstarttime = workoutstarttime;
    }

    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupavatar() {
        return groupavatar;
    }

    public void setGroupavatar(String groupavatar) {
        this.groupavatar = groupavatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFromuserid(int fromuserid) {
        this.fromuserid = fromuserid;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setFromuseravatar(String fromuseravatar) {
        this.fromuseravatar = fromuseravatar;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }

    public void setFromusergender(int fromusergender) {
        this.fromusergender = fromusergender;
    }

    public void setFromusernickname(String fromusernickname) {
        this.fromusernickname = fromusernickname;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setMessageid(long messageid) {
        this.messageid = messageid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getUsername() {
        return username;
    }

    public int getFromuserid() {
        return fromuserid;
    }

    public String getExtra() {
        return extra;
    }

    public int getGender() {
        return gender;
    }

    public String getFromuseravatar() {
        return fromuseravatar;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsernickname() {
        return usernickname;
    }

    public int getMessagetype() {
        return messagetype;
    }

    public int getFromusergender() {
        return fromusergender;
    }

    public String getFromusernickname() {
        return fromusernickname;
    }

    public int getStatus() {
        return status;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getMessageid() {
        return messageid;
    }

    public int getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getSendtime() {
        return sendtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
