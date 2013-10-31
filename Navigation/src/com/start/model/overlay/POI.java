package com.start.model.overlay;

import java.io.Serializable;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;

public interface POI extends Serializable {

	GeoPoint getGeoPoint();
	
	MapData getMapData();
	
	String getVertex();
	
	String getName();
	
	boolean inside(GeoPoint p);
	
}
