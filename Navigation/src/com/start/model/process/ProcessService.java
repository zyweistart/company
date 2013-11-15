package com.start.model.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.start.core.XMLException;
import com.start.model.process.Action.ActionType;
import com.start.model.process.Junction.NodeType;
import com.start.navigation.AppContext;
import com.start.utils.CommonFn;

public class ProcessService {
	
	private String TAG="ProcessService";

	private String currentId;
	
	private String startJunctionId;
	
	private Map<String,Junction> junctions;
	
	private Context mContext;
	
	private AppContext mAppContext;
	
	public ProcessService(Context context){
		this.mContext=context;
		this.mAppContext=AppContext.getInstance();
		try {
			InputStream in =context.getAssets().open("tmp1.xml");
			buildProcessXML(in);
		} catch (Exception e) {
			e.printStackTrace();
			mAppContext.makeTextLong("流程文件定义出错:"+e.getMessage());
		}
	}
	
	private void buildProcessXML(InputStream in) throws XMLException{
		junctions=new HashMap<String,Junction>();
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			document=builder.parse(in);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		Element root=document.getDocumentElement();
		NodeList processList=root.getElementsByTagName("process");
		for (int i = 0; i < processList.getLength(); i++) {
			Node process=processList.item(i);
			NodeList nodeList=process.getChildNodes();
			for (int j= 0; j< nodeList.getLength();j++) {
				Node cNode=nodeList.item(j);
				NodeList aoChildList=cNode.getChildNodes();
				String nodeName=cNode.getNodeName().toLowerCase();
				NamedNodeMap nnM=cNode.getAttributes();
				Junction jun=null;
				String id=null;
				if("start".equals(nodeName)){
					id=nnM.getNamedItem("id").getNodeValue();
					String title=nnM.getNamedItem("title").getNodeValue();
					String next=nnM.getNamedItem("next").getNodeValue();
					jun=new Junction(id,title,NodeType.START);
					jun.setNextNodeId(next);
					startJunctionId=id;
				}else if("switch".equals(nodeName)){
					id=nnM.getNamedItem("id").getNodeValue();
					String title=nnM.getNamedItem("title").getNodeValue();
					jun=new Junction(id,title,NodeType.SWITCH);
					List<Option> options=new ArrayList<Option>();
					for (int l= 0; l< aoChildList.getLength();l++) {
						Node actionNode=aoChildList.item(l);
						if("option".equals(actionNode.getNodeName().toLowerCase())){
							NamedNodeMap actionNNM=actionNode.getAttributes();
							String oId=actionNNM.getNamedItem("id").getNodeValue().toLowerCase();
							String oNext=actionNNM.getNamedItem("next").getNodeValue();
							Option option=new Option(oId,oNext);
							options.add(option);
						}
					}
					jun.setOptions(options);
				}else if("node".equals(nodeName)){
					id=nnM.getNamedItem("id").getNodeValue();
					String title=nnM.getNamedItem("title").getNodeValue();
					String next=nnM.getNamedItem("next").getNodeValue();
					jun=new Junction(id,title,NodeType.EXEC);
					jun.setNextNodeId(next);
				}else if("end".equals(nodeName)){
					id=nnM.getNamedItem("id").getNodeValue();
					String title=nnM.getNamedItem("title").getNodeValue();
					jun=new Junction(id,title,NodeType.END);
				}
				if("start".equals(nodeName)
						||"node".equals(nodeName)
						||"end".equals(nodeName)){
					for (int l= 0; l< aoChildList.getLength();l++) {
						Node actionNode=aoChildList.item(l);
						if("action".equals(actionNode.getNodeName().toLowerCase())){
							NamedNodeMap actionNNM=actionNode.getAttributes();
							
							Node node=actionNNM.getNamedItem("type");
							if(node==null){
								throw new XMLException("行为类型不能为空");
							}
							String type=node.getNodeValue().toLowerCase();
							
							Action action=new Action();
							if("dialog".equals(type)){
								node=actionNNM.getNamedItem("message");
								if(node==null){
									throw new XMLException("对话框动作消息不能为空");
								}
								action.setMessage(node.getNodeValue());
								action.setType(ActionType.DIALOG);
							}else if("location".equals(type)){
								node=actionNNM.getNamedItem("message");
								if(node!=null){
									action.setMessage(node.getNodeValue());
								}
								String content=actionNNM.getNamedItem("content").getNodeValue();
								action.setContent(content);
								action.setType(ActionType.LOCATION);
							}else if("openweb".equals(type)){
								node=actionNNM.getNamedItem("message");
								if(node!=null){
									action.setMessage(node.getNodeValue());
								}
								String url=actionNNM.getNamedItem("url").getNodeValue();
								action.setUrl(url);
								action.setType(ActionType.OPENWEB);
							}
							
							jun.setAction(action);
						}
					}
					
				}
				if("start".equals(nodeName)
						||"switch".equals(nodeName)
						||"node".equals(nodeName)
						||"end".equals(nodeName)){
					junctions.put(id, jun);
				}
			}
		}
	}
	
	public void init(){
		currentId=startJunctionId;
	}
	
	public void next(){
		Junction jun=junctions.get(currentId);
		if(jun!=null){
			Log.v(TAG,"当前节点："+currentId);
			if(jun.getNodeType()==NodeType.START){
				
				currentId=jun.getNextNodeId();
			}else if(jun.getNodeType()==NodeType.SWITCH){
				final Option o1=jun.getOptions().get(0);
				final Option o2=jun.getOptions().get(1);
				final AlertDialog.Builder ad=new AlertDialog.Builder(this.mContext)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(jun.getTitle())
				.setPositiveButton(o1.getTitle(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						currentId=o1.getNextNodeId();
						dialog.dismiss();
					}
				}).setNegativeButton(o2.getTitle(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						currentId=o2.getNextNodeId();
						dialog.dismiss();
					}
				});
				if(jun.getOptions().size()==3){
					final Option o3=jun.getOptions().get(2);
					ad.setNegativeButton(o3.getTitle(), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							
							currentId=o3.getNextNodeId();
							dialog.dismiss();
						}
					});
				}
				ad.show();
			}else if(jun.getNodeType()==NodeType.EXEC){
				
				currentId=jun.getNextNodeId();
			}else if(jun.getNodeType()==NodeType.END){
				CommonFn.alertsDialog(this.mContext, "亲，当前流程结束了哦", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,int which) {
						dialog.dismiss();
					}
					
				}).show();
			}
			if(jun.getNodeType()!=NodeType.SWITCH&&jun.getAction()!=null){
				Action action=jun.getAction();
				if(action.getType()==ActionType.DIALOG){
					CommonFn.alertsDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,int which) {
							dialog.dismiss();
						}
						
					}).show();
				}else if(action.getType()==ActionType.LOCATION){
					final String[] maps=action.getContent().split(";");
					if(action.getMessage()!=null){
						CommonFn.alertsDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								mAppContext.makeTextLong("定义地图："+maps[0]+"经度："+maps[1]+"纬度："+maps[2]);
								dialog.dismiss();
							}
							
						}).show();
					}else{
						mAppContext.makeTextLong("定义地图："+maps[0]+"经度："+maps[1]+"纬度："+maps[2]);
					}
				}else if(action.getType()==ActionType.OPENWEB){
					final Intent intent=new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(action.getUrl()));
					if(action.getMessage()!=null){
						CommonFn.alertsDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								mContext.startActivity(intent);
								dialog.dismiss();
							}
							
						}).show();
					}else{
						this.mContext.startActivity(intent);
					}
				}
			}
		}else{
			Log.v(TAG,"该节点不存在："+currentId);
		}
	}
	
}
