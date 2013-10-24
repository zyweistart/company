package com.start.navigation;

import android.os.Bundle;
import android.view.Window;

import com.start.core.CoreActivity;

public class MainActivity extends CoreActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.getInstance().instanceBMapManager();
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
	}

}
