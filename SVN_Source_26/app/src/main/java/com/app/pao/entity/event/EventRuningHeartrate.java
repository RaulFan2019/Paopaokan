package com.app.pao.entity.event;

import java.io.Serializable;

/**
 * EventBus 传跑步时间
 *
 * @author zhoujun
 *
 */
public class EventRuningHeartrate implements Serializable{

	public int status;// 连接状态
	public int heartrate;// 心率

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(int heartrate) {
		this.heartrate = heartrate;
	}

	public EventRuningHeartrate(int status, int heartrate) {
		this.status = status;
		this.heartrate = heartrate;
	}
}
