package com.start.navigation;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.start.core.AppConfig.PreferencesConfig;
import com.start.model.navigation.MyLocation;
import com.start.service.DepartmentService;
import com.start.service.DoctorService;
import com.start.service.MapDataService;
import com.start.service.RoomAreaService;
import com.start.service.RoomService;
import com.start.service.VertexService;
import com.start.utils.SharedPreferencesUtils;

public class AppContext extends Application {
	
	private static AppContext mInstance;
    private SharedPreferencesUtils sharedPreferencesUtils;
    
    private Paint mPaintStroke;
    
    private DoctorService doctorService;
    private DepartmentService departmentService;
    private MapDataService mapDataService;
    private RoomService roomService;
    private VertexService vertexService;
    private RoomAreaService roomAreaService;

	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
	}
	
	public static AppContext getInstance() {
		return mInstance;
	}
	
	public SharedPreferencesUtils getSharedPreferencesUtils() {
		if(sharedPreferencesUtils==null){
			sharedPreferencesUtils=new SharedPreferencesUtils(this);
		}
		return sharedPreferencesUtils;
	}
	
	public void makeTextShort(String text){
		Toast.makeText(AppContext.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    
    public void makeTextLong(String text){
    		Toast.makeText(AppContext.getInstance().getApplicationContext(), text, Toast.LENGTH_LONG).show();
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
    
    public DoctorService getDoctorService() {
    		if(doctorService==null){
    			doctorService=new DoctorService(this);
    		}
		return doctorService;
	}

	public DepartmentService getDepartmentService() {
		if(departmentService==null){
			departmentService=new DepartmentService(this);
		}
		return departmentService;
	}

	public MapDataService getMapDataService() {
		if(mapDataService==null){
			mapDataService=new MapDataService(this);
		}
		return mapDataService;
	}
	
	public RoomService getRoomService() {
		if(roomService==null){
			roomService=new RoomService(this);
		}
		return roomService;
	}
	
	public VertexService getVertexService() {
		if(vertexService==null){
			vertexService=new VertexService(this);
		}
		return vertexService;
	}

	public RoomAreaService getRoomAreaService() {
		if(roomAreaService==null){
			roomAreaService=new RoomAreaService(this);
		}
		return roomAreaService;
	}

	public MyLocation getMyLocation(){
		return null;
	}
	
	/**
	 * 主体框架是否滚动
	 */
	public boolean isScrollLayoutScrool(){
		return AppContext.getInstance().getSharedPreferencesUtils().getBoolean(PreferencesConfig.ScrollLayoutisScrool, false);
	}
    
}