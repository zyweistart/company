package com.start.service;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.start.model.Book2Shelf;
import com.start.model.Building;
import com.start.model.ClassInfo;
import com.start.model.Course;
import com.start.model.Edge;
import com.start.model.Office;
import com.start.model.Room;
import com.start.model.Shelf;
import com.start.model.Teacher;
import com.start.model.Vertex;

public class DatabaseProvider extends ContentProvider {

	public static final String AUTHORITY = "com.example.provider.MapNavigation";
	private static final String DATABASE_NAME = "map_navigation.db";
	private static final int DATABASE_VERSION = 1;

	private static final int BOOK_2_SHELF = 1;
	private static final int BUILDINGS = 2;
	private static final int BUILDING = 3;
	private static final int ROOMS = 4;
	private static final int ROOM = 5;
	private static final int VERTEXES = 6;
	private static final int EDGES = 7;
	private static final int CLASSES = 8;
	private static final int OFFICES = 9;
	private static final int COURSES = 10;
	private static final int TEACHERS = 11;
	private static final int SHELFS = 12;
	private static final int SHELF = 13;

	private static final UriMatcher sUriMatcher;

	private DatabaseHelper mOpenHelper;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, Book2Shelf.PATH_BOOK_2_SHELF, BOOK_2_SHELF);
		sUriMatcher.addURI(AUTHORITY, Shelf.PATH_SHELF, SHELFS);
		sUriMatcher.addURI(AUTHORITY, Shelf.PATH_SHELF_ID, SHELF);
		sUriMatcher.addURI(AUTHORITY, Building.PATH_BUILDINGS, BUILDINGS);
		sUriMatcher.addURI(AUTHORITY, Building.PATH_BUILDING_ID, BUILDING);
		sUriMatcher.addURI(AUTHORITY, Room.PATH_ROOMS, ROOMS);
		sUriMatcher.addURI(AUTHORITY, Room.PATH_ROOM_ID, ROOM);
		sUriMatcher.addURI(AUTHORITY, Vertex.PATH_VETTEXES, VERTEXES);
		sUriMatcher.addURI(AUTHORITY, Edge.PATH_EDGES, EDGES);
		sUriMatcher.addURI(AUTHORITY, ClassInfo.PATH_CLASSES, CLASSES);
		sUriMatcher.addURI(AUTHORITY, Office.PATH_OFFICES, OFFICES);
		sUriMatcher.addURI(AUTHORITY, Course.PATH_COURSES, COURSES);
		sUriMatcher.addURI(AUTHORITY, Teacher.PATH_TEACHERS, TEACHERS);
	}

	static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Book2Shelf.CREATE_TABLE_SQL);
			db.execSQL(Building.CREATE_TABLE_SQL);
			db.execSQL(Room.CREATE_TABLE_SQL);
			db.execSQL(Vertex.CREATE_TABLE_SQL);
			db.execSQL(Edge.CREATE_TABLE_SQL);
			db.execSQL(ClassInfo.CREATE_TABLE_SQL);
			db.execSQL(Office.CREATE_TABLE_SQL);
			db.execSQL(Course.CREATE_TABLE_SQL);
			db.execSQL(Teacher.CREATE_TABLE_SQL);
			db.execSQL(Shelf.CREATE_TABLE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Book2Shelf.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Building.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Room.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Vertex.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Edge.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + ClassInfo.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Office.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Teacher.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Shelf.TABLE_NAME);
			onCreate(db);
		}

	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		throw new SQLException("Unknown URI " + uri);
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case BOOK_2_SHELF:
			return Book2Shelf.CONTENT_TYPE;
		case BUILDINGS:
			return Building.CONTENT_TYPE;
		case BUILDING:
			return Building.CONTENT_ITEM_TYPE;
		case ROOMS:
			return Room.CONTENT_TYPE;
		case ROOM:
			return Room.CONTENT_ITEM_TYPE;
		case SHELF:
			return Shelf.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (values == null) {
			values = new ContentValues();
		}

		String tableName = null;
		Uri baseUri = null;
		switch (sUriMatcher.match(uri)) {
		case BOOK_2_SHELF:
			tableName = Book2Shelf.TABLE_NAME;
			baseUri = Book2Shelf.CONTENT_ID_URI_BASE;
			break;
		case BUILDINGS:
			tableName = Building.TABLE_NAME;
			baseUri = Building.CONTENT_ID_URI_BASE;
			break;
		case ROOMS:
			tableName = Room.TABLE_NAME;
			baseUri = Room.CONTENT_ID_URI_BASE;
			break;
		case VERTEXES:
			tableName = Vertex.TABLE_NAME;
			baseUri = Vertex.CONTENT_ID_URI_BASE;
			break;
		case EDGES:
			tableName = Edge.TABLE_NAME;
			baseUri = Edge.CONTENT_ID_URI_BASE;
			break;
		case CLASSES:
			tableName = ClassInfo.TABLE_NAME;
			baseUri = ClassInfo.CONTENT_ID_URI_BASE;
			break;
		case OFFICES:
			tableName = Office.TABLE_NAME;
			baseUri = Office.CONTENT_ID_URI_BASE;
			break;
		case COURSES:
			tableName = Course.TABLE_NAME;
			baseUri = Course.CONTENT_ID_URI_BASE;
			break;
		case TEACHERS:
			tableName = Teacher.TABLE_NAME;
			baseUri = Teacher.CONTENT_ID_URI_BASE;
			break;
		case SHELFS:
			tableName = Shelf.TABLE_NAME;
			baseUri = Shelf.CONTENT_ID_URI_BASE;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(tableName, null, values);

		if (rowId > 0) {
			Uri insertedUri = ContentUris.withAppendedId(baseUri, rowId);
			return insertedUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case BOOK_2_SHELF:
			qb.setTables(Book2Shelf.TABLE_NAME);
			break;
		case SHELFS:
			qb.setTables(Shelf.TABLE_NAME);
			break;
		case SHELF:
			qb.setTables(Shelf.TABLE_NAME);
			selection = Shelf._ID + "=" + uri.getPathSegments().get(Shelf.SHELF_ID_PATH_POSITION);
			break;	
		case BUILDINGS:
			qb.setTables(Building.TABLE_NAME);
			break;
		case BUILDING:
			qb.setTables(Building.TABLE_NAME);
			selection = Building._ID + "=" + uri.getPathSegments().get(Building.BUILDING_ID_PATH_POSITION);
			break;
		case ROOMS:
			qb.setTables(Room.TABLE_NAME);
			break;
		case ROOM:
			qb.setTables(Room.TABLE_NAME);
			selection = Room._ID + "=" + uri.getPathSegments().get(Room.ROOM_ID_PATH_POSITION);
			break;
		case VERTEXES:
			qb.setTables(Vertex.TABLE_NAME);
			break;
		case EDGES:
			qb.setTables(Edge.TABLE_NAME);
			break;
		case CLASSES:
			qb.setTables(ClassInfo.TABLE_NAME);
			break;
		case OFFICES:
			qb.setTables(Office.TABLE_NAME);
			break;
		case COURSES:
			qb.setTables(Course.TABLE_NAME);
			break;
		case TEACHERS:
			qb.setTables(Teacher.TABLE_NAME);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		throw new SQLException("Unknown URI " + uri);
	}

}
