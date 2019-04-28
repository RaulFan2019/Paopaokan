package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 保存摄像头信息
 *
 * @author Raul
 *
 */
@Table(name = "camera")
public class DBEntityCamera {

	@Id
	@NoAutoIncrement
	@Column(column = "cameraId")
	private int id;
	@Column(column = "name")
	private String name;
	@Column(column = "longitude")
	private double longitude;
	@Column(column = "latitude")
	private double latitude;
	@Column(column = "url")
	private String url;
	@Column(column = "position")
	private float position;

	public DBEntityCamera() {

	}

	public DBEntityCamera(int id, String name, double longitude, double latitude, String url, float position) {
		super();
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.url = url;
		this.position = position;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

}
