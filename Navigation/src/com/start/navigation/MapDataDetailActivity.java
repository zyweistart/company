package com.start.navigation;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;

/**
 * 导航
 * @author start
 *
 */
public class MapDataDetailActivity extends CoreActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_detail);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String fileno=bundle.getString(MapDataListActivity.FILENO);
			TextView tv=(TextView)findViewById(R.id.activity_map_data_description_txt);
			tv.setText("地图数据描述内容");
			File downFile=new File(new File(Environment.getExternalStorageDirectory().getPath()+Constant.DATADIRFILE),fileno);
			if(downFile.exists()){
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
			makeTextLong("下载");
		}else if(v.getId()==R.id.activity_map_data_use){
			makeTextLong("使用");
		}
	}

}