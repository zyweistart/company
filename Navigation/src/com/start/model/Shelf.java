package com.start.model;

import org.mapsforge.core.model.GeoPoint;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.start.service.DatabaseProvider;
import com.start.utils.Utils;

/**
 * 书架
 * @author start
 *
 */
public class Shelf implements BaseColumns, POI {

	private static final long serialVersionUID = 9106583482717793956L;

	public static final String TABLE_NAME = "shelf";
	
	private static final String SCHEME = "content://";

	public static final String PATH_SHELF = "shelf";

	public static final String PATH_SHELF_ID = PATH_SHELF + "/#";

	public static final int SHELF_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_SHELF);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_SHELF_ID);

	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_SHELF_ID + "#");

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.shelf";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.shelf";
	
	public static final String COLUMN_NAME_LATITUDE1 = "la1";

	public static final String COLUMN_NAME_LONGITUDE1 = "lo1";

	public static final String COLUMN_NAME_LATITUDE2 = "la2";

	public static final String COLUMN_NAME_LONGITUDE2 = "lo2";

	public static final String COLUMN_NAME_VERTEX = "v";	

	public static final String COLUMN_NAME_NUM = "n";

	public static final String COLUMN_NAME_BUILDING = "b";

	public static final String COLUMN_NAME_FLOOR = "f";	
	
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID +" INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NUM + " INTEGER,"
			+ COLUMN_NAME_LATITUDE1 + " DOUBLE,"
			+ COLUMN_NAME_LONGITUDE1 + " DOUBLE,"
			+ COLUMN_NAME_LATITUDE2 + " DOUBLE,"
			+ COLUMN_NAME_LONGITUDE2 + " DOUBLE,"
			+ COLUMN_NAME_VERTEX + " TEXT,"
			+ COLUMN_NAME_BUILDING + " TEXT,"
			+ COLUMN_NAME_FLOOR + " INTEGER"
			+ ");";
	
	private double latitude1, longitude1, latitude2, longitude2;
	private int floor;
	private int num;
	private String building, index, vertex;
	private long id;

	public Shelf(int num, String index, String building, int floor) {
		this.num = num;
		this.building = building;
		this.floor = floor;
		this.index = index;
	}
	
	public Shelf(int num, String building, int floor, double latitude1, double longitude1, double latitude2, double longitude2, String vertex) {
		this(num, "", building, floor);
		this.latitude1 = latitude1;
		this.longitude1 = longitude1;
		this.latitude2 = latitude2;
		this.longitude2 = longitude2;
		this.vertex = vertex;
	}

	@Override
	public GeoPoint getGeoPoint() {
		return Utils.toMFPoint((latitude2 + latitude1) * 0.5, (longitude1 + longitude2) * 0.5);
	}

	@Override
	public boolean inside(GeoPoint g) {
		return latitude2 < g.latitude && g.latitude < latitude1 && longitude1 < g.longitude && g.longitude < longitude2;
	}

	@Override
	public String getName() {
		if (TextUtils.isEmpty(index)) {
			return String.valueOf(num);
		}
		return index;
	}

	@Override
	public String getVertex() {
		return vertex;
	}

	@Override
	public int getFloor() {
		return floor;
	}

	@Override
	public String getBuilding() {
		return building;
	}

	public long getID() {
		return id;
	}

	public int getNum() {
		return num;
	}

	public static Shelf newInstance(Cursor cr) {
		Shelf shelf = new Shelf(cr.getInt(cr.getColumnIndex(COLUMN_NAME_NUM)),
				cr.getString(cr.getColumnIndex(COLUMN_NAME_BUILDING)),
				cr.getInt(cr.getColumnIndex(COLUMN_NAME_FLOOR)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LATITUDE1)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LONGITUDE1)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LATITUDE2)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LONGITUDE2)),
				cr.getString(cr.getColumnIndex(COLUMN_NAME_VERTEX)));
		return shelf;
	}
}
