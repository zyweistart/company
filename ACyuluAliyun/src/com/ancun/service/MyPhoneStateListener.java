package com.ancun.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.widget.DialFloatView;
import com.ancun.yulualiyun.AppContext;
import com.ancun.yulualiyun.MainActivity;
import com.ancun.yulualiyun.R;

/**
 * 电话监听
 */
public class MyPhoneStateListener extends PhoneStateListener {
	
	private static DialFloatView myFV;
	
	private ImageView ivClose;
	private WindowManager wm;
	
	private AppContext appContext;
	
	public MyPhoneStateListener(AppContext appContext){
		this.appContext=appContext;
	}
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		String callDial=appContext.getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, Constant.EMPTYSTR);
		switch (state) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
			if(!Constant.EMPTYSTR.equals(callDial)){
				if(myFV==null){
					myFV=new DialFloatView(appContext);
				    	//获取WindowManager
				    	wm=(WindowManager)appContext.getSystemService(Context.WINDOW_SERVICE);
				    	View view=LayoutInflater.from(appContext).inflate(R.layout.common_diallistener, null);
				    	ivClose=(ImageView)view.findViewById(R.id.dial_listener_btn_close);
	                    ivClose.setOnClickListener(new View.OnClickListener() {		
							@Override
							public void onClick(View v) {
								if(wm!=null&&myFV!=null){
							        	//在程序退出(Activity销毁）时销毁悬浮窗口
							        	wm.removeView(myFV);
							        	myFV=null;
						        }
							}
						});
				    	TextView tvMessage=(TextView)view.findViewById(R.id.dial_listener_text);
				    	tvMessage.setText("您正在通过安存语录与"+callDial+"录音通话中…");
				    	myFV.addView(view);
			        //设置LayoutParams(全局变量）相关参数
				    	WindowManager.LayoutParams wmParams = appContext.getMywmParams();
				    	//设置window type
			        wmParams.type=LayoutParams.TYPE_PHONE;
			        //设置图片格式，效果为背景透明
			        wmParams.format=PixelFormat.RGBA_8888;
			        //设置Window flag
			        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
					//下面的flags属性的效果形同“锁定”。
					//悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
					//wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL 
					//				   | LayoutParams.FLAG_NOT_FOCUSABLE
					//				   | LayoutParams.FLAG_NOT_TOUCHABLE;
			        //调整悬浮窗口至左上角
			        wmParams.gravity=Gravity.LEFT|Gravity.TOP;
			        //以屏幕左上角为原点，设置x、y初始值
			        wmParams.x=0;
			        wmParams.y=0;
			        //设置悬浮窗口长宽数据
			        wmParams.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			        wmParams.height=android.view.ViewGroup.LayoutParams.WRAP_CONTENT;;
			        //显示myFloatView图像
			        wm.addView(myFV, wmParams);
				}
			}
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			if(!Constant.EMPTYSTR.equals(callDial)){
				if(wm!=null&&myFV!=null){
			        	//在程序退出(Activity销毁）时销毁悬浮窗口
			        	wm.removeView(myFV);
			        	myFV=null;
		        }
			}
			appContext.getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, Constant.EMPTYSTR);
			if(appContext.getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false)){
				ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
				ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
				if(!cn.getClassName().equals(MainActivity.class.getName())){
					//Android4.0以上系统默认打完电话后会跳转到
					//{act=android.intent.action.VIEW typ=vnd.android.cursor.dir/calls cmp=com.android.contacts/.activities.DialtactsActivity u=0}
					Intent intent=new Intent(appContext,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					appContext.startActivity(intent);
				}
			}
			break;
		}
		
	}

}
