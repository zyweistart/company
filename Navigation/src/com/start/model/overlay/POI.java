package com.start.model.overlay;

import java.io.Serializable;

import org.mapsforge.core.model.GeoPoint;

public interface POI extends Serializable {

	GeoPoint getGeoPoint();
	
	String getName();
	
	String getMapId();
	
	String getVertexId();
	
	boolean inside(GeoPoint p);
	
}
