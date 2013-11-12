package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ancun.core.Constant;
import com.ancun.core.XMLException;
import com.ancun.core.AliYunOSAPI;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.HttpUtils;
import com.ancun.utils.LogUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.SharedPreferencesUtils;
import com.ancun.utils.StringUtils;
import com.ancun.utils.XMLUtils;
import com.ancun.widget.PullToRefreshListView;

public class AppContext extends Application {
	
	private WindowManager.LayoutParams wmParams;

	public WindowManager.LayoutParams getMywmParams(){
		if(wmParams==null){
			wmParams=new WindowManager.LayoutParams();
		}
		return wmParams;
	}
	
	private AliYunOSAPI yunOSAPI;
	
	/**
	 * 阿里云OSAPI接口
	 */
	public AliYunOSAPI getYunOSAPI() {
		if(yunOSAPI==null){
			yunOSAPI=new AliYunOSAPI(this);
		}
		return yunOSAPI;
	}
	
	private SharedPreferencesUtils sharedPreferencesUtils=null;
	
	public SharedPreferencesUtils getSharedPreferencesUtils() {
		if(sharedPreferencesUtils==null){
			sharedPreferencesUtils=new SharedPreferencesUtils(this);
		}
		return sharedPreferencesUtils;
	}
	
	private Map<String,String> authInfo=new HashMap<String,String>();
	
	public void buildAuth(String xmlContent){
		authInfo.clear();
		if(!"1".equals(getUserInfo().get("masterflag"))){
			//非企业主账号
			try {
				authInfo=XMLUtils.xmlResolveByUserAuth(xmlContent);
			} catch (XMLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 验证当前权限是否满足
	 * @param authName
	 * @return
	 */
	public boolean isAuth(String authName){
		return "1".equals(getUserInfo().get("masterflag"))||(authInfo!=null&&authInfo.containsKey(authName));
	}
	
	private Map<String,Map<String,String>> userInfoAll=new HashMap<String,Map<String,String>>();
	
	private Map<String,String> userInfo=new HashMap<String,String>();
	
	public Map<String, String> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, String> userInfo) {
		this.userInfo = userInfo;
	}
	public Map<String, Map<String, String>> getUserInfoAll() {
		return userInfoAll;
	}

	public void setUserInfoAll(Map<String, Map<String, String>> userInfoAll) {
		this.userInfoAll = userInfoAll;
	}
	/**
	 * 判断是否为老用户
	 */
	public boolean isOldUserFlag(){
		return "1".equals(getUserInfo().get("uflag"));
	}
	
	/**
	 * 获取当前登陆用户的账户号码
	 */
	public String getCurrentLoginPhoneNumber(Activity activity){
		if(userInfo!=null){
			return userInfo.get("phone");
		}
		reLogin(activity, "登录超时");
		return "";
	}
	
	/**
	 * 重新登陆
	 */
	public void reLogin(Activity activity,String exitmsg){
		Bundle bundle=new Bundle();
		Intent intent=new Intent(activity,LoginActivity.class);
		bundle.putBoolean(Constant.BUNLE_AUTOLOGINFLAG, false);
		if(exitmsg!=null){
			bundle.putString(Constant.BUNLE_EXIT_MSG,exitmsg);
		}
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * 加载模式
	 */
	public enum LoadMode{
		//初始化
		INIT,
		//头部刷新
		HEAD,
		//尾部刷新
		FOOT
	}
	
	public void sendPullToRefreshListViewNetRequest(
			final Activity activity,
			final List<Map<String,String>> listDatas,
			final PullToRefreshListView pListView,
			final View mFooterMore,
			final TextView mFooterMoreTitle,
			final ProgressBar mFooterMoreProgressBar,
			final LoadMode loadMode,
			final String Url,
			final Map<String,String> params,
			final Map<String,String> headerParams,
			final UIRunnable uiRunnable,
			final String DATALIST,
			final String TAGName){
		final String CACHE_TAG=String.format("%s-%s", getCurrentLoginPhoneNumber(activity),TAGName);
		if(loadMode==LoadMode.INIT){
			String xmlContent=getSharedPreferencesUtils().getString(CACHE_TAG, Constant.EMPTYSTR);
			if(!StringUtils.isEmpty(xmlContent)){
				try{
					Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(xmlContent);
					if(!mapXML.isEmpty()){
						listDatas.clear();
						for(Map<String,String> content:mapXML.get(DATALIST)){
							listDatas.add(content);
						}
						pListView.setTag(Constant.LISTVIEW_DATA_MORE);
						mFooterMoreTitle.setText(R.string.load_more);
						mFooterMoreProgressBar.setVisibility(View.GONE);
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								uiRunnable.run();
							}
						});
						return;
					}
				}catch(Exception e){
					getSharedPreferencesUtils().putString(CACHE_TAG, Constant.EMPTYSTR);
				}
			}
		}else if(loadMode==LoadMode.FOOT){
			//点击加载更多时
			//判断是否滚动到底部
			boolean scrollEnd = false;
			try {
				if(pListView.getPositionForView(mFooterMore) == pListView.getLastVisiblePosition())
					scrollEnd = true;
			} catch (Exception e) {
				scrollEnd = false;
			}
			if(scrollEnd&&StringUtils.toInt(pListView.getTag())==Constant.LISTVIEW_DATA_MORE){
				pListView.setTag(Constant.LISTVIEW_DATA_LOADING);
				mFooterMoreTitle.setText(R.string.load_ing);
				mFooterMoreProgressBar.setVisibility(View.VISIBLE);
			}else{
				return;
			}
		}
		if(NetConnectManager.isNetWorkAvailable(activity)){
			new Thread(new Runnable() {
				@Override
				public void run() {
					//当前请求加载的数据列表
					Boolean loadDataFlag=false;
					Integer loadDataNum=0;
					try{
						int currentPage=1;
						if(loadMode==LoadMode.FOOT){
							int size=listDatas.size();
							if(size%Constant.PAGESIZE==0){
								currentPage=size/Constant.PAGESIZE+1;
							}else{
								return;
							}
						}
						Map<String,String> requestParams=new HashMap<String,String>();
						if(params!=null){
							requestParams.putAll(params);
						}
						requestParams.put("currentpage",String.valueOf(currentPage));
						requestParams.put("pagesize", String.valueOf(Constant.PAGESIZE));
						String requestContent = XMLUtils.builderRequestXml(Url, requestParams);
						//请求头内容
						Map<String,String> requestHeader=new HashMap<String,String>();
						if(headerParams!=null){
							requestHeader.putAll(headerParams);
						}
						//签名为特殊处理key为不存在时默认用ACCESSKEY签名为""用MD5否则用传入的值进行签名
						if(!requestHeader.containsKey("sign")){
							requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),Constant.ACCESSKEY));
						}else{
							requestHeader.put("sign","".equals(requestHeader.get("sign"))?
									MD5.md5(requestContent):
										StringUtils.signatureHmacSHA1(MD5.md5(requestContent),requestHeader.get("sign")));
						}
						String xmlContent=HttpUtils.requestServerByXmlContent(requestHeader, requestContent);
						Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(xmlContent);
						if(!mapXML.isEmpty()){
							Map<String,String> infoHead=mapXML.get(Constant.RequestXmLConstant.INFO).get(0);
							if(Constant.RequestXmLConstant.SUCCESSCODE.equals(infoHead.get(Constant.RequestXmLConstant.CODE))){
								if(loadMode!=LoadMode.FOOT){
									//如果不为尾部刷新加载则清空数据
									listDatas.clear();
								}
								final List<Map<String,String>> contents=mapXML.get(DATALIST);
								loadDataNum=contents.size();
								if(loadDataNum>0){
									loadDataFlag=true;
								}
								if(loadDataFlag){
									//只在成功且有数据时才进行缓存
									if(loadMode==LoadMode.INIT||loadMode==LoadMode.HEAD){
										getSharedPreferencesUtils().putString(CACHE_TAG, xmlContent);
									}
								}
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										for(Map<String,String> content:contents){
//											listDatas.add(content);
//										}
										listDatas.addAll(contents);
										uiRunnable.run();
									}
								});
							}else if(infoHead.get(Constant.RequestXmLConstant.CODE).equals(Constant.RequestXmLConstant.LOGINERROR)){
//								reLogin(activity,infoHead.get(Constant.RequestXmLConstant.MSG));
								reLogin(activity, "登录超时");
							}else if("110042".equals(infoHead.get(Constant.RequestXmLConstant.CODE))){
								//暂无记录
								if(loadMode==LoadMode.HEAD){
									getSharedPreferencesUtils().putString(CACHE_TAG, "");
									
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											listDatas.clear();
											uiRunnable.run();
										}
									});
								}
							}else{
								makeTextLong(infoHead.get(Constant.RequestXmLConstant.MSG));
							}
						}else{
							makeTextLong(getString(R.string.app_error_please_try_again));
						}
					}catch(Exception e){
						LogUtils.logError(e);
						makeTextLong(getString(R.string.app_error_please_try_again));
					}finally{
						final Boolean flag=loadDataFlag;
						final Integer num=loadDataNum;
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//如果当前为刷新状态获取的数据为空
								if(flag){
									if(num>=Constant.PAGESIZE){
										pListView.setTag(Constant.LISTVIEW_DATA_MORE);
										mFooterMoreTitle.setText(R.string.load_more);
									}else{
										//加载完毕
										pListView.setTag(Constant.LISTVIEW_DATA_FULL);
										mFooterMoreTitle.setText(R.string.load_full);
									}
								}else{
									if(loadMode==LoadMode.FOOT){
										//加载完毕
										pListView.setTag(Constant.LISTVIEW_DATA_FULL);
										mFooterMoreTitle.setText(R.string.load_full);
									}else{
										//数据为空
										pListView.setTag(Constant.LISTVIEW_DATA_EMPTY);
										mFooterMoreTitle.setText(R.string.load_empty);
									}
								}
								mFooterMoreProgressBar.setVisibility(View.GONE);
								//刷新模式为下拉刷新时
								if(loadMode==LoadMode.HEAD){
									pListView.onRefreshComplete();
								}
							}
						});
						
					}
				}}).start();
		}else{
			CommonFn.settingNetwork(activity);
			pListView.setTag(Constant.LISTVIEW_DATA_MORE);
			mFooterMoreTitle.setText(R.string.load_more);
			mFooterMoreProgressBar.setVisibility(View.GONE);
			if(loadMode==LoadMode.HEAD){
				pListView.onRefreshComplete();
			}
		}
	}
	
	public void exeNetRequest(Activity activity,final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable){
		exeNetRequest(activity,getString(R.string.app_pleasewait),Url, params,headerParams, uiRunnable,null);
	}
	
	public void exeNetRequest(final Activity activity,final String tipMsg,final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable,final Map<String,String> customInfo){
		if(NetConnectManager.isNetWorkAvailable(activity)){
			final ProgressDialog mProgressDialog =tipMsg!=null?CommonFn.progressDialog(activity, tipMsg):null;
			if(mProgressDialog!=null){
				mProgressDialog.show();
				mProgressDialog.setCancelable(false);
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						String requestContent = XMLUtils.builderRequestXml(Url, params);
						//请求头内容
						Map<String,String> requestHeader=new HashMap<String,String>();
						if(headerParams!=null){
							requestHeader.putAll(headerParams);
						}
						//签名为特殊处理key为不存在时默认用ACCESSKEY签名为""用MD5否则用传入的值进行签名
						if(!requestHeader.containsKey("sign")){
							requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),Constant.ACCESSKEY));
						}else{
							requestHeader.put("sign","".equals(requestHeader.get("sign"))?
									MD5.md5(requestContent):
										StringUtils.signatureHmacSHA1(MD5.md5(requestContent),requestHeader.get("sign")));
						}
						String xmlContent=HttpUtils.requestServerByXmlContent(requestHeader, requestContent);
						uiRunnable.setResponseContent(xmlContent);
						final Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(xmlContent);
						if(!mapXML.isEmpty()){
							Map<String,String> infoHead=mapXML.get(Constant.RequestXmLConstant.INFO);
							if(infoHead.get(Constant.RequestXmLConstant.CODE).equals(Constant.RequestXmLConstant.SUCCESSCODE)){
								for(String key:mapXML.keySet()){
									uiRunnable.setInfoContent(mapXML.get(key));
									break;
								}
								uiRunnable.setAllInfoContent(mapXML);
								uiRunnable.run();
							}else if(infoHead.get(Constant.RequestXmLConstant.CODE).equals(Constant.RequestXmLConstant.LOGINERROR)){
								reLogin(activity, "登录超时");
							}else if(infoHead.get(Constant.RequestXmLConstant.CODE).equals("110036")||
									infoHead.get(Constant.RequestXmLConstant.CODE).equals("120020")){
								getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
								makeTextLong("用户名或密码有误");
							}else{
								if(customInfo!=null){
									String message=customInfo.get(infoHead.get(Constant.RequestXmLConstant.CODE));
									if(message!=null){
										makeTextLong(message);
										return;
									}
								}
								makeTextLong(infoHead.get(Constant.RequestXmLConstant.MSG));
							}
						}else{
							makeTextLong(getString(R.string.app_error_please_try_again));
						}
					}catch(Exception e){
						LogUtils.logError(e);
						makeTextLong(getString(R.string.app_error_please_try_again));
					}finally{
						if (mProgressDialog != null) {
							mProgressDialog.dismiss();
						}
					}
				}}).start();
		}else{
			CommonFn.settingNetwork(activity);
		}
	}
	
	public void makeTextShort(String text) {
		if(text!=null&&!"".equals(text)){
			sendMessage(Toast.LENGTH_SHORT,text);
		}
	}

	public void makeTextLong(String text) {
		if(text!=null&&!"".equals(text)){
			sendMessage(Toast.LENGTH_LONG,text);
		}
	}
	
	public void sendMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendMessage(msg);
	}
	
	public void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}
	
	public Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Toast.LENGTH_SHORT:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case Toast.LENGTH_LONG:
				Toast.makeText(getApplicationContext(), String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
				break;
			}
		}
		
	};
	
}