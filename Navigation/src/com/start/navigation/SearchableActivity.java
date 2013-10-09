package com.start.navigation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.start.core.AppConfig.Library;
import com.start.model.Book;
import com.start.model.Room;
import com.start.model.Searchable;
import com.start.utils.Utils;
/**
 * 检索
 * @author start
 *
 */
public class SearchableActivity extends ListActivity {
	
	public static final String DEBUG_TAG = "SearchableActivity";
	public static final String SEARCH_BOOK = "book";
	private static final int VIEW_BOOK = 1;

	private boolean searchingBook;
	private TextView mNotificationView;
	private ProgressBar mProgressBar;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_searchable);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
		
		ListView listView = getListView();
		View emptyView = findViewById(R.id.empty_view);
		mNotificationView = (TextView) emptyView.findViewById(R.id.notification);
		mProgressBar = (ProgressBar) emptyView.findViewById(android.R.id.progress);
		listView.setEmptyView(emptyView);		
		
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
			searchingBook = appData.getBoolean(SEARCH_BOOK, false);
			
			if (searchingBook) {
				new AsyncSearchBookTask().execute(query.trim());
			} else {
				mProgressBar.setVisibility(View.INVISIBLE);
				mNotificationView.setText(R.string.no_result);
				Cursor cr = managedQuery(Room.CONTENT_URI, null,Room.COLUMN_NAME_NAME + " LIKE '%" + query + "%'", null, null);
				SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.search_result_item, cr, 
						new String[] { Room.COLUMN_NAME_NAME, Room.COLUMN_NAME_BUILDING }, new int[]{android.R.id.text1, android.R.id.text2});
				setListAdapter(cursorAdapter);
			}
		} else {
			finish();
		}
	}

	private class AsyncSearchBookTask extends AsyncTask<String, Void, List<Searchable>> {

		@Override
		protected void onPostExecute(List<Searchable> books) {
			if (books.size() > 0) {
//				SearchResultAdapter adapter = new SearchResultAdapter(getLayoutInflater());
//				adapter.setData(books);
//				setListAdapter(adapter);
			} else {
				mProgressBar.setVisibility(View.INVISIBLE);
				mNotificationView.setText(R.string.no_result);
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			mNotificationView.setText(R.string.searching);
			super.onPreExecute();
		}

		@Override
		protected List<Searchable> doInBackground(String... params) {
			String keyword = params[0];
			URL url = Library.getQueryURL(keyword);
			String html = Utils.openUrl(url);

			final List<Searchable> bs = new ArrayList<Searchable>();
			if (html != null) {
				Document doc = Jsoup.parse(html);
				Elements elements = doc.select("ol#search_book_list");
				if (elements.size() > 0) {
					Elements bookElements = elements.get(0).getElementsByClass("book_list_info");
					
					for (Element bookElem : bookElements) {
						Book b = new Book();
						Element bookHeader = bookElem.select("h3").first();
						b.setIndex(bookHeader.ownText());
						Element bookHref = bookHeader.select("a").first();
						b.setName(bookHref.text());
						b.setUrl(bookHref.attr("href"));
						bs.add(b);
					}
				}
			} else {
				Log.d(DEBUG_TAG, "get null html content when query");
			}
			return bs;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (searchingBook) {
			Intent intent = new Intent(this, BookDetailsActivity.class);
			intent.putExtra("data", (Book) l.getItemAtPosition(position));
			startActivityForResult(intent, VIEW_BOOK);
		} else {
			Uri uri = Uri.withAppendedPath(Room.CONTENT_ID_URI_BASE, String.valueOf(id));
			AppContext.getInstance().setPathSearchResult(null);

			Intent intent = new Intent(this, MapIndoorActivity.class);
			intent.setData(uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			setResult(RESULT_OK);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
}
