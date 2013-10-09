package com.start.model;

import java.io.Serializable;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.start.navigation.DatabaseProvider;

/**
 * 建筑
 * @author start
 *
 */
public class Building implements BaseColumns, Serializable {
	
	private static final long serialVersionUID = 6095055809218064493L;
	
	private Building() {
	}

	public static final String TABLE_NAME = "buildings";

	private static final String SCHEME = "content://";

	public static final String PATH_BUILDINGS = "buildings";

	public static final String PATH_BUILDING_ID = PATH_BUILDINGS + "/#";

	public static final int BUILDING_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BUILDINGS);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BUILDING_ID);

	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_BUILDING_ID + "#");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.building";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.building";

	public static final String COLUMN_NAME_NAME = "name";
	
	public static final String COLUMN_NAME_DESC = "desc";
	
	public static final String COLUMN_NAME_LATITUDE = "latitude";

	public static final String COLUMN_NAME_LONGITUDE = "longitude";
	
	public static final String COLUMN_NAME_MIN_FLOOR = "min_floor";
	
	public static final String COLUMN_NAME_MAX_FLOOR = "max_floor";
	
	public static final String COLUMN_NAME_GATE = "gate";
	
	public static final String CREATE_TABLE_SQL = "CREATE TABLE " + Building.TABLE_NAME + " (" 
			+ Building._ID + " INTEGER PRIMARY KEY," 
			+ Building.COLUMN_NAME_NAME + " TEXT," 
			+ Building.COLUMN_NAME_DESC + " TEXT,"
			+ Building.COLUMN_NAME_LATITUDE + " DOUBLE,"
			+ Building.COLUMN_NAME_LONGITUDE + " DOUBLE,"
			+ Building.COLUMN_NAME_MIN_FLOOR + " INTEGER,"
			+ Building.COLUMN_NAME_MAX_FLOOR + " INTEGER,"
			+ Building.COLUMN_NAME_GATE + " TEXT"
			+ ");";
	
	private String name;
	private String desc;
	private double longitude;
	private double latitude;
	private int maxFloor;
	private int minFloor;
	private long id;
	private String gate;
	private transient GeoPoint point;

	public static Building newInstance(Context context, String buildingName) {
		Cursor cr = context.getContentResolver().query(CONTENT_URI, null, COLUMN_NAME_NAME + "=?", new String[] { buildingName }, null);
		Building b = null;
		if (cr.moveToNext()) {
			b = newInstance(cr);
		}
		cr.close();
		return b;
	}

	public static Building newInstance(Cursor cr) {
		int id = cr.getColumnIndex(_ID);
		int desc = cr.getColumnIndex(COLUMN_NAME_DESC);
		int maxFloor = cr.getColumnIndex(COLUMN_NAME_MAX_FLOOR);
		int minFloor = cr.getColumnIndex(COLUMN_NAME_MIN_FLOOR);
		int name = cr.getColumnIndex(COLUMN_NAME_NAME);
		int longitude = cr.getColumnIndex(COLUMN_NAME_LONGITUDE);
		int latitude = cr.getColumnIndex(COLUMN_NAME_LATITUDE);
		int gate = cr.getColumnIndex(COLUMN_NAME_GATE);

		Building b = new Building(
				cr.getLong(id), cr.getString(name), cr.getString(desc), cr.getDouble(latitude), cr.getDouble(longitude), 
				cr.getInt(minFloor), cr.getInt(maxFloor), cr.getString(gate));
		return b;
	}

	private Building(long id, String name, String desc, double latitude, double longitude, int minFloor, int maxFloor, String gate) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.latitude = latitude;
		this.longitude = longitude;
		this.minFloor = minFloor;
		this.maxFloor = maxFloor;
		this.gate = gate;
	}

	public GeoPoint getGeoPoint() {
		if (point == null) {
			point = new GeoPoint((int) (latitude*1E6), (int) (longitude*1E6));
		}
		return point;
	}
	
	public int getFloorCount() {
		return maxFloor - minFloor + 1;
	}	

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public int getMinFloor() {
		return minFloor;
	}
	
	public int getMaxFloor() {
		return maxFloor;
	}

	public String getGate() {
		return gate;
	}

	public long getID() {
		return id;
	}
	
}
