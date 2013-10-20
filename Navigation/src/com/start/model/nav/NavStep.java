package com.start.model.nav;

import java.util.ArrayList;

import org.mapsforge.core.model.GeoPoint;

import com.start.model.Vertex;

public class NavStep extends ArrayList<GeoPoint> {

	private static final long serialVersionUID = 1381941828147137113L;

	private int floor;
	private Vertex start, end;
	
	public NavStep(int floor) {
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}

	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex end) {
		this.end = end;
	}
	
}
