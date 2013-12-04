package com.start.navigation;

import android.os.Bundle;

import com.start.core.CoreActivity;

/**
 * 注册
 * @author start
 *
 */
public class RegisterActivity extends CoreActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setCurrentActivityTitle(R.string.activity_register);
	}

}
