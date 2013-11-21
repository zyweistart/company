package com.ancun.core;

import android.os.Bundle;

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
		return mSellerAuthority.getSystemType();
	}
	
	/**
	 * 是否已经使用淘宝账号登录
	 * @return
	 */
	public Boolean isSystemLogin(){
		Boolean status=mSellerServiceHandler.isSellerAcccountLogin();
		if(status){
			//TODO:设置当前登录的账户名称当前为UUID
			mAppContext.setSystemLoginAccount(mAppContext.getUUID());
		}
		return status;
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
		if (bundle.getInt(SellerServiceHandler.KEY_CODE) == SellerServiceHandler.CODE_SUCCESS) {
		    if(SellerServiceHandler.RESULT_ACTIVE==
		    		bundle.getInt(SellerServiceHandler.KEY_RESULT)){
			    	//赠送激活成功初始设置标记
			    	mAppContext.getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
		    }
		}
	}
	
}
