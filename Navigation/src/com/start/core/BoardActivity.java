package com.start.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.start.navigation.R;

public abstract class BoardActivity extends CoreActivity {

	protected ProgressDialog mProgress;

	protected void showProgressDialog() {
		mProgress = ProgressDialog.show(this, null, getString(R.string.loading), true, true, new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				finish();				
			}
			
		});
	}
	
	protected void dismissDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}
	
	protected void startLoader(String...params) {
		new AsyncTask<String, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressDialog();
			}

			@Override
			protected Boolean doInBackground(String... params) {
				return doLoad(params);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissDialog();

				if (result) {
					setupView();
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.no_result), Toast.LENGTH_SHORT).show();
				}			
			}
		}.execute(params);
	}

	@Override
	protected void onStop() {
		dismissDialog();
		super.onStop();
	}
	
	abstract protected void setupView();
	abstract protected boolean doLoad(String...params);
	
}
