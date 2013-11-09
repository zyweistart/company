package com.ancun.yulualiyun.content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreScrollContent;
import com.ancun.model.ContactModel;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.HttpUtils;
import com.ancun.utils.LogUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.StringUtils;
import com.ancun.utils.TimeUtils;
import com.ancun.utils.XMLUtils;
import com.ancun.widget.PullToRefreshListView;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.RecordedAppealConfirmActivity;
import com.ancun.yulualiyun.RecordedAppealTaobaoExtractionCode;
import com.ancun.yulualiyun.RecordedRemark;
import com.ancun.yulualiyun.AppContext.LoadMode;
/**
 * 录音管理
 * @author Start
 */
public class RecordedManagerContent_bak extends CoreScrollContent implements Filterable{

	
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
	
	private FilterContact mFilter; 
	
	private List<Map<String, String>> mListDataItemsAll=new ArrayList<Map<String,String>>();
	private List<Map<String, String>> mListDataItemsFilter=new ArrayList<Map<String,String>>();
	
	private PullToRefreshListView pulllistview;
	private View listview_footer;
	private TextView listview_footer_more;
	private ProgressBar listview_footer_progress;
	private RecordedAdapter adapter;
	
	private EditText etSearchTxt;
	private ImageButton ibSearchBegin;
	private ImageButton ibSearchClean;
	
	private InputMethodManager inputMethodManager;	
	
	public RecordedManagerContent_bak(final Activity activity, int resourceID) {
		super(activity, resourceID);
		
		inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
		
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
				for(Map<String,String> content:mListDataItemsFilter){
					if(content.get(RECORDED_FILENO).equals(fileno)){
						Integer accstatus=Integer.parseInt(content.get(RECORDED_ACCSTATUS));
						//淘申诉
						Bundle bundleTaobao=new Bundle();
						bundleTaobao.putString(RECORDED_FILENO, fileno);
						bundleTaobao.putInt(RECORDED_ACCSTATUS, accstatus);
						Intent intentTaobao;
						if(accstatus==1){
							//查看
							intentTaobao=new Intent(getActivity(),RecordedAppealTaobaoExtractionCode.class);
						}else{
							//生成
							bundleTaobao.putInt("appeal_type", 1);
							intentTaobao=new Intent(getActivity(),RecordedAppealConfirmActivity.class);
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
				for(Map<String,String> content:mListDataItemsFilter){
					if(content.get(RECORDED_FILENO).equals(fileno)){
						Integer cerflag=Integer.parseInt(content.get(RECORDED_CEFFLAG));
						//申请公证
						Bundle bundleNotary=new Bundle();
						bundleNotary.putInt("appeal_type", 2);
						bundleNotary.putString(RECORDED_FILENO,fileno);
						bundleNotary.putInt(RECORDED_CEFFLAG, cerflag);
						Intent intentNotary=new Intent(getActivity(),RecordedAppealConfirmActivity.class);
						intentNotary.putExtras(bundleNotary);
						startActivityForResult(intentNotary,NOTARYREQUESTCODE);
						break;
					}
				}
			}
		});
			
			listview_footer = getLayoutInflater().inflate(R.layout.common_listview_footer, null);
			listview_footer_more = (TextView)listview_footer.findViewById(R.id.listview_foot_more);
			listview_footer_progress = (ProgressBar)listview_footer.findViewById(R.id.listview_foot_progress);
			listview_footer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadData(LoadMode.FOOT);
				}
			});
			
			adapter=new RecordedAdapter();
			
			pulllistview=(PullToRefreshListView)findViewById(R.id.recorded_manager_content_list_pulltorefreshlistview);
			pulllistview.addFooterView(listview_footer);
			pulllistview.setAdapter(adapter);
			pulllistview.setOnItemClickListener(
					new AdapterView.OnItemClickListener() {
		
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
								//点击头部、底部栏无效
						    		if(position == 0 || view == listview_footer) return;
						    		
						    		defaultPlay(view,position-1);
						    		
//						    		adapter.setSelectedPosition(position-1);
//					        		adapter.notifyDataSetInvalidated();
//					        		ViewHolder vh=(ViewHolder)view.getTag();
//					        		if(vh!=null){
//					        			if(vh.accstatus==1){
//					        				imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
//					        			}else{
//					        				imAppealTaobo.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
//					        			}
//					        			imAppealTaobo.setTag(vh.fileno);
//					        			if(vh.cerflag==1){
//					        				imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
//					        			}else{
//					        				imAppealNotary.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
//					        			}
//					        			imAppealNotary.setTag(vh.fileno);
//					        			if(vh.file.exists()){
//					        				//如果文件存在则直接准备播放
//					        				Message msg=new Message();
//					        				msg.what=PLAYERPREPARE;
//					        				msg.obj=vh.file;
//					        				sendMessage(msg);
//					        			}else{
//					        				if(NetConnectManager.isMobilenetwork(getActivity())){
//					        					final String fileno=vh.fileno;
//					        					new AlertDialog.Builder(getActivity())
//					        					.setIcon(android.R.drawable.ic_dialog_info)
//					        					.setTitle("提示！")
//					        					.setMessage("即将通过移动网络加载数据，为了节约流量，推荐您使用WIFI无线网络!")
//					        					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					        						public void onClick(DialogInterface dialog, int whichButton) {
//					        							new DownloadTask().execute(fileno);
//					        						}
//					        					}).setNegativeButton("设置", new DialogInterface.OnClickListener() {
//					        						public void onClick(DialogInterface dialog, int whichButton) {
//					        							Intent netIntent=new Intent(Settings.ACTION_SETTINGS);
//					        							startActivity(netIntent);
//					        						}
//					        					}).show();
//					        				}else{
//					        					new DownloadTask().execute(vh.fileno);
//					        				}
//					        			}
//					        		}
							}
					}
				);
			pulllistview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,int position,long id) {
					//点击头部、底部栏无效
		    			if(position == 0 || view == listview_footer) return false;
		    			adapter.notifyDataSetInvalidated();
					final ViewHolder vh=(ViewHolder)view.getTag();
					new AlertDialog.Builder(getActivity())
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示！")
					.setMessage("录音删除后将无法恢复，您确定彻底删除？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							final EditText input = new EditText(getActivity());
							//设置文本输入时为密码状态
							input.setTransformationMethod(PasswordTransformationMethod.getInstance());
							new AlertDialog.Builder(getActivity())  
			                .setTitle("请输入登录密码")  
			                .setView(input)  
			                .setPositiveButton("确定",  
			                        new DialogInterface.OnClickListener() {  
			  
			                            public void onClick(DialogInterface dialog,  
			                                    int which) {  
			                                final String value = input.getText().toString();
			                                if("".equals(value)){
			                                getAppContext().makeTextLong("删除录音文件必须输入密码！");
			                                	return;
			                                }
			                                Map<String,String> requestParams=new HashMap<String,String>();
			                                requestParams.put("accessid", Constant.ACCESSID);
			                                requestParams.put("fileno", vh.fileno);
			                                requestParams.put("alteract", "4");
			                                requestParams.put("password", MD5.md5(value));
			                                getAppContext().exeNetRequest(getActivity(),Constant.GlobalURL.v4recAlter,requestParams,null,new UIRunnable() {
			                    				
			                    				@Override
			                    				public void run() {
			                    					for(Map<String,String> content:mListDataItemsFilter){
			        									if(content.get(RECORDED_FILENO).equals(vh.fileno)){
			        										mListDataItemsFilter.remove(content);
			        										getActivity().runOnUiThread(new Runnable() {
			        											@Override
			        											public void run() {
			        												adapter.notifyDataSetChanged();
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
			pulllistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
	            public void onRefresh() {
	            		loadData(LoadMode.HEAD);
		        }
		    });
			//搜索按钮
			ibSearchBegin=(ImageButton)findViewById(R.id.contact_search_bar_icon);
			ibSearchBegin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (inputMethodManager.isActive()) {
						inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			});
			//搜索文本
			etSearchTxt=(EditText)findViewById(R.id.contact_search_bar_content);
			etSearchTxt.setHint(R.string.activity_main_tab_recording_serachhint);
			etSearchTxt.addTextChangedListener(new CustomTextWatcher());
			//清除搜索文本
			ibSearchClean=(ImageButton)findViewById(R.id.contact_search_bar_clean);
			ibSearchClean.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					etSearchTxt.setText("");
					inputMethodManager.hideSoftInputFromWindow(etSearchTxt.getWindowToken(), 0);
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

	private int loadRectypeflag=1;
	
	private List<Map<String, String>> mListDataItems1=new ArrayList<Map<String,String>>();
	private List<Map<String, String>> mListDataItems2=new ArrayList<Map<String,String>>();
	
	public void loadData(final LoadMode loadMode){
		inputMethodManager.hideSoftInputFromWindow(etSearchTxt.getWindowToken(), 0);
		
		if(LoadMode.INIT==loadMode||LoadMode.HEAD==loadMode){
			loadRectypeflag=1;
			mListDataItems2.clear();
			etSearchTxt.setText("");
		}
		
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid",Constant.ACCESSID);
		requestParams.put("calltype","1");
		
		requestParams.put("rectype", String.valueOf(loadRectypeflag));
		requestParams.put("callerno","");
		requestParams.put("calledno","");
		requestParams.put("begintime","");
		requestParams.put("endtime","");
		requestParams.put("durmin","");
		requestParams.put("durmax","");
		requestParams.put("licno","");
		requestParams.put("remark","");
		requestParams.put("ordersort","desc");
		sendPullToRefreshListViewNetRequest(
				getActivity(),
				loadRectypeflag==1?mListDataItems1:mListDataItems2,
				pulllistview,
				listview_footer,
				listview_footer_more,
				listview_footer_progress,
				loadMode,Constant.GlobalURL.v4recQry,requestParams,null,
				new UIRunnable(){
					@Override
					public void run() {
						mListDataItemsAll.clear();
						mListDataItemsAll.addAll(mListDataItems1);
						mListDataItemsAll.addAll(mListDataItems2);
						getFilter().filter(etSearchTxt.getText());
					}
				},"reclist",TAG);
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
	public void onDestroy() {
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
	private class RecordedAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return mListDataItemsFilter.size();
		}

		@Override
		public Object getItem(int position) {
			return  mListDataItemsFilter.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private int selectedPosition = -1;// 选中的位置

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
//			if (convertView != null && convertView.getId() == R.id.recordeddetailitemlayout) {
//				holder = (ViewHolder) convertView.getTag();
//			} else {
//				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.lvitem_activity_recorded_manager_content_list, null);
//				holder = new ViewHolder();
//				holder.btnRemark=(ImageButton)convertView.findViewById(R.id.recorded_main_detail_btn_remark);
//				holder.tvTime = (TextView) convertView.findViewById(R.id.recorded_main_detail_time);
//				holder.tvRemark = (TextView) convertView.findViewById(R.id.recorded_main_detail_remark);
//				holder.tvDownloadStatus=(TextView)convertView.findViewById(R.id.recorded_main_download_status);
//				holder.taobao_flag=(ImageView)convertView.findViewById(R.id.recorded_main_detail_taobao_flag);
//				holder.notary_flag=(ImageView)convertView.findViewById(R.id.recorded_main_detail_notary_flag);
//				
//				holder.btnRemark.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						ViewHolder vh=(ViewHolder)v.getTag();
//						Intent intent=new Intent();
//						Bundle bundle=new Bundle();
//						bundle.putString(RECORDED_FILENO,vh.fileno);
//						intent.setClass(getActivity(), RecordedRemark.class);
//						intent.putExtras(bundle);
//						startActivityForResult(intent,REMARKREQUESTCODE);
//					}
//				});
//				convertView.setTag(holder);
//				//第一个view
//				if (position==0) {
//					defaultPlayView=convertView;
//				}
//			}
//			Map<String,String> data=mListDataItemsFilter.get(position);
//			holder.fileno=data.get(RECORDED_FILENO);
//			holder.accstatus=Integer.parseInt(data.get(RECORDED_ACCSTATUS));
//			holder.cerflag=Integer.parseInt(data.get(RECORDED_CEFFLAG));
//			holder.file=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/"+holder.fileno);
//			holder.tvTime.setText(TimeUtils.customerTimeConvert(data.get(RECORDED_TIME)));
//			
//			String oppno=data.get("oppno");
//			ContactModel contactModel=getMainActivity().getContactService().getContactModelByPhone(oppno);
//			if(contactModel!=null){
//				holder.tvTime.setText(contactModel.getName()+data.get("oppno"));
//			}else{
//				holder.tvTime.setText(data.get("oppno"));
//			}
//			
////			holder.tvTime.setText(TimeUtils.customerTimeConvert(data.get("oppno")));
//			holder.tvRemark.setText(data.get(RECORDED_REMARK));
//			holder.btnRemark.setTag(holder);
//			//出证申请标志(1:未申请;2:已申请)
//			if("1".equals(data.get(RECORDED_CEFFLAG))){
//				holder.notary_flag.setImageResource(R.drawable.notary_no);
//			}else{
//				holder.notary_flag.setImageResource(R.drawable.notary_ok);
//			}
//			//提取码状态(1:有效;2:无效)
//			if("1".equals(data.get(RECORDED_ACCSTATUS))){
//				holder.taobao_flag.setImageResource(R.drawable.taobao_ok);
//			}else{
//				holder.taobao_flag.setImageResource(R.drawable.taobao_no);
//			}
//			if(holder.file.exists()){
//				holder.tvDownloadStatus.setText(TimeUtils.secondConvertTime(Integer.parseInt(data.get(RECORDED_DURATION))));
//			}else{
//				holder.tvDownloadStatus.setText("未下载");
//			}
//			if (selectedPosition == position) {
//				convertView.setBackgroundResource(R.drawable.list_item_play_color_bg);
//			} else {
//				convertView.setBackgroundResource(R.drawable.list_item_color_bg);   
//			}
			
			
			if (convertView != null && convertView.getId() == R.id.recordeditemlayout) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = getLayoutInflater().inflate(R.layout.lvitem_content_recordedmanager2, null);
				holder = new ViewHolder();
				holder.head = (ImageView) convertView.findViewById(R.id.recorded_main_head_img);
				holder.name = (TextView) convertView.findViewById(R.id.recorded_main_name);
				holder.phone = (TextView) convertView.findViewById(R.id.recorded_main_phone);
				holder.from = (TextView) convertView.findViewById(R.id.recorded_main_from);
				holder.tvDownloadStatus=(TextView)convertView.findViewById(R.id.recorded_main_download_status);
				holder.btnRemark=(ImageButton)convertView.findViewById(R.id.recorded_main_detail_btn_remark);
				
				holder.btnRemark.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ViewHolder vh=(ViewHolder)v.getTag();
						Intent intent=new Intent();
						Bundle bundle=new Bundle();
						bundle.putString(RECORDED_FILENO,vh.fileno);
						intent.setClass(getActivity(), RecordedRemark.class);
						intent.putExtras(bundle);
						startActivityForResult(intent,REMARKREQUESTCODE);
					}
				});
				
				//
				convertView.setTag(holder);
				//第一个view
				if (position==0) {
					defaultPlayView=convertView;
				}
			}
			Map<String,String> data=mListDataItemsFilter.get(position);
			ContactModel contactModel=getMainActivity().getContactService().getContactModelByPhone(data.get("oppno"));
			if(contactModel!=null){
				holder.name.setTag(contactModel.getName());
				holder.name.setText(contactModel.getName());
				holder.phone.setText(data.get("oppno"));
				if (contactModel.getPhotoID() > 0) {
					holder.head.setImageBitmap(getMainActivity().getContactService().loadContactPhoto(contactModel.getId()));
				}else{
					holder.head.setImageResource(R.drawable.contact_head);
				}
			}else{
				holder.name.setTag(data.get("oppno"));
				holder.name.setText(data.get("oppno"));
				holder.phone.setText("");
				holder.head.setImageResource(R.drawable.contact_head);
			}
			
			holder.phone.setTag(data.get("oppno"));
			
			holder.fileno=data.get(RECORDED_FILENO);
			holder.accstatus=Integer.parseInt(data.get(RECORDED_ACCSTATUS));
			holder.cerflag=Integer.parseInt(data.get(RECORDED_CEFFLAG));
			holder.file=new File(Environment.getExternalStorageDirectory().getPath()+"/ancun/record/"+holder.fileno);
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
			holder.from.setText(TimeUtils.customerTimeConvert(data.get(RECORDED_TIME)));
			holder.btnRemark.setTag(holder);
			return convertView;
		}
	}
	/**
	 * 视图辅助类
	 * @author Start
	 */
	private class ViewHolder {
		private ImageButton btnRemark;
		private TextView tvDownloadStatus;
		private File file;
		private String fileno;
		private Integer cerflag;
		private Integer accstatus;
		private ImageView head;
		private TextView name;
		private TextView phone;
		private TextView from;
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
			pDialog = new ProgressDialog(getActivity());  
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
							getMainActivity().makeTextLong(e.getMessage());
						}finally{
							if(fos!=null){
								try {
									fos.flush();
								} catch (IOException e) {
									getMainActivity().makeTextLong(e.getMessage());
								}finally{
									try {
										fos.close();
									} catch (IOException e) {
										getMainActivity().makeTextLong(e.getMessage());
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
									getMainActivity().makeTextLong("文件大小不一致");
									tmpFile.delete();
								}
							}
						}
					}else{
						getMainActivity().makeTextLong("文件下载失败错误状态码："+response.getStatusLine().getStatusCode());
					}
				}catch(Exception e){
					getMainActivity().makeTextLong("糟糕，好像出错了，请稍候再试！");
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
				adapter.notifyDataSetChanged();
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
				getMainActivity().makeTextLong(e.getMessage());
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
					getMainActivity().makeTextLong(e.getMessage());
				}
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null&&adapter!=null){
			if(requestCode==TAOBAOREQUESTCODE){
				if(resultCode==RecordedAppealTaobaoExtractionCode.RecordedAppealTaobaoExtractionCodeResultCode){
					Bundle bundle=data.getExtras();
					if(bundle!=null){
						String fileno=bundle.getString(RECORDED_FILENO);
						Integer accstatus=bundle.getInt(RECORDED_ACCSTATUS);
						for(Map<String,String> content:mListDataItemsFilter){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_ACCSTATUS, accstatus+"");
								adapter.notifyDataSetChanged();
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
						for(Map<String,String> content:mListDataItemsFilter){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_CEFFLAG, cerflag+"");
								adapter.notifyDataSetChanged();
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
						for(Map<String,String> content:mListDataItemsFilter){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_CEFFLAG, cerflag+"");
								content.put(RECORDED_ACCSTATUS, accstatus+"");
								content.put(RECORDED_REMARK, remark);
								adapter.notifyDataSetChanged();
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
						for(Map<String,String> content:mListDataItemsFilter){
							if(content.get(RECORDED_FILENO).equals(fileno)){
								content.put(RECORDED_FILENO, fileno);
								content.put(RECORDED_REMARK, remark);
								adapter.notifyDataSetChanged();
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
		if(adapter!=null&&view!=null){
			vh=(ViewHolder)view.getTag();
			if(vh!=null){
				adapter.setSelectedPosition(pos);
				adapter.notifyDataSetInvalidated();
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
					if(NetConnectManager.isMobilenetwork(getActivity())){
						final String fileno=vh.fileno;
						new AlertDialog.Builder(getActivity())
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
	
	@Override
	public Filter getFilter() {
		if (mFilter == null) {  
			mFilter = new FilterContact();  
		}  
		return mFilter;  
	}
	
	private class FilterContact extends Filter {  

		@Override  
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			mListDataItemsFilter.clear();
			if (prefix == null || prefix.length() == 0) {
				//输入为空
				mListDataItemsFilter.addAll(mListDataItemsAll);
			} else {
				String pre=prefix.toString().toLowerCase();
				for(Map<String, String> data:mListDataItemsAll){
					String oppno=data.get("oppno");
					//电话号码
					if(oppno.contains(pre)) {
						mListDataItemsFilter.add(data);
					} else {
						//联系人姓名
						ContactModel contactModel=getMainActivity().getContactService().getContactModelByPhone(oppno);
						if(contactModel!=null){
							String name=contactModel.getName().toLowerCase();
							if(name!=null&&!"".equals(name)){
								if(name.contains(pre)){
									mListDataItemsFilter.add(data);
								}else if(name.equals(pre)){
									mListDataItemsFilter.add(data);
								}else if(contactModel.getPinyinName().contains(pre)){
									mListDataItemsFilter.add(data);
								}
							}
						}
					}
				}
			}
			results.values = mListDataItemsFilter;
			results.count = mListDataItemsFilter.size();
			return results;  
		}  

		@Override  
		protected void publishResults(CharSequence constraint, FilterResults results) {
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private class CustomTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
		@Override
		public void afterTextChanged(Editable s) {
			getFilter().filter(s);
		}
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
		final String CACHE_TAG=String.format("%s-%s", getAppContext().getCurrentLoginPhoneNumber(activity),TAGName);
		if(loadMode==LoadMode.INIT){
			String xmlContent=getAppContext().getSharedPreferencesUtils().getString(CACHE_TAG, Constant.EMPTYSTR);
			if(!StringUtils.isEmpty(xmlContent)){
				try{
					Map<String,List<Map<String,String>>> mapXML=XMLUtils.xmlResolvelist(xmlContent);
					if(!mapXML.isEmpty()){
						listDatas.clear();
						for(Map<String,String> content:mapXML.get(DATALIST)){
							listDatas.add(content);
						}
						
						Integer totalcount=Integer.parseInt(mapXML.get(Constant.RequestXmLConstant.PAGEINFO).get(0).get("totalcount"));
						if(totalcount>listDatas.size()){
							pListView.setTag(Constant.LISTVIEW_DATA_MORE);
							mFooterMoreTitle.setText(R.string.load_more);
						} else {
							//如果已经全部加载完毕
							if("1".equals(getAppContext().getUserInfo().get("oldflag"))){
								pListView.setTag(Constant.LISTVIEW_DATA_MORE);
								mFooterMoreTitle.setText(R.string.load_more);
								loadRectypeflag=2;
							}else{
								pListView.setTag(Constant.LISTVIEW_DATA_FULL);
								mFooterMoreTitle.setText(R.string.load_full);
							}
						}
						
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
					getAppContext().getSharedPreferencesUtils().putString(CACHE_TAG, Constant.EMPTYSTR);
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
					Integer totalcount=0;
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
								totalcount=Integer.parseInt(mapXML.get(Constant.RequestXmLConstant.PAGEINFO).get(0).get("totalcount"));
								final List<Map<String,String>> contents=mapXML.get(DATALIST);
								loadDataNum=contents.size();
								if(loadDataNum>0){
									loadDataFlag=true;
								}
								if(loadDataFlag){
									//只在成功且有数据时才进行缓存
									if(loadMode==LoadMode.INIT||loadMode==LoadMode.HEAD){
										getAppContext().getSharedPreferencesUtils().putString(CACHE_TAG, xmlContent);
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
								getAppContext().reLogin(activity, "登录超时");
							}else if("110042".equals(infoHead.get(Constant.RequestXmLConstant.CODE))){
								//暂无记录
								if(loadMode==LoadMode.HEAD){
									getAppContext().getSharedPreferencesUtils().putString(CACHE_TAG, "");
									
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											listDatas.clear();
											uiRunnable.run();
										}
										
									});
								}
							}else{
								getMainActivity().makeTextLong(infoHead.get(Constant.RequestXmLConstant.MSG));
							}
						}else{
							getMainActivity().makeTextLong(getMainActivity().getString(R.string.app_error_please_try_again));
						}
					}catch(Exception e){
						LogUtils.logError(e);
						getMainActivity().makeTextLong(getMainActivity().getString(R.string.app_error_please_try_again));
					}finally{
						final Boolean flag=loadDataFlag;
						final Integer num=loadDataNum;
						final Integer count=totalcount;
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//如果当前为刷新状态获取的数据为空
								if(flag){
									if(num>=Constant.PAGESIZE){
//										pListView.setTag(Constant.LISTVIEW_DATA_MORE);
//										mFooterMoreTitle.setText(R.string.load_more);
										
										if(count>listDatas.size()){
											pListView.setTag(Constant.LISTVIEW_DATA_MORE);
											mFooterMoreTitle.setText(R.string.load_more);
										} else {
											//如果已经全部加载完毕
											if("1".equals(getAppContext().getUserInfo().get("oldflag"))){
												pListView.setTag(Constant.LISTVIEW_DATA_MORE);
												mFooterMoreTitle.setText(R.string.load_more);
												loadRectypeflag=2;
											}else{
												pListView.setTag(Constant.LISTVIEW_DATA_FULL);
												mFooterMoreTitle.setText(R.string.load_full);
											}
										}
										
									}else{
										//加载完毕
										if("1".equals(getAppContext().getUserInfo().get("oldflag"))){
											if(loadRectypeflag==1){
												pListView.setTag(Constant.LISTVIEW_DATA_MORE);
												mFooterMoreTitle.setText(R.string.load_more);
												loadRectypeflag=2;
											}else if(loadRectypeflag==2){
												pListView.setTag(Constant.LISTVIEW_DATA_FULL);
												mFooterMoreTitle.setText(R.string.load_full);
											}
										}else{
											pListView.setTag(Constant.LISTVIEW_DATA_FULL);
											mFooterMoreTitle.setText(R.string.load_full);
										}
									}
								}else{
									if(loadMode==LoadMode.FOOT){
										//加载完毕
										if("1".equals(getAppContext().getUserInfo().get("oldflag"))){
											if(loadRectypeflag==1){
												pListView.setTag(Constant.LISTVIEW_DATA_MORE);
												mFooterMoreTitle.setText(R.string.load_more);
												loadRectypeflag=2;
											}else if(loadRectypeflag==2){
												pListView.setTag(Constant.LISTVIEW_DATA_FULL);
												mFooterMoreTitle.setText(R.string.load_full);
											}
										}else{
											pListView.setTag(Constant.LISTVIEW_DATA_FULL);
											mFooterMoreTitle.setText(R.string.load_full);
										}
									}else{
										//数据为空
										if("1".equals(getAppContext().getUserInfo().get("oldflag"))){
											if(loadRectypeflag==1){
												pListView.setTag(Constant.LISTVIEW_DATA_MORE);
												mFooterMoreTitle.setText(R.string.load_more);
												loadRectypeflag=2;
											}else if(loadRectypeflag==2){
												pListView.setTag(Constant.LISTVIEW_DATA_EMPTY);
												mFooterMoreTitle.setText(R.string.load_empty);
											}
										}else{
											pListView.setTag(Constant.LISTVIEW_DATA_EMPTY);
											mFooterMoreTitle.setText(R.string.load_empty);
										}
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
	
	
	
	public Map<String,List<Map<String,String>>> getData(final String Url,int currentPage,int pagesize,
			final Map<String,String> params,
			final Map<String,String> headerParams) throws Exception{
		//读取记录标记
		params.put("", String.valueOf(loadRectypeflag));
		Map<String,String> requestParams=new HashMap<String,String>();
		if(params!=null){
			requestParams.putAll(params);
		}
		requestParams.put("currentpage",String.valueOf(currentPage));
		requestParams.put("pagesize", String.valueOf(pagesize));
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
		return XMLUtils.xmlResolvelist(xmlContent);
	}
	
}