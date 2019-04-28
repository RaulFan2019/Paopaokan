package com.app.pao.entity.network;

/**
 * 跑步历史 步数信息(从网络获取)
 *
 * @author Raul
 *
 */
public class GetWorkoutStepcountdeltaEntity {

	private String steps;
	private String timeoffset;

	public GetWorkoutStepcountdeltaEntity() {
	}

	public GetWorkoutStepcountdeltaEntity(String steps, String timeoffset) {
		super();
		this.steps = steps;
		this.timeoffset = timeoffset;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getTimeoffset() {
		return timeoffset;
	}

	public void setTimeoffset(String timeoffset) {
		this.timeoffset = timeoffset;
	}

}
