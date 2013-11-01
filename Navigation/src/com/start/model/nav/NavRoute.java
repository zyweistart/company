package com.start.model.nav;

import java.util.ArrayList;

public class NavRoute extends ArrayList<NavStep> {

	private static final long serialVersionUID = 7542240231442289872L;
	
	private String mapId;
	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public NavStep getStep(String mapId) {
		for (NavStep step : this) {
			if (step.getMapId().equals(mapId) ) {
				return step;
			}
		}
		return null;
	}
	
}
