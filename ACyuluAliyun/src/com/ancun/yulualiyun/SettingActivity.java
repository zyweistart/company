package com.ancun.yulualiyun;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.LogUtils;
/**
 * 设置
 * @author Start
 */
public class SettingActivity extends CoreActivity implements OnClickListener {

	/**
	 * 系统拨号盘 提示录音     开关状态
	 */
	private boolean isSettingSystemDialSwitchOpen=true;
	/**
	 * 陌生号码录音提示录音   开关状态
	 */
	private boolean isSettingSystemDialStangerSwitchOpen=true;


	/**
	 * 系统拨号盘 提示录音开关    按钮
	 */
	private ImageButton btnSettingSystemDialSwitch=null;
	/**
	 * 陌生号码录音提示    按钮
	 */
	private ImageButton btnSettingSystemDialStangerSwitch=null;
	/**
	 * 系统拨号盘 提示录音开关   设置
	 */
	private LinearLayout llSettingSystemDialSwitch=null;
	/**
	 * 陌生号码录音提示开关   设置
	 */
	private LinearLayout llSettingSystemDialStangerSwitch=null;
	/**
	 * 小贴士
	 */
	private LinearLayout llSettingHelp=null;
	/**
	 * 意见反馈
	 */
	private LinearLayout llSettingFeedback=null;
	/**
	 * 检测新版本
	 */
	private LinearLayout llSettingNewVersion=null;
	/**
	 * 关于我们
	 */
	private LinearLayout llSettingAboutUs=null;
	/**
	 * 清理本地缓存
	 */
	private LinearLayout llSettingCleanRecordFiles=null;
	/**
	 * 修改密码
	 */
	private LinearLayout llSettingChangePassword=null;
	/**
	 * 重新登录
	 */
	private LinearLayout llSettingLoginAgain=null;
	/**
	 * 退出
	 */
	private LinearLayout llSettingQuit=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setNavigationTitle(R.string.setting_title);
		llSettingSystemDialSwitch=(LinearLayout) findViewById(R.id.setting_system_dial_switch);
		llSettingSystemDialStangerSwitch=(LinearLayout) findViewById(R.id.setting_system_dial_stanger_switch);
		llSettingHelp=(LinearLayout) findViewById(R.id.setting_help);
		llSettingFeedback=(LinearLayout) findViewById(R.id.setting_feedback);
		llSettingNewVersion=(LinearLayout) findViewById(R.id.setting_new_version);
		llSettingAboutUs=(LinearLayout) findViewById(R.id.setting_about_us);
		llSettingCleanRecordFiles=(LinearLayout) findViewById(R.id.setting_clean_record_files);
		llSettingChangePassword=(LinearLayout) findViewById(R.id.setting_change_password);
		llSettingLoginAgain=(LinearLayout) findViewById(R.id.setting_login_again);
		llSettingQuit=(LinearLayout) findViewById(R.id.setting_quit);	
		btnSettingSystemDialSwitch=(ImageButton) findViewById(R.id.setting_system_dial_switch_btn);
		btnSettingSystemDialStangerSwitch=(ImageButton) findViewById(R.id.setting_system_dial_stanger_switch_btn);
		btnSettingSystemDialSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//开关系统拨号盘拨号    录音提示功能
				switchSystemDial();
			}
		});
		btnSettingSystemDialStangerSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//开关系统拨号盘   陌生人录音提示功能
				switchSystemDialStanger();
			}
		});

		isSettingSystemDialSwitchOpen=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_START_EXTERNAL_DIAL,true);
		isSettingSystemDialStangerSwitchOpen=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_UNFAMILIAR_EXTERNAL_DIAL,true);
		setSwitchStatusSystemDial(btnSettingSystemDialSwitch, isSettingSystemDialSwitchOpen);
		setSwitchStatusStangerDial( btnSettingSystemDialStangerSwitch ,isSettingSystemDialSwitchOpen, isSettingSystemDialStangerSwitchOpen);

	}
	/**
	 * 开关系统拨号盘拨号    录音提示功能
	 */
	public void switchSystemDial() {
		isSettingSystemDialSwitchOpen=!isSettingSystemDialSwitchOpen;
		setSwitchStatusSystemDial(btnSettingSystemDialSwitch,isSettingSystemDialSwitchOpen);
		setSwitchStatusStangerDial( btnSettingSystemDialStangerSwitch ,isSettingSystemDialSwitchOpen, isSettingSystemDialStangerSwitchOpen);
		getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_START_EXTERNAL_DIAL,isSettingSystemDialSwitchOpen);

	}
	/**
	 * 开关系统拨号盘   陌生人录音提示功能
	 */
	public void switchSystemDialStanger() {
		if (isSettingSystemDialSwitchOpen) {
			isSettingSystemDialStangerSwitchOpen=!isSettingSystemDialStangerSwitchOpen;
			setSwitchStatusStangerDial( btnSettingSystemDialStangerSwitch ,isSettingSystemDialSwitchOpen, isSettingSystemDialStangerSwitchOpen);
			getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_UNFAMILIAR_EXTERNAL_DIAL,isSettingSystemDialStangerSwitchOpen);
		}else {
			makeTextLong("请先启用系统拨号盘");
		}

	}

	/**
	 * 设置系统拨号盘拨号  开关状态按钮
	 * @param btnSwitch
	 * @param isSystemDialOpen
	 */
	public void setSwitchStatusSystemDial(ImageButton btnSwitch,boolean isSystemDialOpen){
		if (isSystemDialOpen) {
			btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_on_bg);
		}else {
			btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_off_bg);
		}
	}
	/**
	 * 陌生人拨号提醒开关  状态设置 
	 * @param btnSwitch
	 * @param isSystemDialOpen
	 * @param isStangerDialOpen
	 */
	public void setSwitchStatusStangerDial(ImageButton btnSwitch,boolean isSystemDialOpen,boolean isStangerDialOpen) {
		if (isSystemDialOpen) {
			if (isStangerDialOpen) {
				btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_on_bg);
			}else {
				btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_off_bg);
			}
		}else {
			if (isStangerDialOpen) {
				btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_on_dark_bg);
			}else {
				btnSwitch.setBackgroundResource(R.drawable.setting_checkbox_switch_off_bg);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == llSettingSystemDialSwitch) {
			//开关系统拨号盘拨号    录音提示功能
			switchSystemDial();
		} else if (v == llSettingSystemDialStangerSwitch) {
			//开关系统拨号盘   陌生人录音提示功能
			switchSystemDialStanger();
		} else if (v == llSettingHelp) {
			startActivity(new Intent(this, TipActivity.class)); 
		} else if (v == llSettingFeedback) {
			startActivity(new Intent(this, FeedbackActivity.class)); 
		}else if (v == llSettingNewVersion) {
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("type","6");
			requestParams.put("termtype","7");
			Map<String,String> headerParams=new HashMap<String,String>();
			headerParams.put("sign","");
			getAppContext().exeNetRequest(this,Constant.GlobalURL.versioninfoGet,requestParams,headerParams,new UIRunnable() {
				
				@Override
				public void run() {
					Integer maxVersion=Integer.parseInt(getInfoContent().get("maxverno"));
					Integer minVersion=Integer.parseInt(getInfoContent().get("minverno"));
					final String url=getInfoContent().get("url");
					int currentVersionCode=getAppContext().getSharedPreferencesUtils().getInteger(Constant.SharedPreferencesConstant.SP_CURRENTVERSIONCODE,0);
					if(minVersion>currentVersionCode){
						handler.post(new Runnable() {
							@Override
							public void run() {
								new AlertDialog.Builder(SettingActivity.this)
								.setTitle("提示!")
								.setMessage("您使用的版本过低，请立即升级！否则可能无法正常使用。")
								.setCancelable(false)
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
										startActivity(fIntent);
										finish();
									}
								}).show();
							}
						});
					}else if(maxVersion>currentVersionCode){
						handler.post(new Runnable() {
							@Override
							public void run() {
								new AlertDialog.Builder(SettingActivity.this)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setTitle("提示！")
								.setMessage("检测到新版本，是否升级？")
								.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										new DownloadAppTask().execute(url);
									}
								}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}).show();
							}
						});
					}else{
						makeTextLong("已经是最新版本了");
					}
				}
				
			});
		}else if (v == llSettingAboutUs) {
			startActivity(new Intent(SettingActivity.this, AboutUsActivity.class)); 
		}else if (v == llSettingCleanRecordFiles) {//清理录音
			handler.post(new Runnable() {
				@Override
				public void run() {
					new AlertDialog.Builder(SettingActivity.this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示！")
					.setMessage("是否删除本地所有录音缓存文件？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							final ProgressDialog statusDialog =CommonFn.progressDialog(SettingActivity.this,"正在清理本地录音...请稍候");
							statusDialog.show();
							try{
								File dirFile=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/");
								if(dirFile.exists()&&dirFile.isDirectory()){
									File recordFiles[] = dirFile.listFiles(); //声明目录下所有的文件 files[];
									for (int i = 0; i < recordFiles.length; i++) {//遍历目录下所有的文件
										recordFiles[i].delete();//删除
									}
								}
								makeTextLong("成功清理本地所有录音文件");
							}catch (Exception e) {
								LogUtils.logError(e);
								makeTextLong("清理本地录音文件失败");
							}finally{
								statusDialog.dismiss();
							}
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					}).show();
				}
			});
		}else if (v == llSettingChangePassword) {
			startActivity(new Intent(this, ChangePasswordActivity.class)); 
		}else if (v == llSettingLoginAgain) {
			setResult(Constant.SETTING_RELOGIN);
			finish();
		}else if (v == llSettingQuit) {
			setResult(Constant.SETTING_QUIT);
			finish();
		}
	}
	
}