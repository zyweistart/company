package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.service.DatabaseProvider;

public class Office implements BaseColumns {
	public static final String TABLE_NAME = "office";

	public static final String COLUMN_NAME_ROOM = "r";
	public static final String COLUMN_NAME_AFFAIR = "a";
	public static final String COLUMN_NAME_STAFF = "s";
	
	private static final String SCHEME = "content://";

	public static final String PATH_OFFICES = "offices";

	public static final String PATH_OFFICE_ID = PATH_OFFICES + "/#";

	public static final int OFFICE_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_OFFICES);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_OFFICE_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.office";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.office";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_ROOM + " TEXT,"
			+ COLUMN_NAME_AFFAIR + " TEXT,"
			+ COLUMN_NAME_STAFF + " TEXT"
			+ ");";
	
	private String room;
	private String affair;
	private String staff;

	public Office(String room, String affair, String staff) {
		this.room = room;
		this.affair = affair;
		this.staff = staff;
	}

	public String getRoom() {
		return room;
	}
	public String getAffair() {
		return affair;
	}
	public String getStaff() {
		return staff;
	}
	
	
}
