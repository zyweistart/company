package com.start.model.nav;

import java.util.ArrayList;

import com.start.model.MapData;

public class NavRoute extends ArrayList<NavStep> {

	private static final long serialVersionUID = 7542240231442289872L;
	
	private String mapId;
	

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
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
