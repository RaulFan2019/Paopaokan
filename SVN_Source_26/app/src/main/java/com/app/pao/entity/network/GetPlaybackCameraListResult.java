package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul on 2016/1/18.
 * 获取回放视频列表
 */
public class GetPlaybackCameraListResult {


    /**
     * id : 1
     * starttime : 2015-11-08 07:08:22
     * endtime : 2015-11-08 07:09:16
     * url : http://7xo1jr.media1.z0.glb.clouddn.com/28.2015_11_08_07_50_59.180.m3u8
     * cameralatitude : 31.23298
     * cameralongitude : 121.492047
     */

    private List<VideoEntity> video;//视频列表

    public void setVideo(List<VideoEntity> video) {
        this.video = video;
    }

    public List<VideoEntity> getVideo() {
        return video;
    }

    public static class VideoEntity implements Serializable {
        public int id;//序号
        public String starttime;
        public String endtime;
        public int startoffset;
        public int endoffset;
        public String url;//播放地址
        public double cameralatitude;//视频纬度
        public double cameralongitude;//视频经度
        public float positionmeters;//跑步距离
        public String verticalcover;//竖直封面
        public String horizontalcover;//横向封面
        public int pace;//配速

        public int getStartoffset() {
            return startoffset;
        }

        public void setStartoffset(int startoffset) {
            this.startoffset = startoffset;
        }

        public int getEndoffset() {
            return endoffset;
        }

        public void setEndoffset(int endoffset) {
            this.endoffset = endoffset;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setCameralatitude(double cameralatitude) {
            this.cameralatitude = cameralatitude;
        }

        public void setCameralongitude(double cameralongitude) {
            this.cameralongitude = cameralongitude;
        }

        public int getId() {
            return id;
        }

        public String getStarttime() {
            return starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public String getUrl() {
            return url;
        }

        public double getCameralatitude() {
            return cameralatitude;
        }

        public double getCameralongitude() {
            return cameralongitude;
        }

        public float getPositionmeters() {
            return positionmeters;
        }

        public void setPositionmeters(float positionmeters) {
            this.positionmeters = positionmeters;
        }

        public String getVerticalcover() {
            return verticalcover;
        }

        public void setVerticalcover(String verticalcover) {
            this.verticalcover = verticalcover;
        }

        public String getHorizontalcover() {
            return horizontalcover;
        }

        public void setHorizontalcover(String horizontalcover) {
            this.horizontalcover = horizontalcover;
        }

        public int getPace() {
            return pace;
        }

        public void setPace(int pace) {
            this.pace = pace;
        }
    }
}
