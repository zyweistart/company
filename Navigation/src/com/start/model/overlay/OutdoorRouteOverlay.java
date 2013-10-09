package com.start.model.overlay;

import android.app.Activity;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;

/**
 * 路线覆盖图
 * @author start
 *
 */
public class OutdoorRouteOverlay extends RouteOverlay {

	private MapView mapView;

	public OutdoorRouteOverlay(Activity activity, MapView mapView) {
		super(activity, mapView);
		this.mapView= mapView;
	}

	@Override
	protected boolean onTap(int i) {
		mapView.getController().animateTo(getItem(i).getPoint());
		return true;
	}
	
}