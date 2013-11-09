package com.ancun.yulualiyun.accounttl;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.service.PullListViewData;
import com.ancun.service.PullListViewData.OnItemClickListener;
import com.ancun.service.PullListViewData.OnLoadDataListener;
import com.ancun.yulualiyun.R;
import com.ancun.yulualiyun.AppContext.LoadMode;

public class AccountRechargeActivity extends CoreActivity {

	static String DATAMAPITEM_RECORDNO="recordno";
	static String DATAMAPITEM_FEE="fee";
	static String DATAMAPITEM_AMOUNT="amount";
	static String DATAMAPITEM_REMARK="remark";
	
	private PullListViewData rechargePullListViewData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_recharge);
		setNavigationTitle(R.string.accountrecharge_title);
		rechargePullListViewData=new PullListViewData(this);
		rechargePullListViewData.setOnLoadDataListener(
				new OnLoadDataListener(){

					@Override
					public void LoadData(LoadMode loadMode) {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("type","6");
						requestParams.put("property","2");
						Map<String,String> headerParams=new HashMap<String,String>();
						headerParams.put("sign", "");
						rechargePullListViewData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.recprodGet,requestParams,headerParams,new UIRunnable(){
							@Override
							public void run() {
								rechargePullListViewData.getAdapter().notifyDataSetChanged();
							}
						},"recprodlist","recprodlist"+TAG);
					}
					
				});
		rechargePullListViewData.start(R.id.account_recharge_pulltorefreshlistview, 
				new RechargeAdapter(rechargePullListViewData),new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						HolderView holder=(HolderView)view.getTag();
		        		if(holder!=null){
		        			Bundle bundle=new Bundle();
		        			bundle.putString(DATAMAPITEM_RECORDNO, holder.recordno);
		        			bundle.putString(DATAMAPITEM_AMOUNT,holder.amount);
		        			bundle.putInt(DATAMAPITEM_FEE, Integer.parseInt(holder.fee));
		        			Intent intent=new Intent(AccountRechargeActivity.this,AccountRechargeConfirmActivity.class);
		        			intent.putExtras(bundle);
		        			startActivity(intent);
		        			finish();
		        		}
					}
				});

	}
	
	private class RechargeAdapter extends PullListViewData.DataAdapter{
		
		public RechargeAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder=null;
			if(convertView!=null&&convertView.getId()==R.id.common_account_recharge_item){
				holder=(HolderView)convertView.getTag();
			}else{
				holder=new HolderView();
				convertView = View.inflate(getApplicationContext(), R.layout.lvitem_account_recharge, null);
				holder.common_account_recharge_item_fee=(TextView)convertView.findViewById(R.id.common_account_recharge_item_fee);
				holder.common_account_recharge_item_sale=(ImageView)convertView.findViewById(R.id.common_account_recharge_item_sale);
				holder.common_account_recharge_item_remark=(TextView)convertView.findViewById(R.id.common_account_recharge_item_remark);
				convertView.setTag(holder);
			}
			if(holder!=null){
				Map<String,String> data=rechargePullListViewData.getDataItemList().get(position);
				holder.recordno=data.get(DATAMAPITEM_RECORDNO);
				holder.fee=data.get(DATAMAPITEM_FEE);
				holder.amount=data.get(DATAMAPITEM_AMOUNT);
				holder.common_account_recharge_item_fee.setText(holder.fee+"元套餐");
				
				holder.common_account_recharge_item_sale.setVisibility(View.GONE);
				float fee=Float.parseFloat(holder.fee);
				float amount=Float.parseFloat(holder.amount);
				int sale=(int) ((fee*2/amount)*10);
				switch(sale){
				case 9:
					holder.common_account_recharge_item_sale.setVisibility(View.VISIBLE);
					holder.common_account_recharge_item_sale.setImageResource(R.drawable.sale9);
					break;
				case 8:
					holder.common_account_recharge_item_sale.setVisibility(View.VISIBLE);
					holder.common_account_recharge_item_sale.setImageResource(R.drawable.sale8);
					break;
				}
				
//				holder.common_account_recharge_item_remark.setText(data.get(DATAMAPITEM_REMARK));
				holder.common_account_recharge_item_remark.setText(holder.fee+"元兑换"+holder.amount+"分钟通话时长");
			}
			return convertView;
		}

	}
	
	private class HolderView{
		private String recordno;
		private String fee;
		private String amount;
		private TextView common_account_recharge_item_fee;
		private ImageView common_account_recharge_item_sale;
		private TextView common_account_recharge_item_remark;
	}

}