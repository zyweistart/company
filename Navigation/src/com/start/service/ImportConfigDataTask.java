package com.start.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.start.core.AppConfig;
import com.start.model.Book2Shelf;
import com.start.model.Building;
import com.start.model.ClassInfo;
import com.start.model.Course;
import com.start.model.Edge;
import com.start.model.Office;
import com.start.model.Room;
import com.start.model.Shelf;
import com.start.model.Teacher;
import com.start.model.Vertex;
import com.start.utils.Utils;

public class ImportConfigDataTask extends AsyncTask<Void, Void, Boolean> {

	private static final String DEBUG_TAG = "ImportConfigDataTask";
	private Context context;
	private AssetManager mAssetManager;
	private ContentResolver mContentResolver;
	private String message;
	private List<String> buildingNames = new ArrayList<String>();

	public ImportConfigDataTask(Context context) {
		super();
		this.context = context;
		mAssetManager = context.getAssets();
		mContentResolver = context.getContentResolver();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			for (String globalFile : mAssetManager.list(AppConfig.CONFIG_DATA_PATH)) {
				switch (AppConfig.getFileType(globalFile)) {
				case AppConfig.TYPE_BUILDING:
					importBuilding(globalFile);
					break;
				case AppConfig.TYPE_COURSE:
					importCourse(globalFile);
					break;
				case AppConfig.TYPE_TEACHER:
					importTeacher(globalFile);
					break;
				}
			}
		} catch (IOException e) {
			message = "Failed to import config data.";
			Log.e(DEBUG_TAG, e.getMessage());
			return false;
		}

		try {
			for (String buildingName : buildingNames) {
				for (String configFile : mAssetManager.list(AppConfig.CONFIG_DATA_PATH + "/" + buildingName)) {
					switch (AppConfig.getFileType(configFile)) {
					case AppConfig.TYPE_MAP:
						importMap(buildingName, configFile);
						break;
					case AppConfig.TYPE_ROOM:
						importRoom(buildingName, configFile);
						break;
					case AppConfig.TYPE_VERTEX:
						importVertex(buildingName, configFile);
						break;
					case AppConfig.TYPE_EDGE:
						importEdge(buildingName, configFile);
						break;
					case AppConfig.TYPE_CLASS:
						importClass(buildingName, configFile);
						break;
					case AppConfig.TYPE_OFFICE:
						importOffice(buildingName, configFile);
						break;
					case AppConfig.TYPE_SHELF:
						importShelf(buildingName, configFile);
						break;
					case AppConfig.TYPE_BOOK_2_SHELF:
						importBook2Shelf(buildingName, configFile);
						break;
					}
				}
			}
		} catch (IOException e) {
			message = "Failed to import config data.";
			Log.e(DEBUG_TAG, e.getMessage());
			return false;
		}

		return true;
	}

	private boolean importCourse(String globalFile) {
		boolean success = false;

		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH, globalFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 2) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Course.COLUMN_NAME_NAME, tokens[0]);
				values.put(Course.COLUMN_NAME_DESC, tokens[1]);
				mContentResolver.insert(Course.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import courses.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importTeacher(String globalFile) {
		boolean success = false;

		String filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH, globalFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 2) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Teacher.COLUMN_NAME_NAME, tokens[0]);
				values.put(Teacher.COLUMN_NAME_DESC, tokens[1]);
				mContentResolver.insert(Teacher.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import teachers.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importVertex(String buildingName, String configFile) {
		boolean success = false;

		int floor = AppConfig.getFloorByFileName(configFile);
		if (floor == Integer.MAX_VALUE) {
			return success;
		}

		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, configFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 4) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Vertex.COLUMN_NAME_NAME, tokens[0]);
				values.put(Vertex.COLUMN_NAME_LATITUDE, Double.valueOf(tokens[1]));
				values.put(Vertex.COLUMN_NAME_LONGITUDE, Double.valueOf(tokens[2]));
				values.put(Vertex.COLUMN_NAME_TYPE, Integer.valueOf(tokens[3]));
				values.put(Vertex.COLUMN_NAME_BUILDING, buildingName);
				values.put(Vertex.COLUMN_NAME_FLOOR, floor);
				mContentResolver.insert(Vertex.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import vertexes.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}
	
	private boolean importEdge(String buildingName, String configFile) {
		boolean success = false;

		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, configFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 2) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Edge.COLUMN_NAME_START, tokens[0]);
				values.put(Edge.COLUMN_NAME_END, tokens[1]);
				values.put(Edge.COLUMN_NAME_BUILDING, buildingName);
				mContentResolver.insert(Edge.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import edges.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importClass(String buildingName, String configFile) {
		boolean success = false;

		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, configFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 5) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(ClassInfo.COLUMN_NAME_ROOM, tokens[0]);
				values.put(ClassInfo.COLUMN_NAME_DAY, Integer.valueOf(tokens[1]));
				values.put(ClassInfo.COLUMN_NAME_DURATION, tokens[2]);
				values.put(ClassInfo.COLUMN_NAME_COURSE, tokens[3]);
				values.put(ClassInfo.COLUMN_NAME_TEACHER, tokens[4]);
				mContentResolver.insert(ClassInfo.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import classes.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importOffice(String buildingName, String configFile) {
		boolean success = false;

		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, configFile);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 3) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Office.COLUMN_NAME_ROOM, tokens[0]);
				values.put(Office.COLUMN_NAME_AFFAIR, tokens[1]);
				values.put(Office.COLUMN_NAME_STAFF, tokens[2]);
				mContentResolver.insert(Office.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import offices.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean("init", true);
			editor.commit();
		} else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	private boolean importMap(String buildingName, String mapFileName) {
		boolean success = false;

		int floor = AppConfig.getFloorByFileName(mapFileName);
		if (floor == Integer.MAX_VALUE) {
			return success;
		}
		
		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, mapFileName);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			Utils.writeStreamToExternalStorage(context, is, filePath);
			success = true;
		} catch (IOException e) {
			message = "Failed to import maps.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importBuilding(String filePath) {
		boolean success = false;
		InputStream is = null;
		
		filePath = String.format("%1$s/%2$s", AppConfig.CONFIG_DATA_PATH, filePath);
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 7) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Building.COLUMN_NAME_NAME, tokens[0]);
				values.put(Building.COLUMN_NAME_DESC, tokens[1]);
				values.put(Building.COLUMN_NAME_LATITUDE, Double.valueOf(tokens[2]));
				values.put(Building.COLUMN_NAME_LONGITUDE, Double.valueOf(tokens[3]));
				values.put(Building.COLUMN_NAME_MIN_FLOOR, Integer.valueOf(tokens[4]));
				values.put(Building.COLUMN_NAME_MAX_FLOOR, Integer.valueOf(tokens[5]));
				values.put(Building.COLUMN_NAME_GATE, tokens[6]);
				mContentResolver.insert(Building.CONTENT_URI, values);
				buildingNames.add(tokens[0]);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import buildings.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importRoom(String buildingName, String roomFileName) {
		boolean success = false;

		int floor = AppConfig.getFloorByFileName(roomFileName);
		if (floor == Integer.MAX_VALUE) {
			return success;
		}

		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, buildingName, roomFileName);
		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 7) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Room.COLUMN_NAME_TYPE, Integer.valueOf(tokens[0]));
				values.put(Room.COLUMN_NAME_LATITUDE1, Double.valueOf(tokens[1]));
				values.put(Room.COLUMN_NAME_LONGITUDE1, Double.valueOf(tokens[2]));
				values.put(Room.COLUMN_NAME_LATITUDE2, Double.valueOf(tokens[3]));
				values.put(Room.COLUMN_NAME_LONGITUDE2, Double.valueOf(tokens[4]));
				values.put(Room.COLUMN_NAME_NAME, tokens[5]);
				values.put(Room.COLUMN_NAME_VERTEX, tokens[6]);
				values.put(Room.COLUMN_NAME_BUILDING, buildingName);
				values.put(Room.COLUMN_NAME_FLOOR, floor);
				mContentResolver.insert(Room.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import rooms.";
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}

	private boolean importBook2Shelf(String building, String fileName) {
		boolean success = false;
		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, building, fileName);

		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 2) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Book2Shelf.COLUMN_NAME_NUM, tokens[0]);
				values.put(Book2Shelf.COLUMN_NAME_INDEX, tokens[1]);
				values.put(Book2Shelf.COLUMN_NAME_BUILDING, building);
				values.put(Book2Shelf.COLUMN_NAME_FLOOR, Integer.valueOf(fileName.substring(0, fileName.indexOf('.'))));
				mContentResolver.insert(Book2Shelf.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import book-shelf mapping: " + filePath;
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}
	
	private boolean importShelf(String building, String fileName) {
		boolean success = false;
		String filePath = String.format("%1$s/%2$s/%3$s", AppConfig.CONFIG_DATA_PATH, building, fileName);

		InputStream is = null;
		try {
			is = mAssetManager.open(filePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens == null || tokens.length < 6) {
					continue;
				}

				ContentValues values = new ContentValues();
				values.put(Shelf.COLUMN_NAME_NUM, Integer.valueOf(tokens[0]));
				values.put(Shelf.COLUMN_NAME_LATITUDE1, Double.valueOf(tokens[1]));
				values.put(Shelf.COLUMN_NAME_LONGITUDE1, Double.valueOf(tokens[2]));
				values.put(Shelf.COLUMN_NAME_LATITUDE2, Double.valueOf(tokens[3]));
				values.put(Shelf.COLUMN_NAME_LONGITUDE2, Double.valueOf(tokens[4]));
				values.put(Shelf.COLUMN_NAME_VERTEX, tokens[5]);
				values.put(Shelf.COLUMN_NAME_BUILDING, building);
				values.put(Shelf.COLUMN_NAME_FLOOR, Integer.valueOf(fileName.substring(0, fileName.indexOf('.'))));
				mContentResolver.insert(Shelf.CONTENT_URI, values);
			}
			success = true;
		} catch (IOException e) {
			message = "Failed to import book-shelf mapping: " + filePath;
			Log.e(DEBUG_TAG, e.getMessage());
		} finally {
			Utils.closeInputStream(is);
		}
		return success;
	}
}
