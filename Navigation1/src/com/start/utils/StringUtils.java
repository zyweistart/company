package com.start.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.start.core.Constant;
/**
 * 字符串处理类
 * @author Start
 */
public class StringUtils {
	 /**
	 * HmacSHA1签名
	 */
	public static String signatureHmacSHA1(String data,String key){
		try {
			return Base64.encode(HmacSHA1.signature(data, key,Constant.ENCODE));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	 /**
     * DES加密
     */
    public static String doKeyEncrypt(String str,InputStream deskey){
    	try {
			return DES.encrypt(str, deskey);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
    /**
     * DES解密
     */
	public static String doKeyDecrypt(String str,InputStream deskey){
		try {
			return DES.decrypt(str, deskey);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
     * URL编码
     */
	public static String encode(String str) {
		return encode(str,Constant.ENCODE);
	}
	/**
     * URL编码
     */
	public static String encode(String str, String enc) {
		String strEncode = "";
		try {
			if (str != null){
				strEncode = URLEncoder.encode(str, enc);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strEncode;
	}
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * 号码转换，例：1 37-3887-3386转换成13738873386把有格式的转成标准号码格式
	 * @param phone
	 * @return
	 */
	public static String convertPhone(String phone){
		//号码转换
		StringBuilder sbphone=new StringBuilder();
		for(int i=0;i<phone.length();i++){
			char c=phone.charAt(i);
			int asnum=(int)c;
			if(asnum>=48&&asnum<=57){
				sbphone.append(c);
			}
		}
		return sbphone.toString();
	}
	
	public static String convertPhonelike(String phone){
		//号码转换
		StringBuilder sbphone=new StringBuilder();
		for(int i=0;i<phone.length();i++){
			char c=phone.charAt(i);
			int asnum=(int)c;
			if(asnum>=48&&asnum<=57){
				sbphone.append(c);
				sbphone.append("%");
			}
		}
		if(sbphone.length()>0){
			return sbphone.substring(0,sbphone.length()-1);
		}
		return sbphone.toString();
	}
}