package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.start.core.CoreActivity;

/**
 * 设置
 * 
 * @author start
 * 
 */
public class MoreActivity extends CoreActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.more_btn_login) {
			//用户登录
			startActivity(new Intent(this,LoginActivity.class));
		} else if (v.getId() == R.id.more_btn_friends_manager) {
			//好友管理
			if(getAppContext().isLogin()){
				startActivity(new Intent(this,FriendRelationListActivity.class));
			}else{
				makeTextShort(R.string.msg_not_login);
			}
		} else if (v.getId() == R.id.more_btn_map_manager) {
			//地图管理
			if(getAppContext().isLogin()){
				startActivity(new Intent(this,MapDataListActivity.class));
			}else{
				makeTextShort(R.string.msg_not_login);
			}
		} else if (v.getId() == R.id.more_btn_version_check) {
			//版本检测
			makeTextShort(R.string.msg_last_version);
		}
	}

}
