package com.ancun.yulualiyun.accounttl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnItemClickListener;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.AppContext.LoadMode;
import com.ancun.yulualiyun.accountp.MyAccountActivity;

public class AccountMonthConsumeActivity extends CoreActivity{

	private static final String DATAMAPITEM_tmonth="tmonth";
	private static final String DATAMAPITEM_inamount="inamount";
	private static final String DATAMAPITEM_onamount="onamount";
	
	private PullListViewData accountMonthConsumePullListViewData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_month_consume);
		setNavigationTitle(R.string.myaccount_title);
		
		ImageButton btnMonthRecharge=(ImageButton)findViewById(R.id.account_month_btn_recharge);
		btnMonthRecharge.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(AccountMonthConsumeActivity.this,com.ancun.yulu.accounttl.AccountRechargeActivity.class));
				startActivityForResult(new Intent(AccountMonthConsumeActivity.this,com.ancun.yulualiyun.accountp.AccountRechargeActivity.class),MyAccountActivity.REQUESTCODEMyAccountActivity);
			}
		});
		
		accountMonthConsumePullListViewData=new PullListViewData(this);
		accountMonthConsumePullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("changetimeb","");
						requestParams.put("changetimee","");
						requestParams.put("ordersort","");
						accountMonthConsumePullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4accStat,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								accountMonthConsumePullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"accstatlist","accstatlist"+TAG);
					}
					
				});
		accountMonthConsumePullListViewData.start(R.id.account_month_consume_list, 
				new DataAdapter(accountMonthConsumePullListViewData),new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						HolderView vh=(HolderView)view.getTag();
		        		if(vh!=null){
		        			Bundle bundle=new Bundle();
		        			bundle.putInt("year", vh.year);
		        			bundle.putInt("month", vh.month);
		        			Intent intent=new Intent(AccountMonthConsumeActivity.this,AccountDayConsumeActivity.class);
		        			intent.putExtras(bundle);
		        			startActivity(intent);
		        		}
					}
				});
		
		getUserInfo();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(MyAccountActivity.REQUESTCODEMyAccountActivity==requestCode){
			if(MyAccountActivity.RESULTREFRESHCODEMyAccountActivity==resultCode){
				//老用户升级成功跳转至新用户界面
				startActivity(new Intent(this,MyAccountActivity.class));
				finish();
			}
		}
	}
	
	/**
	 * 获取用户信息
	 */
	private void getUserInfo(){
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		getAppContext().exeNetRequest(this,Constant.GlobalURL.v4infoGet,requestParams,null,new UIRunnable(){
			@Override
			public void run() {
				getAppContext().setUserInfo(getInfoContent());
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Integer rectime=Integer.parseInt(getAppContext().getUserInfo().get("rectime"));
						TextView tv_account_month_account_balance=(TextView)findViewById(R.id.account_month_account_balance);
						tv_account_month_account_balance.setText(String.valueOf(rectime/60));
					}
					
				});
			}
		});
	}

	/**
	 * 记录适配器
	 * @author Start
	 */
	private class DataAdapter extends PullListViewData.DataAdapter{
		
		public DataAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			if (convertView != null && convertView.getId() == R.id.recordeddetailitemlayout) {
				holder = (HolderView) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(AccountMonthConsumeActivity.this).inflate(R.layout.lvitem_activity_account_month_consume, null);
				holder = new HolderView();
				holder.account_month_list_item_month=(TextView)convertView.findViewById(R.id.account_month_list_item_month);
				holder.account_month_list_item_recharge = (TextView) convertView.findViewById(R.id.account_month_list_item_recharge);
				holder.account_month_list_item_expense = (TextView) convertView.findViewById(R.id.account_month_list_item_expense);
				convertView.setTag(holder);
			}
			Map<String,String> data=accountMonthConsumePullListViewData.getDataItemList().get(position);
			String month=data.get(DATAMAPITEM_tmonth);
			try {
				DateFormat df=new SimpleDateFormat("yyyy-MM");
				Date da = df.parse(month);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
				holder.year=Integer.parseInt(sdf.format(da));
				sdf=new SimpleDateFormat("MM");
				String m=sdf.format(da);
				holder.account_month_list_item_month.setText(m+"月");
				holder.month=Integer.parseInt(m);
			} catch (ParseException e) {
				makeTextLong(e.getMessage());
			}
			Integer inamount=Integer.parseInt(data.get(DATAMAPITEM_inamount));
			holder.account_month_list_item_recharge.setText((inamount/60)+" 分钟");
			Integer onamount=Integer.parseInt(data.get(DATAMAPITEM_onamount));
			holder.account_month_list_item_expense.setText((onamount/60)+" 分钟");
			return convertView;
		}
	}
	/**
	 * 视图辅助类
	 */
	private class HolderView {
		private int year;
		private int month;
		private TextView account_month_list_item_month;
		private TextView account_month_list_item_recharge;
		private TextView account_month_list_item_expense;
	}
	
}