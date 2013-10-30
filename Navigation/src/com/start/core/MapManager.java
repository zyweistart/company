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

import com.start.model.overlay.POI;
import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;

public abstract class MapManager  extends MapActivity implements OnTouchListener,OnTapMapClickListener{

	private MapView mMapView;
	
	private GestureDetector mGestureDetector;

	/**
	 * 初始化地图
	 */
	public boolean initMap(String fullMapPath){
		mGestureDetector = new GestureDetector(this,new OnTapMapListener(this));
		
		mMapView = new MapView(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		mMapView.setOnTouchListener(this);
		FileOpenResult openResult = mMapView.setMapFile(new File(fullMapPath));
		return openResult.isSuccess();
	}

	/**
	 * 触摸点击地图时触发
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
	
	/**
	 * 触摸点击地图时触发
	 */
	@Override
	public void onClickAt(float x, float y) {
		Projection projection = mMapView.getProjection();
		if (projection == null) {
			return;
		}
		//获取当前触摸的点坐标
//		GeoPoint g = projection.fromPixels((int) x, (int) y);
		//在此判断是否触摸的区域是在某个房间范围内
	}

	/**
	 * 搜索路径
	 * @param poi 当前选定的区域标记
	 */
	public void searchPath(POI poi){
		
	}
	
}