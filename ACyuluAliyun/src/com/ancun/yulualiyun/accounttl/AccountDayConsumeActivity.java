package com.ancun.yulualiyun.accounttl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.utils.TimeUtils;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.AppContext.LoadMode;

public class AccountDayConsumeActivity extends CoreActivity {
	
	//资金流向(1:收入;2:支出)
	private String fundflow="fundflow";
	//录音时长(秒)
	private String changeamount="changeamount";
	//变动时间(格式:yyyy-MM-dd HH:mm:ss)
	private String changetime="changetime";
	
	private PullListViewData accountDayConsumePullListViewData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_day_consume);
		Bundle bundle=getIntent().getExtras();
		final int year=bundle.getInt("year");
		final int month=bundle.getInt("month");
		//设置标题
		setNavigationTitle(year+"年"+month+"月消费明细");
		
		Calendar startD=Calendar.getInstance();
		startD.set(year, month-1,1);
		Calendar startE=Calendar.getInstance();
		startE.set(year, month-1,TimeUtils.getMonthNum(year, month));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		final String startDay=sdf.format(startD.getTime())+"000000";
		final String endDay=sdf.format(startE.getTime())+"235959";
		
		accountDayConsumePullListViewData=new PullListViewData(this);
		accountDayConsumePullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("accessid",Constant.ACCESSID);
						requestParams.put("fundflow","");
						requestParams.put("changetimeb",startDay);
						requestParams.put("changetimee",endDay);
						requestParams.put("ordersort","");
						accountDayConsumePullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4accDetail,requestParams,null,new UIRunnable(){
							@Override
							public void run() {
								accountDayConsumePullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"accdetaillist",year+month+"accdetaillist"+TAG);
					}
					
				});
		accountDayConsumePullListViewData.start(R.id.account_day_consume_list, 
				new DataAdapter(accountDayConsumePullListViewData));
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
			ViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.recordeddetailitemlayout) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(AccountDayConsumeActivity.this).inflate(R.layout.lvitem_activity_account_day_consume, null);
				holder = new ViewHolder();
				holder.account_day_list_item_day=(TextView)convertView.findViewById(R.id.account_day_list_item_day);
				holder.account_day_list_item_hour_minute = (TextView) convertView.findViewById(R.id.account_day_list_item_hour_minute);
				
				holder.account_day_list_item_linelayout_recharge = (LinearLayout) convertView.findViewById(R.id.account_day_list_item_linelayout_recharge);
				holder.account_day_list_item_remark = (TextView) convertView.findViewById(R.id.account_day_list_item_remark);
				holder.account_day_list_item_linelayout_expense = (LinearLayout) convertView.findViewById(R.id.account_day_list_item_linelayout_expense);
//				holder.account_day_list_item_expense = (TextView) convertView.findViewById(R.id.account_day_list_item_expense);
				
				holder.account_day_list_item_money=(TextView)convertView.findViewById(R.id.account_day_list_item_money);
				convertView.setTag(holder);
			}
			Map<String,String> data=accountDayConsumePullListViewData.getDataItemList().get(position);
			String ct=data.get(changetime);
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date da = df.parse(ct);
				SimpleDateFormat ddsdf=new SimpleDateFormat("dd");
				holder.account_day_list_item_day.setText(ddsdf.format(da)+"号");
				SimpleDateFormat hhmmsssdf=new SimpleDateFormat("HH:mm");
				holder.account_day_list_item_hour_minute.setText(hhmmsssdf.format(da));
			} catch (ParseException e) {
				makeTextLong(e.getMessage());
			}
			holder.account_day_list_item_linelayout_recharge.setVisibility(View.VISIBLE);
			holder.account_day_list_item_linelayout_expense.setVisibility(View.GONE);
			if("1".equals(data.get(fundflow))){
//				holder.account_day_list_item_linelayout_recharge.setVisibility(View.VISIBLE);
//				holder.account_day_list_item_linelayout_expense.setVisibility(View.GONE);
//				holder.account_day_list_item_remark.setText(data.get(remark));
				holder.account_day_list_item_remark.setText("充值");
				holder.account_day_list_item_remark.setTextColor(Color.RED);
				Integer amount=Integer.parseInt(data.get(changeamount));
				holder.account_day_list_item_money.setText("+"+(amount/60)+" 分钟");
			}else{
//				holder.account_day_list_item_linelayout_recharge.setVisibility(View.GONE);
//				holder.account_day_list_item_linelayout_expense.setVisibility(View.VISIBLE);
//				String timeremark=data.get(subremark);
//				if(Constant.EMPTYSTR.equals(timeremark)){
//					holder.account_day_list_item_expense.setText(timeremark);
//				}else{
//					holder.account_day_list_item_expense.setText(TimeUtils.secondConvertTime(Integer.parseInt(timeremark)));
//				}
				holder.account_day_list_item_remark.setText("录音");
				holder.account_day_list_item_remark.setTextColor(Color.BLUE);
				Integer amount=Integer.parseInt(data.get(changeamount));
				holder.account_day_list_item_money.setText("-"+(amount/60)+" 分钟");
			}
			return convertView;
		}
	}
	/**
	 * 视图辅助类
	 * @author Start
	 */
	private class ViewHolder {
		private TextView account_day_list_item_day;
		private TextView account_day_list_item_hour_minute;
		private LinearLayout account_day_list_item_linelayout_recharge;
		private TextView account_day_list_item_remark;
		private LinearLayout account_day_list_item_linelayout_expense;
//		private TextView account_day_list_item_expense;
		private TextView account_day_list_item_money;
	}
	
}