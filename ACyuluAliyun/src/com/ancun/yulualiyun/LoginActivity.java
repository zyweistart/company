package com.ancun.yulualiyun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.LogUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.StringUtils;

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
			final String account=et_activity_login_account.getText().toString();
			final String password=et_activity_login_password.getText().toString();
			if(account==null||"".equals(account)){
				makeTextShort("账号输入错误");
			}else if(password==null||"".equals(password)){
				makeTextShort("密码输入错误");
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
							//TODO:个人用户暂无法登录
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
			startActivity(new Intent(this,RegisterActivity.class));
		}else if(v==btn_activity_login_forgetpwd){
			//忘记密码
			startActivity(new Intent(this,ForgetpwdActivity.class));
		}
	}
	
}