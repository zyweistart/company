package com.start.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.FriendHistory;
import com.start.model.UIRunnable;
import com.start.utils.CommonFn;

/**
 * 好友历史列表
 * @author start
 *
 */
public class FriendHistoryListActivity extends CoreActivity {

	private ListView mFriendHistoryList;
	private FriendHistoryAdapter mFriendHistoryAdapter;
	private List<FriendHistory> mDataItemList=new ArrayList<FriendHistory>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_history_list);
		setCurrentActivityTitle(R.string.activity_title_friend_history_list);
		
		mFriendHistoryAdapter=new FriendHistoryAdapter();
		mFriendHistoryList=(ListView)findViewById(R.id.activity_friend_history_list);
		mFriendHistoryList.setAdapter(mFriendHistoryAdapter);
		
		//加载数据
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				mDataItemList=getAppContext().getFriendHistoryService().findAllByMyId(getAppContext().getMyID());
				return !mDataItemList.isEmpty();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					mFriendHistoryAdapter.notifyDataSetChanged();
				}
				super.onPostExecute(result);
			}
			
		}.execute();
		
	}
	
	
	
	public class FriendHistoryAdapter extends  BaseAdapter{
		
		@Override
		public int getCount() {
			return mDataItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return  mDataItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FriendHistoryViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_friend_content) {
				holder = (FriendHistoryViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_friend, null);
				holder = new FriendHistoryViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_friend_name);
				holder.btnAuthorize = (ImageButton) convertView.findViewById(R.id.lvitem_friend_authorize);
				holder.btnAuthorize.setVisibility(View.VISIBLE);
				holder.btnAuthorize.setTag(holder);
				holder.btnAuthorize.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FriendHistoryViewHolder vh=(FriendHistoryViewHolder)v.getTag();
						if(vh!=null){
							CommonFn.alertsDialog(FriendHistoryListActivity.this, "确定要对该好友开放自己的位置信息吗?", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Map<String,String> requestParams=new HashMap<String,String>();
									Map<String,String> headerParams=new HashMap<String,String>();
									headerParams.put("sign", "");
									getHttpService().exeNetRequest(Constant.ServerAPI.nOpenLocation,requestParams,headerParams,new UIRunnable() {
										
										@Override
										public void run() {
											makeTextLong("放开位置成功");
										}
										
									});
								}
								
							}).show();
							
						}
					}
					
				});
				
				convertView.setTag(holder);
			}
			FriendHistory fh=mDataItemList.get(position);
			holder.name.setText(fh.getFriendId());
			return convertView;
		}
		
		public class FriendHistoryViewHolder {
			TextView name;
			ImageButton btnAuthorize;
		}
		
	}

}