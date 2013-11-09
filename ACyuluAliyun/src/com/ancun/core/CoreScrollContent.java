package com.ancun.core;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.ancun.yulualiyun.AppContext;
import com.ancun.yulualiyun.MainActivity;

/**
 * 主体内容区核心父类
 * @author Start
 */
public abstract class CoreScrollContent {
	/**
	 * 类标签名
	 */
	protected final String TAG=this.getClass().getSimpleName();
	/**
	 * 当前Activity
	 */
	private Activity activity;
	/**
	 * 上下文对象
	 */
	private Context context;
	
	private LayoutInflater layoutInflater;
	/**
	 * 当前布局视图
	 */
	private View layoutView;
	/**
	 * 是否打开时刷新数据
	 */
	public Boolean isOpenRefreshData=false;
	/**
	 * 内容缓存对象
	 */
	private final static  Map<Class<?>,CoreScrollContent> contentCache=new HashMap<Class<?>,CoreScrollContent>();
	
	public CoreScrollContent(Activity activity, int resourceID) {
		this.activity = activity;
		this.context = activity;
		layoutInflater = LayoutInflater.from(context);
		layoutView = layoutInflater.inflate(resourceID, null);
		contentCache.put(this.getClass(), this);
	}
	
	protected AppContext getAppContext(){
		return (AppContext)this.activity.getApplication();
	}
	
	protected View findViewById(int id) {
		return layoutView.findViewById(id);
	}

	public void overridePendingTransition(int enterAnim, int exitAnim) {
		activity.overridePendingTransition(enterAnim, exitAnim);
	}

	public void startActivity(Intent intent) {
		activity.startActivity(intent);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
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
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CoreScrollContent.this.processMessage(msg);
		}
	};
	/**
	 * 处理handler消息
	 */
	protected  void processMessage(Message msg){}
	/**
	 * 销毁时调用
	 */
	public void finish(){}

	public Activity getActivity() {
		return activity;
	}

	public Context getContext() {
		return context;
	}
	
	public View getLayoutView() {
		return layoutView;
	}

	public LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	public static Map<Class<?>, CoreScrollContent> getContentcache() {
		return contentCache;
	}
	
	public MainActivity getMainActivity(){
		return (MainActivity)activity;
	}
	
	public void onPause(){
	}
	
	public void onDestroy(){
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
	}
	
}