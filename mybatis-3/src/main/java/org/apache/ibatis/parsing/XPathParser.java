/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.builder.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class XPathParser {

  private XPathParserProduct xPathParserProduct = new XPathParserProduct();
private final Document document;
  public XPathParser(String xml) {
    xPathParserProduct.commonConstructor(false, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(new StringReader(xml)));
  }

  public XPathParser(Reader reader) {
    xPathParserProduct.commonConstructor(false, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(reader));
  }

  public XPathParser(InputStream inputStream) {
    xPathParserProduct.commonConstructor(false, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(inputStream));
  }

  public XPathParser(Document document) {
    xPathParserProduct.commonConstructor(false, null, null);
    this.document = document;
  }

  public XPathParser(String xml, boolean validation) {
    xPathParserProduct.commonConstructor(validation, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(new StringReader(xml)));
  }

  public XPathParser(Reader reader, boolean validation) {
    xPathParserProduct.commonConstructor(validation, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(reader));
  }

  public XPathParser(InputStream inputStream, boolean validation) {
    xPathParserProduct.commonConstructor(validation, null, null);
    this.document = xPathParserProduct.createDocument(new InputSource(inputStream));
  }

  public XPathParser(Document document, boolean validation) {
    xPathParserProduct.commonConstructor(validation, null, null);
    this.document = document;
  }

  public XPathParser(String xml, boolean validation, Properties variables) {
    xPathParserProduct.commonConstructor(validation, variables, null);
    this.document = xPathParserProduct.createDocument(new InputSource(new StringReader(xml)));
  }

  public XPathParser(Reader reader, boolean validation, Properties variables) {
    xPathParserProduct.commonConstructor(validation, variables, null);
    this.document = xPathParserProduct.createDocument(new InputSource(reader));
  }

  public XPathParser(InputStream inputStream, boolean validation, Properties variables) {
    xPathParserProduct.commonConstructor(validation, variables, null);
    this.document = xPathParserProduct.createDocument(new InputSource(inputStream));
  }

  public XPathParser(Document document, boolean validation, Properties variables) {
    xPathParserProduct.commonConstructor(validation, variables, null);
    this.document = document;
  }

  public XPathParser(String xml, boolean validation, Properties variables, EntityResolver entityResolver) {
    xPathParserProduct.commonConstructor(validation, variables, entityResolver);
    this.document = xPathParserProduct.createDocument(new InputSource(new StringReader(xml)));
  }

  public XPathParser(Reader reader, boolean validation, Properties variables, EntityResolver entityResolver) {
    xPathParserProduct.commonConstructor(validation, variables, entityResolver);
    this.document = xPathParserProduct.createDocument(new InputSource(reader));
  }

  public XPathParser(InputStream inputStream, boolean validation, Properties variables, EntityResolver entityResolver) {
    xPathParserProduct.commonConstructor(validation, variables, entityResolver);
    this.document = xPathParserProduct.createDocument(new InputSource(inputStream));
  }

  public XPathParser(Document document, boolean validation, Properties variables, EntityResolver entityResolver) {
    xPathParserProduct.commonConstructor(validation, variables, entityResolver);
    this.document = document;
  }

  public void setVariables(Properties variables) {
    xPathParserProduct.setVariables(variables);
  }

  public String evalString(String expression) {
    return xPathParserProduct.evalString(document, expression);
  }

  public String evalString(Object root, String expression) {
    return xPathParserProduct.evalString(root, expression);
  }

  public Boolean evalBoolean(String expression) {
    return xPathParserProduct.evalBoolean(document, expression);
  }

  public Boolean evalBoolean(Object root, String expression) {
    return xPathParserProduct.evalBoolean(root, expression);
  }

  public Short evalShort(String expression) {
    return evalShort(document, expression);
  }

  public Short evalShort(Object root, String expression) {
    return Short.valueOf(xPathParserProduct.evalString(root, expression));
  }

  public Integer evalInteger(String expression) {
    return evalInteger(document, expression);
  }

  public Integer evalInteger(Object root, String expression) {
    return Integer.valueOf(xPathParserProduct.evalString(root, expression));
  }

  public Long evalLong(String expression) {
    return evalLong(document, expression);
  }

  public Long evalLong(Object root, String expression) {
    return Long.valueOf(xPathParserProduct.evalString(root, expression));
  }

  public Float evalFloat(String expression) {
    return evalFloat(document, expression);
  }

  public Float evalFloat(Object root, String expression) {
    return Float.valueOf(xPathParserProduct.evalString(root, expression));
  }

  public Double evalDouble(String expression) {
    return xPathParserProduct.evalDouble(document, expression);
  }

  public Double evalDouble(Object root, String expression) {
    return xPathParserProduct.evalDouble(root, expression);
  }

  public List<XNode> evalNodes(String expression) {
    return xPathParserProduct.evalNodes(document, expression, this);
  }

  public List<XNode> evalNodes(Object root, String expression) {
    return xPathParserProduct.evalNodes(root, expression, this);
  }

  public XNode evalNode(String expression) {
    return xPathParserProduct.evalNode(document, expression, this);
  }

  public XNode evalNode(Object root, String expression) {
    return xPathParserProduct.evalNode(root, expression, this);
  }

}
