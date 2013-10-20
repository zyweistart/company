package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.start.core.CoreActivity;

public class MainActivity extends CoreActivity implements OnClickListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.getInstance().instanceBMapManager();
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.navHospital:
				startActivity(new Intent(MainActivity.this,MapOutdoorActivity.class));
				break;
			case R.id.navLibrary:
				startActivity(new Intent(MainActivity.this,MapOutdoorActivity.class));
				break;
		}
	}

}
