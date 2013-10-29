package com.start.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

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

import com.start.core.AppConfig;
import com.start.model.Room;
import com.start.model.overlay.POI;
import com.start.model.overlay.POIMarker;
import com.start.service.MapDataAdapter;
import com.start.service.MapDataService;
import com.start.utils.Utils;
import com.start.widget.OnTapMapListener;
import com.start.widget.OnTapMapListener.OnTapMapClickListener;
import com.start.widget.ScrollLayout;

/**
 * 主界面
 * @author start
 *
 */
public class MainActivity extends MapActivity implements OnTouchListener,OnClickListener,OnTapMapClickListener {

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
	
	private MapView mMapView;
	/**
	 * 当前打开的地图编号
	 */
	private String currentMapID;
	/**
	 * 当前选重的Marker
	 */
	protected POIMarker mPOIMarker;
	/**
	 * 当前地图对应的所有房间列表
	 */
	private Map<String,List<Room>> mRooms=new HashMap<String,List<Room>>();
	
	private MapDataService mapDataService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext=AppContext.getInstance();
		
		mapDataService=new MapDataService(this);
		
		MapDataAdapter mapAdapter=new MapDataAdapter(this.getLayoutInflater());
		
		ListView mapIndexListView=(ListView)findViewById(R.id.module_main_frame_map_content_mapdataindexlist);
		mapIndexListView.setAdapter(mapAdapter);
		mapIndexListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			}
			
		});
		mapAdapter.setData(mapDataService.findAll());
		
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
	    	mScrollLayout.setIsScroll(appContext.isScrollLayoutScrool());
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
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
			    				//TODO:点击当前项刷新
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
					//TODO:切换列表视图-如果列表数据为空：加载数据
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

    private GestureDetector mGestureDetector;
    
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
		setMapFile();
		
		//process
		
		//friend
		
	}
	
	private void setMapFile() {
		String path = String.format("%1$s/%2$s.map", AppConfig.CONFIG_DATA_PATH_MEDMAP,"2main");
		String fullPath=Utils.getFile(this, path).getPath();
		FileOpenResult openResult = mMapView.setMapFile(Utils.getFile(this, path));
		if (!openResult.isSuccess()) {
			return;
		}
		updateOverlay();
	}
	
	private void updateOverlay() {
		
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

		List<Room> rooms = mRooms.get(currentMapID);
		for (Room r : rooms) {
			if (r.inside(g)) {
				tapPOI(r);
				return;
			}
		}
		
	}

	private void tapPOI(POI poi) {
		
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
		}
	}
	
}