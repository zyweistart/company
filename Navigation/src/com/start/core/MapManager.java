package com.start.core;

import java.io.File;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.NavRoute;
import com.start.model.nav.NavStep;
import com.start.model.nav.PathSearchResult;
import com.start.model.overlay.MyLocationMarker;
import com.start.navigation.R;
import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;

public abstract class MapManager  extends MapActivity implements OnTouchListener,OnTapMapClickListener{

	private Paint mPaintStroke;
	private GestureDetector mGestureDetector;
	
	private MapView mMapView;
	private ArrayItemizedOverlay mMyLocOverlay;
	private MyLocationMarker mMyLocMarker;

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
	
	public Paint getPaintStroke(){
		if(mPaintStroke==null){
			mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintStroke.setStyle(Paint.Style.STROKE);
			mPaintStroke.setColor(Color.BLUE);
			mPaintStroke.setAlpha(96);
			mPaintStroke.setStrokeWidth(5);
		}
		return mPaintStroke;
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
	
	/**
	 * 获取地图上的覆盖图
	 */
	public ArrayItemizedOverlay getArrayItemizedOverlay(PathSearchResult res,String currentMapId) {
		if (res == null) {
			return null;
		}

		NavRoute route = res.getRoute();
		if (route == null) {
			return null;
		}

		NavStep step = route.getStep(currentMapId);
		if (step == null) {
			return null;
		}

		getMapView().setCenter(step.getStart().getGeoPoint());
		
		ArrayItemizedOverlay arrayItems = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.icon_node));
		arrayItems.addItem(new OverlayItem(step.getStart().getGeoPoint(), null, null, ArrayItemizedOverlay.boundCenter(getResources().getDrawable(R.drawable.icon_node))));

		if (step.getEnd() != null) {
			arrayItems.addItem(new OverlayItem(step.getEnd().getGeoPoint(), null, null, ArrayItemizedOverlay.boundCenter(getResources().getDrawable(R.drawable.icon_node))));
		}

		if (res.getStartPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint start = (IndoorEndPoint) res.getStartPoint();
			// 如果终点位置在当前地图上则添加起点覆盖图
			if (start.getMapId().equals(currentMapId)) {
				arrayItems.addItem(new OverlayItem(start.getGeoPoint(), null, null, ArrayItemizedOverlay.boundCenter(getResources().getDrawable(R.drawable.icon_nav_start))));
			}
		}

		if (res.getEndPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint end = (IndoorEndPoint) res.getEndPoint();
			// 如果终点在当前地图则添加终点覆盖图
			if (end.getMapId().equals(currentMapId)) {
				arrayItems.addItem(new OverlayItem(end.getGeoPoint(), null, null, ArrayItemizedOverlay.boundCenter(getResources().getDrawable(R.drawable.icon_nav_end))));
			}
		}
		return arrayItems;
	}
	
	/**
	 * 获取地图上覆盖的路线
	 */
	public ArrayWayOverlay getWayOverlay(PathSearchResult result,String currentMapId) {

		if (result == null) {
			return null;
		}

		NavRoute route = result.getRoute();
		if (route == null) {
			return null;
		}

		NavStep step = route.getStep(currentMapId);
		if (step == null) {
			return null;
		}
		
		OverlayWay way = new OverlayWay();
		int size=step.size();
		GeoPoint[][] nodes;
//		GeoPoint[][] nodes = new GeoPoint[1][size];
//		for (int i = 0; i < size; i++) {
//			nodes[0][i] = step.get(i);
//		}
		
		if (result.getStartPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint start = (IndoorEndPoint) result.getStartPoint();
			if (start.getMapId().equals(currentMapId)) {
				size=size+1;
				nodes = new GeoPoint[1][size];
				nodes[0][0]=start.getGeoPoint();
				for (int i = 1; i < size; i++) {
					nodes[0][i] = step.get(i-1);
				}
			}else{
				nodes = new GeoPoint[1][size];
				for (int i = 0; i < size; i++) {
					nodes[0][i] = step.get(i);
				}
			}
		}else{
			nodes = new GeoPoint[1][size];
			for (int i = 0; i < size; i++) {
				nodes[0][i] = step.get(i);
			}
		}
//
//		if (result.getEndPoint() instanceof IndoorEndPoint) {
//			IndoorEndPoint end = (IndoorEndPoint) result.getEndPoint();
//			if (end.getMapId().equals(mCurrentMapData.getId())) {
//				nodes[0][++size]=end.getGeoPoint();
//			}
//		}
		
		way.setWayNodes(nodes);
		ArrayWayOverlay ways = new ArrayWayOverlay(null, getPaintStroke());
		ways.addWay(way);
		return ways;
	}
	
	/**
	 * 添加用户位置标记
	 */
	public void addMyLocMarker(List<Overlay> itemList,MyLocation myLocation,String currentMapId) {
		// 如果当前定位的位置与当前的地图相同则添加位置标记
		if (myLocation.getMapId().equals(currentMapId)) {
			
			if(mMyLocOverlay==null){
				Drawable d=getResources().getDrawable(R.drawable.ic_my_loc);
				mMyLocOverlay = new ArrayItemizedOverlay(d);
				mMyLocMarker=new MyLocationMarker(myLocation, d);
				mMyLocOverlay.addItem(mMyLocMarker);
				if(itemList!=null){
					itemList.add(mMyLocOverlay);
				}else{
					getMapView().getOverlays().add(mMyLocOverlay);
				}
			}else{
				mMyLocOverlay.clear();
				mMyLocMarker.setPoint(myLocation.getGeoPoint());
				mMyLocOverlay.addItem(mMyLocMarker);
			}
			
		}else{
			if(mMyLocOverlay!=null){
				mMyLocOverlay.clear();
			}
		}
	}
	
}