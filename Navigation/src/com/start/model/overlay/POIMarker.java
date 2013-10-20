package com.start.model.overlay;

import org.mapsforge.android.maps.overlay.Marker;

import android.graphics.drawable.Drawable;

import com.start.model.POI;

public class POIMarker extends Marker {

	protected POI poi;

	public POIMarker(POI obj, Drawable drawable) {
		super(obj.getGeoPoint(), drawable);
		this.poi = obj;
	}

	public int getFloor() {
		return poi.getFloor();
	}
	
	public POI getPOI() {
		return poi;
	}
}
