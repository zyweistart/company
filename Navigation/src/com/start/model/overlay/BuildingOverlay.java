package com.start.model.overlay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.start.model.Building;
import com.start.navigation.AppContext;
import com.start.navigation.MapIndoorActivity;
import com.start.navigation.R;

/**
 * 建筑覆盖图
 * @author start
 *
 */
public class BuildingOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Context mContext;

	public BuildingOverlay(Drawable marker,MapView mapView,Context context) {
		super(marker,mapView);
		this.mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		return mGeoList.size();
	}

	@Override
	protected boolean onTap(int i) {
		Intent intent = new Intent(mContext, MapIndoorActivity.class);
		intent.setData(Uri.withAppendedPath(Building.CONTENT_ID_URI_BASE, mGeoList.get(i).getSnippet()));
		mContext.startActivity(intent);
		return true;
	}
	
	public void setData(List<Building> buildings) {
		if (buildings == null) {
			return;
		}
		this.removeAll();
		Drawable marker = AppContext.getInstance().getResources().getDrawable(R.drawable.ic_building);
		for (Building b : buildings) {
			OverlayItem item = new OverlayItem(b.getGeoPoint(), b.getDesc(), String.valueOf(b.getID()));
			item.setMarker(marker);
			mGeoList.add(item);
		}
		this.addItem(mGeoList);
	}
}
