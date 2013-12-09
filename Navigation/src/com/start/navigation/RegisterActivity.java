package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.utils.CommonFn;

/**
 * 注册
 * @author start
 *
 */
public class RegisterActivity extends CoreActivity implements OnClickListener {

	private EditText etUserName;
	private EditText etCode;
	private EditText etPassword;
	private EditText etRePassword;
	private LinearLayout registerMainFramework;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setCurrentActivityTitle(R.string.activity_register);
		
		etUserName=(EditText)findViewById(R.id.register_et_username);
		etCode=(EditText)findViewById(R.id.register_et_code);
		etPassword=(EditText)findViewById(R.id.register_et_new_password);
		etRePassword=(EditText)findViewById(R.id.register_et_re_new_password);
		registerMainFramework=(LinearLayout)findViewById(R.id.register_ll_main_framework);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.register_btn_send){
			String userName=String.valueOf(etUserName.getText());
			String code=String.valueOf(etCode.getText());
			String password=String.valueOf(etPassword.getText());
			String rePassword=String.valueOf(etRePassword.getText());
			if(TextUtils.isEmpty(userName)){
				makeTextLong("用户名不能为空");
			}else if(TextUtils.isEmpty(code)){
				makeTextLong("验证码不能为空");
			}else if(TextUtils.isEmpty(password)){
				makeTextLong("密码不能为空");
			}else if(!password.equals(rePassword)){
				makeTextLong("两次密码输入不一致");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid",Constant.ACCESSID_LOCAL);
				requestParams.put("email", userName);
				requestParams.put("pwd", password);
				requestParams.put("authcode", code);
				requestParams.put("loginflag", "1");
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", Constant.ACCESSKEY_LOCAL);
				getHttpService().exeNetRequest(Constant.ServerAPI.userReg,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
							}
						});
					}
				});
			}
		}else if(v.getId()==R.id.register_btn_send_code){
			String userName=String.valueOf(etUserName.getText());
			if(TextUtils.isEmpty(userName)){
				makeTextLong("用户名不能为空");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid",Constant.ACCESSID_LOCAL);
				requestParams.put("email", userName);
				requestParams.put("type", "1");
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", Constant.ACCESSKEY_LOCAL);
				getHttpService().exeNetRequest(Constant.ServerAPI.uacodeGet,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								CommonFn.alertsDialog(RegisterActivity.this, "验证码已发送，请查看你的邮箱！", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog, int which) {
										if(registerMainFramework.getVisibility()==View.GONE){
											registerMainFramework.setVisibility(View.VISIBLE);
										}
									}
									
								}).show();
							}
						});
					}
				});
				
			}
		}
	}

}
