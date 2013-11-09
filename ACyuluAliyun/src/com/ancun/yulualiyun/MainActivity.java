package com.ancun.yulualiyun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.Constant.Auth;
import com.ancun.core.CoreActivity;
import com.ancun.core.CoreScrollContent;
import com.ancun.model.UIRunnable;
import com.ancun.utils.LogUtils;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.PasswordVerityUtils;
import com.ancun.utils.TimeUtils;
import com.ancun.widget.DialFloatView;
import com.ancun.widget.ScrollLayout;
import com.ancun.yulualiyun.AppContext.LoadMode;
import com.ancun.yulualiyun.content.ContactContent;
import com.ancun.yulualiyun.content.DialContent;
import com.ancun.yulualiyun.content.RecentContent;
import com.ancun.yulualiyun.content.RecordedManagerContent;

/**
 * @author Start
 */
public class MainActivity extends CoreActivity implements ScrollLayout.LayoutChangeListener, OnClickListener {
	
	private TelephonyManager manager;
	private InputMethodManager imManager; 
	
	private ImageButton activity_main_ibmore; 
	private Button activity_main_btnacount,activity_main_btnnetworktip;
	private TextView tab1,tab2,tab3,tab4;
	private ImageView top_bar_select;
	private ScrollLayout activity_main_scrolllayout;

	private DialContent dialContent;
	private RecentContent recentContent;
	private ContactContent contactContent;
	private RecordedManagerContent recordedManagerContent;
	
	private TextView main_tip_content;
	
	private long lastPressTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main_tip_content=(TextView)findViewById(R.id.main_tip_content);
//		main_tip_content.setVisibility(View.VISIBLE);
		main_tip_content.setText("欢迎使用安存@记忆管理产品服务");
		//我的账户
		activity_main_btnacount=(Button)findViewById(R.id.activity_main_btnacount); 
		activity_main_btnacount.setOnClickListener(this);
		//如果不是主账户则隐藏
		if(!"1".equals(getAppContext().getUserInfo().get("masterflag"))){
			activity_main_btnacount.setVisibility(View.GONE);
		}
		//更多
		activity_main_ibmore=(ImageButton)findViewById(R.id.activity_main_ibmore); 
		activity_main_ibmore.setOnClickListener(this);
		//拨号盘标签
		tab1 = (TextView) findViewById(R.id.tab1);
		//最近通话标签
		tab2 = (TextView) findViewById(R.id.tab2);
		//联系人标签
		tab3 = (TextView) findViewById(R.id.tab3);
		//我的录音标签
		tab4 = (TextView) findViewById(R.id.tab4);
		//标签选择时
		top_bar_select = (ImageView) findViewById(R.id.top_bar_select);
		//网络连接
		activity_main_btnnetworktip=(Button)findViewById(R.id.activity_main_btnnetworktip);
		activity_main_btnnetworktip.setOnClickListener(this);
		//主体框架
		activity_main_scrolllayout = (ScrollLayout) findViewById(R.id.activity_main_scrolllayout);
		activity_main_scrolllayout.addChangeListener(this);
		//拨号盘
		dialContent = new DialContent(this, R.layout.content_dial);
		//最近通话
		recentContent = new RecentContent(this, R.layout.content_recent);
		recentContent.isOpenRefreshData=true;
		//联系人
		contactContent = new ContactContent(this, R.layout.content_contact);
		contactContent.isOpenRefreshData=true;
		//我的录音
		recordedManagerContent = new RecordedManagerContent(this,R.layout.content_recordedmanager);
		recordedManagerContent.isOpenRefreshData=true;
		
		activity_main_scrolllayout.addView(dialContent.getLayoutView());
		activity_main_scrolllayout.addView(recentContent.getLayoutView());
		activity_main_scrolllayout.addView(contactContent.getLayoutView());
		activity_main_scrolllayout.addView(recordedManagerContent.getLayoutView());
		//默认选择为第一屏取值为0，1，2  ，3 
		activity_main_scrolllayout.snapToScreen(0);
		
		imManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//电话监听
		manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		manager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
		
		//网络检测
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(NetCheckReceiver.NetCheckFlag){
								activity_main_btnnetworktip.setVisibility(View.GONE);
							}else{
								activity_main_btnnetworktip.setVisibility(View.VISIBLE);
							}
						}
					});
					TimeUtils.sleep(1000);
				}
			}
		}).start();
		//版本检测
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(NetConnectManager.isNetWorkAvailable(MainActivity.this)){
					Map<String,String> requestParams=new HashMap<String,String>();
					requestParams.put("type","6");
					requestParams.put("termtype","7");
					Map<String,String> headerParams=new HashMap<String,String>();
					headerParams.put("sign", "");
					getAppContext().exeNetRequest(MainActivity.this,null,Constant.GlobalURL.versioninfoGet,requestParams,headerParams,new UIRunnable() {
						@Override
						public void run() {
							Integer maxVersion=Integer.parseInt(getInfoContent().get("maxverno"));
							Integer minVersion=Integer.parseInt(getInfoContent().get("minverno"));
							final String url=getInfoContent().get("url");
							int currentVersionCode=getAppContext().getSharedPreferencesUtils().getInteger(Constant.SharedPreferencesConstant.SP_CURRENTVERSIONCODE,0);
							if(minVersion>currentVersionCode){
								handler.post(new Runnable() {
									@Override
									public void run() {
										new AlertDialog.Builder(MainActivity.this)
										.setMessage("您使用的版本过低，请立即升级！否则可能无法正常使用。")
										.setCancelable(false)
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
												startActivity(fIntent);
												finish();
											}
										}).show();
									}
								});
							}else if(maxVersion>currentVersionCode){
								try{
									SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
									Date date= sdf.parse(TimeUtils.getSysdate());
									String datim=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, "");
									if("".equals(datim)){
										datim=TimeUtils.getSysdate();
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, datim);
									}
									Date mydate= sdf.parse(datim);
									long  day=(date.getTime()-mydate.getTime());//(24*60*60*1000);
									//每天检测一次
									if(day>30){
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, TimeUtils.getSysdate());
										handler.post(new Runnable() {
											@Override
											public void run() {
												new AlertDialog.Builder(MainActivity.this)
												.setIcon(android.R.drawable.ic_dialog_info)
												.setMessage("检测到新版本，是否升级？")
												.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int whichButton) {
														new DownloadAppTask().execute(url);
													}
												}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int whichButton) {
														dialog.dismiss();
													}
												}).show();
											}
										});
									}
								}catch(Exception e){
									LogUtils.logError(e);
								}
							}
						}
					},null);
				}
			}
			
		}).start();
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String password=bundle.getString("password");
			//密码修改标记开
			if(password!=null&&!PasswordVerityUtils.verify(null, password)){
				new AlertDialog.Builder(MainActivity.this)
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("您的密码过于简单,存在严重的安全隐患,建议您及时修改 ,以免造成录音被误删等不必要的麻烦和损失。")
				.setPositiveButton("立即修改", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class)); 
					}
				}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).show();
			}
		}
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false)){
			getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false);
		}
	}

	@Override
	public void doChange(int lastIndex, int currentIndex) {
		if (lastIndex != currentIndex) {
			//更改图标
			switch (currentIndex) {
			case 0:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 1:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 2:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 3:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				break;
			default:
				break;
			}
			TranslateAnimation animation = null;
			int tab1_x=0;
			int tab2_x=((LinearLayout) tab2.getParent()).getWidth();
			int tab3_x=((LinearLayout) tab2.getParent()).getWidth()+((LinearLayout) tab3.getParent()).getWidth();
			int tab4_x=((LinearLayout) tab2.getParent()).getWidth()+((LinearLayout) tab3.getParent()).getWidth()+((LinearLayout) tab4.getParent()).getWidth();
			switch (currentIndex) {
			case 0:
				if (lastIndex == 1) {
					animation = new TranslateAnimation(tab2_x,tab1_x,0, 0);
				} else if (lastIndex == 2) {
					animation = new TranslateAnimation(tab3_x,tab1_x , 0, 0);
				}else if (lastIndex == 3) {
					animation = new TranslateAnimation(tab4_x,tab1_x, 0, 0);
				}
				break;
			case 1:
				if (lastIndex ==0) {
					// 左到中
					animation = new TranslateAnimation(tab1_x,tab2_x,0, 0);
				} else if (lastIndex  ==2) {
					// 右到中
					animation = new TranslateAnimation(tab3_x,tab2_x, 0,0);
				}else if (lastIndex == 3) {
					animation = new TranslateAnimation(tab4_x,tab2_x, 0, 0);
				}
				break;
			case 2:
				if (lastIndex == 0) {
					animation = new TranslateAnimation( tab1_x,tab3_x, 0, 0);
				}else if (lastIndex == 1) {
					animation = new TranslateAnimation( tab2_x,tab3_x, 0,0);
				} else if (lastIndex == 3) {
					animation = new TranslateAnimation( tab4_x,tab3_x, 0, 0);
				}
				break;
			case 3:
				if (lastIndex == 0) {
					animation = new TranslateAnimation( tab1_x,tab4_x, 0, 0);
				}else if (lastIndex == 1) {
					animation = new TranslateAnimation( tab2_x,tab4_x, 0, 0);
				}else if (lastIndex == 2) {
					animation = new TranslateAnimation( tab3_x,tab4_x, 0,0);
				}
				break;
			}
			animation.setDuration(30);
			animation.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.overshoot_interpolator));
 			animation.setFillAfter(true);
 			animation.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.decelerate_interpolator));
			top_bar_select.startAnimation(animation);
		}
		//初始切换面板时进行数据加载
		if(currentIndex==1){
			if(recentContent.isOpenRefreshData){
				//加载最近通话记录
				recentContent.loadData(true);
				recentContent.isOpenRefreshData=false;
			}
		}else if(currentIndex==2){
			if(contactContent.isOpenRefreshData){
				//加载通讯录
				contactContent.loadData(true);
				contactContent.isOpenRefreshData=false;
			}
		}else if(currentIndex==3){
			if(recordedManagerContent.isOpenRefreshData){
				//加载录音数据
				recordedManagerContent.loadData(LoadMode.INIT);
//				recordedManagerContent.getRecordedManagerPullListviewData().getOnLoadDataListener().LoadData(LoadMode.INIT);
				recordedManagerContent.isOpenRefreshData=false;
			}
		}
	}

	@Override
	public void onClick(View v) {
		imManager.hideSoftInputFromWindow(contactContent.getEtSearch().getWindowToken(), 0);
		if (v == tab1.getParent()) {
			activity_main_scrolllayout.snapToScreen(0);
		} else if (v == tab2.getParent()) {
			activity_main_scrolllayout.snapToScreen(1);
		} else if (v == tab3.getParent()) {
			activity_main_scrolllayout.snapToScreen(2);
		} else if (v == tab4.getParent()) {
			if(getAppContext().isAuth(Auth.v4recqry1)){
				activity_main_scrolllayout.snapToScreen(3);
				recordedManagerContent.resetSearch();
			}else{
				makeTextShort("暂无权限");
			}
		}else if(v==activity_main_btnnetworktip){
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		}else if(v==activity_main_ibmore){
			startActivityForResult(new Intent(this, SettingActivity.class), 0);
		}else if(v==activity_main_btnacount){
			getAppContext().setPayBasePackages(false);
			getAppContext().setPackagesinfos(null);
//			if(getAppContext().isOldUserFlag()){
//				//时长老用户
//				startActivity(new Intent(this,AccountMonthConsumeActivity.class));
//			}else{
//				//新套餐用户隐藏我的账户
//				startActivity(new Intent(this,MyAccountActivity.class));
//			}
			//新套餐用户隐藏我的账户
			startActivity(new Intent(this,AccountActivity.class));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onActivityResult(requestCode,resultCode,data);
		}
		if(resultCode== Constant.SETTING_RELOGIN){
			getAppContext().reLogin(this, null);
		}else if(resultCode==Constant.SETTING_QUIT){
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-lastPressTime) > 2000){
	        	makeTextShort("再按一次退出程序");                                
	        	lastPressTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onPause() {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onPause();
		}
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onDestroy();
		}
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.finish();
		}
		super.onDestroy();
	}
	
	public ScrollLayout getMainContentLayout() {
		return activity_main_scrolllayout;
	}

	/**
	 * 远程视图全局变量
	 */
	private static DialFloatView myFV;
	/**
	 * 电话监听
	 */
	public class MyPhoneStateListener extends PhoneStateListener {
		
		private ImageView ivClose;
		private WindowManager wm;
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			String callDial=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, Constant.EMPTYSTR);
			switch (state) {
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if(!Constant.EMPTYSTR.equals(callDial)){
					if(myFV==null){
						myFV=new DialFloatView(getApplicationContext());
					    	//获取WindowManager
					    	wm=(WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
					    	View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.common_diallistener, null);
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
					    	WindowManager.LayoutParams wmParams = ((AppContext)getApplication()).getMywmParams();
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
				getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, Constant.EMPTYSTR);
				if(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false)){
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
					if(!cn.getClassName().equals(MainActivity.class.getName())){
						//Android4.0以上系统默认打完电话后会跳转到
						//{act=android.intent.action.VIEW typ=vnd.android.cursor.dir/calls cmp=com.android.contacts/.activities.DialtactsActivity u=0}
						Intent intent=new Intent(MainActivity.this,MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}
				break;
			}
			
		}

	}
	
}