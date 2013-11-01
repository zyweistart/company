package com.start.model.nav;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.MapData;
import com.start.model.overlay.POI;

public class MyLocation implements POI {

	private static final long serialVersionUID = 81813808830727223L;
	/**
	 * 当前所在的地图数据
	 */
	private MapData targetMapData;
	/**
	 * 室内的位置点
	 */
	private GeoPoint indoorPoint;

	public MyLocation(MapData targetMapData, GeoPoint indoorPoint) {
		this.targetMapData = targetMapData;
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
		return targetMapData;
	}

	@Override
	public String getVertexId() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean inside(GeoPoint p) {
		return false;
	}

}