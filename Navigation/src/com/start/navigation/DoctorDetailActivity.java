package com.start.navigation;

import android.os.Bundle;
import android.util.Log;

import com.start.core.CoreActivity;
import com.start.model.Doctor;
import com.start.service.DoctorService;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity {

	private Doctor doctor;
	private DoctorService doctorService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		doctorService=new DoctorService(this);
		
		String doctorId=getIntent().getExtras().getString(Doctor.COLUMN_NAME_ID);
		doctor=doctorService.findById(doctorId);
		
		Log.v(TAG,"doctorId:"+doctor.getId()+"---"+doctor.getName());
	}

}
