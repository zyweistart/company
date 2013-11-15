package com.ancun.yulualiyun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ancun.core.CoreActivity;

/**
 * 账户激活赠送
 * @author start
 *
 */
public class ActivationAccountActivity extends CoreActivity{

	private static final int REQUEST_CODE_ACTIVATION=222;
	public static final int RESULT_CODE_REGISTER=222;
	
	private LinearLayout ll1;
	private LinearLayout ll2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activationaccount);
		
		setNavigationTitle("激活赠送服务");
		
		
		ll1=(LinearLayout)findViewById(R.id.btn_activation_ll_register1);
		ll2=(LinearLayout)findViewById(R.id.btn_activation_ll_register2);
		
		ImageButton button1=(ImageButton)findViewById(R.id.btn_activation_register1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ll1.setVisibility(View.GONE);
				ll2.setVisibility(View.VISIBLE);
				
			}
		});
		
		ImageButton button2=(ImageButton)findViewById(R.id.btn_activation_register2);
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle bundle=new Bundle();
				bundle.putBoolean(RegisterActivity.ACTIVATION_FLAG, true);
				Intent intent=new Intent(ActivationAccountActivity.this,RegisterActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent,REQUEST_CODE_ACTIVATION);
			}
		});
		ImageButton button3=(ImageButton)findViewById(R.id.btn_activation_register3);
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ActivationAccountActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("亲，当您下次需激活安存语录赠送服务时，可在使用淘宝卖家账号登录云OS后启动安存语录按照提示操作~")
				.setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(WelcomeActivity.RESULT_CODE_REGISTER);
						finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
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
		setResult(WelcomeActivity.RESULT_CODE_REGISTER);
		super.onBackPressed();
	}
	
}
