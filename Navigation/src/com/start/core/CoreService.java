package com.start.core;

import java.util.Map;

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
	
	public void save(String tableName,Map<String,String> mValues){
		SQLiteDatabase sdb=this.dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues(mValues.size());
		for(String key:mValues.keySet()){
			values.put(key, mValues.get(key));
		}
		sdb.insert(tableName, null, values);
	}
	
}
