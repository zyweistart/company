package com.start.model.overlay;

import org.mapsforge.android.maps.overlay.Marker;

import android.graphics.drawable.Drawable;

public class POIMarker extends Marker {

	protected POI poi;

	public POIMarker(POI obj, Drawable drawable) {
		super(obj.getGeoPoint(), drawable);
		this.poi = obj;
	}
	
	public POI getPOI() {
		return poi;
	}
	
}
