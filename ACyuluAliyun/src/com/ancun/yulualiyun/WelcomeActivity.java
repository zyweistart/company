package com.ancun.yulualiyun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.utils.LogUtils;

/**
 * 欢迎界面
 * @author Start
 */
public class WelcomeActivity extends CoreActivity implements AnimationListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		if(!getAppContext().getYunOSAPI().isMjPhone()){
			//TODO：不是阿里云环境处理
			makeTextLong("当前环境不是卖家手机，无法运行");
			finish();
		}
		
		if(!getAppContext().getYunOSAPI().isSystemLogin()){
			//TODO:淘宝卖家账号未进行登录时处理
			makeTextLong("还未使用淘宝账号进行登录");
		}
		
		ImageView imageView = (ImageView)findViewById(R.id.activity_welcome_logo);
		Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.activity_welcome_alpha);
		//启动Fill保持
		alphaAnimation.setFillEnabled(true);
		//设置动画的最后一帧是保持在View上面
		alphaAnimation.setFillAfter(true);
		imageView.setAnimation(alphaAnimation);
		//为动画设置监听
		alphaAnimation.setAnimationListener(this);
 	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		 try {
        	//getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(),0);
			int curVersionCode=packInfo.versionCode;
			boolean FIRST_LOADAPP=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_FIRST_LOADAPP,true);
			if(curVersionCode>getAppContext().getSharedPreferencesUtils().getInteger(Constant.SharedPreferencesConstant.SP_CURRENTVERSIONCODE,0)){
				FIRST_LOADAPP=true;
			}
			//更新当前版本号
			getAppContext().getSharedPreferencesUtils().putInteger(Constant.SharedPreferencesConstant.SP_CURRENTVERSIONCODE, curVersionCode);
			if(FIRST_LOADAPP){
				getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_FIRST_LOADAPP,false);
				//如果为首次运行应用则进入引导页
				new AlertDialog.Builder(this)
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("是否在桌面创建快捷方式")
				.setPositiveButton("不，谢谢", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
						finish();
					}
				}).setNegativeButton("创建", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					    //为程序创建桌面快捷方式
					    //同时需要在manifest中设置以下权限：
					    //<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
						Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
						// 快捷方式的名称
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
						// 不允许重复创建
						shortcut.putExtra("duplicate", false);
						Intent respondIntent = new Intent(WelcomeActivity.this, WelcomeActivity.this.getClass());
						respondIntent.setAction(WelcomeActivity.this.getPackageName() + "." + WelcomeActivity.this.getLocalClassName());
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, respondIntent);
						// 快捷方式的图标
						ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(WelcomeActivity.this, R.drawable.ic_launcher);
						shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
						sendBroadcast(shortcut);
						dialog.dismiss();
						startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
						finish();
					}
				}).show();
			}else{
				Intent intent=new Intent(this,LoginActivity.class);
				if(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_AUTOLOGIN, false)){
					Bundle bundle=new Bundle();
					bundle.putBoolean(Constant.BUNLE_AUTOLOGINFLAG, true);
					intent.putExtras(bundle);
				}
				startActivity(intent);
				this.finish();
			}
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	
	@Override
	public void onBackPressed() {
		//屏蔽返回按钮
//		super.onBackPressed();
	}
	
}