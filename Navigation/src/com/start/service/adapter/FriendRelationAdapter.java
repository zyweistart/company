package com.start.service.adapter;

import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.navigation.R;
import com.start.service.PullListViewData;

public class FriendRelationAdapter extends PullListViewData.DataAdapter{
	
	private CoreActivity mActivity;
	private PullListViewData mPullListViewData;
	
	public FriendRelationAdapter(CoreActivity activity,PullListViewData pullListViewData) {
		pullListViewData.super();
		this.mActivity=activity;
		this.mPullListViewData=pullListViewData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendRelationViewHolder holder;
		if (convertView != null && convertView.getId() == R.id.lvitem_friend_content) {
			holder = (FriendRelationViewHolder) convertView.getTag();
		}else{
			convertView = mActivity.getLayoutInflater().inflate(R.layout.lvitem_friend, null);
			holder = new FriendRelationViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.lvitem_friend_name);
			holder.btnRemove = (Button) convertView.findViewById(R.id.lvitem_friend_remove);
			holder.btnRemove.setTag(holder);
			holder.btnRemove.setVisibility(View.VISIBLE);
			holder.btnRemove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FriendRelationViewHolder vh=(FriendRelationViewHolder)v.getTag();
					if(vh!=null){
						mActivity.makeTextLong("解除关系");
					}
				}
				
			});
			convertView.setTag(holder);
		}
		holder.data=mPullListViewData.getDataItemList().get(position);
		holder.name.setText("好友:"+holder.data.get("oppno"));
		return convertView;
	}
	
	public class FriendRelationViewHolder {
		Map<String,String> data;
		TextView name;
		Button btnRemove;
	}
	
}