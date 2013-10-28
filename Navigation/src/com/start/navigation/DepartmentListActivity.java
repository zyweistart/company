package com.start.navigation;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.service.DepartmentService;

/**
 * 科室列表
 * @author start
 *
 */
public class DepartmentListActivity extends CoreActivity {

	
	private List<Department> departments;
	private DepartmentService departmentService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_list);
		departmentService=new DepartmentService(this);
		
		departments=departmentService.findAll();
		
	}
	
	private class RecordedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(DepartmentListActivity.this).inflate(null, null);
				holder=new ViewHolder();
				convertView.setTag(holder);
			}
			return convertView;
		}
		
		private class ViewHolder{
			
		}
		
	}

}
