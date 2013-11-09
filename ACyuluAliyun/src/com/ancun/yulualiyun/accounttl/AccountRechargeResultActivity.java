package com.ancun.yulualiyun.accounttl;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ancun.core.CoreActivity;
import com.ancun.yulualiyun.R;

public class AccountRechargeResultActivity extends CoreActivity {
	
	private Button account_recharge_result_submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_recharge_result);
 
		setNavigationTitle(R.string.accountrecharge_title);
		
		account_recharge_result_submit=(Button)findViewById(R.id.account_recharge_result_submit);
		account_recharge_result_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

}
