package com.start.model.process;

public class Action {

	public enum ActionType{
		LOCATION,//定位
		OPENWEB,//打开浏览器
		DIALOG//弹出对话框
	}
	
	private ActionType type; 
	
	/**
	 * 提示信息
	 */
	private String message;
	
	//打开浏览器必填
	private String url;
	
	//定位必填
	private String latitude;
	
	private String longitude;

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}