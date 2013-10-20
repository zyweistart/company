package com.start.model;

import java.io.Serializable;

public class Book implements Searchable, Serializable {

	private static final long serialVersionUID = 8593388029620940597L;

	private String name;
	private String index;
	private String url;

	public String getHeader() {
		return name;
	}

	public String getSubHeader() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	 
}
