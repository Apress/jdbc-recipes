package jcb.util;

import org.xml.sax.InputSource;
import org.w3c.dom.*;


import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.jdom.transform.*;
import org.jdom.output.*;
import org.jdom.input.*;


import java.io.*;
import java.util.*;

 /**
  * This class is a utility class; it provides
  * methods for manipulation of XML documents.
  * <p>
  * @author: Mahmoud Parsian
  * @since JDK 1.4
  *
  */
 public class DocumentManager {

	  static DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY =  null;

	  static {
		  try {
			  DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
			  DOCUMENT_BUILDER_FACTORY.setValidating(false);
		  }
		  catch(Exception e) {
			  // ignore for now.
		  }
	  }

	 public static final String HTML_END_TAG_UPPERCASE = "</HTML>";
	 public static final String HTML_END_TAG_LOWERCASE = "</html>";

	 public static final int UNDEFINED_POSITION_VALUE = -1;

	 private static final String LIST_TAG_BEGIN =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><result rank=\"1\" score=\"1.0\"><title><titleBlock/></title><abstract><abstractBlock/></abstract><list firstResult=\"0\" ";
	 private static final String LIST_TAG_END   =  "</list></result></resultSet>";
	 private static final String LIST_TAG_COMPLEX_NULL =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><result rank=\"1\" score=\"1.0\"><title><titleBlock/></title><abstract><abstractBlock/></abstract><list firstResult=\"0\"  numResults=\"0\" of=\"complex\" totalResults=\"0\"></list></result></resultSet>";
	 private static final String LIST_TAG_SIMPLE_NULL =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><result rank=\"1\" score=\"1.0\"><title><titleBlock/></title><abstract><abstractBlock/></abstract><list firstResult=\"0\"  numResults=\"0\" of=\"simple\" totalResults=\"0\"></list></result></resultSet>";

	 private static final String LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><result rank=\"1\" score=\"1.0\"><title><titleBlock/></title><abstract><abstractBlock/></abstract><list firstResult=\"0\" of=\"simple\" numResults=\"1\" totalResults=\"1\">";
	 private static final String LIST_TAG_BEGIN_COMPLEX_ONE_ELEMENT =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><result rank=\"1\" score=\"1.0\"><title><titleBlock/></title><abstract><abstractBlock/></abstract><list firstResult=\"0\" of=\"complex\" numResults=\"1\" totalResults=\"1\">";

	 //
	 // this tag denotes the error tag beginning
	 //
	 private static final String INFO_TAG_BEGIN =   "<resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><information><message>";
	 private static final String ERROR_TAG_BEGIN =  "<resultSet firstResult=\"0\" moreResults=\"false\" numResults=\"1\" totalResults=\"1\"><errata><error>";

	 //
	 // this tag denotes the error tag ending
	 //
	 private static final String INFO_TAG_END =  "</message></information></resultSet>";
	 private static final String ERROR_TAG_END =  "</error></errata></resultSet>";

	 public static String removeTrailingHTML(String text) {
		if  ((text == null) || (text.length() == 0)) {
			return text;
		}

		int indexOfHTML = text.lastIndexOf(HTML_END_TAG_LOWERCASE);
		if (indexOfHTML == -1) {
			// then it is not found. check for </HTML>
			indexOfHTML = text.lastIndexOf(HTML_END_TAG_UPPERCASE);
			if (indexOfHTML == -1) {
				// not found
			}
			else {
				text = text.substring(0, indexOfHTML+7);
			}
		}
		else {
			text = text.substring(0, indexOfHTML+7);
		}

		return text;
	 }

	 public static String getErrorMessage(String error) {
		return ERROR_TAG_BEGIN+error+ERROR_TAG_END;
	 }

	 public static String getMessage(String message) {
		return INFO_TAG_BEGIN+message+INFO_TAG_END;
	 }


  public void print(Document doc) {
    printNode( doc, "" );
  }

  private void printNode(Node n, String indent) {
    switch(n.getNodeType()) {
      case Node.DOCUMENT_NODE:
        NodeList children = n.getChildNodes();
        if( children != null )
        {
          for( int i = 0; i < children.getLength(); i++ )
          {
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
        if( text.length() > 0 )
        {
          log( indent + "  " + text );
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

  private void log(String msg){
    System.out.println("DocumentManager: " + msg);
  }


  private String getElementStart(Node e){
    StringBuffer buf = new StringBuffer();
    buf.append( "<" + e.getNodeName() );
    buf.append( getElementAttributes(e) );
    buf.append( ">" );
    return buf.toString();
  }

  private void getElementChildren(Node e, String indent){
    NodeList children = e.getChildNodes();
    if( children != null ){
      for( int i = 0; i < children.getLength(); i++ )
        printNode(children.item(i), indent + "  ");
    }
  }

  private String getElementEnd(Node e){
    return "</" + e.getNodeName() + ">";
  }

  private String getElementAttributes(Node e){
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

  private String getTextNode(Node t){
    return t.getNodeValue().trim();
  }

  private String getProcessingInstructionNode(Node pi){
    return "<?" + pi.getNodeName() + " " + pi.getNodeValue() + "?>";
  }

  private String getCommentNode(Node c){
	  return "comment: " + c.getNodeValue();
  }

  public static Document stringToDOM(StringBuffer sb) {
	  return stringToDOM(new String(sb));
  }


  public static Document stringToDOM(String s) {
		//System.out.println("DocumentManager: stringToDOM(): begin -----------------");
		//System.out.println(s);
		//System.out.println("DocumentManager: stringToDOM(): end -----------------");

		if ((s == null) || (s.length() ==0)) {
			return null;
		}

		Document doc = null;
		try {
		  //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  //factory.setValidating(false);
		  DocumentBuilder builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
		  //builder.setErrorHandler(null);
		  //builder.setEntityResolver(null);
		  InputSource is = new InputSource(new StringReader(s));
		  doc = builder.parse( is );
		}
		catch( Exception e){
		  e.printStackTrace();
		}

		return doc;
  }





  	/****
  	public static Document stringToDOM(String s) {
	  	// mp: this is the NEW one
		if ((s == null) || (s.length() ==0)) {
			return null;
		}

		Document doc = null;

		try {
			InputSource source = new InputSource(new StringReader(s));
			DOMParser parser = new DOMParser();
		    //parser.setValidating(false);
			parser.parse(source);
			doc = parser.getDocument();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}
	****/




    /***
	private static String getParameter(String parameterName, Document parameters) {
		try {
			 NodeList list = parameters.getElementsByTagName(parameterName);
			 Node node = (Node) list.item(0);
			 Text result = (Text) node.getFirstChild();
			 String value = result.getNodeValue();
			 System.out.println("sqladapter: getParameter(): parameterName="+parameterName+"\tparameterValue="+value);
			 return value;
		}
		catch (Exception e) {
			return null;
		}
	}
	***/

   /**
    * Return an Element given an XML document, tag name, and index
    *
    * @param     doc     XML docuemnt
    * @param     tagName a tag name
    * @param     index   a index
    * @return    an Element
    */
   public static Element getElement(Document doc, String tagName, int index ){

      NodeList rows = doc.getDocumentElement().getElementsByTagName( tagName );
      //printNodeTypes( rows );
      return (Element)rows.item( index );
   }

   /**
    * Return an Element given an XML document, tag name, and index
    *
    * @param     element     XML Element
    * @param     tagName a tag name
    * @param     index   a index
    * @return    an Element
    */
   public static Element getElement(Element element, String tagName, int index ){

      NodeList rows = element.getElementsByTagName( tagName );
      //printNodeTypes( rows );
      return (Element)rows.item( index );
   }

   /**
    * Return an Element given an XML document, tag name, and index
    *
    * @param     doc     XML docuemnt
    * @param     tagName a tag name
    * @param     index   a index
    * @return    an Element
    */
   public static Element getElement(SimpleXML xml, String tagName, int index ){
	   return getElement(xml.getDocument(), tagName, index );
   }

   /**
    * Return the number of document in an XML document
    * (given an XML document and a tag name, return the number of ocurances)
    *
    *  @param     element     XML Element
    *  @param     tagName a tag name
    *  @return    the number of document in an XML document
    */
   public static int getSize( Element element, String tagName ){

      NodeList rows = element.getElementsByTagName( tagName );
      if (rows == null) {
		  return 0;
	  }

      return rows.getLength();
   }

   /**
    * Return the number of document in an XML document
    * (given an XML document and a tag name, return the number of ocurances)
    *
    *  @param     doc     XML document
    *  @param     tagName a tag name
    *  @return    the number of document in an XML document
    */
   public static int getSize( Document doc , String tagName ){

      NodeList rows = doc.getDocumentElement().getElementsByTagName( tagName );
      if (rows == null) {
		  return 0;
	  }

      return rows.getLength();
   }

   /**
    * Return the number of document in an XML document
    * (given an XML document and a tag name, return the number of ocurances)
    *
    *  @param     doc     XML document
    *  @param     tagName a tag name
    *  @return    the number of document in an XML document
    */
   public static int getSize( SimpleXML xml , String tagName ){
      return getSize(xml.getDocument(), tagName);
   }

   /**
    *  Given a document element, must get the element specified
    *  by the tagName, then must traverse that Node to get the value.
    *  Step1) get Element of name tagName from e
    *  Step2) cast element to Node and then traverse it for its non-whitespace, cr/lf value.
    *  Step3) return it!
    *
    *  NOTE: Element is a subclass of Node
    *
    *  @param    e   an Element
    *  @param    tagName a tag name
    *  @return   s   the value of a Node
    */
   public static String getValue( Element e , String tagName ){
      try{
         //get node lists of a tag name from a Element
         NodeList elements = e.getElementsByTagName( tagName );
         if (elements == null) {
			 return null;
		 }
		 //printNodeTypes(elements);

         Node node = elements.item( 0 );
         if (node == null) {
			 return null;
		 }

         NodeList nodes = node.getChildNodes();
		 //printNodeTypes(nodes);

         //find a value whose value is non-whitespace
         String s;
         for( int i=0; i<nodes.getLength(); i++){
            s = ((Node)nodes.item( i )).getNodeValue().trim();
            if(s.equals("") || s.equals("\r")) {
               continue;
            }
            else return s;
         }

      }
      catch(Exception ex){
         //System.out.println( ex );
         ex.printStackTrace();
      }

      return null;

   }

   /**
    *  Given a document element, must get the element specified
    *  by the tagName, then must traverse that Node to get the value.
    *  Step1) get Element of name tagName from e
    *  Step2) cast element to Node and then traverse it for its non-whitespace, cr/lf value.
    *  Step3) return it!
    *
    *  NOTE: Element is a subclass of Node
    *
    *  @param    e   an Element
    *  @param    tagName a tag name
    *  @return   s   the value of a Node
    */
   public static int getIntegerValue( Element e , String tagName ){
      try{
         //get node lists of a tag name from a Element
         NodeList elements = e.getElementsByTagName( tagName );
         if (elements == null) {
			 return UNDEFINED_POSITION_VALUE;
		 }
		 //printNodeTypes(elements);

         Node node = elements.item( 0 );
         if (node == null) {
			 return UNDEFINED_POSITION_VALUE;
		 }

         NodeList nodes = node.getChildNodes();
		 //printNodeTypes(nodes);

         //find a value whose value is non-whitespace
         String s;
         for( int i=0; i<nodes.getLength(); i++){
            s = ((Node)nodes.item( i )).getNodeValue().trim();
            if(s.equals("") || s.equals("\r")) {
               continue;
            }
            else {
				if ((s == null) || (s.length() == 0)) {
					return UNDEFINED_POSITION_VALUE;
				}
				else {
					try {
						return Integer.parseInt(s);
					}
					catch(Exception ex) {
						return UNDEFINED_POSITION_VALUE;
					}
				}
			}
         }

      }
      catch(Exception ex){
         //System.out.println(ex);
         ex.printStackTrace();
      }

      return UNDEFINED_POSITION_VALUE;

   }

   /**
    *  For testing purpose, it print out Node list
    *
    *  @param     rows    a Nodelist
    */
   public static void printNodeTypes( NodeList rows ){

      System.out.println( "---- printNodeTypes() ---- begin");
      System.out.println( "\tenumerating NodeList (of Elements):");
      System.out.println( "\tClass\tNT\tNV" );
      //iterate a given Node list
      for( int ri = 0 ; ri < rows.getLength() ; ri++){
         Node n = (Node)rows.item( ri );
         if( n instanceof Element) {
            System.out.print( "\tElement" );
         }
         else {
            System.out.print( "\tNode" );
         }

         //print out Node type and Node value
         System.out.println("\t"+n.getNodeType() + "\t" +n.getNodeValue());
      }

   	System.out.println();
   	System.out.println( "---- printNodeTypes() ---- end");
   }

	public static String getParameter(String parameterName, SimpleXML xml) {
		try {
			 return getParameter(parameterName, xml.getDocument());
		}
		catch (Exception e) {
			return null;
		}
	}

	public static String getParameter(String parameterName, Document parameters) {
		try {
			 NodeList list = parameters.getElementsByTagName(parameterName);
			 Node node = (Node) list.item(0);
			 Text result = (Text) node.getFirstChild();
			 String value = result.getNodeValue();
			 //System.out.println("adapter: getParameter(): parameterName="+parameterName+"\tparameterValue="+value);
			 return value;
		}
		catch (Exception e) {
			return null;
		}
	}



  public static Document toDocument(java.util.Date date) {
		if (date == null) {
			return stringToDOM(LIST_TAG_SIMPLE_NULL);
		}

		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"date\">");
		buffer.append(date);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return stringToDOM(buffer);
  }

  public static Document toDocument(java.sql.Date date) {
		if (date == null) {
			return stringToDOM(LIST_TAG_SIMPLE_NULL);
		}

		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"date\">");
		buffer.append(date);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return stringToDOM(buffer);
  }

  public static Document toDocument(java.lang.Void input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"void\">void</simple>");
		buffer.append(LIST_TAG_END);
		return stringToDOM(buffer);
  }

  //
  // String
  //
  public static String toStringXML(java.lang.String str) {
		if (str == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
			buffer.append("<simple name=\"element\" type=\"string\">");
			buffer.append(str);
			buffer.append("</simple>");
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(java.lang.StringBuffer sb) {
		return stringToDOM(toStringXML(sb.toString()));
  }

  public static Document toDocument(java.lang.String str) {
		return stringToDOM(toStringXML(str));
  }

  public static Document toDocument(org.w3c.dom.Document doc) {
	  return doc;
  }

  //
  // char and Character
  //
  public static String toStringXML(char input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"string\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Character input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.charValue());
		}
  }

  public static Document toDocument(java.lang.Character input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(char input) {
	  return stringToDOM(toStringXML(input));
  }


  //
  // boolean  and Boolean
  //
  public static String toStringXML(boolean input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"boolean\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Boolean input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.booleanValue());
		}
  }

  public static Document toDocument(java.lang.Boolean input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(boolean input) {
	  return stringToDOM(toStringXML(input));
  }


  //
  // short  and Short
  //
  public static String toStringXML(short input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"integer\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Short input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.shortValue());
		}
  }

  public static Document toDocument(java.lang.Short input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(short input) {
	  return stringToDOM(toStringXML(input));
  }

  //
  // int  and Integer
  //
  public static String toStringXML(int input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"integer\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Integer input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.intValue());
		}
  }

  public static Document toDocument(java.lang.Integer input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(int input) {
	  return stringToDOM(toStringXML(input));
  }


  //
  // long  and Long
  //
  public static String toStringXML(long input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"integer\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Long input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.longValue());
		}
  }

  public static Document toDocument(java.lang.Long input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(long input) {
	  return stringToDOM(toStringXML(input));
  }


  //
  // float  and Float
  //
  public static String toStringXML(float input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"float\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Float input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.floatValue());
		}
  }

  public static Document toDocument(java.lang.Float input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(float input) {
	  return stringToDOM(toStringXML(input));
  }


  //
  // double  and Double
  //
  public static String toStringXML(double input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"double\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Double input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.doubleValue());
		}
  }

  public static Document toDocument(java.lang.Double input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(double input) {
	  return stringToDOM(toStringXML(input));
  }



  //
  // byte  and Byte
  //
  public static String toStringXML(byte input) {
		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN_SIMPLE_ONE_ELEMENT);
		buffer.append("<simple name=\"element\" type=\"byte\">");
		buffer.append(input);
		buffer.append("</simple>");
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(java.lang.Byte input) {
		if (input == null) {
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			return toStringXML(input.byteValue());
		}
  }

  public static Document toDocument(java.lang.Byte input) {
	  return stringToDOM(toStringXML(input));
  }

  public static Document toDocument(byte input) {
	  return stringToDOM(toStringXML(input));
  }



  public static String getObjectType(Object value) {
		if (value == null) {
			return DataTypes.STRING_TYPE;
		}
		else if (value instanceof java.lang.Byte) {
			return DataTypes.BYTE_TYPE;
		}
		else if (value instanceof java.lang.Boolean) {
			return DataTypes.BOOLEAN_TYPE;
		}
		else if (value instanceof java.lang.String) {
			return DataTypes.STRING_TYPE;
		}
		else if (value instanceof java.lang.Long) {
			return DataTypes.LONG_TYPE;
		}
		else if (value instanceof java.lang.Integer) {
			return DataTypes.INTEGER_TYPE;
		}
		else if (value instanceof java.lang.Short) {
			return DataTypes.SHORT_TYPE;
		}
		else if (value instanceof java.lang.Float) {
			return DataTypes.FLOAT_TYPE;
		}
		else if (value instanceof java.lang.Double) {
			return DataTypes.DOUBLE_TYPE;
		}
		else if (value instanceof java.lang.Character) {
			return DataTypes.CHAR_TYPE;
		}
		else if (value instanceof java.sql.Date) {
			return DataTypes.SQL_DATE_TYPE;
		}
		else if (value instanceof java.util.Date) {
			return DataTypes.UTIL_DATE_TYPE;
		}
		else if (value instanceof java.util.Hashtable) {
			return DataTypes.STRUCT_TYPE;
		}
		else if (value instanceof java.util.List) {
			return DataTypes.STRUCT_TYPE;
		}
		else {
			return DataTypes.UNDEFINED_TYPE;
		}
	}

	public static String nullToXML() {
		return LIST_TAG_COMPLEX_NULL;
	}


	public static Document toDocument(java.util.List list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}

	public static Document toDocument(java.util.AbstractList list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}

	public static Document toDocument(java.util.ArrayList list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}

	public static Document toDocument(java.util.LinkedList list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}

	public static Document toDocument(java.util.AbstractSequentialList list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}

	public static Document toDocument(java.util.Vector list) {
		return stringToDOM(collectionToXML((java.util.Collection)list));
	}


	public static Document toDocument(java.util.Collection collection) {
		return stringToDOM(collectionToXML(collection));
	}

	public static Document toDocument(java.util.AbstractCollection acollection) {
		return stringToDOM(collectionToXML((java.util.Collection)acollection));
	}

	public static Document toDocument(java.util.Set set) {
		return stringToDOM(collectionToXML((java.util.Collection)set));
	}

	public static Document toDocument(java.util.SortedSet sset) {
		return stringToDOM(collectionToXML((java.util.Collection)sset));
	}

	public static Document toDocument(java.util.AbstractSet set) {
		return stringToDOM(collectionToXML((java.util.Collection)set));
	}

	public static Document toDocument(java.util.HashSet set) {
		return stringToDOM(collectionToXML((java.util.Collection)set));
	}

	public static Document toDocument(java.util.LinkedHashSet set) {
		return stringToDOM(collectionToXML((java.util.Collection)set));
	}

	public static Document toDocument(java.util.TreeSet set) {
		return stringToDOM(collectionToXML((java.util.Collection)set));
	}

	public static String collectionToXML(java.util.Collection collection) {
		if (collection == null) {
			return LIST_TAG_SIMPLE_NULL;
		}

		int size = collection.size();
		if (size == 0)	{
			return LIST_TAG_SIMPLE_NULL;
		}

		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
		buffer.append(" of=\"simple\" numResults=\"");
		buffer.append(size);
		buffer.append("\" totalResults=\"");
		buffer.append(size);
		buffer.append("\">");

		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			buffer.append("<simple name=\"element\" type=\"string\">");
			buffer.append((String)iterator.next());
			buffer.append("</simple>");
		}

		buffer.append(LIST_TAG_END);
		return buffer.toString();
	}


	public static Document toDocument(java.util.Map map) {
		return stringToDOM(mapToXML(map));
	}

	public static Document toDocument(java.util.SortedMap smap) {
		return stringToDOM(mapToXML((java.util.Map)smap));
	}

	public static Document toDocument(java.util.Hashtable map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.Properties map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.AbstractMap map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.HashMap map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.WeakHashMap map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.IdentityHashMap map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static Document toDocument(java.util.TreeMap map) {
		return stringToDOM(mapToXML((java.util.Map)map));
	}

	public static String mapToXML(java.util.SortedMap smap) {
		return mapToXML( (java.util.Map) smap );
	}

	public static String mapToXML(java.util.Map map) {
		if (map == null)	{
			return LIST_TAG_COMPLEX_NULL;
		}

		int size = map.size();
		if (size == 0)	{
			return LIST_TAG_COMPLEX_NULL;
		}

		Set keys = map.keySet();
		if ((keys == null) || (keys.size() == 0)) {
			return LIST_TAG_COMPLEX_NULL;
		}

		StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
		buffer.append(" of=\"complex\" numResults=\"");
		buffer.append(size);
		buffer.append("\" totalResults=\"");
		buffer.append(size);
		buffer.append("\">");

		Iterator iterator = keys.iterator();
		while (iterator.hasNext()) {

			Object key = iterator.next();
			Object value = map.get(key);

			buffer.append("<complex><simple name=\"key\" type=\"");
			buffer.append(getObjectType(key));
			buffer.append("\">");
			buffer.append(key);
			buffer.append("</simple><simple name=\"value\" type=\"");
			buffer.append(getObjectType(value));
			buffer.append("\">");
			buffer.append(value);
			buffer.append("</simple></complex>");
		}
		buffer.append(LIST_TAG_END);
		return buffer.toString();
  }

  public static String toStringXML(Character[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"string\">");
				if (array[i] != null) {
					buffer.append(array[i].charValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(char[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"string\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Character[] array) {
	  return stringToDOM(toStringXML(array) );
  }

  public static Document toDocument(char[] array) {
	  return stringToDOM(toStringXML(array) );
  }


  public static String toStringXML(boolean[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"boolean\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Boolean[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"boolean\">");
				if (array[i] != null) {
					buffer.append(array[i].booleanValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Boolean[] array) {
	  return stringToDOM(toStringXML(array) );
  }

  public static Document toDocument(boolean[] array) {
	  return stringToDOM(toStringXML(array) );
  }

  public static String toStringXML(byte[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"byte\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Byte[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"byte\">");
				if (array[i] != null) {
					buffer.append(array[i].byteValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Byte[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static Document toDocument(byte[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(short[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"short\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Short[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"short\">");
				if (array[i] != null) {
					buffer.append(array[i].shortValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Short[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static Document toDocument(short[] array) {
	  return stringToDOM(toStringXML(array));
  }


  public static String toStringXML(int[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"integer\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Integer[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"integer\">");
				if (array[i] != null) {
					buffer.append(array[i].intValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Integer[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static Document toDocument(int[] array) {
	  return stringToDOM(toStringXML(array));
  }


  public static String toStringXML(long[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"long\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Long[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"long\">");
				if (array[i] != null) {
					buffer.append(array[i].longValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Long[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static Document toDocument(long[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(float[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"float\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Float[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"float\">");
				if (array[i] != null) {
					buffer.append(array[i].floatValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Float[] array) {
	  return stringToDOM(toStringXML(array));
  }
  public static Document toDocument(float[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(double[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"double\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(Double[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"double\">");
				if (array[i] != null) {
					buffer.append(array[i].doubleValue());
				}
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(Double[] array) {
	  return stringToDOM(toStringXML(array));
  }
  public static Document toDocument(double[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(String[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"string\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(String[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(StringBuffer[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"string\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static Document toDocument(StringBuffer[] array) {
	  return stringToDOM(toStringXML(array));
  }



  public static Document toDocument(java.sql.Date[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static Document toDocument(java.util.Date[] array) {
	  return stringToDOM(toStringXML(array));
  }

  public static String toStringXML(java.sql.Date[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"date\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }

  public static String toStringXML(java.util.Date[] array) {
		if ((array == null) || (array.length == 0))	{
			return LIST_TAG_SIMPLE_NULL;
		}
		else {
			StringBuffer buffer = new StringBuffer(LIST_TAG_BEGIN);
			buffer.append(" of=\"simple\" numResults=\"");
			buffer.append(array.length);
			buffer.append("\" totalResults=\"");
			buffer.append(array.length);
			buffer.append("\">");
			for (int i=0; i < array.length; i++) {
				buffer.append("<simple name=\"element\" type=\"date\">");
				buffer.append(array[i]);
				buffer.append("</simple>");
			}
			buffer.append(LIST_TAG_END);
			return buffer.toString();
		}
  }


   public static void main( String [] args ) throws Exception {

		String s = "<request><operation id=\"login\"><dsn>mysql</dsn></operation><operation id=\"login\"><dsn>mysql</dsn></operation></request>";
		Document d = stringToDOM(s);

		DocumentManager mgr = new DocumentManager();
		mgr.print(d);


		java.util.Date javaUtilDate = new java.util.Date();
		java.sql.Date javaSqlDate = new java.sql.Date(javaUtilDate.getTime());

		Document utildate = toDocument(javaUtilDate);
		mgr.print(utildate);

		Document sqldate = toDocument(javaSqlDate);
		mgr.print(sqldate);
  }


  public static Document toDocument(java.lang.Object input)
  	throws Exception {

	  if (input == null) {
		  // return an empty result set
		  return stringToDOM(nullToXML());
	  }


	  if (input instanceof org.w3c.dom.Document) {
		  return (org.w3c.dom.Document) input;
	  }
	  else if (input instanceof java.lang.Void) {
		  return toDocument( (java.lang.Void) input );
	  }
	  else if (input instanceof java.sql.Date) {
		  return toDocument( (java.sql.Date) input );
	  }
	  else if (input instanceof java.util.Date) {
		  return toDocument( (java.util.Date) input );
	  }
	  else if (input instanceof java.lang.StringBuffer) {
		  return toDocument( (java.lang.StringBuffer) input );
	  }
	  else if (input instanceof java.lang.String) {
		  return toDocument( (java.lang.String) input );
	  }
	  else if (input instanceof java.lang.Boolean) {
		  return toDocument( (java.lang.Boolean) input );
	  }
	  else if (input instanceof java.lang.Character) {
		  return toDocument( (java.lang.Character) input );
	  }
	  else if (input instanceof java.lang.Short) {
		  return toDocument( (java.lang.Short) input );
	  }
	  else if (input instanceof java.lang.Integer) {
		  return toDocument( (java.lang.Integer) input );
	  }
	  else if (input instanceof java.lang.Long) {
		  return toDocument( (java.lang.Long) input );
	  }
	  else if (input instanceof java.lang.Float) {
		  return toDocument( (java.lang.Float) input );
	  }
	  else if (input instanceof java.lang.Double) {
		  return toDocument( (java.lang.Double) input );
	  }
	  else if (input instanceof java.lang.Byte) {
		  return toDocument( (java.lang.Byte) input );
	  }
	  else if (input instanceof java.sql.Date[]) {
		  return toDocument( (java.sql.Date[]) input );
	  }
	  else if (input instanceof java.util.Date[]) {
		  return toDocument( (java.util.Date[]) input );
	  }
	  else if (input instanceof java.lang.StringBuffer[]) {
		  return toDocument( (java.lang.StringBuffer[]) input );
	  }
	  else if (input instanceof java.lang.String[]) {
		  return toDocument( (java.lang.String[]) input );
	  }
	  else if (input instanceof java.lang.Boolean[]) {
		  return toDocument( (java.lang.Boolean[]) input );
	  }
	  else if (input instanceof java.lang.Character[]) {
		  return toDocument( (java.lang.Character[]) input );
	  }
	  else if (input instanceof java.lang.Short[]) {
		  return toDocument( (java.lang.Short[]) input );
	  }
	  else if (input instanceof java.lang.Integer[]) {
		  return toDocument( (java.lang.Integer[]) input );
	  }
	  else if (input instanceof java.lang.Long) {
		  return toDocument( (java.lang.Long) input );
	  }
	  else if (input instanceof java.lang.Float[]) {
		  return toDocument( (java.lang.Float[]) input );
	  }
	  else if (input instanceof java.lang.Double[]) {
		  return toDocument( (java.lang.Double[]) input );
	  }
	  else if (input instanceof java.lang.Byte[]) {
		  return toDocument( (java.lang.Byte[]) input );
	  }
	  else if (input instanceof char[]) {
		  return toDocument( (char[]) input );
	  }
	  else if (input instanceof boolean[]) {
		  return toDocument( (boolean[]) input );
	  }
	  else if (input instanceof byte[]) {
		  return toDocument( (byte[]) input );
	  }
	  else if (input instanceof short[]) {
		  return toDocument( (short[]) input );
	  }
	  else if (input instanceof int[]) {
		  return toDocument( (int[]) input );
	  }
	  else if (input instanceof long[]) {
		  return toDocument( (long[]) input );
	  }
	  else if (input instanceof double[]) {
		  return toDocument( (double[]) input );
	  }
	  else if (input instanceof float[]) {
		  return toDocument( (float[]) input );
	  }
	  else if (input instanceof java.util.Properties) {
		  return toDocument( (java.util.Properties) input );
	  }
	  else if (input instanceof java.util.Hashtable) {
		  return toDocument( (java.util.Hashtable) input );
	  }
	  else if (input instanceof java.util.LinkedList) {
		  return toDocument( (java.util.LinkedList) input );
	  }
	  else if (input instanceof java.util.Vector) {
		  return toDocument( (java.util.Vector) input );
	  }
	  else if (input instanceof java.util.ArrayList) {
		  return toDocument( (java.util.ArrayList) input );
	  }
	  else if (input instanceof java.util.AbstractSequentialList) {
		  return toDocument( (java.util.AbstractSequentialList) input );
	  }
	  else if (input instanceof java.util.AbstractSet) {
		  return toDocument( (java.util.AbstractSet) input );
	  }
	  else if (input instanceof java.util.AbstractList) {
		  return toDocument( (java.util.AbstractList) input );
	  }
	  else if (input instanceof java.util.AbstractCollection) {
		  return toDocument( (java.util.AbstractCollection) input );
	  }
	  else if (input instanceof java.util.HashSet) {
		  return toDocument( (java.util.HashSet) input );
	  }
	  else if (input instanceof java.util.LinkedHashSet) {
		  return toDocument( (java.util.LinkedHashSet) input );
	  }
	  else if (input instanceof java.util.TreeSet) {
		  return toDocument( (java.util.TreeSet) input );
	  }
	  else if (input instanceof java.util.HashMap) {
		  return toDocument( (java.util.HashMap) input );
	  }
	  else if (input instanceof java.util.WeakHashMap) {
		  return toDocument( (java.util.WeakHashMap) input );
	  }
	  else if (input instanceof java.util.IdentityHashMap) {
		  return toDocument( (java.util.IdentityHashMap) input );
	  }
	  else if (input instanceof java.util.TreeMap) {
		  return toDocument( (java.util.TreeMap) input );
	  }
	  else if (input instanceof java.util.AbstractMap) {
		  return toDocument( (java.util.AbstractMap) input );
	  }
	  else if (input instanceof java.util.SortedSet) {
		  return toDocument( (java.util.SortedSet) input );
	  }
	  else if (input instanceof java.util.Set) {
		  return toDocument( (java.util.Set) input );
	  }
	  else if (input instanceof java.util.List) {
		  return toDocument( (java.util.List) input );
	  }
	  else if (input instanceof java.util.Collection) {
		  return toDocument( (java.util.Collection) input );
	  }
	  else if (input instanceof java.util.SortedMap) {
		  return toDocument( (java.util.SortedMap) input );
	  }
	  else if (input instanceof java.util.Map) {
		  return toDocument( (java.util.Map) input );
	  }
	  else {
		  //
		  // it is an undefined type
		  //
		  return toDocument( (java.lang.String) input );
	  }

  }

   /**
     * Convert document to string.
     * Does work very well with all types of XML
     * @param document the document object.
     * @return String.
     */
    public static String documentToString(org.w3c.dom.Document document)
    	throws Exception {

        String result = null;
        if(document != null) {
			OutputFormat format = new OutputFormat(document);
			StringWriter strWriter = new StringWriter();
			XMLSerializer xmlSerializer = new XMLSerializer(strWriter, format);
			xmlSerializer.serialize(document.getDocumentElement());
			result = strWriter.toString();
        }
        return result;
    }

   /**
     * Convert element to string.
     * Does work very well with all types of XML
     * @param element the Element object.
     * @return String.
     */
    public static String documentToString(org.w3c.dom.Element element)
    	throws Exception {

        String result = null;
        if(element != null) {
			OutputFormat format = new OutputFormat();
			StringWriter strWriter = new StringWriter();
			XMLSerializer xmlSerializer = new XMLSerializer(strWriter,format);
			xmlSerializer.serialize(element);
			result = strWriter.toString();
        }
        return result;
    }

    /**
     * Does not work very well with all types of XML
     */
	public static String document2String(Document document)
		throws Exception {

		//System.out.println("document2String(): begin...");

		if (document == null) {
			return null;
		}

		String result = null;
		StringWriter strWtr = new StringWriter();
		StreamResult strResult = new StreamResult(strWtr);
		TransformerFactory tfac = TransformerFactory.newInstance();
		Transformer trans = tfac.newTransformer();
		trans.transform(new DOMSource(document.getDocumentElement()), strResult);
		result = strResult.getWriter().toString();
		//System.out.println("document2String: result="+result);
		return result;
	}

	public static Element getRootElement(Document doc) {
		if (doc == null) {
			return null;
		}
		else {
			return (Element) doc.getFirstChild();
		}
	}

	public  static  org.jdom.Document toJDOM(org.w3c.dom.Document dom)
		throws Exception {
		if (dom == null) {
			return null;
		}
		else {
			org.jdom.input.DOMBuilder builder = new org.jdom.input.DOMBuilder();
			org.jdom.Document jdomDoc = builder.build(dom);
			return jdomDoc;
		}
	}

	public  static  org.jdom.Document toJDOM(String str)
		throws Exception {
		if ((str == null) || (str.length() == 0)) {
			return null;
		}
		else {
           java.io.InputStream stream = new java.io.StringBufferInputStream( str );
           org.jdom.Document jdomDoc = new org.jdom.input.SAXBuilder().build( stream );
           return jdomDoc;
		}
	}

	public static String xmlToString(org.jdom.Document doc) throws Exception {
		StringWriter writer = new StringWriter();
		org.jdom.output.XMLOutputter xmlOutputter
			= new org.jdom.output.XMLOutputter("", false);

		xmlOutputter.output(doc, writer);
		return writer.toString();
	}

	public static String xmlToString(org.jdom.Element ele) throws Exception {
		return xmlToString(ele, false);
	}

	public static String xmlToString(org.jdom.Element ele, boolean inner)
		throws Exception {
		StringWriter writer = new StringWriter();
		org.jdom.output.XMLOutputter xmlOutputter
			= new org.jdom.output.XMLOutputter("", false);

		if( inner == true ) {
			xmlOutputter.outputElementContent(ele, writer);
		}
		else {
			xmlOutputter.output(ele, writer);
		}
		return writer.toString();
	}


	/**
	 * return the element as an XML stream
	 */
	public static String xmlToString(org.w3c.dom.Element element)
		throws Exception {

		return documentToString(element);
	}

	/**
	 * return the element as an XML stream. If inner=true, then only inner
	 * components of element are returned. For example if element represents
	 * the following:
	 *
	 * 	<tag attr1="value1" attr2="value2">tag-value
	 *			<tag1>....</tag1>
	 *			...
	 *			<tagn>...</tagn>
	 * 	</tag>
	 *
	 * then the result will be:
	 *
	 * 			tag-value
	 *			<tag1>....</tag1>
	 *			...
	 *			<tagn>...</tagn>
	 *
	 */
	public static String xmlToString(org.w3c.dom.Element element, boolean inner)
		throws Exception {

		if(!inner) {
			return documentToString(element);
		}

		// here inner=true
		if (element == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		String nodeValue = element.getNodeValue();
		if (nodeValue != null) {
			buffer.append(nodeValue);
		}

		NodeList list = element.getChildNodes();
		if (list == null) {
			return buffer.toString();
		}

		int length = list.getLength();
		if (length < 1) {
			return buffer.toString();
		}

		for (int i=0; i < length; i++) {
			Node node = list.item(i);
			if (node != null) {
				buffer.append(DOM2Writer.nodeToString(node, true));
			}
		}

		return buffer.toString();
	}

	public static String transform(org.w3c.dom.Element element,
								   java.lang.String stylesheetFileName)
  		throws Exception {

		if (element == null) {
			return null;
		}

		if ((stylesheetFileName == null) ||
			(stylesheetFileName.length() == 0)) {
  			throw new Exception("Document Manager: style sheet filename can not be null/empty.");
		}

		String xmlString = documentToString(element);
    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

		File xsltFile = new File(stylesheetFileName);
    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		return transformToString(xml, xslt);
	}

	public static String transform(org.w3c.dom.Document doc,
								   java.lang.String stylesheetFileName)
  		throws Exception {

		if (doc == null) {
			return null;
		}

		if ((stylesheetFileName == null) ||
			(stylesheetFileName.length() == 0)) {
  			throw new Exception("Document Manager: style sheet filename can not be null/empty.");
		}

		String xmlString = documentToString(doc);
    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

		File xsltFile = new File(stylesheetFileName);
    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		return transformToString(xml, xslt);
	}

	public static String transform(org.jdom.Element ele,
									String stylesheetFileName)
  		throws Exception {

		String xmlString = xmlToString(ele);
    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

		File xsltFile = new File(stylesheetFileName);
    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		return transformToString(xml, xslt);
	}

	public static  String transform(org.jdom.Document jdom,
									String stylesheetFileName)
  		throws Exception {

		String xmlString = xmlToString(jdom);
    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

		File xsltFile = new File(stylesheetFileName);
    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		return transformToString(xml, xslt);
	}

	public static  String transformToString(String xmlString,
  									        String xsltString)
  		throws Exception {

    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xsltString));

		StringWriter writer = transform(xml, xslt);

		return writer.toString();
  }

	public static  StringWriter transform(String xmlString,
  									      String xsltString)
  		throws Exception {

    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xmlString));

    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(new StringReader(xsltString));

		return transform(xml, xslt);
  }

	public static  String transformToString(File xmlFile,
  									        File xsltFile)
  		throws Exception {

    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(xmlFile);

    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		StringWriter writer = transform(xml, xslt);

		return writer.toString();
  }

	public static  StringWriter transform(File xmlFile,
  									      File xsltFile)
  		throws Exception {

    	javax.xml.transform.Source xml =
        	new javax.xml.transform.stream.StreamSource(xmlFile);

    	javax.xml.transform.Source xslt =
        	new javax.xml.transform.stream.StreamSource(xsltFile);

		return transform(xml, xslt);
  }

	public static  StringWriter transform(javax.xml.transform.Source xml,
  									      javax.xml.transform.Source xslt)
  	throws Exception {

		// provide output medium
		StringWriter sw = new StringWriter();
    	javax.xml.transform.Result result =
    		new javax.xml.transform.stream.StreamResult(sw);

    	// create an instance of TransformerFactory
    	javax.xml.transform.TransformerFactory transFact =
        	javax.xml.transform.TransformerFactory.newInstance();

    	javax.xml.transform.Transformer trans =
        	transFact.newTransformer(xslt);

    	trans.transform(xml, result);

    	return sw;
  }

	public static  String transformToString(javax.xml.transform.Source xml,
  									        javax.xml.transform.Source xslt)
  	throws Exception {

		StringWriter writer = transform(xml, xslt);
    	return writer.toString();
  }
}