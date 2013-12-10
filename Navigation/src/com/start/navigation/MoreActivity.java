package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;

/**
 * 设置
 * 
 * @author start
 * 
 */
public class MoreActivity extends CoreActivity implements OnClickListener {

	
	private LinearLayout llLogin;
	private LinearLayout llLogout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		setCurrentActivityTitle(R.string.activity_title_more);
		
		llLogin=(LinearLayout)findViewById(R.id.more_login);
		llLogout=(LinearLayout)findViewById(R.id.more_logout);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getAppContext().isLogin()){
			llLogin.setVisibility(View.GONE);
			llLogout.setVisibility(View.VISIBLE);
		}else{
			llLogin.setVisibility(View.VISIBLE);
			llLogout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.more_login) {
			//用户登录
			startActivity(new Intent(this,LoginActivity.class));
		} else if (v.getId() == R.id.more_logout) {
			//用户退出
			new AlertDialog.Builder(MoreActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_you_are_sure_logout)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Boolean isChecked=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false);
					if(isChecked){
						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferences.LOGIN_AUTOLOGIN, false);
						getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_ACCOUNT, Constant.EMPTYSTR);
						getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferences.LOGIN_PASSWORD, Constant.EMPTYSTR);
					}
					getAppContext().setUserInfo(null);
					llLogin.setVisibility(View.VISIBLE);
					llLogout.setVisibility(View.GONE);
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).show();
			
		} else if (v.getId() == R.id.more_friend_relation_manager) {
			//好友管理
			startActivity(new Intent(this,FriendRelationListActivity.class));
		} else if (v.getId() == R.id.more_data_file_manager) {
			//地图管理
			startActivity(new Intent(this,MapDataListActivity.class));
		} else if (v.getId() == R.id.more_new_version_check) {
			//版本检测
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("type","6");
			requestParams.put("termtype","7");
			Map<String,String> headerParams=new HashMap<String,String>();
			headerParams.put("sign","");
			getHttpService().exeNetRequest(Constant.ServerAPI.nVersionCheck,requestParams,headerParams,new UIRunnable() {
				
				@Override
				public void run() {
					Map<String,String> data=getContent().get("versioninfo");
					final Integer maxVersion=Integer.parseInt(data.get("maxverno"));
					final Integer minVersion=Integer.parseInt(data.get("minverno"));
					final String url=data.get("url");
					final int currentVersionCode=getAppContext().getSharedPreferencesUtils().getInteger(Constant.SharedPreferences.SP_CURRENTVERSIONCODE,0);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(minVersion>currentVersionCode){
								new AlertDialog.Builder(MoreActivity.this)
								.setMessage(R.string.msg_have_new_version_1)
								.setCancelable(false)
								.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
										startActivity(fIntent);
										setResult(Constant.ActivityResultCode.EXITAPP);
										finish();
									}
								}).show();
							}else if(maxVersion>currentVersionCode){
								new AlertDialog.Builder(MoreActivity.this)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setMessage(R.string.msg_have_new_version_2)
								.setPositiveButton(R.string.msg_upgrade_now, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
//										new DownloadAppTask().execute(url);
									}
								}).setNegativeButton(R.string.msg_later, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}).show();
							}else{
								makeTextLong(R.string.msg_last_version);
							}
						}
						
					});
				}
			});
		}else if(v.getId()==R.id.more_modifypwd){
			startActivity(new Intent(this,ModifyPwdActivity.class));
		}else if(v.getId()==R.id.more_btn_exit_app){
			
			new AlertDialog.Builder(MoreActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(R.string.msg_exit_app)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setResult(Constant.ActivityResultCode.EXITAPP);
					finish();
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).show();
			
		}
	}

}
