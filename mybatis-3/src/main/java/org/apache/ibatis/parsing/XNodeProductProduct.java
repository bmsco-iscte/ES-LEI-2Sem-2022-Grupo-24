package org.apache.ibatis.parsing;


import java.util.Properties;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NodeList;

public class XNodeProductProduct {
	private final Properties variables;

	public XNodeProductProduct(Properties variables) {
		this.variables = variables;
	}

	public Properties getVariables() {
		return variables;
	}

	public Properties parseAttributes(Node n) {
		Properties attributes = new Properties();
		NamedNodeMap attributeNodes = n.getAttributes();
		if (attributeNodes != null) {
			for (int i = 0; i < attributeNodes.getLength(); i++) {
				Node attribute = attributeNodes.item(i);
				String value = PropertyParser.parse(attribute.getNodeValue(), variables);
				attributes.put(attribute.getNodeName(), value);
			}
		}
		return attributes;
	}

	public String getBodyData(Node child) {
		if (child.getNodeType() == Node.CDATA_SECTION_NODE || child.getNodeType() == Node.TEXT_NODE) {
			String data = ((CharacterData) child).getData();
			data = PropertyParser.parse(data, variables);
			return data;
		}
		return null;
	}

	public String parseBody(Node node) {
		String data = getBodyData(node);
		if (data == null) {
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				data = getBodyData(child);
				if (data != null) {
					break;
				}
			}
		}
		return data;
	}
}