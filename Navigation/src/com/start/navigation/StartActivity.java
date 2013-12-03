package com.start.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.start.core.CoreActivity;

/**
 * 开始页
 * @author start
 *
 */
public class StartActivity extends CoreActivity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		//验证是否已经进行初始化
//		if (!prefs.getBoolean("init", false)) {
//			// Register a listener to be invoked when initialization completes
//			prefs.registerOnSharedPreferenceChangeListener(this);
//			// Start asynchronous task to import data from configuration into database
//			new ImportConfigDataTask(getApplicationContext()).execute();
//		} else {
			// Go ahead to next screen
			forward();
//		}	
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (TextUtils.equals("init", key)) {
			
			prefs.unregisterOnSharedPreferenceChangeListener(this);
			
			forward();
		}
	}
	
	private void forward() {
		final View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				startActivity(new Intent(StartActivity.this, MapDataListActivity.class));
				finish();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}
			
		});
	}
	
}