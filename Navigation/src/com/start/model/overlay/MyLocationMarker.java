package com.start.model.overlay;

import android.graphics.drawable.Drawable;

import com.start.model.navigation.MyLocation;

public class MyLocationMarker extends POIMarker {

	public MyLocationMarker(MyLocation newLocation, Drawable drawable) {
		super(newLocation, drawable);
	}
	
	public void updateLocation(MyLocation newLocation) {
		this.poi = newLocation;
		setGeoPoint(newLocation.getGeoPoint());
	}
}
