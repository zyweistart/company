package com.start.model;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;

public class Edge implements BaseColumns {

	public static final String COLUMN_NAME_START = "s";
	public static final String COLUMN_NAME_END = "e";
	public static final String COLUMN_NAME_BUILDING = "b";	

	public static final String TABLE_NAME = "edge";

	private static final String SCHEME = "content://";

	public static final String PATH_EDGES = "edges";

	public static final String PATH_EDGE_ID = PATH_EDGES + "/#";

	public static final int EDGE_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_EDGES);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_EDGE_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.edge";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.edge";

	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_START + " TEXT,"
			+ COLUMN_NAME_END + " TEXT,"
			+ COLUMN_NAME_BUILDING + " TEXT"
			+ ");";

}
