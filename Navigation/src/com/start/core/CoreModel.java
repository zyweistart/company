package com.start.core;

import android.provider.BaseColumns;

public abstract class CoreModel implements BaseColumns {

	public static final String COLUMN_NAME_ID=_ID;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}