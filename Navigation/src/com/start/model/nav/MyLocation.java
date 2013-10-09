package com.start.model.nav;

import com.start.model.POI;

public class MyLocation implements POI {

	private static final long serialVersionUID = 5370262122361994376L;

	private String building;
	private int floor;
	private com.baidu.platform.comapi.basestruct.GeoPoint outdoorPoint;
	private org.mapsforge.core.model.GeoPoint indoorPoint;

	public MyLocation(String building, int floor, 
			org.mapsforge.core.model.GeoPoint indoorPoint,
			com.baidu.platform.comapi.basestruct.GeoPoint outdoorPoint) {
		this.building = building;
		this.floor = floor;
		this.indoorPoint = indoorPoint;
		this.outdoorPoint = outdoorPoint;
	}

	public MyLocation(com.baidu.platform.comapi.basestruct.GeoPoint outdoorPoint) {
		this.outdoorPoint = outdoorPoint;
	}

	public com.baidu.platform.comapi.basestruct.GeoPoint getOutdoorPoint() {
		return outdoorPoint;
	}

	public boolean isIndoor() {
		return building != null;
	}

	public String getBuilding() {
		return building;
	}

	public int getFloor() {
		return floor;
	}

	public boolean locateIn(String building, int currentFloor) {
		return isIndoor() && building.equals(building) && currentFloor == floor;
	}

	@Override
	public org.mapsforge.core.model.GeoPoint getGeoPoint() {
		return indoorPoint;
	}

	@Override
	public String getName() {
		return "my location";
	}

	@Override
	public String getVertex() {
		return null;
	}

	@Override
	public boolean inside(org.mapsforge.core.model.GeoPoint p) {
		return false;
	}
}
