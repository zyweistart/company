package com.ancun.yulualiyun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;


public class NetCheckReceiver extends BroadcastReceiver{

	/**
	 * 网络检测标记
	 */
	public static Boolean NetCheckFlag=true;
	
    //android 中网络变化时所发的Intent的名字
    private static final String NETACTION="android.net.conn.CONNECTIVITY_CHANGE";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if(NETACTION.equals(intent.getAction())){
        	//Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
            if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)){
            	//网络断开
            	NetCheckReceiver.NetCheckFlag = false;
            }else{
            	//网络没有断开
            	NetCheckReceiver.NetCheckFlag = true;
            }
        }
    }
}