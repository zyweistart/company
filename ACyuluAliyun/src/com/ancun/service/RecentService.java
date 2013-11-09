package com.ancun.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.ancun.core.Constant;
import com.ancun.core.CoreServiceModel;
import com.ancun.model.RecentModel;
import com.ancun.utils.TimeUtils;

public class RecentService extends CoreServiceModel {

	public RecentService(Context context) {
		super(context);
	}

	/**
	 * 保存
	 */
	public void save(RecentModel recentModel) {
		ContentValues values = new ContentValues();
		values.put("phone", recentModel.getPhone());
		values.put("calltime", TimeUtils.getSysTime());
		values.put("status", recentModel.getStatus());
		getSQLiteDatabase().insert(RecentModel.TABLENAME, null, values);
	}

	/**
	 * 删除
	 */
	public void delete(Integer id) {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, "recent_id=?",
				new String[] { id.toString() });
	}

	/**
	 * 删除
	 */
	public void deleteByPhone(String phone) {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, "phone=?",
				new String[] { phone });
	}

	/**
	 * 清空
	 */
	public void deleteAll() {
		getSQLiteDatabase().delete(RecentModel.TABLENAME, null, null);
	}

	/**
	 * 根据主键ID查找对象
	 */
	public RecentModel find(Integer id) {
		RecentModel recentModel = null;
		Cursor cursor = getSQLiteDatabase()
				.query(RecentModel.TABLENAME,
						new String[] { "recent_id", "calltime", "phone",
								"status" }, "recent_id=?",
						new String[] { id.toString() }, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				recentModel = new RecentModel();
				recentModel.setRecent_id(cursor.getInt(cursor
						.getColumnIndex("recent_id")));
				recentModel.setCalltime(cursor.getString(cursor
						.getColumnIndex("calltime")));
				recentModel.setPhone(cursor.getString(cursor
						.getColumnIndex("phone")));
				recentModel.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));
			}
		} finally {
			cursor.close();
		}
		return recentModel;
	}

	/**
	 * 获取所有记录
	 */
	public List<RecentModel> findAll() {
		List<RecentModel> recentModels = new ArrayList<RecentModel>();
		Cursor cursor = getSQLiteDatabase().query(RecentModel.TABLENAME,
				new String[] { "recent_id", "calltime", "phone", "status" },
				null, null, "phone", null, "calltime desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					RecentModel recent = new RecentModel();
					recent.setRecent_id(cursor.getInt(cursor
							.getColumnIndex("recent_id")));
					recent.setCalltime(cursor.getString(cursor
							.getColumnIndex("calltime")));
					recent.setPhone(cursor.getString(cursor
							.getColumnIndex("phone")));
					recent.setStatus(cursor.getInt(cursor
							.getColumnIndex("status")));
					recentModels.add(recent);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return recentModels;
	}

	/**
	 * 获取所有记录
	 */
	public List<RecentModel> findAllByPhone(String phone) {
		List<RecentModel> recentModels = new ArrayList<RecentModel>();
		Cursor cursor = getSQLiteDatabase().query(RecentModel.TABLENAME,
				new String[] { "recent_id", "calltime", "phone", "status" },
				"phone=?", new String[] { phone }, null, null, "calltime desc");
		try {
			if (cursor.moveToFirst()) {
				do {
					RecentModel recent = new RecentModel();
					recent.setRecent_id(cursor.getInt(cursor
							.getColumnIndex("recent_id")));
					recent.setCalltime(cursor.getString(cursor
							.getColumnIndex("calltime")));
					recent.setPhone(cursor.getString(cursor
							.getColumnIndex("phone")));
					recent.setStatus(cursor.getInt(cursor
							.getColumnIndex("status")));
					recentModels.add(recent);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return recentModels;
	}

	/**
	 * 获取外部的全部通话记录 需要android.permission.READ_CALL_LOG权限
	 */
	public List<RecentModel> findCallRecords() {
		List<RecentModel> recents=new ArrayList<RecentModel>();
		ContentResolver cr = getContext().getContentResolver();
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StringBuilder filterName=new StringBuilder();
		for(int i=0;i<Constant.noCall.size();i++){
			filterName.append("?");
			if(Constant.noCall.size()-1>i){
				filterName.append(",");
			}
		}
		String[] filterCall=new String[Constant.noCall.size()];
		Constant.noCall.toArray(filterCall);
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, 
				new String[] {
				CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, 
				CallLog.Calls.TYPE, 
				CallLog.Calls.DATE }, 
				CallLog.Calls.NUMBER+" not in("+filterName.toString()+")", 
				filterCall,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		try {
			if (cursor.moveToFirst()) {
				do {
					RecentModel recentModel = new RecentModel();
					recentModel.setName(cursor.getString(0));
					recentModel.setPhone(cursor.getString(1));
					recentModel.setStatus(cursor.getInt(2));
					recentModel.setCalltime(sfd.format(new Date(Long.parseLong(cursor.getString(3)))));
					recents.add(recentModel);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return recents;
	}
}