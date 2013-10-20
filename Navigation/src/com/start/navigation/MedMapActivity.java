//package com.start.navigation;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.jgrapht.Graph;
//import org.jgrapht.alg.DijkstraShortestPath;
//import org.jgrapht.graph.DefaultWeightedEdge;
//import org.mapsforge.android.maps.MapActivity;
//import org.mapsforge.android.maps.MapView;
//import org.mapsforge.android.maps.Projection;
//import org.mapsforge.android.maps.overlay.ListOverlay;
//import org.mapsforge.android.maps.overlay.OverlayItem;
//import org.mapsforge.core.model.GeoPoint;
//import org.mapsforge.map.reader.header.FileOpenResult;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.GestureDetector;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//
//import com.start.Util;
//import com.start.core.AppConfig;
//import com.start.model.medmap.Department;
//import com.start.model.medmap.DepartmentStore;
//import com.start.model.medmap.Doctor;
//import com.start.model.medmap.Map;
//import com.start.model.medmap.Room;
//import com.start.model.medmap.UserLocation;
//import com.start.model.medmap.Vertex;
//import com.start.model.overlay.MyLocationMarker;
//import com.start.model.overlay.POIMarker;
//import com.start.utils.Utils;
//
//public class MedMapActivity extends MapActivity implements OnClickListener, OnEditorActionListener, OnFocusChangeListener, OnItemClickListener {
//
//	private GestureDetector mGestureDetector;
//	private Button[] mTabs;
//	private Drawable[] selectedTabs;
//	private Drawable[] unSelectedTabs;
//	private int[] tabResID;
//	private View[] tabViews;
//	private int currentTabIndex;
//	private ViewGroup container;
//	private PopupWindow popup;
//	private Map map;
//	private TextView mapTitle;
//	private EditText queryView;
//	private View queryContainer;
//	private View queryContentContainer;
//	private View cancelQueryButton;
//	private View queryTabDepartment;
//	private View queryTabDoctor;
//	private ListView queryListView;
//	private boolean isQueryDoctor;
//	private ArrayAdapter<Department> queryDepartmentAdapter;
//	private ArrayAdapter<Doctor> queryDoctorAdapter;
//	private File[] mapFiles;
//	private int currentMapIndex;
//	private List<DefaultWeightedEdge> edgeList;
//	private Graph<Vertex, DefaultWeightedEdge> graph;
//	private UserLocation userLocation;
//	private Vertex startVertex;
//	private Vertex endVertex;
//	private Paint paintStroke;
//	private int guideStep;
//	private int[] guideStepResIDArray;
//	private ImageView guideImage;
//	private boolean isEmergency;
//	private Button buttonNext;
//	private AsyncTask<Handler, Void, Integer> task;
//	private Vertex[] restroomVertexes;
//	
//	private MapView mMapView;
////	private ListOverlay mListOverlay;
////	private POIMarker mPOIMarker;
////	private MyLocationMarker mMyLocMarker;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_med_map);
//		container = (ViewGroup) findViewById(R.id.container);
//		mTabs = new Button[5];
//		mTabs[0] = (Button) findViewById(R.id.tab_map);
//		mTabs[1] = (Button) findViewById(R.id.tab_introduction);
//		mTabs[2] = (Button) findViewById(R.id.tab_guide);
//		mTabs[3] = (Button) findViewById(R.id.tab_departments);
//		mTabs[4] = (Button) findViewById(R.id.tab_visit);
//		selectedTabs = new Drawable[] { 
//				getResources().getDrawable(R.drawable.tab_map_d), 
//				getResources().getDrawable(R.drawable.tab_introduction_d),
//				getResources().getDrawable(R.drawable.tab_guide_d),
//				getResources().getDrawable(R.drawable.tab_list_d),
//				getResources().getDrawable(R.drawable.tab_visit_d) };
//		unSelectedTabs = new Drawable[] {
//				getResources().getDrawable(R.drawable.tab_map_n),
//				getResources().getDrawable(R.drawable.tab_introduction_n),
//				getResources().getDrawable(R.drawable.tab_guide_n),
//				getResources().getDrawable(R.drawable.tab_list_n),
//				getResources().getDrawable(R.drawable.tab_visit_n) };
//		tabResID = new int[]{R.layout.tab_map, R.layout.tab_introduction, R.layout.tab_guide, R.layout.tab_departments, R.layout.tab_visit};
//		currentTabIndex = 0;
//		tabViews = new View[]{
//				null,
//				null,
//				null,
//				null,
//				null
//		};
//		
//		mMapView = new MapView(this);
//		mMapView.setBuiltInZoomControls(true);
//		mMapView.setClickable(true);
//		mMapView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//				if(popup != null && popup.isShowing()){
//					popup.dismiss();
//				}
//				
//				return mGestureDetector.onTouchEvent(event);
//			}
//		});
//		
//		
//		graph = DepartmentStore.getInstance(this).getGraph();
//		currentMapIndex = 0;
//		mapFiles = new File[]{new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "all.map"),
//				new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "1.map"),
//				new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "2.map")
//		};
//		tabViews[0] = getLayoutInflater().inflate(R.layout.tab_map, container, false);
//		tabViews[0].findViewById(R.id.restroom).setOnClickListener(this);
//		tabViews[0].findViewById(R.id.drinking_water).setOnClickListener(this);
//		ListView mapList = (ListView) tabViews[0].findViewById(R.id.map_list);
//		mapList.setAdapter(new ArrayAdapter<String>(this, R.layout.cell_map, new String[]{"0", "1", "2"}));
//		mapList.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//				currentMapIndex = position;
//				setMap();
//				addOverlay();
//			}
//		});
//		mapTitle = (TextView) tabViews[0].findViewById(R.id.title);
//		setMap();
//		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
//			@Override
//			public boolean onSingleTapConfirmed(MotionEvent e) {
//				Projection projection = mMapView.getProjection();
//				GeoPoint p = projection.fromPixels((int)e.getX(), (int)e.getY());
//				for (final Room room : map.getRooms()) {
//					if(room.contains(p)){
//						mMapView.getMapViewPosition().setCenter(new GeoPoint(room.getCenter().getLatitude(), room.getCenter().getLongitude()));
//						View contentView = getLayoutInflater().inflate(R.layout.popup, null);
//						((TextView)contentView.findViewById(R.id.popup_title)).setText(room.getName());
//						//进入部门介绍
//						if(room.getDepartmentIndex() > -1){
//							contentView.findViewById(R.id.popup_title).setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									startActivity(new Intent(MedMapActivity.this, DepartmentActivity.class).putExtra("position", room.getDepartmentIndex()).putExtra("isFromMap", true));
//								}
//							});
//						}
//						if(room.getMapIndex() > 0 || room.getRoomNumber() == 24){
//							contentView.findViewById(R.id.navigation).setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									UserLocation userLocation = Util.locateUser();
//									double distance = -1f;
//									startVertex = null;
//									endVertex = room.getCenter();
//									for (Vertex vertex : graph.vertexSet()) {
//										if(vertex.getMapIndex() == userLocation.getMapIndex()){
//											if(startVertex == null){
//												startVertex = vertex;
//												distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//											} else {
//												double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//												if(tmp < distance){
//													startVertex = vertex;
//													distance = tmp;
//												}
//											}
//										}
//									}
//									edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//									Log.d("sss", "start: " + startVertex.getVertexNumber());
//									Log.d("sss", "end: " + endVertex.getVertexNumber());
//									locateUser(null);
//									popup.dismiss();
//								}
//							});
//							Paint paint = new Paint();
//							paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
//							popup = new PopupWindow(contentView, (int)(paint.measureText(room.getName()) + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics())), 100);
//							popup.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
//						} else {
//							contentView.findViewById(R.id.navigation).setVisibility(View.GONE);
//							contentView.findViewById(R.id.separator).setVisibility(View.GONE);
//							((TextView)contentView.findViewById(R.id.popup_title)).setCompoundDrawables(null, null, null, null);
//							Paint paint = new Paint();
//							paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
//							popup = new PopupWindow(contentView, (int)(paint.measureText(room.getName()) + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())), 100);
//							popup.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
//						}
//						break;
//					}
//				}
//				return true;
//			}
//			
//		});
//		((ViewGroup)tabViews[0].findViewById(R.id.map_container)).addView(mMapView, 0);
//		queryView = (EditText) tabViews[0].findViewById(R.id.query);
//		queryContainer = tabViews[0].findViewById(R.id.query_container);
//		queryContentContainer = tabViews[0].findViewById(R.id.query_content_container);
//		cancelQueryButton = tabViews[0].findViewById(R.id.button_cancel);
//		
//		queryView.setOnFocusChangeListener(this);
//		queryView.setOnEditorActionListener(this);
//		cancelQueryButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//取消搜索按钮隐藏
//				v.setVisibility(View.GONE);
//				//搜索文本框设置为空
//				queryView.setText("");
//				queryContainer.requestFocus();
//				//搜索内容主体设置为隐藏
//				queryContentContainer.setVisibility(View.INVISIBLE);
//				//隐藏软键盘
//				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(queryView.getWindowToken(), 0);
//			}
//		});
//		container.addView(tabViews[0]);
//		queryTabDepartment = tabViews[0].findViewById(R.id.query_tab_department);
//		queryTabDepartment.setOnClickListener(this);
//		queryTabDoctor = tabViews[0].findViewById(R.id.query_tab_doctor);
//		queryTabDoctor.setOnClickListener(this);
//		queryListView = (ListView) tabViews[0].findViewById(R.id.query_list);
//		queryDepartmentAdapter = new ArrayAdapter<Department>(this, R.layout.row_department, R.id.department, DepartmentStore.getInstance(this).getAllDepartments());
//		queryListView.setAdapter(queryDepartmentAdapter);
//		queryListView.setOnItemClickListener(this);
//		paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paintStroke.setStyle(Paint.Style.STROKE);
//		paintStroke.setColor(Color.BLUE);
//		paintStroke.setStrokeWidth(3);
//		guideStepResIDArray = new int[]{
//				R.drawable.guide_step_1,
//				R.drawable.guide_step_2,
//				R.drawable.guide_step_3,
//				R.drawable.guide_step_4,
//				R.drawable.guide_step_5,
//				R.drawable.guide_step_6,
//				R.drawable.guide_step_7,
//				R.drawable.guide_step_8,
//				R.drawable.guide_step_9,
//				R.drawable.guide_step_10,
//				R.drawable.guide_step_11,
//				R.drawable.guide_step_12,
//				R.drawable.guide_step_13,
//				R.drawable.guide_step_14,
//				R.drawable.guide_step_15,
//				R.drawable.guide_step_16,
//				R.drawable.guide_step_17,
//				R.drawable.guide_step_18,
//				R.drawable.guide_step_19,
//				R.drawable.guide_step_20,
//		};
//
//		restroomVertexes = new Vertex[2];
//		
//		for (Vertex v : graph.vertexSet()) {
//			if(v.getMapIndex() == 2){
//				if(v.getVertexNumber() == 100){
//					restroomVertexes[0] = v;
//				} else if(v.getVertexNumber() == 26){
//					restroomVertexes[1] = v;
//				}
//			}
//		}
//		
//		task = new AsyncTask<Handler, Void, Integer>(){
//			@Override
//			protected Integer doInBackground(Handler... params) {
//				while(!isCancelled()){
//					params[0].post(new Runnable() {
//						@Override
//						public void run() {
//							userLocation = Util.locateUser();
//							if(endVertex != null){
//								Vertex tmpVertex = startVertex;
//								double distance = -1f;
//								startVertex = null;
//								for (Vertex vertex : graph.vertexSet()) {
//									if(vertex.getMapIndex() == userLocation.getMapIndex()){
//										if(startVertex == null){
//											startVertex = vertex;
//											distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//										} else {
//											double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//											if(tmp < distance){
//												startVertex = vertex;
//												distance = tmp;
//											}
//										}
//									}
//								}
//								if(startVertex.equals(tmpVertex)){
//									return;
//								}
//								edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//							}
//							addOverlay();
//						}
//					});
//					SystemClock.sleep(2000);
//				}
//				return null;
//			}
//			
//		}.execute(new Handler());
//		
//		
//		
//	}
//
//	
//	@Override
//	protected void onDestroy() {
//		if(task != null){
//			task.cancel(true);
//		}
//		super.onDestroy();
//	}
//
//	public void onTabClicked(View view) {
//		int index = 0;
//		switch (view.getId()) {
//		case R.id.tab_introduction:
//			index = 1;
//			break;
//		case R.id.tab_guide:
//			index = 2;
//			break;
//		case R.id.tab_departments:
//			index = 3;
//			break;
//		case R.id.tab_visit:
//			index = 4;
//			break;
//		default:
//			break;
//		}
//		if (currentTabIndex != index) {
//			if(index != 0 && popup != null && popup.isShowing()){
//				popup.dismiss();
//			}
//			if(tabViews[index] == null){
//				tabViews[index] = getLayoutInflater().inflate(tabResID[index], container, false);
//				switch (index) {
//				case 2:
//					guideImage = (ImageView) tabViews[2].findViewById(R.id.guide_image);
//					buttonNext = (Button) tabViews[2].findViewById(R.id.button_next);
//					buttonNext.setOnClickListener(this);
//					break;
//				case 3:{
//					ListView listView = (ListView) tabViews[3].findViewById(R.id.list);
//					listView.setAdapter(new ArrayAdapter<Department>(this, R.layout.row_department, R.id.department, DepartmentStore.getInstance(this).getAllDepartments()));
//					listView.setOnItemClickListener(new OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
//							startActivityForResult(new Intent(MedMapActivity.this, DepartmentActivity.class).putExtra("position", position), 0);
//						}
//					});
//					break;
//				}
//				case 4:{
//					ListView listView = (ListView) tabViews[4].findViewById(R.id.list);
//					listView.setAdapter(new ArrayAdapter<String>(this, R.layout.row_department, R.id.department, new String[]{"病人甲", "病人乙", "病人丙"}));
//					listView.setOnItemClickListener(new OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
//							if(cancelQueryButton.getVisibility() == View.VISIBLE){
//								cancelQueryButton.performClick();
//							}
//							tabViews[currentTabIndex].setVisibility(View.INVISIBLE);
//							mTabs[currentTabIndex].setCompoundDrawablesWithIntrinsicBounds(null, unSelectedTabs[currentTabIndex], null, null);
//							tabViews[0].setVisibility(View.VISIBLE);
//							mTabs[0].setCompoundDrawablesWithIntrinsicBounds(null, selectedTabs[0], null, null);
//							currentTabIndex = 0;
//							if(currentMapIndex != 1){
//								currentMapIndex = 1;
//								setMap();
//							}
//							UserLocation userLocation = Util.locateUser();
//							double distance = -1f;
//							startVertex = null;
//							for (Vertex vertex : graph.vertexSet()) {
//								if(vertex.getMapIndex() == userLocation.getMapIndex()){
//									if(startVertex == null){
//										startVertex = vertex;
//										distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//									} else {
//										double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//										if(tmp < distance){
//											startVertex = vertex;
//											distance = tmp;
//										}
//									}
//								}
//							}
//							int patientRoomNumber = position + 69;
//							for (Vertex v : graph.vertexSet()) {
//								if(v.getMapIndex() == 1 && v.getVertexNumber() == patientRoomNumber){
//									endVertex = v;
//									edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//									break;
//								}
//							}
//							mMapView.getMapViewPosition().setCenter(new GeoPoint(endVertex.getLatitude(), endVertex.getLongitude()));
//							addOverlay();
//						}
//					});
//					break;
//				}
//				default:
//					break;
//				}
//				container.addView(tabViews[index]);
//			} else {
//				tabViews[index].setVisibility(View.VISIBLE);
//			}
//			tabViews[currentTabIndex].setVisibility(View.INVISIBLE);
//			mTabs[currentTabIndex].setCompoundDrawablesWithIntrinsicBounds(null, unSelectedTabs[currentTabIndex], null, null);
//			mTabs[index].setCompoundDrawablesWithIntrinsicBounds(null, selectedTabs[index], null, null);
//			currentTabIndex = index;
//		}
//	}
//
//	
//	private void navigateToVertex(int vertextNumber, int mapIndex){
//		for (Vertex v : graph.vertexSet()) {
//			if(v.getMapIndex() == mapIndex && v.getVertexNumber() == vertextNumber){
//				endVertex = v;
//				break;
//			}
//		}
//		if(cancelQueryButton.getVisibility() == View.VISIBLE){
//			cancelQueryButton.performClick();
//		}
//		tabViews[currentTabIndex].setVisibility(View.INVISIBLE);
//		mTabs[currentTabIndex].setCompoundDrawablesWithIntrinsicBounds(null, unSelectedTabs[currentTabIndex], null, null);
//		tabViews[0].setVisibility(View.VISIBLE);
//		mTabs[0].setCompoundDrawablesWithIntrinsicBounds(null, selectedTabs[0], null, null);
//		currentTabIndex = 0;
//		if(currentMapIndex != endVertex.getMapIndex()){
//			currentMapIndex = endVertex.getMapIndex();
//			setMap();
//		}
//		UserLocation userLocation = Util.locateUser();
//		double distance = -1f;
//		startVertex = null;
//		for (Vertex vertex : graph.vertexSet()) {
//			if(vertex.getMapIndex() == userLocation.getMapIndex()){
//				if(startVertex == null){
//					startVertex = vertex;
//					distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//				} else {
//					double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//					if(tmp < distance){
//						startVertex = vertex;
//						distance = tmp;
//					}
//				}
//			}
//		}			
//		edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//		mMapView.getMapViewPosition().setCenter(new GeoPoint(endVertex.getLatitude(), endVertex.getLongitude()));
//		addOverlay();
//	}
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.restroom:
//			cancelQueryButton.performClick();
//			UserLocation u = Util.locateUser();
//			if(u.getMapIndex() == 1){
//				navigateToVertex(11, 1);
//				break;
//			}
//			if(u.getMapIndex() == 2){
//				if(Util.distFrom(u.getLatitude(), u.getLongitude(), restroomVertexes[0].getLatitude(), restroomVertexes[0].getLongitude())
//				< Util.distFrom(u.getLatitude(), u.getLongitude(), restroomVertexes[1].getLatitude(), restroomVertexes[1].getLongitude())){
//					navigateToVertex(100, 2);
//				} else {
//					navigateToVertex(26, 2);
//				}
//			}
//			break;
//		case R.id.drinking_water:
//			navigateToVertex(18, 2);
//			break;
//		case R.id.query_tab_department:
//			if(isQueryDoctor){
//				v.setBackgroundResource(R.drawable.tabs_bar_left_on);
//				queryTabDoctor.setBackgroundResource(R.drawable.tabs_bar_right_off);
//				isQueryDoctor = false;
//				queryListView.setAdapter(queryDepartmentAdapter);
//				queryDepartmentAdapter.getFilter().filter(queryView.getText());
//			}
//			break;
//		case R.id.query_tab_doctor:
//			if(!isQueryDoctor){
//				v.setBackgroundResource(R.drawable.tabs_bar_right_on);
//				queryTabDepartment.setBackgroundResource(R.drawable.tabs_bar_left_off);
//				isQueryDoctor = true;
//				if(queryDoctorAdapter == null){
//					queryDoctorAdapter = new ArrayAdapter<Doctor>(this, R.layout.row_doctor, R.id.doctor, DepartmentStore.getInstance(this).getAllDoctors());
//				}
//				queryListView.setAdapter(queryDoctorAdapter);
//				queryDoctorAdapter.getFilter().filter(queryView.getText());
//			}
//			break;
//		case R.id.button_next:
//			switch (guideStep) {
//			case 0:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[1]));
//				guideStep++;
//				break;
//			case 1:
//				new AlertDialog.Builder(this).setTitle(getString(R.string.emergency_or_not))
//				.setNegativeButton(R.string.emergency, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[2]));
//						guideStep++;
//						isEmergency = true;
//					}
//				})
//				.setPositiveButton(R.string.pre_diagnosis, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[2]));
//						guideStep++;
//						isEmergency = false;
//					}
//				}).show();
//				break;
//			case 2:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[3]));
//				if(isEmergency){
//					navigateToVertex(203, 1);
//				} else {
//					navigateToVertex(105, 2);
//				}
//				guideStep++;
//				break;
//			case 3:
//				new AlertDialog.Builder(this).setTitle(getString(R.string.gynecology))
//				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[4]));
//						if(isEmergency){
//							navigateToVertex(200, 1);
//						} else {
//							navigateToVertex(112, 2);
//						}
//						guideStep++;
//					}
//				}).setCancelable(false).show();
//				break;
//			case 4:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[5]));
//				navigateToVertex(9, 1);
//				guideStep++;
//				break;
//			case 5:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[6]));
//				new AlertDialog.Builder(this).setTitle(getString(R.string.choose_next))
//				.setPositiveButton(R.string.end, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[7]));
//						guideStep = -1;
//						buttonNext.setText(getString(R.string.start_over));
//					}
//				})
//				.setNeutralButton(R.string.assay, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[11]));
//						guideStep = 8;
//						if(isEmergency){
//							navigateToVertex(200, 1);
//						} else {
//							navigateToVertex(112, 2);
//						}
//					}
//				})
//				.setNegativeButton(R.string.get_medicine, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[8]));
//						guideStep = 6;
//						if(isEmergency){
//							navigateToVertex(200, 1);
//						} else {
//							navigateToVertex(112, 2);
//						}
//					}
//				})
//				.show();
//				break;
//			case 6:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[9]));
//				guideStep = 7;
//				if(isEmergency){
//					navigateToVertex(206, 1);
//				} else {
//					navigateToVertex(110, 2);
//				}
//				break;
//			case 7:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[10]));
//				guideStep = -1;
//				buttonNext.setText(getString(R.string.start_over));
//				break;
//			case 8:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[12]));
//				if(isEmergency){
//					navigateToVertex(233, 1);
//				} else {
//					navigateToVertex(104, 2);
//				}
//				guideStep = 9;
//				break;
//			case 9:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[13]));
//				if(isEmergency){
//					navigateToVertex(233, 1);
//				} else {
//					navigateToVertex(104, 2);
//				}
//				guideStep = 10;
//				break;
//			case 10:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[14]));
//				navigateToVertex(9, 1);
//				guideStep = 11;
//				break;
//			case 11:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[15]));
//				new AlertDialog.Builder(this).setTitle(getString(R.string.choose_next))
//				.setPositiveButton(R.string.end, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[16]));
//						guideStep = -1;
//						buttonNext.setText(getString(R.string.start_over));
//					}
//				})
//				.setNegativeButton(R.string.get_medicine, new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[17]));
//						guideStep = 12;
//						if(isEmergency){
//							navigateToVertex(200, 1);
//						} else {
//							navigateToVertex(112, 2);
//						}
//					}
//				})
//				.show();
//				break;
//			case 12:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[18]));
//				guideStep = 13;
//				if(isEmergency){
//					navigateToVertex(206, 1);
//				} else {
//					navigateToVertex(110, 2);
//				}
//				break;
//			case 13:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[19]));
//				guideStep = -1;
//				buttonNext.setText(getString(R.string.start_over));
//				break;
//			case -1:
//				guideImage.setImageDrawable(getResources().getDrawable(guideStepResIDArray[0]));
//				guideStep = 0;
//				buttonNext.setText(getString(R.string.next));
//				break;
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == RESULT_OK){
//			navigateToRoomInMap(data.getIntExtra("roomNumber", -1), data.getIntExtra("mapIndex", -1));
//		}
//	}
//	
//	
//	
//	
//	private void navigateToRoomInMap(int roomNumber, int mapIndex){
//		if(cancelQueryButton.getVisibility() == View.VISIBLE){
//			cancelQueryButton.performClick();
//		}
//		tabViews[currentTabIndex].setVisibility(View.INVISIBLE);
//		mTabs[currentTabIndex].setCompoundDrawablesWithIntrinsicBounds(null, unSelectedTabs[currentTabIndex], null, null);
//		tabViews[0].setVisibility(View.VISIBLE);
//		mTabs[0].setCompoundDrawablesWithIntrinsicBounds(null, selectedTabs[0], null, null);
//		currentTabIndex = 0;
//		if(currentMapIndex != mapIndex){
//			currentMapIndex = mapIndex;
//			setMap();
//		}
//		for (final Room room : map.getRooms()) {
//			if(room.getRoomNumber() == roomNumber){
//				mMapView.getMapViewPosition().setCenter(new GeoPoint(room.getCenter().getLatitude(), room.getCenter().getLongitude()));
//				View contentView = getLayoutInflater().inflate(R.layout.popup, null);
//				((TextView)contentView.findViewById(R.id.popup_title)).setText(room.getName());
//				contentView.findViewById(R.id.popup_title).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						startActivity(new Intent(MedMapActivity.this, DepartmentActivity.class).putExtra("position", room.getDepartmentIndex()).putExtra("isFromMap", true));
//					}
//				});
//				contentView.findViewById(R.id.navigation).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						UserLocation userLocation = Util.locateUser();
//						double distance = -1f;
//						startVertex = null;
//						endVertex = room.getCenter();
//						for (Vertex vertex : graph.vertexSet()) {
//							if(vertex.getMapIndex() == userLocation.getMapIndex()){
//								if(startVertex == null){
//									startVertex = vertex;
//									distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//								} else {
//									double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//									if(tmp < distance){
//										startVertex = vertex;
//										distance = tmp;
//									}
//								}
//							}
//						}
//						edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//						Log.d("sss", "start: " + startVertex.getVertexNumber());
//						Log.d("sss", "end: " + endVertex.getVertexNumber());
//						locateUser(null);
//						popup.dismiss();
//					}
//				});
//				Paint paint = new Paint();
//				paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
//				popup = new PopupWindow(contentView, (int)(paint.measureText(room.getName()) + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics())), 100);
//				popup.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
//				mMapView.getController().setZoom(21);
//				break;
//			}
//		}
//	}
//	@Override
//	public boolean onEditorAction(TextView arg0, int actionID, KeyEvent event) {
//		if(actionID == EditorInfo.IME_ACTION_SEARCH){
//			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(queryView.getWindowToken(), 0);
//			if(isQueryDoctor){
//				queryDoctorAdapter.getFilter().filter(queryView.getText());
//			} else {
//				queryDepartmentAdapter.getFilter().filter(queryView.getText());
//			}
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		if(hasFocus){
//			if(popup != null && popup.isShowing()){
//				popup.dismiss();
//			}
//			cancelQueryButton.setVisibility(View.VISIBLE);
//			queryContentContainer.setVisibility(View.VISIBLE);
//			if(isQueryDoctor){
//				queryDoctorAdapter.getFilter().filter(queryView.getText());
//			} else {
//				queryDepartmentAdapter.getFilter().filter(queryView.getText());
//			}
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//		if(isQueryDoctor){
//			startActivityForResult(new Intent(this, DoctorDetailsActivity.class).putExtra("doctor", (Doctor)adapter.getItemAtPosition(position)), 0);
//		} else {
//			Department department = (Department) adapter.getItemAtPosition(position);
//			cancelQueryButton.setVisibility(View.GONE);
//			queryContentContainer.setVisibility(View.INVISIBLE);
//			queryContainer.requestFocus();
//			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(queryView.getWindowToken(), 0);
//			
//			if(currentMapIndex != department.getMapIndex()){
//				currentMapIndex = department.getMapIndex();
//				setMap();
//			}
//			for (final Room room : map.getRooms()) {
//				if(room.getRoomNumber() == department.getRoomNumber()){
//					mMapView.getMapViewPosition().setCenter(new GeoPoint(room.getCenter().getLatitude(), room.getCenter().getLongitude()));
//					View contentView = getLayoutInflater().inflate(R.layout.popup, null);
//					((TextView)contentView.findViewById(R.id.popup_title)).setText(room.getName());
//					contentView.findViewById(R.id.popup_title).setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							startActivity(new Intent(MedMapActivity.this, DepartmentActivity.class).putExtra("position", room.getDepartmentIndex()).putExtra("isFromMap", true));
//						}
//					});
//					contentView.findViewById(R.id.navigation).setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							UserLocation userLocation = Util.locateUser();
//							double distance = -1f;
//							startVertex = null;
//							endVertex = room.getCenter();
//							for (Vertex vertex : graph.vertexSet()) {
//								if(vertex.getMapIndex() == userLocation.getMapIndex()){
//									if(startVertex == null){
//										startVertex = vertex;
//										distance = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//									} else {
//										double tmp = Util.distFrom(vertex.getLatitude(), vertex.getLongitude(), userLocation.getLatitude(), userLocation.getLongitude());
//										if(tmp < distance){
//											startVertex = vertex;
//											distance = tmp;
//										}
//									}
//								}
//							}
//							edgeList = DijkstraShortestPath.findPathBetween(graph, startVertex, endVertex);
//							locateUser(null);
//							popup.dismiss();
//						}
//					});
//					Paint paint = new Paint();
//					paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
//					popup = new PopupWindow(contentView, (int)(paint.measureText(room.getName()) + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics())), 100);
//					popup.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
//					break;
//				}
//			}
//		}
//	}
//	
//	public void locateUser(View view){
//		userLocation = Util.locateUser();
//		if(userLocation.getMapIndex() != currentMapIndex){
//			currentMapIndex = userLocation.getMapIndex();
//			setMap();
//		}
//		mMapView.getMapViewPosition().setCenter(new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()));
//		addOverlay();
//	}
//	
//	private void addOverlay(){
//		mMapView.getOverlays().clear();
//		if(userLocation != null && userLocation.getMapIndex() == currentMapIndex){
//			ArrayItemizedOverlay a = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.ic_my_loc));
//			OverlayItem item = new OverlayItem();
//			item.setPoint(new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()));
//			a.addItem(item);
//			mMapView.getOverlays().add(a);	
//		}
//		Log.d("sss", "edlist null" + (edgeList == null));
//		if(edgeList != null){
//			OverlayWay way = new OverlayWay();
//			int size = edgeList.size();
//			if(size > 0){
//				List<GeoPoint> points = new ArrayList<GeoPoint>();
//				if(startVertex.getMapIndex() == currentMapIndex){
//					points.add(new GeoPoint(startVertex.getLatitude(), startVertex.getLongitude()));
//				}
//				Vertex referenceVertex = startVertex;
//				for (int i = 0; i < size - 1; i++) {
//					Vertex source = graph.getEdgeSource(edgeList.get(i));
//					if(source.equals(referenceVertex)){
//						referenceVertex = graph.getEdgeTarget(edgeList.get(i)); 
//					} else {
//						referenceVertex = source;
//					}
//					if(referenceVertex.getMapIndex() == currentMapIndex){
//						points.add(new GeoPoint(referenceVertex.getLatitude(), referenceVertex.getLongitude()));
//					}
//				}
//				if(endVertex.getMapIndex() == currentMapIndex){
//					points.add(new GeoPoint(endVertex.getLatitude(), endVertex.getLongitude()));
//				}
//				int pointsSize = points.size();
//				if(pointsSize > 0){
//					if(pointsSize > 1){
//						GeoPoint[][] nodes = new GeoPoint[1][pointsSize];
//						for (int i = 0; i < pointsSize; i++) {
//							nodes[0][i] = points.get(i);
//						}
//						way.setWayNodes(nodes);
//						ArrayWayOverlay ways = new ArrayWayOverlay(null, paintStroke);
//						ways.addWay(way);
//						mMapView.getOverlays().add(ways);
//					}
//					if(startVertex.getMapIndex() == currentMapIndex){
//						ArrayItemizedOverlay a = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.icon_nav_start));
//						a.addItem(new OverlayItem(new GeoPoint(startVertex.getLatitude(), startVertex.getLongitude()), null, null, null));
//						mMapView.getOverlays().add(a);
//					}
//					if(endVertex.getMapIndex() == currentMapIndex){
//						ArrayItemizedOverlay b = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.icon_nav_end));
//						b.addItem(new OverlayItem(new GeoPoint(endVertex.getLatitude(), endVertex.getLongitude()), null, null, null));
//						mMapView.getOverlays().add(b);
//					}
//				}
//			} else if (endVertex.getMapIndex() == currentMapIndex){
//				way.setWayNodes(new GeoPoint[][]{{new GeoPoint(endVertex.getLatitude(), endVertex.getLongitude()), new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude())}});
//				ArrayWayOverlay ways = new ArrayWayOverlay(null, paintStroke);
//				ways.addWay(way);
//				mMapView.getOverlays().add(ways);
//			}
//		}
//	}
//	
//	private void setMap(){
//		if(popup != null && popup.isShowing()){
//			popup.dismiss();
//		}
//		map = DepartmentStore.getInstance(this).getMaps()[currentMapIndex];
//		FileOpenResult openResult = mMapView.setMapFile(mapFiles[currentMapIndex]);
//		if (!openResult.isSuccess()) {
//			return;
//		}
//		mapTitle.setText(map.getName());
//		mMapView.getController().setZoom(20);
//	}
//	
//}
