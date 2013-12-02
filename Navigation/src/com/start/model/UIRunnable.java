package com.start.model;

import java.util.Map;

public abstract class UIRunnable{
	
	private Map<String, String> info;
	
	private Map<String,Map<String,String>> content;

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public Map<String, Map<String, String>> getContent() {
		return content;
	}

	public void setContent(Map<String, Map<String, String>> content) {
		this.content = content;
	}

	public abstract void run();
	
}