package com.start.core;

import com.start.navigation.AppContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CoreService {

	private Context mContext;
	private SQLiteDBHelper dbHelper;
	private AppContext mAppContext;
	
	public CoreService(Context context){
		this.mContext=context;
		this.mAppContext=AppContext.getInstance();
		this.dbHelper=new SQLiteDBHelper(mContext);
	}
	
	public Context getmContext() {
		return mContext;
	}

	public SQLiteDBHelper getDbHelper() {
		return dbHelper;
	}

	public void insert(String tableName,ContentValues values){
		SQLiteDatabase sdb=null;
		try{
			sdb=this.dbHelper.getWritableDatabase();
			sdb.insert(tableName, null, values);
		}finally{
			if(sdb!=null){
				sdb.close();
			}
		}
	}
	
	public void update(String tableName,ContentValues values,String whereClause, String[] whereArgs){
		SQLiteDatabase sdb=null;
		try{
			sdb=this.dbHelper.getWritableDatabase();
			sdb.update(tableName, values, whereClause, whereArgs);
		}finally{
			if(sdb!=null){
				sdb.close();
			}
		}
	}
	
	public String getCurrentDataNo(){
		return mAppContext.getCurrentDataNo();
	}
	
}
