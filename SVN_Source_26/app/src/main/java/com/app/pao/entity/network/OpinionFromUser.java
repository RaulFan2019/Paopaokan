package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by LY on 2016/3/21.
 */
public class OpinionFromUser {


    /**
     * commentid : 206
     * comment : 华为畅享5，不能愉快的使用了。点击中间的小人开始跑就自动重启
     * date : 2015-12-29 23:29:21
     * response : [{"response":"谢谢宝贵意见，尽快改正","date":"2016-01-06 00:00:00"}]
     */

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        private String commentid;
        private String comment;
        private String date;
        /**
         * response : 谢谢宝贵意见，尽快改正
         * date : 2016-01-06 00:00:00
         */

        private List<ResponseBean> response;

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ResponseBean> getResponse() {
            return response;
        }

        public void setResponse(List<ResponseBean> response) {
            this.response = response;
        }

        public static class ResponseBean {
            private String response;
            private String date;

            public String getResponse() {
                return response;
            }

            public void setResponse(String response) {
                this.response = response;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
