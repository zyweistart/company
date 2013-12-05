package com.start.navigation;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Doctor;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity implements OnClickListener{

	private Doctor doctor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		String doctorId=getIntent().getExtras().getString(Doctor.COLUMN_NAME_ID);
		doctor=getAppContext().getDoctorService().findById(doctorId);
		if(doctor!=null){
			setCurrentActivityTitle(doctor.getName());
			TextView tv=(TextView)findViewById(R.id.activity_doctor_detail_content);
			tv.setText(doctor.getIntroduction());
		}else{
			finish();
		}

	}

	@Override
	public void onClick(View v) {
//		Bundle bundle=new Bundle();
//		bundle.putString(Department.COLUMN_NAME_ID, doctor.getDepartmentId());
//		Intent intent=new Intent(DoctorDetailActivity.this,DepartmentDetailActivity.class);
//		intent.putExtras(bundle);
//		startActivity(intent);
	}

}
