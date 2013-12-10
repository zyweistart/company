package com.ancun.yulualiyun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ancun.core.AliYunOSAPI;
import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.LogUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.StringUtils;
import com.yunos.boot.SellerServiceHandler;
import com.yunos.seller.SellerAuthority;

public class LoginActivity extends CoreActivity implements OnClickListener {
	
	/**
	 * 账号,密码
	 */
	private EditText et_activity_login_account,et_activity_login_password;
	
	/**
	 * 自动登录
	 */
	private CheckBox cb_activity_login_autologin;
	
	/**
	 * 登录按钮,注册按钮,忘记密码按钮
	 */
	private Button btn_activity_login,btn_activity_login_regsiter,btn_activity_login_forgetpwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_activity_login_account=(EditText)findViewById(R.id.et_activity_login_account);
		et_activity_login_password=(EditText)findViewById(R.id.et_activity_login_password);
		cb_activity_login_autologin=(CheckBox)findViewById(R.id.cb_activity_login_autologin);
		//登录
		btn_activity_login=(Button)findViewById(R.id.btn_activity_login);
		btn_activity_login.setOnClickListener(this);
		//注册
		btn_activity_login_regsiter=(Button)findViewById(R.id.btn_activity_login_regsiter);
		btn_activity_login_regsiter.setOnClickListener(this);
		//忘记密码
		btn_activity_login_forgetpwd=(Button)findViewById(R.id.btn_activity_login_forgetpwd);
		btn_activity_login_forgetpwd.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundle=getIntent().getExtras();
		if(bundle==null){
			et_activity_login_account.setText(CommonFn.getPhoneNumber(this));
		}else{
			String phone=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_ACCOUNT,Constant.EMPTYSTR);
			et_activity_login_account.setText(phone);
			if(bundle.getBoolean(Constant.BUNLE_AUTOLOGINFLAG,true)){
				cb_activity_login_autologin.setChecked(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_AUTOLOGIN, false));
				if(!Constant.EMPTYSTR.equals(phone)){
					String password =  getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
					if(!Constant.EMPTYSTR.equals(password)){
						try {
							et_activity_login_password.setText(StringUtils.doKeyDecrypt(password,getAssets().open(Constant.DESKEYKEY)));
							btn_activity_login.performClick();
						} catch (IOException e) {
							LogUtils.logError(e);
						}
					}
				}
			}else{
				makeTextLong(bundle.getString(Constant.BUNLE_EXIT_MSG));
			}
		}
	}

	@Override
	public void onClick(View v) {
		//关闭软键盘
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.hideSoftInputFromWindow(et_activity_login_account.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et_activity_login_password.getWindowToken(), 0);
		if(v  == btn_activity_login){
			getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
			final String account=String.valueOf(et_activity_login_account.getText());
			final String password=String.valueOf(et_activity_login_password.getText());
			if(TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
				makeTextShort("账号或密码不能为空");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("username", account);
				requestParams.put("loginsource", "h");
				requestParams.put("ip", "");
				requestParams.put("mac", "");
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", MD5.md5(password));
				getAppContext().exeNetRequest(this,Constant.GlobalURL.v4Login,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						try{
							getAppContext().setUserInfoAll(getAllInfoContent());
							getAppContext().setUserInfo(getAllInfoContent().get("v4info"));
							if("1".equals(getAppContext().getUserInfo().get("usertype"))){
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										new AlertDialog.Builder(LoginActivity.this)
										.setIcon(android.R.drawable.ic_dialog_info)
										.setMessage("仅适用于企业及机构手机用户")
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												
											}
										}).show();
									}
									
								});
							}else{
								getAppContext().buildAuth(getResponseContent());
								getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_ACCOUNT,account);
								boolean autoLogin=cb_activity_login_autologin.isChecked();
								//判断是否为自动登录，自动登录则保存密码
								if(autoLogin){
									getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,StringUtils.doKeyEncrypt(password,getAssets().open(Constant.DESKEYKEY)));
								}else{
									getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
								}
								getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_AUTOLOGIN,autoLogin);
								Constant.ACCESSID=getAppContext().getUserInfo().get("accessid");
								Constant.ACCESSKEY=getAppContext().getUserInfo().get("accesskey");
								
								//进入主界面
								Bundle bundle=new Bundle();
								bundle.putString("password", password);
								Intent intent=new Intent(LoginActivity.this,MainActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
								finish();
							}
						}catch(Exception e){
							LogUtils.logError(e);
						}
					}
					
				});
			}
		}else if(v==btn_activity_login_regsiter){
			//注册界面
			new AlertDialog.Builder(this).
			setIcon(android.R.drawable.ic_dialog_info).
			setMessage("如您为淘宝卖家且所购卖家手机套餐中包含该应用服务，请点击“偶是掌柜” 激活开通套餐所含安存语录服务；\n非淘宝卖家或所购卖家手机套餐中不含该应用服务，点击“普通注册”开通现在也可获赠月度免费体验服务~\n特别提醒掌柜们：同一手机号码仅可激活一种类型赠送服务，如选择普通注册则可能导致卖家手机套餐中所含该应用服务无法正常获取~").
			setPositiveButton("偶是掌柜，激活开通", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new AlertDialog.Builder(LoginActivity.this).
					setIcon(android.R.drawable.ic_dialog_info).
					setMessage("亲，激活卖家手机套餐所含安存语录服务前，需确认已使用购买套餐时的淘宝卖家ID成功登录云OS，恭请掌柜的先行确认下何如？").
					setPositiveButton("查看云OS当前登陆账号及状态", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent netIntent=new Intent("com.yunos.account.action.LOGIN");
							startActivity(netIntent);
						}
					}).setNegativeButton("确认OK，激活服务，赶紧的", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new Thread(new Runnable() {
							
								@Override
								public void run() {
									
									int systemType=getAppContext().getYunOSAPI().getSystemType();
									if(systemType==SellerAuthority.SELLER_PHONE){
										if(getAppContext().getYunOSAPI().isSellerAcccountLogin()){
											Bundle bundle=getAppContext().getYunOSAPI().isValidServiceStatus();
											int keyCoe=bundle.getInt(SellerServiceHandler.KEY_CODE);
											Log.v(AliYunOSAPI.TAG,"isValidServiceStatus KEY_CODE:"+keyCoe);
											if (keyCoe== SellerServiceHandler.CODE_SUCCESS) {
												int keyResult=bundle.getInt(SellerServiceHandler.KEY_RESULT);
												Log.v(AliYunOSAPI.TAG,"isValidServiceStatus KEY_RESULT:"+keyResult);
												if(keyResult==SellerServiceHandler.RESULT_INACTIVE){
													Intent intent=new Intent(LoginActivity.this,ActivationAccountActivity.class);
													startActivityForResult(intent,WelcomeActivity.REQUEST_CODE_WELCOME);
													return;
												}
											}
										}
									}
									
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											
											new AlertDialog.Builder(LoginActivity.this).
											setIcon(android.R.drawable.ic_dialog_info).
											setMessage("当前淘宝ID所含安存语录服务已被激活过或暂时无法激活，请关闭应用稍后再试，或确认下当前登录云OS的淘宝ID是否为购买卖家手机套餐时所用的卖家账号，不便之处还请见谅啦").
											setPositiveButton("确定", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
												}
											}).show();
											
										}
									});
									
								}
							}).start();
						}
					}).show();
				}
			}).setNegativeButton("普通注册", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(intent);
				}
			}).show();
		}else if(v==btn_activity_login_forgetpwd){
			//忘记密码
			startActivity(new Intent(this,ForgetpwdActivity.class));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==WelcomeActivity.REQUEST_CODE_WELCOME){
			if(resultCode==WelcomeActivity.RESULT_CODE_REGISTER){
				String phone=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_ACCOUNT,Constant.EMPTYSTR);
				et_activity_login_account.setText(phone);
				cb_activity_login_autologin.setChecked(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_AUTOLOGIN, false));
				if(!Constant.EMPTYSTR.equals(phone)){
					String password =  getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
					if(!Constant.EMPTYSTR.equals(password)){
						try {
							et_activity_login_password.setText(StringUtils.doKeyDecrypt(password,getAssets().open(Constant.DESKEYKEY)));
							btn_activity_login.performClick();
						} catch (IOException e) {
							LogUtils.logError(e);
						}
					}
				}
			}
		}
	}
	
}