package org.apache.ibatis.parsing;


import org.w3c.dom.Node;
import java.util.Properties;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.CharacterData;

public class XNodeProduct {
	private XNodeProductProduct xNodeProductProduct;
	private final Node node;
	private final XPathParser xpathParser;

	public XNodeProduct(XPathParser xpathParser, Node node, Properties variables) {
		this.xNodeProductProduct = new XNodeProductProduct(variables);
		this.xpathParser = xpathParser;
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	public XPathParser getXpathParser() {
		return xpathParser;
	}

	public String getPath() {
		StringBuilder builder = new StringBuilder();
		Node current = node;
		while (current instanceof Element) {
			if (current != node) {
				builder.insert(0, "/");
			}
			builder.insert(0, current.getNodeName());
			current = current.getParentNode();
		}
		return builder.toString();
	}

	public XNode newXNode(Node node) {
		return new XNode(xpathParser, node, xNodeProductProduct.getVariables());
	}

	public XNode getParent() {
		Node parent = node.getParentNode();
		if (!(parent instanceof Element)) {
			return null;
		} else {
			return new XNode(xpathParser, parent, xNodeProductProduct.getVariables());
		}
	}

	public List<XNode> getChildren() {
		List<XNode> children = new ArrayList<>();
		NodeList nodeList = node.getChildNodes();
		if (nodeList != null) {
			for (int i = 0, n = nodeList.getLength(); i < n; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					children.add(new XNode(xpathParser, node, xNodeProductProduct.getVariables()));
				}
			}
		}
		return children;
	}

	public Properties parseAttributes(Node n) {
		return xNodeProductProduct.parseAttributes(n);
	}

	public String getBodyData(Node child) {
		return xNodeProductProduct.getBodyData(child);
	}

	public String parseBody(Node node) {
		return xNodeProductProduct.parseBody(node);
	}
}