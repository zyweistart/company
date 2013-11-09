package com.ancun.yulualiyun.accountp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.utils.StringUtils;
import com.ancun.yulualiyun.AppContext.LoadMode;
import com.ancun.yulualiyun.R;

public class MyAccountActivity extends CoreActivity implements OnClickListener {
	
	public static final int REQUESTCODEMyAccountActivity=0;
	public static final int RESULTREFRESHCODEMyAccountActivity=1;
	
	private ImageButton activity_myaccount_btn_RightTitle;
	private Button activity_myaccount_btn_rechargelist;
	private Button activity_myaccount_btn_userecord;
	
	private PullListViewData rechargePullListViewData;
	private PullListViewData useRecordPullListViewData;
	
	private TextView activity_myaccount_storageinfo;
	private TextView activity_myaccount_baseinfo;
	private TextView activity_myaccount_durationinfo;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
		setNavigationTitle(R.string.myaccount_title);
		//刷新用户信息
		refreshUserInfo();
		//账户充值按钮
		activity_myaccount_btn_RightTitle=(ImageButton)findViewById(R.id.common_title_btn_right);
		activity_myaccount_btn_RightTitle.setOnClickListener(this);
		//当前基础月度可用容量
		activity_myaccount_storageinfo=(TextView)findViewById(R.id.activity_myaccount_storageinfo);
		activity_myaccount_storageinfo.setText("当前基础月度可用容量：正在计算...");
		//基础包月套餐
		activity_myaccount_baseinfo=(TextView)findViewById(R.id.activity_myaccount_baseinfo);
		activity_myaccount_baseinfo.setText("基础月度服务：正在计算...");
		//增值时长剩余
		activity_myaccount_durationinfo=(TextView)findViewById(R.id.activity_myaccount_durationinfo);
		activity_myaccount_durationinfo.setText("增值时长剩余：正在计算...");
		activity_myaccount_durationinfo.setVisibility(View.GONE);
		//充值套餐标签按钮
		activity_myaccount_btn_rechargelist=(Button)findViewById(R.id.activity_myaccount_btn_rechargelist);
		activity_myaccount_btn_rechargelist.setEnabled(false);
		activity_myaccount_btn_rechargelist.setOnClickListener(this);
		//使用记录标签按钮
		activity_myaccount_btn_userecord=(Button)findViewById(R.id.activity_myaccount_btn_userecord);
		activity_myaccount_btn_userecord.setEnabled(true);
		activity_myaccount_btn_userecord.setOnClickListener(this);
		//充值套餐列表
		rechargePullListViewData=new PullListViewData(this);
		rechargePullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						rechargePullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4combinfoGet,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								//数据更改通知
								rechargePullListViewData.getAdapter().notifyDataSetChanged();
								try{
									getAppContext().setPayBasePackages(false);
									activity_myaccount_btn_RightTitle.setVisibility(View.GONE);
									
									int basesum=0,baseuse=0,timecan=0,storsum=0;
									SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddHHmmss");
									SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Long currentTimeLong=Long.parseLong(sdf1.format(new Date()));
									getAppContext().setPackagesinfos(rechargePullListViewData.getDataItemList());
									List<Map<String,String>> dataItems=new ArrayList<Map<String,String>>();
									for(Map<String,String> data:rechargePullListViewData.getDataItemList()){
										Integer ctype=Integer.parseInt(data.get("ctype"));
										//ctype(1：存储2：时长3：个人基础套餐0：试用套餐)
										if(ctype==0||ctype==1||ctype==3){
											Long starttime=Long.parseLong(sdf1.format(sdf2.parse(data.get("starttime"))));
											Long endtime=Long.parseLong(sdf1.format(sdf2.parse(data.get("endtime"))));
											//只计算当前生效的套餐
											if(currentTimeLong>=starttime&&currentTimeLong<=endtime){
												storsum+=StringUtils.toInt(data.get("auciquotalimit"), 0);
												if(ctype==0||ctype==3){
													basesum=StringUtils.toInt(data.get("rectimelimit"), 0);
													baseuse=StringUtils.toInt(data.get("useurectime"), 0);
													if("3".equals(ctype)){
														//标记当前用户已经购买过基础套餐
														getAppContext().setPayBasePackages(true);
													}
												}
											}
										}else if(ctype==2){
											//时长使用时间不限所以不判断是否在有效期之内
											int timesum=StringUtils.toInt(data.get("rectimelimit"), 0);
											int timeuse=StringUtils.toInt(data.get("useurectime"), 0);
											timecan+=timesum-timeuse;
										}
										//TODO:特殊处理隐藏时长套餐
										if(ctype!=1&&ctype!=2){
											dataItems.add(data);
										}
									}
									rechargePullListViewData.setDataItemList(dataItems);
									//总容量减去录音总大小（包括回收站）
									float storcan=storsum-StringUtils.toInt(getAppContext().getUserInfo().get("rtsize"), 0);
									
									//显示到Activity页面上
									DecimalFormat df=new DecimalFormat("#.##");
									activity_myaccount_storageinfo.setText("当前基础月度可用容量："+(df.format((float)(storcan/1024/1024)))+"MB");
									activity_myaccount_baseinfo.setText("基础月度服务：剩余"+((basesum-baseuse)/60)+"分钟，已用"+(baseuse/60)+"分钟");
									activity_myaccount_durationinfo.setText("增值时长剩余："+(timecan/60)+"分钟");
									
//									activity_myaccount_btn_RightTitle.setVisibility(View.VISIBLE);
								}catch(Exception e){
									Log.e(TAG,e.getMessage());
									activity_myaccount_btn_RightTitle.setVisibility(View.GONE);
								}
							}
						},"combolist","combolist"+TAG);
					}
					
				});
		rechargePullListViewData.start(R.id.activity_myaccount_pulllistview_rechargelist, 
				new RechargeAdapter(rechargePullListViewData),null,LoadMode.HEAD);
		
		//使用记录列表
		useRecordPullListViewData=new PullListViewData(this);
		useRecordPullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						useRecordPullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4accnewDetail,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								useRecordPullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"detaillist","detaillist"+TAG);
					}
					
				});
		useRecordPullListViewData.start(R.id.activity_myaccount_pulllistview_userecord, 
				new UseRecordAdapter(useRecordPullListViewData));
		
    }
	 
	@Override
	public void onClick(View v) {
		if(activity_myaccount_btn_RightTitle==v){
			//账户充值
			startActivityForResult(new Intent(this,AccountRechargeActivity.class),REQUESTCODEMyAccountActivity);
		}else if(activity_myaccount_btn_rechargelist==v){
			//充值套餐
			activity_myaccount_btn_rechargelist.setEnabled(false);
			activity_myaccount_btn_userecord.setEnabled(true);
			rechargePullListViewData.getPulllistview().setVisibility(View.VISIBLE);
			useRecordPullListViewData.getPulllistview().setVisibility(View.GONE);
		}else if(activity_myaccount_btn_userecord==v){
			//使用记录
			activity_myaccount_btn_rechargelist.setEnabled(true);
			activity_myaccount_btn_userecord.setEnabled(false);
			rechargePullListViewData.getPulllistview().setVisibility(View.GONE);
			useRecordPullListViewData.getPulllistview().setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUESTCODEMyAccountActivity==requestCode){
			if(RESULTREFRESHCODEMyAccountActivity==resultCode){
				//如果从充值返回则重新加载账户信息
				rechargePullListViewData.getOnLoadDataListener().LoadData(LoadMode.HEAD);
			}
		}
	}
	
	/**
	 * 充值套餐
	 * @author Start
	 *
	 */
	public class RechargeAdapter extends PullListViewData.DataAdapter{
		
		public RechargeAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RechargeViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_activity_myaccount_recharge_item_layout) {
				holder = (RechargeViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_myaccount_recharge_item, null);
				holder = new RechargeViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_txt1);
				holder.remark = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_txt2);
				holder.frtime = (LinearLayout) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_fr_time);
				holder.starttime = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_txt3);
				holder.endtime = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_txt4);
				holder.notime = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_recharge_item_txt5);
				convertView.setTag(holder);
			}
			Map<String,String> data=rechargePullListViewData.getDataItemList().get(position);
			String ctype=data.get("ctype");
			if("1".equals(ctype)){
				holder.name.setText("增值存储");
				holder.frtime.setVisibility(View.VISIBLE);
				holder.notime.setVisibility(View.GONE);
				holder.remark.setText((Integer.parseInt(data.get("auciquotalimit"))/1024/1024)+"MB");
				holder.starttime.setText(data.get("starttime").substring(0,10));
				holder.endtime.setText(data.get("endtime").substring(0,10));
			}else if("2".equals(ctype)){
				holder.name.setText("增值时长");
				holder.frtime.setVisibility(View.GONE);
				holder.notime.setVisibility(View.VISIBLE);
				holder.remark.setText((Integer.parseInt(data.get("rectimelimit"))/60)+"分钟");
			}else if("3".equals(ctype)||"0".equals(ctype)){
				if("3".equals(ctype)){
					getAppContext().setPayBasePackages(true);
					holder.name.setText("云OS用户专享体验套餐");
				}else{
					holder.name.setText("试用套餐");
				}
				holder.frtime.setVisibility(View.VISIBLE);
				holder.notime.setVisibility(View.GONE);
				holder.remark.setText((Integer.parseInt(data.get("rectimelimit"))/60)+"分钟\t"+(Integer.parseInt(data.get("auciquotalimit"))/1024/1024)+"MB");
				holder.starttime.setText(data.get("starttime").substring(0,10));
				holder.endtime.setText(data.get("endtime").substring(0,10));
			}
			
			return convertView;
		}
		
		class RechargeViewHolder {
			TextView name;
			TextView remark;
			TextView starttime;
			TextView endtime;
			LinearLayout frtime;
			TextView notime;
		}
		
	}
	
	/**
	 * 使用记录
	 * @author Start
	 *
	 */
	public class UseRecordAdapter extends PullListViewData.DataAdapter{
		
		public UseRecordAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			UseRecordViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_activity_myaccount_userecord_item_layout) {
				holder = (UseRecordViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_myaccount_userecord_item, null);
				holder = new UseRecordViewHolder();
				holder.time = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt1);
				holder.remark = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt2);
				holder.timelong = (TextView) convertView.findViewById(R.id.lvitem_activity_myaccount_userecord_item_txt3);
				convertView.setTag(holder);
			}
			Map<String,String> data=useRecordPullListViewData.getDataItemList().get(position);
			holder.time.setText(data.get("cgtime"));
			
			StringBuilder strbuild=new StringBuilder();
			String cgtype=data.get("cgtype");
			String cgsubtype=data.get("cgsubtype");
			if("1".equals(cgtype)){
				strbuild.append("活动-");
				if("1".equals(cgsubtype)){
					strbuild.append("新手注册礼");
				}else if("2".equals(cgsubtype)){
					strbuild.append("新手体验礼");
				}else{
					strbuild.append("未知");
				}
			}else if("2".equals(cgtype)){
				strbuild.append("套餐充值购买-");
				if("1".equals(cgsubtype)){
					strbuild.append("存储套餐");
				}else if("2".equals(cgsubtype)){
					strbuild.append("时长套餐");
				}else if("3".equals(cgsubtype)){
					strbuild.append("个人基础套餐");
				}else{
					strbuild.append("未知");
				}
			}else if("3".equals(cgtype)){
				strbuild.append("套餐生效-");
				if("1".equals(cgsubtype)){
					strbuild.append("存储套餐");
				}else if("3".equals(cgsubtype)){
					strbuild.append("个人基础套餐");
				}else{
					strbuild.append("未知");
				}
			}else if("4".equals(cgtype)){
				strbuild.append("套餐失效-");
				if("0".equals(cgsubtype)){
					strbuild.append("试用套餐");
				}else if("1".equals(cgsubtype)){
					strbuild.append("存储套餐");
				}else if("3".equals(cgsubtype)){
					strbuild.append("个人基础套餐");
				}
			}else if("5".equals(cgtype)){
				strbuild.append("套餐转换-");
				if("2".equals(cgsubtype)){
					strbuild.append("时长套餐");
				}else{
					strbuild.append("未知");
				}
			}else if("6".equals(cgtype)){
				strbuild.append("通话录音-");
				if("1".equals(cgsubtype)){
					strbuild.append("主叫录音");
				}else if("2".equals(cgsubtype)){
					strbuild.append("被叫录音");
				}else if("3".equals(cgsubtype)){
					strbuild.append("WebCall录音");
				}else{
					strbuild.append("未知");
				}
			}else if("7".equals(cgtype)){
				strbuild.append("录音删除-");
				if("1".equals(cgsubtype)){
					strbuild.append("主叫录音删除");
				}else if("2".equals(cgsubtype)){
					strbuild.append("被叫录音删除");
				}else if("3".equals(cgsubtype)){
					strbuild.append("WebCall录音删除");
				}else{
					strbuild.append("未知");
				}
			}
//			holder.remark.setText(data.get("remark"));
			holder.remark.setText(strbuild.toString());
			
			int cgrectime=StringUtils.toInt(data.get("cgrectime"),0);
			int cgquota=StringUtils.toInt(data.get("cgquota"),0);
			StringBuilder disStr=new StringBuilder();
			if(cgrectime>0){
				disStr.append("1".equals(data.get("cgflag"))?"+":"-");
				disStr.append(cgrectime/60+"分钟");
				if(cgquota>0){
					disStr.append("\n");
				}
			}
			if(cgquota>0){
				disStr.append("1".equals(data.get("cgflag"))?"+":"-");
				DecimalFormat df=new DecimalFormat("#.##");
				disStr.append(df.format((double)cgquota/1024/1024)+"MB");
			}
			holder.timelong.setText(disStr.toString());
			return convertView;
		}
		
		class UseRecordViewHolder {
			TextView time;
			TextView remark;
			TextView timelong;
		}
		
	}
}