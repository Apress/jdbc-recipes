/**
 * File        : SimpleProgramToAccessMySQLDatabase.java
 * Author      : Mahmoud Parsian
 * Date        : February 22, 2004
 * Description : Simple program to illustrate JDBC concepts
 */

import java.sql.*;

public class SimpleProgramToAccessMySQLDatabase {
    public static void main(String[] args)
        throws SQLException {

        String name = null;   // name of a cat
        String trick = null;  // trick of a cat
        Connection conn = null ; // database connection object

        try {
        	String driver = "com.mysql.jdbc.Driver";

        	// load the MySQL JDBC Driver
            Class.forName(driver);

			// define database connection parameters
			String databaseURL = "jdbc:mysql://localhost/tiger";
			String databaseUser = "root";
			String databasePassword = "root";

			// get a database connection object: A connection (session) with a
			// specific database. SQL statements are executed and results are
			// returned within the context of a connection.
            conn = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);

            // create a statement: This object will be used for executing
            // a static SQL statement and returning the results it produces.
            Statement stmt = conn.createStatement();

			// start a transaction
            conn.setAutoCommit(false);

            // create a table called cats_tricks
            stmt.executeUpdate("CREATE TABLE cats_tricks " +
                "(name VARCHAR(30), trick VARCHAR(30))");

            // insert two new records to the cats_tricks table
            stmt.executeUpdate("INSERT INTO cats_tricks VALUES('mono', 'rollover')" );
            stmt.executeUpdate("INSERT INTO cats_tricks VALUES('mono', 'jump')" );

            // commit the transaction
            conn.commit();

            // set auto commit to true (from now on every single
            // statement will be treated as a single transaction
            conn.setAutoCommit(true) ;

			// get all of the the records from the cats_tricks table
            ResultSet rs = stmt.executeQuery("SELECT name, trick FROM cats_tricks");

            // iterate the result set and get one row at a time
            while( rs.next() ) {
                 name = rs.getString(1); // name is the 1st column in the query
                 trick = rs.getString(2); // trick is the 2nd column in the query
                 System.out.println("name="+name);
                 System.out.println("trick="+trick);
                 System.out.println("==========");
            }
       }
       catch (ClassNotFoundException e){
       		// if the driver class not found, then we will be here
            System.out.println(e);
       }
       catch (SQLException e){
       		// something went wrong, we are handling the exception here
            if ( conn != null ){
                conn.rollback();
                conn.setAutoCommit(true);
            }

            System.out.println("SQLException caught");
            System.out.println("---");
            // iterate and get all of the errors as much as possible.
            while ( e != null ){
                System.out.println("Message   : " + e.getMessage());
                System.out.println("SQLState  : " + e.getSQLState());
                System.out.println("ErrorCode : " + e.getErrorCode());
                System.out.println("---");
                e = e.getNextException();
            }
       }
   }
}
