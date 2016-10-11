package jcb.util;


import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.ext.LexicalHandler;

import org.w3c.dom.Document;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLValidator extends DefaultHandler
    implements LexicalHandler {

    boolean errorOnly = false; //display error if false;
    Document doc = null;

    public XMLValidator(String xmlFilename, boolean _errorOnly, boolean validate) {

        errorOnly = _errorOnly;

        // Use the validating parser
        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setValidating(validate);
        //factory.setNamespaceAware(true);
        try {
            // Set up output stream
            out = new OutputStreamWriter(System.out, "UTF8");

            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            saxParser.parse( new File(xmlFilename), this /*handler*/);

            // Step 1: create a DocumentBuilderFactory and configure it
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            doc = dbf.newDocumentBuilder().parse(new File(xmlFilename));

        }
        catch (SAXParseException spe) {
            // Error generated by the parser
            System.out.println("\n** Parsing error"
                + ", line " + spe.getLineNumber()
                + ", uri " + spe.getSystemId());
            System.out.println("   " + spe.getMessage() );

            // Use the contained exception, if any
            Exception  x = spe;

            if (spe.getException() != null)
                x = spe.getException();
            x.printStackTrace();

        }
        catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception  x = sxe;

            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();

        }
        catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();

        }
        catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
    }
    public XMLValidator(String xmlFilename, boolean _errorOnly) {
        this(xmlFilename, _errorOnly, true);
    }

    public Document getDocument() {
        return doc;
    }

    public static void main(String argv[])
    {
        if (argv.length < 1) {
            System.err.println("Usage: java XMLVerifier filename [errorOnly] ");
            System.err.println("   errorOnly = [true|false]");
            System.exit(1);
        }

        try {
            new XMLValidator(argv[0], argv[1].equalsIgnoreCase("true"));
        }
        catch (Exception e) {
            new XMLValidator(argv[0], false);
        }

        System.exit(0);
    }

    static private Writer  out;
    private String indentString = "    "; // Amount to indent
    private int indentLevel = 0;

    private void print(String msg) throws IOException {
        if (!errorOnly) {
            out.write(msg);
        }
    }

    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    public void setDocumentLocator(Locator l)
    {
        // Save this to resolve relative URIs or to give diagnostics.
        try {
            print("LOCATOR");
            print("\n SYS ID: " + l.getSystemId() );
            out.flush();
        }
        catch (IOException e) {
        // Ignore errors
        }
    }

    public void startDocument()
    throws SAXException
    {
        nl();
        nl();
        emit("START DOCUMENT");
        nl();
        emit("<?xml version='1.0'?>");
    }

    public void endDocument()
    throws SAXException
    {
        nl();
        emit("END DOCUMENT");
        try {
            nl();
            out.flush();
        }
        catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startElement(String namespaceURI,
        String lName, // local name
        String qName, // qualified name
        Attributes attrs)
    throws SAXException
    {
        indentLevel++;
        nl();
        emit("ELEMENT: ");
        String eName = lName; // element name

        if ("".equals(eName)) eName = qName; // namespaceAware = false
        emit("<" + eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name

                if ("".equals(aName)) aName = attrs.getQName(i);
                nl();
                emit("   ATTR: ");
                emit(aName);
                emit("\t\"");
                emit(attrs.getValue(i));
                emit("\"");
            }
        }
        if (attrs.getLength() > 0) nl();
        emit(">");
    }

    public void endElement(String namespaceURI,
        String sName, // simple name
        String qName  // qualified name
    )
    throws SAXException
    {
        nl();
        emit("END_ELM: ");
        emit("</" + sName + ">");
        indentLevel--;
    }

    public void characters(char buf[], int offset, int len)
    throws SAXException
    {
        nl();
        emit("CHARS:   ");
        String s = new String(buf, offset, len);

        if (!s.trim().equals("")) emit(s);
    }

    public void ignorableWhitespace(char buf[], int offset, int len)
    throws SAXException
    {
    // Ignore it
    }

    public void processingInstruction(String target, String data)
    throws SAXException
    {
        nl();
        emit("PROCESS: ");
        emit("<?" + target + " " + data + "?>");
    }

    //===========================================================
    // SAX ErrorHandler methods
    //===========================================================

    // treat validation errors as fatal
    public void error(SAXParseException e)
    throws SAXParseException
    {
        throw e;
    }

    // dump warnings too
    public void warning(SAXParseException err)
    throws SAXParseException
    {
        System.out.println("** Warning"
            + ", line " + err.getLineNumber()
            + ", uri " + err.getSystemId());
        System.out.println("   " + err.getMessage());
    }

    //===========================================================
    // LexicalEventListener methods
    //===========================================================

    public void comment(char[] ch, int start, int length)
    throws SAXException
    {
    }

    public void startCDATA()
    throws SAXException
    {
        nl();
        emit("START CDATA SECTION");
    }

    public void endCDATA()
    throws SAXException
    {
        nl();
        emit("END CDATA SECTION");
    }

    public void startEntity(java.lang.String name)
    throws SAXException
    {
        nl();
        emit("START ENTITY: " + name);
    }

    public void endEntity(java.lang.String name)
    throws SAXException
    {
        nl();
        emit("END ENTITY: " + name);
    }

    public void startDTD(String name, String publicId, String systemId)
    throws SAXException
    {
        nl();
        emit("START DTD: " + name
            + "\n           publicId=" + publicId
            + "\n           systemId=" + systemId);
    }

    public void endDTD()
    throws SAXException
    {
        nl();
        emit("END DTD");
    }

    //===========================================================
    // Utility Methods ...
    //===========================================================

    // Wrap I/O exceptions in SAX exceptions, to
    // suit handler signature requirements
    private void emit(String s)
    throws SAXException
    {
        try {
            print(s);
            out.flush();
        }
        catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    // Start a new line
    // and indent the next line appropriately
    private void nl()
    throws SAXException
    {
        String lineEnd =  System.getProperty("line.separator");

        try {
            print(lineEnd);
            for (int i = 0; i < indentLevel; i++) print(indentString);
        }
        catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }
}
