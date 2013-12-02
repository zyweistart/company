package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.utils.CommonFn;

/**
 * 好友关系列表
 * @author start
 *
 */
public class FriendRelationListActivity extends CoreActivity implements OnClickListener {

	private Button mModuleMainHeaderContentAdd;
	
	private PullListViewData friendRelationData;
	
	public static final int RESULT_REFRESH_CODE=222;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_relation_list);
		mModuleMainHeaderContentAdd = (Button) findViewById(R.id.module_main_header_content_add);
		mModuleMainHeaderContentAdd.setOnClickListener(this);
		mModuleMainHeaderContentAdd.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!getAppContext().isLogin()){
			
			CommonFn.buildDialog(this, R.string.msg_not_login, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(FriendRelationListActivity.this,LoginActivity.class));
				}
				
			}).show();
			
		}else{
			friendRelationData=new PullListViewData(this);
			friendRelationData.setOnLoadDataListener(
					new OnLoadDataListener(){

						@Override
						public void LoadData(LoadMode loadMode) {
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("accessid",Constant.ACCESSID);
							friendRelationData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4recQry,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									friendRelationData.getAdapter().notifyDataSetChanged();
								} 
							},"reclist","reclist"+TAG);
						}
						
					});
			friendRelationData.start(R.id.activity_friend_relation_pulllistview, 
					new FriendRelationAdapter(friendRelationData));
		}
	}
	
	public class FriendRelationAdapter extends PullListViewData.DataAdapter{
		
		public FriendRelationAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FriendRelationViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_friend_content) {
				holder = (FriendRelationViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_friend, null);
				holder = new FriendRelationViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_friend_name);
				holder.btnLocation = (Button) convertView.findViewById(R.id.lvitem_friend_location);
				holder.btnLocation.setVisibility(View.GONE);
				
				holder.btnRemove = (Button) convertView.findViewById(R.id.lvitem_friend_remove);
				holder.btnRemove.setTag(holder);
				holder.btnRemove.setVisibility(View.VISIBLE);
				holder.btnRemove.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FriendRelationViewHolder vh=(FriendRelationViewHolder)v.getTag();
						if(vh!=null){
							makeTextLong(vh.data+"");
						}
					}
					
				});
				
				holder.btnAuthorize = (Button) convertView.findViewById(R.id.lvitem_friend_authorize);
				holder.btnAuthorize.setVisibility(View.GONE);
				convertView.setTag(holder);
			}
			holder.data=friendRelationData.getDataItemList().get(position);
			holder.name.setText("好友:"+holder.data.get("oppno"));
			return convertView;
		}
		
		public class FriendRelationViewHolder {
			Map<String,String> data;
			TextView name;
			Button btnLocation;
			Button btnRemove;
			Button btnAuthorize;
		}
		
	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent(this,FriendRelationSetActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_REFRESH_CODE){
			friendRelationData.getPulllistview().clickRefresh();
		}
	}

}