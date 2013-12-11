package com.start.model;

import com.start.core.CoreModel;


public class MapData extends CoreModel {
	
	public static final String TABLE_NAME="ST_MAPDATA";
	
	public static final String COLUMN_NAME_NAME="name";
	public static final String COLUMN_NAME_VERTEXID="vertextId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_NO + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_VERTEXID + " TEXT"
			+ ");";
	
	/**
	 * 地图名称
	 */
	private String name;
	/**
	 * 地图数据的初始点
	 */
	private String vertexId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVertexId() {
		return vertexId;
	}

	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}
	
}