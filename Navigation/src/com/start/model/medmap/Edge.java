package com.start.model.medmap;

import com.start.core.CoreModel;

public class Edge extends CoreModel {

	public static String TABLE_NAME="ST_EDGE";
	
	public static String COLUMN_NAME_VERTEXSTARTID="vertexStartId";
	public static String COLUMN_NAME_VERTEXENDID="vertexEndId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_VERTEXSTARTID + " TEXT,"
			+ COLUMN_NAME_VERTEXENDID + " TEXT"
			+ ");";

	private String vertexStartId;
	
	private String vertexEndId;

	public String getVertexStartId() {
		return vertexStartId;
	}

	public void setVertexStartId(String vertexStartId) {
		this.vertexStartId = vertexStartId;
	}

	public String getVertexEndId() {
		return vertexEndId;
	}

	public void setVertexEndId(String vertexEndId) {
		this.vertexEndId = vertexEndId;
	}
	
}