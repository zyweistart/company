package com.start.core;

import java.util.HashMap;

public class AppConfig {

	// Configuration directory
	public static final String CONFIG_DATA_PATH_MEDMAP = "med_data";
	
	public static final String F_DEPARTMENT="department.txt";
	public static final String F_DEPARTMENTHASROOM="department_has_room.txt";
	public static final String F_DOCTOR="doctor.txt";
	public static final String F_EDGE="edge.txt";
	public static final String F_MAPDATA="mapdata.txt";
	public static final String F_ROOM="room.txt";
	public static final String F_ROOMAREA="roomarea.txt";
	public static final String F_VERTEX="vertex.txt";
	
	public static final String F_MAPMAIN="main.map";
	public static final String F_MAP0201="0201.map";
	public static final String F_MAP0202="0202.map";
	
	// Configuration file type
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_DEPARTMENT = 0;
	public static final int TYPE_ROOM = 1;
	public static final int TYPE_MAPDATA = 2;
	public static final int TYPE_VERTEX = 3;
	public static final int TYPE_DEPARTMENTHASROOM = 4;
	public static final int TYPE_EDGE = 5;
	public static final int TYPE_DOCTOR =6;
	public static final int TYPE_ROOMAREA =7;
	public static final int TYPE_MAP = 8;

	// File extension matcher
	private static final HashMap<String, Integer> mFileExtMatcher = new HashMap<String, Integer>();

	static {
		mFileExtMatcher.put(F_DEPARTMENT, TYPE_DEPARTMENT);
		mFileExtMatcher.put(F_DEPARTMENTHASROOM, TYPE_DEPARTMENTHASROOM);
		mFileExtMatcher.put(F_DOCTOR, TYPE_DOCTOR);
		mFileExtMatcher.put(F_EDGE, TYPE_EDGE);
		mFileExtMatcher.put(F_MAPDATA, TYPE_MAPDATA);
		mFileExtMatcher.put(F_ROOM, TYPE_ROOM);
		mFileExtMatcher.put(F_ROOMAREA, TYPE_ROOMAREA);
		mFileExtMatcher.put(F_VERTEX, TYPE_VERTEX);
		
		mFileExtMatcher.put(F_MAPMAIN, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0201, TYPE_MAP);
		mFileExtMatcher.put(F_MAP0202, TYPE_MAP);
	}

	/**
	 * Get file type by file name according to its extension
	 * 
	 * @param fileName
	 * @return file type
	 */
	public static int getFileType(String fileName) {
		if (mFileExtMatcher.containsKey(fileName)) {
			return mFileExtMatcher.get(fileName);
		} else {
			return TYPE_UNKNOWN;
		}
	}

	/**
	 * Get floor that the file belongs to according to file name
	 * 
	 * @param fileName
	 * @return the floor that contained in file name
	 */
	public static int getFloorByFileName(String fileName) {
		String floorStr = fileName.substring(0, fileName.indexOf('.'));
		try {
			return Integer.valueOf(floorStr);
		} catch (NumberFormatException nfe) {
			return Integer.MAX_VALUE;
		}
	}
}