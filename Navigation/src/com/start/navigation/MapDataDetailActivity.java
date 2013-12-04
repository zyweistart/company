package com.start.navigation;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.service.tasks.DownloadTask;
import com.start.utils.NetConnectManager;
import com.start.utils.Utils;

/**
 * 导航
 * @author start
 *
 */
public class MapDataDetailActivity extends CoreActivity implements OnClickListener {

	private String fileno=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_detail);
		setCurrentActivityTitle(R.string.activity_title_map_data_detail);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			fileno=bundle.getString(MapDataListActivity.FILENO);
			TextView tv=(TextView)findViewById(R.id.activity_map_data_description_txt);
			tv.setText("地图数据描述内容");
			File dataFile=Utils.getFile(this, fileno);
			if(dataFile.exists()){
				Button btnDownload=(Button)findViewById(R.id.activity_map_data_download);
				Button btnUse=(Button)findViewById(R.id.activity_map_data_use);
				btnDownload.setVisibility(View.GONE);
				btnUse.setVisibility(View.VISIBLE);
			}
		}else{
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.activity_map_data_download){
			if(NetConnectManager.isMobilenetwork(this)){
				new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(R.string.msg_use_mobile_data)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						new DownloadTask(MapDataDetailActivity.this,fileno).execute();
					}
				}).show();
			}else{
				new DownloadTask(MapDataDetailActivity.this,fileno).execute();
			}
		}else if(v.getId()==R.id.activity_map_data_use){
			getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.CURRENTDATAFILENO, fileno);
			makeTextLong(R.string.msg_switching_datafile);
		}
	}

}