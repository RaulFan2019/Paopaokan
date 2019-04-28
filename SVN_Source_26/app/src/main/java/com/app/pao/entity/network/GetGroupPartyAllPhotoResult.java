package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 * <p/>
 * 活动所有图片
 */
public class GetGroupPartyAllPhotoResult {
    private List<PartyPicture> picture;

    public List<PartyPicture> getPicture() {
        return picture;
    }

    public void setPicture(List<PartyPicture> picture) {
        this.picture = picture;
    }

    public static class PartyPicture implements Serializable{
        private int photoid;
        private String url;

        public int getPhotoid() {
            return photoid;
        }

        public void setPhotoid(int photoid) {
            this.photoid = photoid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
