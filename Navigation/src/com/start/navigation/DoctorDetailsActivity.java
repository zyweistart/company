//package com.start.navigation;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.start.model.medmap.DepartmentStore;
//import com.start.model.medmap.Doctor;
//
//public class DoctorDetailsActivity extends Activity {
//	private Doctor doctor;
//	private int departmentIndex;
//	private int doctorIndex;
//	private boolean isFromMap;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_doctor_details);
//		doctor = (Doctor) getIntent().getSerializableExtra("doctor");
//		if(doctor == null){
//			isFromMap = false;
//			departmentIndex = getIntent().getIntExtra("departmentIndex", -1);
//			doctorIndex = getIntent().getIntExtra("doctorIndex", -1);
//			doctor = DepartmentStore.getInstance(this).getAllDepartments()[departmentIndex].getDoctors()[doctorIndex];
//		} else {
//			isFromMap = true;
//		}
//		((TextView)findViewById(R.id.doctor_name)).setText(doctor.getName());
//		((TextView)findViewById(R.id.intro)).setText(doctor.getIntroduction());
//	}
//	
//	public void toDepartment(View view){
//		if(isFromMap){
//			startActivityForResult(new Intent(this, DepartmentActivity.class).putExtra("isFromDoctor", true),0);
//		} else {
//			setResult(RESULT_OK);
//			finish();
//		}
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK){
//			setResult(RESULT_OK, data);
//			finish();
//		}
//	}
//}
