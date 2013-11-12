package com.start.model.process;

import java.util.Map;

public class Node {

	public enum NodeType {
		START,EXEC,END
	}
	
	private NodeType nodeType;
	private String nodeName;
	private Node nextNode;
	private Map<String,Action> actions;
	
	public Node(NodeType nodeType,String nodeName){
		this.nodeType=nodeType;
		this.nodeName=nodeName;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public String getNodeName() {
		return nodeName;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public Map<String, Action> getActions() {
		return actions;
	}

	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}
	
}
