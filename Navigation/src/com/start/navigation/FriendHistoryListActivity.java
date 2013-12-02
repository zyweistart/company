package com.start.navigation;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.FriendHistory;

/**
 * 好友历史列表
 * @author start
 *
 */
public class FriendHistoryListActivity extends CoreActivity {

	private ListView mFriendHistoryList;
	private List<FriendHistory> dataItemList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_history_list);
		
		dataItemList=getAppContext().getFriendHistoryService().findAllByMyId("");
		
		mFriendHistoryList=(ListView)findViewById(R.id.activity_friend_history_list);
		mFriendHistoryList.setAdapter(new FriendHistoryAdapter());
	}
	
	public class FriendHistoryAdapter extends  BaseAdapter{
		
		@Override
		public int getCount() {
			return dataItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return  dataItemList.get(position);
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
				holder.btnLocation = (Button) convertView.findViewById(R.id.lvitem_friend_location);
				holder.btnLocation.setVisibility(View.GONE);
				
				holder.btnRemove = (Button) convertView.findViewById(R.id.lvitem_friend_remove);
				holder.btnRemove.setVisibility(View.GONE);
				
				holder.btnAuthorize = (Button) convertView.findViewById(R.id.lvitem_friend_authorize);
				holder.btnAuthorize.setVisibility(View.VISIBLE);
				holder.btnAuthorize.setTag(holder);
				holder.btnAuthorize.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FriendHistoryViewHolder vh=(FriendHistoryViewHolder)v.getTag();
						if(vh!=null){
							
						}
					}
					
				});
				
				convertView.setTag(holder);
			}
			FriendHistory fh=dataItemList.get(position);
			holder.name.setText("好友:"+fh.getFriendId());
			return convertView;
		}
		
		public class FriendHistoryViewHolder {
			TextView name;
			Button btnLocation;
			Button btnRemove;
			Button btnAuthorize;
		}
	}

}