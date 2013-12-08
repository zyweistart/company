package com.start.service.adapter;

import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.start.navigation.MainActivity;
import com.start.navigation.R;
import com.start.service.PullListViewData;

public class FriendLocationAdapter extends PullListViewData.DataAdapter{
		
		private MainActivity mActivity;
		private PullListViewData mPullListViewData;
	
		public FriendLocationAdapter(MainActivity activity,PullListViewData pullListViewData) {
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
				holder.btnLocation = (ImageButton) convertView.findViewById(R.id.lvitem_friend_location);
				holder.btnLocation.setTag(holder);
				holder.btnLocation.setVisibility(View.VISIBLE);
				holder.btnLocation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FriendRelationViewHolder vh=(FriendRelationViewHolder)v.getTag();
						if(vh!=null){
							
							new AlertDialog.Builder(mActivity)
							.setIcon(android.R.drawable.ic_dialog_info)
							.setMessage("确定导航到当前好友的位置点吗?")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
//									Map<String,String> data=vh.data;
//									String mapId=data.get("mapId");
//									String latitude=data.get("latitude");
//									String longitude=data.get("longitude");
									mActivity.location("0101","0.0007071","0.0012444");
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.dismiss();
								}
							}).show();
							
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
			ImageButton btnLocation;
		}
		
	}