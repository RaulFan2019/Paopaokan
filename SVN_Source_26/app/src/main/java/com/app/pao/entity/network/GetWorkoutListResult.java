package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 获取跑步历史列表
 */
public class GetWorkoutListResult {

    /**
     * id : 139
     * workout : [{"id":"2774681","name":"2015-11-20 07:57:33","starttime":"2015-11-20 07:57:33","duration":"2155",
     * "length":"7218","maxheight":"22","minheight":"22","maxpace":"888","minpace":"294","avgpace":"298",
     * "calorie":"523","type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1",
     * "thumbupcount":0,"hasGiveThumbup":0},{"id":"2774680","name":"2015-11-19 23:27:08","starttime":"2015-11-19
     * 23:27:08","duration":"0","length":null,"maxheight":0,"minheight":0,"maxpace":0,"minpace":0,"avgpace":0,
     * "calorie":"0","type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1",
     * "thumbupcount":0,"hasGiveThumbup":0},{"id":"2774679","name":"2015-11-19 22:26:31","starttime":"2015-11-19
     * 22:26:31","duration":"1802","length":"56","maxheight":"18","minheight":"16","maxpace":"1998","minpace":"577",
     * "avgpace":"31831","calorie":"4","type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)",
     * "gender":"1","thumbupcount":0,"hasGiveThumbup":0},{"id":"2774678","name":"2015-11-19 20:47:32",
     * "starttime":"2015-11-19 20:47:32","duration":"5","length":"131","maxheight":"0","minheight":"0",
     * "maxpace":"60","minpace":"41","avgpace":"38","calorie":"9","type":"1","status":"2","userid":"10060",
     * "nickname":"阿木木得得","avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-19_564db17822bad.jpg","gender":"1",
     * "thumbupcount":0,"hasGiveThumbup":0},{"id":"2774677","name":"2015-11-19 20:45:21","starttime":"2015-11-19
     * 20:45:21","duration":"25","length":"829","maxheight":"0","minheight":"0","maxpace":"60","minpace":"32",
     * "avgpace":"30","calorie":"60","type":"1","status":"2","userid":"10060","nickname":"阿木木得得",
     * "avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-19_564db17822bad.jpg","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0},{"id":"2774672","name":"2015-11-19 15:45:44","starttime":"2015-11-19 15:45:44",
     * "duration":"0","length":null,"maxheight":0,"minheight":0,"maxpace":0,"minpace":0,"avgpace":0,"calorie":"0",
     * "type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0},{"id":"2774660","name":"2015-11-18 17:56:05","starttime":"2015-11-18 17:56:05",
     * "duration":"0","length":null,"maxheight":0,"minheight":0,"maxpace":0,"minpace":0,"avgpace":0,"calorie":"0",
     * "type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0},{"id":"2774659","name":"2015-11-18 17:55:56","starttime":"2015-11-18 17:55:56",
     * "duration":"0","length":null,"maxheight":0,"minheight":0,"maxpace":0,"minpace":0,"avgpace":0,"calorie":"0",
     * "type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0},{"id":"2774658","name":"2015-11-18 17:54:46","starttime":"2015-11-18 17:54:46",
     * "duration":"0","length":null,"maxheight":0,"minheight":0,"maxpace":0,"minpace":0,"avgpace":0,"calorie":"0",
     * "type":"1","status":"2","userid":"10362","nickname":"沈爱疯四","avatar":"(null)","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0},{"id":"2774618","name":"2015-11-12 15:25:13","starttime":"2015-11-12 15:25:13",
     * "duration":"4084","length":"5","maxheight":"17","minheight":"15","maxpace":"0","minpace":"0",
     * "avgpace":"811409","calorie":"0","type":"1","status":"2","userid":"139","nickname":"晓俊 手机号",
     * "avatar":"http://7xk0si.com1.z0.glb.clouddn.com/2015-11-05_563b4ec976ce0.jpg","gender":"1","thumbupcount":0,
     * "hasGiveThumbup":0}]
     * maxworkoutlength : 19949100
     */

    private String id;// 查询的用户号
    private String maxworkoutlength;//最长距离
    /**
     * id : 2774681
     * name : 2015-11-20 07:57:33
     * starttime : 2015-11-20 07:57:33
     * duration : 2155
     * length : 7218
     * maxheight : 22
     * minheight : 22
     * maxpace : 888
     * minpace : 294
     * avgpace : 298
     * calorie : 523
     * type : 1
     * status : 2
     * userid : 10362
     * nickname : 沈爱疯四
     * avatar : (null)
     * gender : 1
     * thumbupcount : 0
     * hasGiveThumbup : 0
     */

    private List<WorkoutEntity> workout;//跑步历史列表

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxworkoutlength(String maxworkoutlength) {
        this.maxworkoutlength = maxworkoutlength;
    }

    public void setWorkout(List<WorkoutEntity> workout) {
        this.workout = workout;
    }

    public String getId() {
        return id;
    }

    public String getMaxworkoutlength() {
        return maxworkoutlength;
    }

    public List<WorkoutEntity> getWorkout() {
        return workout;
    }

    public static class WorkoutEntity {
        public int id;//历史id
        public String thumbnail;//跑步路线截取图像
        public int avgheartrate;//心率
        public String racename;//活动名称
        public String name;//历史名称
        public String starttime;//历史开始时间
        public long duration;//用时
        public float length;//距离
        public double maxheight;//最高海拔
        public double minheight;//最低海拔
        public int maxpace;//最快配速
        public int minpace;//最低配速
        public int avgpace;//平均配速
        public float calorie;//卡路里
        public int type;//
        public int status;//跑步状态
        public int userid;//用户id
        public String nickname;//用户昵称
        public String avatar;//头像
        public int gender;//性别
        public int thumbupcount;//点攒数量
        public int hasGiveThumbup;//自己是否点攒过
        public String birthdate;//生日
        public int videoready;//0没有视频，1有视频
        public int socialcount;//评论和点赞加起来的次数

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

        public int getSocialcount() {
            return socialcount;
        }

        public void setSocialcount(int socialcount) {
            this.socialcount = socialcount;
        }

        public String getRacename() {
            return racename;
        }

        public void setRacename(String racename) {
            this.racename = racename;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStarttime() {
            return starttime;
        }

        public long getDuration() {
            return duration;
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

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getThumbupcount() {
            return thumbupcount;
        }

        public void setThumbupcount(int thumbupcount) {
            this.thumbupcount = thumbupcount;
        }

        public int getHasGiveThumbup() {
            return hasGiveThumbup;
        }

        public void setHasGiveThumbup(int hasGiveThumbup) {
            this.hasGiveThumbup = hasGiveThumbup;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public int getVideoready() {
            return videoready;
        }

        public void setVideoready(int videoready) {
            this.videoready = videoready;
        }
    }
}
