package com.start.model.medmap;

import org.mapsforge.core.model.GeoPoint;

import com.start.core.CoreModel;

public class Room extends CoreModel {
	
public static String TABLE_NAME="ST_DOCTOR";
	
	public static String COLUMN_NAME_MAPID="mapId";
	public static String COLUMN_NAME_NAME="name";
	public static String COLUMN_NAME_DEPARTMENTID="departmentId";
	public static String COLUMN_NAME_VERTEXID="vertextId";
	public static String COLUMN_NAME_LEFT="left";
	public static String COLUMN_NAME_TOP="top";
	public static String COLUMN_NAME_RIGHT="right";
	public static String COLUMN_NAME_BOTTOM="bottom";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_MAPID + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_DEPARTMENTID + " TEXT,"
			+ COLUMN_NAME_VERTEXID + " TEXT,"
			+ COLUMN_NAME_LEFT + " TEXT,"
			+ COLUMN_NAME_TOP + " TEXT,"
			+ COLUMN_NAME_RIGHT + " TEXT,"
			+ COLUMN_NAME_BOTTOM + " TEXT"
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
	 * 多边形bounding box的左限
	 */
	private double left;
	/**
	 * 多边形bounding box的上限
	 */
	private double top;
	/**
	 * 多边形bounding box的右限
	 */
	private double right;
	/**
	 * 多边形bounding box的下限
	 */
	private double bottom;
	
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


	public double getLeft() {
		return left;
	}


	public void setLeft(double left) {
		this.left = left;
	}


	public double getTop() {
		return top;
	}


	public void setTop(double top) {
		this.top = top;
	}


	public double getRight() {
		return right;
	}


	public void setRight(double right) {
		this.right = right;
	}


	public double getBottom() {
		return bottom;
	}


	public void setBottom(double bottom) {
		this.bottom = bottom;
	}


	public boolean contains(GeoPoint geoPoint) {
		return false;
	}
}
