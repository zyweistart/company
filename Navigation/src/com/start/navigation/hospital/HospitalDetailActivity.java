package com.start.navigation.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.start.core.CoreActivity;
import com.start.navigation.R;

/**
 * 医院详细
 * @author start
 *
 */
public class HospitalDetailActivity extends CoreActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hospital_detail);
		setCurrentActivityTitle(R.string.activity_title_hospital_detail);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.hotspital_detail_show){
			HospitalMainActivity.ShowMainData=true;
			startActivity(new Intent(this,HospitalMainActivity.class));
			finish();
		}
	}

}