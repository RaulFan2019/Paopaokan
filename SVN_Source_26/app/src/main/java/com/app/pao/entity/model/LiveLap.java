package com.app.pao.entity.model;

import java.util.List;

/**
 * 直播LAP对象
 *
 * @author Raul
 *
 */
public class LiveLap {
	public String starttime;
	public long duration;
	public int lap;
	public float length;
	public List<LiveLocation> locationdata;
	public List<LiveHeartrateEntity> heartratedata;

	public LiveLap() {

	}

	public LiveLap(String starttime, long duration, int lap, float length, List<LiveLocation> locationdata,
				   List<LiveHeartrateEntity> heartratedata) {
		super();
		this.starttime = starttime;
		this.duration = duration;
		this.lap = lap;
		this.length = length;
		this.locationdata = locationdata;
		this.heartratedata = heartratedata;
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

	public int getLap() {
		return lap;
	}

	public void setLap(int lap) {
		this.lap = lap;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public List<LiveLocation> getLocationdata() {
		return locationdata;
	}

	public void setLocationdata(List<LiveLocation> locationdata) {
		this.locationdata = locationdata;
	}

	public List<LiveHeartrateEntity> getHeartratedata() {
		return heartratedata;
	}

	public void setHeartratedata(List<LiveHeartrateEntity> heartratedata) {
		this.heartratedata = heartratedata;
	}

}
