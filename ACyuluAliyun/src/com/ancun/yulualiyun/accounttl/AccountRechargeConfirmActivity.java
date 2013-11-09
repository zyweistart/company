package com.ancun.yulualiyun.accounttl;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ancun.alipay.MobileSecurePayHelper;
import com.ancun.alipay.MobileSecurePayer;
import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.yulualiyun.R;

public class AccountRechargeConfirmActivity extends CoreActivity implements OnClickListener {

	//支付宝支付消息
	public static final int ALIPAY_MSG=0xAC0001;
	
	private String recordno;
	private Integer money;
	
	private TextView tvRechargeAccount;
	private TextView tvRechargeAmount;
	private TextView tvRechargeNowAccount;
	private TextView tvRechargeOriginalAccount;
	private TextView tvRechargeConfirmInfo;
	private TextView tvRechargeConfirmPay;
	
	private Button btnRechargeSubmit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_recharge_confirm);
		setNavigationTitle(R.string.accountrecharge_title);
		
		recordno=getIntent().getExtras().getString(AccountRechargeActivity.DATAMAPITEM_RECORDNO);
		money=getIntent().getExtras().getInt(AccountRechargeActivity.DATAMAPITEM_FEE);
		
		tvRechargeAccount=(TextView)findViewById(R.id.account_recharge_confirm_account);
		tvRechargeAmount=(TextView)findViewById(R.id.account_recharge_confirm_amount);
		
		String rectime=getAppContext().getUserInfo().get("rectime");
		tvRechargeOriginalAccount=(TextView)findViewById(R.id.account_recharge_confirm_original_account);
		tvRechargeOriginalAccount.setText(String.valueOf((Integer.parseInt(rectime)/60)));
		tvRechargeNowAccount=(TextView)findViewById(R.id.account_recharge_confirm_now_account);
		tvRechargeNowAccount.setText(String.valueOf((Integer.parseInt(rectime)/60+Integer.parseInt(getIntent().getExtras().getString(AccountRechargeActivity.DATAMAPITEM_AMOUNT)))));

		tvRechargeConfirmInfo=(TextView)findViewById(R.id.account_recharge_confirm_info);
		tvRechargeConfirmPay=(TextView)findViewById(R.id.account_recharge_confirm_pay);
		
		tvRechargeAmount.setText(String.valueOf(money));
		
		btnRechargeSubmit=(Button)findViewById(R.id.account_recharge_confirm_submit);
		btnRechargeSubmit.setOnClickListener(this);
		
		tvRechargeAccount.setText(String.valueOf(getAppContext().getUserInfo().get("phone")));
	}

	@Override
	public void onClick(View v) {
		tvRechargeConfirmInfo.setBackgroundResource(R.drawable.account_recharge_title_bg_before);
		tvRechargeConfirmPay.setBackgroundResource(R.drawable.account_recharge_title_bg);
		tvRechargeConfirmPay.setTextColor(this.getResources().getColor(R.color.white));
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(AccountRechargeConfirmActivity.this);
		if (!mspHelper.detectMobile_sp()){
			return;
		}
		//请求参数
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		requestParams.put("recprod",recordno);
		getAppContext().exeNetRequest(this,Constant.GlobalURL.v4alipayReq,requestParams,null,new UIRunnable() {
			@Override
			public void run() {
				String reqContent=getInfoContent().get("reqcontent");
				MobileSecurePayer msp = new MobileSecurePayer();
				msp.pay(reqContent, handler,ALIPAY_MSG, AccountRechargeConfirmActivity.this);
			}
			
		});
	}
	
	@Override
	public void processMessage(Message msg) {
		switch(msg.what){
			case ALIPAY_MSG:
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid", Constant.ACCESSID_LOCAL);
				requestParams.put("payproduct", "3");
				requestParams.put("rescontent", String.valueOf(msg.obj));
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign",Constant.ACCESSKEY_LOCAL);
				getAppContext().exeNetRequest(this,Constant.GlobalURL.v4ealipayRes,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						startActivity(new Intent(AccountRechargeConfirmActivity.this,AccountRechargeResultActivity.class));
						finish();
					}
					
				});
				break;
		}
	}

	public static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		private Activity mcontext;
		
		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}
		
		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}
	
}
