package com.start.navigation;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.start.core.BoardActivity;
import com.start.model.Book;
import com.start.model.Book2Shelf;
import com.start.model.Shelf;

public class BookDetailsActivity extends BoardActivity {

	protected ShelfListAdapter adapter;

	@Override
	protected void setupView() {
		adapter.notifyDataSetChanged();
	}

	@Override
	protected boolean doLoad(String... params) {
		String bookIndex = getBookIndex(params[0]);
		ContentResolver resolver = getContentResolver();
		Cursor cr = resolver.query(Book2Shelf.CONTENT_URI, null, Book2Shelf.COLUMN_NAME_INDEX + " LIKE '" + bookIndex + "%'", null, null);

		List<Shelf> shelves = new ArrayList<Shelf>(cr.getCount());
		if (cr.moveToNext()) {
			int num = cr.getColumnIndex(Book2Shelf.COLUMN_NAME_NUM);
			int index = cr.getColumnIndex(Book2Shelf.COLUMN_NAME_INDEX);
			int building = cr.getColumnIndex(Book2Shelf.COLUMN_NAME_BUILDING);
			int floor = cr.getColumnIndex(Book2Shelf.COLUMN_NAME_FLOOR);
			
			do {
				Shelf shelf = new Shelf(cr.getInt(num), cr.getString(index), cr.getString(building), cr.getInt(floor));
				shelves.add(shelf);
			} while (cr.moveToNext());
		}
		cr.close();

		adapter.setData(shelves);
		return true;
	}

	private String getBookIndex(String bookIndex) {
		int dot = bookIndex.indexOf('.');
		int slash = bookIndex.indexOf('/');
		if (slash > 0) {
			if (dot > 0) {
				bookIndex = bookIndex.substring(0, Math.min(slash, dot));
			} else {
				bookIndex = bookIndex.substring(0, slash);
			}
		} else if (dot > 0) {
			bookIndex = bookIndex.substring(0, dot);
		}

		return bookIndex;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_book_details);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);		
		
		adapter = new ShelfListAdapter(getLayoutInflater());
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				AppContext.getInstance().setPathSearchResult(null);

				Shelf shelf = (Shelf) adapterView.getItemAtPosition(position);
				Cursor cr = getContentResolver().query(Shelf.CONTENT_URI, new String[]{Shelf._ID}, Shelf.COLUMN_NAME_NUM + "=? AND " + Shelf.COLUMN_NAME_BUILDING + "=? AND " + Shelf.COLUMN_NAME_FLOOR + "=?", 
						new String[] { String.valueOf(shelf.getNum()), shelf.getBuilding(), String.valueOf(shelf.getFloor()) }, null);
				
				if (cr.moveToNext()) {
					long shelfID = cr.getLong(cr.getColumnIndex(Shelf._ID));

					Intent intent = new Intent(getApplicationContext(), MapIndoorActivity.class);
					intent.setData(Uri.withAppendedPath(Shelf.CONTENT_ID_URI_BASE, String.valueOf(shelfID)));
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					
					setResult(RESULT_OK);
					startActivity(intent);
					finish();
				}
				cr.close();
			}
		});

		Bundle extras = getIntent().getExtras();
		Book book = (Book) extras.getSerializable("data");
		((TextView) findViewById(R.id.name)).setText(book.getName());
		((TextView) findViewById(R.id.index)).setText(book.getIndex());

		startLoader(book.getIndex());
	}
	
	private class ShelfListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Shelf> shelves;

		public ShelfListAdapter(LayoutInflater inflater) {
			this.inflater = inflater;
		}

		public void setData(List<Shelf> shelves) {
			this.shelves = shelves;
		}

		@Override
		public int getCount() {
			return shelves != null ? shelves.size() : 0;
		}

		@Override
		public Shelf getItem(int position) {
			return shelves.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_result_item, parent, false);
			}
			Shelf shelf = getItem(position);
			((TextView) convertView.findViewById(android.R.id.text1)).setText(shelf.getName());
			((TextView) convertView.findViewById(android.R.id.text2)).setText(shelf.getBuilding());
			return convertView;
		}
		
	}
}
