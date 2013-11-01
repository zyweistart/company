package com.start.model.nav;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;



public class IndoorEndPoint implements EndPoint {

	private MapData mapData;
	private String vertex;
	private GeoPoint geoPoint;
	
	public IndoorEndPoint(MapData mapData, GeoPoint p) {
		this.mapData=mapData;
		this.geoPoint = p;
	}
	
	public IndoorEndPoint(MapData mapData, GeoPoint p, String vertex) {
		this.mapData=mapData;
		this.geoPoint = p;
		this.vertex = vertex;
	}

	public MapData getMapData() {
		return mapData;
	}

	public void setMapData(MapData mapData) {
		this.mapData = mapData;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

}
