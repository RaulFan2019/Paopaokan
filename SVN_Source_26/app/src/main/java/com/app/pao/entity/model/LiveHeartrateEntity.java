package com.app.pao.entity.model;

/**
 * 直播心率类
 *
 * @author Raul
 *
 */
public class LiveHeartrateEntity {

	public int bpm;
	public int timeoffset;

	public LiveHeartrateEntity() {

	}

	public LiveHeartrateEntity(int bpm, int timeoffset) {
		super();
		this.bpm = bpm;
		this.timeoffset = timeoffset;
	}

	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public int getTimeoffset() {
		return timeoffset;
	}

	public void setTimeoffset(int timeoffset) {
		this.timeoffset = timeoffset;
	}

}
