package com.start.core;

import java.io.File;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;

public abstract class MapManager  extends MapActivity implements OnTouchListener,OnTapMapClickListener{

	private MapView mMapView;
	
	private GestureDetector mGestureDetector;

	public Boolean setMapFile(File mapFile) {
		FileOpenResult openResult = getMapView().setMapFile(mapFile);
		return openResult.isSuccess();
	}

	public MapView getMapView() {
		if(mMapView==null){
			mMapView = new MapView(this);
			mMapView.setBuiltInZoomControls(true);
			mMapView.setClickable(true);
			mMapView.setOnTouchListener(this);
			mMapView.getController().setZoom(20);
		}
		return mMapView;
	}

	public GestureDetector getGestureDetector() {
		if(mGestureDetector==null){
			mGestureDetector = new GestureDetector(this,new OnTapMapListener(this));
		}
		return mGestureDetector;
	}
	
	/**
	 * 触摸点击地图时触发
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		getGestureDetector().onTouchEvent(event);
		return false;
	}
	
	/**
	 * 触摸点击地图时触发
	 */
	@Override
	public void onClickAt(float x, float y) {
		Projection projection = getMapView().getProjection();
		if (projection == null) {
			return;
		}
	}
	
}