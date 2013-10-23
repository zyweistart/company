package com.start.model;

import org.mapsforge.core.model.GeoPoint;

import com.start.core.CoreModel;

public class Room extends CoreModel {
	
	public static String TABLE_NAME="ST_ROOM";
	
	public static final String COLUMN_NAME_MAPID="mapId";
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_VERTEXID="vertextId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_MAPID + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_VERTEXID + " TEXT"
			+ ");";
	
	/**
	 * 房间名字
	 */
	private String name;
	/**
	 * 所属地图编号
	 */
	private String mapId;
	/**
	 * 所属科室编号（可选）
	 */
	private String departmentId;
	/**
	 * 导航中心点（Vertex对象）
	 */
	private String vertextId;
	/**
	 * 坐标区域
	 */
	private double[][] area;
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMapId() {
		return mapId;
	}


	public void setMapId(String mapId) {
		this.mapId = mapId;
	}


	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getVertextId() {
		return vertextId;
	}


	public void setVertextId(String vertextId) {
		this.vertextId = vertextId;
	}

	public double[][] getArea() {
		return area;
	}

	public void setArea(double[][] area) {
		this.area = area;
	}

	public boolean contains(GeoPoint geoPoint) {
		return false;
	}
}
