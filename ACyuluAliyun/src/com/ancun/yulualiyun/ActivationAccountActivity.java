package com.ancun.yulualiyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ancun.core.CoreActivity;

public class ActivationAccountActivity extends CoreActivity{

	private static final int REQUEST_CODE_ACTIVATION=222;
	public static final int RESULT_CODE_REGISTER=222;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activationaccount);
		
		setNavigationTitle("激活赠送服务");
		
		Button button=(Button)findViewById(R.id.btn_activation_register);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle bundle=new Bundle();
				bundle.putBoolean(RegisterActivity.ACTIVATION_FLAG, true);
				Intent intent=new Intent(ActivationAccountActivity.this,RegisterActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent,REQUEST_CODE_ACTIVATION);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE_ACTIVATION){
			if(resultCode==RESULT_CODE_REGISTER){
				setResult(WelcomeActivity.RESULT_CODE_REGISTER);
				finish();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		//屏蔽返回按钮
	}
	
}
