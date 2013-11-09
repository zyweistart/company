package com.ancun.yulualiyun.accountp;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.AppContext.LoadMode;

public class AccountRechargeActivity extends CoreActivity implements OnClickListener {

	public static final int REQUESTCODEAccountRechargeActivity=0;
	
	private Button activity_accountrecharge_btn_personal;
	private Button activity_accountrecharge_btn_duration;
	private Button activity_accountrecharge_btn_storage;
	
	private PullListViewData basePackagesPullListViewData;
	private PullListViewData durationPullListViewData;
	private PullListViewData storagePullListViewData;
	
	private static final String PRODUCTCODE=Constant.SYSTEMTEST?
			"d7025d331c315d856dfdee440f3f9c34":
				"2cec276a043223d9ff47859082cd99bc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountrecharge);
		setNavigationTitle(R.string.accountrecharge_title);
		
		//个人版基础套餐标签按钮
		activity_accountrecharge_btn_personal=(Button)findViewById(R.id.activity_accountrecharge_btn_basepackages);
		activity_accountrecharge_btn_personal.setEnabled(false);
		activity_accountrecharge_btn_personal.setOnClickListener(this);
		//时长套餐标签按钮
		activity_accountrecharge_btn_duration=(Button)findViewById(R.id.activity_accountrecharge_btn_duration);
		activity_accountrecharge_btn_duration.setEnabled(true);
		activity_accountrecharge_btn_duration.setOnClickListener(this);
		//存储套餐标签按钮
		activity_accountrecharge_btn_storage=(Button)findViewById(R.id.activity_accountrecharge_btn_storage);
		activity_accountrecharge_btn_storage.setEnabled(true);
		activity_accountrecharge_btn_storage.setOnClickListener(this);
		
		//个人版基础套餐
		basePackagesPullListViewData=new PullListViewData(this);
		basePackagesPullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("productrecordno",PRODUCTCODE);
						requestParams.put("type","3");
						requestParams.put("status","1");
						basePackagesPullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4QrycomboList,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								basePackagesPullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"packageslist","packageslist3"+TAG);
					}
					
				});
		basePackagesPullListViewData.start(R.id.activity_accountrecharge_pulllistview_base_packages, 
				new BasePackagesAdapter(basePackagesPullListViewData));

		//时长套餐
		durationPullListViewData=new PullListViewData(this);
		durationPullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("productrecordno",PRODUCTCODE);
						requestParams.put("type","2");
						requestParams.put("status","1");
						durationPullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4QrycomboList,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								durationPullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"packageslist","packageslist2"+TAG);
					}
					
				});
		durationPullListViewData.start(R.id.activity_accountrecharge_pulllistview_duration, 
				new DurationAdapter(durationPullListViewData));
		
		//存储套餐
		storagePullListViewData=new PullListViewData(this);
		storagePullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("productrecordno",PRODUCTCODE);
						requestParams.put("type","1");
						requestParams.put("status","1");
						storagePullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4QrycomboList,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								storagePullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"packageslist","packageslist1"+TAG);
					}
					
				});
		storagePullListViewData.start(R.id.activity_accountrecharge_pulllistview_storage, 
				new StorageAdapter(storagePullListViewData));
		
	}

	@Override
	public void onClick(View v) {
		if(activity_accountrecharge_btn_personal==v){
			activity_accountrecharge_btn_personal.setEnabled(false);
			activity_accountrecharge_btn_duration.setEnabled(true);
			activity_accountrecharge_btn_storage.setEnabled(true);
		}else if(activity_accountrecharge_btn_duration==v){
			activity_accountrecharge_btn_personal.setEnabled(true);
			activity_accountrecharge_btn_duration.setEnabled(false);
			activity_accountrecharge_btn_storage.setEnabled(true);
		}else if(activity_accountrecharge_btn_storage==v){
			activity_accountrecharge_btn_personal.setEnabled(true);
			activity_accountrecharge_btn_duration.setEnabled(true);
			activity_accountrecharge_btn_storage.setEnabled(false);
		}
		basePackagesPullListViewData.getPulllistview().setVisibility(activity_accountrecharge_btn_personal.isEnabled()?View.GONE:View.VISIBLE);
		durationPullListViewData.getPulllistview().setVisibility(activity_accountrecharge_btn_duration.isEnabled()?View.GONE:View.VISIBLE);
		storagePullListViewData.getPulllistview().setVisibility(activity_accountrecharge_btn_storage.isEnabled()?View.GONE:View.VISIBLE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUESTCODEAccountRechargeActivity==requestCode){
			if(MyAccountActivity.RESULTREFRESHCODEMyAccountActivity==resultCode){
				setResult(resultCode);
				finish();
			}
		}
	}
	
	public class BasePackagesAdapter extends PullListViewData.DataAdapter{
		
		public BasePackagesAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RechargeViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_activity_recharge_base_item_layout) {
				holder = (RechargeViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_recharge_base_item, null);
				holder = new RechargeViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt1);
				holder.tip = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_tip);
				holder.time = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt2);
				holder.storage = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt3);
				holder.button = (Button) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_btn4);
				convertView.setTag(holder);
			}
			Map<String,String> data=basePackagesPullListViewData.getDataItemList().get(position);
			holder.code=data.get("recordno");
			holder.tip.setText("有效期"+data.get("valid")+"天");
			holder.name.setText(data.get("name"));
			holder.time.setText(data.get("duration")+"分钟");
			holder.storage.setText(data.get("storage")+"MB");
			holder.button.setTag(data);
			holder.button.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(final View v) {
					if(getAppContext().isOldUserFlag()&&"1".equals(getAppContext().getUserInfo().get("payuserflag"))){
						Integer rectime=Integer.parseInt(getAppContext().getUserInfo().get("rectime"));
						new AlertDialog.Builder(AccountRechargeActivity.this)
						.setMessage("您原时长套餐还剩"+String.valueOf(rectime/60)+"分钟，衷心建议您用完原套餐再充值，如果您坚持继续充值，原套餐中的可录音时长会被清零，多可惜啊")
						.setPositiveButton("充值", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Map<String,String> d=(Map<String,String>)v.getTag();
								Bundle bundle=new Bundle();
								bundle.putInt(RechargeDetailActivity.TAGMODE_INT, 3);
								bundle.putString(RechargeDetailActivity.TAGCODE, d.get("recordno"));
								bundle.putString(RechargeDetailActivity.TAGNAME, d.get("name"));
								bundle.putString(RechargeDetailActivity.TAGPRICE, d.get("newprice"));
								bundle.putString(RechargeDetailActivity.TAGTIME, d.get("valid"));
								bundle.putString(RechargeDetailActivity.TAGVALUE, d.get("duration")+"分钟\t"+d.get("storage")+"MB");
								Intent intent=new Intent(AccountRechargeActivity.this,RechargeDetailActivity.class);
								intent.putExtras(bundle);
								startActivityForResult(intent,REQUESTCODEAccountRechargeActivity);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).show();
					}else{
						Map<String,String> d=(Map<String,String>)v.getTag();
						Bundle bundle=new Bundle();
						bundle.putInt(RechargeDetailActivity.TAGMODE_INT, 3);
						bundle.putString(RechargeDetailActivity.TAGCODE, d.get("recordno"));
						bundle.putString(RechargeDetailActivity.TAGNAME, d.get("name"));
						bundle.putString(RechargeDetailActivity.TAGPRICE, d.get("newprice"));
						bundle.putString(RechargeDetailActivity.TAGTIME, d.get("valid"));
						bundle.putString(RechargeDetailActivity.TAGVALUE, d.get("duration")+"分钟\t"+d.get("storage")+"MB");
						Intent intent=new Intent(AccountRechargeActivity.this,RechargeDetailActivity.class);
						intent.putExtras(bundle);
						startActivityForResult(intent,REQUESTCODEAccountRechargeActivity);
					}
				}
				
			});
			
			return convertView;
		}
		
	}
	
	public class DurationAdapter extends PullListViewData.DataAdapter{
		
		public DurationAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RechargeViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_activity_recharge_base_item_layout) {
				holder = (RechargeViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_recharge_base_item, null);
				holder = new RechargeViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt1);
				holder.tip = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_tip);
				holder.time = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt2);
				holder.storage = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt3);
				holder.storage.setVisibility(View.GONE);
				holder.button = (Button) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_btn4);
				convertView.setTag(holder);
			}
			Map<String,String> data=durationPullListViewData.getDataItemList().get(position);
			holder.code=data.get("recordno");
			holder.tip.setText("无时间限制");
			holder.name.setText(data.get("name"));
			holder.time.setText(data.get("duration")+"分钟");
			holder.button.setTag(data);
			holder.button.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(final View v) {
					if(getAppContext().isOldUserFlag()){
						new AlertDialog.Builder(AccountRechargeActivity.this)
						.setMessage("要先购买基础包月套餐后才能购买增值时长套餐，打好基础很重要哦")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).show();
					}else{
						if(getAppContext().isPayBasePackages()){
							Map<String,String> d=(Map<String,String>)v.getTag();
							Bundle bundle=new Bundle();
							bundle.putInt(RechargeDetailActivity.TAGMODE_INT, 1);
							bundle.putString(RechargeDetailActivity.TAGCODE, d.get("recordno"));
							bundle.putString(RechargeDetailActivity.TAGNAME, d.get("name"));
							bundle.putString(RechargeDetailActivity.TAGPRICE, d.get("newprice"));
							bundle.putString(RechargeDetailActivity.TAGTIME, d.get("valid"));
							bundle.putString(RechargeDetailActivity.TAGVALUE, d.get("duration"));
							Intent intent=new Intent(AccountRechargeActivity.this,RechargeDetailActivity.class);
							intent.putExtras(bundle);
							startActivityForResult(intent,REQUESTCODEAccountRechargeActivity);
						}else{
							//增值时长套餐的购买需要在有基础套餐之上
							makeTextLong("请先购买基础套餐");
						}
					}
				}
				
			});
			
			return convertView;
		}
		
	}
	
	public class StorageAdapter extends PullListViewData.DataAdapter{
		
		public StorageAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RechargeViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_activity_recharge_base_item_layout) {
				holder = (RechargeViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_activity_recharge_base_item, null);
				holder = new RechargeViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt1);
				holder.tip = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_tip);
				holder.time = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt2);
				holder.time.setVisibility(View.GONE);
				holder.storage = (TextView) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_txt3);
				holder.button = (Button) convertView.findViewById(R.id.lvitem_activity_recharge_base_item_btn4);
				convertView.setTag(holder);
			}
			Map<String,String> data=storagePullListViewData.getDataItemList().get(position);
			holder.code=data.get("recordno");
			holder.tip.setText("有效期"+data.get("valid")+"天");
			holder.name.setText(data.get("name"));
			holder.storage.setText(data.get("storage")+"MB");
			holder.button.setTag(data);
			holder.button.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					if(getAppContext().isOldUserFlag()){
						new AlertDialog.Builder(AccountRechargeActivity.this)
						.setMessage("要先购买基础包月套餐后才能购买增值存储套餐，更多空间更多安心")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).show();
					}else{
						if(getAppContext().isPayBasePackages()){
							Map<String,String> d=(Map<String,String>)v.getTag();
							Bundle bundle=new Bundle();
							bundle.putInt(RechargeDetailActivity.TAGMODE_INT, 2);
							bundle.putString(RechargeDetailActivity.TAGCODE, d.get("recordno"));
							bundle.putString(RechargeDetailActivity.TAGNAME, d.get("name"));
							bundle.putString(RechargeDetailActivity.TAGPRICE, d.get("newprice"));
							bundle.putString(RechargeDetailActivity.TAGTIME, d.get("valid"));
							bundle.putString(RechargeDetailActivity.TAGVALUE, d.get("storage"));
							Intent intent=new Intent(AccountRechargeActivity.this,RechargeDetailActivity.class);
							intent.putExtras(bundle);
							startActivityForResult(intent,REQUESTCODEAccountRechargeActivity);
						}else{
							//存储套餐的购买需要在有基础套餐之上
							makeTextLong("请先购买基础套餐");
						}
					}
				}
				
			});
			
			return convertView;
		}
		
	}
	
	class RechargeViewHolder {
		String code;
		TextView name;
		TextView tip;
		TextView time;
		TextView storage;
		Button button;
	}

}