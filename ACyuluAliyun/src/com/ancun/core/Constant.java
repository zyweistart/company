package com.ancun.core;

import java.util.ArrayList;
import java.util.List;

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
	
	public static final String TESTSERVERNO="057156559570";

	public static final String RESTURL=SYSTEMTEST?
//			"http://192.168.0.161:8080/http/HttpService"://本机地址
//			"http://192.168.0.221:8080/accore/http/HttpService"://公司内网
			"http://115.236.89.210:8888/accore/http/HttpService"://公司外网
			"http://server.ancun.com:3391/rest/RestService";//正式地址
	/**
	 * 本地通行证编号
	 */
	public static final String ACCESSID_LOCAL=SYSTEMTEST?
			"b99810e661d546f14763f15a9270c28d":
				"e4e60d050f5513e086b47c4fdca35646";
	/**
	 * 本地通行证密钥
	 */
	public static final String ACCESSKEY_LOCAL=SYSTEMTEST?
			"aRxzVdMiopnzpJco3fPJOmXSDZL7rGiL3UgbqQA9YeJ=":
				"fUETGmOIiMujnipQnFm6rJIyO2wJDZSftsK4dwmmoSZ=";
	/**
	 * 通行证编号
	 */
	public static String ACCESSID="";
	/**
	 * 通行证密钥
	 */
	public static String ACCESSKEY="";
	/**
	 * 空字符
	 */
	public static final String EMPTYSTR="";
	/**
	 * 编码
	 */
	public static final String ENCODE="UTF-8";
	/**
	 * SQLite数据库名称
	 */
	public static final String SQLiteDataBaseName="DBAC951335.db";
	/**
	 * 加密键
	 */
	public static final String DESKEYKEY="deskeykey.data";
	/**
	 * 需要过滤的号码
	 */
	public static List<String> noCall=new ArrayList<String>();
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int PAGESIZE=8;
	/**
	 * 设置与帮助页面 重新登录标志
	 */
	public static final int SETTING_RELOGIN=1;
	/**
	 * 设置与帮助页面 重新退出标志
	 */
	public static final int SETTING_QUIT=2;
	
	public static final String BUNLE_AUTOLOGINFLAG="autoLoginFlag";
	
	public static final String BUNLE_EXIT_MSG="exitmsg";
	/**
	 * SharedPreferences常量
	 * @author Start
	 */
	public final class SharedPreferencesConstant{
		/**
		 * 是否绑定阿里云淘宝账户
		 */
		public static final String SP_ISBIND_TAOBAOACCOUNT="SP_ISBIND_TAOBAOACCOUNT";
		/**
		 * 当前的版本号
		 */
		public static final String SP_CURRENTVERSIONCODE="SP_CURRENTVERSIONCODE";
		/**
		 * 是否首次加载应用
		 */
		public static final String SP_FIRST_LOADAPP="SP_FIRST_LOADAPP";
		/**
		 * 是否允许通话拦截
		 */
		public static final String SP_CALL_ISALLOW="CALL_ISALLOW";
		/**
		 * 当前登录的手机账号
		 */
		public static final String SP_ACCOUNT="LOGIN_SP_ACCOUNT";
		/**
		 * 当前登录的账号密码
		 */
		public static final String SP_PASSWORD="LOGIN_SP_PASSWORD";
		/**
		 * 登录界面自动登录标志
		 */
		public static final String SP_AUTOLOGIN="LOGIN_SP_AUTOLOGIN";
		/**
		 * 拔通的电话
		 */
		public static final String SP_CALL_DIAL="SP_CALL_DIAL";
		/**
		 * 是否启用外部拔号盘
		 */
		public static final String SP_START_EXTERNAL_DIAL="SP_START_EXTERNAL_DIAL";
		/**
		 * 陌生号码外部提示是否关闭
		 */
		public static final String SP_UNFAMILIAR_EXTERNAL_DIAL="SP_UNFAMILIAR_EXTERNAL_DIAL";
		/**
		 * 最近版本检测时间
		 */
		public static final String SP_VERSION_DATA="SP_VERSION_DATA";
		/**
		 * Android4.0以上系统默认打完电话后会跳转到
		 * {act=android.intent.action.VIEW typ=vnd.android.cursor.dir/calls cmp=com.android.contacts/.activities.DialtactsActivity u=0}
		 */
		public static final String SP_MAINACTIVITY_FLAG="SP_MAINACTIVITY_FLAG";
		/**
		 * 存储服务端的电话号码
		 */
		public static final String SP_SERVER_CALL="SP_SERVER_CALL";
		
	}
	/**
	 * 服务端返回的XML标签
	 * @author Start
	 */
	public final class RequestXmLConstant{
		public static final String INFO="info";
		
		public static final String PAGEINFO="pageinfo";
		
		public static final String CONTENT="content";
		
		public static final String CODE="code";
		
		public static final String MSG="msg";
		
		public static final String SUCCESSCODE="100000";
		
		public static final String LOGINERROR="110026";
	
	}
	/**
	 * 用户接口
	 * @author Start
	 */
	public final class GlobalURL{
		/**
		 * 检查手机号码是否已被注册
		 */
		public static final String v4Check="v4Check";
		/**
		 * 短信校验码获取
		 */
		public static final String v4scodeGet="v4scodeGet";
		/**
		 * 注册
		 */
		public static final String v4Signup="v4Signup";
		/**
		 * 密码重置
		 */
		public static final String v4pwdReset="v4pwdReset";
		/**
		 * 登录
		 */
		public static final String v4Login = "v4Login";
		/**
		 * 退出
		 */
		public static final String v4Logout="v4Logout";
		/**
		 * 用户信息获取
		 */
		public static final String v4infoGet="v4infoGet";
		/**
		 * 密码修改
		 */
		public static final String v4pwdModify="v4pwdModify";
		/**
		 * 短信校验码验证
		 */
		public static final String v4scodeVer="v4scodeVer";
		/**
		 * 呼叫请求
		 */
		public static final String v4Call="v4Call";
		/**
		 * 呼叫取消
		 */
		public static final String v4Cancel="v4Cancel";
		/**
		 * 录音查询
		 */
		public static final String v4recQry = "v4recQry";
		/**
		 * 录音信息获取
		 */
		public static final String v4recGet = "v4recGet";
		/**
		 * 录音备注修改
		 */
		public static final String v4recRemark = "v4recRemark";
		/**
		 * 录音下载
		 */
		public static final String v4recDown = "v4recDown";
		/**
		 * 录音出证
		 */
		public static final String v4recCer="v4recCer";
		/**
		 * 录音统计
		 */
		public static final String v4recStat="v4recStat";
		/**
		 * 录音状态变更
		 */
		public static final String v4recAlter="v4recAlter";
		/**
		 * 外部呼叫请求
		 */
		public static final String v4eCall="v4eCall";
		/**
		 * 外部呼叫取消
		 */
		public static final String v4eCancel="v4eCancel";
		/**
		 * 支付宝充值申请
		 */
		public static final String v4alipayReq="v4alipayReq";
		/**
		 * 支付宝返回结果校验
		 */
		public static final String v4ealipayRes="v4ealipayRes";
		/**
		 * 账户明细
		 */
		public static final String v4accDetail="v4accDetail";
		/**
		 * 账户统计
		 */
		public static final String v4accStat="v4accStat";
		/**
		 * 录音提取码
		 */
		public static final String v4recAcccode="v4recAcccode";
		/**
		 * 终端版本信息获取
		 */
		public static final String versioninfoGet="versioninfoGet";
		/**
		 * 意见反馈
		 */
		public static final String v4Feedback="v4Feedback";
		/**
		 * 充值产品获取
		 */
		public static final String recprodGet="recprodGet";
		/**
		 * 推送通知接口
		 */
		public static final String v4Message="v4Message";
		/**
		 * 用户套餐信息获取
		 */
		public static final String v4combinfoGet="v4combinfoGet";
		/**
		 * 用户账户信息查询
		 */
		public static final String v4accnewDetail="v4accnewDetail";
		/**
		 *  新充值产品查询
		 */
		public static final String v4QrycomboList="v4QrycomboList";
	}
	
	public final class Auth{
		/**
		 * 录音查询
		 */
		public static final String v4recqry1="v4recqry1";
		/**
		 * 录音详情查看
		 */
		public static final String v4recget1="v4recget1";
		/**
		 * 录音备注修改
		 */
		public static final String v4recremark="v4recremark";
		/**
		 * 录音收听下载
		 */
		public static final String v4recdown1="v4recdown1";
		/**
		 * 录音删除
		 */
		public static final String v4recalter1="v4recalter1";
		/**
		 * 录音撤销提取
		 */
		public static final String v4recalter8="v4recalter8";
		/**
		 * 提取列表
		 */
		public static final String v4recqry3="v4recqry3";
		
	}
	
	static{
		noCall.add("95105856");
		noCall.add("110");
		noCall.add("112");
		noCall.add("119");
	}
}