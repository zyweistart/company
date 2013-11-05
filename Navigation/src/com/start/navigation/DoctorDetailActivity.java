package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.model.Doctor;

/**
 * 医生详细
 * @author start
 *
 */
public class DoctorDetailActivity extends CoreActivity {

	private Doctor doctor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		
		String doctorId=getIntent().getExtras().getString(Doctor.COLUMN_NAME_ID);
		doctor=getAppContext().getDoctorService().findById(doctorId);
		
		
		Button btnDepartment=(Button)findViewById(R.id.doctor_detail_btnDepartment);
		btnDepartment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString(Department.COLUMN_NAME_ID, doctor.getDepartmentId());
				Intent intent=new Intent(DoctorDetailActivity.this,DepartmentDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
		
		TextView tvDetailContent=(TextView)findViewById(R.id.doctor_detail_content);
		tvDetailContent.setText("doctorId:"+doctor.getId()+"---"+doctor.getName());
	}

}
