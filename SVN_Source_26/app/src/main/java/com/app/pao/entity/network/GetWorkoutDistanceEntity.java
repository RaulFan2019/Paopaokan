package com.app.pao.entity.network;

import java.io.Serializable;

/**
 * 跑步历史距离信息(从网络获取)
 *
 * @author Raul
 *
 */
public class GetWorkoutDistanceEntity implements Serializable {

	private String distance;
	private String timeoffset;

	public GetWorkoutDistanceEntity() {
	}

	public GetWorkoutDistanceEntity(String distance, String timeoffset) {
		super();
		this.distance = distance;
		this.timeoffset = timeoffset;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTimeoffset() {
		return timeoffset;
	}

	public void setTimeoffset(String timeoffset) {
		this.timeoffset = timeoffset;
	}

}
