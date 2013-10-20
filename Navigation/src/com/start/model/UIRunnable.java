package com.start.model;

import java.util.Map;

public abstract class UIRunnable{
	
	private Map<String, String> infoContent;
	
	private Map<String,Map<String,String>> allInfoContent;

	public Map<String, String> getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(Map<String, String> infoContent) {
		this.infoContent = infoContent;
	}

	public Map<String, Map<String, String>> getAllInfoContent() {
		return allInfoContent;
	}

	public void setAllInfoContent(Map<String, Map<String, String>> allInfoContent) {
		this.allInfoContent = allInfoContent;
	}

	public abstract void run();
	
}