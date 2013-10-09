package com.start.model.nav;

import java.util.ArrayList;

import com.start.model.Building;

public class NavRoute extends ArrayList<NavStep> {

	private static final long serialVersionUID = 7542240231442289872L;
	private Building building;

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public NavStep getStep(int floor) {
		for (NavStep step : this) {
			if (step.getFloor() == floor) {
				return step;
			}
		}
		return null;
	}
}
