package com.start.navigation;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.Doctor;
import com.start.service.DoctorService;

/**
 * 医生列表
 * @author start
 *
 */
public class DoctorListActivity extends CoreActivity  implements OnItemClickListener{

	
	private List<Doctor> doctors;
	private DoctorService doctorService;
	
	private ListView departmentListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_list);
		
		doctorService=new DoctorService(this);
		
		doctors=doctorService.findAll();
		
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
			holder.tvName.setText(doctor.getName());
			return convertView;
		}
		
	}

	private class ViewHolder{
		
		private TextView tvName;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ViewHolder vh=(ViewHolder)arg1.getTag();
		Log.v(TAG,vh+"");
	}

}
