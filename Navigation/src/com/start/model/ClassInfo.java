package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;

public class ClassInfo implements BaseColumns, AbsClass {
	
	public static final String TABLE_NAME = "class_info";

	private static final String SCHEME = "content://";

	public static final String PATH_CLASSES = "classes";

	public static final String PATH_CLASS_ID = PATH_CLASSES + "/#";

	public static final int CLASS_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_CLASSES);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_CLASS_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.class";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.class";
	
	public static final String COLUMN_NAME_ROOM = "room";
	public static final String COLUMN_NAME_DAY = "day";
	public static final String COLUMN_NAME_DURATION = "duration";
	public static final String COLUMN_NAME_COURSE = "course";
	public static final String COLUMN_NAME_TEACHER = "teacher";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_ROOM + " TEXT,"
			+ COLUMN_NAME_DAY + " INTEGER,"
			+ COLUMN_NAME_DURATION + " TEXT,"
			+ COLUMN_NAME_COURSE + " TEXT,"
			+ COLUMN_NAME_TEACHER + " TEXT"
			+ ");";
	
	private String room;
	private String duration;
	private int day;
	private String course;
	private String teacher;

	public ClassInfo(int day, String duration, String course, String teacher) {
		this.day = day;
		this.duration = duration;
		this.course = course;
		this.teacher = teacher;
	}

	public String getRoom() {
		return room;
	}
	public String getDuration() {
		return duration;
	}
	
	@Override
	public String getCourse() {
		return course;
	}
	
	@Override
	public String getTeacher() {
		return teacher;
	}

	@Override
	public int compareTo(AbsClass another) {
		ClassInfo anotherClass = (ClassInfo) another;
		int result = day - anotherClass.day;
		if (result == 0) {
			result = duration.compareTo(anotherClass.duration);
		}
		return result;
	}

	@Override
	public int getWeekday() {
		return day;
	}

}
