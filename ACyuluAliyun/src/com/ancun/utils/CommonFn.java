package com.ancun.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class CommonFn {

	/**
	 * 获取本机号码
	 */
	public static String getPhoneNumber(Context context){ 
		//创建电话管理器
		TelephonyManager mTelephonyMgr;    
		//获取系统固定号码
		mTelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		//返回手机号码
		String phone=mTelephonyMgr.getLine1Number();
		if(phone==null){
			return "";
		}else if(phone.length()==11){
			return phone;
		}
		return "";
	}
	
	public static ProgressDialog progressDialog(Context context,String message){
		ProgressDialog dialog = new ProgressDialog(context);
		if(message!=null){
			dialog.setMessage(message);
		}
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}
	/**
	 * 弹出网络设置对话框
	 */
	public static void settingNetwork(final Context context){
		AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
		aDialog.
		setIcon(android.R.drawable.ic_dialog_info).
		setTitle("提示！").
		setMessage("当前无法连接到网络，是否立即进行设置？").
		setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setNeutralButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(netIntent);
			}
		}).show();
	}
	
	/**
	 * 检测网络连接
	 */
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public static AlertDialog buildDialog(Context context, String message, DialogInterface.OnClickListener positiveButtonListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("确认", positiveButtonListener);
		return builder.create();
	}
	
}