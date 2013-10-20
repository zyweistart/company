package com.start.model.nav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.start.model.Edge;
import com.start.model.Vertex;

/**
 * A Graph represents all the vertexes and edges consist the map.
 */
public class Graph extends ArrayList<LinkedList<Short>> {

	private static final long serialVersionUID = -1310931483972347772L;

	private HashMap<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	private ArrayList<String> vertexNames = new ArrayList<String>();

	/**
	 * Add a vertex into the graph
	 * @param v the vertex to add
	 */
	public synchronized void addVertex(Vertex v) {
		vertexMap.put(v.getName(), v);
		vertexNames.add(v.getName());
		add(new LinkedList<Short>());
	}

	/**
	 * Add an edge into the graph
	 * 
	 * @param aVertexName the name of the end point of edge
	 * @param bVertexName the name of the end point of edge
	 */
	public synchronized void addEdge(String aVertexName, String bVertexName) {
		int aVertexIdx = vertexNames.indexOf(aVertexName);
		int bVertexIdx = vertexNames.indexOf(bVertexName);

		if (aVertexIdx == -1 || bVertexIdx == -1) {
			Log.e("error", "wrong edge: " + aVertexName + ", " + bVertexName);
			return;
		}
		get(aVertexIdx).add((short) bVertexIdx);
		get(bVertexIdx).add((short) aVertexIdx);
	}
	
	private boolean hasEdge(int a, int b) {
		for (Short s : get(a)) {
			if (s == b) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the nearest vertex to a point in the map
	 *  
	 * @param geoPoint
	 * @return the nearest vertex
	 */
	public Vertex getClosestVertex(org.mapsforge.core.model.GeoPoint geoPoint) {
		double minDist = Double.MAX_VALUE;
		double longitude = geoPoint.longitude, latitude = geoPoint.latitude;

		Vertex target = null;
		for (Vertex v : vertexMap.values()) {
			double dist = Math.abs(longitude - v.getLongitude()) + Math.abs(latitude - v.getLatitude());
			if (dist < minDist) {
				target = v;
				minDist = dist;
			}
		}
		return target;
	}
	
	/*
	 * Shortest path algorithm
	 */
	private ArrayList<Vertex> searchPath(short start, short end) {
		Queue<Short> q = new LinkedList<Short>();
		int n = vertexNames.size();
		boolean[] visit = new boolean[n];
		int[] pre = new int[n];
		q.add(start);
		pre[start] = -1;
		visit[start] = true;
		while (!q.isEmpty()) {
			int cur = q.peek();
			if (cur == end)
				break;
			q.remove();
			
			for (Short i : get(cur)) {
				if (hasEdge(cur, i)/*graph[cur][i] == 1*/ && !visit[i]) {
					q.add((short) i);
					pre[i] = cur;
					visit[i] = true;
				}
			}
		}
		ArrayList<Vertex> p = new ArrayList<Vertex>();
		if (!q.isEmpty()) {
			for (int i = end; i != -1; i = pre[i]) {
				p.add(vertexMap.get(vertexNames.get(i)));
			}
		}
		return p;
	}

	/**
	 * Find the shortest path between two vertex
	 * 
	 * @param startName the start of the path
	 * @param endName the end of the path
	 * 
	 * @return The shortest path
	 */
	public NavRoute findPath(String startName, String endName) {
		int start = vertexNames.indexOf(startName), end = vertexNames.indexOf(endName);
		ArrayList<Vertex> pathVertexes = searchPath((short) start, (short) end);
		return dividePath(pathVertexes);
	}
	
	/*
	 * Divide the path into segments by vertex' floor.
	 */
	private NavRoute dividePath(ArrayList<Vertex> path) {
		if (path == null || path.size() < 1) {
			return null;
		}
		
		int len = path.size();
		NavRoute route = new NavRoute();

		Vertex start = path.get(len - 1);
		NavStep step = new NavStep(start.getFloor());
		step.setStart(start);
		route.add(step);
		if (len == 1) {
			return route;
		}

		step.add(start.getGeoPoint());

		int i = len - 2;
		while (i >= 0) {
			Vertex v = path.get(i);
			if (v.getFloor() != step.getFloor()) {				
				step = new NavStep(v.getFloor());
				route.add(step);
				step.setStart(v);
				step.add(v.getGeoPoint());
			} else {
				step.add(v.getGeoPoint());
				step.setEnd(v);
			}
			i--;
		}
		return route;
	}

	/**
	 * Init a graph for a building
	 * 
	 * @param context
	 * @param buildingName building name
	 */
	public void init(Context context, String buildingName) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cr = resolver.query(Vertex.CONTENT_URI, null, Vertex.COLUMN_NAME_BUILDING + "='" + buildingName +"'", null, null);
		if (cr.moveToNext()) {
			int name = cr.getColumnIndex(Vertex.COLUMN_NAME_NAME);
			int longitude = cr.getColumnIndex(Vertex.COLUMN_NAME_LONGITUDE);
			int latitude = cr.getColumnIndex(Vertex.COLUMN_NAME_LATITUDE);
			int type = cr.getColumnIndex(Vertex.COLUMN_NAME_TYPE);
			int floor = cr.getColumnIndex(Vertex.COLUMN_NAME_FLOOR);

			do {
				Vertex v = new Vertex(cr.getString(name), cr.getDouble(latitude), cr.getDouble(longitude), cr.getInt(type), cr.getInt(floor), buildingName);
				addVertex(v);
			} while (cr.moveToNext());
		}
		cr.close();

		cr = resolver.query(Edge.CONTENT_URI, null, Edge.COLUMN_NAME_BUILDING + "='" + buildingName + "'", null, null);
		if (cr.moveToNext()) {
			int start = cr.getColumnIndex(Edge.COLUMN_NAME_START);
			int end = cr.getColumnIndex(Edge.COLUMN_NAME_END);
			do {
				String aVertexName = cr.getString(start);
				String bVertexName = cr.getString(end);
				addEdge(aVertexName, bVertexName);
			} while (cr.moveToNext());
		}
		cr.close();
	}
}
