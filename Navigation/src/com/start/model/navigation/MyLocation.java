package com.start.model.navigation;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;

public class MyLocation {

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
	
	/**
	 * 判断是否在室内
	 * @return
	 */
	public boolean isIndoor(){
		return true;
	}
	
	
	public GeoPoint getGeoPoint() {
		return indoorPoint;
	}

	public MapData getMapData() {
		return currentMapData;
	}
	

}
