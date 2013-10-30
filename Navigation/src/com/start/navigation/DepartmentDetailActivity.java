package com.start.navigation;

import android.os.Bundle;
import android.util.Log;

import com.start.core.CoreActivity;
import com.start.model.Department;

/**
 * 科室详细
 * @author start
 *
 */
public class DepartmentDetailActivity extends CoreActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_detail);
		
		String departmentId=getIntent().getExtras().getString(Department._ID);
		Log.v(TAG,"departmentId:"+departmentId);
	}

}
