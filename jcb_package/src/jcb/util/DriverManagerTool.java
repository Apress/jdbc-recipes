package jcb.util;

import java.util.*;
import java.io.*;
import java.sql.*;


/**
 * This class provides methods to support
 * JDBC driver-related functions.
 */
 public class DriverManagerTool {

	 /**
	  * Get a new database connection from a given driver, url,
	  * and properties (includer "user" and "password" properties).
	  * @param driver the JDBC driver.
	  * @param url a database URL.
	  * @param props the database properties.
	  * @return a new database connection object.
	  * @throws SQLException Failed to create a new database connection.
	  */
	  public static java.sql.Connection getConnection(java.sql.Driver driver,
	 												 String url,
	 												 Properties props)
	 	throws SQLException {

		return driver.connect(url, props);
	 }

	 public static java.sql.Driver getDriver(String driverClassName)
	 	throws InstantiationException,
	 	       ClassNotFoundException,
	 	       IllegalAccessException {

		    Class driverClass = Class.forName(driverClassName);
			java.sql.Driver driver = (java.sql.Driver) driverClass.newInstance();
			System.out.println("getDriver: driver is OK. driver=" + driver);
			return driver;
	}

     /**
      * Retrieves an XML with all of the currently loaded
      * JDBC drivers to which the current caller has access.
      * @return all loaded JDBC drivers as an XML (serialized
      * as a String object).
      */
      public static String getLoadedDrivers() {

		  java.util.Enumeration e = java.sql.DriverManager.getDrivers();
		  StringBuffer sb = new StringBuffer("<?xml version='1.0'>");
		  sb.append("<loaded_drivers>");
		  while (e.hasMoreElements()) {
         		Object driver = e.nextElement();
         		//System.out.println("JDBC Driver="+driver);
         		appendXMLTag(sb, "loadedDriver", driver.toString());
		  }
		  sb.append("</loaded_drivers>");
		  return sb.toString();
      }

      private static void appendXMLTag(StringBuffer buffer,
	                                   String tagName,
	                                   boolean value) {
			buffer.append("<");
			buffer.append(tagName);
			buffer.append(">");
			buffer.append(value);
			buffer.append("</");
			buffer.append(tagName);
			buffer.append(">");
	  }

	  private static void appendXMLTag(StringBuffer buffer,
	                                   String tagName,
	                                   int value) {
			buffer.append("<");
			buffer.append(tagName);
			buffer.append(">");
			buffer.append(value);
			buffer.append("</");
			buffer.append(tagName);
			buffer.append(">");
	  }

	  private static void appendXMLTag(StringBuffer buffer,
	                                   String tagName,
	                                   String value) {
			buffer.append("<");
			buffer.append(tagName);
			buffer.append(">");
			buffer.append(value);
			buffer.append("</");
			buffer.append(tagName);
			buffer.append(">");
	}

     /**
      *
      * Print out all loaded JDBC drivers.
      *
      */
      public static void main(String[] args) throws Exception {

		  try {
			  	// register two drivers
		  	  	//Class.forName("org.gjt.mm.mysql.Driver");
		  		//Class.forName("oracle.jdbc.driver.OracleDriver");

		  		// get loaded drivers
		  		//System.out.println(getLoadedDrivers());

		  		java.sql.Driver mysqlDriver = getDriver("org.gjt.mm.mysql.Driver");
				String url = "jdbc:mysql://localhost/empDB";
				String username = "root";
				String password = "root";
				java.util.Properties props = new java.util.Properties();
		  		props.put("user", username);
				props.put("password", password);
				java.sql.Connection conn = getConnection(mysqlDriver, url, props);
		  		System.out.println("conn="+conn);
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
      }
}
