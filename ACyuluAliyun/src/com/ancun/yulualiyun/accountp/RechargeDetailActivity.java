package com.ancun.yulualiyun.accountp;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ancun.alipay.MobileSecurePayHelper;
import com.ancun.alipay.MobileSecurePayer;
import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.TimeUtils;
import com.ancun.yulualiyun.R;

public class RechargeDetailActivity extends CoreActivity implements OnClickListener{
	
	//支付宝支付消息
	public static final int ALIPAY_MSG=0xAC0001;
	
	public static final long DAY_MILLISECOND = 86400000L;
	
	public static final String TAGCODE="RECORDNO";
	public static final String TAGNAME="name";
	public static final String TAGVALUE="value";
	public static final String TAGPRICE="price";
	public static final String TAGTIME="time";
	public static final String TAGMODE_INT="TAGMODE";
	
	private Bundle bundle;
	
	private TextView activity_rechargedetail_packages_tip;
	private TextView activity_rechargedetail_name;
	private TextView activity_rechargedetail_value;
	private TextView activity_rechargedetail_price;
	private TextView activity_rechargedetail_valid;
	private TextView activity_rechargedetail_starttime;
	private TextView activity_rechargedetail_endtime;
	private TextView activity_rechargedetail_account;
	private TextView activity_rechargedetail_result_content;
	private Spinner activity_rechargedetail_num_spi5;
	private TextView activity_rechargedetail_oldvalue;
	private TextView activity_rechargedetail_oldendtime;
	private TextView activity_rechargedetail_convertmoney;
	
	private LinearLayout activity_rechargedetail_fr_detail;
	private LinearLayout activity_rechargedetail_fr_result;
	private LinearLayout activity_rechargedetail_fr_num;
	private LinearLayout activity_rechargedetail_fr_valid;
	private LinearLayout activity_rechargedetail_fr_starttime;
	private LinearLayout activity_rechargedetail_fr_endtime;
	private LinearLayout activity_rechargedetail_fr_oldaccount;
	private LinearLayout activity_rechargedetail_fr_convertmoney;
	
	private Button activity_rechargedetail_btn_payment;
	private Button activity_rechargedetail_btn_paymentback;
	
	private SimpleDateFormat sdfyyyyMMdd_F=new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
	private SimpleDateFormat sdfyyyyMMddHHmmss=new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdfyyyyMMddHHmmss_F=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechargedetail);
		setNavigationTitle(R.string.accountrecharge_title);
		
		activity_rechargedetail_fr_detail=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_detail);
		activity_rechargedetail_fr_result=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_result);
		activity_rechargedetail_fr_num=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_num);
		activity_rechargedetail_fr_valid=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_valid);
		activity_rechargedetail_fr_starttime=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_starttime);
		activity_rechargedetail_fr_endtime=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_endtime);
		
		activity_rechargedetail_name=(TextView)findViewById(R.id.activity_rechargedetail_name);
		activity_rechargedetail_value=(TextView)findViewById(R.id.activity_rechargedetail_value);
		activity_rechargedetail_price=(TextView)findViewById(R.id.activity_rechargedetail_price);
		activity_rechargedetail_valid=(TextView)findViewById(R.id.activity_rechargedetail_valid);
		activity_rechargedetail_starttime=(TextView)findViewById(R.id.activity_rechargedetail_starttime);
		activity_rechargedetail_endtime=(TextView)findViewById(R.id.activity_rechargedetail_endtime);
		activity_rechargedetail_account=(TextView)findViewById(R.id.activity_rechargedetail_account);
		activity_rechargedetail_result_content=(TextView)findViewById(R.id.activity_rechargedetail_result_content);
		
		activity_rechargedetail_num_spi5=(Spinner)findViewById(R.id.activity_rechargedetail_num_spi5);

		activity_rechargedetail_packages_tip=(TextView)findViewById(R.id.activity_rechargedetail_packages_tip);
		activity_rechargedetail_fr_oldaccount=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_oldaccount);
		activity_rechargedetail_oldvalue=(TextView)findViewById(R.id.activity_rechargedetail_oldvalue);
		activity_rechargedetail_oldendtime=(TextView)findViewById(R.id.activity_rechargedetail_oldendtime);
		activity_rechargedetail_fr_convertmoney=(LinearLayout)findViewById(R.id.activity_rechargedetail_fr_convertmoney);
		activity_rechargedetail_convertmoney=(TextView)findViewById(R.id.activity_rechargedetail_convertmoney);
		
		bundle=getIntent().getExtras();
		activity_rechargedetail_name.setText(bundle.getString(TAGNAME));
		activity_rechargedetail_price.setText(String.valueOf(Float.parseFloat(bundle.getString(TAGPRICE))));
		
		activity_rechargedetail_account.setText(getAppContext().getCurrentLoginPhoneNumber(this));
		
		activity_rechargedetail_btn_payment=(Button)findViewById(R.id.activity_rechargedetail_btn_payment);
		activity_rechargedetail_btn_payment.setOnClickListener(this);
		activity_rechargedetail_btn_paymentback=(Button)findViewById(R.id.activity_rechargedetail_btn_paymentback);
		activity_rechargedetail_btn_paymentback.setOnClickListener(this);
		
		switch(bundle.getInt(TAGMODE_INT)){
		case 1:
			//时长
			activity_rechargedetail_fr_num.setVisibility(View.VISIBLE);
			activity_rechargedetail_fr_starttime.setVisibility(View.GONE);
			activity_rechargedetail_fr_endtime.setVisibility(View.GONE);
			activity_rechargedetail_value.setText(bundle.getString(TAGVALUE)+"分钟");
			activity_rechargedetail_valid.setText("无时间限制");
			
			ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.activity_accountrecharge_duration_spi5_data));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			activity_rechargedetail_num_spi5.setAdapter(adapter);
			activity_rechargedetail_num_spi5.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					Integer f=Integer.parseInt(String.valueOf(activity_rechargedetail_num_spi5.getSelectedItem()));
					DecimalFormat df=new DecimalFormat("#.##");
					activity_rechargedetail_price.setText(df.format(Float.parseFloat(bundle.getString(TAGPRICE))*f));
					activity_rechargedetail_value.setText(String.valueOf(Integer.parseInt(bundle.getString(TAGVALUE))*f)+"分钟");
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
				
			});
			break;
		case 2:
			//存储
			activity_rechargedetail_price.setText(String.valueOf(Float.parseFloat(bundle.getString(TAGPRICE))));
			
			activity_rechargedetail_fr_valid.setVisibility(View.GONE);
			activity_rechargedetail_value.setText(bundle.getString(TAGVALUE)+"MB");
			boolean storageflag=true;
			for(Map<String,String> data:getAppContext().getPackagesinfos()){
				if("1".equals(data.get("ctype"))){
					storageflag=false;
					try{
						Long starttime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("starttime"))));
						Long endtime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("endtime"))));
						Long currentTimeLong=Long.parseLong(sdfyyyyMMddHHmmss.format(new Date()));
						//只计算当前生效的套餐
						if(currentTimeLong>=starttime&&currentTimeLong<=endtime){
							if(isCurrentStorageExist(data)){
								new AlertDialog.Builder(this)
								.setMessage(bundle.getString(TAGNAME)+"已续费")
								.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										finish();
									}
								}).setCancelable(false).show();
								return;
							}
							//已购买的存储容量
							int storsum=Integer.parseInt(data.get("auciquotalimit"))/1024/1024;
							//将要购买的存储容量
							int tarvalue=Integer.parseInt(bundle.getString(RechargeDetailActivity.TAGVALUE));
							if(tarvalue>=storsum){
								//升级、续费
								activity_rechargedetail_packages_tip.setVisibility(View.VISIBLE);
								activity_rechargedetail_fr_oldaccount.setVisibility(View.VISIBLE);
							}else{
								new AlertDialog.Builder(this)
								.setMessage("暂不支持存储套餐降容量级别购买，请选择存储容量等于或大于您当前在使用套餐的存储服务，多点空间才能有备无患哦。")
								.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										finish();
									}
								}).setCancelable(false).show();
								return;
							}
						
							if(tarvalue>storsum){
								//升级
								//日期计算
								String endtimeday=data.get("endtime").substring(0,10);
								Calendar c1 = Calendar.getInstance();
								c1.setTime(new Date());
								c1.add(Calendar.DAY_OF_YEAR, Integer.parseInt(bundle.getString(TAGTIME)));
								activity_rechargedetail_starttime.setText(sdfyyyyMMdd_F.format(new Date()));
								activity_rechargedetail_endtime.setText(sdfyyyyMMdd_F.format(c1.getTime()));
								activity_rechargedetail_oldvalue.setText(storsum+"MB");
								activity_rechargedetail_oldendtime.setText(endtimeday);
								//金额计算
								int oridays=(int)Math.ceil(((double)(sdfyyyyMMddHHmmss_F.parse(data.get("endtime")).getTime()-new Date().getTime()))/DAY_MILLISECOND);
								int leftdays=Integer.parseInt(data.get("buyvtime"));
								double deamount =Double.parseDouble((data.get("buyprice"))) *oridays / leftdays;
								double amount = Double.parseDouble(bundle.getString(TAGPRICE)) - deamount;
								//金额必须为大于零的正整数
								if(amount<=0){
									new AlertDialog.Builder(this)
									.setMessage("无法购买该套餐")
									.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											finish();
										}
									}).setCancelable(false).show();
									return;
								}
								DecimalFormat df=new DecimalFormat("#.##");
								activity_rechargedetail_price.setText(Double.parseDouble(bundle.getString(TAGPRICE))+"-"+String.valueOf(df.format(deamount))+"="+String.valueOf(df.format(amount)));
								
								activity_rechargedetail_fr_convertmoney.setVisibility(View.VISIBLE);
								activity_rechargedetail_convertmoney.setText(data.get("buyprice")+"*"+oridays+"/"+leftdays+"="+String.valueOf(df.format(deamount)));
							}else{
								//续费则取最后续费的套餐
								Map<String,String> lastData=lastNewPackages("1");
								//日期计算
								String endtimeday=lastData.get("endtime").substring(0,10);
								Calendar c1 = Calendar.getInstance();
								c1.setTime(sdfyyyyMMdd_F.parse(endtimeday));
								c1.add(Calendar.DAY_OF_YEAR, 1);
								Calendar c2 = Calendar.getInstance();
								c2.setTime(c1.getTime());
								c2.add(Calendar.DAY_OF_YEAR, Integer.parseInt(bundle.getString(TAGTIME)));
								activity_rechargedetail_starttime.setText(sdfyyyyMMdd_F.format(c1.getTime()));
								activity_rechargedetail_endtime.setText(sdfyyyyMMdd_F.format(c2.getTime()));
								activity_rechargedetail_oldvalue.setText(storsum+"MB");
								activity_rechargedetail_oldendtime.setText(endtimeday);
							}
						}
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}
			if(storageflag){
				//未购买存储套餐
				Calendar c1 = Calendar.getInstance();
				c1.setTime(new Date());
				c1.add(Calendar.DAY_OF_YEAR, Integer.parseInt(bundle.getString(TAGTIME)));
				activity_rechargedetail_starttime.setText(sdfyyyyMMdd_F.format(new Date()));
				activity_rechargedetail_endtime.setText(sdfyyyyMMdd_F.format(c1.getTime()));
			}
			break;
		case 3:
			//基础
			activity_rechargedetail_fr_valid.setVisibility(View.GONE);
			activity_rechargedetail_value.setText(bundle.getString(TAGVALUE));
			//判断是否已经购买基础套餐
			if(getAppContext().isPayBasePackages()){
				Map<String,String> nData=lastNewPackages("3");
				if(nData!=null){
//					activity_rechargedetail_packages_tip.setVisibility(View.VISIBLE);
//					activity_rechargedetail_fr_oldaccount.setVisibility(View.VISIBLE);
					String endtimeday=nData.get("endtime").substring(0,10);
					try {
						SimpleDateFormat sdf=new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
						Calendar c1 = Calendar.getInstance();
						c1.setTime(sdf.parse(endtimeday));
						c1.add(Calendar.DAY_OF_YEAR, 1);
						Calendar c2 = Calendar.getInstance();
						c2.setTime(c1.getTime());
						c2.add(Calendar.DAY_OF_YEAR, Integer.parseInt(bundle.getString(TAGTIME)));
						activity_rechargedetail_starttime.setText(sdf.format(c1.getTime()));
						activity_rechargedetail_endtime.setText(sdf.format(c2.getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					int basesum=Integer.parseInt(nData.get("rectimelimit"))/60;
					int storsum=Integer.parseInt(nData.get("auciquotalimit"))/1024/1024;
					activity_rechargedetail_oldvalue.setText(basesum+"分钟\t"+storsum+"MB");
					activity_rechargedetail_oldendtime.setText(endtimeday);
				}
			}else{
				//没有购买任何套餐
				Calendar c1 = Calendar.getInstance();
				c1.setTime(new Date());
				c1.add(Calendar.DAY_OF_YEAR, Integer.parseInt(bundle.getString(TAGTIME)));
				activity_rechargedetail_starttime.setText(sdfyyyyMMdd_F.format(new Date()));
				activity_rechargedetail_endtime.setText(sdfyyyyMMdd_F.format(c1.getTime()));
			}
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		if(activity_rechargedetail_btn_payment==v){
			MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
			if (mspHelper.detectMobile_sp()){
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid", Constant.ACCESSID);
				requestParams.put("payuse","3");
				requestParams.put("recprod",getIntent().getExtras().getString(TAGCODE));
				if(bundle.getInt(TAGMODE_INT)==2||bundle.getInt(TAGMODE_INT)==3){
					//存储套餐和基础套餐为1
					requestParams.put("quantity","1");
				}else{
					//时长套餐根据份数
					requestParams.put("quantity",String.valueOf(activity_rechargedetail_num_spi5.getSelectedItem()));
				}
				//默认为1新购或者续费
				requestParams.put("actflag","1");
				if(bundle.getInt(TAGMODE_INT)==2){
					//存储
					for(Map<String,String> data:getAppContext().getPackagesinfos()){
						if("1".equals(data.get("ctype"))){
							try{
								Long starttime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("starttime"))));
								Long endtime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("endtime"))));
								Long currentTimeLong=Long.parseLong(sdfyyyyMMddHHmmss.format(new Date()));
								//只计算当前生效的套餐
								if(currentTimeLong>=starttime&&currentTimeLong<=endtime){
									int storsum=Integer.parseInt(data.get("auciquotalimit"))/1024/1024;
									int tarvalue=Integer.parseInt(bundle.getString(RechargeDetailActivity.TAGVALUE));
									if(tarvalue>storsum){
										//升级
										requestParams.put("actflag","2");
									}else if(tarvalue==storsum){
										//续费
										requestParams.put("actflag","1");
									}else{
										new AlertDialog.Builder(this)
										.setMessage("暂不支持存储套餐降容量级别购买，请选择存储容量等于或大于您当前在使用套餐的存储服务，多点空间才能有备无患哦。")
										.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												finish();
											}
										}).setCancelable(false).show();
										return;
									}
								}
							}catch(ParseException e){
								e.printStackTrace();
							}
						}
					}
				}
				getAppContext().exeNetRequest(this,Constant.GlobalURL.v4alipayReq,requestParams,null,new UIRunnable() {
					@Override
					public void run() {
						String reqContent=getInfoContent().get("reqcontent");
						MobileSecurePayer msp = new MobileSecurePayer();
						msp.pay(reqContent, handler,ALIPAY_MSG, RechargeDetailActivity.this);
					}
					
				});
			}
		}else if(activity_rechargedetail_btn_paymentback==v){
			finish();
		}
	}
	
	@Override
	public void processMessage(Message msg) {
		switch(msg.what){
			case ALIPAY_MSG:
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid", Constant.ACCESSID_LOCAL);
				requestParams.put("payproduct", "3");
				requestParams.put("rescontent", String.valueOf(msg.obj));
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign",Constant.ACCESSKEY_LOCAL);
				getAppContext().exeNetRequest(this,Constant.GlobalURL.v4ealipayRes,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								refreshUserInfo();
								//成功后返回我们账户需要刷新相应信息
								setResult(MyAccountActivity.RESULTREFRESHCODEMyAccountActivity);
								//充值成功
								activity_rechargedetail_result_content.setText("您已经为账户("+getAppContext().getCurrentLoginPhoneNumber(RechargeDetailActivity.this)+")购买: "+getIntent().getExtras().getString(TAGNAME));
								activity_rechargedetail_fr_detail.setVisibility(View.GONE);
								activity_rechargedetail_fr_result.setVisibility(View.VISIBLE);
							}
						});
					}
					
				});
				break;
		}
	}

	public Map<String,String> lastNewPackages(String ctype){
		Long nendtime=null;
		Map<String,String> ndata=null;
		for(Map<String,String> data:getAppContext().getPackagesinfos()){
			if(ctype.equals(data.get("ctype"))){
				try {
					Long endtime = Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("endtime"))));
					if(nendtime!=null){
						if(endtime>nendtime){
							nendtime=endtime;
							ndata=data;
						}
					}else{
						nendtime=endtime;
						ndata=data;
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return ndata;
	}
	
	public boolean isCurrentStorageExist(Map<String,String> exitData) throws ParseException{
		for(Map<String,String> data:getAppContext().getPackagesinfos()){
			if("1".equals(data.get("ctype"))){
				Long starttime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("starttime"))));
				Long endtime=Long.parseLong(sdfyyyyMMddHHmmss.format(sdfyyyyMMddHHmmss_F.parse(data.get("endtime"))));
				Long currentTimeLong=Long.parseLong(sdfyyyyMMddHHmmss.format(new Date()));
				//只计算当前生效的套餐
				if(!(currentTimeLong>=starttime&&currentTimeLong<=endtime)){
					if(Integer.parseInt(exitData.get("auciquotalimit"))==Integer.parseInt(data.get("auciquotalimit"))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
}