package com.start.core;

import android.app.Activity;

import com.start.navigation.AppContext;

public abstract class CoreActivity extends Activity{

	protected final String TAG=this.getClass().getSimpleName();
	
	protected AppContext getAppContext(){
		return AppContext.getInstance();
	}
	
	protected void makeTextShort(String text){
		getAppContext().makeTextShort(text);
    }
    
	protected void makeTextLong(String text){
		getAppContext().makeTextLong(text);
    }
	
}