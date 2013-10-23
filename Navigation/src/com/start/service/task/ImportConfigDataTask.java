package com.start.service.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.start.core.AppConfig;
import com.start.core.CoreService;
import com.start.model.Department;
import com.start.model.DepartmentHasRoom;
import com.start.model.Doctor;
import com.start.model.Edge;
import com.start.model.MapData;
import com.start.model.Room;
import com.start.model.RoomArea;
import com.start.model.Vertex;

public class ImportConfigDataTask extends AsyncTask<Void, Void, Boolean> {

	private static final String DEBUG_TAG = "ImportConfigDataTask";
	
	private Context mContext;
	private AssetManager mAssetManager;
	private CoreService mCoreService;
	
	public ImportConfigDataTask(Context context) {
		this.mContext = context;
		this.mAssetManager = context.getAssets();
		this.mCoreService=new CoreService(mContext);
	}

	private List<String[]> readFileData(String fullFilePath){
		List<String[]> datas=null;
		InputStream is = null;
		try {
			datas=new ArrayList<String[]>();
			is = mAssetManager.open(fullFilePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens=line.split(";");
				if(tokens==null){
					continue;
				}
				datas.add(tokens);
			}
			return datas;
		} catch (IOException e) {
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally{
					is=null;
				}
			}
		}
		return null;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			//导航数据1
			for(String fileName:mAssetManager.list(AppConfig.CONFIG_DATA_PATH_MEDMAP)){
				if(AppConfig.F_DEPARTMENT.equals(fileName)){
					importDepartment(fileName);
				}else if(AppConfig.F_DEPARTMENTHASROOM.equals(fileName)){
					importDepartmentHasRoom(fileName);
				}else if(AppConfig.F_DOCTOR.equals(fileName)){
					importDoctor(fileName);
				}else if(AppConfig.F_EDGE.equals(fileName)){
					importEdge(fileName);
				}else if(AppConfig.F_MAPDATA.equals(fileName)){
					importMapData(fileName);
				}else if(AppConfig.F_ROOM.equals(fileName)){
					importRoom(fileName);
				}else if(AppConfig.F_ROOMAREA.equals(fileName)){
					importRoomArea(fileName);
				}else if(AppConfig.F_VERTEX.equals(fileName)){
					importEdge(fileName);
				}
			}
		} catch (IOException e) {
			Log.e(DEBUG_TAG, e.getMessage());
			return false;
		}
		return true;
	}
	
	void importDepartment(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=4){
					Map<String,String> values=new HashMap<String,String>();
					values.put(Department._ID, data[0]);
					values.put(Department.COLUMN_NAME_NAME, data[1]);
					values.put(Department.COLUMN_NAME_INTRODUCTION, data[2]);
					values.put(Department.COLUMN_NAME_MAJORROOMID, data[3]);
					mCoreService.save(Department.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importDepartmentHasRoom(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=2){
					Map<String,String> values=new HashMap<String,String>();
					values.put(DepartmentHasRoom.COLUMN_NAME_DEPARTMENTID, data[0]);
					values.put(DepartmentHasRoom.COLUMN_NAME_ROOMID, data[1]);
					mCoreService.save(DepartmentHasRoom.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importRoom(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=4){
					Map<String,String> values=new HashMap<String,String>();
					values.put(Room.COLUMN_NAME_MAPID, data[0]);
					values.put(Room._ID, data[1]);
					values.put(Room.COLUMN_NAME_NAME, data[2]);
					values.put(Room.COLUMN_NAME_VERTEXID, data[3]);
					mCoreService.save(Room.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importRoomArea(String fileName){
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=3){
					Map<String,String> values=new HashMap<String,String>();
					values.put(RoomArea.COLUMN_NAME_ROOMID, data[0]);
					values.put(RoomArea.COLUMN_NAME_LATITUDE, data[1]);
					values.put(RoomArea.COLUMN_NAME_LONGITUDE, data[2]);
					mCoreService.save(RoomArea.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importDoctor(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=7){
					Map<String,String> values=new HashMap<String,String>();
					values.put(Doctor._ID, data[0]);
					values.put(Doctor.COLUMN_NAME_NAME, data[1]);
					values.put(Doctor.COLUMN_NAME_SEX, data[2]);
					values.put(Doctor.COLUMN_NAME_TITLE, data[3]);
					values.put(Doctor.COLUMN_NAME_SPECIALTY, data[4]);
					values.put(Doctor.COLUMN_NAME_INTRODUCTION, data[5]);
					values.put(Doctor.COLUMN_NAME_DEPARTMENTID, data[6]);
					mCoreService.save(Doctor.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importVertex(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=4){
					Map<String,String> values=new HashMap<String,String>();
					values.put(Vertex.COLUMN_NAME_MAPID, data[0]);
					values.put(Vertex._ID, data[1]);
					values.put(Vertex.COLUMN_NAME_LATITUDE, data[2]);
					values.put(Vertex.COLUMN_NAME_LONGITUDE, data[3]);
					mCoreService.save(Vertex.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importMapData(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=2){
					Map<String,String> values=new HashMap<String,String>();
					values.put(MapData._ID, data[0]);
					values.put(MapData.COLUMN_NAME_NAME, data[1]);
					mCoreService.save(MapData.TABLE_NAME,values);
				}
			}
		}
	}
	
	void importEdge(String fileName) {
		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH_MEDMAP, fileName);
		List<String[]> datas=readFileData(filePath);
		if(datas!=null){
			for(String[] data:datas){
				if(data.length!=3){
					Map<String,String> values=new HashMap<String,String>();
//					values.put(Edge._ID, data[0]);mapid
					values.put(Edge._ID, data[1]);
					values.put(Edge.COLUMN_NAME_VERTEXSTARTID, data[2]);
					values.put(Edge.COLUMN_NAME_VERTEXENDID, data[3]);
					mCoreService.save(Edge.TABLE_NAME,values);
				}
			}
		}
	}
	
}
