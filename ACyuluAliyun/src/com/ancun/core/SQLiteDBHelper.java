package com.ancun.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ancun.model.ContactModel;
import com.ancun.model.RecentModel;

public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASEVERSION = 1;
	
	public SQLiteDBHelper(Context context) {
		super(context, Constant.SQLiteDataBaseName, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//联系人
		StringBuilder sql=new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(ContactModel.TABLENAME);
		sql.append("(");
		sql.append("contact_id integer primary key autoincrement,");
		sql.append("key varchar,");
		sql.append("flag integer,");
		sql.append("usersetflag integer");
		sql.append(")");
		db.execSQL(sql.toString());
		//最近联系人
		sql=new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(RecentModel.TABLENAME);
		sql.append("(");
		sql.append("recent_id integer primary key autoincrement,");
		sql.append("oppo varchar,");
		sql.append("phone varchar,");
		sql.append("status varchar,");
		sql.append("calltime varchar");
		sql.append(")");
		db.execSQL(sql.toString());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}