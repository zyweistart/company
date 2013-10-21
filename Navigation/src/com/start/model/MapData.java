package com.start.model;

import com.start.core.CoreModel;


public class MapData extends CoreModel {
	
	public static final String TABLE_NAME="ST_MAPDATA";
	
	public static final String COLUMN_NAME_NAME="name";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_NAME + " TEXT"
			+ ");";
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}