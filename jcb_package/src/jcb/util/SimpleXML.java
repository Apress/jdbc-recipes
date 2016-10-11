package jcb.util;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;
import org.apache.xpath.XPathAPI;
import javax.xml.transform.TransformerException;

import java.io.*;
import java.util.*;

/**
 * This class provides simple XML object for easy access. It keeps a Document object internally.
 * During object contruction, it generates DOM tree from file or string or input stream, etc. It
 * generates an empty Document object with default top node if input xml is not specified.
 * It provides set of APIs to access text nodes and their attributes. The class also provides APIs
 * to print out the XML object.
 * (XML).
 *
 * Example: <p>
 * <pre>
 * 1. Create SimleXML from XML file and get top children list
 *		 SimpleXML xml = new XML(new File(xmlfilename));
 *		 String [] children = xml.getChildren();
 *
 * 2. Create DOM tree from scrach *
 *		 SimpleXML xml = new XML("MyTop");
 *		 Node rootNode = xml.getRootNode();
 *		 Node node = xml.createNode("h3");
 *		 rootNode.appendChild(node);
 *		 xml.printTree();
 *
 * 3. Using text value from node which is located at xPath location
 *		 SimpleXML xml = new XML(new File("server.properties"));
 *		 String value = xml.getPathValue("/server/mailServer/Port");
 *
 * </pre>
 */
public class SimpleXML {

	static DocumentBuilderFactory _dbf = null;
	static boolean _ignoreWhitespace = false;
	static boolean _ignoreComments = false;
	static boolean _putCDATAIntoText = false;
	static boolean _createEntityRefs = false;
	static boolean _validation = false;
	//
	// Changed _out from an OutputStream to a Writer -
	// since JSP tags MUST use Writers instead of OutputStreams
	// OutputStream _out = null;
	//
	Writer _out = null;
	Document _doc = null;
	String xmlFilename = "";

	synchronized static DocumentBuilderFactory getDocumentBuilder() {
		if (_dbf == null) {
			try {
				_dbf = DocumentBuilderFactory.newInstance();
				_dbf.setValidating(_validation);
			}
			catch (Exception e){
				System.out.println("DocumentBuilderFactory:" + e.toString());
			}
		}
		return _dbf;
	}

	/**
	 * Constructs a class which holds Document from specified filename.
	 *
	 * @param file input file handle
	 * @exception Exception if specified file is not a valid XML file
	 */
	public SimpleXML(File file) throws Exception {
		// parse the input file
		try {
			xmlFilename = file.getAbsolutePath();
			DocumentBuilder db = getDocumentBuilder().newDocumentBuilder();
			_doc = db.parse(file);
		}
		catch (SAXException se){
			System.out.println("SAXException:" + se.getMessage());
			throw new Exception (se.getMessage());
		}
		catch (IOException ioe){
			System.out.println("IOException:" + ioe.toString());
			throw new Exception (ioe.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception:" + e.toString());
			throw new Exception(e.toString());
		}
	}

	/**
	 * Constructs a class which holds Document from specified document.
	 * Throw exception
	 * if specified file is not a valid XML file.
	 *
	 * @param doc input document
	 * @exception Exception if docment is not valid
	 */
	public SimpleXML(Document doc) throws Exception {
		_doc = doc;
	}


	/**
	 * Constrcuts a class which holds document from input string.
	 * @param str string which contains the entire XML
	 * @param notRoot dummy boolean to distinguish it from the other String constructor
	 * @exception Exception if input string is not valid XML.
	 */
	public SimpleXML(String str, boolean notRoot) throws Exception {
		try {
			if ((str.equals("")) || (str==null))
				throw new Exception("Empty XML string");
			StringReader sr = new StringReader(str);
			InputSource is = new InputSource(sr);
			DocumentBuilder db = getDocumentBuilder().newDocumentBuilder();
			_doc = db.parse(is);
		}
		catch (SAXException se) {
			System.out.println("SAXException:" + se.getMessage());
				throw new Exception (se.getMessage());
		}
		catch (IOException ioe) {
			System.out.println("IOException:" + ioe.toString());
			throw new Exception (ioe.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception:" + e.toString());
			throw new Exception(e.toString());
		}
	}



	/**
	 * Constructs a XML class which holds Document from input stream.
	 *
	 * @param inputStream
	 * @exception Exception if input stream is not valid XML
	 */
	public SimpleXML(InputStream inputStream) throws Exception {
			// parse the input file
		try {
			DocumentBuilder db = getDocumentBuilder().newDocumentBuilder();
			// Set an ErrorHandler before parsing
			// OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
			// db.setErrorHandler(new XMLErrorHandler(new PrintWriter(errorWriter, true)));
			_doc = db.parse(inputStream);
		}
		catch (SAXException se) {
			System.out.println("SAXException:" + se.getMessage());
			throw new Exception (se.getMessage());
		}
		catch (IOException ioe) {
			System.out.println("IOException:" + ioe.toString());
			throw new Exception (ioe.getMessage());
		}
		catch (Exception e) {
			System.out.println("Exception:" + e.toString());
			throw new Exception(e.toString());
		}
	}

	/**
	 * Constructs new empty XML class with specified name as root tag name.
	 *
	 * @param rootTag root tag name
	 * @exception Exception error to create Document from root tag.
	 */
	public SimpleXML(String rootTag) throws Exception {
		this(rootTag, new Hashtable()); // use an empty hashtable
	}

	/**
	 * Constructs new XML Document with specified name and attributes
	 * for root tag.
	 *
 	 * @param rootTag name of root tag
	 * @param attrs hashtable which holds name/value pairs associated with the root tag.
	 */
	public SimpleXML(String rootTag, Hashtable attrs) throws Exception {
		try {
			DocumentBuilder db = getDocumentBuilder().newDocumentBuilder();
			_doc = db.newDocument();
			createRootNode(rootTag, attrs);
		}
		catch (ParserConfigurationException pce) {
			throw new Exception(pce.toString());
		}
	}

	/**
	 * Create root node with specified name
	 * @param elementName name of root node.
	 */
	public Node createRootNode(String elementName) {
		return createRootNode(elementName, null);
	}

	/**
	 * Create root node with specified name and attributes
	 * @param elementName name of root tag
	 * @param attrs attributes associated with the root tag.
	 */
	public Node createRootNode(String elementName, Hashtable attrs) {

		System.out.println("Creating root: " + elementName);
		Element elementNode = _doc.createElement(elementName);
		if (attrs != null) {
			Enumeration names = attrs.keys();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String value = (String) attrs.get(name);
				elementNode.setAttribute(name, value);
			}
		}
		_doc.appendChild(elementNode);
		return elementNode;
	}


	/**
	 * Create a node with specified name and value
	 * @param elementName name of the element
	 * @param value value for the tag
	 */
	public Node createNode(String elementName, String value)
	{
		if (value == null) {
			value = "";
		}
		Element elementNode = _doc.createElement(elementName);
		Text textNode = _doc.createTextNode(value);
		elementNode.appendChild(textNode);
		return elementNode;
	}

	/**
	 * Create a node with specified element tag and attribute name and value
	 * @param elementName name of the element tag
	 * @param elementValue texzt value of the element tag
	 * @param attrName name of the attribute
	 * @param attrValue value for the attribute
	 */
	public Node createNode(String elementName,
						   String elementValue,
						   String attrName,
						   String attrValue) {

		if (elementValue == null) {
			elementValue = "";
		}
		Element elementNode = _doc.createElement(elementName);
		Text textNode = _doc.createTextNode(elementValue);
		if (attrValue != null) {
			elementNode.appendChild(textNode);
			elementNode.setAttribute(attrName, attrValue);
		}
		return elementNode;
	}

	/**
	 * Create a node with specified name and attributes
	 * @param elementName name of the element
	 * @param attrs hashtable which holds all name/value pairs as attributes for the element.
	 */
	public Node createNode(String elementName, Hashtable attrs)
	{
		Element elementNode = _doc.createElement(elementName);
		Enumeration names = attrs.keys();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = (String) attrs.get(name);
			elementNode.setAttribute(name, value);
		}
		return elementNode;
	}

	/**
	 * Create a node with specified name, value and attributes
	 * @param elementName name of the element
	 * @param elementValue text value of the element
	 * @param attrs hashtable which holds all name/value pairs as attributes for the element.
	 */
	public Node createNode(String elementName,
						   String elementValue,
						   Hashtable attrs) {
		Element elementNode = _doc.createElement(elementName);
		Text textNode = _doc.createTextNode(elementValue);
		elementNode.appendChild(textNode);
		Enumeration names = attrs.keys();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = (String) attrs.get(name);
			elementNode.setAttribute(name, value);
		}

		return elementNode;
	}

	/**
	 * Create a node with specified name, value and attributes
	 * @param elementName name of the element
	 * @param elementValue text value of the element
	 * @param attrs array of attribute names
	 * @param values array of attribute values
	 */
	public Node createNode(String elementName,
						   String elementValue,
						   String[] attrs,
						   String[] values) {
		Element elementNode = _doc.createElement(elementName);
		Text textNode = _doc.createTextNode(elementValue);
		elementNode.appendChild(textNode);
		for (int i=0; i < attrs.length; i++) {
			elementNode.setAttribute(attrs[i], values[i]);
		}
		return elementNode;
	}

	/**
	 * Create a node with specified name and attributes
	 * @param elementName name of the element
	 * @param attrs array of attribute names
	 * @param values array of attribute values
	 */
	public Node createNode(String elementName,
						   String[] attrs,
						   String[] values) {
		Element elementNode = _doc.createElement(elementName);
		//Text textNode = _doc.createTextNode(elementValue);
		//elementNode.appendChild(textNode);
		for (int i=0; i < attrs.length; i++) {
			elementNode.setAttribute(attrs[i], values[i]);
		}
		return elementNode;
	}

	/**
	 * Create a node with specified element name
	 * @param elementName name of the element
	 */
	public Node createNode(String elementName) {
		return _doc.createElement(elementName);
	}

	/**
	 * Append a node to the end of list.
	 * @param child node to append
	 */
	public Node add(Node child) {
		getRootNode().appendChild(child);
		return child;
	}

	/**
	 * Append a node to the end of list.
	 * @param elementName name of the node to append
	 * @param value text value set for the node
	 */
	public Node add(String elementName, String value) {
		Node node = createNode(elementName, value);
		getRootNode().appendChild(node);
		return node;
	}

	/**
	 * Append a node to an existing node
	 * @param parenetNode node to append child node
	 * @param elementName name of child node to append
	 * @param value value for the child node.
	 */
	public Node add(Node parentNode, String elementName, String value) {
		Node node = createNode(elementName, value);
		parentNode.appendChild(node);
		return node;
	}

	/**
	 * Append a node to an existing node
	 */
	public Node add(Node parentNode, String elementName, Hashtable attrs) {
		Node node = createNode(elementName, attrs);
		parentNode.appendChild(node);
		return node;
	}

	/**
	 * Get current Document object stored in the class
	 */
	public Document getDocument() {
		return _doc;
	}

	/**
	 * Get root node of the XML document
	 */
	public Node getRootNode() {
		return _doc.getFirstChild();
	}

	/**
	 * Get list of child name from specified node
	 * @param node input node
	 */
	public String [] getChildren(Node node) {
		Vector v = new Vector();
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				v.addElement(child.getNodeName());
			}
		}

		String [] list = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			list[i] = (String)	v.elementAt(i);
		}
		return list;
	}


	/**
	 * Get list of top first level children names
	 */
	String [] getChildren() {
		return getChildren(_doc);
	}


	/**
	 * Get list of child node in vector from specified node
	 * @param node input node
	 */
	public Vector getChildrenNodes(Node node) {
		Vector v = new Vector();
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				v.addElement(child);
			}
		}

		return v;
	}

	/**
	 * Get immediate child from the node base name
	 * @param node input node
	 * @param name name of child node
	 */
	public Node getChild(Node node, String name) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().compareTo(name) == 0) {
					return child;
				}
			}
		}
		return null;
	}


	/**
	 * Search node with specified tag name
	 * @param elementName name of element node
	 */
	Node searchNode(String elementName) {
		NodeList nodeList = _doc.getElementsByTagName(elementName);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0);
		}
		return null;
	}


	/**
	 * Get attribute value from its name under the specified node
	 * @param elemNode input node
	 * @param attrName name of the target attribute
	 */
	public static String getAttrText(Node elemNode, String attrName) {
		if (elemNode.hasAttributes() == false) {
			return null;
		}

		NamedNodeMap atts = elemNode.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Node attrNode = atts.item(i);
			String name = attrNode.getNodeName();
			if (attrName.compareToIgnoreCase(name) == 0) {
				return attrNode.getNodeValue();
			}
		}
		return null;
	}


	/**
	  * get attribute value of the node
	  * @param elemNode input node
	  * @param attrName name of the target attribute
	  */
	public String getAttrValue(Node elemNode, String attrName) {
		return getAttrText(elemNode,attrName);
	}

	/**
	 * Get list of attribute name/value as a string on specified node
	 */
	String getAttrNameValues(Node elemNode) {
		if (elemNode.hasAttributes() == false) {
			return null;
		}

		StringBuffer output = new StringBuffer();
		NamedNodeMap atts = elemNode.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Node attrNode = atts.item(i);
			String name = attrNode.getNodeName();
			output.append(" " + name + "=" + "\"" + attrNode.getNodeValue() + "\"");
		}
		return output.toString();
	}

	/**
	 * Get text value from current node
	 * @param elemNode input node
	 */
	public String getTextElementValue(Node elemNode) {
		return getText(elemNode);
	}

	/**
	 * Get text value from a node
	 * @param elemNode input node
	 * @return text value, null if there is no value
	 */
	 public static String getText(Node elemNode) {
		if (elemNode.hasChildNodes()) {
			for (Node child = elemNode.getFirstChild(); child != null; child = child.getNextSibling()) {
				if (child.getNodeType() == Node.TEXT_NODE) {
					String s =	child.getNodeValue();
					if (s != null) {
						return s.trim();
					}
					return null;
				}
			}
		}
		return null;

	 }

	/**
	 * Get text value of document location. Location is specified through
	 * xPath.
	 * <p>
	 *
	 * getPathValue("/server/db/action") returns first
	 * text value of <action> under <db>.
	 * <p>
	 * getPathValue("/server/db[@source="oracle"]/action")
	 * returns text value of <action> under oracle database
	 *
	 * @param path xPath value
	 * @return text value of the node.
	 */

	public String getPathValue(String path) {
		return getPathValue(getDocument(),path);
	}

	/**
	 * Get node value of document location.
	 * @param path xPath to specify location of the node
	 */
	public Node getPathNode(String path) {
		NodeIterator nl = null;
		Node n;
		try {
			//Use the XPathAPI class from Xalan to evaluate the expression
			nl = XPathAPI.selectNodeIterator(getDocument(), path);
			//Iterate over the resulting Node set, printing out the top-level results
			while ((n = nl.nextNode())!= null) {
				return n;
			}
		}
		catch (TransformerException e) {
			System.out.println("getPathValue:" + path + ":" + e.toString());
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Get text node value based on specified location under given node. This is static version.
	 * Path is following XPath syntax.
	 * <p>
	 * getPathValue("/server/db/action") returns first
	 * text value of <action> under <db>.
	 * <p>
	 * getPathValue("/server/db[@source="oracle"]/action")
	 * returns text value of <action> under oracle database
	 *
	 * @param node input node
	 * @param path xPath to locate return node.
	 */

	public static String getPathValue(Node node, String path) {
		NodeIterator nl = null;
		Node n;
		try {
			//Use the XPathAPI class from Xalan to evaluate the expression
			nl = XPathAPI.selectNodeIterator(node, path);
			//Iterate over the resulting Node set, printing out the top-level results
			while ((n = nl.nextNode())!= null) {
				return getText(n);
			}
		}
		catch (TransformerException e) {
			System.out.println("getPathValue:" + path + ":" + e.toString());
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * Get list of text values matching specified xPath.
	 * @param path xPath to find nodes in the xml document.
	 */
	public String[] getPathValues(String path) {
		NodeIterator nl = null;
		Node n;
		try {
			//Use the XPathAPI class from Xalan to evaluate the expression
			nl = XPathAPI.selectNodeIterator(getDocument(), path);
			Vector v = new Vector();
			//Iterate over the resulting Node set, return as a String array
			while ((n = nl.nextNode())!= null) {
				String value = getTextElementValue(n);
				if (value != null) {
					v.add(getTextElementValue(n));
				}
			}

			if (v.size() > 0) {
				String [] list = new String[v.size()];
				for (int i = 0; i < v.size(); i++) {
					list[i] = (String) v.elementAt(i);
				}
				return list;
			}
		}
		catch (TransformerException e) {
			System.out.println("getPathValues:" + path + ":" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return value of attribute which resides on first node of
	 * specified xPath.
	 *
	 * @param path xPath to locate node
	 * @param attrName input attribute name
	 *
	 */
	public String getPathAttrValue(String path, String attrName) {
		NodeIterator nl = null;
		Node n;
		try {
			//Use the XPathAPI class from Xalan to evaluate the expression
			nl = XPathAPI.selectNodeIterator(getDocument(), path);
			//Iterate over the resulting Node set, return first one on the list
			while ((n = nl.nextNode())!= null) {
				return getAttrValue(n, attrName);
			}
		}
		catch (TransformerException e) {
			System.out.println("getPathAttrValue:" + path + ":" + attrName + ":" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get list of attribue values from specified xPath and attribute
	 * name
	 * @param path xPath to locate nodes
	 * @param attrName input attribute name
	 *
	 **/
	public String [] getPathAttrValues(String path, String attrName) {
		NodeIterator nl = null;
		Node n;
		try {
			//Use the XPathAPI class from Xalan to evaluate the expression
			nl = XPathAPI.selectNodeIterator(getDocument(), path);
			Vector v = new Vector();
			//Iterate over the resulting Node set, return as a String array
			while ((n = nl.nextNode())!= null) {
				String value = getAttrValue(n, attrName);
				if (value != null) {
					v.add(value);
				}
			}

			if (v.size() > 0) {
				String [] list = new String[v.size()];
				for (int i = 0; i < v.size(); i++) {
					list[i] = (String) v.elementAt(i);
				}
				return list;
			}
		}
		catch (TransformerException e) {
			System.out.println("getPathAttrValues:" + path + ":" + attrName + ":" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Convenience method to select a list of nodes matching a given xpath,
	 * relative to the root.
	 *
	 * @param XPath to search for
	 * @exception javax.transform.TransformerException
	 */

	public NodeList selectNodeList(String xpath) throws TransformerException {
		return XPathAPI.selectNodeList(_doc.getDocumentElement(),xpath);
	}

	/**
	 * Convenience method to select a node matching a given xpath, relative to the root.
	 * Returns null if there are none.
	 *
	 * @param XPath to search for
	 * @exception javax.transform.TransformerException
	 */
	public Node selectSingleNode(String xpath) throws TransformerException {
		return XPathAPI.selectSingleNode(_doc.getDocumentElement(),xpath);
	}


	/**
	 * Get property of xPath node.
	 */
	public String getProperty(String xPath) {
		return getPathValue(xPath);
	}


	/**
	 * Set text value to the element
	 */
	public void setTextElementValue(Node node, String value) throws java.io.IOException {
		setText(node,value);
	}

	/**
	 * Set an element's text value, static version
	 */
	public static void setText(Node node, String value) throws java.io.IOException {
		for (Node child = node.getFirstChild();
			 child != null;
			 child = child.getNextSibling()) {

			if (child.getNodeType() == Node.TEXT_NODE) {
					child.setNodeValue(value);
					return;
			}
		}
		//	throw new java.io.IOException("No text element under the node");
		Text tnode = node.getOwnerDocument().createTextNode(value);
		node.appendChild(tnode);
	}


	void outputIndent(int level) {
		for (int i = 0; i < level; i++) {
			print("	");
		}
	}

	void print(String text) {
		try {
			if (_out == null) {
				System.out.print(text);
			}
			else {
				//_out.write(text.getBytes());
				_out.write(text);
			}
		}
		catch (java.io.IOException e) {
			System.out.println("OctoXML.print:" + e.toString());
		}
	}

	void println(String line) {
		try {
			if (_out == null) {
				System.out.println(line);
			}
			else {
				//_out.write(line.getBytes());
				_out.write(line);
				_out.write('\n');
			}
		}
		catch (java.io.IOException e) {
			System.out.println("OctoXML.println:" + e.toString());
		}
	}

	void outputOpenTag(int level, String tag, String value) {
		//System.out.println("output tag:" + tag, 9);
		outputIndent(level);
		if (value != null) {
			println("<" + tag + ">" + value);
		}
		else {
			println("<" + tag + ">");
		}
	}

	void outputOpenTag(int level, String tag, String attrList, String value) {
		//System.out.println("output tag:" + tag, 9);
		outputIndent(level);
		if (value != null) {
			println("<" + tag + attrList + ">" + value);
		}
		else {
			println("<" + tag + attrList + ">");
		}
	}

	void outputCloseTag(int level, String tag) {
		outputIndent(level);
		println("</" + tag + ">");
	}

	void printTree(Node node, int level) {
		String text = getTextElementValue(node);
		text = cleanup(text);
		String name = node.getNodeName();
		String attrList = getAttrNameValues(node);
		//System.out.println("printTree:" + name + ":" + text, 9);
		if (attrList != null) {
			outputOpenTag(level, name, attrList, text);
		}
		else {
			outputOpenTag(level, name, text);
		}


		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				printTree(child, level + 1);
			}
		}
		outputCloseTag(level, name);
	}

	public void printTree() {
		Node node = getRootNode();
		printTree(node, 0);
	}

	/**
	 * Output XML to OutputStream.
	 * @param out output stream to hold xml data
	 */
	public void printTree(OutputStream out) {
		OutputStreamWriter outwriter = new OutputStreamWriter(out);
		printTree(outwriter);
	}

	/**
	 * Output XML to OutputStream.
	 * @param node current node
	 * @param out output stream to hold node XML.
	 */
	public void printTree(Node node, OutputStream out) {
		OutputStreamWriter outwriter = new OutputStreamWriter(out);
		printTree(node, outwriter);
	}

	/**
	 * Output XML to Writer.
	 * @param out output stream to hold text xml document.
	 */
	public void printTree(Writer out) {
		_out = out;
		try {
			printTree();
			_out.flush();
		}
		catch (Exception e) {
			System.out.println("printTree:" + e.toString());
		}
		_out = null;
	}

	/**
	 * Output XML to Writer.
	 * @param node current node
	 * @param out output stream to hold text xml
	 */
	public void printTree(Node node, Writer out) {
		_out = out;
		try {
			printTree(node, 0);
			_out.flush();
		}
		catch (Exception e) {
			System.out.println("printTree:" + e.toString());
		}
		_out = null;
	}

	/**
	 * Output XML + XSL to OutputStream
	 *
	 * @param Output stream to write xml to
	 * @param Name of XSL file to write it to
	 * @exception java.io.IOException
	 */
	public void printTree(OutputStream out, String xslFile) throws java.io.IOException {
		_out = new OutputStreamWriter(out);
		printTreeXSL(xslFile);
		_out = null;
	}


	/**
	 * Output XML + XSL to Writer
	 * @param out output stream which holds text xml document
	 * @param xslFile xsl filename for style sheet transform.
	 */
	public void printTree(Writer out, String xslFile) throws java.io.IOException {
		_out = out;
		printTreeXSL(xslFile);
		_out = null;
	}

	void printTreeXSL(String xslFile) throws IOException {
		//System.out.println("Generate HTML from XML + " + xslFile, 9);
		try {
			_out.write(TransformUtil.transform(xslFile,_doc));
		}
		catch (Exception e) {
			//e.printStackTrace();
			String errorMsg = "<html><h1> Fail to generate HTML from XML:" + e.toString() + "</h1></html>";
			_out.write(errorMsg);
		}

	}

	/**
	  * Output XML to a String
	  */
	 public String xml() {
		StringWriter sw = new StringWriter();
		printTree(sw);
		return sw.toString();
	 }


	/**
	 * Prints out the tree as a single long line, no indentation or beautification
	 */
	public void printNoIndent(Writer out) {
		_out = out;
		printNodeNoIndent(getRootNode());
	}


	void printNodeNoIndent(Node node) {
		String text = getTextElementValue(node);
		text = cleanup(text);
		String name = node.getNodeName();
		String attrList = getAttrNameValues(node);
		//System.out.println("printNoIndent:" + name + ":" + text, 9);
		if (attrList != null) {
			openTagNoIndent(name, attrList, text);
		}
		else {
			openTagNoIndent(name, text);
		}


		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				printNodeNoIndent(child);
			}
		}
		closeTagNoIndent(name);
	}


	void openTagNoIndent(String tag, String value) {
		//System.out.println("output tag:" + tag, 9);
		if (value != null) {
			print("<" + tag + ">" + value);
		}
		else {
			print("<" + tag + ">");
		}
	}

	void openTagNoIndent( String tag, String attrList, String value) {
		//System.out.println("output tag:" + tag, 9);
		if (value != null) {
			print("<" + tag + attrList + ">" + value);
		}
		else {
			print("<" + tag + attrList + ">");
		}
	}

	void closeTagNoIndent(String tag) {
		print("</" + tag + ">");
	}

	/**
	 * Escape string data to follow XML standard.
	 * @param data input string for xml
	 */
	public static String escapeData(String data) {
		if (data == null) {
			return data;
		}
		StringBuffer newData = new StringBuffer();
		for (int i = 0; i < data.length(); i++) {
			char ch = data.charAt(i);
			if (ch == '<') {
				newData.append("&lt;");
			}
			else if (ch == '&') {
				newData.append("&amp;");
			}
			else if (ch == '>') {
				newData.append("&gt;");
			}
			else if (ch == '"') {
				newData.append("&quot;");
			}
			else if (ch == '\'') {
				newData.append("&apos;");
			}
			else {
				newData.append(ch);
			}
		}
		return newData.toString();
	}

	/**
	 * Print out the xml document
	 */
	public void print(Document doc) {
		printNode( doc, "" );
	}

	/**
	 * Print out the xml document under specified node
	 * @param n  input node
	 */
	public void printNode(Node n, String indent) {
		switch(n.getNodeType()) {

			case Node.DOCUMENT_NODE:
				NodeList children = n.getChildNodes();
				if( children != null ){
					for( int i = 0; i < children.getLength(); i++ ){
						printNode(children.item(i), indent + " " );
					}
				}
				break;
			case Node.ELEMENT_NODE:
				log( indent + getElementStart(n) );
				getElementChildren(n,indent) ;
				log( indent + getElementEnd(n) );
				break;
			case Node.TEXT_NODE:
				String text = getTextNode(n);
				if( text.length() > 0 ){
					log( indent + "	" + text );
				}
				break;
			case Node.PROCESSING_INSTRUCTION_NODE:
				log( indent + getProcessingInstructionNode(n) );
				break;
			case Node.COMMENT_NODE:
				log( indent + getCommentNode(n) );
				break;
		}
	}

	/**
	 * Logging message
	 */
	public void log(String msg){
		System.out.println("DocumentManager: " + msg);
	}

	/**
	 * Get starting node in String
	 * @param e  input node
	 */
	protected String getElementStart(Node e){
		StringBuffer buf = new StringBuffer();
		buf.append( "<" + e.getNodeName() );
		buf.append( getElementAttributes(e) );
		buf.append( ">" );
		return buf.toString();
	}

	/**
	 * Print out children of the node
	 */
	protected void getElementChildren(Node e, String indent){
		NodeList children = e.getChildNodes();
		if( children != null ){
			for( int i = 0; i < children.getLength(); i++ )
				printNode(children.item(i), indent + "	");
		}
	}

	protected String getElementEnd(Node e){
		return "</" + e.getNodeName() + ">";
	}

	protected String getElementAttributes(Node e){
		StringBuffer buf = new StringBuffer();
		NamedNodeMap attributes = e.getAttributes();
		for( int i = 0; i < attributes.getLength(); i++ ){
		Node n = attributes.item(i);
		buf.append( " " );
		buf.append( n.getNodeName() );
		buf.append( "=" );
		buf.append( n.getNodeValue() );
		}
		return buf.toString();
	}

	protected String getTextNode(Node t){
		return t.getNodeValue().trim();
	}

	protected String getProcessingInstructionNode(Node pi){
		return "<?" + pi.getNodeName() + " " + pi.getNodeValue() + "?>";
	}

	protected String getCommentNode(Node c){
		return "comment: " + c.getNodeValue();
	}

	/**
	 * Constructs a Document object from input string
	 */
	public static Document stringToDOM(String s) {

		if ((s == null) || (s.length() ==0)) {
			return null;
		}

		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(s));
			doc = builder.parse( is );
		}
		catch( Exception e){
			e.printStackTrace();
		}
		return doc;
	}


	/**
	 * Makes a string XML-safe by substituting escape sequences
	 */

	 public static String cleanup(String input)
	 {
		if (input==null) {
			return null;
		}

		String output = input.replaceAll("#","&#35;");
		output = output.replaceAll("&","&#38;");
		output = output.replaceAll("&#38;#35;","&#35;");
		output = output.replaceAll("<","&#60;");
		output = output.replaceAll(">","&#62;");
		return output;
	 }


	void test() {
		Node rootNode = getRootNode();
		Node node = createNode("html");
		rootNode.appendChild(node);
		printTree();
	}


	/**
	 * create a file in runtime.
	 */
	void testCreate() {
		Node htmlNode = getRootNode();
		Node node = getRootNode();
		Node bodyNode = createNode("body");
		Node h1Node = createNode("h1", "title is octopus!");
		Node h2Node = createNode("h2", "company overview!");
		htmlNode.appendChild(bodyNode);
		bodyNode.appendChild(h1Node);
		bodyNode.appendChild(h2Node);
		printTree();
	}

	/****
	note: removed for javadoc


	public static void main(String[] args) throws Exception {
		try {
			SimpleXML xml = new SimpleXML(new File(args[0]));
			Node node = xml.getPathNode(args[1]);

			System.out.println("node = " + node);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			xml.printTree(node, out);
			System.out.println("size = " + out.size());

			// System.out.println("out = " + out.toString());
			SimpleXML xml2 = new SimpleXML(out.toString(), true);
			xml2.printTree();
		}
		catch (Exception e) {
			System.out.println("error:" + e.toString());
		}

	}
	***/

}
