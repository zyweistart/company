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

import com.start.core.XMLException;
import com.start.model.process.Action.ActionType;
import com.start.model.process.Junction.NodeType;

public class ProcessService {

	private String startJunctionId;
	
	private String endJunctionId;
	
	private Map<String,Junction> junctions;
	
	public void buildProcessXML(InputStream in) throws XMLException{
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
				String id=nnM.getNamedItem("id").getNodeValue();
				if("start".equals(nodeName)){
					String title=nnM.getNamedItem("title").getNodeValue();
					String next=nnM.getNamedItem("next").getNodeValue();
					jun=new Junction(id,title,NodeType.START);
					jun.setNextNodeId(next);
					startJunctionId=id;
				}else if("switch".equals(nodeName)){
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
					String title=nnM.getNamedItem("title").getNodeValue();
					String next=nnM.getNamedItem("next").getNodeValue();
					jun=new Junction(id,title,NodeType.EXEC);
					jun.setNextNodeId(next);
				}else if("end".equals(nodeName)){
					jun=new Junction(id,"",NodeType.END);
				}
				if("start".equals(nodeName)
						||"node".equals(nodeName)
						||"end".equals(nodeName)){
					List<Action> actions=new ArrayList<Action>();
					for (int l= 0; l< aoChildList.getLength();l++) {
						Node actionNode=aoChildList.item(l);
						if("action".equals(actionNode.getNodeName().toLowerCase())){
							NamedNodeMap actionNNM=actionNode.getAttributes();
							String type=actionNNM.getNamedItem("type").getNodeValue().toLowerCase();
							String content=actionNNM.getNamedItem("content").getNodeValue();
							Action action=new Action();
							if("dialog".equals(type)){
								action.setType(ActionType.DIALOG);
							}else if("location".equals(type)){
								action.setType(ActionType.LOCATION);
							}else if("openweb".equals(type)){
								action.setType(ActionType.OPENWEB);
							}
							action.setContent(content);
							actions.add(action);
						}
					}
					jun.setActions(actions);
				}
				if("start".equals(nodeName)
						||"switch".equals(nodeName)
						||"node".equals(nodeName)
						||"end".equals(nodeName)){
					junctions.put(nodeName, jun);
				}
			}
		}
	}
	
}
