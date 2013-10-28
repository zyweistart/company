package com.start.model.overlay;

import org.mapsforge.core.model.GeoPoint;

public interface POI {

	GeoPoint getGeoPoint();
	String getName();
	String getVertex();
	int getFloor();
	String getBuilding();
	boolean inside(GeoPoint p);
}
