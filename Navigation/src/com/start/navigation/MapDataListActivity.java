package com.start.navigation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.UIRunnable;
import com.start.navigation.MapDataListActivity.MapDataPullListAdapter.MapDataViewHolder;
import com.start.service.HttpService.LoadMode;
import com.start.service.PullListViewData;
import com.start.service.PullListViewData.OnLoadDataListener;
import com.start.service.tasks.ImportDataFileTask;
import com.start.utils.CommonFn;
import com.start.utils.HttpUtils;
import com.start.utils.MD5;
import com.start.utils.StringUtils;
import com.start.utils.XMLUtils;
import com.start.utils.ZipUtils;

/**
 * 地图数据列表
 * @author start
 *
 */
public class MapDataListActivity extends CoreActivity implements OnClickListener{

	private PullListViewData mapDataPullListData;
	
	public static final String FILENO="fileno";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_data_list);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!getAppContext().isLogin()){
			
			CommonFn.buildDialog(this, R.string.msg_not_login, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(MapDataListActivity.this,LoginActivity.class));
				}
				
			}).show();
			
		}else{
			mapDataPullListData=new PullListViewData(this);
			mapDataPullListData.setOnLoadDataListener(
					new OnLoadDataListener(){

						@Override
						public void LoadData(LoadMode loadMode) {
							Map<String,String> requestParams=new HashMap<String,String>();
							requestParams.put("accessid",Constant.ACCESSID);
							mapDataPullListData.sendPullToRefreshListViewNetRequest(loadMode,Constant.GlobalURL.v4recQry,requestParams,null,new UIRunnable(){
								@Override
								public void run() {
									mapDataPullListData.getAdapter().notifyDataSetChanged();
								} 
							},"reclist","reclist"+TAG);
						}
						
					});
			mapDataPullListData.start(R.id.activity_map_data_pulllistview, 
					new MapDataPullListAdapter(mapDataPullListData));
		}
	}
	
	public class MapDataPullListAdapter extends PullListViewData.DataAdapter{
		
		public MapDataPullListAdapter(PullListViewData pullListViewData) {
			pullListViewData.super();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapDataViewHolder holder;
			if (convertView != null && convertView.getId() == R.id.lvitem_mapdata_content) {
				holder = (MapDataViewHolder) convertView.getTag();
			}else{
				convertView = getLayoutInflater().inflate(R.layout.lvitem_mapdata, null);
				holder = new MapDataViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.lvitem_mapdata_name);
				holder.download = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_download);
				holder.download.setOnClickListener(MapDataListActivity.this);
				holder.use = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_use);
				holder.use.setOnClickListener(MapDataListActivity.this);
				holder.detail = (Button) convertView.findViewById(R.id.lvitem_mapdata_btn_detail);
				holder.detail.setOnClickListener(MapDataListActivity.this);
				convertView.setTag(holder);
			}
			Map<String,String> data=mapDataPullListData.getDataItemList().get(position);
			holder.fileno=data.get(FILENO);
			holder.name.setText("地图:"+data.get("oppno"));
			File downFile=new File(new File(Environment.getExternalStorageDirectory().getPath()+Constant.DATADIRFILE),holder.fileno);
			if(downFile.exists()){
				holder.download.setVisibility(View.GONE);
				holder.use.setVisibility(View.VISIBLE);
			}else{
				holder.download.setVisibility(View.VISIBLE);
				holder.use.setVisibility(View.GONE);
			}
			holder.download.setTag(holder);
			holder.use.setTag(holder);
			holder.detail.setTag(holder);
			return convertView;
		}
		
		public class MapDataViewHolder {
			String fileno;
			TextView name;
			Button download;
			Button use;
			Button detail;
		}
		
	}

	@Override
	public void onClick(View v) {
		final MapDataViewHolder vh=(MapDataViewHolder)v.getTag();
		if(vh==null)return;
		if(v.getId()==R.id.lvitem_mapdata_btn_download){
//			if(NetConnectManager.isMobilenetwork(this)){
//				new AlertDialog.Builder(this)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setMessage(R.string.msg_use_mobile_data)
//				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.dismiss();
//					}
//				}).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						new DownloadTask().execute(vh.fileno);
//					}
//				}).show();
//			}else{
//				new DownloadTask().execute(vh.fileno);
//			}
			new DecompressTask().execute("a586054a207abc9fe4fe4945e5c666dc");
		}else if(v.getId()==R.id.lvitem_mapdata_btn_use){
			//使用当前数据
			new ImportDataFileTask(this).execute("a586054a207abc9fe4fe4945e5c666dc");
		}else if(v.getId()==R.id.lvitem_mapdata_btn_detail){
//			Bundle bundle=new Bundle();
//			bundle.putString(FILENO, vh.fileno);
//			Intent intent=new Intent(this,MapDataDetailActivity.class);
//			intent.putExtras(bundle);
//			startActivity(intent);
		}
	}
	
	public class DownloadTask extends AsyncTask<String, Float, File> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// 创建ProgressDialog对象  
			pDialog = new ProgressDialog(MapDataListActivity.this);  
			// 设置进度条风格，风格为圆形，旋转的  
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			// 设置ProgressDialog 标题  
			pDialog.setTitle(R.string.prompt);  
			// 设置ProgressDialog提示信息  
			pDialog.setMessage(getString(R.string.msg_downloading_datafile));  
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
			String dxternalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
			File tempDirFile=new File(dxternalStorageDirectory+Constant.TMPDIRFILE);
			if(!tempDirFile.exists()){
				tempDirFile.mkdirs();
			}
			File downFile=new File(tempDirFile,fileno);
			if(!downFile.exists()){
				try{
					Map<String,String> pars=new HashMap<String,String>();
					pars.put("accessid",Constant.ACCESSID);
					pars.put(FILENO,fileno);
					String requestContent = XMLUtils.builderRequestXml(Constant.GlobalURL.v4DataFileDownload, pars);
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
						File tmpFile=new File(tempDirFile,fileno+".tmp");
						if(tmpFile.exists()){
							tmpFile.delete();
						}
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
									//文件大小不一致
									makeTextLong(R.string.msg_error_please_try_again);
									tmpFile.delete();
								}
							}
						}
					}else{
						//文件下载失败
						makeTextLong(R.string.msg_error_please_try_again);
					}
				}catch(Exception e){
					makeTextLong(R.string.msg_error_please_try_again);
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
				new DecompressTask().execute("a586054a207abc9fe4fe4945e5c666dc");
			}
		}
	}
	
	public class DecompressTask extends AsyncTask<String, Float, Boolean> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog=CommonFn.progressDialog(MapDataListActivity.this, getString(R.string.msg_decompressing_datafile));
			pDialog.show();
			pDialog.setCancelable(false);
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			final String fileno=params[0];
			String externalStorageDirectory=Environment.getExternalStorageDirectory().getPath();
			File decompressFile=new File(new File(externalStorageDirectory+Constant.TMPDIRFILE),fileno);
			if(decompressFile.exists()){
				try {
					String folderPath=externalStorageDirectory+Constant.DATADIRFILE+fileno+"/";
					ZipUtils.UnZipFolder(decompressFile.getAbsolutePath(), folderPath);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					//TODO:只要执行结束则删除
//					decompressFile.delete();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pDialog.dismiss();
			if(result){
				mapDataPullListData.getAdapter().notifyDataSetChanged();
			}
		}
		
	}
	
}