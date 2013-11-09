package com.ancun.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ancun.core.CoreServiceModel;
import com.ancun.model.MessageModel;

public class MessageService extends CoreServiceModel {
	
	public MessageService(Context context) {
		super(context);
	}
	
	public MessageModel findByCode(String code){
		MessageModel message=null;
		Cursor cursor = getSQLiteDatabase().query(MessageModel.TABLENAME, new String[]{"id","code", "title", "content","showtime","readflag"},"code=?",new String[]{code}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				message = new MessageModel();
				message.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("id"))));
				message.setCode(cursor.getString(cursor.getColumnIndex("code")));
				message.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				message.setContent(cursor.getString(cursor.getColumnIndex("content")));
				message.setShowTime(cursor.getString(cursor.getColumnIndex("showtime")));
				message.setReadFlag(Integer.parseInt(cursor.getString(cursor.getColumnIndex("readflag"))));
			}
		}finally{
			cursor.close();
		}
		return message;
	}
	
	public void save(MessageModel mm){
		ContentValues values = new ContentValues();
		values.put("code", mm.getCode());
		values.put("title", mm.getTitle());
		values.put("content", mm.getContent());
		values.put("showtime", mm.getShowTime());
		values.put("readflag", mm.getReadFlag());
		getSQLiteDatabase().insert(MessageModel.TABLENAME, null, values);
	}
	/**
	 * 更新为已读标记
	 */
	public void updateReadFlag(String code){
		ContentValues values = new ContentValues();
		values.put("readflag", "2");
		getSQLiteDatabase().update(MessageModel.TABLENAME, values, "code=?", new String[]{code});
	}
	/**
	 * 获取当前时间段要显示的消息列表
	 */
	public List<MessageModel> getThisTimeShowMessageList(){
		List<MessageModel> messages = new ArrayList<MessageModel>();
		Cursor cursor = getSQLiteDatabase().query(MessageModel.TABLENAME, new String[]{"id","code", "title", "content","showtime","readflag"},"showtime<=? and readflag=1",new String[]{"2013-03-07"}, null, null, "showtime desc");
		try{
			if(cursor.moveToFirst()){
				do {
					MessageModel message = new MessageModel();
					message.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("id"))));
					message.setCode(cursor.getString(cursor.getColumnIndex("code")));
					message.setTitle(cursor.getString(cursor.getColumnIndex("title")));
					message.setContent(cursor.getString(cursor.getColumnIndex("content")));
					message.setShowTime(cursor.getString(cursor.getColumnIndex("showtime")));
					message.setReadFlag(Integer.parseInt(cursor.getString(cursor.getColumnIndex("readflag"))));
					messages.add(message);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return messages;
	}
	
}