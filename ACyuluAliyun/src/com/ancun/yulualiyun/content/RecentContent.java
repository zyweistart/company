package com.ancun.yulualiyun.content;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.core.CoreScrollContent;
import com.ancun.model.ContactModel;
import com.ancun.model.RecentModel;
import com.ancun.utils.CommonFn;
import com.ancun.utils.TimeUtils;
import com.ancun.yulualiyun.R;

/**
 * 最近通话记录
 * @author Start
 */
public class RecentContent extends CoreScrollContent implements OnClickListener {

	private ListView listview;

	private DataAdapter adapter;

	private List<RecentModel> mListDataItems = new ArrayList<RecentModel>();

	public RecentContent(Activity activity, int resourceID) {
		super(activity, resourceID);
		listview = (ListView) findViewById(R.id.recent_contacts_listview);
	}

	public void loadData(final Boolean flag){
		// 加载通话记录
		final ProgressDialog dialog = CommonFn.progressDialog(getContext(),"加载中...");
		dialog.show();
		new Thread() {
			public void run() {
				try{
					mListDataItems = getMainActivity().getRecentService().findCallRecords();
					getActivity().runOnUiThread(new Runnable() {
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
				convertView = getLayoutInflater().inflate(R.layout.lvitem_content_recent, null);
				holder.head = (ImageView) convertView.findViewById(R.id.recentcontact_photo);
				holder.call_in_out_flag = (ImageView) convertView.findViewById(R.id.recentcontact_in_call);
				holder.name = (TextView) convertView.findViewById(R.id.recentcontact_name_text);
				holder.phone = (TextView) convertView.findViewById(R.id.recentcontact_phone_text);
				holder.from = (TextView) convertView.findViewById(R.id.recentcontact_time);
				holder.call = (ImageButton) convertView.findViewById(R.id.recentcontact_call);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			RecentModel recent = mListDataItems.get(position);
			ContactModel contactModel = getMainActivity().getContactService().getContactModelByPhone(recent.getPhone());
			if (contactModel != null) {
//				holder.name.setText(contactModel.getName());
//				holder.recentName=contactModel.getName();
				if (contactModel.getPhotoID() > 0) {
					holder.head.setImageBitmap(getMainActivity().getContactService().loadContactPhoto(contactModel.getId()));
				} else {
					holder.head.setImageResource(R.drawable.contact_head);
				}
			} else {
//				holder.name.setText("未知号码");
//				holder.recentName=recent.getPhone();
				holder.head.setImageResource(R.drawable.contact_head);
			}
			if(recent.getName()!=null&&!"".equals(recent.getName())){
				holder.name.setText(recent.getName());
				holder.phone.setVisibility(View.VISIBLE);
			} else {
				holder.name.setText(recent.getPhone());
				holder.phone.setVisibility(View.GONE);
			}
			
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
			holder.phone.setText(recent.getPhone());
			holder.from.setText(TimeUtils.customerTimeConvert(recent.getCalltime()));
			holder.call.setTag(holder);
			holder.call.setOnClickListener(RecentContent.this);
			return convertView;
		}
	}
	
	private class HolderView {
		private ImageView head;
		private ImageView call_in_out_flag;
		private TextView name;
		private TextView phone;
		private TextView from;
		private ImageButton call;
	}

	@Override
	public void onClick(View v) {
		HolderView vh = (HolderView) v.getTag();
		if (vh != null) {
			getMainActivity().inAppDial(String.valueOf(vh.phone.getText()));
		}
	}

}