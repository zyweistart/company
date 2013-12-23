package com.start.service.tasks;

import java.io.File;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.start.core.CoreActivity;
import com.start.navigation.AppContext;
import com.start.navigation.R;
import com.start.utils.CommonFn;
import com.start.utils.FileUtils;
import com.start.utils.Utils;

public class ReDownloadTask extends AsyncTask<Void, Float, Boolean> {

	private String fileno;
	private CoreActivity mContext;
	private AppContext mAppContext;
	private ProgressDialog pDialog;
	
	public ReDownloadTask(CoreActivity context,String fileno){
		this.mContext=context;
		this.fileno=fileno;
		this.mAppContext=AppContext.getInstance();
	}

	@Override
	protected void onPreExecute() {
		pDialog=CommonFn.progressDialog(mContext, mContext.getString(R.string.msg_clear_datafile));
		pDialog.show();
		pDialog.setCancelable(false);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		File dataFile=Utils.getFile(mContext, fileno);
		if(FileUtils.delFolder(dataFile.getAbsolutePath())){
			mAppContext.getDepartmentService().clearAll();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();
		if(result){
			new DownloadTask(mContext,fileno).execute();
		}
	}
	
}