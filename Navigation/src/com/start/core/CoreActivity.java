package com.start.core;

import android.app.Activity;
import android.widget.TextView;

import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.service.HttpService;

public abstract class CoreActivity extends Activity{

	protected final String TAG=this.getClass().getSimpleName();
	
	private HttpService httpService;
	
	public HttpService getHttpService() {
		if(httpService==null){
			httpService=new HttpService(this);
		}
		return httpService;
	}

	public AppContext getAppContext(){
		return AppContext.getInstance();
	}
	
	public void makeTextShort(int resId){
		getAppContext().makeTextShort(resId);
    }
	
	public void makeTextShort(String text){
		getAppContext().makeTextShort(text);
    }
    
	public void makeTextLong(int resId){
		getAppContext().makeTextLong(resId);
    }
	
	public void makeTextLong(String text){
		getAppContext().makeTextLong(text);
    }
	
	public void setCurrentActivityTitle(int resId){
		setCurrentActivityTitle(getString(resId));
	}
	
	public void setCurrentActivityTitle(String title){
		TextView tv=(TextView)findViewById(R.id.module_main_header_content_title);
		if(tv!=null){
			tv.setText(title);
		}
	}
	
}