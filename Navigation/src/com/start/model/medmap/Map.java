package com.start.model.medmap;

import com.start.core.CoreModel;


public class Map extends CoreModel {
	
	public static String TABLE_NAME="ST_MAP";
	
	public static String COLUMN_NAME_NAME="name";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
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