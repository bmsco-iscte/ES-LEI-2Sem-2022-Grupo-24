package org.apache.ibatis.parsing;


import org.xml.sax.EntityResolver;
import java.util.Properties;
import javax.xml.xpath.XPath;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.apache.ibatis.builder.BuilderException;
import javax.xml.xpath.XPathFactory;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class XPathParserProduct {
	private XPathParserProductProduct xPathParserProductProduct = new XPathParserProductProduct();
	private Properties variables;
	public void setVariables(Properties variables) {
		this.variables = variables;
	}

	public Document createDocument(InputSource inputSource) {
		return xPathParserProductProduct.createDocument(inputSource);
	}

	public void commonConstructor(boolean validation, Properties variables, EntityResolver entityResolver) {
		xPathParserProductProduct.commonConstructor(validation, variables, entityResolver, this);
	}

	public Object evaluate(String expression, Object root, QName returnType) {
		return xPathParserProductProduct.evaluate(expression, root, returnType);
	}

	public Boolean evalBoolean(Object root, String expression) {
		return xPathParserProductProduct.evalBoolean(root, expression);
	}

	public Double evalDouble(Object root, String expression) {
		return xPathParserProductProduct.evalDouble(root, expression);
	}

	public String evalString(Object root, String expression) {
		String result = (String) xPathParserProductProduct.evaluate(expression, root, XPathConstants.STRING);
		result = PropertyParser.parse(result, variables);
		return result;
	}

	public List<XNode> evalNodes(Object root, String expression, XPathParser xPathParser) {
		List<XNode> xnodes = new ArrayList<>();
		NodeList nodes = (NodeList) xPathParserProductProduct.evaluate(expression, root, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			xnodes.add(new XNode(xPathParser, nodes.item(i), variables));
		}
		return xnodes;
	}

	public XNode evalNode(Object root, String expression, XPathParser xPathParser) {
		Node node = (Node) xPathParserProductProduct.evaluate(expression, root, XPathConstants.NODE);
		if (node == null) {
			return null;
		}
		return new XNode(xPathParser, node, variables);
	}
}