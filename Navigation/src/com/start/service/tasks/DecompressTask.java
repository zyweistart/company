package com.start.service.tasks;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.start.core.Constant;
import com.start.navigation.R;
import com.start.utils.CommonFn;
import com.start.utils.ZipUtils;

public class DecompressTask extends AsyncTask<String, Float, Boolean> {

	private Context mContext;
	private ProgressDialog pDialog;
	
	public DecompressTask(Context context){
		this.mContext=context;
	}

	@Override
	protected void onPreExecute() {
		pDialog=CommonFn.progressDialog(mContext, mContext.getString(R.string.msg_decompressing_datafile));
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
//				decompressFile.delete();
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();
		if(result){
			
		}
	}
	
}