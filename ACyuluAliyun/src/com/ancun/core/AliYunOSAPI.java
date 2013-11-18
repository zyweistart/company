package com.ancun.core;

import android.os.Bundle;

import com.ancun.utils.StringUtils;
import com.ancun.yulualiyun.AppContext;
import com.yunos.boot.SellerServiceHandler;
import com.yunos.seller.SellerAuthority;

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
	 * 阿里云设备唯一码，或淘宝账号
	 * @return
	 */
	public String getUUID(){
		return StringUtils.random();
	}
	
	/**
	 * 卖家账号
	 * @return
	 */
	public String sellerAccount(){
		if(isSystemLogin()){
			return "";
		}else{
			return "";
		}
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
	 * 判断是否为阿里云手机
	 */
	public Boolean isAliYunPhone(){
		return true;
	}
	
	/**
	 * 是否已经使用淘宝账号登录
	 * @return
	 */
	public Boolean isSystemLogin(){
		return mSellerServiceHandler.isSellerAcccountLogin();
	}
	
	/**
	 * 登录帐号对应的激活码是否已激活
	 */
	public Boolean isActivation(){
		//TODO:登录帐号对应的激活码是否已激活
		return true;
	}
	
	/**
	 * 验证当前服务的状态
	 */
	public int isValidServiceStatus(){
		Bundle bundle = mSellerServiceHandler.queryPartnerServiceStatus(APPNAME);
		if (bundle.getInt(SellerServiceHandler.KEY_CODE) == SellerServiceHandler.CODE_SUCCESS) {
			return bundle.getInt(SellerServiceHandler.KEY_RESULT);
		}
		return -1;
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
