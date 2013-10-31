package com.start.model.nav;

import java.util.ArrayList;

import com.start.model.MapData;

public class NavRoute extends ArrayList<NavStep> {

	private static final long serialVersionUID = 7542240231442289872L;
	
	private MapData mapData;
	
	public MapData getMapData() {
		return mapData;
	}

	public void setMapData(MapData mapData) {
		this.mapData = mapData;
	}

	public NavStep getStep(MapData mapData) {
		for (NavStep step : this) {
			if (step.getMapId().equals(mapData.getId()) ) {
				return step;
			}
		}
		return null;
	}
}
