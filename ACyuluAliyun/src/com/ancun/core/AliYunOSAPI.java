package com.ancun.core;

import android.os.Bundle;
import android.util.Log;

import com.ancun.yulualiyun.AppContext;
import com.yunos.boot.SellerServiceHandler;
import com.yunos.seller.SellerAuthority;

/**
 * 阿里云接口API
 * 注：必须在非UI主线程当中调用
 * @author start
 *
 */
public class AliYunOSAPI {
	
	public static final String TAG="AliYunOSAPI";

	private static final String APPNAME="com.ancun.yulualiyun";
	
	private AppContext mAppContext;
	
	private SellerAuthority mSellerAuthority;
	private SellerServiceHandler mSellerServiceHandler;
	
	public AliYunOSAPI(AppContext context){
		this.mAppContext=context;
		this.mSellerAuthority = new SellerAuthority(context);
		this.mSellerServiceHandler = new SellerServiceHandler(context);
	}
	
	/**
	 * SellerAuthority.SELLER_PHONE 卖家手机
	 * SellerAuthority.NORMAL_PHONE 普通手机
	 * SellerAuthority.ERROR_NETWORK_NOT_AVAILABLE 网络不可用
	 * @return
	 */
	public int getSystemType(){
		int systemType=mSellerAuthority.getSystemType();
		Log.v(TAG,"getSystemType:"+systemType);
		return systemType;
	}
	
	/**
	 * 当前是否有账户登录
	 * @return
	 */
	public Boolean isAccountLogin(){
		Boolean isAccountLogin=mSellerServiceHandler.isAccountLogin();
		Log.v(TAG,"isAccountLogin:"+isAccountLogin);
		return isAccountLogin;
	}
	
	/**
	 * 是否卖家账户已登录
	 * @return
	 */
	public Boolean isSellerAcccountLogin(){
		Boolean isSellerAcccountLogin=mSellerServiceHandler.isSellerAcccountLogin();
		Log.v(TAG,"isSellerAcccountLogin:"+isSellerAcccountLogin);
		return isSellerAcccountLogin;
	}
	
	/**
	 * 验证当前服务的状态
	 */
	public Bundle isValidServiceStatus(){
		return mSellerServiceHandler.queryPartnerServiceStatus(APPNAME);
	}
	
	/**
	 * 赠送成功后通知云OS
	 */
	public void notifyOSService(){
		Bundle bundle = mSellerServiceHandler.activePartnerService(APPNAME);
		int keyCode=bundle.getInt(SellerServiceHandler.KEY_CODE);
		Log.v(TAG,"notifyOSService KEY_CODE:"+keyCode);
		if (keyCode== SellerServiceHandler.CODE_SUCCESS) {
			int keyResult=bundle.getInt(SellerServiceHandler.KEY_RESULT);
		    if(SellerServiceHandler.RESULT_ACTIVE==keyResult){
		    		Log.v(TAG,"notifyOSService KEY_RESULT:"+keyResult);
			    	//赠送激活成功初始设置标记
			    	mAppContext.getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
		    }
		}
	}
	
}
