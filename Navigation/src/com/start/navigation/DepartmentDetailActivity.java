package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;

/**
 * 科室详细
 * @author start
 *
 */
public class DepartmentDetailActivity extends CoreActivity implements OnClickListener {

	private Department department;
	
	private TextView mModuleMainHeaderContentTitle;
	private Button mModuleMainHeaderContentLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_detail);
		
		initHeaderView();
		
		String departmentId=getIntent().getExtras().getString(Department.COLUMN_NAME_ID);
		department=getAppContext().getDepartmentService().findById(departmentId);
		
		
		Button btnDoctorList=(Button)findViewById(R.id.department_detail_btndoctorlist);
		btnDoctorList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString(Department.COLUMN_NAME_ID, department.getId());
				Intent intent=new Intent(DepartmentDetailActivity.this,DoctorListActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		TextView tvDetailContent=(TextView)findViewById(R.id.department_detail_content);
		tvDetailContent.setText("departmentId:"+department.getId()+"---"+department.getName());
		
	}

	/**
	 * 初始化头部视图
	 */
	private void initHeaderView() {
		mModuleMainHeaderContentTitle = (TextView) findViewById(R.id.module_main_header_content_title);
		mModuleMainHeaderContentTitle.setText("部门详细");
		mModuleMainHeaderContentLocation = (Button) findViewById(R.id.module_main_header_content_location);
		mModuleMainHeaderContentLocation.setOnClickListener(this);
		mModuleMainHeaderContentLocation.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		Intent data=new Intent();
		Bundle bundle=new Bundle();
		bundle.putString(Department.COLUMN_NAME_ID, department.getId());
		data.putExtras(bundle);
		setResult(MainActivity.RESULTCODE_LOCATION_DEPARTMENT,data);
		finish();
	}
	
}