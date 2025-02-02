package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2015/12/5.
 * 获取赛事列表结果
 */
public class GetRaceListResult {


    /**
     * count : 1
     * race : [{"raceid":"1","name":"2015苏州太湖国际马拉松",
     * "description":"苏州第一次迎来了期盼已久的全程马拉松赛事\u2014\u20142015苏州太湖国际马拉松赛即将于12月27日9：00鸣枪开跑。","begintime":"2015-12-27
     * 09:00:00","livebegintime":"2015-12-27 08:00:00","liveendtime":"2015-12-27 15:00:00","status":"1",
     * "barimage":"http://7xot4d.com1.z0.glb.clouddn.com/123Go.jpg","h5url":""}]
     */

    public int count;//赛事数量
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

    public List<RaceEntity> race;//比赛列表

    public void setCount(int count) {
        this.count = count;
    }

    public void setRace(List<RaceEntity> race) {
        this.race = race;
    }

    public int getCount() {
        return count;
    }

    public List<RaceEntity> getRace() {
        return race;
    }

    public static class RaceEntity implements Serializable {
        public int raceid;//比赛id
        public String name;//比赛名称
        public String description;//比赛描述
        public String begintime;//开始时间
        public String livebegintime;//直播时间
        public String liveendtime;//结束时间
        public int status;//状态
        public String barimage;//显示图片
        public String h5url;//H5链接
        public String barh5url;//barH5 页面

        public String getBarh5url() {
            return barh5url;
        }

        public void setBarh5url(String barh5url) {
            this.barh5url = barh5url;
        }

        public void setRaceid(int raceid) {
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

        public void setStatus(int status) {
            this.status = status;
        }

        public void setBarimage(String barimage) {
            this.barimage = barimage;
        }

        public void setH5url(String h5url) {
            this.h5url = h5url;
        }

        public int getRaceid() {
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

        public int getStatus() {
            return status;
        }

        public String getBarimage() {
            return barimage;
        }

        public String getH5url() {
            return h5url;
        }
    }
}
