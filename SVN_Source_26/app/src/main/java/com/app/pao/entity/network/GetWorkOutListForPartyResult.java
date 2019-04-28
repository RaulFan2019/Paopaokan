package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Administrator on 2016/1/15.
 *
 * 活动历史
 */
public class GetWorkOutListForPartyResult {
    private List<PartyWorkOut> workout;

    public List<PartyWorkOut> getWorkout() {
        return workout;
    }

    public void setWorkout(List<PartyWorkOut> workout) {
        this.workout = workout;
    }

    public static class PartyWorkOut{
        private int id;
        private String starttime;
        private long duration;//秒数
        private float length;
        private int selected;//1,已经选择用于活动打开 0,没有选择

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

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }
    }
}
