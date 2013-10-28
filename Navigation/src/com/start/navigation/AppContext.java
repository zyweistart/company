package com.start.navigation;

import android.app.Application;
import android.widget.Toast;

import com.start.core.AppConfig.PreferencesConfig;
import com.start.utils.SharedPreferencesUtils;

public class AppContext extends Application {
	
	private static AppContext mInstance;
    private SharedPreferencesUtils sharedPreferencesUtils;

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
	
    /**
	 * 主体框架是否滚动
	 */
	public boolean isScrollLayoutScrool(){
		return AppContext.getInstance().getSharedPreferencesUtils().getBoolean(PreferencesConfig.ScrollLayoutisScrool, false);
	}
    
}