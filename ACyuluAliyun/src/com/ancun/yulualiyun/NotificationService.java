package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ancun.core.Constant;
import com.ancun.model.MessageModel;
import com.ancun.service.MessageService;
import com.ancun.utils.HttpUtils;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.XMLUtils;

public class NotificationService extends Service{

	private static String TAG=NotificationService.class.getSimpleName();
	
	private Timer timer;
	
	private NotificationManager mNotificationManager;
	
	private MessageService messageService;
	
	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		messageService=new MessageService(this);
		timer= new Timer();
		timer.schedule(new GetNetDataTask(),1000,60*60*1000);
		timer.schedule(new ShowMessageTask(),1000,60*60*1000);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private class GetNetDataTask extends TimerTask {

		private boolean flag = false;
		
		@Override
		public void run() {
			if(flag) {
				return;
			}
			flag=false;
			if(NetConnectManager.isNetWorkAvailable(NotificationService.this)){
				flag = true;
				new GetNetDataThread().start();
	    	}
		}

		private class GetNetDataThread extends Thread {
			public void run() {
				try{
					Map<String,String> requestParams=new HashMap<String,String>();
					requestParams.put("categoryid","3");
					String requestContent = XMLUtils.builderRequestXml(Constant.GlobalURL.v4Message, requestParams);
					Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(HttpUtils.requestServer(null, requestContent));
					Map<String,String> infoHead=mapXML.get(Constant.RequestXmLConstant.INFO).get(0);
					if(Constant.RequestXmLConstant.SUCCESSCODE.equals(infoHead.get(Constant.RequestXmLConstant.CODE))){
						for(Map<String,String> content:mapXML.get("datalist")){
							if(messageService.findByCode(content.get(""))==null){
								MessageModel message=new MessageModel();
								message.setCode(content.get(""));
								message.setTitle(content.get(""));
								message.setContent(content.get(""));
								message.setShowTime(content.get(""));
								message.setReadFlag(1);
								messageService.save(message);
							}
						}
					}
				}catch(Exception e){
					Log.e(TAG,e.getMessage());
				}finally{
					flag=false;
				}
			}
		}

	}
	private class ShowMessageTask extends TimerTask {

		private boolean flag = false;
		
		@Override
		public void run() {
			if(flag) {
				return;
			}
			flag=false;
			if(NetConnectManager.isNetWorkAvailable(NotificationService.this)){
				flag = true;
				new ShowMessageThread().start();
	    	}
		}

		private class ShowMessageThread extends Thread {
			@SuppressWarnings("deprecation")
			public void run() {
				try{
					List<MessageModel> messages=messageService.getThisTimeShowMessageList();
					for(MessageModel message:messages){
						int icon = R.drawable.ic_launcher;
				        CharSequence tickerText = message.getTitle();
				        Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
				        notification.flags = Notification.FLAG_AUTO_CANCEL;
				        //定义下拉通知栏时要展现的内容信息
				        CharSequence contentTitle = message.getTitle();
				        CharSequence contentText = message.getContent();
				        Bundle bundle=new Bundle();
				        Intent notificationIntent = new Intent(NotificationService.this, MessageActivity.class);
				        bundle.putString(MessageActivity.MESSAGECODE, message.getCode());
				        notificationIntent.putExtras(bundle);
				        PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this, 0,notificationIntent, 0);
				        notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText,contentIntent); 
				        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
				        mNotificationManager.notify(1, notification);
					}
				}catch(Exception e){
					Log.e(TAG,e.getMessage());
				}finally{
					flag=false;
				}
			}
		}

	}
}
