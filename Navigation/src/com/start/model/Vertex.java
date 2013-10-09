package com.start.model;

import org.mapsforge.core.model.GeoPoint;

import android.net.Uri;
import android.provider.BaseColumns;

import com.start.navigation.DatabaseProvider;
import com.start.utils.Utils;

/**
 * 位置点
 * @author start
 *
 */
public class Vertex implements BaseColumns {
	public static final String COLUMN_NAME_NAME = "n";
	public static final String COLUMN_NAME_LATITUDE = "y";
	public static final String COLUMN_NAME_LONGITUDE = "x";
	public static final String COLUMN_NAME_TYPE = "t";
	public static final String COLUMN_NAME_FLOOR = "f";
	public static final String COLUMN_NAME_BUILDING = "b";	

	public static final String TABLE_NAME = "vertex";

	private static final String SCHEME = "content://";

	public static final String PATH_VETTEXES = "vertexes";

	public static final String PATH_VERTEX_ID = PATH_VETTEXES + "/#";

	public static final int VERTEX_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_VETTEXES);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_VERTEX_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.vertex";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.vertex";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_BUILDING + " TEXT,"
			+ COLUMN_NAME_FLOOR + " INTEGER,"
			+ COLUMN_NAME_TYPE + " INTEGER,"
			+ COLUMN_NAME_LONGITUDE + " DOUBLE,"
			+ COLUMN_NAME_LATITUDE + " DOUBLE"
			+ ");";
	
	private String name, building;
	private double latitude, longitude;
	private int type, floor;
	private GeoPoint point;
	
	public GeoPoint getGeoPoint() {
		if (point == null) {
			point = Utils.toMFPoint(latitude, longitude);
		}
		return point;
	}

	public String getName() {
		return name;
	}

	public String getBuilding() {
		return building;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getType() {
		return type;
	}

	public int getFloor() {
		return floor;
	}

	public Vertex(String name, double latitude,
			double longitude, int type, int floor, String building) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.floor = floor;
		this.building = building;
	}

}
