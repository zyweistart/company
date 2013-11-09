package com.ancun.model;

import java.util.Map;

public abstract class UIRunnable{
	
	private String responseContent;
	
	private Map<String, String> infoContent;
	
	private Map<String,Map<String,String>> allInfoContent;

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	
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