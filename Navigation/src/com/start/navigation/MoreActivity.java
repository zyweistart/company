package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.start.core.CoreActivity;

/**
 * 设置
 * 
 * @author start
 * 
 */
public class MoreActivity extends CoreActivity implements OnClickListener {

	
	private Button btnLogin;
	private Button btnLogout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		btnLogin=(Button)findViewById(R.id.more_btn_login);
		btnLogout=(Button)findViewById(R.id.more_btn_logout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getAppContext().isLogin()){
			btnLogin.setVisibility(View.GONE);
			btnLogout.setVisibility(View.VISIBLE);
		}else{
			btnLogin.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.more_btn_login) {
			//用户登录
			startActivity(new Intent(this,LoginActivity.class));
		} else if(v.getId() == R.id.more_btn_logout){
			//用户退出
			
		} else if (v.getId() == R.id.more_btn_friends_manager) {
			//好友管理
			startActivity(new Intent(this,FriendRelationListActivity.class));
		} else if (v.getId() == R.id.more_btn_map_manager) {
			//地图管理
			startActivity(new Intent(this,MapDataListActivity.class));
		} else if (v.getId() == R.id.more_btn_version_check) {
			//版本检测
			makeTextShort(R.string.msg_last_version);
		}
	}

}
