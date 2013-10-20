package com.start.model.nav;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OutdoorEndPoint implements EndPoint {

	private GeoPoint geoPoint;

	public OutdoorEndPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	
}
