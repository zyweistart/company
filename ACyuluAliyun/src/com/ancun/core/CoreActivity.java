package com.ancun.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ancun.model.UIRunnable;
import com.ancun.service.ContactService;
import com.ancun.service.RecentService;
import com.ancun.utils.StringUtils;
import com.ancun.yulualiyun.AppContext;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.content.RecordedManagerContent;
import com.baidu.mobstat.StatActivity;
import com.umeng.analytics.MobclickAgent;

public abstract class CoreActivity  extends StatActivity {

	protected final String TAG=this.getClass().getSimpleName();
	
	private RecentService recentService;
	
	private ContactService contactService;
	
	public AppContext getAppContext(){
		return (AppContext)getApplication();
	}
	
	public void makeTextShort(String text) {
		getAppContext().makeTextShort(text);
	}

	public void makeTextLong(String text) {
		getAppContext().makeTextLong(text);
	}
	
	protected void sendMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		sendMessage(msg);
	}
	
	protected void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	protected void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}
	
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			processMessage(msg);
		}
	};
	
	/**
	 * 处理handler消息
	 */
	protected void processMessage(Message msg) {}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!Constant.SYSTEMTEST){
			//测试环境下不做统计日志提交
			MobclickAgent.onResume(this);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(!Constant.SYSTEMTEST){
			//测试环境下不做统计日志提交
			MobclickAgent.onPause(this);
		}
	}

	@Override
	protected void onDestroy() {
		if(contactService!=null){
			contactService.closeSQLiteDBHelper();
		}
		if(recentService!=null){
			recentService.closeSQLiteDBHelper();
		}
		super.onDestroy();
	}
	
	public RecentService getRecentService() {
		if(recentService==null){
			recentService=new RecentService(this);
		}
		return recentService;
	}

	public ContactService getContactService() {
		if(contactService==null){
			contactService=new ContactService(this);
		}
		return contactService;
	}
	
	/**
	 * 设置当前导航栏的标题
	 */
	protected void setNavigationTitle(int resid){
		setNavigationTitle(getResources().getString(resid));
	}
	
	/**
	 * 设置当前导航栏的标题
	 */
	protected void setNavigationTitle(String title){
		TextView tvTitleName=(TextView)findViewById(R.id.common_title_name);
		if(tvTitleName!=null){
			tvTitleName.setText(title);
		}
	}
	
	/**
	 * 刷新用户信息
	 */
	protected void refreshUserInfo(){
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		getAppContext().exeNetRequest(this,Constant.GlobalURL.v4infoGet,requestParams,null,new UIRunnable(){
			@Override
			public void run() {
				getAppContext().setUserInfoAll(getAllInfoContent());
				getAppContext().setUserInfo(getInfoContent());
			}
		});
	}
	
	/**
	 * 应用内拔号
	 */
	public void inAppDial(final String dial) {
		boolean SP_ALIYUN_DIAL_MESSAGE=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_DIAL_MESSAGE,true);
		if(SP_ALIYUN_DIAL_MESSAGE){
			final CheckBox cb=new CheckBox(this);
			cb.setText("不再提示");
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setCancelable(false)
			.setMessage("应用正准备调用你的拨号盘进行拨号通话")
			.setView(cb)
			.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(cb.isChecked()){
						getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_DIAL_MESSAGE,false);
					}
					inAppDial2(dial);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		}else{
			inAppDial2(dial);
		}
	}
	
	private void inAppDial2(final String dial){
		if(Constant.noCall.contains(dial)){
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+dial));
			startActivity(intent);
		}else{
			Map<String,String> requestParams=new HashMap<String,String>();
			final String phone=StringUtils.convertPhone(dial);
			requestParams.put("accessid",Constant.ACCESSID);
			//1:主叫;2:被叫
			requestParams.put("calltype","1");
			requestParams.put("oppno",phone);
			getAppContext().exeNetRequest(this,Constant.GlobalURL.v4Call,requestParams,null,new UIRunnable() {
				
				@Override
				public void run() {
//					ContactService contactService=new ContactService(CoreActivity.this);
//					contactService.dialByModifyContact(phone);
					//重新加载最后通话记录
//					((RecentContent)CoreScrollContent.getContentcache().get(RecentContent.class)).isOpenRefreshData=true;
					//重新加载我的录音列表
					((RecordedManagerContent)CoreScrollContent.getContentcache().get(RecordedManagerContent.class)).isOpenRefreshData=true;
//					((RecordedManagerContent)CoreScrollContent.getContentcache().get(RecordedManagerContent.class)).isLoadCacheData=false;
					//开启通话Activity
					getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_CALL_ISALLOW, false);
					getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, phone);
					getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, true);
					
					String serverno=(Constant.SYSTEMTEST ?Constant.TESTSERVERNO:getInfoContent().get("serverno"));
					getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_SERVER_CALL, serverno);
					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+serverno));
					startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * 应用更新下载
	 */
	public class DownloadAppTask extends AsyncTask<String, Float, File> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// 创建ProgressDialog对象  
			pDialog = new ProgressDialog(CoreActivity.this);  
			// 设置进度条风格，风格为圆形，旋转的  
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			// 设置ProgressDialog 标题  
			pDialog.setTitle("提示");  
			// 设置ProgressDialog提示信息  
			pDialog.setMessage("正在下载安存语录中......");  
			// 设置ProgressDialog标题图标  
			//            pDialog.setIcon(R.drawable.img2);  
			// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确  
			pDialog.setIndeterminate(false);  
			// 设置ProgressDialog 进度条进度  
			pDialog.setProgress(100);
			// 设置ProgressDialog 是否可以按退回键取消  
			pDialog.setCancelable(false);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					onCancelled();
				}
			});
			pDialog.show();
		}

		@Override
		protected void onCancelled() {
			cancel(true);
		}

		@Override
		protected File doInBackground(String... params) {
			final String url=params[0];
			//主目录创建
			File dirFile=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/tmp/");
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File downFile=new File(dirFile,"ancunyulu");
			if(downFile.exists()){
				//如果已经存在则删除原先的文件
				downFile.delete();
			}
			try{
				File tmpFile=new File(dirFile,"ancunyulu"+".tmp");
				if(tmpFile.exists()){
					tmpFile.delete();
				}
				HttpClient client = new DefaultHttpClient();
				//设置超时时间为10秒
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
				HttpPost post = new HttpPost(url);
				HttpResponse response=client.execute(post);
				if(response!=null&&response.getStatusLine().getStatusCode()==200){
					int len=-1;
					long reallen=0;
					//获取下载文件的总大小
					long totallen=response.getEntity().getContentLength();
					InputStream is=null;
					FileOutputStream fos=null;
					byte[] buffer=new byte[1024*8];
					try {
						is=response.getEntity().getContent();
						fos = new FileOutputStream(tmpFile);
						while((len=is.read(buffer))!=-1&&!isCancelled()){
							fos.write(buffer,0,len);
							reallen+=len;
							publishProgress((float)reallen/totallen);
						}
					} catch (Exception e) {
						getAppContext().makeTextLong(e.getMessage());
					}finally{
						if(fos!=null){
							try {
								fos.flush();
							} catch (IOException e) {
								getAppContext().makeTextLong(e.getMessage());
							}finally{
								try {
									fos.close();
								} catch (IOException e) {
									getAppContext().makeTextLong(e.getMessage());
								}finally{
									fos=null;
								}
							}
						}
						buffer=null;
						if(isCancelled()){
							tmpFile.delete();
						}else{
							if(tmpFile.length()==totallen){
								tmpFile.renameTo(downFile);
							}else{
//								getAppContext().makeTextLong("文件大小不一致");
								getAppContext().makeTextLong("下载发生异常，请重试");
								tmpFile.delete();
							}
						}
					}
				}else{
//					getAppContext().makeTextLong("文件下载失败错误状态码："+response.getStatusLine().getStatusCode());
					getAppContext().makeTextLong("下载发生异常，请重试");
				}
			}catch(Exception e){
				getAppContext().makeTextLong("下载发生异常，请重试");
//				getAppContext().makeTextLong("请求出现异常，请稍候再试！");
			}
			return downFile;
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			//更新下载进度条
			pDialog.setProgress(Math.round(values[0]*100f)); 
		}

		@Override
		protected void onPostExecute(File result) {
			pDialog.dismiss();
			if(result.exists()){
				//安装应用
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
				startActivity(intent);
				finish();
			}
		}
	}
	
}