package com.ancun.yulualiyun;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.utils.TimeUtils;
import com.ancun.yulualiyun.AppContext.LoadMode;

public class AccountActivity extends CoreActivity implements OnClickListener {
	
	public static final int REQUESTCODEMyAccountActivity=0;
	public static final int RESULTREFRESHCODEMyAccountActivity=1;
	
	private ImageButton activity_myaccount_btn_RightTitle;
	private Button activity_myaccount_btn_rechargelist;
	private Button activity_myaccount_btn_userecord;
	
	private PullListViewData useRecordPullListViewData;
	
	private TextView activity_myaccount_storageinfo;
	private TextView activity_myaccount_baseinfo;
	private TextView activity_myaccount_info;
	
	private ListView activity_myaccount_pulllistview_rechargelist;
	
	private RechargeAdapter rechargeAdapter;
	private List<Map<String,String>> rechargeList=new ArrayList<Map<String,String>>();
	
	private static final String NAME1="淘宝卖家手机卖家专享年度免费服务";
	private static final String NAME2="淘宝卖家手机（阿里云OS）专享月度体验服务";
	
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
		setNavigationTitle(R.string.myaccount_title);
		//账户充值按钮
		activity_myaccount_btn_RightTitle=(ImageButton)findViewById(R.id.common_title_btn_right);
		activity_myaccount_btn_RightTitle.setOnClickListener(this);
		
		activity_myaccount_info=(TextView)findViewById(R.id.activity_myaccount_info);
		activity_myaccount_info.setVisibility(View.GONE);
		//基础包月套餐
		activity_myaccount_baseinfo=(TextView)findViewById(R.id.activity_myaccount_baseinfo);
		activity_myaccount_baseinfo.setText("通话录音存储空间：剩余0MB，已用0MB");
		//当前基础月度可用容量
		activity_myaccount_storageinfo=(TextView)findViewById(R.id.activity_myaccount_storageinfo);
		activity_myaccount_storageinfo.setText("已赠送基础服务套餐0个月，剩余未赠送套餐0个月");
		activity_myaccount_storageinfo.setVisibility(View.GONE);
		//充值套餐标签按钮
		activity_myaccount_btn_rechargelist=(Button)findViewById(R.id.activity_myaccount_btn_rechargelist);
		activity_myaccount_btn_rechargelist.setEnabled(false);
		activity_myaccount_btn_rechargelist.setOnClickListener(this);
		//使用记录标签按钮
		activity_myaccount_btn_userecord=(Button)findViewById(R.id.activity_myaccount_btn_userecord);
		activity_myaccount_btn_userecord.setEnabled(true);
		activity_myaccount_btn_userecord.setOnClickListener(this);
		
		activity_myaccount_pulllistview_rechargelist=(ListView)findViewById(R.id.activity_myaccount_istview_rechargelist);
		rechargeAdapter=new RechargeAdapter();
		activity_myaccount_pulllistview_rechargelist.setAdapter(rechargeAdapter);
		
		//使用记录列表
		useRecordPullListViewData=new PullListViewData(this);
		useRecordPullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("calltype","1");
						requestParams.put("oppno","");
						requestParams.put("callerno","");
						requestParams.put("calledno","");
						requestParams.put("begintime","");
						requestParams.put("endtime","");
						requestParams.put("remark","");
						requestParams.put("durmin","");
						requestParams.put("durmax","");
						requestParams.put("licno","");
						requestParams.put("ordersort","desc");
						useRecordPullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4recQry,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								useRecordPullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"reclist","reclist"+TAG);
					}
					
				});
		useRecordPullListViewData.start(R.id.activity_myaccount_pulllistview_userecord, 
				new UseRecordAdapter(useRecordPullListViewData));
		
		getUserInfo();
		
    }
	 
	@Override
	public void onClick(View v) {
		if(activity_myaccount_btn_RightTitle==v){
			//账户充值
			
		}else if(activity_myaccount_btn_rechargelist==v){
			//充值套餐
			activity_myaccount_btn_rechargelist.setEnabled(false);
			activity_myaccount_btn_userecord.setEnabled(true);
			activity_myaccount_pulllistview_rechargelist.setVisibility(View.VISIBLE);
			useRecordPullListViewData.getPulllistview().setVisibility(View.GONE);
		}else if(activity_myaccount_btn_userecord==v){
			//使用记录
			activity_myaccount_btn_rechargelist.setEnabled(true);
			activity_myaccount_btn_userecord.setEnabled(false);
			activity_myaccount_pulllistview_rechargelist.setVisibility(View.GONE);
			useRecordPullListViewData.getPulllistview().setVisibility(View.VISIBLE);
		}
	}
	
	private void getUserInfo(){
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		getAppContext().exeNetRequest(this,Constant.GlobalURL.v4infoGet,requestParams,null,new UIRunnable(){
			@Override
			public void run() {
				getAppContext().setUserInfoAll(getAllInfoContent());
				getAppContext().setUserInfo(getAllInfoContent().get("v4info"));
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						//赠送用户
//						if("yunos01".equals(getAppContext().getUserInfo().get("prodid"))){
//							activity_myaccount_storageinfo.setVisibility(View.VISIBLE);
//						}else{
//							
//						}
						activity_myaccount_info.setText("当前账户："+getAppContext().getUserInfo().get("phone"));
						activity_myaccount_info.setVisibility(View.VISIBLE);
						getPackInfo();
					}
					
				});
			}
		});
		

	}
	
	private void getPackInfo(){
		
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		getAppContext().exeNetRequest(this,"v4currcomboGet",requestParams,null,new UIRunnable(){
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Map<String,String> entinfo=getAppContext().getUserInfoAll().get("entinfo");
						Map<String,String> data=new HashMap<String,String>();
						//赠送用户
						if("yunos01".equals(getAppContext().getUserInfo().get("prodid"))){
							data.put("name", NAME1);
						}else{
							data.put("name", NAME2);
						}
						data.put("used", "1");
						
						DecimalFormat df=new DecimalFormat("#.##");
						Float bquota=Float.parseFloat(getInfoContent().get("bquota"));
						Float bq=(float)(bquota/1024/1024);
						data.put("rectimelimit",df.format(bq)+"MB");
						data.put("starttime", getAppContext().getUserInfo().get("signuptime"));
						data.put("endtime",getInfoContent().get("comboendtime"));
						rechargeList.clear();
						rechargeList.add(data);
						rechargeAdapter.notifyDataSetChanged();
						
//						Map<String,String> entinfo=getAppContext().getUserInfoAll().get("entinfo");
//						DecimalFormat df=new DecimalFormat("#.##");
//						
//						Float bquota=Float.parseFloat(getInfoContent().get("bquota"));
						Float rtsize=Float.parseFloat(entinfo.get("nrtsize"));
//						Float bq=(float)(bquota/1024/1024);
						Float rt=(float)(rtsize/1024/1024);
						String noUseV=df.format(bq-rt);
						String UseV=df.format(rt);
						activity_myaccount_baseinfo.setText("通话录音存储空间：剩余"+noUseV+"MB，已用"+UseV+"MB");
//					
//						try {
//							int totalDay=TimeUtils.getDaysBetween(getAppContext().getUserInfo().get("signuptime"), getInfoContent().get("comboendtime"));
//							
////							int useMonth=TimeUtils.DiffMonth(getAppContext().getUserInfo().get("signuptime"),"2019-10-10 10:10:10");
//							int useMonth=TimeUtils.DiffMonth(getAppContext().getUserInfo().get("signuptime"),TimeUtils.getSysTimeS());
//							int noUseMonth=(totalDay+1)/31-useMonth;
//							if(noUseMonth<0){
//								useMonth=(totalDay+1)/31;
//							}
//							
//							activity_myaccount_storageinfo.setText("已赠送基础服务套餐"+useMonth+"个月，剩余未赠送套餐"+noUseMonth+"个月");
//							
//							SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							Calendar a = Calendar.getInstance();
//							a.setTime(sdf2.parse(getAppContext().getUserInfo().get("signuptime")));
//							int endYear=a.get(Calendar.YEAR);
//							int endMonth=a.get(Calendar.MONTH)+1;
//							int endDay=a.get(Calendar.DAY_OF_MONTH);
//							
//							List<Map<String,String>> tmpData=new ArrayList<Map<String,String>>();
//							for(int i=1;i<=useMonth;i++){
//								Map<String,String> data=new HashMap<String,String>();
//								if(noUseMonth<0){
//									data.put("used", "0");
//								}else{
//									if(i==(useMonth)){
//										data.put("used", "1");
//									}else{
//										data.put("used", "0");
//									}
//								}
////								data.put("rectimelimit", df.format(bq/12)+"MB");
//								data.put("rectimelimit","500MB");
//								
//								String mStrMonth=endMonth+"";
//								if(endMonth<10){
//									mStrMonth="0"+mStrMonth;
//								}
//								String mStrDay=endDay+"";
//								if(endDay<10){
//									mStrDay="0"+mStrDay;
//								}
//								data.put("starttime", endYear+"-"+mStrMonth+"-"+mStrDay);
////								a.add(Calendar.MONTH, 1);
//								a.add(Calendar.DAY_OF_MONTH, 31);
//								endYear=a.get(Calendar.YEAR);
//								endMonth=a.get(Calendar.MONTH)+1;
//								endDay=a.get(Calendar.DAY_OF_MONTH);
//								
//								mStrMonth=endMonth+"";
//								if(endMonth<10){
//									mStrMonth="0"+mStrMonth;
//								}
//								int e=endDay-1;
//								mStrDay=e+"";
//								if(e<10){
//									mStrDay="0"+mStrDay;
//								}
//								data.put("endtime", endYear+"-"+mStrMonth+"-"+mStrDay);
//								tmpData.add(data);
//							}
//							rechargeList.clear();
//							for(int i=tmpData.size()-1;i>=0;i--){
//								rechargeList.add(tmpData.get(i));
//							}
//							rechargeAdapter.notifyDataSetChanged();
//						} catch (ParseException e) {
//							e.printStackTrace();
//						}
						
					}
				});
			}
		});
		
	}
	
	/**
	 * 充值套餐
	 * @author Start
	 *
	 */
	public class RechargeAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return rechargeList.size();
		}

		@Override
		public Object getItem(int position) {
			return  rechargeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
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
				holder.usedImg = (ImageView) convertView.findViewById(R.id.myaccount_recharge_used_img);
				convertView.setTag(holder);
			}
			Map<String,String> data=rechargeList.get(position);
			String used=data.get("used");
			if("1".equals(used)){
				holder.usedImg.setImageResource(R.drawable.myaccount_recharge_used);
			}else{
				holder.usedImg.setImageResource(R.drawable.myaccount_recharge_noused);
			}
			holder.name.setText(data.get("name"));
			holder.frtime.setVisibility(View.VISIBLE);
			holder.notime.setVisibility(View.GONE);
			holder.remark.setText(data.get("rectimelimit"));
			holder.starttime.setText(data.get("starttime").substring(0,10));
			holder.endtime.setText(data.get("endtime").substring(0,10));
			return convertView;
		}
		
		class RechargeViewHolder {
			TextView name;
			TextView remark;
			TextView starttime;
			TextView endtime;
			LinearLayout frtime;
			TextView notime;
			ImageView usedImg;
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
			holder.time.setText(data.get("begintime"));
			holder.remark.setText("录音");
			
			DecimalFormat df=new DecimalFormat("#.##");
			float filesize=Float.parseFloat(data.get("filesize"));
			String duration=TimeUtils.secondConvertTime(Integer.parseInt(data.get("duration")));
			
			holder.timelong.setText(df.format((float)(filesize/1024/1024))+"MB\n"+duration);
			return convertView;
		}
		
		class UseRecordViewHolder {
			TextView time;
			TextView remark;
			TextView timelong;
		}
		
	}
}