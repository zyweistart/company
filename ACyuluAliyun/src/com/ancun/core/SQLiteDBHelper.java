package com.ancun.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ancun.model.ContactModel;
import com.ancun.model.MessageModel;
import com.ancun.model.RecentModel;

public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASEVERSION = 4;
	
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
		//消息
		sql=new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(MessageModel.TABLENAME);
		sql.append("(");
		sql.append("id integer primary key autoincrement,");
		sql.append("content varchar,");
		sql.append("showTime varchar,");
		sql.append("readFlag varchar");
		sql.append(")");
		db.execSQL(sql.toString());
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
		if(oldVersion==1){
			if(newVersion==4){
				updateTable(db);
			}
		}else if(oldVersion==2){
			if(newVersion==4){
				updateTable(db);
			}
		}else if(oldVersion==3){
			if(newVersion==4){
				updateTable(db);
			}
		}
	}

	@Deprecated
	void newVersion2(SQLiteDatabase db){

	}
	
	@Deprecated
	void newVersion3(SQLiteDatabase db){
		StringBuilder sql=new StringBuilder();
		sql.append("ALTER TABLE "+ContactModel.TABLENAME+"  ADD COLUMN usersetflag integer");
		db.execSQL(sql.toString());
		//把以前版本中的手动选择模式全部发为不自动提示模式
		ContentValues values = new ContentValues();
		values.put("flag", "2");
		values.put("usersetflag", "1");
		db.update(ContactModel.TABLENAME, values, "flag=?", new String[]{"1"});
	}
	
	void updateTable(SQLiteDatabase db){
		StringBuilder sql=new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(MessageModel.TABLENAME);
		sql.append("(");
		sql.append("id integer primary key autoincrement,");
		sql.append("content varchar,");
		sql.append("showTime varchar,");
		sql.append("readFlag varchar");
		sql.append(")");
		db.execSQL(sql.toString());
	}
	
}