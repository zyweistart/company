package com.start.model;

import java.io.Serializable;

import org.mapsforge.core.model.GeoPoint;

public interface POI extends Serializable {

	GeoPoint getGeoPoint();
	String getName();
	String getVertex();
	int getFloor();
	String getBuilding();
	boolean inside(GeoPoint p);
}
