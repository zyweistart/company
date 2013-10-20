package com.start.core;

import android.provider.BaseColumns;

public abstract class CoreModel implements BaseColumns {

	public static final String COLUMN_NAME_ID=_ID;
	public static final String COLUMN_NAME_CODE="code";
	
	private String id;

	private String code;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}