package com.start.model.navigation;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;
import com.start.model.overlay.POI;

public class MyLocation implements POI {

	/**
	 * 当前所在的地图数据
	 */
	private MapData currentMapData;
	/**
	 * 室内的位置点
	 */
	private GeoPoint indoorPoint;

	public MyLocation(MapData currentMapData, GeoPoint indoorPoint) {
		this.currentMapData = currentMapData;
		this.indoorPoint = indoorPoint;
	}

	public boolean locateIn(MapData targetMapData) {
		return targetMapData.getId().equals(targetMapData.getId());
	}
	
	@Override
	public GeoPoint getGeoPoint() {
		return indoorPoint;
	}

	@Override
	public MapData getMapData() {
		return currentMapData;
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
