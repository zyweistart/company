package com.start.model.process;

import java.util.List;

public class Junction {

	public enum NodeType {
		START,SWITCH,EXEC,END
	}
	
	private String id;
	private String title;
	private NodeType nodeType;
	/**
	 * 除了SWITCH其它节点都可存在
	 */
	private String nextNodeId;
	/**
	 * 只能在SWITCH节点中存在
	 */
	private List<Option> options;
	/**
	 * 除了SWITCH其它节点都可存在
	 */
	private List<Action> actions;
	
	public Junction(String id,String title,NodeType nodeType){
		this.id=id;
		this.title=title;
		this.nodeType=nodeType;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}

	public String getNextNodeId() {
		if(NodeType.END==getNodeType()||
				NodeType.SWITCH==getNodeType()){
			throw new IllegalArgumentException("getNextNodeId:"+getNodeType());
		}
		return nextNodeId;
	}

	public void setNextNodeId(String nextNodeId) {
		if(NodeType.END==getNodeType()||
				NodeType.SWITCH==getNodeType()){
			throw new IllegalArgumentException("setNextNodeId:"+getNodeType());
		}
		this.nextNodeId = nextNodeId;
	}

	public List<Option> getOptions() {
		if(NodeType.SWITCH!=getNodeType()){
			throw new IllegalArgumentException("getOptions:"+getNodeType());
		}
		return options;
	}

	public void setOptions(List<Option> options) {
		if(NodeType.SWITCH!=getNodeType()){
			throw new IllegalArgumentException("setOptions:"+getNodeType());
		}
		this.options = options;
	}

	public List<Action> getActions() {
		if(NodeType.SWITCH==getNodeType()){
			throw new IllegalArgumentException("setActions:"+getNodeType());
		}
		return actions;
	}

	public void setActions(List<Action> actions) {
		if(NodeType.SWITCH==getNodeType()){
			throw new IllegalArgumentException("setActions:"+getNodeType());
		}
		this.actions = actions;
	}
	
}