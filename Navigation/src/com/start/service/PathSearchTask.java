package com.start.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.start.model.Building;
import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.Graph;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.MKSearchListenerBase;
import com.start.model.nav.NavRoute;
import com.start.model.nav.OutdoorEndPoint;
import com.start.model.nav.PathSearchResult;
import com.start.navigation.AppContext;
import com.start.navigation.R;

/**
 * Asynchronous task searching for navigation path for the same building, outdoor to indoor 
 * and in different buildings
 *
 */
public class PathSearchTask extends AsyncTask<EndPoint, Void, PathSearchResult> {

	protected Context context;
	protected ProgressDialog dialog;
	protected PathSearchListener listener;
	
	private volatile boolean outdoorSearchFinished;
	private volatile MKRoute outdoorRoute;
	
	/**
	 * The parameter <code>context</code> should implement {@link PathSearchListener} to register as a listener for searching result.
	 * 
	 * @param context
	 */
	public PathSearchTask(Context context) {
		this.context = context;
		if (context instanceof PathSearchListener) {
			this.listener = (PathSearchListener) context;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		// Show a loading progress dialog
		dialog = ProgressDialog.show(this.context, null, context.getString(R.string.searching), true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				PathSearchTask.this.cancel(true);
			}
		});

		// Start BAIDU Map Manager if the possible result includes outdoor part.
		AppContext.getInstance().instanceBMapManager().start();
	}

	@Override
	protected PathSearchResult doInBackground(EndPoint... params) {
		PathSearchResult result = new PathSearchResult(params[0], params[1]);

		IndoorEndPoint ep = (IndoorEndPoint) params[1];
		
		if (params[0] instanceof IndoorEndPoint) {
			
			IndoorEndPoint sp = (IndoorEndPoint) params[0];
			if (sp.getBuildingName().equals(ep.getBuildingName())) {
				
				// Search path in the same building.
				Building b = Building.newInstance(context, sp.getBuildingName());
				Graph g = getGraph(b.getName());

				NavRoute r = searchInBuilding(g, sp.getGeoPoint(), ep.getVertex(), b);
				
				result.indoorRouteEnd = r;
				result.type = PathSearchResult.Type.IN_BUILDING;
				
			} else {
				
				// Search path in the different buildings.
				Building sb = Building.newInstance(context, sp.getBuildingName());
				Graph g = getGraph(sb.getName());
				NavRoute r1 = searchInBuilding(g, sp.getGeoPoint(), sb.getGate(), sb);
				
				result.indoorRouteStart = r1;

				Building eb = Building.newInstance(context, ep.getBuildingName());
				searchOutdoor(sb.getGeoPoint(), eb.getGeoPoint());
				result.outdoorRoute = outdoorRoute;
				
				g = getGraph(eb.getName());

				NavRoute r2 = searchInBuilding(g, eb.getGate(), ep.getVertex(), eb);
				result.indoorRouteEnd = r2;
				result.type = PathSearchResult.Type.BETWEEN_BUILDING;
			}

		} else {
			
			// Search path from outdoor to indoor.
			OutdoorEndPoint sp = (OutdoorEndPoint) params[0];
			Building eb = Building.newInstance(context, ep.getBuildingName());
			Graph g = getGraph(eb.getName());

			NavRoute r = searchInBuilding(g, eb.getGate(), ep.getVertex(), eb);
			result.indoorRouteEnd = r;

			searchOutdoor(sp.getGeoPoint(), eb.getGeoPoint());
			result.outdoorRoute = outdoorRoute;
			result.type = PathSearchResult.Type.OUTDOOR_INDOOR;
		}

		return result;
	}

	@Override
	protected void onPostExecute(PathSearchResult result) {
		super.onPostExecute(result);
		
		dialog.dismiss();

		AppContext.getInstance().instanceBMapManager().stop();

		if (listener != null) {
			
			if (result.isValid()) {
				listener.onGetResult(result);
			} else {
				Toast.makeText(context, R.string.no_result, Toast.LENGTH_SHORT).show();
			}

		}
	}

	/*
	 * Wait for BAIDU outdoor path searching for up to 20 seconds
	 */
	private void waitOutdoorSearchResult() {
		int n = 10;
		while (n > 0) {
			if (outdoorSearchFinished) {
				break;
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}

			n--;
		}
	}

	protected void searchOutdoor(final com.baidu.platform.comapi.basestruct.GeoPoint sp, final com.baidu.platform.comapi.basestruct.GeoPoint ep) {
		outdoorSearchFinished = false;

		((Activity) context).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {

				MKSearch mkSearch = new MKSearch();
				mkSearch.init(AppContext.getInstance().instanceBMapManager(), mkListener);

				MKPlanNode startNode = new MKPlanNode();
				startNode.pt = sp;
				MKPlanNode endNode = new MKPlanNode();
				endNode.pt = ep;
				
				
				mkSearch.walkingSearch(context.getString(R.string.hangzhou), startNode, context.getString(R.string.hangzhou), endNode);
			}
		});

		waitOutdoorSearchResult();
	}

	private Graph getGraph(String buildingName) {
		Graph g = new Graph();
		g.init(context, buildingName);
		return g;
	}

	protected NavRoute searchInBuilding(Graph g, org.mapsforge.core.model.GeoPoint startPoint, String endVertex, Building building) {
		if (startPoint == null || endVertex == null || building == null) {
			throw new IllegalArgumentException("null parameters");
		}

		Vertex v = g.getClosestVertex(startPoint);
		if (v == null) {
			return null;
		}
		NavRoute route = searchInBuilding(g, v.getName(), endVertex, building);
		return route;
	}

	protected NavRoute searchInBuilding(Graph g, final String startVertex, final String endVertex, final Building building) {
		if (startVertex == null || endVertex == null || building == null) {
			throw new IllegalArgumentException("null parameters");
		}

		NavRoute route = g.findPath(startVertex, endVertex);
		if (route != null) {
			route.setBuilding(building);
		}
		return route;
	}

	private MKSearchListenerBase mkListener = new MKSearchListenerBase() {

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult walkingResult, int error) {
			if (error == 0) {
				outdoorRoute = walkingResult.getPlan(0).getRoute(0);
			}

			outdoorSearchFinished = true;
		}
	};

	/**
	 * Interface definition for a callback to be invoked when a path search succeed.
	 */
	public interface PathSearchListener {
		
		/**
		 * Called when a path search succeed.
		 * 
		 * @param result the path search result
		 */
		void onGetResult(PathSearchResult result);
	}
}
