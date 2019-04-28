package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/12/5.
 * 正在直播列表
 */
public class GetRaceLiveListResult {


    /**
     * raceid : 1
     * name : 2015苏州太湖国际马拉松
     * description : 苏州第一次迎来了期盼已久的全程马拉松赛事——2015苏州太湖国际马拉松赛即将于12月27日9：00鸣枪开跑。
     * begintime : 2015-12-27 09:00:00
     * livebegintime : 2015-12-27 08:00:00
     * liveendtime : 2015-12-27 15:00:00
     * status : 1
     * barimage : http://7xot4d.com1.z0.glb.clouddn.com/123Go.jpg
     * h5url :
     */

    private RaceEntity race;
    /**
     * userid : 10072
     * nickname : d
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-06_563c1dbd5337d.jpg
     * gender : 1
     * birthdate : 1992-11-10
     * workoutid : 3748588
     * length : 1040
     * duration : 762
     * status : 1
     */

    private List<RaceUserEntity> friends;//好友直播列表
    private List<RaceUserEntity> rungroupmembers;//好友直播列表

    /**
     * userid : 140
     * nickname : 韩向阳
     * avatar : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-01_5635e9756fb44.jpg
     * gender : 1
     * birthdate : 1975-01-29
     * workoutid : 0
     * length : 0
     * duration : 0
     * status : 0
     */

    private List<RaceUserEntity> stars;//全明星直播列表

    public List<RaceUserEntity> getRungroupmembers() {
        return rungroupmembers;
    }

    public void setRungroupmembers(List<RaceUserEntity> rungroupmembers) {
        this.rungroupmembers = rungroupmembers;
    }

    public void setRace(RaceEntity race) {
        this.race = race;
    }

    public void setFriends(List<RaceUserEntity> friends) {
        this.friends = friends;
    }

    public void setStars(List<RaceUserEntity> stars) {
        this.stars = stars;
    }

    public RaceEntity getRace() {
        return race;
    }

    public List<RaceUserEntity> getFriends() {
        return friends;
    }

    public List<RaceUserEntity> getStars() {
        return stars;
    }

    public static class RaceEntity {
        private String raceid;
        private String name;
        private String description;
        private String begintime;
        private String livebegintime;
        private String liveendtime;
        private String status;
        private String barimage;
        private String h5url;

        public void setRaceid(String raceid) {
            this.raceid = raceid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setBegintime(String begintime) {
            this.begintime = begintime;
        }

        public void setLivebegintime(String livebegintime) {
            this.livebegintime = livebegintime;
        }

        public void setLiveendtime(String liveendtime) {
            this.liveendtime = liveendtime;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setBarimage(String barimage) {
            this.barimage = barimage;
        }

        public void setH5url(String h5url) {
            this.h5url = h5url;
        }

        public String getRaceid() {
            return raceid;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getBegintime() {
            return begintime;
        }

        public String getLivebegintime() {
            return livebegintime;
        }

        public String getLiveendtime() {
            return liveendtime;
        }

        public String getStatus() {
            return status;
        }

        public String getBarimage() {
            return barimage;
        }

        public String getH5url() {
            return h5url;
        }
    }

    public static class RaceUserEntity {
        private int userid;
        private String nickname;
        private String avatar;
        private int gender;
        private String birthdate;
        private int workoutid;
        private float length;
        private long duration;
        private int status;

        public void setUserid(int userid) {
            this.userid = userid;
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

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public void setWorkoutid(int workoutid) {
            this.workoutid = workoutid;
        }

        public void setLength(float length) {
            this.length = length;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserid() {
            return userid;
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

        public String getBirthdate() {
            return birthdate;
        }

        public int getWorkoutid() {
            return workoutid;
        }

        public float getLength() {
            return length;
        }

        public long getDuration() {
            return duration;
        }

        public int getStatus() {
            return status;
        }
    }


}
