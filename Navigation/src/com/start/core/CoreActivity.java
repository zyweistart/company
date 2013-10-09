package com.start.core;

import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.widget.MenuDialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

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
	
	protected Dialog createSearchOptionDialog() {
		MenuDialog menuDialog = new MenuDialog(this, R.style.dialog);
		Window win = menuDialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP | Gravity.RIGHT;
		params.y = getResources().getDimensionPixelSize(R.dimen.actionbar_height);
		win.setAttributes(params);
		menuDialog.setCanceledOnTouchOutside(true);
		return menuDialog;
	}
	
}
