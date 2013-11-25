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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.start.core.AppConfig;
import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.NavRoute;
import com.start.model.nav.NavStep;
import com.start.model.nav.PathSearchResult;
import com.start.model.overlay.MyLocationMarker;
import com.start.model.overlay.POI;
import com.start.model.overlay.POIMarker;
import com.start.model.process.Junction;
import com.start.model.process.Junction.NodeType;
import com.start.model.process.ProcessService;
import com.start.model.process.ProcessService.ProcessListener;
import com.start.service.MapDataAdapter;
import com.start.service.PathSearchTask;
import com.start.service.PathSearchTask.PathSearchListener;
import com.start.utils.CommonFn;
import com.start.utils.Utils;
import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;

/**
 * 主界面
 * 
 * @author start
 * 
 */
public class MainActivity extends MapActivity implements OnTouchListener,
		OnClickListener, OnTapMapClickListener,OnEditorActionListener, OnFocusChangeListener, PathSearchListener,ProcessListener {

	private static final String BUNDLEDATA_DATA = "data";

	private AppContext appContext;

	private long lastPressTime;
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

	private TextView mModuleMainHeaderContentTitle;
	private Button mModuleMainHeaderContentLocation;
	private Button mModuleMainHeaderContentSearch;

	private Button mModuleMainFrameIntroduction_btnHospital;
	private Button mModuleMainFrameIntroduction_btnDepartment;
	private Button mModuleMainFrameIntroduction_btnDoctor;
	
	private TextView mModuleMainFrameProcessTitle;
	private Button mModuleMainFrameProcessNext;
	
	Map<String, ViewCollections> mMapViewCollections = new HashMap<String, ViewCollections>();

	private GestureDetector mGestureDetector;
	/**
	 * 当前使用的地图
	 */
	private MapData mCurrentMapData;
	/**
	 * 当前选重的房间标记集合
	 */
	private List<POIMarker> mPoiMarkers;
	/**
	 * 我的位置覆盖图
	 */
	private MyLocationMarker mMyLocMarker;
	/**
	 * 地图索引视图列表
	 */
	private ListView mMapIndexListView;
	/**
	 * 地图索引适配器
	 */
	private MapDataAdapter mMapDataAdapter;
	/**
	 * 地图上房间的集合
	 */
	private Map<String, List<Room>> mRooms;
	
	private ProcessService process;

	private EditText mapQuery;
	private Button mapButtonCancel;
	private LinearLayout mapLLQueryContentContainer;
	
	private Boolean isCurrentTabDepartment;
	private ArrayAdapter<Doctor> doctorArrayAdapter;
	private ArrayAdapter<Department> departmentArrayAdapter;
	private ListView mModuleMainFrameMapQueryList;
	
	public static String currentLocationDepartmentId;
	
	private Button mModuleMainFrameMapQueryContentTabDepartment;
	private Button mModuleMainFrameMapQueryContentTabDoctor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appContext = AppContext.getInstance();

		this.initHeaderView();
		this.initFooterView();
		this.initMainFrameView();

		new LoadingMapContentDataByRoom().execute();
		
		process=new ProcessService(this,this);
		process.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(currentLocationDepartmentId!=null){
			if(mCurSel!=1){
				setCurPoint(1);
			}
			Department department=appContext.getDepartmentService().findById(currentLocationDepartmentId);
			if(department!=null){
				Room room=appContext.getRoomService().findById(department.getMajorRoomId());
				if(room!=null){
					
					mCurrentMapData = mMapDataAdapter.getItem(mMapDataAdapter
							.getMapDataPositionByMapId(room.getMapId()));
					setMapFile();
					tapPOI(room);
					
				}
			}
			currentLocationDepartmentId=null;
		}
		if (mCurSel == 0 && !rboIntroduction.isChecked()) {
			rboIntroduction.setChecked(true);
			rboMap.setChecked(false);
			rboProcess.setChecked(false);
			rboFriend.setChecked(false);
			mCurSel = 0;
			setCurPoint(-1);
		}
	}

	private void setCurPoint(int index) {
		if (mCurSel != index) {
			if(index==-1){
				index=0;
			}
			if (index >= 0 && mFrameViewCount > index) {
				// 点击当前项刷新
				if (index == 0) {
					mModuleMainHeaderContentTitle.setText("介绍");
					mModuleMainHeaderContentLocation.setVisibility(View.GONE);
					mModuleMainHeaderContentSearch.setVisibility(View.GONE);
					mModuleMainFrameIntroductionContent.setVisibility(View.VISIBLE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				} else if (index == 1) {
					mModuleMainHeaderContentTitle.setText("地图");
					mModuleMainHeaderContentLocation.setVisibility(View.VISIBLE);
					mModuleMainHeaderContentSearch.setVisibility(View.VISIBLE);
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.VISIBLE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				} else if (index == 2) {
					mModuleMainHeaderContentTitle.setText("流程");
					mModuleMainHeaderContentLocation.setVisibility(View.GONE);
					mModuleMainHeaderContentSearch.setVisibility(View.GONE);
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.VISIBLE);
					mModuleMainFrameFriendContent.setVisibility(View.GONE);
				} else if (index == 3) {
					mModuleMainHeaderContentTitle.setText("好友");
					mModuleMainHeaderContentLocation.setVisibility(View.GONE);
					mModuleMainHeaderContentSearch.setVisibility(View.GONE);
					mModuleMainFrameIntroductionContent.setVisibility(View.GONE);
					mModuleMainFrameMapContent.setVisibility(View.GONE);
					mModuleMainFrameProcessContent.setVisibility(View.GONE);
					mModuleMainFrameFriendContent.setVisibility(View.VISIBLE);
				}
				mButtons[mCurSel].setChecked(false);
				mButtons[index].setChecked(true);
				mCurSel = index;
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
		mModuleMainHeaderContentTitle = (TextView) findViewById(R.id.module_main_header_content_title);
		mModuleMainHeaderContentLocation = (Button) findViewById(R.id.module_main_header_content_location);
		mModuleMainHeaderContentLocation.setOnClickListener(this);
		mModuleMainHeaderContentSearch = (Button) findViewById(R.id.module_main_header_content_search);
		mModuleMainHeaderContentSearch.setOnClickListener(this);
	}

	/**
	 * 初始化底部视图
	 */
	private void initFooterView() {
		rboIntroduction = (RadioButton) findViewById(R.id.main_footbar_introduction);
		rboMap = (RadioButton) findViewById(R.id.main_footbar_map);
		rboProcess = (RadioButton) findViewById(R.id.main_footbar_process);
		rboFriend = (RadioButton) findViewById(R.id.main_footbar_friend);

		imMore = (ImageView) findViewById(R.id.main_footbar_more);
		imMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,MoreActivity.class);
				startActivity(intent);

			}
		});

		mMainContentLayout = (LinearLayout) findViewById(R.id.main_content_layout);
		mModuleMainFrameMapContent = (View) findViewById(R.id.module_main_frame_map_content);
		

		mapQuery=(EditText)findViewById(R.id.module_main_frame_map_query);
		mapQuery.setOnFocusChangeListener(this);
		mapQuery.setOnEditorActionListener(this);
		mapButtonCancel=(Button)findViewById(R.id.module_main_frame_map_button_cancel);
		mapButtonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mapQuery.setText(R.string.empty);
				mModuleMainFrameMapContent.requestFocus();
				mapButtonCancel.setVisibility(View.GONE);
				mapLLQueryContentContainer.setVisibility(View.GONE);
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			}
		});
		mapLLQueryContentContainer=(LinearLayout)findViewById(R.id.module_main_frame_map_query_content_container);
		
		
		mModuleMainFrameIntroductionContent = (View) findViewById(R.id.module_main_frame_introduction_content);
		mModuleMainFrameProcessContent = (View) findViewById(R.id.module_main_frame_process_content);
		mModuleMainFrameFriendContent = (View) findViewById(R.id.module_main_frame_friend_content);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.module_main_footer_content);
		mFrameViewCount = mMainContentLayout.getChildCount();
		mButtons = new RadioButton[mFrameViewCount];

		for (int i = 0; i < mFrameViewCount; i++) {
			mButtons[i] = (RadioButton) linearLayout.getChildAt(i * 2);
			mButtons[i].setTag(i);
			mButtons[i].setChecked(false);
			mButtons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					setCurPoint((Integer) (v.getTag()));
				}
			});
		}

	}

	/**
	 * 初始化主体框架视图
	 */
	private void initMainFrameView() {
		// introduction
		mModuleMainFrameIntroduction_btnHospital = (Button) findViewById(R.id.module_main_frame_introduction_btnHospital);
		mModuleMainFrameIntroduction_btnHospital.setOnClickListener(this);
		mModuleMainFrameIntroduction_btnDepartment = (Button) findViewById(R.id.module_main_frame_introduction_btnDepartment);
		mModuleMainFrameIntroduction_btnDepartment.setOnClickListener(this);
		mModuleMainFrameIntroduction_btnDoctor = (Button) findViewById(R.id.module_main_frame_introduction_btnDoctor);
		mModuleMainFrameIntroduction_btnDoctor.setOnClickListener(this);
		
		// map
		mGestureDetector = new GestureDetector(this, new OnTapMapListener(this));
		
		// process
		mModuleMainFrameProcessTitle = (TextView) findViewById(R.id.module_main_frame_process_title);
		mModuleMainFrameProcessNext = (Button) findViewById(R.id.module_main_frame_process_next);
		mModuleMainFrameProcessNext.setOnClickListener(this);
		
		// friend

	}

	/**
	 * 设置视图的地图数据文件
	 */
	private void setMapFile() {
		if (mCurrentMapData == null) {
			return;
		}
		
		// 设置当前地图索引选重状态
		mMapIndexListView.setItemChecked(mMapDataAdapter
				.getMapDataPositionByMapId(mCurrentMapData.getId()), true);

		for (String mapId : mMapViewCollections.keySet()) {
			ViewCollections vc = mMapViewCollections.get(mapId);
			if (mapId.equals(mCurrentMapData.getId())) {
				vc.getMapView().setVisibility(View.VISIBLE);
			} else {
				vc.getMapView().setVisibility(View.GONE);
			}
		}

		updateOverlay();
	}

	/**
	 * 更新地图上的覆盖图、路线、位置等信息
	 */
	private void updateOverlay() {

		ArrayList<OverlayItem> markers = getMarkers();
		Polyline routeLine = getRouteLine();

		ViewCollections vc = mMapViewCollections.get(mCurrentMapData.getId());

		List<OverlayItem> itemList = vc.getListOverlay().getOverlayItems();
		synchronized (itemList) {
			itemList.clear();

			// TODO:添加房间区域,该IF为测试代码可删除
			if (mRooms != null) {
				List<Room> rooms = mRooms.get(mCurrentMapData.getId());
				List<GeoPoint> gps = new ArrayList<GeoPoint>();
				for (Room r : rooms) {
					List<RoomArea> ras = appContext.getRoomAreaService().findAllByRoomId(r.getId());
					for (RoomArea ra : ras) {
						gps.add(new GeoPoint(Double.parseDouble(ra.getLatitude()), Double.parseDouble(ra.getLongitude())));
					}
				}
				PolygonalChain pc = new PolygonalChain(gps);
				Polyline arealine = new Polyline(pc,appContext.getPaintStroke());
				itemList.add(arealine);
			}

			
			boolean isSetCenterPoint=true;
			if (markers != null || routeLine != null) {
				if (markers != null) {
					itemList.addAll(markers);
				}
				if (routeLine != null) {
					itemList.add(routeLine);
				}
			}
			
			MyLocation myLocation = appContext.getMyLocation();
			addMyLocMarker(myLocation);
			
			if(mPoiMarkers!=null){
				ArrayList<OverlayItem> poiMarkers = new ArrayList<OverlayItem>();
				for(POIMarker marker:mPoiMarkers){
					if(mCurrentMapData.getId().equals(marker.getPOI().getMapId())){
						poiMarkers.add(marker);
						if(isSetCenterPoint){
							// 设置当前第一个目标位置点为中心点
							vc.getMapView().getMapViewPosition().setCenter(marker.getPOI().getGeoPoint());
							isSetCenterPoint=false;
						}
					}
				}
				itemList.addAll(poiMarkers);
			}
			
			if(isSetCenterPoint){
				vc.getMapView().getMapViewPosition().setCenter(vc.getMapView().getMapViewPosition().getCenter());
			}
			
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

		ViewCollections vc = mMapViewCollections.get(mCurrentMapData.getId());

		vc.getMapView().getMapViewPosition()
				.setCenter(step.getStart().getGeoPoint());
		ArrayList<OverlayItem> markers = new ArrayList<OverlayItem>();
		Marker lineStart = new Marker(step.getStart().getGeoPoint(),
				Marker.boundCenter(getResources().getDrawable(
						R.drawable.icon_node)));
		markers.add(lineStart);

		if (step.getEnd() != null) {
			Marker lineEnd = new Marker(step.getEnd().getGeoPoint(),
					Marker.boundCenter(getResources().getDrawable(
							R.drawable.icon_node)));
			markers.add(lineEnd);
		}

		if (res.getStartPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint start = (IndoorEndPoint) res.getStartPoint();
			// 如果终点位置在当前地图上则添加起点覆盖图
			if (start.getMapId().equals(mCurrentMapData.getId())) {
				Marker searchStart = new Marker(start.getGeoPoint(),
						Marker.boundCenterBottom(getResources().getDrawable(
								R.drawable.icon_nav_start)));
				markers.add(searchStart);
			}
		}

		if (res.getEndPoint() instanceof IndoorEndPoint) {
			IndoorEndPoint end = (IndoorEndPoint) res.getEndPoint();
			// 如果终点在当前地图则添加终点覆盖图
			if (end.getMapId().equals(mCurrentMapData.getId())) {
				Marker searchEnd = new Marker(end.getGeoPoint(),
						Marker.boundCenterBottom(getResources().getDrawable(
								R.drawable.icon_nav_end)));
				markers.add(searchEnd);
			}
		}

		return markers;
	}

	/**
	 * 获取地图上覆盖的路线
	 */
	private Polyline getRouteLine() {

		PathSearchResult result = appContext.getPathSearchResult();
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
		// 如果当前定位的位置与当前的地图相同则添加位置标记
		if (myLocation.getMapId().equals(mCurrentMapData.getId())) {
			if (mMyLocMarker == null) {
				mMyLocMarker = new MyLocationMarker(myLocation,
						Marker.boundCenter(getResources().getDrawable(
								R.drawable.ic_my_loc)));
			} else {
				mMyLocMarker.updateLocation(myLocation);
			}

			ViewCollections vc = mMapViewCollections.get(mCurrentMapData
					.getId());

			List<OverlayItem> itemList = vc.getListOverlay().getOverlayItems();
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

		ViewCollections vc = mMapViewCollections.get(mCurrentMapData.getId());

		vc.getMapView().getMapViewPosition().setCenter(poi.getGeoPoint());
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Utils.DLG_POI) {
			POI poi = (POI) args.getSerializable(BUNDLEDATA_DATA);
			((TextView) dialog.findViewById(R.id.poiName)).setText(poi
					.getName());
			dialog.findViewById(R.id.direction).setTag(poi);
			dialog.findViewById(R.id.poiName).setTag(poi);
//			if (mPOIMarker != null) {
				dialog.getWindow().getAttributes().y = -50;
//			} else {
//				dialog.getWindow().getAttributes().y = 0;
//			}
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
			// dialog = createSearchOptionDialog();
			break;
		case Utils.DLG_POI:
			dialog = CommonFn.createPOIDialog(this);
			break;
		case Utils.DLG_EXIT_NAVIGATION:
			dialog = CommonFn.buildDialog(this, R.string.exit_navigation,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							appContext.setPathSearchResult(null);
							updateOverlay();
						}
					});
			break;
		default:
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.module_main_frame_introduction_btnHospital) {
			// 医院介绍
			
		} else if (v.getId() == R.id.module_main_frame_introduction_btnDepartment) {
			// 部门介绍
			Intent intent = new Intent(this, DepartmentListActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.module_main_frame_introduction_btnDoctor) {
			// 医生介绍
			Intent intent = new Intent(this, DoctorListActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.module_main_frame_process_next) {
			mModuleMainFrameProcessNext.setText("下一步");
			if(process.isProcessEnd()){
				mModuleMainFrameProcessTitle.setText("准备开始");
				process.init();
			}else{
				process.execute();
			}
		} else if (v.getId() == R.id.direction) {
			POI r = (POI) v.getTag();

			location(mCurrentMapData.getId(),r.getVertexId());

		} else if (v.getId() == R.id.poiName) {
			POI r = (POI) v.getTag();
			if(r!=null){
				DepartmentHasRoom departmentHasRoom=appContext.getDepartmentHasRoomService().findByRoomId(r.getId());
				if(departmentHasRoom!=null){
					Bundle bundle=new Bundle();
					bundle.putString(Department.COLUMN_NAME_ID, departmentHasRoom.getDepartmentId());
					Intent intent=new Intent(this,DepartmentDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		} else if (v.getId() == R.id.module_main_header_content_location) {
			MyLocation myLocation = appContext.getMyLocation();
			mCurrentMapData = mMapDataAdapter.getItem(mMapDataAdapter
					.getMapDataPositionByMapId(myLocation.getMapId()));
			setMapFile();
			addMyLocMarker(myLocation);
		} else if (v.getId() == R.id.module_main_header_content_search) {

		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_department) {
			if(!isCurrentTabDepartment){
				isCurrentTabDepartment=true;
				if(departmentArrayAdapter == null){
					List<Department> departments=appContext.getDepartmentService().findAll();
					departmentArrayAdapter = new ArrayAdapter<Department>(this, R.layout.lvitem_department, R.id.lvitem_department_name, departments);
				}
				v.setBackgroundResource(R.drawable.tabs_bar_left_on);
				mModuleMainFrameMapQueryContentTabDoctor.setBackgroundResource(R.drawable.tabs_bar_right_off);
				mModuleMainFrameMapQueryList.setAdapter(departmentArrayAdapter);
				departmentArrayAdapter.getFilter().filter(mapQuery.getText());
			}
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_doctor) {
			if(!isCurrentTabDepartment){
				isCurrentTabDepartment=false;
				if(doctorArrayAdapter == null){
					List<Doctor> doctors=appContext.getDoctorService().findAll();
					doctorArrayAdapter = new ArrayAdapter<Doctor>(this, R.layout.lvitem_department, R.id.lvitem_department_name, doctors);
				}
				v.setBackgroundResource(R.drawable.tabs_bar_left_on);
				mModuleMainFrameMapQueryContentTabDepartment.setBackgroundResource(R.drawable.tabs_bar_right_off);
				mModuleMainFrameMapQueryList.setAdapter(doctorArrayAdapter);
				doctorArrayAdapter.getFilter().filter(mapQuery.getText());
			}
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_restroom) {
			appContext.makeTextLong("房间");
		} else if (v.getId() == R.id.module_main_frame_map_query_content_tab_drinking_water) {
			appContext.makeTextLong("开水间");
		}
	}

	@Override
	public void onClickAt(float xPixel, float yPixel) {
		ViewCollections vc = mMapViewCollections.get(mCurrentMapData.getId());

		Projection projection = vc.getMapView().getProjection();
		if (projection == null) {
			return;
		}

		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);

		if (mPoiMarkers != null) {
			for(POIMarker marker:mPoiMarkers){
				POI poi = marker.getPOI();
				if (poi.inside(g)) {
					tapPOI(poi);
					return;
				}
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
		mPoiMarkers=null;
		if (result.getType() == PathSearchResult.Type.IN_BUILDING) {
			setMapFile();
		} else if (result.getType() == PathSearchResult.Type.BETWEEN_BUILDING) {
			// NavRoute route = result.indoorRouteStart;

		} else if (result.getType() == PathSearchResult.Type.OUTDOOR_INDOOR) {
			finish();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (appContext.getPathSearchResult() != null) {
			showDialog(Utils.DLG_EXIT_NAVIGATION);
		} else if (mPoiMarkers != null) {
			mPoiMarkers = null;
			updateOverlay();
			return;
		} else {
			if ((System.currentTimeMillis() - lastPressTime) > 2000) {
				appContext.makeTextShort("再按一次退出程序");
				lastPressTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
			}
		}
	}

	/**
	 * 加载所有房间数据
	 * 
	 * @author start
	 * 
	 */
	private class LoadingMapContentDataByRoom extends
			AsyncTask<Void, Void, Map<String, List<Room>>> {

		@Override
		protected Map<String, List<Room>> doInBackground(Void... params) {
			return appContext.getRoomService().findAllPullMap();
		}

		@Override
		protected void onPostExecute(Map<String, List<Room>> result) {
			super.onPostExecute(result);
			mRooms = result;
			// 等加载完房间数据后再加载地图数据
			new LoadingMapContentDataByMapData().execute();
		}

	};

	/**
	 * 加载所有地图数据
	 * 
	 * @author start
	 * 
	 */
	private class LoadingMapContentDataByMapData extends
			AsyncTask<Void, Void, List<MapData>> {

		@Override
		protected List<MapData> doInBackground(Void... params) {
			return appContext.getMapDataService().findAll();
		}

		@Override
		protected void onPostExecute(List<MapData> result) {
			super.onPostExecute(result);
			
			// 地图数据加载完毕后才把地图视图显示到页面上
			ViewGroup container = (ViewGroup) findViewById(R.id.module_main_frame_map_contentll);

			for (int i = 0; i < result.size(); i++) {
				MapData md = result.get(i);
				ViewCollections vc = new ViewCollections();

				String path = String.format("%1$s/%2$s.map",
						AppConfig.CONFIG_DATA_PATH_MEDMAP, md.getId());
				FileOpenResult openResult = vc.getMapView().setMapFile(
						Utils.getFile(MainActivity.this, path));
				if (!openResult.isSuccess()) {
					return;
				}

				container.addView(vc.getMapView(), 0);
				
				mMapViewCollections.put(md.getId(), vc);
			}

			mMapDataAdapter = new MapDataAdapter(getLayoutInflater());
			mMapDataAdapter.setData(result);

			mMapIndexListView = (ListView) findViewById(R.id.module_main_frame_map_content_mapdataindexlist);
			mMapIndexListView.setAdapter(mMapDataAdapter);
			mMapIndexListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					MapData md = (MapData) view.getTag();
					// 只有当点击的索引不同时才会进行切换
					if (!md.getId().equals(mCurrentMapData.getId())) {
						mCurrentMapData = md;
						setMapFile();
					}
				}

			});
			
			//初始显示地图为用户所在的位置地图
			MyLocation myLocation=appContext.getMyLocation();
			mCurrentMapData =mMapDataAdapter.getItem(mMapDataAdapter.getMapDataPositionByMapId(myLocation.getMapId()));

			setMapFile();

		}

	};

	private class ViewCollections {

		private MapView mapView;
		private ListOverlay listOverlay;

		public ViewCollections() {
			mapView = new MapView(MainActivity.this);
			mapView.setBuiltInZoomControls(true);
			mapView.setClickable(true);
			mapView.setOnTouchListener(MainActivity.this);

			listOverlay = new ListOverlay();
			mapView.getOverlays().add(listOverlay);

			mapView.setVisibility(View.GONE);
		}

		public MapView getMapView() {
			return mapView;
		}

		public ListOverlay getListOverlay() {
			return listOverlay;
		}

	}

	@Override
	public void location(String mapId, String vertexId) {

		if(mCurSel!=1){
			setCurPoint(1);
		}
		
		Vertex vertex=AppContext.getInstance().getVertexService().findById(vertexId);
		if(vertex!=null){
			
			MyLocation myLocation = appContext.getMyLocation();

			if (myLocation != null) {
				PathSearchTask search = new PathSearchTask(this);
				EndPoint sp = new IndoorEndPoint(myLocation.getMapId(),
						myLocation.getGeoPoint());
				EndPoint ep = new IndoorEndPoint(mapId,
						new GeoPoint(Double.parseDouble(vertex.getLatitude()), 
								Double.parseDouble(vertex.getLongitude())), 
								vertexId);
				search.execute(sp, ep);
			} else {
				Toast.makeText(this, "当前位置不可用", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	public void result(Junction jun) {
		if(process.isProcessEnd()){
			mModuleMainFrameProcessNext.setText("重新开始");
		}else{
			mModuleMainFrameProcessNext.setText("下一步");
		}
		if(jun.getNodeType()!=NodeType.SWITCH){
			mModuleMainFrameProcessTitle.setText("当前流程的节点："+jun.getTitle());
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			mapButtonCancel.setVisibility(View.VISIBLE);
			mapLLQueryContentContainer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_SEARCH){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mapQuery.getWindowToken(), 0);
			
			return true;
		}
		return false;
	}

}