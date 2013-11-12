package com.ancun.yulualiyun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.ancun.utils.CommonFn;
import com.ancun.utils.LogUtils;

/**
 * 欢迎界面
 * @author Start
 */
public class WelcomeActivity extends CoreActivity implements AnimationListener {
	
	private static final int REQUEST_CODE_WELCOME=111;
	public static final int RESULT_CODE_REGISTER=111;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
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
	
	private void forward(){
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE_WELCOME){
			if(resultCode==RESULT_CODE_REGISTER){
				forward();
			}
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
//		boolean SP_ALIYUN_INIT_SET=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,false);
//		if(!SP_ALIYUN_INIT_SET){
			if(getAppContext().getYunOSAPI().isMjPhone()){
				if(getAppContext().getYunOSAPI().isSystemLogin()){
					if(getAppContext().getYunOSAPI().isActivation()){
						if(getAppContext().getYunOSAPI().isServiceUse()){
							CommonFn.buildDialog(this, "您购买的服务套餐中所包含该应用服务已激活，请确认本机号码同注册号码相同 并登录", new OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
									
									forward();
								}
								
							}).show();
							return;
						}
						if(getAppContext().getYunOSAPI().isValidService()){
							Intent intent=new Intent(this,ActivationAccountActivity.class);
							startActivityForResult(intent,REQUEST_CODE_WELCOME);
							return;
						}else{
							//进入正常自主注册开通流程，赠送阿里云手机专享体验服务（电商单门版）一个月
						}
					}else{
						
						CommonFn.buildDialog(this, "如果您购买的服务套餐含有该应用的服务,且尚未激活,请进入“设置关于本机—激活赠送服务”中进行激活", new OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,int which) {
								
								forward();
								
							}
							
						}).show();
						return;
						
					}
				}else{
					//进入正常自主注册开通流程，赠送阿里云手机专享体验服务（电商单门版）一个月
				}
			}else{
				if(getAppContext().getYunOSAPI().isAliYunPhone()){
					
					//TODO:引导用户进入正常自主注册开通流程，需确认的问题：默认开通账户类型？所赠体验服务?
					
				}else{
					//进入正常自主注册开通流程，赠送阿里云手机专享体验服务（电商单门版）一个月
				}
			}
//		}
		forward();
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