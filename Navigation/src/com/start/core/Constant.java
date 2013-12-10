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
	public static final boolean SYSTEMTEST=false;

	public static final String RESTURL=SYSTEMTEST?
			"http://115.236.89.210:8888/accore/http/HttpService"://公司外网
			"http://account.chinacloudapp.cn:81/http/HttpService";//正式地址
	
	public static String ACCESSID="";
	public static String ACCESSKEY="";
	
	public static final String ACCESSID_LOCAL=SYSTEMTEST?
			"e75cf0bee6850378bdb606e13172018e":
			"e75cf0bee6850378bdb606e13172018e";
	public static final String ACCESSKEY_LOCAL=SYSTEMTEST?
			"NDBmOTE2NWZlYTdhZWNlNTE0OTQyYmYxMWNjNGI0NTg=":
			"NDBmOTE2NWZlYTdhZWNlNTE0OTQyYmYxMWNjNGI0NTg=";
	
	public static final int PAGESIZE=8;
	public static final String EMPTYSTR="";
	public static final String ENCODE="UTF-8";
	
	public static final String DATADIRFILE="/navigation/data/";//数据主目录
	public static final String TMPDIRFILE="/navigation/tmp/";//临时目录

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final class ServerAPI{
		/**
		 * 验证码获取
		 */
		public static final String uacodeGet="uacodeGet";
		/**
		 * 注册
		 */
		public static final String userReg="userReg";
		/**
		 * 密码重置
		 */
		public static final String userpwdReset="userpwdReset";
		/**
		 * 登录
		 */
		public static final String userLogin="userLogin";
		/**
		 * 密码修改
		 */
		public static final String userpwdMod="userpwdMod";
		/**
		 * 我开放位置权限的好友查询
		 */
		public static final String nOpenFriendList="ufriendoQuery";
		/**
		 * 对好友开放位置权限管理
		 */
		public static final String ufriendoDeal="ufriendoDeal";
		/**
		 * 获取好友位置列表
		 */
		public static final String nFriendLocationList="ufriendoQuery";
		/**
		 * 对好友开放位置信息
		 */
		public static final String nOpenLocation="serverinfoGet";
		/**
		 * 上传位置信息
		 */
		public static final String nUploadMyLocation="";
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