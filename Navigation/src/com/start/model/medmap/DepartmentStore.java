//package com.start.model.medmap;
//
//import java.io.ObjectInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultWeightedEdge;
//
//import android.content.Context;
//
//import com.start.navigation.R;
//
//public class DepartmentStore{
//
//	private static DepartmentStore instance;
//	private Context context;
//	private Graph<Vertex, DefaultWeightedEdge> graph;
//	private Department[] allDepartments;
//	private Map[] allMaps;
//	private List<Doctor> allDoctors;
//	private DepartmentStore(Context context){
//		this.context = context.getApplicationContext();
//		init();
//	}
//	public static DepartmentStore getInstance(Context context){
//		if(instance == null){
//			instance = new DepartmentStore(context);
//		}
//		return instance;
//	}
//	
//	public Graph<Vertex, DefaultWeightedEdge> getGraph(){
//		return graph;
//	}
//	
//	public Map[] getMaps(){
//		return allMaps;
//	}
//	
//	public Department[] getAllDepartments(){
//		return allDepartments;
//	}
//	
//	public List<Doctor> getAllDoctors(){
//		return allDoctors;
//	}
//	
//	@SuppressWarnings("unchecked")
//	private void init(){
//		ObjectInputStream in;
//		try {
//			in = new ObjectInputStream(context.getResources().openRawResource(R.raw.department));
//			Department d0 = (Department) in.readObject();
//			Department d1 = (Department) in.readObject();
//			Department d2 = (Department) in.readObject();
//			allDepartments = new Department[]{d0, d1, d2};
//			allDoctors = new ArrayList<Doctor>();
//			for (Doctor d : d0.getDoctors()) {
//				allDoctors.add(d);
//			}
//			for (Doctor d : d1.getDoctors()) {
//				allDoctors.add(d);
//			}
//			for (Doctor d : d2.getDoctors()) {
//				allDoctors.add(d);
//			}
//			in.close();
//			in = new ObjectInputStream(context.getResources().openRawResource(R.raw.all));
//			graph = (Graph<Vertex, DefaultWeightedEdge>) in.readObject();
//			Map map0 = (Map) in.readObject();
//			Map map1 = (Map) in.readObject();
//			Map map2 = (Map) in.readObject();
//			allMaps = new Map[]{map0, map1, map2};
//			in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
