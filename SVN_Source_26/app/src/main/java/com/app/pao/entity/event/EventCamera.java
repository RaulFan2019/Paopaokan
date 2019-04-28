package com.app.pao.entity.event;

import java.io.Serializable;

/**
 * 摄像头变化事件(一个摄像头)
 *
 * @author Raul
 */
public class EventCamera implements Serializable{

    public static final int CMD_PLAYING = 1;
    public static final int CMD_STOP = 2;
    public static final int CMD_PAUSE = 3;
    public static final int CMD_CONTINUE = 4;

    public String cameraUrl;
    public int CMD;

    public EventCamera() {

    }

    public EventCamera(String cameraUrl, int cMD) {
        super();
        this.cameraUrl = cameraUrl;
        CMD = cMD;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public int getCMD() {
        return CMD;
    }

    public void setCMD(int cMD) {
        CMD = cMD;
    }

}
