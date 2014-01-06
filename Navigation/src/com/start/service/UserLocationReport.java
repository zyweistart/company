package com.start.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.start.core.Constant;
import com.start.model.UIRunnable;
import com.start.navigation.AppContext;

public class UserLocationReport extends Thread {

	private HttpService httpService;
	private AppContext mAppContext;
	private String lastLocation;
	private Boolean flag;
	
	public UserLocationReport(Activity activity){
		httpService=new HttpService(activity);
		mAppContext=AppContext.getInstance();
		flag=true;
	}
	
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		while(true&&flag){
			//未登录则不上报位置信息
			if(mAppContext.isLogin()){
				final String data=mAppContext.getSharedPreferencesUtils().getString(Constant.SharedPreferences.USERLOCATION, Constant.EMPTYSTR);
				if(!Constant.EMPTYSTR.equals(data)&&!data.equals(lastLocation)){
					
					String[] info=data.split(";");
					if(info.length==3){
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("maprecordno",mAppContext.getCurrentDataNo());
						requestParams.put("submapno", info[0]);
						requestParams.put("latitude", info[1]);
						requestParams.put("longitude", info[2]);
						httpService.exeNetRequest(null,Constant.ServerAPI.userposReport,requestParams,null,new UIRunnable() {
							
							@Override
							public void run() {}
						});
					}
					lastLocation=data;
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
