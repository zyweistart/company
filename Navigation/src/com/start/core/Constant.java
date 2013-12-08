package com.start.core;


/**
 * 常量
 * @author Start
 */
public final class Constant {
	/**
	 * true:测试环境
	 * false:正式环境
	 */
	public static final boolean SYSTEMTEST=true;

	public static final String RESTURL=SYSTEMTEST?
			"http://115.236.89.210:8888/accore/http/HttpService"://公司外网
			"http://server.ancun.com:3391/rest/RestService";//正式地址
	
	public static String ACCESSID="";
	public static String ACCESSKEY="";
	public static final String EMPTYSTR="";
	public static final String ENCODE="UTF-8";
	public static final int PAGESIZE=8;
	
	public static final String DATADIRFILE="/navigation/data/";//数据主目录
	public static final String TMPDIRFILE="/navigation/tmp/";//临时目录

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final class ServerAPI{
		/**
		 * 登录
		 */
		public static final String nLogin="v4Login";
		/**
		 * 注册
		 */
		public static final String nRegister="v4Signup";
		/**
		 * 忘记密码
		 */
		public static final String nForgetPwd="";
		/**
		 * 获取好友位置列表
		 */
		public static final String nFriendLocationList="v4recQry";
		/**
		 * 获取已开放的好友列表
		 */
		public static final String nOpenFriendList="v4recQry";
		/**
		 * 对好友开放位置信息
		 */
		public static final String nOpenLocation="serverinfoGet";
		/**
		 * 上传位置信息
		 */
		public static final String nUploadMyLocation="";
		/**
		 * 数据文件获取列表
		 */
		public static final String nDataFileList="v4recQry";
		/**
		 * 数据文件详细
		 */
		public static final String nDataFileDetail="";
		/**
		 * 数据文件下载
		 */
		public static final String nDataFileDownload="v4recDown";
		/**
		 * 检测版本更新
		 */
		public static final String nVersionCheck="versioninfoGet";
	}
	
	/**
	 * Activity返回代码
	 * @author start
	 *
	 */
	public final class ActivityResultCode{
		public static final int EXITAPP=9991;
	}
	
	public final class SharedPreferences{
		/**
		 * 当前使用的数据文件编号
		 */
		public static final String CURRENTDATAFILENO="CURRENTDATAFILENO";
		
		public static final String SP_CURRENTVERSIONCODE="SP_CURRENTVERSIONCODE";
		
		public static final String LOGIN_ACCOUNT="LOGIN_ACCOUNT";
		public static final String LOGIN_PASSWORD="LOGIN_PASSWORD";
		public static final String LOGIN_AUTOLOGIN="LOGIN_AUTOLOGIN";
		
	}
	
}