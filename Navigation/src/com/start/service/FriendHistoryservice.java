package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.FriendHistory;

public class FriendHistoryservice extends CoreService {

	public FriendHistoryservice(Context context) {
		super(context);
	}

	public List<FriendHistory> findAllByMyId(String myId){
		List<FriendHistory> fhs = new ArrayList<FriendHistory>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(FriendHistory.TABLE_NAME, 
				new String[]{
				FriendHistory.COLUMN_NAME_ID,
				FriendHistory.COLUMN_NAME_MYID,
				FriendHistory.COLUMN_NAME_FRIENDID,
				FriendHistory.COLUMN_NAME_CTEATETIME},
					FriendHistory.COLUMN_NAME_MYID+"=?",new String[]{myId}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					FriendHistory fh = new FriendHistory();
					fh.setId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_ID)));
					fh.setMyId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_MYID)));
					fh.setFriendId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_FRIENDID)));
					fh.setCreateTime(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_CTEATETIME)));
					fhs.add(fh);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return fhs;
	}
	
	public FriendHistory findByMyIdAndFriendId(String myId,String friendId){
		Cursor cursor = getDbHelper().getReadableDatabase().query(FriendHistory.TABLE_NAME, 
				new String[]{
				FriendHistory.COLUMN_NAME_ID,
				FriendHistory.COLUMN_NAME_MYID,
				FriendHistory.COLUMN_NAME_FRIENDID,
				FriendHistory.COLUMN_NAME_CTEATETIME},
					FriendHistory.COLUMN_NAME_MYID+"=? AND "+
							FriendHistory.COLUMN_NAME_FRIENDID+"=?,",new String[]{myId,friendId}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				FriendHistory fh = new FriendHistory();
				fh.setId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_ID)));
				fh.setMyId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_MYID)));
				fh.setFriendId(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_FRIENDID)));
				fh.setCreateTime(cursor.getString(cursor.getColumnIndex(FriendHistory.COLUMN_NAME_CTEATETIME)));
				return fh;
			}
		}finally{
			cursor.close();
		}
		return null;
	}
	
}