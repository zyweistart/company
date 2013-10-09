package com.start.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASEVERSION = 4;
	
	public SQLiteDBHelper(Context context) {
		super(context, "", null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	//	//1把数据表重名为临时表
	//	db.execSQL("ALTER TABLE "+ContactModel.TABLENAME+" RENAME TO __temp__"+ContactModel.TABLENAME);
	//	db.execSQL("ALTER TABLE "+RecentModel.TABLENAME+" RENAME TO __temp__"+RecentModel.TABLENAME);
	//	//2创建当前版本对应的数据表
	//	onCreate(db);
	//	//3把临时表中的数据迁移到新创建的表中
	//	db.execSQL("INSERT INTO "+ContactModel.TABLENAME+"() SELECT contact_id,key,flag FROM __temp__"+ContactModel.TABLENAME);
	//	db.execSQL("INSERT INTO "+RecentModel.TABLENAME+"() SELECT recent_id,oppo,phone,status,calltime FROM __temp__"+RecentModel.TABLENAME);
	//	//4删除临时表完成数据迁移
	//	db.execSQL("DROP TABLE IF EXISTS __temp__"+ContactModel.TABLENAME);
	//	db.execSQL("DROP TABLE IF EXISTS __temp__"+RecentModel.TABLENAME);
	//	//注:如果只是添加字段列则可以使用
	//	//ALTER TABLE tableName ADD COLUMN fieldName BLOB;该格式进行添加
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}