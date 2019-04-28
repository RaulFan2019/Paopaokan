package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/***
 * 分段信息(从网络获取)
 */
public class GetWorkoutLapEntity implements Serializable {
	public String starttime;
	public int duration;
	public int lap;
	public float length;
	public int status;
	public List<GetWorkoutLocationEntity> locationdata;
	public List<GetWorkoutSpeedEntity> speeddata;
	public List<GetWorkoutDistanceEntity> distancedata;
	public List<GetWorkoutHeartrateEntity> heartratedata;
	public List<GetWorkoutCaloriesEntity> caloriesdata;
	public List<GetWorkoutStepcountcadenceEntity> stepcountcadencedata;
	public List<GetWorkoutStepcountdeltaEntity> stepcountdeltadata;

	public GetWorkoutLapEntity(String starttime, int duration, int lap, float length, int status,
							   List<GetWorkoutLocationEntity> locationdata, List<GetWorkoutSpeedEntity> speeddata,
							   List<GetWorkoutDistanceEntity> distancedata, List<GetWorkoutHeartrateEntity> heartratedata,
							   List<GetWorkoutCaloriesEntity> caloriesdata, List<GetWorkoutStepcountcadenceEntity> stepcountcadencedata,
							   List<GetWorkoutStepcountdeltaEntity> stepcountdeltadata) {
		super();
		this.starttime = starttime;
		this.duration = duration;
		this.lap = lap;
		this.length = length;
		this.status = status;
		this.locationdata = locationdata;
		this.speeddata = speeddata;
		this.distancedata = distancedata;
		this.heartratedata = heartratedata;
		this.caloriesdata = caloriesdata;
		this.stepcountcadencedata = stepcountcadencedata;
		this.stepcountdeltadata = stepcountdeltadata;
	}

	public GetWorkoutLapEntity() {

	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GetWorkoutLocationEntity> getLocationdata() {
		return locationdata;
	}

	public void setLocationdata(List<GetWorkoutLocationEntity> locationdata) {
		this.locationdata = locationdata;
	}

	public List<GetWorkoutSpeedEntity> getSpeeddata() {
		return speeddata;
	}

	public void setSpeeddata(List<GetWorkoutSpeedEntity> speeddata) {
		this.speeddata = speeddata;
	}

	public List<GetWorkoutDistanceEntity> getDistancedata() {
		return distancedata;
	}

	public void setDistancedata(List<GetWorkoutDistanceEntity> distancedata) {
		this.distancedata = distancedata;
	}

	public List<GetWorkoutHeartrateEntity> getHeartratedata() {
		return heartratedata;
	}

	public void setHeartratedata(List<GetWorkoutHeartrateEntity> heartratedata) {
		this.heartratedata = heartratedata;
	}

	public List<GetWorkoutCaloriesEntity> getCaloriesdata() {
		return caloriesdata;
	}

	public void setCaloriesdata(List<GetWorkoutCaloriesEntity> caloriesdata) {
		this.caloriesdata = caloriesdata;
	}

	public List<GetWorkoutStepcountcadenceEntity> getStepcountcadencedata() {
		return stepcountcadencedata;
	}

	public void setStepcountcadencedata(List<GetWorkoutStepcountcadenceEntity> stepcountcadencedata) {
		this.stepcountcadencedata = stepcountcadencedata;
	}

	public List<GetWorkoutStepcountdeltaEntity> getStepcountdeltadata() {
		return stepcountdeltadata;
	}

	public void setStepcountdeltadata(List<GetWorkoutStepcountdeltaEntity> stepcountdeltadata) {
		this.stepcountdeltadata = stepcountdeltadata;
	}

}
