package com.start.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CoreService {

	private Context mContext;
	private SQLiteDBHelper dbHelper;
	
	public CoreService(Context context){
		this.mContext=context;
		this.dbHelper=new SQLiteDBHelper(mContext);
	}
	
	public Context getmContext() {
		return mContext;
	}

	public SQLiteDBHelper getDbHelper() {
		return dbHelper;
	}

	public void insert(String tableName,ContentValues values){
		SQLiteDatabase sdb=this.dbHelper.getWritableDatabase();
		sdb.insert(tableName, null, values);
	}
	
}
