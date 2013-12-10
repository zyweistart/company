package com.start.navigation.hospital;

import android.os.Bundle;

import com.start.core.CoreActivity;
import com.start.navigation.R;

/**
 * 医院详细
 * @author start
 *
 */
public class HospitalDetailActivity extends CoreActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hospital_detail);
		setCurrentActivityTitle(R.string.activity_title_hospital_detail);
	}

}
