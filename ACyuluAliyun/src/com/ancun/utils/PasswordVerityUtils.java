package com.ancun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ancun.core.Constant;
import com.ancun.yulualiyun.AppContext;

public class PasswordVerityUtils {

	public static boolean verify(AppContext context,String str){
		return verify(context, str,Constant.EMPTYSTR);
	}
	
	public static boolean verify(AppContext context,String str,String pretip){
		if(isSpace(str)){
			if(context!=null){
				context.makeTextShort(pretip+"不能包含非法字符");
			}
		}else if(Constant.EMPTYSTR.equals(str)){
			if(context!=null){
				context.makeTextShort(pretip+"不能为空");
			}
		}else if(!isLengthOK(str)){
			if(context!=null){
				context.makeTextShort(pretip+"长度应为6-16个字符");
			}
		}else if(isAllNumber(str)){
			if(context!=null){
				context.makeTextShort(pretip+"不能全为数字");
			}
		}else if(isSameChars(str)){
			if(context!=null){
				context.makeTextShort(pretip+"不能为同一字符");
			}
		}else{
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符长度是否符合规定
	 */
	public static boolean isLengthOK(String str){
		if(Constant.EMPTYSTR.equals(str)){
			return false;
		}
		return str.length()>=6&&str.length()<=16;
	}
	
	/**
	 * 判断是否为纯数字
	 */
	public static boolean isAllNumber(String str){
		if(Constant.EMPTYSTR.equals(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern
				.matcher((CharSequence) str);
		return matcher.matches();
	}
	
	/**
	 * 判断是否为相同字符
	 */
	public static boolean isSameChars(String str){
		if(Constant.EMPTYSTR.equals(str)){
			return false;
		} else if (str.length() < 2) {
			return true;
		}
		char first = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) != first) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否存在空格字符
	 */
	public static boolean isSpace(String str){
		return str.indexOf(" ")>-1;
	}
	
}
