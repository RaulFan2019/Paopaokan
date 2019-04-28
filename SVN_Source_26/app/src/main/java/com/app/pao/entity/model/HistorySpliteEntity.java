package com.app.pao.entity.model;

import java.io.Serializable;

/**
 * 分段历史信息(用于历史显示)
 *
 * @author Raul
 *
 */
public class HistorySpliteEntity implements Serializable {

	private float length;// 距离
	private long duration;// 用时
	private int avgheartrate;// 平均心率
	private double Latitude;// 纬度
	private double Longitude;// 经度

	public HistorySpliteEntity(float length, long duration, int avgheartrate,
							   double latitude, double longitude) {
		super();
		this.length = length;
		this.duration = duration;
		this.avgheartrate = avgheartrate;
		Latitude = latitude;
		Longitude = longitude;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getAvgheartrate() {
		return avgheartrate;
	}

	public void setAvgheartrate(int avgheartrate) {
		this.avgheartrate = avgheartrate;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

}
