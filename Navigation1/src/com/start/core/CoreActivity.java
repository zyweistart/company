package com.start.core;

import android.app.Activity;

import com.start.navigation.AppContext;

public abstract class CoreActivity extends Activity{

	protected final String TAG=this.getClass().getSimpleName();
	
	protected static final int DLG_SEARCH_OPTION=1;
	protected static final int DLG_EXIT_NAVIGATION=2;
	
	protected void makeTextShort(String text){
		AppContext.getInstance().makeTextShort(text);
    }
    
	protected void makeTextLong(String text){
    		AppContext.getInstance().makeTextLong(text);
    }
	
}