package com.start.navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.start.core.CoreActivity;
import com.start.model.AbsClass;
import com.start.model.ClassInfo;
import com.start.model.Course;
import com.start.model.RemoteClassInfo;
import com.start.model.Teacher;
import com.start.service.adapter.CourseListAdapter;
import com.start.utils.CommonFn;
import com.start.utils.HttpHelper;

public class ClassTableActivity extends CoreActivity implements OnItemClickListener {

	private static final boolean USE_LOCAL_DATA = false;

	private AsyncTask<String, Void, List<AbsClass>> mLoader = new AsyncTask<String, Void, List<AbsClass>>() {

		@Override
		protected List<AbsClass> doInBackground(String... params) {
			Cursor cr = getContentResolver().query(ClassInfo.CONTENT_URI, null, ClassInfo.COLUMN_NAME_ROOM + "=?", new String[]{params[0]}, ClassInfo._ID + " ASC");
			
			ArrayList<AbsClass> classes = new ArrayList<AbsClass>();
			try {
				if (cr.moveToNext()) {
					int dayIndex = cr.getColumnIndex(ClassInfo.COLUMN_NAME_DAY);
					int durationIndex = cr.getColumnIndex(ClassInfo.COLUMN_NAME_DURATION);
					int courseIndex = cr.getColumnIndex(ClassInfo.COLUMN_NAME_COURSE);
					int teacherIndex = cr.getColumnIndex(ClassInfo.COLUMN_NAME_TEACHER);
								
					do {
						ClassInfo c = new ClassInfo(cr.getInt(dayIndex), cr.getString(durationIndex), cr.getString(courseIndex), cr.getString(teacherIndex));
						classes.add(c);
					} while (cr.moveToNext());
				}
			} finally {
				cr.close();
			}
			
			if (classes.size() > 0) {
				CourseListAdapter.sortCourse(classes);
			}
			return classes;
		}

		@Override
		protected void onPostExecute(List<AbsClass> result) {
			super.onPostExecute(result);
			adapter.setData(result);
			adapter.notifyDataSetChanged();
			showMessage(false, R.string.no_course);
		}
		
	};

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		AbsClass c = (AbsClass) adapterView.getItemAtPosition(position);
		
		Intent intent = new Intent(this, ClassActivity.class);
		intent.putExtra(Course.TABLE_NAME, c.getCourse());
		intent.putExtra(Teacher.TABLE_NAME, c.getTeacher());
		startActivity(intent);
	}
	
	private CourseListAdapter adapter;
	
	private String queryUrl;
	private String viewState;
	private String classroomName;
	
	private View checkCodeView;
	private ProgressBar progressBar;
	private TextView messageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_course_of_classroom);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
		
		LinearLayout emptyView = (LinearLayout) findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(android.R.id.progress);
		messageText = (TextView) emptyView.findViewById(R.id.tips);

		ListView courseList = (ListView) findViewById(R.id.course_list);
		adapter = new CourseListAdapter(this);
		courseList.setAdapter(adapter);
		courseList.setEmptyView(emptyView);
		courseList.setOnItemClickListener(this);
		
		classroomName = getIntent().getStringExtra(ClassInfo.COLUMN_NAME_ROOM);

		if (USE_LOCAL_DATA) {
			mLoader.execute(classroomName);
		} else {
			if (HttpHelper.isSessionExpired()) {
				new PrepareLogonTask().execute();
			} else {
				queryUrl = HttpHelper.getQueryUrl();
				new LoadQueryPageTask().execute(queryUrl);
			}	
		}
	}
	
	private void showMessage(boolean progress, int id) {
		if (progress) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
		messageText.setText(id);
	}

	private class PrepareLogonTask extends AsyncTask<Void, Integer, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showMessage(true, R.string.loading);
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			HttpGet request = new HttpGet(HttpHelper.HOST);
			HttpResponse response = HttpHelper.get(request);
			if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			boolean success = false;
			InputStream is = null;
			try {
				is = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, HttpHelper.CHARSET_NAME));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.contains("__VIEWSTATE")) {
						viewState = line.substring(line.indexOf(HttpHelper.VIEWSTATE_START) + HttpHelper.VIEWSTATE_START.length(), line.indexOf(HttpHelper.VIEWSTATE_END));
						break;
					}
				}
				success = true;
			} catch (IOException e) {
			} finally {
				CommonFn.close(is);
			}
			
			request.abort();
			
			if (!success) {
				return null;
			}
					
			request = new HttpGet(HttpHelper.CHECKCODE_URL);
			response = HttpHelper.get(request);
			if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			Bitmap bitmap = null;
			try {
				is = response.getEntity().getContent();
				bitmap = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
			} finally {
				CommonFn.close(is);
			}
			
			request.abort();

			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result == null) {
				Log.d(HttpHelper.TAG, "null checkcode");
				showMessage(false, R.string.logon_fail_and_refresh);
				return;
			}

			if (checkCodeView == null) {
				View viewStub = findViewById(R.id.stub);
				checkCodeView = ((ViewStub) viewStub).inflate();
			}
			
			((ImageView) checkCodeView.findViewById(R.id.image)).setImageBitmap(result);
			final EditText checkCode = (EditText) checkCodeView.findViewById(R.id.checkcode);
			Button postBtn = (Button) checkCodeView.findViewById(R.id.post);
			postBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if (checkCode.getText().length() == 4) {
						new LogonTask().execute(checkCode.getText().toString());
					}
				}
			});
		}
	}

	private class LogonTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showMessage(true, R.string.logoning);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(checkCodeView.getWindowToken(), 0);
			checkCodeView.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewState));
			formparams.add(new BasicNameValuePair("TextBox1", "1214000"));
			formparams.add(new BasicNameValuePair("TextBox2", "1qaz2wsx"));
			formparams.add(new BasicNameValuePair("TextBox3", params[0]));
			formparams.add(new BasicNameValuePair("RadioButtonList1", "教师"));
			formparams.add(new BasicNameValuePair("Button1", null));
			formparams.add(new BasicNameValuePair("lbLanguage", null));

			UrlEncodedFormEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(formparams, HttpHelper.CHARSET_NAME);
				entity.setContentEncoding(HttpHelper.CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}

			HttpPost request = new HttpPost(HttpHelper.LOGON_URL);
			request.setEntity(entity);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");

			// Logon
			HttpResponse response = HttpHelper.post(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			InputStream is = null;
			String extractUrlLine= null;
			try {
				is = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, HttpHelper.CHARSET_NAME));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.contains("kbcx.aspx")) {
						extractUrlLine = line.substring(line.indexOf("kbcx.aspx"));
						break;
					}
				}
				
				if (extractUrlLine != null && extractUrlLine.startsWith("kbcx.aspx")) {
					extractUrlLine = extractUrlLine.substring(0, extractUrlLine.indexOf("\""));
				}
			} catch (IOException e) {
			} finally {
				CommonFn.close(is);
			}
			request.abort();
			
			if (extractUrlLine == null) {
				return null;
			}

			String name = extractUrlLine.substring(extractUrlLine.indexOf("xm=") + "xm=".length(), extractUrlLine.indexOf("&gnmkdm"));
			try {
				queryUrl = HttpHelper.HOST + extractUrlLine.replace(name, URLEncoder.encode(name, HttpHelper.CHARSET_NAME));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
			
			request.abort();
			return queryUrl;
		}
		
		@Override
		protected void onPostExecute(String url) {
			super.onPostExecute(url);

			if (url != null) {
				Log.d(HttpHelper.TAG, "Logon success");
				HttpHelper.updateSession(false, queryUrl);
				new LoadQueryPageTask().execute(url);
			} else {
				Log.d(HttpHelper.TAG, "Logon failed");
				HttpHelper.updateSession(true, null);
				showMessage(false, R.string.logon_fail_and_refresh);
			}
		}
	}

	private class LoadQueryPageTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			showMessage(true, R.string.loading);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpGet getRequest = null;
			getRequest = new HttpGet(params[0]);

			URI referrerURI = HttpHelper.getReferrer();
			String referrer = referrerURI == null ? HttpHelper.HOST + "js_main.aspx" : referrerURI.toString();
			getRequest.setHeader("Referer", referrer);

			// switch to kbcx page
			HttpResponse response = HttpHelper.get(getRequest);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return false;
			}

			boolean success = false;
			InputStream is = null;
			try {
				is = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, HttpHelper.CHARSET_NAME));
				String line = null;
				String newViewState = null;
				while ((line = reader.readLine()) != null) {
					if (line.contains("__VIEWSTATE")) {
						newViewState = line.substring(line.indexOf(HttpHelper.VIEWSTATE_START));
						newViewState = newViewState.substring(HttpHelper.VIEWSTATE_START.length(), newViewState.indexOf(HttpHelper.VIEWSTATE_END));
						break;
					}
				}
				success = newViewState != null;
				viewState = newViewState;
			} catch (IOException e) {
			} finally {
				CommonFn.close(is);
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				new QueryTask().execute(classroomName);
			} else {
				HttpHelper.updateSession(true, null);
				showMessage(false, R.string.logon_fail_and_refresh);
			}
		}
	}

	private class QueryTask extends AsyncTask<String, Void, List<AbsClass>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showMessage(true, R.string.querying);
		}

		@Override
		protected List<AbsClass> doInBackground(String... params) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			formparams.add(new BasicNameValuePair("__VIEWSTATE", viewState));
			formparams.add(new BasicNameValuePair("xn", "2012-2013"));
			formparams.add(new BasicNameValuePair("xq", "2"));
			formparams.add(new BasicNameValuePair("zc", null));
			formparams.add(new BasicNameValuePair("xqj", null));
			formparams.add(new BasicNameValuePair("sjd", null));
			formparams.add(new BasicNameValuePair("xiaoq", null));
			formparams.add(new BasicNameValuePair("jslb", null));
			formparams.add(new BasicNameValuePair("min_zws", "0"));
			formparams.add(new BasicNameValuePair("max_zws", null));
			formparams.add(new BasicNameValuePair("xkkh", null));
			formparams.add(new BasicNameValuePair("kcmc", null));
			formparams.add(new BasicNameValuePair("jsxm", null));
			formparams.add(new BasicNameValuePair("kkxy", null));
			formparams.add(new BasicNameValuePair("ddlkkxy", null));
			if (classroomName.contains("-")) {
				formparams.add(new BasicNameValuePair("jsmc", classroomName.substring(0, classroomName.indexOf("-"))));
			} else {
				formparams.add(new BasicNameValuePair("jsmc", classroomName));
			}
			formparams.add(new BasicNameValuePair("Button2", "按条件查询上课情况"));

			UrlEncodedFormEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(formparams, HttpHelper.CHARSET_NAME);
				entity.setContentEncoding(HttpHelper.CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}

			HttpPost request = new HttpPost(queryUrl);
			request.setEntity(entity);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setHeader("Referer", queryUrl);
			
			HttpResponse response = HttpHelper.post(request);
			if (response == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			InputStream is = null;
			List<AbsClass> courses = null;
			try {
				is = response.getEntity().getContent();
				courses = parseHtml(is);
			} catch (IOException e) {
			} finally {
				CommonFn.close(is);
			}
			
			request.abort();

			if (courses != null && courses.size() > 0) {
				CourseListAdapter.sortCourse(courses);
			}
			return courses;
		}

		@Override
		protected void onPostExecute(List<AbsClass> result) {
			super.onPostExecute(result);
			Log.d(HttpHelper.TAG, "query finished");
			showMessage(false, R.string.no_course);
			if (result != null && result.size() > 0) {
				adapter.setData(result);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private List<AbsClass> parseHtml(InputStream is) throws IOException {
		Document doc = Jsoup.parse(is, HttpHelper.CHARSET_NAME, HttpHelper.HOST);
		Elements elements = doc.select("table#Datagrid2");
		
		if (elements == null || elements.size() == 0) return null;
		
		Element table = elements.first();
		if (table == null) return null;
		
		Element tbody = table.child(0);
		if (tbody == null) return null;
		
		elements = tbody.children();
		if (elements == null) return null;
		
		List<AbsClass> courses = new ArrayList<AbsClass>();
		for (int i = 1; i < elements.size(); i++) {
			Element element = elements.get(i);
			
			RemoteClassInfo course = new RemoteClassInfo(element.child(1).text(), element.child(2).text(), element.child(3).text());
			Log.d(HttpHelper.TAG, course.getCourse());
			int start = 0, end = 0;
			for (int w = 4; w < 24; w++) {
				String text = element.child(w).text();
				if (start == 0 && text.equals("★")) {
					start = w;
					continue;
				}
				if (start != 0 && end == 0 && !text.equals("★")) {
					end = w;
					break;
				}
			}
			if (start == 0 || end == 0) {
				continue;
			} else {
				course.setStartWeek(start);
				course.setEndWeek(end);
				courses.add(course);
			}
		}
		
		return courses;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.refresh, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!USE_LOCAL_DATA && item.getItemId() == R.id.refresh) {
			adapter.setData(new ArrayList<AbsClass>());
			adapter.notifyDataSetChanged();
			if (HttpHelper.isSessionExpired()) {
				new PrepareLogonTask().execute();
			} else {
				new LoadQueryPageTask().execute(queryUrl);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
