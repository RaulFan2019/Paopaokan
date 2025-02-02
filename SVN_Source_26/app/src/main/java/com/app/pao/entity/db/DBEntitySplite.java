package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 保存分段信息
 *
 * @author Raul
 *
 */
@Table(name = "splite")
public class DBEntitySplite implements Serializable{

	@Id
	@NoAutoIncrement
	@Column(column = "spliteid")
	public long spliteid;
	@Column(column = "id")
	public long id;// 序号
	@Column(column = "workoutName")
	public String workoutName;// 跑步历史名称
	@Column(column = "length")
	public float length;// 距离
	@Column(column = "duration")
	public long duration;// 用时
	@Column(column = "avgheartrate")
	public int avgheartrate;// 平均心率
	@Column(column = "minaltitude")
	public double minaltitude;// 最低海拔
	@Column(column = "maxaltitude")
	public double maxaltitude;// 最高海拔
	@Column(column = "status")
	public int status;// 1正在跑,2 结束了
	@Column(column = "Latitude")
	public double Latitude;// 纬度
	@Column(column = "Longitude")
	public double Longitude;// 经度

	public DBEntitySplite() {

	}

	public DBEntitySplite(long spliteid, long id, String workoutName, float length, long duration, int avgheartrate,
						  double minaltitude, double maxaltitude, int status, double Latitude, double Longitude) {
		super();
		this.spliteid = spliteid;
		this.id = id;
		this.workoutName = workoutName;
		this.length = length;
		this.duration = duration;
		this.avgheartrate = avgheartrate;
		this.minaltitude = minaltitude;
		this.maxaltitude = maxaltitude;
		this.status = status;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
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

	public long getSpliteid() {
		return spliteid;
	}

	public void setSpliteid(long spliteid) {
		this.spliteid = spliteid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWorkoutName() {
		return workoutName;
	}

	public void setWorkoutName(String workoutName) {
		this.workoutName = workoutName;
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

	public double getMinaltitude() {
		return minaltitude;
	}

	public void setMinaltitude(double minaltitude) {
		this.minaltitude = minaltitude;
	}

	public double getMaxaltitude() {
		return maxaltitude;
	}

	public void setMaxaltitude(double maxaltitude) {
		this.maxaltitude = maxaltitude;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
