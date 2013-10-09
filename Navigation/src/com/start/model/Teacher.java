package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;

public class Teacher implements BaseColumns {
	
	public static final String TABLE_NAME = "teacher";
	
	private static final String SCHEME = "content://";

	public static final String PATH_TEACHERS = "teachers";

	public static final String PATH_TEACHER_ID = PATH_TEACHERS + "/#";

	public static final int TEACHER_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_TEACHERS);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_TEACHER_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.teacher";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.teacher";
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

	public Teacher(String name, String desc) {
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
