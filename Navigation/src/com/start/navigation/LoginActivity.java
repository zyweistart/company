package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.utils.MD5;

/**
 * 登录
 * @author start
 *
 */
public class LoginActivity extends CoreActivity implements  OnClickListener {

	private EditText etName;
	private EditText etPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		etName=(EditText)findViewById(R.id.login_account_name);
		etPassword=(EditText)findViewById(R.id.login_account_password);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.login_btn_account){
			String username=String.valueOf(etName.getText());
			String password=String.valueOf(etPassword.getText());
			if(TextUtils.isEmpty(username)){
				makeTextShort("账号不能为空");
			}else if(TextUtils.isEmpty(password)){
				makeTextShort("密码不能为空");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("username", username);
				requestParams.put("loginsource", "h");
				requestParams.put("ip", "");
				requestParams.put("mac", "");
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", MD5.md5(password));
				getHttpService().exeNetRequest(Constant.ServerAPI.nLogin,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						getAppContext().setUserInfo(getContent());
						Map<String,String> data=getAppContext().getUserInfoByKey("v4info");
						Constant.ACCESSID=data.get("accessid");
						Constant.ACCESSKEY=data.get("accesskey");
						finish();
					}
					
				});
			}
		}else if(v.getId()==R.id.login_btn_register){
			startActivity(new Intent(this,RegisterActivity.class));
		}else if(v.getId()==R.id.login_btn_forgetpwd){
			startActivity(new Intent(this,ForgetPwdActivity.class));
		}
	}

}