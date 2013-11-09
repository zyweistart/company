package com.start.model.process;

public class Node {

	private NodeType nodeType;
	private String nodeName;
	
	public Node(NodeType nodeType,String nodeName){
		this.nodeType=nodeType;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public String getNodeName() {
		return nodeName;
	}
	
}
