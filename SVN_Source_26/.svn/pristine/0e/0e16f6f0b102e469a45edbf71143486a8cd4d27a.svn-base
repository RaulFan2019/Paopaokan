package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2016/1/11.
 */
public class GetDynamicListResult {


    /**
     * intstarttime : 1452384888
     * starttime : 2016-01-10 08:14:48
     * dynamictype : 1
     * userid : 140
     * id : 4022106
     * info : {"name":"2016-01-10 08:14:48","duration":"4170","length":"16916","maxheight":"7","minheight":"7",
     * "maxpace":"3066","minpace":"40","avgpace":"246","calorie":"1314","type":"1","status":"2"}
     * nickname : 韩向阳 (Speer
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-12-09_56682c992417f.jpg
     * gender : 1
     * birthdate : 1975-01-29
     * thumbupcount : 1
     * hasGiveThumbup : 0
     * comments : [{"userid":"139","commenttime":"2016-01-10 16:39:09","comment":"addoil","username":"15921613019",
     * "nickname":"挑灯看剑","gender":"1","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-23_5652d1e66c065.jpg",
     * "replyuserid":"140","replyusername":"13501636086","replynickname":"韩向阳 (Speer","replygender":"1",
     * "replyavatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-12-09_56682c992417f.jpg"}]
     */

    public int intstarttime;//时间
    public String starttime;//开始时间
    public int dynamictype;//动态类型 1. 跑步 ,2 活动
    public int userid;//用户id 或 活动创建者id
    public int id;//workout id 或 活动id
    public String info;//动态内容
    public String nickname;//用户昵称
    public String avatar;//用户头像
    public String gender;//用户性别
    public String birthdate;//用户生日
    public int thumbupcount;//点攒数量
    public int hasGiveThumbup;//是否能点攒
    public int commentscount;//评论数量


    /**
     * userid : 139
     * commenttime : 2016-01-10 16:39:09
     * comment : addoil
     * username : 15921613019
     * nickname : 挑灯看剑
     * gender : 1
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-23_5652d1e66c065.jpg
     * replyuserid : 140
     * replyusername : 13501636086
     * replynickname : 韩向阳 (Speer
     * replygender : 1
     * replyavatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-12-09_56682c992417f.jpg
     */

    private List<CommentsEntity> comments;//评论

    public int getCommentscount() {
        return commentscount;
    }

    public void setCommentscount(int commentscount) {
        this.commentscount = commentscount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setIntstarttime(int intstarttime) {
        this.intstarttime = intstarttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setDynamictype(int dynamictype) {
        this.dynamictype = dynamictype;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setThumbupcount(int thumbupcount) {
        this.thumbupcount = thumbupcount;
    }

    public void setHasGiveThumbup(int hasGiveThumbup) {
        this.hasGiveThumbup = hasGiveThumbup;
    }

    public void setComments(List<CommentsEntity> comments) {
        this.comments = comments;
    }

    public int getIntstarttime() {
        return intstarttime;
    }

    public String getStarttime() {
        return starttime;
    }

    public int getDynamictype() {
        return dynamictype;
    }

    public int getUserid() {
        return userid;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getThumbupcount() {
        return thumbupcount;
    }

    public int getHasGiveThumbup() {
        return hasGiveThumbup;
    }

    public List<CommentsEntity> getComments() {
        return comments;
    }


    public static class CommentsEntity {
        private int userid;
        private String commenttime;
        private String comment;
        private String nickname;
        private int replyuserid;
        private String replynickname;

        public CommentsEntity() {

        }

        public CommentsEntity(int userid, String commenttime, String comment, String nickname, int replyuserid,
                              String replynickname) {
            super();
            this.userid = userid;
            this.commenttime = commenttime;
            this.comment = comment;
            this.nickname = nickname;
            this.replyuserid = replyuserid;
            this.replynickname = replynickname;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public void setCommenttime(String commenttime) {
            this.commenttime = commenttime;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }


        public void setReplyuserid(int replyuserid) {
            this.replyuserid = replyuserid;
        }


        public void setReplynickname(String replynickname) {
            this.replynickname = replynickname;
        }


        public int getUserid() {
            return userid;
        }

        public String getCommenttime() {
            return commenttime;
        }

        public String getComment() {
            return comment;
        }


        public String getNickname() {
            return nickname;
        }


        public int getReplyuserid() {
            return replyuserid;
        }

        public String getReplynickname() {
            return replynickname;
        }

    }

    public static class Workout {

        /**
         * name : 2016-01-10 08:14:48
         * duration : 4170
         * length : 16916
         * maxheight : 7
         * minheight : 7
         * maxpace : 3066
         * minpace : 40
         * avgpace : 246
         * calorie : 1314
         * type : 1
         * status : 2
         */

        public String name;
        public long duration;//时间
        public float length;//长度
        public double maxheight;
        public double minheight;
        public int maxpace;//最大配速
        public int minpace;//最小配速
        public int avgpace;//平均配速
        public float calorie;//卡路里
        public int type;
        public int status;
        public int avgheartrate;
        public int videoready;
        public String thumbnail;//跑步路线截取图像

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getAvgheartrate() {
            return avgheartrate;
        }

        public void setAvgheartrate(int avgheartrate) {
            this.avgheartrate = avgheartrate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public float getLength() {
            return length;
        }

        public void setLength(float length) {
            this.length = length;
        }

        public double getMaxheight() {
            return maxheight;
        }

        public void setMaxheight(double maxheight) {
            this.maxheight = maxheight;
        }

        public double getMinheight() {
            return minheight;
        }

        public void setMinheight(double minheight) {
            this.minheight = minheight;
        }

        public int getMaxpace() {
            return maxpace;
        }

        public void setMaxpace(int maxpace) {
            this.maxpace = maxpace;
        }

        public int getMinpace() {
            return minpace;
        }

        public void setMinpace(int minpace) {
            this.minpace = minpace;
        }

        public int getAvgpace() {
            return avgpace;
        }

        public void setAvgpace(int avgpace) {
            this.avgpace = avgpace;
        }

        public float getCalorie() {
            return calorie;
        }

        public void setCalorie(float calorie) {
            this.calorie = calorie;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getVideoready() {
            return videoready;
        }

        public void setVideoready(int videoready) {
            this.videoready = videoready;
        }
    }

    public static class Party {

        /**
         * name : 测试跑步
         * description : 我的心理压力过来了。
         * <p>
         * rungroupid : 153
         * locationcity :
         * locationprovince :
         * rungroupavatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-12-10_56687a2212908.jpg
         */

        public int id;//跑团id
        public String name;//活动名称
        public String description;//活动描述
        public int rungroupid;//跑团ID
        public String locationcity;//活动城市
        public String locationprovince;//活动省
        public String rungroupavatar;//跑团头像
        public String starttime;//开始时间
        public int status;//活动状态
        public int personstatus;//人员状态
        public String location;//详细位置
        public String picture;//活动照片
        public float avglength;//平均距离　
        public long avgduration;//平均时间
        public int avgpace;//平均配速
        public String rungroupname;//跑团名称
        public String endtime;//结束时间
        public int signupcount;//活动人数

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPersonstatus() {
            return personstatus;
        }

        public void setPersonstatus(int personstatus) {
            this.personstatus = personstatus;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public float getAvglength() {
            return avglength;
        }

        public void setAvglength(float avglength) {
            this.avglength = avglength;
        }

        public long getAvgduration() {
            return avgduration;
        }

        public void setAvgduration(long avgduration) {
            this.avgduration = avgduration;
        }

        public int getAvgpace() {
            return avgpace;
        }

        public void setAvgpace(int avgpace) {
            this.avgpace = avgpace;
        }

        public String getRungroupname() {
            return rungroupname;
        }

        public void setRungroupname(String rungroupname) {
            this.rungroupname = rungroupname;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setRungroupid(int rungroupid) {
            this.rungroupid = rungroupid;
        }

        public void setLocationcity(String locationcity) {
            this.locationcity = locationcity;
        }

        public void setLocationprovince(String locationprovince) {
            this.locationprovince = locationprovince;
        }

        public void setRungroupavatar(String rungroupavatar) {
            this.rungroupavatar = rungroupavatar;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getRungroupid() {
            return rungroupid;
        }

        public String getLocationcity() {
            return locationcity;
        }

        public String getLocationprovince() {
            return locationprovince;
        }

        public String getRungroupavatar() {
            return rungroupavatar;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public int getSignupcount() {
            return signupcount;
        }

        public void setSignupcount(int signupcount) {
            this.signupcount = signupcount;
        }
    }
}
