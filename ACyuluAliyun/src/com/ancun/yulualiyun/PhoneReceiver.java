package com.ancun.yulualiyun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ancun.core.Constant;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.SharedPreferencesUtils;

/**
 * 通话广播
 * @author Start
 */
public class PhoneReceiver extends BroadcastReceiver {

	private final static String NEW_OUTGOING_CALL="android.intent.action.NEW_OUTGOING_CALL";
	
	private SharedPreferencesUtils preferences;
	
	@Override
	public void onReceive(final Context context,Intent intent) {
		if(intent.getAction().equals(PhoneReceiver.NEW_OUTGOING_CALL)){
			//拔打的号码
			String oppo=intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
			if(Constant.noCall.contains(oppo)){
				setResultData(oppo);
			}else{
				preferences=new SharedPreferencesUtils(context);
				if(preferences.getBoolean(Constant.SharedPreferencesConstant.SP_START_EXTERNAL_DIAL,true)){
					//本地的号码，必须登陆过一次语录系统，且只能本地号码进行登陆
					String phone=preferences.getString(Constant.SharedPreferencesConstant.SP_ACCOUNT,Constant.EMPTYSTR);
					Boolean isAllow=preferences.getBoolean(Constant.SharedPreferencesConstant.SP_CALL_ISALLOW, true);
					if(!Constant.EMPTYSTR.equals(phone)&&isAllow){
						String serverno=preferences.getString(Constant.SharedPreferencesConstant.SP_SERVER_CALL, Constant.EMPTYSTR);
						if(Constant.EMPTYSTR.equals(serverno)){
							if(!NetConnectManager.isNetWorkAvailable(context)){
								if(preferences!=null){
									preferences.putBoolean(Constant.SharedPreferencesConstant.SP_CALL_ISALLOW, true);
								}
								setResultData(oppo);
								return;
							}
						}
						Bundle bundle=new Bundle();
						//拔打的电话
						bundle.putString("oppo", oppo);
						//本地记录的号码
						bundle.putString("phone", phone);
						Intent newIntent=new Intent(context,DialActivity.class);
						newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						newIntent.putExtras(bundle);
						context.startActivity(newIntent);
						setResultData(null);
					}else{
						if(preferences!=null){
							preferences.putBoolean(Constant.SharedPreferencesConstant.SP_CALL_ISALLOW, true);
						}
						setResultData(oppo);
					}
				}else{
					setResultData(oppo);
				}
			}
		}
	}
	
}