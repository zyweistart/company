//package com.start.navigation;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.start.model.medmap.Department;
//import com.start.model.medmap.DepartmentStore;
//
//public class DepartmentActivity extends Activity {
//
//	private Department department;
//	private int departmentIndex;
//	private boolean isFromMap;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_department);
//		
//		department = (Department) getIntent().getSerializableExtra("department");
//		if(department == null){
//			departmentIndex = getIntent().getIntExtra("position", -1);
//			department = DepartmentStore.getInstance(this).getAllDepartments()[departmentIndex];
//		}
//		((TextView)findViewById(R.id.department_name)).setText(department.getName());
//		((TextView)findViewById(R.id.department_details)).setText(department.getIntroduction());
//		isFromMap = getIntent().getBooleanExtra("isFromMap", false);
//	}
//
//	
//	public void doctors(View view){
//		startActivity(new Intent(this, DoctorActivity.class).putExtra("position", departmentIndex));
//	}
//	
//	public void showOnMap(View view){
//		if(isFromMap){
//			finish();
//		} else {
//			setResult(RESULT_OK, new Intent().putExtra("mapIndex", department.getMapIndex()));
//			finish();
//		}
//	}
//}
