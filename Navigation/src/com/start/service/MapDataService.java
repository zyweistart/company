package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.MapData;

public class MapDataService extends CoreService {

	public MapDataService(Context context) {
		super(context);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	public List<MapData> findAll(){
		List<MapData> mapDatas = new ArrayList<MapData>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(MapData.TABLE_NAME, 
				new String[]{
					MapData.COLUMN_NAME_ID,
					MapData.COLUMN_NAME_NAME,
					MapData.COLUMN_NAME_VERTEXID},
					MapData.COLUMN_NAME_NO+" = ?",
					new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					MapData mapData = new MapData();
					mapData.setId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_ID)));
					mapData.setName(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_NAME)));
					mapData.setVertexId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_VERTEXID)));
					mapDatas.add(mapData);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return mapDatas;
	}
	
	public List<MapData> findAll(String currentDataNo){
		List<MapData> mapDatas = new ArrayList<MapData>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(MapData.TABLE_NAME, 
				new String[]{
					MapData.COLUMN_NAME_ID,
					MapData.COLUMN_NAME_NAME,
					MapData.COLUMN_NAME_VERTEXID},
					MapData.COLUMN_NAME_NO+" = ?",
					new String[]{currentDataNo}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					MapData mapData = new MapData();
					mapData.setId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_ID)));
					mapData.setName(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_NAME)));
					mapData.setVertexId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_VERTEXID)));
					mapDatas.add(mapData);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return mapDatas;
	}
	
	public MapData findById(String Id){
		MapData mapData =null;
		Cursor cursor = getDbHelper().getReadableDatabase().query(MapData.TABLE_NAME, 
				new String[]{
				MapData.COLUMN_NAME_ID,
				MapData.COLUMN_NAME_NAME,
				MapData.COLUMN_NAME_VERTEXID},
				MapData.COLUMN_NAME_ID+" = ? AND "+MapData.COLUMN_NAME_NO+" = ?",
				new String[]{Id,getCurrentDataNo()},null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					mapData = new MapData();
					mapData.setId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_ID)));
					mapData.setName(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_NAME)));
					mapData.setVertexId(cursor.getString(cursor.getColumnIndex(MapData.COLUMN_NAME_VERTEXID)));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return mapData;
	}
	
}