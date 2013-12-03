package com.start.navigation;

import java.io.File;
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
import com.start.navigation.MapDataListActivity.MapDataPullListAdapter.MapDataViewHolder;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.service.tasks.ImportDataFileTask;
import com.start.utils.CommonFn;
import com.start.utils.Utils;

/**
 * 地图数据列表
 * @author start
 *
 */
public class MapDataListActivity extends CoreActivity implements OnClickListener{

	private PullListViewData mapDataPullListData;
	
	public static final String FILENO="fileno";
	
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
					new MapDataPullListAdapter(mapDataPullListData));
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
				holder.download = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_download);
				holder.download.setOnClickListener(MapDataListActivity.this);
				holder.use = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_use);
				holder.use.setOnClickListener(MapDataListActivity.this);
				holder.detail = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_detail);
				holder.detail.setOnClickListener(MapDataListActivity.this);
				convertView.setTag(holder);
			}
			Map<String,String> data=mapDataPullListData.getDataItemList().get(position);
			holder.fileno=data.get(FILENO);
			holder.name.setText(data.get(FILENO));
			File dataFile=Utils.getFile(MapDataListActivity.this, holder.fileno);
			if(dataFile.exists()){
				holder.download.setVisibility(View.GONE);
				holder.use.setVisibility(View.VISIBLE);
			}else{
				holder.download.setVisibility(View.VISIBLE);
				holder.use.setVisibility(View.GONE);
			}
			holder.download.setTag(holder);
			holder.use.setTag(holder);
			holder.detail.setTag(holder);
			return convertView;
		}
		
		public class MapDataViewHolder {
			String fileno;
			TextView name;
			Button download;
			Button use;
			Button detail;
		}
		
	}

	@Override
	public void onClick(View v) {
		final MapDataViewHolder vh=(MapDataViewHolder)v.getTag();
		if(vh==null)return;
		if(v.getId()==R.id.lvitem_mapdata_btn_download){
//			if(NetConnectManager.isMobilenetwork(this)){
//				new AlertDialog.Builder(this)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setMessage(R.string.msg_use_mobile_data)
//				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.dismiss();
//					}
//				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						new DownloadTask(MapDataListActivity.this).execute(vh.fileno);
//					}
//				}).show();
//			}else{
//				new DownloadTask(MapDataListActivity.this).execute(vh.fileno);
//			}
//			new DecompressTask(this,"a586054a207abc9fe4fe4945e5c666dc").execute();
			new ImportDataFileTask(this,"a586054a207abc9fe4fe4945e5c666dc").execute();
		}else if(v.getId()==R.id.lvitem_mapdata_btn_use){
			//使用当前数据
			getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.CURRENTDATAFILENO, vh.fileno);
			makeTextLong(R.string.msg_switching_datafile);
		}else if(v.getId()==R.id.lvitem_mapdata_btn_detail){
			Bundle bundle=new Bundle();
			bundle.putString(FILENO, vh.fileno);
			Intent intent=new Intent(this,MapDataDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
}