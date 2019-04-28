package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/11/16.
 */
public class GetUploadAvatarResult {


    /**
     * url : http://7xk0si.com1.z0.glb.clouddn.com/2015-11-16_56497111b44cd.png
     */

    public String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public GetUploadAvatarResult() {

    }

    public GetUploadAvatarResult(String url) {
        super();
        this.url = url;
    }
}
