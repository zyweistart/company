package com.start.navigation;

import java.util.Map;

import org.mapsforge.core.model.GeoPoint;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.start.model.nav.MyLocation;
import com.start.model.nav.PathSearchResult;
import com.start.service.DepartmentHasRoomService;
import com.start.service.DepartmentService;
import com.start.service.DoctorService;
import com.start.service.EdgeService;
import com.start.service.MapDataService;
import com.start.service.RoomAreaService;
import com.start.service.RoomService;
import com.start.service.VertexService;
import com.start.utils.SharedPreferencesUtils;

public class AppContext extends Application {
	
	private static AppContext mInstance;
    private SharedPreferencesUtils sharedPreferencesUtils;
    
    private PathSearchResult pathSearchResult;
    
    private Paint mPaintStroke;
    
    private DoctorService doctorService;
    private DepartmentService departmentService;
    private DepartmentHasRoomService departmentHasRoomService;
    private MapDataService mapDataService;
    private RoomService roomService;
    private VertexService vertexService;
    private RoomAreaService roomAreaService;
    private EdgeService edgeService;

	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
	}
	
	public static AppContext getInstance() {
		return mInstance;
	}
	
	public String getDeviceId(){
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		return tm.getDeviceId();
	}
	
	public SharedPreferencesUtils getSharedPreferencesUtils() {
		if(sharedPreferencesUtils==null){
			sharedPreferencesUtils=new SharedPreferencesUtils(this);
		}
		return sharedPreferencesUtils;
	}
	
	public PathSearchResult getPathSearchResult() {
		return pathSearchResult;
	}

	public void setPathSearchResult(PathSearchResult pathSearchResult) {
		this.pathSearchResult = pathSearchResult;
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

	public DepartmentHasRoomService getDepartmentHasRoomService() {
		if(departmentHasRoomService==null){
			departmentHasRoomService=new DepartmentHasRoomService(this);
		}
		return departmentHasRoomService;
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
	
	public EdgeService getEdgeService() {
		if(edgeService==null){
			edgeService=new EdgeService(this);
		}
		return edgeService;
	}

	/**
	 * 当前用户是否已经登录
	 * @return
	 */
	public Boolean isLogin(){
		return userInfo!=null;
	}
	
	private Map<String,Map<String,String>> userInfo;
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public Map<String,String> getUserInfoByKey(String key){
		if(userInfo!=null){
			return userInfo.get(key);
		}
		return null;
	}
	
	public void setUserInfo(Map<String, Map<String, String>> userInfo) {
		this.userInfo = userInfo;
	}

	public MyLocation getMyLocation(){
		return new MyLocation("0102", new GeoPoint(0.0007388,0.0012458));
	}
	
	public void makeTextShort(int resId) {
		sendMessage(Toast.LENGTH_SHORT,getString(resId));
	}
	
	public void makeTextShort(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_SHORT,text);
		}
	}

	public void makeTextLong(int resId) {
		sendMessage(Toast.LENGTH_LONG,getString(resId));
	}
	
	public void makeTextLong(String text) {
		if(!TextUtils.isEmpty(text)){
			sendMessage(Toast.LENGTH_LONG,text);
		}
	}
	
	public void sendMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendMessage(msg);
	}
	
	public void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}
	
	public Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Toast.LENGTH_SHORT:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case Toast.LENGTH_LONG:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
				break;
			}
		}
		
	};
    
}