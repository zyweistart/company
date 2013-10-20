package com.start.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.start.core.AppConfig;
import com.start.model.Building;
import com.start.model.ClassInfo;
import com.start.model.Office;
import com.start.model.POI;
import com.start.model.Room;
import com.start.model.Shelf;
import com.start.model.nav.EndPoint;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MyLocation;
import com.start.model.nav.NavRoute;
import com.start.model.nav.NavStep;
import com.start.model.nav.OutdoorEndPoint;
import com.start.model.nav.PathSearchResult;
import com.start.model.nav.PathSearchResult.Type;
import com.start.model.overlay.MyLocationMarker;
import com.start.model.overlay.POIMarker;
import com.start.service.OnTapMapListener;
import com.start.service.adapter.FloorIndexAdapter;
import com.start.service.task.PathSearchTask;
import com.start.service.task.PathSearchTask.PathSearchListener;
import com.start.utils.CommonFn;
import com.start.utils.Utils;

public class MapIndoorActivity extends MapActivity implements OnItemClickListener, OnTapMapListener.OnClickListener, View.OnClickListener, PathSearchListener {

	private boolean searchBook;
	
	private Building mBuilding;
	private Room[][] mRooms;

	private int mListViewCheckedPos;
	private ListView mListView;
	private FloorIndexAdapter mFloorAdapter;

	private Paint mPaintStroke;
	
	private MapView mMapView;
	private ListOverlay mListOverlay;
	private POIMarker mPOIMarker;
	private MyLocationMarker mMyLocMarker;
	
	private GestureDetector mGestureDetector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_map_indoor);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);

		mGestureDetector = new GestureDetector(new OnTapMapListener(this));

		ViewGroup container = (ViewGroup) findViewById(R.id.container);
		mMapView = new MapView(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		mMapView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});
		container.addView(mMapView, 0);

		mFloorAdapter = new FloorIndexAdapter(getLayoutInflater());
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(mFloorAdapter);
		mListView.setOnItemClickListener(this);

		mListOverlay = new ListOverlay();
		mMapView.getOverlays().add(mListOverlay);
		handleIntent();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		ViewGroup actionBar = (ViewGroup) findViewById(R.id.actionbar);
		actionBar.findViewById(R.id.actionbar_search).setVisibility(View.VISIBLE);
		((TextView) actionBar.findViewById(R.id.actionbar_title)).setText(mBuilding.getDesc());
	}

	@Override
	public boolean onSearchRequested() {
		Bundle searchTarget = new Bundle();
		searchTarget.putBoolean(SearchableActivity.SEARCH_BOOK, searchBook);
		startSearch(null, false, searchTarget, false);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case Utils.DLG_SEARCH_OPTION:
			dialog = CommonFn.createSearchOptionDialog(this);
			break;
		case Utils.DLG_POI:
			dialog = CommonFn.createPOIDialog(this);
			break;
		case Utils.DLG_EXIT_NAVIGATION:
			dialog = CommonFn.alertDialog(this, R.string.exit_navigation, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppContext.getInstance().setPathSearchResult(null);
					updateOverlay();
				}
			});
			break;
		default:
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		if (id == Utils.DLG_POI) {
			POI poi = (POI) args.getSerializable("data");
			((TextView) dialog.findViewById(R.id.poiName)).setText(poi.getName());
			dialog.findViewById(R.id.direction).setTag(poi);
			dialog.findViewById(R.id.poiName).setTag(poi);
			if (mPOIMarker != null) {
				dialog.getWindow().getAttributes().y = -50;
			} else {
				dialog.getWindow().getAttributes().y = 0;
			}
			return;
		} else {
			super.onPrepareDialog(id, dialog, args);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.actionbar_search) {
			showDialog(Utils.DLG_SEARCH_OPTION);
		} else if (id == R.id.room) {
			searchBook = false;
			onSearchRequested();
		} else if (id == R.id.book) {
			searchBook = true;
			onSearchRequested();
		} else if (id == R.id.poiName) {
			POI r = (POI) v.getTag();
			if (r instanceof Room) {
				Intent intent = new Intent();
				Room room = (Room) r;
				if (room.getType() == Room.CLASSROOM) {
					intent.putExtra(ClassInfo.COLUMN_NAME_ROOM, r.getName());
					intent.setClass(this, ClassTableActivity.class);
				} else if (room.getType() == Room.OFFICE) {
					intent.putExtra(Office.COLUMN_NAME_ROOM, r.getName());
					intent.setClass(this, OfficeActivity.class);
				} else {
					return;
				}
				startActivity(intent);
			}
		} else if (id == R.id.direction) {
			PathSearchTask search = new PathSearchTask(this);
			POI r = (POI) v.getTag();
			MyLocation myLocation = AppContext.getInstance().getMyLocation();
			if (myLocation != null) {
				EndPoint sp = null;
				//当前自己的位置
				if (myLocation.isIndoor()) {
					sp = new IndoorEndPoint(myLocation.getBuilding(), myLocation.getFloor(), myLocation.getGeoPoint());
				} else {
					sp = new OutdoorEndPoint(myLocation.getOutdoorPoint());
				}
				//目标位置
				EndPoint ep = new IndoorEndPoint(mBuilding.getName(), r.getFloor(), r.getGeoPoint(), r.getVertex());
				search.execute(sp, ep);
			} else {
				Toast.makeText(this, R.string.my_location_unavailable, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClickAt(float xPixel, float yPixel) {
		Projection projection = mMapView.getProjection();
		if (projection == null) {
			return;
		}

		GeoPoint g = projection.fromPixels((int) xPixel, (int) yPixel);

		if (mPOIMarker != null) {
			POI poi = mPOIMarker.getPOI();
			if (poi.inside(g)) {
				tapPOI(poi);
				return;
			}
		}

		if (mRooms == null) {
			return;
		}

		int index = toFloorNum(mListViewCheckedPos) - mBuilding.getMinFloor();
		//获取当前楼层索引的所有房间
		Room[] rooms = mRooms[index];
		for (Room r : rooms) {
			//单击的点是否在当前房间内
			if (r.inside(g)) {
				tapPOI(r);
				return;
			}
		}
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		if (mListViewCheckedPos == position) {
			return;
		}

		mListViewCheckedPos = position;
		setMapFile(toListIndex(mListViewCheckedPos));
		updateOverlay();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (AppContext.getInstance().getPathSearchResult() != null && AppContext.getInstance().getPathSearchResult().getType() == PathSearchResult.Type.IN_BUILDING) {
			showDialog(Utils.DLG_EXIT_NAVIGATION);
		} else if (mPOIMarker != null) {
			mPOIMarker = null;
			updateOverlay();
			return;
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onGetResult(PathSearchResult result) {
		AppContext.getInstance().setPathSearchResult(result);

		mPOIMarker = null;
		
		if(result.getType()==Type.IN_BUILDING){
			NavRoute route = result.indoorRouteEnd;
			moveToFloor(route.get(0).getFloor());
		} else if(result.getType()==Type.BETWEEN_BUILDING){
			NavRoute route = result.indoorRouteStart;
			moveToBuilding(route.getBuilding(), route.get(0).getFloor());
		} else if(result.getType()==Type.OUTDOOR_INDOOR){
			finish();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void tapPOI(POI poi) {
		Bundle data = new Bundle();
		data.putSerializable("data", poi);
		showDialog(Utils.DLG_POI, data);
		mMapView.getMapViewPosition().setCenter(poi.getGeoPoint());
	}

	private void handleIntent() {
		Uri uri = getIntent().getData();

		Building building = null;
		int floor = 1;

		if (uri != null) {
			ContentResolver resolver = getContentResolver();
			Cursor cr = resolver.query(uri, null, null, null, null);

			if (cr.moveToNext()) {
				String type = resolver.getType(uri);

				if (Building.CONTENT_ITEM_TYPE.equals(type)) {
					building = Building.newInstance(cr);
					floor = 1;
				} else if (Room.CONTENT_ITEM_TYPE.equals(type)) {
					Room room = Room.newInstance(cr);
					mPOIMarker = new POIMarker(room, Marker.boundCenterBottom(getResources().getDrawable(R.drawable.ic_poi)));

					floor = room.getFloor();

					Cursor cursor = resolver.query(Building.CONTENT_URI, null, Building.COLUMN_NAME_NAME + "='" + room.getBuilding() + "'", null, null);
					if (cursor.moveToNext()) {
						building = Building.newInstance(cursor);
					} else {
						finish();
					}
				} else if (Shelf.CONTENT_ITEM_TYPE.equals(type)) {
					Shelf shelf = Shelf.newInstance(cr);
					mPOIMarker = new POIMarker(shelf, Marker.boundCenterBottom(getResources().getDrawable(R.drawable.ic_poi)));

					floor = shelf.getFloor();

					Cursor cursor = resolver.query(Building.CONTENT_URI, null, Building.COLUMN_NAME_NAME + "='" + shelf.getBuilding() + "'", null, null);
					if (cursor.moveToNext()) {
						building = Building.newInstance(cursor);
					} else {
						finish();
					}
				}
			} else {
				finish();
			}

			cr.close();
		}

		if (!isFinishing()) {
			moveToBuilding(building, floor);
		}
	}

	/**
	 * 移动到当前建筑内
	 * @param b
	 * @param floor
	 */
	private void moveToBuilding(Building b, int floor) {
		if (mBuilding == null || mBuilding.getID() != b.getID()) {
			mBuilding = b;
			mFloorAdapter.setData(mBuilding);
			new LoadingRoomInfoList().execute();
		}

		moveToFloor(floor);
	}
	
	/**
	 * 移动到当前建筑的楼层内
	 * @param floor
	 */
	private void moveToFloor(int floor) {
		mListViewCheckedPos = toListIndex(floor);
		//标记当前楼层号为选重状态
		mListView.setItemChecked(mListViewCheckedPos, true);
		
		setMapFile(floor);
	}
	
	/**
	 * 设置当前建筑楼层对应的Map文件
	 * @param floor
	 */
	private void setMapFile(int floor) {
		String path = String.format("%1$s/%2$s/%3$s.map", AppConfig.CONFIG_DATA_PATH, mBuilding.getName(), floor);
		File file = Utils.getFile(MapIndoorActivity.this, path);
		if (file == null) {
			throw new IllegalArgumentException("null map file");
		}

		FileOpenResult openResult = mMapView.setMapFile(file);
		if (!openResult.isSuccess()) {
			return;
		}

		updateOverlay();
	}
	
	/**
	 * 更新当前的覆盖层
	 */
	private void updateOverlay() {
		if (mBuilding == null) {
			return;
		}

		MyLocation myLocation = AppContext.getInstance().getMyLocation();
		int currentFloor = toFloorNum(mListViewCheckedPos);

		ArrayList<OverlayItem> markers=null;
		Polyline routeLine =null;
		
		if (AppContext.getInstance().getPathSearchResult() != null) {
			PathSearchResult res = AppContext.getInstance().getPathSearchResult();
			NavRoute route = res.getRouteByBuilding(mBuilding);
			if (route != null) {
				NavStep step = route.getStep(currentFloor);
				if (step != null) {
					//位置覆盖层
					mMapView.getMapViewPosition().setCenter(step.getStart().getGeoPoint());
					markers=new ArrayList<OverlayItem>();
					Marker lineStart = new Marker(step.getStart().getGeoPoint(), Marker.boundCenter(getResources().getDrawable(R.drawable.icon_node)));
					markers.add(lineStart);

					if (step.getEnd() != null) {
						Marker lineEnd = new Marker(step.getEnd().getGeoPoint(), Marker.boundCenter(getResources().getDrawable(R.drawable.icon_node)));
						markers.add(lineEnd);
					}

					if (res.getStartPoint() instanceof IndoorEndPoint) {
						IndoorEndPoint start = (IndoorEndPoint) res.getStartPoint();
						if (start.getBuildingName().equals(mBuilding.getName()) && start.getFloor() == currentFloor) {
							Marker searchStart = new Marker(start.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_start)));
							markers.add(searchStart);
						}
					}

					if (res.getEndPoint() instanceof IndoorEndPoint) {
						IndoorEndPoint end = (IndoorEndPoint) res.getEndPoint();
						if (end.getBuildingName().equals(mBuilding.getName()) && end.getFloor() == currentFloor) {
							Marker searchEnd = new Marker(end.getGeoPoint(), Marker.boundCenterBottom(getResources().getDrawable(R.drawable.icon_nav_end)));
							markers.add(searchEnd);
						}
					}
					
					//路线
					if (step.size() > 1) {
						if (mPaintStroke == null) {
							mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
							mPaintStroke.setStyle(Paint.Style.STROKE);
							mPaintStroke.setColor(Color.BLUE);
							mPaintStroke.setAlpha(96);
							mPaintStroke.setStrokeWidth(5);
						}
						PolygonalChain pc = new PolygonalChain(step);
						routeLine = new Polyline(pc, mPaintStroke);
					}
					
				}
			}
		}

		List<OverlayItem> itemList = mListOverlay.getOverlayItems();
		synchronized (itemList) {
			
			itemList.clear();

			if (routeLine != null || markers != null) {
				if (routeLine != null) {
					itemList.add(routeLine);
				}
				if (markers != null) {
					itemList.addAll(markers);
				}
			} else {
				if (myLocation != null && myLocation.locateIn(mBuilding.getName(), currentFloor)) {
					//如果当前返回的位置点在当前建筑的当前楼层上则添加自己的位置点
					if (mMyLocMarker == null) {
						mMyLocMarker = new MyLocationMarker(myLocation, Marker.boundCenter(getResources().getDrawable(R.drawable.ic_my_loc)));
					} else {
						mMyLocMarker.updateLocation(myLocation);
					}
					synchronized (itemList) {
						//判断是否已经添加过位置点
						if (!itemList.contains(mMyLocMarker)) {
							itemList.add(mMyLocMarker);
						}
					}
				}
				//当前房间当前书架位置点
				if (mPOIMarker != null && mPOIMarker.getFloor() == currentFloor) {
					itemList.add(mPOIMarker);
					//设置当前目标位置点为中心点
					mMapView.getMapViewPosition().setCenter(mPOIMarker.getGeoPoint());
				}
			}
			mMapView.getMapViewPosition().setCenter(mMapView.getMapViewPosition().getCenter());
		}
	}

	/**
	 * 根据楼层号转换为列索引
	 */
	private int toListIndex(int floorNum) {
		return mBuilding.getMaxFloor() - floorNum;
	}

	/**
	 * 根据列索引转换为楼层号
	 */
	private int toFloorNum(int listIndex) {
		if (mBuilding == null) {
			return Integer.MAX_VALUE;
		}
		return mBuilding.getMaxFloor() - listIndex;
	}

	/**
	 * 删除当前自己的位置点
	 */
//	private void removeMyLocMarker() {
//		if (mMyLocMarker != null) {
//			List<OverlayItem> itemList = mListOverlay.getOverlayItems();
//			synchronized (itemList) {
//				if (itemList.contains(mMyLocMarker)) {
//					itemList.remove(mMyLocMarker);
//				}
//			}
//		}
//	}

	/**
	 * 加载所有的房间到数组中
	 * @author start
	 */
	private class LoadingRoomInfoList extends AsyncTask<Void, Void, Room[][]> {

		@Override
		protected Room[][] doInBackground(Void... params) {
			return Room.load(getApplicationContext(), mBuilding);
		}

		@Override
		protected void onPostExecute(Room[][] result) {
			super.onPostExecute(result);
			mRooms = result;
		}

	};
}
