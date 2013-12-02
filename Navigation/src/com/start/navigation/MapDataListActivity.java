package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.navigation.MapDataListActivity.MapDataPullListAdapter.MapDataViewHolder;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnItemClickListener;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.utils.CommonFn;

/**
 * 地图数据列表
 * @author start
 *
 */
public class MapDataListActivity extends CoreActivity {

	private PullListViewData mapDataPullListData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_list);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!getAppContext().isLogin()){
			
			CommonFn.buildDialog(this, R.string.msg_not_login, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(MapDataListActivity.this,LoginActivity.class));
				}
				
			}).show();
			
		}else{
			mapDataPullListData=new PullListViewData(this);
			mapDataPullListData.setOnLoadDataListener(
					new OnLoadDataListener(){

						@Override
						public void LoadData(LoadMode loadMode) {
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("accessid",Constant.ACCESSID);
							mapDataPullListData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4recQry,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									mapDataPullListData.getAdapter().notifyDataSetChanged();
								} 
							},"reclist","reclist"+TAG);
						}
						
					});
			mapDataPullListData.start(R.id.activity_map_data_pulllistview, 
					new MapDataPullListAdapter(mapDataPullListData),
					new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
							MapDataViewHolder vh=(MapDataViewHolder)view.getTag();
							if(vh!=null){
								getAppContext().makeTextLong(""+ vh.data);
							}
						}
						
					});
		}
	}
	
	public class MapDataPullListAdapter extends PullListViewData.DataAdapter{
		
		public MapDataPullListAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapDataViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_mapdata_content) {
				holder = (MapDataViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_mapdata, null);
				holder = new MapDataViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_mapdata_name);
				convertView.setTag(holder);
			}
			holder.data=mapDataPullListData.getDataItemList().get(position);
			holder.name.setText("好友:"+holder.data.get("oppno"));
			return convertView;
		}
		
		public class MapDataViewHolder {
			Map<String,String> data;
			TextView name;
		}
		
	}

}
