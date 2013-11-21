package com.ancun.yulualiyun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.utils.CommonFn;
import com.ancun.utils.LogUtils;
import com.ancun.utils.NetConnectManager;
import com.yunos.boot.SellerServiceHandler;
import com.yunos.seller.SellerAuthority;

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
				startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
				finish();
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
	public void onAnimationEnd(Animation animation) {
		boolean SP_ALIYUN_INIT_SET=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,false);
		if(!SP_ALIYUN_INIT_SET){
			if(NetConnectManager.isNetWorkAvailable(WelcomeActivity.this)){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						//是否满足卖家手机条件
						if(getAppContext().getYunOSAPI().getSystemType()==SellerAuthority.ERROR_NETWORK_NOT_AVAILABLE){
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									//网络不可用
									AlertDialog.Builder aDialog = new AlertDialog.Builder(WelcomeActivity.this);
									aDialog.setCancelable(false).
									setIcon(android.R.drawable.ic_dialog_info).
									setMessage("当前网络不可用，请稍候再试").
									setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											finish();
										}
									}).show();
								}
							});
							return;
						}else if(getAppContext().getYunOSAPI().getSystemType()==SellerAuthority.NORMAL_PHONE){
							//普通手机
							getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
							forward();
							return;
						}else if(getAppContext().getYunOSAPI().getSystemType()==SellerAuthority.SELLER_PHONE){
							//卖家手机
							//是否已经使用淘宝卖家账号登录
							if(!getAppContext().getYunOSAPI().isSystemLogin()){
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										new AlertDialog.Builder(WelcomeActivity.this)
										.setIcon(android.R.drawable.ic_dialog_info)
										.setCancelable(false)
										.setMessage("如您为淘宝卖家，则您购买的服务套餐中可能包含该应用的赠送服务，请使用淘宝卖家账号登录云OS后重新登录安存语录以确认，以免影响您正常获赠该应用相关服务。")
										.setPositiveButton("现在登录云OS ", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												Intent netIntent=new Intent("com.yunos.account.action.LOGIN");
												startActivity(netIntent);
												finish();
											}
										}).setNegativeButton("暂不登录，了解应用先", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												
												getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
												forward();
											}
										}).show();
									}
								});
								return;
							}
							
							Bundle bundle=getAppContext().getYunOSAPI().isValidServiceStatus();
							int keyCoe=bundle.getInt(SellerServiceHandler.KEY_CODE);
							if (keyCoe== SellerServiceHandler.CODE_SUCCESS) {
								int keyResult=bundle.getInt(SellerServiceHandler.KEY_RESULT);
								if(keyResult==SellerServiceHandler.RESULT_ACTIVE){
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											//已经赠送
											CommonFn.buildDialog(WelcomeActivity.this, "您购买的服务套餐中所包含该应用服务已激活，请确认本机号码同注册号码相同 并登录", new OnClickListener(){

												@Override
												public void onClick(DialogInterface dialog,
														int which) {
													
													//有服务已赠送则下次不再进行检测
													getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
													
													forward();
												}
												
											}).show();
										}
									});
									return;
								}else if(keyResult==SellerServiceHandler.RESULT_INACTIVE){
									//未赠送
									Intent intent=new Intent(WelcomeActivity.this,ActivationAccountActivity.class);
									startActivityForResult(intent,REQUEST_CODE_WELCOME);
									return;
								}else if(keyResult==SellerServiceHandler.RESULT_EXPIRED||
										keyResult==SellerServiceHandler.RESULT_NO_SERVICE){
									//服务不存在或已过期
									getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
									forward();
									return;
								}else{
									//或其它异常
									forward();
									return;
								}
							}else{
								//系统异常：500，未登陆：-101
								//淘宝账户异常：601，卖家账户等级过低：602
								forward();
								return;
							}
						}else{
							forward();
							return;
						}
					}
				}).start();
			}else{
				//自为防止初始设置时网络未开启而导致激活失效所以首次启动应用如果没有网络则必须设置网络
				new AlertDialog.Builder(WelcomeActivity.this).
				setCancelable(false).
				setIcon(android.R.drawable.ic_dialog_info).
				setMessage("当前无法连接到网络，是否立即进行设置？").
				setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
						startActivity(netIntent);
						finish();
					}
				}).show();
			}
		}else{
			forward();
		}
	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
}