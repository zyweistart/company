package com.start.model.nav;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.overlay.POI;

public class MyLocation implements POI {

	private static final long serialVersionUID = 81813808830727223L;
	/**
	 * 当前所在的地图数据
	 */
	private String mapId;
	/**
	 * 室内的位置点
	 */
	private GeoPoint indoorPoint;

	public MyLocation(String targetMapId, GeoPoint indoorPoint) {
		this.mapId = targetMapId;
		this.indoorPoint = indoorPoint;
	}

	public boolean locateIn(String targetMapId) {
		return mapId.equals(targetMapId);
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

	@Override
	public String getMapId() {
		return mapId;
	}

	@Override
	public String getId() {
		return null;
	}

}