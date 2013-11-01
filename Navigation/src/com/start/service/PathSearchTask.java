package com.start.service;

import org.mapsforge.core.model.GeoPoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.start.model.MapData;
import com.start.model.Vertex;
import com.start.model.nav.EndPoint;
import com.start.model.nav.Graph;
import com.start.model.nav.IndoorEndPoint;
import com.start.model.nav.NavRoute;
import com.start.model.nav.PathSearchResult;

public class PathSearchTask extends AsyncTask<EndPoint, Void, PathSearchResult> {

	protected Context context;
	protected ProgressDialog dialog;
	protected PathSearchListener listener;
	
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
		dialog = ProgressDialog.show(this.context, null, "正在查询中...", true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				PathSearchTask.this.cancel(true);
			}
			
		});
		
	}

	@Override
	protected PathSearchResult doInBackground(EndPoint... params) {
		PathSearchResult result = new PathSearchResult(params[0], params[1]);

		IndoorEndPoint ep = (IndoorEndPoint) params[1];
		
		if (params[0] instanceof IndoorEndPoint) {
			
			IndoorEndPoint sp = (IndoorEndPoint) params[0];
			//当前位置与目标位置在同一张地图
			if (sp.getMapData().getId().equals(ep.getMapData().getId())) {
				
				// Search path in the same building.
				Graph g = getGraph(sp.getMapData());

				NavRoute r = searchInBuilding(g, sp.getGeoPoint(), ep.getVertex(), sp.getMapData());
				
				result.indoorRouteEnd = r;
				result.type = PathSearchResult.Type.IN_BUILDING;
				
			} else {
				
				// Search path in the different buildings.
				Graph g = getGraph(sp.getMapData());
				NavRoute r1 = searchInBuilding(g, sp.getGeoPoint(), sp.getVertex(), sp.getMapData());
				result.indoorRouteStart = r1;
				
				g = getGraph(ep.getMapData());
				NavRoute r2 = searchInBuilding(g, ep.getGeoPoint(), ep.getVertex(), ep.getMapData());
				result.indoorRouteEnd = r2;
				
				result.type = PathSearchResult.Type.BETWEEN_BUILDING;
			}

		}

		return result;
	}

	@Override
	protected void onPostExecute(PathSearchResult result) {
		super.onPostExecute(result);
		
		dialog.dismiss();

		if (listener != null) {
			
			if (result.isValid()) {
				listener.onGetResult(result);
			} else {
				Toast.makeText(context, "暂无结果", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private Graph getGraph(MapData mapData) {
		Graph g = new Graph();
		g.init(context, mapData);
		return g;
	}

	protected NavRoute searchInBuilding(Graph g, GeoPoint startPoint, String endVertex, MapData mapData) {
		if (startPoint == null || endVertex == null || mapData == null) {
			throw new IllegalArgumentException("null parameters");
		}

		Vertex v = g.getClosestVertex(startPoint);
		if (v == null) {
			return null;
		}
		NavRoute route = searchInBuilding(g, v.getId(), endVertex, mapData);
		return route;
	}

	protected NavRoute searchInBuilding(Graph g, final String startVertex, final String endVertex, final MapData mapData) {
		if (startVertex == null || endVertex == null || mapData == null) {
			throw new IllegalArgumentException("null parameters");
		}

		NavRoute route = g.findPath(startVertex, endVertex);
		if (route != null) {
			route.setMapId(mapData.getId());
		}
		return route;
	}

	public interface PathSearchListener {
		
		void onGetResult(PathSearchResult result);
		
	}
	
}