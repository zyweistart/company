package com.start.navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapsforge.core.model.GeoPoint;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.start.core.Constant;
import com.start.model.LoadMode;
import com.start.model.UIRunnable;
import com.start.model.nav.MyLocation;
import com.start.model.nav.PathSearchResult;
import com.start.utils.CommonFn;
import com.start.utils.HttpUtils;
import com.start.utils.LogUtils;
import com.start.utils.MD5;
import com.start.utils.NetConnectManager;
import com.start.utils.SharedPreferencesUtils;
import com.start.utils.StringUtils;
import com.start.utils.XMLUtils;
import com.start.widget.PullToRefreshListView;

public class AppContext extends Application {

	/**
	 * 百度地图授权Key是否正确
	 */
	public boolean m_bKeyRight;
	/**
	 * 百度地图授权Key
	 */
	private static final String BAIDUMAPKEY = "5f681baed1fb6cb29011fb8f81522c30";
	
	private static AppContext mInstance;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private BMapManager mBMapManager;
    private PathSearchResult pathSearchResult;
    
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		instanceBMapManager();
	}
	
	public static AppContext getInstance() {
		return mInstance;
	}
	
	public SharedPreferencesUtils getSharedPreferencesUtils() {
		if(sharedPreferencesUtils==null){
			sharedPreferencesUtils=new SharedPreferencesUtils(this);
		}
		return sharedPreferencesUtils;
	}
	
	public BMapManager instanceBMapManager(){
		AppContext.getInstance().m_bKeyRight=true;
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(this);
	        if (!mBMapManager.init(BAIDUMAPKEY,new MKGeneralListener(){

	        		// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	        		@Override
	            public void onGetNetworkState(int iError) {
	                if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                    AppContext.getInstance().makeTextLong("您的网络出错啦！");
	                }
	                else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                    AppContext.getInstance().makeTextLong("输入正确的检索条件！");
	                }
	            }

	            @Override
	            public void onGetPermissionState(int iError) {
	                if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
	                    //授权Key错误
	                    AppContext.getInstance().makeTextLong("请输入正确的百度地图授权Key！");
	                    AppContext.getInstance().m_bKeyRight = false;
	                }
	            }
	        	
	        })) {
	            makeTextLong("BMapManager  初始化错误!");
	        }
        }
		return mBMapManager;
	}
	
	public PathSearchResult getPathSearchResult() {
		return pathSearchResult;
	}

	public void setPathSearchResult(PathSearchResult pathSearchResult) {
		this.pathSearchResult = pathSearchResult;
	}

	public void makeTextShort(String text){
		Toast.makeText(AppContext.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    
    public void makeTextLong(String text){
    		Toast.makeText(AppContext.getInstance().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    
    /**
	 * 重新登陆
	 */
	public void reLogin(Activity activity,String exitmsg){
		Bundle bundle=new Bundle();
		Intent intent=new Intent(activity,LoginActivity.class);
		bundle.putBoolean(Constant.BundleConstant.BUNLE_AUTOLOGINFLAG, false);
		if(exitmsg!=null){
			bundle.putString(Constant.BundleConstant.BUNLE_EXIT_MSG,exitmsg);
		}
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
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
		//缓存标签
		final String CACHE_TAG=String.format("%s-%s","",TAGName);
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
							Map<String,String> infoHead=mapXML.get(XMLUtils.RequestXmLConstant.INFO).get(0);
							if(XMLUtils.RequestXmLConstant.SUCCESSCODE.equals(infoHead.get(XMLUtils.RequestXmLConstant.CODE))){
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
							}else if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals(XMLUtils.RequestXmLConstant.LOGINERROR)){
//								reLogin(activity,infoHead.get(Constant.RequestXmLConstant.MSG));
								reLogin(activity, "登录超时");
							}else if("110042".equals(infoHead.get(XMLUtils.RequestXmLConstant.CODE))){
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
								makeTextLong(infoHead.get(XMLUtils.RequestXmLConstant.MSG));
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
    
	public void sendNetRequest(Activity activity,final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable){
		sendNetRequest(activity,getString(R.string.app_pleasewait),Url, params,headerParams, uiRunnable,null);
	}
	
	public void sendNetRequest(final Activity activity,final String tipMsg,final String Url,final Map<String,String> params,final Map<String,String> headerParams,final UIRunnable uiRunnable,final Map<String,String> customInfo){
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
						final Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(HttpUtils.requestServer(requestHeader, requestContent));
						if(!mapXML.isEmpty()){
							Map<String,String> infoHead=mapXML.get(XMLUtils.RequestXmLConstant.INFO);
							if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals(XMLUtils.RequestXmLConstant.SUCCESSCODE)){
								for(String key:mapXML.keySet()){
									uiRunnable.setInfoContent(mapXML.get(key));
									break;
								}
								uiRunnable.setAllInfoContent(mapXML);
								uiRunnable.run();
							}else if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals(XMLUtils.RequestXmLConstant.LOGINERROR)){
								reLogin(activity, "登录超时");
							}else if(infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals("110036")||
									infoHead.get(XMLUtils.RequestXmLConstant.CODE).equals("120020")){
								//用户名或密码有误则清除密码
//								getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
								makeTextLong("用户名或密码有误");
							}else{
								if(customInfo!=null){
									String message=customInfo.get(infoHead.get(XMLUtils.RequestXmLConstant.CODE));
									if(message!=null){
										makeTextLong(message);
										return;
									}
								}
								makeTextLong(infoHead.get(XMLUtils.RequestXmLConstant.MSG));
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
	
	public MyLocation getMyLocation() {
		MyLocation myLocation = new MyLocation("LIB", 1, new GeoPoint(76.740133,-129.009255), null);
		return myLocation;
	}
	
	
}