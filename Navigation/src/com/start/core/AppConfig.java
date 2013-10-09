package com.start.core;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.util.Log;

public class AppConfig {
	/*
	 * Library configuration 
	 */
	private static final String DEBUG_TAG ="AppConfig";

	public static class Library {
		private static final String Host = "http://210.33.91.65:8080/opac/";
		private static final String QUERY_PARAMS = "openlink.php?historyCount=1&strText=%1$s&doctype=ALL&strSearchType=title&match_flag=forward&displaypg=20&sort=CATA_DATE&orderby=desc&showmode=list&dept=ALL";
	
		private static URL constructUrl(String param) {
			URL url = null;
			try {
				url = new URL(Host + param);
			} catch (MalformedURLException e) {
				Log.e(DEBUG_TAG, "wrong format url");
			}
			return url;
		}

		public static URL getQueryURL(String keyword) {
			try {
				keyword = URLEncoder.encode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return constructUrl(String.format(QUERY_PARAMS, keyword));
		}
	}

	/*
	 * Local database configurations
	 */
	// Configuration directory
	public static final String CONFIG_DATA_PATH = "raw_data";
	
	// Configuration file extension
	public static final String BUILDING_FILE_EXT = "building.csv";
	public static final String MAP_FILE_EXT = "map";
	public static final String ROOM_FILE_EXT = "room.csv";
	public static final String VERTEX_FILE_EXT = "vertex.csv";
	public static final String CLASS_FILE_EXT = "class.csv";
	public static final String OFFICE_FILE_EXT = "office.csv";
	public static final String EDGE_FILE_EXT = "edge.csv";
	public static final String COURSE_FILE_EXT = "course.csv";
	public static final String TEACHER_FILE_EXT = "teacher.csv";
	public static final String SHELF_FILE_EXT = "shelf.csv";
	public static final String BOOK_2_SHELF_FILE_EXT = "book_2_shelf.csv";
	
	// Configuration file type
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_BUILDING = 0;
	public static final int TYPE_ROOM = 1;
	public static final int TYPE_MAP = 2;
	public static final int TYPE_VERTEX = 3;
	public static final int TYPE_CLASS = 4;
	public static final int TYPE_OFFICE = 5;
	public static final int TYPE_EDGE = 6;
	public static final int TYPE_COURSE = 7;
	public static final int TYPE_TEACHER = 8;
	public static final int TYPE_SHELF =9;
	public static final int TYPE_BOOK_2_SHELF =10;

	// File extension matcher
	private static final HashMap<String, Integer> mFileExtMatcher = new HashMap<String, Integer>();

	static {
		mFileExtMatcher.put(BUILDING_FILE_EXT, TYPE_BUILDING);
		mFileExtMatcher.put(ROOM_FILE_EXT, TYPE_ROOM);
		mFileExtMatcher.put(MAP_FILE_EXT, TYPE_MAP);
		mFileExtMatcher.put(VERTEX_FILE_EXT, TYPE_VERTEX);
		mFileExtMatcher.put(CLASS_FILE_EXT, TYPE_CLASS);
		mFileExtMatcher.put(OFFICE_FILE_EXT, TYPE_OFFICE);
		mFileExtMatcher.put(EDGE_FILE_EXT, TYPE_EDGE);
		mFileExtMatcher.put(COURSE_FILE_EXT, TYPE_COURSE);
		mFileExtMatcher.put(TEACHER_FILE_EXT, TYPE_TEACHER);
		mFileExtMatcher.put(SHELF_FILE_EXT, TYPE_SHELF);
		mFileExtMatcher.put(BOOK_2_SHELF_FILE_EXT, TYPE_BOOK_2_SHELF);
	}

	/**
	 * Get file type by file name according to its extension
	 * 
	 * @param fileName
	 * @return file type
	 */
	public static int getFileType(String fileName) {
		String ext = fileName.substring(fileName.indexOf('.') + 1);
		if (mFileExtMatcher.containsKey(ext)) {
			return mFileExtMatcher.get(ext);
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
