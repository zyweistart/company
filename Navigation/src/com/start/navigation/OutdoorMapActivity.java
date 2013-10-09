package com.start.navigation;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.start.core.CoreActivity;
import com.start.model.Building;
import com.start.model.nav.PathSearchResult;
import com.start.model.nav.PathSearchResult.Type;
import com.start.model.overlay.BuildingOverlay;
import com.start.model.overlay.OutdoorRouteOverlay;
import com.start.utils.Utils;

/**
 * 室外
 * @author start
 *
 */
@SuppressWarnings("deprecation")
public class OutdoorMapActivity extends CoreActivity implements View.OnClickListener {

	private static final int DEFAULT_ZOOM_LEVEL = 17;
	private static final GeoPoint DEFAULT_CENTER = new GeoPoint(30315941, 120396365);
	private boolean isSearchBook;
	
	private MapView mMapView;
	private MapController mMapController;
	
	private LocationData mLocationData;
	private LocationClient mLocationClient;
	
	private BuildingOverlay mBuildingOverlay;
	private MyLocationOverlay myLocationOverlay;
	private OutdoorRouteOverlay mRouteOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppContext.getInstance().instanceBMapManager();
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_outdoor);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);

		mMapView = (MapView)findViewById(R.id.bmapView);
		//获取地图控制器
        mMapController = mMapView.getController();
        //在给定的中心点GeoPoint上设置地图视图。
        mMapController.setCenter(DEFAULT_CENTER);
        //设置地图的缩放级别。 这个值的取值范围是[3,19]。
        mMapController.setZoom(DEFAULT_ZOOM_LEVEL);
        //设置地图是否响应点击事件
        mMapController.enableClick(true);
        
        //设置是否打开卫星图
//        mMapView.setSatellite(true);
        // 设置mapview是否支持双击放大效果
		mMapView.setDoubleClickZooming(true);
		//设置是否启用内置的缩放控件。 如果启用，MapView将自动显示这些缩放控件。 
		//参数： true - 内置的缩放控件是否启用。如果是false，用户处理缩放控件在界面上的显示。
		mMapView.setBuiltInZoomControls(true);
		mMapView.setOnTouchListener(null);
        
        //MKMapViewListener 用于处理地图事件回调
		mMapView.regMapViewListener(AppContext.getInstance().instanceBMapManager(), new MKMapViewListener() {
        	
			@Override
			public void onMapMoveFinish() {
				//在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件
				 * 显示底图poi名称并移动至该点
				 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
				 */
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					makeTextLong(title);
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}
			
			@Override
			public void onGetCurrentMap(Bitmap b) {
				//当调用过 mMapView.getCurrentMap()后，此回调会被触发可在此保存截图至存储设备
			}

			@Override
			public void onMapAnimationFinish() {
				//地图完成带动画的操作（如: animationTo()）后，此回调被触发
			}
            
			@Override
			public void onMapLoadFinish() {
				//在此处理地图载完成事件 
			}
			
		});
    
		mLocationClient = new LocationClient(this);
		LocationClientOption options = new LocationClientOption();
		//打开gps
		options.setOpenGps(true);
		//设置坐标类型
		options.setCoorType("bd09ll"); 
		options.setScanSpan(1000);
		mLocationClient.setLocOption(options);
		
		myLocationOverlay = new MyLocationOverlay(mMapView);
		mLocationData = new LocationData();
		myLocationOverlay.setData(mLocationData);

		Drawable marker = getResources().getDrawable(R.drawable.ic_building);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		mBuildingOverlay = new BuildingOverlay(marker,mMapView, this);

		List<Overlay> overlays = new ArrayList<Overlay>(2);
		overlays.add(myLocationOverlay);
		overlays.add(mBuildingOverlay);
		mMapView.getOverlays().addAll(overlays);
		
//		new LoadBuildingTask().execute();
    }
    
    //如果Activity实例是第一次启动，则不调用，否则，以后的每次重新启动都会调用
    @Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		ViewGroup actionBar = (ViewGroup) findViewById(R.id.actionbar);
		actionBar.findViewById(R.id.actionbar_search).setVisibility(View.VISIBLE);
		actionBar.findViewById(R.id.actionbar_locate).setVisibility(View.VISIBLE);
	}
    
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        
        mLocationClient.registerLocationListener(bdLocationListener);
		mLocationClient.start();
		mLocationClient.requestLocation();
		
		PathSearchResult result = AppContext.getInstance().getPathSearchResult();
		if (result != null) {
			//显示百度地图上的路径
			if(result.getType()==Type.OUTDOOR_INDOOR||
					result.getType()==Type.BETWEEN_BUILDING){
				MKRoute route = result.outdoorRoute;
				if (mRouteOverlay == null) {
					mRouteOverlay = new OutdoorRouteOverlay(this, mMapView);
				}
				mRouteOverlay.setData(route);
				if (!mMapView.getOverlays().contains(mRouteOverlay)) {
					mMapView.getOverlays().add(mRouteOverlay);
				}
			}
		}
		
		mMapView.refresh();
    }
    
    @Override
    protected void onPause() {
    		super.onPause();
    		mLocationClient.unRegisterLocationListener(bdLocationListener);
		mLocationClient.stop();
        mMapView.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null){
        		mLocationClient.stop();
        }
        mMapView.destroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	mMapView.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	mMapView.onRestoreInstanceState(savedInstanceState);
    }

	private BDLocationListener bdLocationListener=new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			mLocationData.latitude = location.getLatitude();
			mLocationData.longitude = location.getLongitude();
			mLocationData.direction = location.getDerect();
			mLocationData.accuracy = location.getRadius();
			
			myLocationOverlay.setData(mLocationData);
		}
	
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.actionbar_search:
			showDialog(DLG_SEARCH_OPTION);
			break;
		case R.id.actionbar_locate:
			mMapView.refresh();
			mMapController.animateTo(new GeoPoint((int)(mLocationData.latitude* 1e6), (int)(mLocationData.longitude *  1e6)));
			break;
		case R.id.room:
			isSearchBook=false;
			onSearchRequested();
			break;
		case R.id.book:
			isSearchBook=true;
			onSearchRequested();
			break;
		}
	}

	@Override
	public boolean onSearchRequested() {
		Bundle searchTarget = new Bundle();
		searchTarget.putBoolean(SearchableActivity.SEARCH_BOOK, isSearchBook);
		startSearch(null, false, searchTarget, false);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DLG_SEARCH_OPTION:
				return createSearchOptionDialog();
			case DLG_EXIT_NAVIGATION:
				return Utils.buildDialog(this, R.string.exit_navigation, new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppContext.getInstance().setPathSearchResult(null);
						mMapView.getOverlays().remove(mRouteOverlay);
						mMapView.refresh();
					}
				});
			default:
				return super.onCreateDialog(id);
		}
	}

	@Override
	public void onBackPressed() {
		if(AppContext.getInstance().getPathSearchResult()!=null){
			showDialog(DLG_EXIT_NAVIGATION);
		} else {
			super.onBackPressed();
		}
	}
	
	/**
	 * 异步加载地图上标注的建筑点
	 */
	private class LoadBuildingTask extends AsyncTask<Void, Void, List<Building>> {

		@Override
		protected List<Building> doInBackground(Void... params) {
			ArrayList<Building> list = new ArrayList<Building>();
			Cursor cr = getContentResolver().query(Building.CONTENT_URI, null, null, null, null);
			if (cr.moveToNext()) {
				do {
					Building b = Building.newInstance(cr);
					list.add(b);
				} while (cr.moveToNext());
			}
			cr.close();

			return list;
		}

		@Override
		protected void onPostExecute(List<Building> result) {
			super.onPostExecute(result);
			mBuildingOverlay.setData(result);
			mMapView.refresh();
		}
		
	}
	
}