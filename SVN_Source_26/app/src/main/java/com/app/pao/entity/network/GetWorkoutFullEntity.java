package com.app.pao.entity.network;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 跑步历史具体信息(从网络获取)
 *
 * @author Raul
 *
 */
public class GetWorkoutFullEntity implements Serializable {
	public int id;
	public String name;
	public String starttime;// 开始时间
	public long duration;// 持续时间(秒)
	public float length;// 距离(米)
	public int type;
	public int status;
	public double maxheight;// 最高高度
	public double minheight;// 最低高度
	public int maxpace;// 最大步数
	public int minpace;// 最小步数
	public float calorie;// 消耗卡路里
	public int users_id;// 用户id
	public int maxspeed;// 最大配速
	public int minspeed;// 最小配速
	public int avgspeed;// 平均配速
	public int maxheartrate;// 最大心率
	public int minheartrate;// 最小心率
	public int avgheartrate;// 平均心率
	public String avatar;//头像
	public List<GetWorkoutLapEntity> lap;
	public List<GetWorkoutSplitsEntity> splits;

	public GetWorkoutFullEntity() {
	}

	public GetWorkoutFullEntity(int id, String name, String starttime, long duration, float length, int type, int status,
								double maxheight, double minheight, int maxpace, int minpace, float calorie, int users_id, int maxspeed,
								int minspeed, int avgspeed, int maxheartrate, int minheartrate, int avgheartrate,String avatar,
								List<GetWorkoutLapEntity> lap, List<GetWorkoutSplitsEntity> splits) {
		super();
		this.id = id;
		this.name = name;
		this.starttime = starttime;
		this.duration = duration;
		this.length = length;
		this.type = type;
		this.status = status;
		this.maxheight = maxheight;
		this.minheight = minheight;
		this.maxpace = maxpace;
		this.minpace = minpace;
		this.calorie = calorie;
		this.users_id = users_id;
		this.maxspeed = maxspeed;
		this.minspeed = minspeed;
		this.avgspeed = avgspeed;
		this.maxheartrate = maxheartrate;
		this.minheartrate = minheartrate;
		this.avgheartrate = avgheartrate;
		this.lap = lap;
		this.splits = splits;
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getMaxheight() {
		return maxheight;
	}

	public void setMaxheight(double maxheight) {
		this.maxheight = maxheight;
	}

	public double getMinheight() {
		return minheight;
	}

	public void setMinheight(double minheight) {
		this.minheight = minheight;
	}

	public int getMaxpace() {
		return maxpace;
	}

	public void setMaxpace(int maxpace) {
		this.maxpace = maxpace;
	}

	public int getMinpace() {
		return minpace;
	}

	public void setMinpace(int minpace) {
		this.minpace = minpace;
	}

	public float getCalorie() {
		return calorie;
	}

	public void setCalorie(float calorie) {
		this.calorie = calorie;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(int maxspeed) {
		this.maxspeed = maxspeed;
	}

	public int getMinspeed() {
		return minspeed;
	}

	public void setMinspeed(int minspeed) {
		this.minspeed = minspeed;
	}

	public int getAvgspeed() {
		return avgspeed;
	}

	public void setAvgspeed(int avgspeed) {
		this.avgspeed = avgspeed;
	}

	public int getMaxheartrate() {
		return maxheartrate;
	}

	public void setMaxheartrate(int maxheartrate) {
		this.maxheartrate = maxheartrate;
	}

	public int getMinheartrate() {
		return minheartrate;
	}

	public void setMinheartrate(int minheartrate) {
		this.minheartrate = minheartrate;
	}

	public int getAvgheartrate() {
		return avgheartrate;
	}

	public void setAvgheartrate(int avgheartrate) {
		this.avgheartrate = avgheartrate;
	}

	public List<GetWorkoutLapEntity> getLap() {
		return lap;
	}

	public void setLap(List<GetWorkoutLapEntity> lap) {
		this.lap = lap;
	}

	public List<GetWorkoutSplitsEntity> getSplits() {
		return splits;
	}

	public void setSplits(List<GetWorkoutSplitsEntity> splits) {
		this.splits = splits;
	}

}
