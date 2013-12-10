package com.start.navigation.hospital;

import android.os.Bundle;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Doctor;
import com.start.navigation.R;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		String doctorId=getIntent().getExtras().getString(Doctor.COLUMN_NAME_ID);
		Doctor doctor=getAppContext().getDoctorService().findById(doctorId);
		if(doctor!=null){
			setCurrentActivityTitle(doctor.getName());
			TextView tv=(TextView)findViewById(R.id.activity_doctor_detail_content);
			tv.setText(doctor.getIntroduction());
		}else{
			finish();
		}

	}

}
