package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * Created by Raul on 2016/1/25.
 * 直播时的评论和点赞
 */
public class LiveSocial implements Serializable {

    /**
     * type : 1
     * userid : 10060
     * useravatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-12-10_56689ab655fa9.jpg
     * usernickname : 阿木木得得得阿快
     * comment :
     * time : 2016-01-24 13:14:03
     */

    public int id;
    public int type;//type：1，,点赞 2，评论
    public int userid;
    public String useravatar;
    public String usernickname;
    public String comment;
    public String time;
    public int mediatype;
    public String mediaurl;
    public int length;
    public boolean readnow = false;
    public boolean hasread;
    public double latitude;
    public double longitude;
    public float position;
    public boolean isSelect = false;
    public int replyuserid;
    public String replyusername;
    public String replynickname;
    public int replygender;
    public String replyavatar;

    public LiveSocial() {

    }

    public LiveSocial(int type, int userid, String useravatar, String usernickname, String comment, String time
            , int mediatype, String mediaurl, int length) {
        super();
        this.type = type;
        this.userid = userid;
        this.useravatar = useravatar;
        this.usernickname = usernickname;
        this.comment = comment;
        this.time = time;
        this.mediaurl = mediaurl;
        this.mediatype = mediatype;
        this.length = length;
        this.readnow = false;
        this.hasread = false;
    }

    public LiveSocial(String replyavatar, int type, int userid, String useravatar,
                      String usernickname, String comment, String time, int mediatype,
                      String mediaurl, int length, boolean readnow, boolean hasread,
                      double latitude, double longitude, float position, boolean isSelect,
                      int replyuserid, String replyusername, String replynickname, int replygender) {
        super();
        this.replyavatar = replyavatar;
        this.type = type;
        this.userid = userid;
        this.useravatar = useravatar;
        this.usernickname = usernickname;
        this.comment = comment;
        this.time = time;
        this.mediatype = mediatype;
        this.mediaurl = mediaurl;
        this.length = length;
        this.readnow = readnow;
        this.hasread = hasread;
        this.latitude = latitude;
        this.longitude = longitude;
        this.position = position;
        this.isSelect = isSelect;
        this.replyuserid = replyuserid;
        this.replyusername = replyusername;
        this.replynickname = replynickname;
        this.replygender = replygender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public int getReplyuserid() {
        return replyuserid;
    }

    public void setReplyuserid(int replyuserid) {
        this.replyuserid = replyuserid;
    }

    public String getReplyusername() {
        return replyusername;
    }

    public void setReplyusername(String replyusername) {
        this.replyusername = replyusername;
    }

    public String getReplynickname() {
        return replynickname;
    }

    public void setReplynickname(String replynickname) {
        this.replynickname = replynickname;
    }

    public int getReplygender() {
        return replygender;
    }

    public void setReplygender(int replygender) {
        this.replygender = replygender;
    }

    public String getReplyavatar() {
        return replyavatar;
    }

    public void setReplyavatar(String replyavatar) {
        this.replyavatar = replyavatar;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isReadnow() {
        return readnow;
    }

    public void setReadnow(boolean readnow) {
        this.readnow = readnow;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public int getUserid() {
        return userid;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public String getUsernickname() {
        return usernickname;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public int getMediatype() {
        return mediatype;
    }

    public void setMediatype(int mediatype) {
        this.mediatype = mediatype;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHasread() {
        return hasread;
    }

    public void setHasread(boolean hasread) {
        this.hasread = hasread;
    }
}
