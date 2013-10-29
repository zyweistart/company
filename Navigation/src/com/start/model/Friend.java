package com.start.model;

import com.start.core.CoreModel;

public class Friend extends CoreModel {

	public static final String TABLE_NAME="ST_FRIEND";
	
	public static final String COLUMN_NAME_MYID="myId";
	public static final String COLUMN_NAME_FRIENDID="friendId";
	
	public static final String CREATE_TABLE_SQL = 
			"CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " TEXT,"
			+ COLUMN_NAME_MYID + " TEXT,"
			+ COLUMN_NAME_FRIENDID + " TEXT"
			+ ");";

	private String myId;
	
	private String friendId;

	public String getMyId() {
		return myId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

}
