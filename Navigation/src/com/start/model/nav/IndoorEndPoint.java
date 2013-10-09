package com.start.model.nav;

import org.mapsforge.core.model.GeoPoint;



public class IndoorEndPoint implements EndPoint {

	private String buildingName;
	private String vertex;
	private GeoPoint geoPoint;
	private int floor;
	
	public IndoorEndPoint(String b, int floor, GeoPoint p) {
		this.buildingName = b;
		this.floor = floor;
		this.geoPoint = p;
	}
	
	public IndoorEndPoint(String b, int floor, GeoPoint p, String vertex) {
		this.buildingName = b;
		this.floor = floor;
		this.geoPoint = p;
		this.vertex = vertex;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public String getVertex() {
		return vertex;
	}
	
	public int getFloor() {
		return floor;
	}
	
}
