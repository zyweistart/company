//package com.start.navigation;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.start.model.medmap.Department;
//import com.start.model.medmap.DepartmentStore;
//import com.start.model.medmap.Doctor;
//
//public class DoctorActivity extends Activity implements OnItemClickListener {
//
//	private ListView listView;
//	private Department department;
//	private int departmentIndex;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_doctor);
//		listView = (ListView) findViewById(R.id.list);
//		departmentIndex = getIntent().getIntExtra("position", -1);
//		if(departmentIndex > -1){
//			department = DepartmentStore.getInstance(this).getAllDepartments()[departmentIndex];
//		}
//		
//		listView.setAdapter(new ArrayAdapter<Doctor>(this, R.layout.row_doctor,
//				R.id.doctor, department.getDoctors()));
//		
//		listView.setOnItemClickListener(this);
//		
//		
//	}
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		startActivityForResult(new Intent(this, DoctorDetailsActivity.class).putExtra("departmentIndex", departmentIndex).putExtra("doctorIndex", position), 0);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK){
//			finish();
//		}
//	}
//}
