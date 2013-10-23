package com.start.model;

import com.start.core.CoreModel;

public class RoomArea extends CoreModel {

	public static final String TABLE_NAME="ST_ROOMAREA";
	
	public static final String COLUMN_NAME_ROOMID="roomId";
	public static final String COLUMN_NAME_LATITUDE="latitude";
	public static final String COLUMN_NAME_LONGITUDE="longitude";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_LATITUDE + " TEXT,"
			+ COLUMN_NAME_LONGITUDE + " TEXT,"
			+ COLUMN_NAME_ROOMID + " TEXT"
			+ ");";

	private String roomId;
	
	private String vertexId;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getVertexId() {
		return vertexId;
	}

	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}
	
}