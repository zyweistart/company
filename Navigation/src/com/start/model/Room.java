package com.start.model;

import java.util.ArrayList;

import org.mapsforge.core.model.GeoPoint;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.start.service.DatabaseProvider;
import com.start.utils.Utils;

public class Room implements BaseColumns, Searchable, POI {
	
	private static final long serialVersionUID = 1253052028855824222L;	

	public static final String TABLE_NAME = "rooms";

	private static final String SCHEME = "content://";

	public static final String PATH_ROOMS = "rooms";

	public static final String PATH_ROOM_ID = PATH_ROOMS + "/#";

	public static final int ROOM_ID_PATH_POSITION = 1;

	public static final Uri CONTENT_URI = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_ROOMS);

	public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + DatabaseProvider.AUTHORITY + "/" + PATH_ROOM_ID);

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.room";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.room";

	public static final String COLUMN_NAME_BUILDING = "building";
	
	public static final String COLUMN_NAME_FLOOR = "floor";
	
	public static final String COLUMN_NAME_NAME = "name";
	
	public static final String COLUMN_NAME_TYPE = "type";
	
	public static final String COLUMN_NAME_LATITUDE1 = "latitude1";

	public static final String COLUMN_NAME_LONGITUDE1 = "longitude1";
	
	public static final String COLUMN_NAME_LATITUDE2 = "latitude2";

	public static final String COLUMN_NAME_LONGITUDE2 = "longitude2";
	
	public static final String COLUMN_NAME_VERTEX = "vertex";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY,"
			+ COLUMN_NAME_NAME + " TEXT,"
			+ COLUMN_NAME_BUILDING + " TEXT,"
			+ COLUMN_NAME_FLOOR + " INTEGER,"
			+ COLUMN_NAME_VERTEX + " TEXT,"
			+ COLUMN_NAME_TYPE + " INTEGER,"
			+ COLUMN_NAME_LATITUDE1 + " DOUBLE,"
			+ COLUMN_NAME_LATITUDE2 + " DOUBLE,"
			+ COLUMN_NAME_LONGITUDE1 + " DOUBLE,"
			+ COLUMN_NAME_LONGITUDE2 + " DOUBLE"
			+ ");";
		
	
	public static final int OFFICE = 0, CLASSROOM = 1, UNKNOWN = 2;
	private double latitude1, latitude2, longitude1, longitude2;
	private int floor, type;
	private long id;
	private String building, name, vertex;

	private Room(long id, String name, int type, double latitude1, double longitude1, double latitude2, double longitude2, 
			String building, int floor, String vertex) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.latitude1 = latitude1;
		this.longitude1 = longitude1;
		this.latitude2 = latitude2;
		this.longitude2 = longitude2;
		this.building = building;
		this.floor = floor;
		this.vertex = vertex;
	}

	public String getHeader() {
		return name;
	}

	public String getSubHeader() {
		return "";
	}

	public static Room[][] load(Context context, Building building) {
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cr = contentResolver.query(CONTENT_URI, null, COLUMN_NAME_BUILDING + "=?", new String[] { building.getName() }, null);
		
		ArrayList<Room> roomList = new ArrayList<Room>();
		if (cr.moveToNext()) {
			int name = cr.getColumnIndex(COLUMN_NAME_NAME);
			int type = cr.getColumnIndex(COLUMN_NAME_TYPE);
			int latitude1 = cr.getColumnIndex(COLUMN_NAME_LATITUDE1);
			int latitude2 = cr.getColumnIndex(COLUMN_NAME_LATITUDE2);
			int longitude1 = cr.getColumnIndex(COLUMN_NAME_LONGITUDE1);
			int longitude2 = cr.getColumnIndex(COLUMN_NAME_LONGITUDE2);
			int vertex = cr.getColumnIndex(COLUMN_NAME_VERTEX);
			int floor = cr.getColumnIndex(COLUMN_NAME_FLOOR);
			int id = cr.getColumnIndex(_ID);

			do {
				Room r = new Room(cr.getLong(id), cr.getString(name), cr.getInt(type), cr.getDouble(latitude1), cr.getDouble(longitude1),
						cr.getDouble(latitude2), cr.getDouble(longitude2), building.getName(), cr.getInt(floor), cr.getString(vertex));
				roomList.add(r);
			} while (cr.moveToNext());						
		}
		cr.close();

		if (roomList.size() > 0) {
			int[] roomsCount = new int[building.getFloorCount()];

			for (Room r : roomList) {
				int index = r.getFloor() - building.getMinFloor();
				roomsCount[index]++;
			}

			Room[][] roomArray = new Room[roomsCount.length][];
			for (int i = 0; i < roomsCount.length; i++) {
				roomArray[i] = new Room[roomsCount[i]];
			}

			for (Room r : roomList) {
				int index = r.getFloor() - building.getMinFloor();
				roomArray[index][--roomsCount[index]] = r;
			}
			return roomArray;
		}
		return null;
	}

	@Override
	public GeoPoint getGeoPoint() {
		return Utils.toMFPoint((latitude1 + latitude2) * 0.5, (longitude1 + longitude2) * 0.5);
	}

	@Override
	public boolean inside(GeoPoint g) {
		double latitude = g.latitude, longitude = g.longitude;
		return (latitude2 < latitude && latitude < latitude1 && longitude1 < longitude && longitude < longitude2);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVertex() {
		return vertex;
	}

	@Override
	public int getFloor() {
		return floor;
	}

	public int getType() {
		return type;
	}

	@Override
	public String getBuilding() {
		return building;
	}

	public long getID() {
		return id;
	}

	public static Room newInstance(Cursor cr) {
		Room r = new Room(cr.getInt(cr.getColumnIndex(_ID)), cr.getString(cr.getColumnIndex(COLUMN_NAME_NAME)), 
				cr.getInt(cr.getColumnIndex(COLUMN_NAME_TYPE)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LATITUDE1)), cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LONGITUDE1)),
				cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LATITUDE2)), cr.getDouble(cr.getColumnIndex(COLUMN_NAME_LONGITUDE2)),
				cr.getString(cr.getColumnIndex(COLUMN_NAME_BUILDING)), cr.getInt(cr.getColumnIndex(COLUMN_NAME_FLOOR)),
				cr.getString(cr.getColumnIndex(COLUMN_NAME_VERTEX)));
		return r;
	}
}
