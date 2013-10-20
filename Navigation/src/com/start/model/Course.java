package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;

public class Course implements BaseColumns {	

	public static final String TABLE_NAME = "course";
	
	private static final String SCHEME = "content://";

	public static final String PATH_COURSES = "courses";

	public static final String PATH_COURSE_ID = PATH_COURSES + "/#";

	public static final int COURSE_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_COURSES);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_COURSE_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.course";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.course";
	

	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_DESC = "desc";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_DESC + " TEXT"
			+ ");";
	
	private String name;
	private String desc;

	public Course(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	
	
}
