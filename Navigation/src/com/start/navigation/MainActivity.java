package com.start.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.start.core.AppConfig;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.nav.EndPoint;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.NavRoute;
import com.start.model.nav.NavStep;
import com.start.model.nav.PathSearchResult;
import com.start.model.overlay.MyLocationMarker;
import com.start.model.overlay.POI;
import com.start.model.overlay.POIMarker;
import com.start.service.MapDataAdapter;
import com.start.service.PathSearchTask;
import com.start.service.PathSearchTask.PathSearchListener;
import com.start.utils.CommonFn;
import com.start.utils.Utils;
import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;

/**
 * 主界面
 * @author start
 *
 */
public class MainActivity extends MapActivity implements OnTouchListener,OnClickListener,OnTapMapClickListener,PathSearchListener {

	private static final String BUNDLEDATA_DATA="data";
	
	private AppContext appContext;
	
	private int mCurSel;
	private int mFrameViewCount;
	private RadioButton rboIntroduction;
	private RadioButton rboMap;
	private RadioButton rboProcess;
	private RadioButton rboFriend;
	private ImageView imMore;
	private RadioButton[] mButtons;
	
	private LinearLayout mMainContentLayout;
	private View mModuleMainFrameMapContent;
	private View mModuleMainFrameIntroductionContent;
	private View mModuleMainFrameProcessContent;
	private View mModuleMainFrameFriendContent;
	
	private Button mModuleMainFrameIntroduction_btnHospital;
	private Button mModuleMainFrameIntroduction_btnDepartment;
	private Button mModuleMainFrameIntroduction_btnDoctor;
	
	private GestureDetector mGestureDetector;
	private MapView mMapView;
	private ListView mMapIndexListView;
	private MapDataAdapter mMapDataAdapter;
	private ListOverlay mListOverlay;
	private MapData mCurrentMapData;
	private POIMarker mPOIMarker;
	private MyLocationMarker mMyLocMarker;
	private Map<String,List<Room>> mRooms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext=AppContext.getInstance();
		
		this.initHeaderView();
        this.initFooterView();
        this.initMainFrameView();
        
        new LoadingMapContentDataByRoom().execute();
        
	}
	
	@Override
	protected void onResume() {
	    	super.onResume();
	    	if(mCurSel==0&&!rboIntroduction.isChecked()) {
	    		rboIntroduction.setChecked(true);
	    		rboMap.setChecked(false);
	    		rboProcess.setChecked(false);
	    		rboFriend.setChecked(false);
	    		mCurSel=0;
	    		setCurPoint(mCurSel);
	    	}
    }
	
	private void setCurPoint(int index){
		if(mCurSel != index) {
			if(index>=0&&mFrameViewCount>index){
				//点击当前项刷新
				if(index==0){
					mModuleMainFrameIntroductionContent.setVisibility(View.VISIBLE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				}else if(index==1){
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.VISIBLE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				}else if(index==2){
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.VISIBLE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				}else if(index==3){
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.VISIBLE);
				}
				mButtons[mCurSel].setChecked(false);
		    		mButtons[index].setChecked(true);  
				mCurSel=index;
			}
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return false;
	}
	
	/**
	 * 初始化头部视图
	 */
	private void initHeaderView() {
		
	}
	
	/**
	 * 初始化底部视图
	 */
	private void initFooterView() {
		rboIntroduction=(RadioButton)findViewById(R.id.main_footbar_introduction);
		rboMap=(RadioButton)findViewById(R.id.main_footbar_map);
		rboProcess=(RadioButton)findViewById(R.id.main_footbar_process);
		rboFriend=(RadioButton)findViewById(R.id.main_footbar_friend);
		
		imMore = (ImageView)findViewById(R.id.main_footbar_more);
	    	imMore.setOnClickListener(new View.OnClickListener() {
	    		public void onClick(View v) {
	    			
	    			Intent intent=new Intent(MainActivity.this,MoreActivity.class);
	    			startActivity(intent);
	    			
	    		}
	    	});    	
		
	    	mMainContentLayout=(LinearLayout)findViewById(R.id.main_content_layout);
	    	mModuleMainFrameMapContent=(View)findViewById(R.id.module_main_frame_map_content);
		mModuleMainFrameIntroductionContent=(View)findViewById(R.id.module_main_frame_introduction_content);
		mModuleMainFrameProcessContent=(View)findViewById(R.id.module_main_frame_process_content);
		mModuleMainFrameFriendContent=(View)findViewById(R.id.module_main_frame_friend_content);
	    	
	    	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.module_main_footer_content);
	    	mFrameViewCount=mMainContentLayout.getChildCount();
	    	mButtons = new RadioButton[mFrameViewCount];
	    	
	    	for(int i = 0; i < mFrameViewCount; i++) 	{
	    		mButtons[i] = (RadioButton) linearLayout.getChildAt(i*2);
	    		mButtons[i].setTag(i);
	    		mButtons[i].setChecked(false);
	    		mButtons[i].setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						setCurPoint((Integer)(v.getTag()));
					}
				});
	    	}
	    	
	}

    /**
     * 初始化主体框架视图
     */
	private void initMainFrameView() {
		//introduction
		mModuleMainFrameIntroduction_btnHospital = (Button) findViewById(R.id.module_main_frame_introduction_btnHospital);
		mModuleMainFrameIntroduction_btnHospital.setOnClickListener(this);
		mModuleMainFrameIntroduction_btnDepartment = (Button) findViewById(R.id.module_main_frame_introduction_btnDepartment);
		mModuleMainFrameIntroduction_btnDepartment.setOnClickListener(this);
		mModuleMainFrameIntroduction_btnDoctor = (Button) findViewById(R.id.module_main_frame_introduction_btnDoctor);
		mModuleMainFrameIntroduction_btnDoctor.setOnClickListener(this);
		//map
		mGestureDetector = new GestureDetector(this,new OnTapMapListener(this));
		
		mMapView = new MapView(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		mMapView.setOnTouchListener(this);
		
		mListOverlay = new ListOverlay();
		mMapView.getOverlays().add(mListOverlay);
		
		//process
		
		//friend
		
	}
	
	/**
	 * 设置视图的地图数据文件
	 */
	private void setMapFile() {
		if(mCurrentMapData==null){
			return;
		}
		
		//设置当前地图索引选重状态
		mMapIndexListView.setItemChecked(mMapDataAdapter.getMapDataPositionByMapId(mCurrentMapData.getId()), true);
		
		String path = String.format("%1$s/%2$s.map", AppConfig.CONFIG_DATA_PATH_MEDMAP,mCurrentMapData.getId());
		FileOpenResult openResult = mMapView.setMapFile(Utils.getFile(this, path));
		if (!openResult.isSuccess()) {
			return;
		}
		
		updateOverlay();
	}
	
	/**
	 * 更新地图上的覆盖图、路线、位置等信息
	 */
	private void updateOverlay() {

		ArrayList<OverlayItem> markers = getMarkers();
		Polyline routeLine = getRouteLine();
		
		List<OverlayItem> itemList = mListOverlay.getOverlayItems();
		synchronized (itemList) {
			itemList.clear();
			
			//添加房间区域
			if(mRooms!=null){
				List<Room> rooms=mRooms.get(mCurrentMapData.getId());
				List<GeoPoint> gps=new ArrayList<GeoPoint>();
				for(Room r:rooms){
					List<RoomArea> ras=appContext.getRoomAreaService().findAllByRoomId(r.getId());
					for(RoomArea ra:ras){
						gps.add(new GeoPoint(Double.parseDouble(ra.getLatitude()), Double.parseDouble(ra.getLongitude())));
					}
				}
				PolygonalChain pc = new PolygonalChain(gps);
				Polyline arealine = new Polyline(pc, appContext.getPaintStroke());
				itemList.add(arealine);
			}
			
			if (routeLine != null || markers != null) {
				if (routeLine != null) {
					itemList.add(routeLine);
				}
				if (markers != null) {
					itemList.addAll(markers);
				}
				//当前房间当前书架位置点
				if (mPOIMarker != null) {
					itemList.add(mPOIMarker);
					//设置当前目标位置点为中心点
					mMapView.getMapViewPosition().setCenter(mPOIMarker.getGeoPoint());
				}
			}
			MyLocation myLocation=appContext.getMyLocation();
			addMyLocMarker(myLocation);
			mMapView.getMapViewPosition().setCenter(mMapView.getMapViewPosition().getCenter());
		}
	}
	
	/**
	 * 获取地图上的覆盖图
	 */
	private ArrayList<OverlayItem> getMarkers() {
		PathSearchResult res = appContext.getPathSearchResult();
		if (res == null) {
			return null;
		}
		
		NavRoute route = res.getRoute();
		if (route == null) {
			return null;
		}

		NavStep step = route.getStep(mCurrentMapData.getId());
		if (step == null) {
			return null;
		}

		mMapView.getMapViewPosition().setCenter(step.getStart().getGeoPoint());
		ArrayList<OverlayItem> markers = new ArrayList<OverlayItem>();
		Marker lineStart = new Marker(step.getStart().getGeoPoint(), Marker.boundCenter(getResources().getDrawable(R.drawable.icon_node)));
		markers.add(lineStart);

		if (step.getEnd() != null) {
			Marker lineEnd = new Marker(step.getEnd().getGeoPoint(), Marker.boundCenter(getResources().getDrawable(R.drawable.icon_node)));
			markers.add(lineEnd);
		}

		if (res.getStartPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint start = (IndoorEndPoint) res.getStartPoint();
			//如果终点位置在当前地图上则添加起点覆盖图
			if(start.getMapId().equals(mCurrentMapData.getId())){
				Marker searchStart = new Marker(start.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_start)));
				markers.add(searchStart);
			}
		}

		if (res.getEndPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint end = (IndoorEndPoint) res.getEndPoint();
			//如果终点在当前地图则添加终点覆盖图
			if(end.getMapId().equals(mCurrentMapData.getId())){
				Marker searchEnd = new Marker(end.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_end)));
				markers.add(searchEnd);
			}
		}

		return markers;
	}
	
	/**
	 * 获取地图上覆盖的路线
	 */
	private Polyline getRouteLine() {
		
		PathSearchResult result=appContext.getPathSearchResult();
		if (result == null) {
			return null;
		}

		NavRoute route = result.getRoute();
		if (route == null) {
			return null;
		}

		NavStep step = route.getStep(mCurrentMapData.getId());
		if (step == null) {
			return null;
		}

		Polyline routeLine = null;
		if (step.size() > 1) {
			PolygonalChain pc = new PolygonalChain(step);
			routeLine = new Polyline(pc, appContext.getPaintStroke());
		}
		return routeLine;
	}
	
	/**
	 * 添加用户位置标记
	 */
	private void addMyLocMarker(MyLocation myLocation) {
		//如果当前定位的位置与当前的地图相同则添加位置标记
		if(myLocation.getMapId().equals(mCurrentMapData.getId())){
			if (mMyLocMarker == null) {
				mMyLocMarker = new MyLocationMarker(myLocation, Marker.boundCenter(getResources().getDrawable(R.drawable.ic_my_loc)));
			} else {
				mMyLocMarker.updateLocation(myLocation);
			}

			List<OverlayItem> itemList = mListOverlay.getOverlayItems();
			synchronized (itemList) {
				if (!itemList.contains(mMyLocMarker)) {
					itemList.add(mMyLocMarker);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void tapPOI(POI poi) {
		Bundle data = new Bundle();
		data.putSerializable(BUNDLEDATA_DATA, poi);
		showDialog(Utils.DLG_POI, data);
		mMapView.getMapViewPosition().setCenter(poi.getGeoPoint());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Utils.DLG_POI) {
			POI poi = (POI) args.getSerializable(BUNDLEDATA_DATA);
			((TextView) dialog.findViewById(R.id.poiName)).setText(poi.getName());
			dialog.findViewById(R.id.direction).setTag(poi);
			dialog.findViewById(R.id.poiName).setTag(poi);
			if (mPOIMarker != null) {
				dialog.getWindow().getAttributes().y = -50;
			} else {
				dialog.getWindow().getAttributes().y = 0;
			}
			return;
		} else {
			super.onPrepareDialog(id, dialog, args);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Utils.DLG_SEARCH_OPTION:
//			dialog = createSearchOptionDialog();
			break;
		case Utils.DLG_POI:
			dialog = CommonFn.createPOIDialog(this);
			break;
		default:
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.module_main_frame_introduction_btnHospital){
			//医院介绍
			Intent intent=new Intent(this,DoctorListActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.module_main_frame_introduction_btnDepartment){
			//部门介绍
			Intent intent=new Intent(this,DepartmentListActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.module_main_frame_introduction_btnDoctor){
			//医生介绍
			Intent intent=new Intent(this,DoctorListActivity.class);
			startActivity(intent);
		}if (v.getId() == R.id.poiName) {
//			POI r = (POI) v.getTag();
			//跳转至POI
		} else if (v.getId() == R.id.direction) {
			
			PathSearchTask search = new PathSearchTask(this);
			POI r = (POI) v.getTag();

			MyLocation myLocation = appContext.getMyLocation();
			
			if (myLocation != null) {
				
				EndPoint sp = new IndoorEndPoint(myLocation.getMapId(), myLocation.getGeoPoint());
				EndPoint ep = new IndoorEndPoint(mCurrentMapData.getId(), r.getGeoPoint(), r.getVertexId());
				search.execute(sp, ep);
			} else {
				Toast.makeText(this, "当前位置不可用", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	@Override
	public void onClickAt(float xPixel, float yPixel) {
		Projection projection = mMapView.getProjection();
		if (projection == null) {
			return;
		}

		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);

		if (mPOIMarker != null) {
			POI poi = mPOIMarker.getPOI();
			if (poi.inside(g)) {
				tapPOI(poi);
				return;
			}
		}

		if (mRooms.isEmpty()) {
			return;
		}

		List<Room> rooms = mRooms.get(mCurrentMapData.getId());
		for (Room r : rooms) {
			if (r.inside(g)) {
				tapPOI(r);
				return;
			}
		}
	}

	@Override
	public void onGetResult(PathSearchResult result) {
		mPOIMarker = null;
		if(result.getType()==PathSearchResult.Type.IN_BUILDING){
			setMapFile();
		}else if(result.getType()==PathSearchResult.Type.BETWEEN_BUILDING){
//			NavRoute route = result.indoorRouteStart;
			
		}else if(result.getType()==PathSearchResult.Type.OUTDOOR_INDOOR){
			finish();
		}
	}

	/**
	 * 加载所有房间数据
	 * @author start
	 *
	 */
	private class LoadingMapContentDataByRoom extends AsyncTask<Void, Void, Map<String,List<Room>>> {

		@Override
		protected Map<String,List<Room>> doInBackground(Void... params) {
			return appContext.getRoomService().findAllPullMap();
		}

		@Override
		protected void onPostExecute(Map<String,List<Room>> result) {
			super.onPostExecute(result);
			mRooms=result;
			//等加载完房间数据后再加载地图数据
			new LoadingMapContentDataByMapData().execute();
		}

	};
	
	/**
	 * 加载所有地图数据
	 * @author start
	 *
	 */
	private class LoadingMapContentDataByMapData extends AsyncTask<Void, Void, List<MapData>> {

		@Override
		protected List<MapData> doInBackground(Void... params) {
			return appContext.getMapDataService().findAll();
		}

		@Override
		protected void onPostExecute(List<MapData> result) {
			super.onPostExecute(result);
			//设置首次展示的地图数据
			mCurrentMapData=result.get(0);
			
			mMapDataAdapter=new MapDataAdapter(getLayoutInflater());
			mMapDataAdapter.setData(result);
			
			mMapIndexListView=(ListView)findViewById(R.id.module_main_frame_map_content_mapdataindexlist);
			mMapIndexListView.setAdapter(mMapDataAdapter);
			mMapIndexListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					MapData md=(MapData)view.getTag();
					//只有当点击的索引不同时才会进行切换
					if(!md.getId().equals(mCurrentMapData.getId())){
						mCurrentMapData=md;
						setMapFile();
					}
				}
				
			});
			
			//地图数据加载完毕后才把地图视图显示到页面上
			ViewGroup container = (ViewGroup) findViewById(R.id.module_main_frame_map_content);
			container.addView(mMapView, 0);
			
			setMapFile();
			
		}

	};
	
}