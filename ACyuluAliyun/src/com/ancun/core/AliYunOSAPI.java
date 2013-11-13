package com.ancun.core;

import com.ancun.utils.StringUtils;
import com.ancun.yulualiyun.AppContext;

public class AliYunOSAPI {

	@SuppressWarnings("unused")
	private AppContext mContext;
	
//	private SellerAuthority mSellerAuthority
	
	public AliYunOSAPI(AppContext context){
		this.mContext=context;
//		this.mSellerAuthority = new SellerAuthority(context);
	}
	
	/**
	 * 阿里云设备唯一码，或淘宝账号
	 * @return
	 */
	public String getUUID(){
		return StringUtils.random();
	}
	
	/**
	 * 是否卖家机
	 * @return
	 */
	public Boolean isMjPhone(){
		return true;
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
		return true;
	}
	
	/**
	 * 登录帐号对应的激活码是否已激活
	 */
	public Boolean isActivation(){
		return true;
	}
	
	/**
	 * 登录帐号对应的激活码和应用服务是否有效[有该服务且还未赠送]
	 */
	public Boolean isValidService(){
		return true;
	}
	
	/**
	 * 该服务是否已经赠送使用
	 * @return
	 */
	public Boolean isServiceUse(){
		return false;
	}
	
}
