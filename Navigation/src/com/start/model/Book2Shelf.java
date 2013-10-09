package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;

/**
 * Database help class that represents a mapping record of book to shelf
 * 
 *
 */
public class Book2Shelf implements BaseColumns {
	public static final String TABLE_NAME = "book_2_shelf";

	private static final String SCHEME = "content://";

	public static final String PATH_BOOK_2_SHELF = "book_2_shelf";

	public static final String PATH_BOOK_2_SHELF_ID = PATH_BOOK_2_SHELF + "/";

	public static final int SHELF_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BOOK_2_SHELF);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BOOK_2_SHELF_ID);

	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BOOK_2_SHELF_ID + "#");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.book_2_shelf";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.book_2_shelf";

	public static final String COLUMN_NAME_NUM = "n";
	
	public static final String COLUMN_NAME_INDEX = "i";
	
	public static final String COLUMN_NAME_BUILDING = "b";

	public static final String COLUMN_NAME_FLOOR = "f";	
	
	public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" 
			+ _ID + " INTEGER PRIMARY KEY," 
			+ COLUMN_NAME_NUM + " INTEGER," 
			+ COLUMN_NAME_INDEX + " TEXT,"
			+ COLUMN_NAME_BUILDING + " TEXT,"
			+ COLUMN_NAME_FLOOR + " INTEGER"
			+ ");";
}
