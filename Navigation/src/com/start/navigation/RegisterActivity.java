package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;

/**
 * 注册
 * @author start
 *
 */
public class RegisterActivity extends CoreActivity implements OnClickListener {

	private EditText etUserName;
	private EditText etPassword;
	private EditText etRePassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setCurrentActivityTitle(R.string.activity_register);
		
		etUserName=(EditText)findViewById(R.id.register_et_username);
		etPassword=(EditText)findViewById(R.id.register_et_new_password);
		etRePassword=(EditText)findViewById(R.id.register_et_re_new_password);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.register_btn_send){
			String userName=String.valueOf(etUserName.getText());
			String password=String.valueOf(etPassword.getText());
			String rePassword=String.valueOf(etRePassword.getText());
			if(TextUtils.isEmpty(userName)){
				makeTextLong("用户名不能为空");
			}else if(TextUtils.isEmpty(password)){
				makeTextLong("密码不能为空");
			}else if(!password.equals(rePassword)){
				makeTextLong("两次密码输入不一致");
			}else{
				
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("signupsource","h");
				requestParams.put("ip", "");
				requestParams.put("mac", "");
				requestParams.put("loginflag", "1");
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", "");
				getHttpService().exeNetRequest(Constant.ServerAPI.nRegister,requestParams,headerParams,new UIRunnable() {
					
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
		}
	}

}
