package com.ancun.yulualiyun;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.core.CoreActivity;
import com.ancun.model.RecentModel;
import com.ancun.service.RecentService;
import com.ancun.utils.CommonFn;
import com.ancun.utils.TimeUtils;
/**
 * 通话记录详细列表
 * @author Start
 */
public class RecentManagerContentListActivity extends CoreActivity implements  AdapterView.OnItemLongClickListener,OnClickListener {

	private ListView listview;

	private DataAdapter adapter;

	private List<RecentModel> mListDataItems = new ArrayList<RecentModel>();

	private String phone;
	
	private RecentService recentService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_manager_content_list);
		
		phone=getIntent().getExtras().getString("phone");
		
		setNavigationTitle("与 "+getIntent().getExtras().getString("name")+" 通话记录");
		
		recentService=new RecentService(this);
		
		listview = (ListView) findViewById(R.id.app_listview_pulltorefreshlistview);
		listview.setOnItemLongClickListener(this);
		loadData(true);
	}

	public void loadData(final Boolean flag){
		// 加载通话记录
		final ProgressDialog dialog = CommonFn.progressDialog(this,"最近联系人加载中......");
		dialog.show();
		new Thread() {
			public void run() {
				try{
					mListDataItems = recentService.findAllByPhone(phone);
					runOnUiThread(new Runnable() {
						public void run() {
							if (flag||adapter==null) {
								adapter = new DataAdapter();
								listview.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
						}
					});
				}finally{
					dialog.dismiss();
				}
				
			};
		}.start();
	}
	/**
	 * 记录适配器
	 * @author Start
	 */
	public class DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListDataItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mListDataItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			if (convertView == null) {
				holder = new HolderView();
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_recent_manager_content_list, null);
				holder.call_in_out_flag = (ImageView) convertView.findViewById(R.id.recentcontact_in_call);
				holder.from = (TextView) convertView.findViewById(R.id.recentcontact_time);
				holder.call = (ImageButton) convertView.findViewById(R.id.recentcontact_call);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			RecentModel recent = mListDataItems.get(position);
			if(recent.getStatus()==CallLog.Calls.INCOMING_TYPE){
				//呼入
				holder.call_in_out_flag.setImageResource(R.drawable.flag_call_in);
			}else if(recent.getStatus()==CallLog.Calls.OUTGOING_TYPE){
				//呼出
				holder.call_in_out_flag.setImageResource(R.drawable.flag_call_out);
			}else{
				//呼入未接通
				holder.call_in_out_flag.setImageResource(R.drawable.flag_call_in);
			}
			holder.recentId = recent.getRecent_id();
			holder.from.setText(TimeUtils.customerTimeConvert(recent.getCalltime()));
			holder.call.setTag(holder);
			holder.call.setOnClickListener(RecentManagerContentListActivity.this);
			return convertView;
		}
	}
	
	private class HolderView {
		private Integer recentId;
		private ImageView call_in_out_flag;
		private TextView from;
		private ImageButton call;
	}

	@Override
	public void onClick(View v) {
		HolderView vh = (HolderView) v.getTag();
		if (vh != null) {
			inAppDial(phone);
//			dialService.dial(phone);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		final HolderView vh = (HolderView) arg1.getTag();
		if (vh != null) {
			if(vh.recentId!=null){
				new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("提示！")
				.setMessage("确认删除该通话记录吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						recentService.delete(vh.recentId);
						// 重新加载最后通话记录
						loadData(false);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).show();
			}
		}
		return false;
	}

}