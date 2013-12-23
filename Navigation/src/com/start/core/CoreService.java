package com.start.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.Edge;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;
import com.start.navigation.AppContext;

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
	
	public void delete(String tableName,String whereClause, String[] whereArgs){
		SQLiteDatabase sdb=null;
		try{
			sdb=this.dbHelper.getWritableDatabase();
			sdb.delete(tableName, whereClause, whereArgs);
		}finally{
			if(sdb!=null){
				sdb.close();
			}
		}
	}
	
	public void clearAll(){
		delete(Department.TABLE_NAME, Department.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(DepartmentHasRoom.TABLE_NAME, DepartmentHasRoom.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(Doctor.TABLE_NAME, Doctor.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(Edge.TABLE_NAME, MapData.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(MapData.TABLE_NAME, MapData.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(Room.TABLE_NAME, Room.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(RoomArea.TABLE_NAME, RoomArea.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
		delete(Vertex.TABLE_NAME, Vertex.COLUMN_NAME_NO+"=?", new String[]{getCurrentDataNo()});
	}
	
	public String getCurrentDataNo(){
		return mAppContext.getCurrentDataNo();
	}
	
}
