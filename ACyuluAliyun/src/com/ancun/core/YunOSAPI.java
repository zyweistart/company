package com.ancun.core;

import android.content.Context;

public class YunOSAPI {

	@SuppressWarnings("unused")
	private Context mContext;
	
//	private SellerAuthority mSellerAuthority
	
	public YunOSAPI(Context context){
		this.mContext=context;
//		this.mSellerAuthority = new SellerAuthority(context);
	}
	
	/**
	 * 普通手机
	 * @return
	 */
	public Boolean normalPhone(){
		return false;
	}
	
	/**
	 * 卖家手机
	 * @return
	 */
	public Boolean sellerPhone(){
		return false;
	}
	
	/**
	 * 网络不可用
	 * @return
	 */
	public Boolean errorNetworkNotAvailable(){
		return true;
	}
	
	/**
	 * 是否卖家机
	 * @return
	 */
	public Boolean isMjPhone(){
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
	 * @param accountId
	 * @param activationCode
	 * @return
	 */
	public Boolean isActivation(String accountId,String activationCode){
		return false;
	}
	
	/**
	 * 登录帐号对应的激活码和应用服务是否有效[有该服务且还未赠送]
	 * @param accountId
	 * @param activationCode
	 * @param AppId
	 * @return
	 */
	public Boolean isValidService(String accountId,String activationCode,String appId){
		return false;
	}
}
