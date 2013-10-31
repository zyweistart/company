package com.start.model.overlay;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;

public interface POI {

	GeoPoint getGeoPoint();
	
	MapData getMapData();
	
	String getVertex();
	
	boolean inside(GeoPoint p);
	
}
