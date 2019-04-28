package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/23.
 * 跑步信息上传内容
 */
public class GetRunningUploadResult {

    /**
     * id : 3884689
     * newWorkout : 0
     * needNotifyBeginRunning : 0
     * thumbupcount : 2
     * totallength : 33000
     * comments : [{"mediaurl":"http://123.m3u8"}]
     * newrecord : [{"type":3,"record":"33","prerecord":"334","percentage":100,"prepercentage":"100"},
     * {"type":4,"record":179,"prerecord":"1884","percentage":100,"prepercentage":"100"},
     * {"type":5,"record":1652,"prerecord":"3878","percentage":100,"prepercentage":"100"},
     * {"type":7,"record":"4327","prerecord":"14961","percentage":100,"prepercentage":"100"}]
     */

    public int id;
    private int newWorkout;
    private int needNotifyBeginRunning;
    private int thumbupcount;
    private float totallength;
    private int isworkoutend;//1.跑步结束
    /**
     * mediaurl : http://123.m3u8
     */

    private List<CommentsEntity> comments;
    /**
     * type : 3
     * record : 33
     * prerecord : 334
     * percentage : 100
     * prepercentage : 100
     */

    private List<NewrecordEntity> newrecord;

    public void setId(int id) {
        this.id = id;
    }

    public void setNewWorkout(int newWorkout) {
        this.newWorkout = newWorkout;
    }

    public void setNeedNotifyBeginRunning(int needNotifyBeginRunning) {
        this.needNotifyBeginRunning = needNotifyBeginRunning;
    }

    public int getIsworkoutend() {
        return isworkoutend;
    }

    public void setIsworkoutend(int isworkoutend) {
        this.isworkoutend = isworkoutend;
    }

    public void setThumbupcount(int thumbupcount) {
        this.thumbupcount = thumbupcount;
    }

    public void setTotallength(float totallength) {
        this.totallength = totallength;
    }

    public void setComments(List<CommentsEntity> comments) {
        this.comments = comments;
    }

    public void setNewrecord(List<NewrecordEntity> newrecord) {
        this.newrecord = newrecord;
    }

    public int getId() {
        return id;
    }

    public int getNewWorkout() {
        return newWorkout;
    }

    public int getNeedNotifyBeginRunning() {
        return needNotifyBeginRunning;
    }

    public int getThumbupcount() {
        return thumbupcount;
    }

    public float getTotallength() {
        return totallength;
    }

    public List<CommentsEntity> getComments() {
        return comments;
    }

    public List<NewrecordEntity> getNewrecord() {
        return newrecord;
    }

    public static class CommentsEntity {
        private String mediaurl;

        public void setMediaurl(String mediaurl) {
            this.mediaurl = mediaurl;
        }

        public String getMediaurl() {
            return mediaurl;
        }
    }

    /**
     *  type=1，最长距离，type=2,最长时间,type=3,最快配速,type=4,最快五公里，type=5,最快10公里,type=6，最快半马，type=7，最快全马
     */
    public static class NewrecordEntity {
        private int type;
        private float record;
        private float prerecord;
        private int ranking;
        private int preranking;

        public NewrecordEntity() {
        }

        public NewrecordEntity(int type, float record, float prerecord, int ranking, int preranking) {
            super();
            this.type = type;
            this.record = record;
            this.prerecord = prerecord;
            this.ranking = ranking;
            this.preranking = preranking;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setRecord(int record) {
            this.record = record;
        }

        public void setPrerecord(int prerecord) {
            this.prerecord = prerecord;
        }


        public int getType() {
            return type;
        }

        public float getRecord() {
            return record;
        }

        public float getPrerecord() {
            return prerecord;
        }

        public void setRecord(float record) {
            this.record = record;
        }

        public void setPrerecord(float prerecord) {
            this.prerecord = prerecord;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getPreranking() {
            return preranking;
        }

        public void setPreranking(int preranking) {
            this.preranking = preranking;
        }
    }
}
