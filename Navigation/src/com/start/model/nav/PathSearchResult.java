package com.start.model.nav;

import android.text.TextUtils;

import com.baidu.mapapi.search.MKRoute;
import com.start.model.Building;

public class PathSearchResult {

	public enum Type {NORMAL, IN_BUILDING, BETWEEN_BUILDING, OUTDOOR_INDOOR, OUTDOOR }
	public NavRoute indoorRouteStart;
	public MKRoute outdoorRoute;
	public NavRoute indoorRouteEnd;
	public volatile boolean outdoorSearchFinished = true;
	public Type type;
	public EndPoint start, end;

	public PathSearchResult(EndPoint start, EndPoint end) {
		this.start = start;
		this.end = end;
		
	}

	public Type getType() {
		return type == null ? Type.NORMAL : type;
	}
	
	public NavRoute getRouteByBuilding(Building b) {
		if (indoorRouteStart != null && TextUtils.equals(indoorRouteStart.getBuilding().getName(), b.getName())) {
			return indoorRouteStart;
		} else if (indoorRouteEnd != null && TextUtils.equals(indoorRouteEnd.getBuilding().getName(), b.getName())) {
			return indoorRouteEnd;
		} else {
			return null;
		}
	}
	
	public EndPoint getStartPoint() {
		return start;
	}
	
	public EndPoint getEndPoint() {
		return end;
	}
	
	public boolean isValid() {
		boolean valid = false;
		switch (type) {
		case IN_BUILDING:
			valid = indoorRouteEnd != null;
			break;
		case BETWEEN_BUILDING:
			valid = indoorRouteStart != null && indoorRouteEnd != null && outdoorRoute != null;
			break;
		case OUTDOOR_INDOOR:
			valid = outdoorRoute != null && indoorRouteEnd != null;
			break;
		case OUTDOOR:
			valid = outdoorRoute != null;
			break;
		default:
			valid = false;
		}
		
		return valid;
	}
}
