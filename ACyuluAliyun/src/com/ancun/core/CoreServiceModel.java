package com.ancun.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class CoreServiceModel {

	private Context context;
	
	private SQLiteDBHelper dbHelper;

	private SQLiteDatabase sqlDb;
	
	public CoreServiceModel(Context context){
		this.context=context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public SQLiteDBHelper getDbHelper() {
		if(this.dbHelper==null){
			this.dbHelper = new SQLiteDBHelper(getContext());
			getSQLiteDatabase();
		}
		return dbHelper;
	}

	public SQLiteDatabase getSQLiteDatabase(){
		if(this.sqlDb==null){
			this.sqlDb=this.getDbHelper().getWritableDatabase();
		}
		return this.sqlDb;
	}

	public void closeSQLiteDBHelper(){
		if(this.dbHelper!=null){
			this.getDbHelper().close();
			this.dbHelper=null;
		}
	}
	
}