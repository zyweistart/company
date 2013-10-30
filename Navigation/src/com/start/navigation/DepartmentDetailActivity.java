package com.start.navigation;

import android.os.Bundle;
import android.util.Log;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.service.DepartmentService;

/**
 * 科室详细
 * @author start
 *
 */
public class DepartmentDetailActivity extends CoreActivity {

	private Department department;
	private DepartmentService departmentService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_detail);
		
		departmentService=new DepartmentService(this);
		
		String departmentId=getIntent().getExtras().getString(Department.COLUMN_NAME_ID);
		department=departmentService.findById(departmentId);
		
		Log.v(TAG,"departmentId:"+department.getId()+"---"+department.getName());
	}

}
