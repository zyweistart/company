package com.ancun.core;

import com.ancun.utils.StringUtils;

import android.content.Context;

public class AliYunOSAPI {

	@SuppressWarnings("unused")
	private Context mContext;
	
//	private SellerAuthority mSellerAuthority
	
	public AliYunOSAPI(Context context){
		this.mContext=context;
//		this.mSellerAuthority = new SellerAuthority(context);
	}
	
	/**
	 * 阿里云设备唯一码
	 * @return
	 */
	public String getUUID(){
		return StringUtils.random();
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
	 */
	public Boolean isActivation(){
		return false;
	}
	
	/**
	 * 登录帐号对应的激活码和应用服务是否有效[有该服务且还未赠送]
	 */
	public Boolean isValidService(){
		return false;
	}
	
}
