package com.start.navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.start.service.ImportConfigDataTask;

public class SplashActivity extends Activity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//验证是否已经进行初始化
		if (!prefs.getBoolean("init", false)) {
			// Register a listener to be invoked when initialization completes
			prefs.registerOnSharedPreferenceChangeListener(this);
			// Start asynchronous task to import data from configuration into database
			new ImportConfigDataTask(getApplicationContext()).execute();
		} else {
			// Go ahead to next screen
			forward();
		}		
	}
	
	private void forward() {
		// Start login screen
		startActivity(new Intent(this, MainActivity.class));
		// Finish current screen since user won't go back
		finish();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (TextUtils.equals("init", key)) {
			
			prefs.unregisterOnSharedPreferenceChangeListener(this);
			
			forward();
		}
	}
	
}