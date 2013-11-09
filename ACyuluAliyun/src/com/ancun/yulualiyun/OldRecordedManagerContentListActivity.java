package com.ancun.yulualiyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnItemClickListener;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.utils.HttpUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.StringUtils;
import com.ancun.utils.TimeUtils;
import com.ancun.utils.XMLUtils;
import com.ancun.yulualiyun.AppContext.LoadMode;
/**
 * 旧版通话记录详细列表
 * @author Start
 */
public class OldRecordedManagerContentListActivity extends CoreActivity implements View.OnClickListener{

	static final int TAOBAOREQUESTCODE=0xAC001;
	static final int NOTARYREQUESTCODE=0xAC002;
	static final int REMARKREQUESTCODE=0xAC003;

	static final int TAOBAORESULTCODE=0xAB004;
	static final int NOTARYRESULTCODE=0xAB005;
	
	static final String RECORDED_FILENO="fileno";
	static final String RECORDED_TIME="begintime";
	static final String RECORDED_REMARK="remark";
	static final String RECORDED_DURATION="duration";
	static final String RECORDED_CEFFLAG="cerflag";
	static final String RECORDED_ACCSTATUS="accstatus";
	
	//播放暂停
	public static final int PLAY=0xAC0005;
	//准备播放
	public static final int PLAYERPREPARE=0xAC0004;
	//播放进度条
	public static final int UPDATEPROGRSS=0xAC0006;

	private MediaPlayer mediaPlayer;

	//播放区域的布局
	public RelativeLayout playerLayout;
	//已经播放的时间
	private TextView tvPlayTime;
	private TextView tvDurationTime; 
	private ImageButton btnrecorded_main_detaillist_btn_play;
	//提取码申请
	private ImageButton imAppealTaobo;
	//申请公证
	private ImageButton imAppealNotary;
	//定义进度条
	private SeekBar sbPlayerProgress;
	//当前播放的点
	private int currentPosition;
	//默认播放的view
	private View defaultPlayView=null;
	
	private PullListViewData recordedManagerContentListPullListViewData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorded_manager_content_list);
		btnrecorded_main_detaillist_btn_play=(ImageButton)findViewById(R.id.recorded_main_detaillist_btn_play);
		btnrecorded_main_detaillist_btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vh==null) {
					defaultPlay(defaultPlayView,0);
				}else {
					sendEmptyMessage(PLAY);
				}
			}
		});
		imAppealTaobo=(ImageButton)findViewById(R.id.recorded_main_detaillist_taobao_appeal);
		imAppealTaobo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileno=String.valueOf(v.getTag());
				for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
					if(content.get(RECORDED_FILENO).equals(fileno)){
						Integer accstatus=Integer.parseInt(content.get(RECORDED_ACCSTATUS));
						//淘申诉
						Bundle bundleTaobao=new Bundle();
						bundleTaobao.putString(RECORDED_FILENO, fileno);
						bundleTaobao.putInt(RECORDED_ACCSTATUS, accstatus);
						Intent intentTaobao;
						if(accstatus==1){
							//查看
							intentTaobao=new Intent(OldRecordedManagerContentListActivity.this,RecordedAppealTaobaoExtractionCode.class);
						}else{
							//生成
							bundleTaobao.putInt("appeal_type", 1);
							intentTaobao=new Intent(OldRecordedManagerContentListActivity.this,RecordedAppealConfirmActivity.class);
						}
						intentTaobao.putExtras(bundleTaobao);
						startActivityForResult(intentTaobao, TAOBAOREQUESTCODE);
						break;
					}
				}
			}
		});
		imAppealNotary=(ImageButton)findViewById(R.id.recorded_main_detaillist_notarization_appeal);
		imAppealNotary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileno=String.valueOf(v.getTag());
				for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
					if(content.get(RECORDED_FILENO).equals(fileno)){
						Integer cerflag=Integer.parseInt(content.get(RECORDED_CEFFLAG));
						//申请公证
						Bundle bundleNotary=new Bundle();
						bundleNotary.putInt("appeal_type", 2);
						bundleNotary.putString(RECORDED_FILENO,fileno);
						bundleNotary.putInt(RECORDED_CEFFLAG, cerflag);
						Intent intentNotary=new Intent(OldRecordedManagerContentListActivity.this,RecordedAppealConfirmActivity.class);
						intentNotary.putExtras(bundleNotary);
						startActivityForResult(intentNotary,NOTARYREQUESTCODE);
						break;
					}
				}
			}
		});
		setNavigationTitle("使用原时长套餐所保全录音");
//		if(oppno!=null){
			recordedManagerContentListPullListViewData=new PullListViewData(this);
			recordedManagerContentListPullListViewData.setOnLoadDataListener(
					new OnLoadDataListener(){

						@Override
						public void LoadData(LoadMode loadMode) {
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("accessid",Constant.ACCESSID);
							requestParams.put("calltype","1");
							requestParams.put("rectype", "2");
							requestParams.put("callerno","");
							requestParams.put("calledno","");
							requestParams.put("begintime","");
							requestParams.put("endtime","");
							requestParams.put("durmin","");
							requestParams.put("durmax","");
							requestParams.put("licno","");
							requestParams.put("remark","");
							requestParams.put("ordersort","desc");
							recordedManagerContentListPullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4recQry,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
								}
							},"reclist",TAG);
						}
						
					});
			recordedManagerContentListPullListViewData.start(R.id.recorded_manager_content_list_pulltorefreshlistview, 
					new RecordedAdapter(recordedManagerContentListPullListViewData),new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
							((RecordedAdapter)recordedManagerContentListPullListViewData.getAdapter()).setSelectedPosition(position-1);
			        		vh=(ViewHolder)view.getTag();
			        		if(vh!=null){
			        			if(vh.accstatus==1){
			        				imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
			        			}else{
			        				imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
			        			}
			        			imAppealTaobo.setTag(vh.fileno);
			        			if(vh.cerflag==1){
			        				imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
			        			}else{
			        				imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
			        			}
			        			imAppealNotary.setTag(vh.fileno);
			        			if(vh.file.exists()){
			        				//如果文件存在则直接准备播放
			        				Message msg=new Message();
			        				msg.what=PLAYERPREPARE;
			        				msg.obj=vh.file;
			        				sendMessage(msg);
			        			}else{
			        				if(NetConnectManager.isMobilenetwork(OldRecordedManagerContentListActivity.this)){
			        					final String fileno=vh.fileno;
			        					new AlertDialog.Builder(OldRecordedManagerContentListActivity.this)
			        					.setIcon(android.R.drawable.ic_dialog_info)
			        					.setTitle("提示！")
			        					.setMessage("即将通过移动网络加载数据，为了节约流量，推荐您使用WIFI无线网络!")
			        					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			        						public void onClick(DialogInterface dialog, int whichButton) {
			        							new DownloadTask().execute(fileno);
			        						}
			        					}).setNegativeButton("设置", new DialogInterface.OnClickListener() {
			        						public void onClick(DialogInterface dialog, int whichButton) {
			        							Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
			        							startActivity(netIntent);
			        						}
			        					}).show();
			        				}else{
			        					new DownloadTask().execute(vh.fileno);
			        				}
			        			}
			        		}
						}
					});
			recordedManagerContentListPullListViewData.getPulllistview().setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
					final ViewHolder vh=(ViewHolder)view.getTag();
					new AlertDialog.Builder(OldRecordedManagerContentListActivity.this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示！")
					.setMessage("录音删除后将无法恢复，您确定彻底删除？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							final EditText input = new EditText(OldRecordedManagerContentListActivity.this);
							//设置文本输入时为密码状态
							input.setTransformationMethod(PasswordTransformationMethod.getInstance());
							new AlertDialog.Builder(OldRecordedManagerContentListActivity.this)  
			                .setTitle("请输入登录密码")  
			                .setView(input)  
			                .setPositiveButton("确定",  
			                        new DialogInterface.OnClickListener() {  
			  
			                            public void onClick(DialogInterface dialog,  
			                                    int which) {  
			                                final String value = input.getText().toString();
			                                if("".equals(value)){
			                                	makeTextLong("删除录音文件必须输入密码！");
			                                	return;
			                                }
			                                Map<String,String> requestParams=new HashMap<String,String>();
			                                requestParams.put("accessid", Constant.ACCESSID);
			                                requestParams.put("fileno", vh.fileno);
			                                requestParams.put("alteract", "4");
			                                requestParams.put("password", MD5.md5(value));
			                                getAppContext().exeNetRequest(OldRecordedManagerContentListActivity.this,Constant.GlobalURL.v4recAlter,requestParams,null,new UIRunnable() {
			                    				
			                    				@Override
			                    				public void run() {
			                    					for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
			        									if(content.get(RECORDED_FILENO).equals(vh.fileno)){
			        										recordedManagerContentListPullListViewData.getDataItemList().remove(content);
			        										runOnUiThread(new Runnable() {
			        											@Override
			        											public void run() {
			        												recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
			        											}
			        										});
			        										break;
			        									}
			        								}
			                    				}
			                    				
			                    			});
			                            }  
			                        }).setNegativeButton("取消", null).show();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					}).show();
					return false;
				}
			});
			
			//播放区域
			playerLayout=(RelativeLayout)findViewById(R.id.recorded_main_detaillist_rl_player);
			playerLayout.setVisibility(View.GONE);
			//当前播放时长的textview
			tvPlayTime=(TextView)findViewById(R.id.recorded_main_detaillist_tv_playtime); 
			//总播放时长的textview
			tvDurationTime=(TextView)findViewById(R.id.recorded_main_detaillist_tv_totaltime);
			//进度条
			sbPlayerProgress=(SeekBar)findViewById(R.id.recorded_main_detaillist_seekbar);
			sbPlayerProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
					if(mediaPlayer!=null){
						//假设改变源于用户拖动
						if (fromUser) {
							mediaPlayer.seekTo(progress);
						}
					}
					//没有可播放的音乐不能拖动
					if (vh==null) {
						sbPlayerProgress.setProgress(0);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					if(mediaPlayer!=null){
						if(mediaPlayer.isPlaying()){
							mediaPlayer.pause();
						}
					}
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					if(mediaPlayer!=null){
						currentPosition=seekBar.getProgress();
						sendEmptyMessage(PLAY);
					}
				}
			});
			progressUpdateThread.start();
//		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mediaPlayer!=null){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
				btnrecorded_main_detaillist_btn_play.setBackgroundResource(R.drawable.recorded_play_to_play_normal);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if(mediaPlayer!=null){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer=null;
		}
		super.onDestroy();
	}
	/**
	 * 记录适配器
	 * @author Start
	 */
	private class RecordedAdapter extends PullListViewData.DataAdapter{
		
		public RecordedAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}
		
		private int selectedPosition = -1;// 选中的位置

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.recordeddetailitemlayout) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(OldRecordedManagerContentListActivity.this).inflate(R.layout.lvitem_activity_recorded_manager_content_list, null);
				holder = new ViewHolder();
				holder.btnRemark=(ImageButton)convertView.findViewById(R.id.recorded_main_detail_btn_remark);
				holder.tvTime = (TextView) convertView.findViewById(R.id.recorded_main_detail_time);
				holder.tvRemark = (TextView) convertView.findViewById(R.id.recorded_main_detail_remark);
				holder.tvDownloadStatus=(TextView)convertView.findViewById(R.id.recorded_main_download_status);
				holder.taobao_flag=(ImageView)convertView.findViewById(R.id.recorded_main_detail_taobao_flag);
				holder.notary_flag=(ImageView)convertView.findViewById(R.id.recorded_main_detail_notary_flag);
				
				holder.btnRemark.setOnClickListener(OldRecordedManagerContentListActivity.this);
				convertView.setTag(holder);
				//第一个view
				if (position==0) {
					defaultPlayView=convertView;
				}
			}
			Map<String,String> data=recordedManagerContentListPullListViewData.getDataItemList().get(position);
			holder.fileno=data.get(RECORDED_FILENO);
			holder.accstatus=Integer.parseInt(data.get(RECORDED_ACCSTATUS));
			holder.cerflag=Integer.parseInt(data.get(RECORDED_CEFFLAG));
			holder.file=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/"+holder.fileno);
//			holder.tvTime.setText(TimeUtils.customerTimeConvert(data.get(RECORDED_TIME)));
			holder.tvTime.setText(TimeUtils.customerTimeConvert(data.get("oppno")));
			holder.tvRemark.setText(data.get(RECORDED_REMARK));
			holder.btnRemark.setTag(holder);
			//出证申请标志(1:未申请;2:已申请)
			if("1".equals(data.get(RECORDED_CEFFLAG))){
				holder.notary_flag.setImageResource(R.drawable.notary_no);
			}else{
				holder.notary_flag.setImageResource(R.drawable.notary_ok);
			}
			//提取码状态(1:有效;2:无效)
			if("1".equals(data.get(RECORDED_ACCSTATUS))){
				holder.taobao_flag.setImageResource(R.drawable.taobao_ok);
			}else{
				holder.taobao_flag.setImageResource(R.drawable.taobao_no);
			}
			if(holder.file.exists()){
				holder.tvDownloadStatus.setText(TimeUtils.secondConvertTime(Integer.parseInt(data.get(RECORDED_DURATION))));
			}else{
				holder.tvDownloadStatus.setText("未下载");
			}
			if (selectedPosition == position) {
				convertView.setBackgroundResource(R.drawable.list_item_play_color_bg);
			} else {
				convertView.setBackgroundResource(R.drawable.list_item_color_bg);   
			}
			return convertView;
		}
	}
	/**
	 * 视图辅助类
	 * @author Start
	 */
	private class ViewHolder {
		private ImageButton btnRemark;
		private TextView tvTime;
		private TextView tvRemark;
		private TextView tvDownloadStatus;
		private File file;
		private String fileno;
		private Integer cerflag;
		private Integer accstatus;
		private ImageView taobao_flag;
		private ImageView notary_flag;
	}
	/**
	 * 录音文件下载Task
	 * @author Start
	 */
	private class DownloadTask extends AsyncTask<String, Float, File> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// 创建ProgressDialog对象  
			pDialog = new ProgressDialog(OldRecordedManagerContentListActivity.this);  
			// 设置进度条风格，风格为圆形，旋转的  
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			// 设置ProgressDialog 标题  
			pDialog.setTitle("提示");  
			// 设置ProgressDialog提示信息  
			pDialog.setMessage("录音文件下载");  
			// 设置ProgressDialog标题图标  
			//            pDialog.setIcon(R.drawable.img2);  
			// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确  
			pDialog.setIndeterminate(false);  
			// 设置ProgressDialog 进度条进度  
			pDialog.setProgress(100);
			// 设置ProgressDialog 是否可以按退回键取消  
			pDialog.setCancelable(true);
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
			final String fileno=params[0];
			//主目录创建
			File dirFile=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/");
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File downFile=new File(dirFile,fileno);
			if(!downFile.exists()){
				try{
					File tmpFile=new File(dirFile,fileno+".tmp");
					Map<String,String> pars=new HashMap<String,String>();
					pars.put("accessid",Constant.ACCESSID);
					pars.put(RECORDED_FILENO,fileno);
					if(tmpFile.exists()){
						tmpFile.delete();
					}
					String requestContent = XMLUtils.builderRequestXml(Constant.GlobalURL.v4recDown, pars);
					Map<String,String> requestHeader=new HashMap<String,String>();
					requestHeader.put("sign",StringUtils.signatureHmacSHA1(MD5.md5(requestContent),Constant.ACCESSKEY));
					HttpResponse response=HttpUtils.requestDownServer(requestHeader, requestContent);
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
							makeTextLong(e.getMessage());
						}finally{
							if(fos!=null){
								try {
									fos.flush();
								} catch (IOException e) {
									makeTextLong(e.getMessage());
								}finally{
									try {
										fos.close();
									} catch (IOException e) {
										makeTextLong(e.getMessage());
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
									makeTextLong("文件大小不一致");
									tmpFile.delete();
								}
							}
						}
					}else{
						makeTextLong("文件下载失败错误状态码："+response.getStatusLine().getStatusCode());
					}
				}catch(Exception e){
					makeTextLong("糟糕，好像出错了，请稍候再试！");
				}
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
				recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
				Message msg=new Message();
				msg.what=PLAYERPREPARE;
				msg.obj=result;
				sendMessage(msg);
			}
		}
	}
	
	@Override
	public void processMessage(Message msg) {
		switch(msg.what){
		case PLAYERPREPARE:
			File file=(File)msg.obj;
			try {
				playerLayout.setVisibility(View.VISIBLE);
				if(mediaPlayer!=null){
					if(mediaPlayer.isPlaying()){
						mediaPlayer.stop();
					}
					mediaPlayer.release();
					mediaPlayer=null;
				}
				mediaPlayer=new MediaPlayer();
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						currentPosition=0;
						sbPlayerProgress.setProgress(currentPosition);
						tvPlayTime.setText(TimeUtils.convertTime(currentPosition));
						btnrecorded_main_detaillist_btn_play.setBackgroundResource(R.drawable.recorded_play_to_play_normal);
					}
				});
				mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (Exception e) {
				makeTextLong(e.getMessage());
			}
			currentPosition=0;
			sbPlayerProgress.setProgress(currentPosition);
			sbPlayerProgress.setMax(mediaPlayer.getDuration());
			tvPlayTime.setText(TimeUtils.convertTime(currentPosition));
			tvDurationTime.setText(TimeUtils.convertTime(mediaPlayer.getDuration()));
			btnrecorded_main_detaillist_btn_play.setBackgroundResource(R.drawable.recorded_play_to_stop_normal);
			break;
		case PLAY:
			if(mediaPlayer!=null){
				if(mediaPlayer.isPlaying()){
					mediaPlayer.pause();
					btnrecorded_main_detaillist_btn_play.setBackgroundResource(R.drawable.recorded_play_to_play_normal);
				}else{
					if(currentPosition>0){
						mediaPlayer.seekTo(currentPosition);
					}
					mediaPlayer.start();
					btnrecorded_main_detaillist_btn_play.setBackgroundResource(R.drawable.recorded_play_to_stop_normal);
				}
			}
			break;
		case UPDATEPROGRSS:
			if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
				currentPosition=mediaPlayer.getCurrentPosition();
				sbPlayerProgress.setProgress(currentPosition);
				//显示当前播放时间
				tvPlayTime.setText(TimeUtils.convertTime(currentPosition));
			}
			break;
		}
	}
	/**
	 * 每隔一秒更新播放进度
	 */
	private Thread progressUpdateThread=new Thread(){
		public void run() {
			while(true){
				try {
					sendEmptyMessage(UPDATEPROGRSS);
					Thread.sleep(200);
				} catch (Exception e) {
					makeTextLong(e.getMessage());
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		ViewHolder vh=(ViewHolder)v.getTag();
		Intent intent=new Intent();
		Bundle bundle=new Bundle();
		bundle.putString(RECORDED_FILENO,vh.fileno);
		intent.setClass(OldRecordedManagerContentListActivity.this, RecordedRemark.class);
		intent.putExtras(bundle);
		startActivityForResult(intent,REMARKREQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null&&recordedManagerContentListPullListViewData.getAdapter()!=null){
			if(requestCode==TAOBAOREQUESTCODE){
				if(resultCode==RecordedAppealTaobaoExtractionCode.RecordedAppealTaobaoExtractionCodeResultCode){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RECORDED_FILENO);
						Integer accstatus=bundle.getInt(RECORDED_ACCSTATUS);
						for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_ACCSTATUS, accstatus+"");
								recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
								if(accstatus==1){
									imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
								}else{
									imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
								}
								break;
							}
						}
					}
				}
			}else if(requestCode==NOTARYREQUESTCODE){
				if(resultCode==3){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RECORDED_FILENO);
						Integer cerflag=bundle.getInt(RECORDED_CEFFLAG);
						for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_CEFFLAG, cerflag+"");
								recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
								if(cerflag==1){
									imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
								}else{
									imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
								}
								break;
							}
						}
					}
				}
			}else if(requestCode==REMARKREQUESTCODE){
				if(resultCode==RecordedRemark.REMARKRESULTCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RECORDED_FILENO);
						Integer cerflag=bundle.getInt(RECORDED_CEFFLAG);
						Integer accstatus=bundle.getInt(RECORDED_ACCSTATUS);
						String remark=bundle.getString(RECORDED_REMARK);
						for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_CEFFLAG, cerflag+"");
								content.put(RECORDED_ACCSTATUS, accstatus+"");
								content.put(RECORDED_REMARK, remark);
								recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
								if(accstatus==1){
									imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
								}else{
									imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
								}
								if(cerflag==1){
									imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
								}else{
									imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
								}
								break;
							}
						}
					}
				}else if(resultCode==RecordedRemark.REMARKMODIFYCODE){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RECORDED_FILENO);
						String remark=bundle.getString(RECORDED_REMARK);
						for(Map<String,String> content:recordedManagerContentListPullListViewData.getDataItemList()){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_REMARK, remark);
								recordedManagerContentListPullListViewData.getAdapter().notifyDataSetChanged();
								break;
							}
						}
					}
				}
			}
		}
	}

	private ViewHolder vh;

	public void defaultPlay(View view,int pos) {
		if(recordedManagerContentListPullListViewData.getAdapter()!=null&&view!=null){
			vh=(ViewHolder)view.getTag();
			if(vh!=null){
				((RecordedAdapter)recordedManagerContentListPullListViewData.getAdapter()).setSelectedPosition(pos);
				recordedManagerContentListPullListViewData.getAdapter().notifyDataSetInvalidated();
				if(vh.accstatus==1){
					imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
				}else{
					imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
				}
				imAppealTaobo.setTag(vh.fileno);
				if(vh.cerflag==1){
					imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
				}else{
					imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
				}
				imAppealNotary.setTag(vh.fileno);
				if(vh.file.exists()){
					//如果文件存在则直接准备播放
					Message msg=new Message();
					msg.what=PLAYERPREPARE;
					msg.obj=vh.file;
					sendMessage(msg);
				}else{
					if(NetConnectManager.isMobilenetwork(this)){
						final String fileno=vh.fileno;
						new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示！")
						.setMessage("当前网络环境为移动网络模式，是否确认下载!")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								new DownloadTask().execute(fileno);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).show();
					}else{
						new DownloadTask().execute(vh.fileno);
					}
				}
			}
		}
	}
	
}