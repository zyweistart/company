package com.start.navigation;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.start.model.nav.NavRoute;
import com.start.model.nav.NavStep;
import com.start.model.nav.PathSearchResult;
import com.start.model.navigation.MyLocation;
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
import com.start.widget.ScrollLayout;

/**
 * 主界面
 * @author start
 *
 */
public class MainActivity extends MapActivity implements OnTouchListener,OnClickListener,OnTapMapClickListener,PathSearchListener {

	private AppContext appContext;
	
	private int mCurSel;
	private int mViewCount;
	private RadioButton[] mButtons;
	private ScrollLayout mScrollLayout;
	
	private RadioButton rboIntroduction;
	private RadioButton rboMap;
	private RadioButton rboProcess;
	private RadioButton rboFriend;
	private ImageView imMore;
	
	private Button module_main_frame_introduction_btnHospital;
	private Button module_main_frame_introduction_btnDepartment;
	private Button module_main_frame_introduction_btnDoctor;
	
	private GestureDetector mGestureDetector;
	
	private ListView mMapIndexListView;
	private MapView mMapView;
	private ListOverlay mListOverlay;
	/**
	 * 我的位置
	 */
	private MyLocationMarker mMyLocMarker;
	/**
	 * 当前打开的地图编号
	 */
	private MapData currentMapData;
	/**
	 * 当前选重的Marker
	 */
	protected POIMarker mPOIMarker;
	/**
	 * 当前地图对应的所有房间列表
	 */
	private Map<String,List<Room>> mRooms=new HashMap<String,List<Room>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext=AppContext.getInstance();
		
		MapDataAdapter mapAdapter=new MapDataAdapter(this.getLayoutInflater());
		
		mMapIndexListView=(ListView)findViewById(R.id.module_main_frame_map_content_mapdataindexlist);
		mMapIndexListView.setAdapter(mapAdapter);
		mMapIndexListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				MapData md=(MapData)view.getTag();
				if(!md.getId().equals(currentMapData.getId())){
					currentMapData=md;
					setMapFile();
				}
			}
			
		});
		List<MapData> mapDatas=appContext.getMapDataService().findAll();
		mRooms=appContext.getRoomService().findAllPullMap();
		
		currentMapData=mapDatas.get(0);
		mapAdapter.setData(mapDatas);
		
		this.initHeaderView();
        this.initFooterView();
        this.initMainFrameView();
        
	}
	
	@Override
    protected void onResume() {
	    	super.onResume();
	    	if(mViewCount == 0) mViewCount = mScrollLayout.getChildCount();
	    	if(mCurSel == 0 && !rboIntroduction.isChecked()) {
	    		rboIntroduction.setChecked(true);
	    		rboMap.setChecked(false);
	    		rboProcess.setChecked(false);
	    		rboFriend.setChecked(false);
	    	}
	    	//读取左右滑动配置
	    	mScrollLayout.setIsScroll(false);
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
		
	    	mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
	    	
	    	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.module_main_footer_content);
	    	mViewCount = mScrollLayout.getChildCount();
	    	mButtons = new RadioButton[mViewCount];
	    	
	    	for(int i = 0; i < mViewCount; i++) 	{
	    		mButtons[i] = (RadioButton) linearLayout.getChildAt(i*2);
	    		mButtons[i].setTag(i);
	    		mButtons[i].setChecked(false);
	    		mButtons[i].setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						int pos = (Integer)(v.getTag());
			    			if(mCurSel == pos) {
			    				//点击当前项刷新
			    			}
						mScrollLayout.snapToScreen(pos);
					}
				});
	    	}
	    	
	    	//设置第一显示屏
	    	mCurSel = 0;
	    	mButtons[mCurSel].setChecked(true);
	    	
	    	mScrollLayout.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
				public void OnViewChange(int viewIndex) {
					//切换列表视图-如果列表数据为空：加载数据
					setCurPoint(viewIndex);
				}
			});
	}
	
	/**
     * 设置底部栏当前焦点
     * @param index
     */
    private void setCurPoint(int index) {
	    	if (index < 0 || index > mViewCount - 1 || mCurSel == index)
	    		return;
	   	
	    	mButtons[mCurSel].setChecked(false);
	    	mButtons[index].setChecked(true);	
	    	mCurSel = index;
    }

    /**
     * 初始化主体框架视图
     */
	private void initMainFrameView() {
		//introduction
		module_main_frame_introduction_btnHospital = (Button) findViewById(R.id.module_main_frame_introduction_btnHospital);
		module_main_frame_introduction_btnHospital.setOnClickListener(this);
		module_main_frame_introduction_btnDepartment = (Button) findViewById(R.id.module_main_frame_introduction_btnDepartment);
		module_main_frame_introduction_btnDepartment.setOnClickListener(this);
		module_main_frame_introduction_btnDoctor = (Button) findViewById(R.id.module_main_frame_introduction_btnDoctor);
		module_main_frame_introduction_btnDoctor.setOnClickListener(this);
		//map
		mGestureDetector = new GestureDetector(this,new OnTapMapListener(this));
		
		ViewGroup container = (ViewGroup) findViewById(R.id.module_main_frame_map_content);
		mMapView = new MapView(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		mMapView.setOnTouchListener(this);
		container.addView(mMapView, 0);
		
		mListOverlay = new ListOverlay();
		mMapView.getOverlays().add(mListOverlay);
		
		setMapFile();
		
		//process
		
		//friend
		
	}
	
	private void setMapFile() {
		
//		mMapIndexListView.setItemChecked(mListViewCheckedPos, true);
		
		String path = String.format("%1$s/%2$s.map", AppConfig.CONFIG_DATA_PATH_MEDMAP,currentMapData.getId());
		FileOpenResult openResult = mMapView.setMapFile(Utils.getFile(this, path));
		if (!openResult.isSuccess()) {
			return;
		}
		updateOverlay();
	}
	
	private void updateOverlay() {

		ArrayList<OverlayItem> markers = getMarkers();
		Polyline routeLine = getRouteLine("");
		
		List<Room> rooms=mRooms.get(currentMapData.getId());

		List<GeoPoint> gps=new ArrayList<GeoPoint>();
		
		for(Room r:rooms){
			
			List<RoomArea> ras=appContext.getRoomAreaService().findAllByRoomId(r.getId());
			
			for(RoomArea ra:ras){
				gps.add(new GeoPoint(Double.parseDouble(ra.getLatitude()), Double.parseDouble(ra.getLongitude())));
			}
		}
		
		PolygonalChain pc = new PolygonalChain(gps);
		routeLine = new Polyline(pc, appContext.getPaintStroke());

		List<OverlayItem> itemList = mListOverlay.getOverlayItems();
		synchronized (itemList) {
			itemList.clear();

			if (routeLine != null || markers != null) {
				if (routeLine != null) {
					itemList.add(routeLine);
				}
				if (markers != null) {
					itemList.addAll(markers);
				}
				MyLocation myLocation=appContext.getMyLocation(currentMapData);
				addMyLocMarker(myLocation);
//				当前房间当前书架位置点
//				if (mPOIMarker != null) {
//					itemList.add(mPOIMarker);
//					//设置当前目标位置点为中心点
//					mMapView.getMapViewPosition().setCenter(mPOIMarker.getGeoPoint());
//				}
			}
			mMapView.getMapViewPosition().setCenter(mMapView.getMapViewPosition().getCenter());
		}
	}
	
	private ArrayList<OverlayItem> getMarkers() {
		PathSearchResult res = appContext.getPathSearchResult();
		if (res == null) {
			return null;
		}
		
		NavRoute route = res.getRouteByBuilding();
		if (route == null) {
			return null;
		}

		NavStep step = route.getStep(currentMapData.getId());
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
			Marker searchStart = new Marker(start.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_start)));
			markers.add(searchStart);
		}

		if (res.getEndPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint end = (IndoorEndPoint) res.getEndPoint();
			Marker searchEnd = new Marker(end.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_end)));
			markers.add(searchEnd);
		}

		return markers;
	}
	
	private Polyline getRouteLine(String currentMapId) {
		return null;
	}
	
	private void addMyLocMarker(MyLocation myLocation) {
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
	
	@SuppressWarnings("deprecation")
	private void tapPOI(POI poi) {
		Bundle data = new Bundle();
		data.putSerializable("data", poi);
		showDialog(Utils.DLG_POI, data);
		mMapView.getMapViewPosition().setCenter(poi.getGeoPoint());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Utils.DLG_POI) {
			POI poi = (POI) args.getSerializable("data");
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

			MyLocation myLocation = appContext.getMyLocation(currentMapData);
			
			if (myLocation != null) {
				EndPoint sp  = new IndoorEndPoint(myLocation.getMapData(), myLocation.getGeoPoint());
				EndPoint ep = new IndoorEndPoint(currentMapData, r.getGeoPoint(), r.getVertexId());
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

		List<Room> rooms = mRooms.get(currentMapData.getId());
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
//			NavRoute route = result.indoorRouteEnd;
			
		}else if(result.getType()==PathSearchResult.Type.BETWEEN_BUILDING){
//			NavRoute route = result.indoorRouteStart;
			
		}else if(result.getType()==PathSearchResult.Type.OUTDOOR_INDOOR){
			finish();
		}
	}

}