package com.start.navigation;

import android.os.Bundle;
import android.util.Log;

import com.start.core.CoreActivity;
import com.start.model.Doctor;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		
		String doctorId=getIntent().getExtras().getString(Doctor._ID);
		Log.v(TAG,"doctorId:"+doctorId);
	}

}
