package com.start.navigation;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Department;
import com.start.model.Doctor;

/**
 * 医生列表
 * @author start
 *
 */
public class DoctorListActivity extends CoreActivity  implements OnItemClickListener{

	
	private List<Doctor> doctors;
	
	private ListView departmentListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_list);

		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String departmentId=bundle.getString(Department.COLUMN_NAME_ID);
			if(departmentId!=null){
				doctors=getAppContext().getDoctorService().findByDepartmentId(departmentId);
			}else{
				doctors=getAppContext().getDoctorService().findAll();
			}
		}else{
			doctors=getAppContext().getDoctorService().findAll();
		}
		
		DataAdapter adapter=new DataAdapter();
		departmentListView=(ListView)findViewById(R.id.module_main_frame_doctor_list);
		departmentListView.setAdapter(adapter);
		departmentListView.setOnItemClickListener(this);
		
	}
	
	private class DataAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return doctors.size();
		}

		@Override
		public Object getItem(int position) {
			return doctors.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_department_content) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(DoctorListActivity.this).inflate(R.layout.lvitem_department, null);
				holder=new ViewHolder();
				holder.tvName=(TextView)convertView.findViewById(R.id.lvitem_department_name);
				
				convertView.setTag(holder);
			}
			
			Doctor doctor=doctors.get(position);
			holder.doctor=doctor;
			holder.tvName.setText(doctor.getName());
			return convertView;
		}
		
	}

	private class ViewHolder{
		
		private Doctor doctor;
		
		private TextView tvName;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ViewHolder vh=(ViewHolder)arg1.getTag();
		if(vh!=null){
			Bundle bundle=new Bundle();
			bundle.putString(Doctor._ID, vh.doctor.getId());
			Intent intent=new Intent(this,DoctorDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

}
