package com.start.model;

import com.start.core.CoreModel;


public class Vertex extends CoreModel {
	
	public static final String TABLE_NAME="ST_VERTEX";
	
	public static final String COLUMN_NAME_MAPID="mapId";
	public static final String COLUMN_NAME_LATITUDE="latitude";
	public static final String COLUMN_NAME_LONGITUDE="longitude";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_MAPID + " TEXT,"
			+ COLUMN_NAME_LATITUDE + " TEXT,"
			+ COLUMN_NAME_LONGITUDE + " TEXT"
			+ ");";
	
	/**
	 * 所属地图编号
	 */
	private int mapId;
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 经度
	 */
	private double longitude;
	
	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
