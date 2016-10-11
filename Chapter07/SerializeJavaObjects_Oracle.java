import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
 * The following class provides:
 *      1) how to serialize a Java object to the Oracle database.
 *      2) how to de-serialize a Java object from the Oracle database.
 *
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
class SerializeJavaObjects_Oracle {

    static final String GET_JAVA_OBJECT_SEQUENCE = "SELECT java_object_sequence.nextval FROM dual";
    static final String WRITE_OBJECT_SQL = "BEGIN " +
                                         "  INSERT INTO java_objects(object_id, object_name, object_value) " +
                                         "  VALUES (?, ?, empty_blob()) " +
                                         "  RETURN object_value INTO ?; " +
                                         "END;";
    static final String READ_OBJECT_SQL     = "SELECT object_value FROM java_objects WHERE object_id = ?";

    /**
     * Create a connection object.
     */
     public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "mp";
        String password = "mp2";
        //String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
        //String username = "octopus";
        //String password = "octopus";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }


    /**
     * This method writes a Java object to an Oracle database (serialization).
     */
    public static long writeJavaObject(Connection conn, Object object)
        throws Exception {

        long id = getNextSequenceValue(conn);
        String className = object.getClass().getName();
        CallableStatement cstmt = conn.prepareCall(WRITE_OBJECT_SQL);

        // set and register input parameters
        cstmt.setLong(1, id);
        cstmt.setString(2, className);

        // register output parameters
        cstmt.registerOutParameter(3, java.sql.Types.BLOB);

        cstmt.executeUpdate();
        BLOB blob = (BLOB) cstmt.getBlob(3);
        OutputStream os = blob.getBinaryOutputStream();
        ObjectOutputStream oop = new ObjectOutputStream(os);
        oop.writeObject(object);
        oop.flush();
        oop.close();
        os.close();
        close(cstmt);
        System.out.println("writeJavaObject: done serializing: " + className);
        return id;
    }


    /**
     * This method reads a Java object from an Oracle database (de-serialization).
     */
    public static Object readJavaObject(Connection conn, long id)
        throws Exception {

        PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        InputStream is = rs.getBlob(1).getBinaryStream();
        ObjectInputStream oip = new ObjectInputStream(is);
        Object object = oip.readObject();
        String className = object.getClass().getName();
        oip.close();
        is.close();
        close(rs);
        close(pstmt);
        System.out.println("readJavaObject: done de-serializing: " + className);
        return object;
    }


    /**
     * Create a Primary key id for Java objects
     *
     */
    private static long getNextSequenceValue (Connection conn)
        throws SQLException {

        Statement stmt = conn.createStatement();
        ResultSet rs   = stmt.executeQuery(GET_JAVA_OBJECT_SEQUENCE);
        rs.next();
        long id = rs.getLong(1);
        close(rs);
        close(stmt);
        return id;
    }

    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) {
                // ignore
            }
        }
    }

    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                // ignore
            }
        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException e) {
                // ignore
            }
        }
    }

    private static void close(CallableStatement cstmt) {
        if (cstmt != null) {
            try {
                cstmt.close();
            }
            catch (SQLException e) {
                // ignore
            }
        }
    }

    private static void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            }
            catch (SQLException e) {
                // ignore
            }
        }
    }

    private static List buildList() {
        List list = new ArrayList();
        list.add("This is a short string.");
        list.add(new Integer(1234));
        list.add(new java.util.Date());
        return list;
    }

    /**
     * This is the driver method (for testing purposes).
     */
    public static void main (String args[]) {

        Connection conn = null;
        try {
            // connect to the database
            conn = getConnection();
            System.out.println("conn="+conn);

            // turn off AutoCommit
            conn.setAutoCommit(false);

            List list = buildList();
            System.out.println("[Before Serialization] list="+list);

            // serialize list (as a java object)
            long objectID = writeJavaObject(conn, list);

            // commit the transaction
            conn.commit();

            System.out.println("Serialized objectID => " + objectID);
            // de-serialize list a java object from a given objectID
            List listFromDatabase = (List) readJavaObject(conn, objectID);
            System.out.println("[After De-Serialization] list=" + listFromDatabase);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            close(conn);
        }
    }
}


/****
C:\zmp\book\chapters>sqlplus octopus/octopus

SQL*Plus: Release 9.2.0.1.0 - Production on Sat Oct 11 23:14:59 2003

Copyright (c) 1982, 2002, Oracle Corporation.  All rights reserved.


Connected to:
Oracle9i Enterprise Edition Release 9.2.0.1.0 - Production
With the Partitioning, OLAP and Oracle Data Mining options
JServer Release 9.2.0.1.0 - Production



SQL> CREATE SEQUENCE java_object_sequence  INCREMENT BY 1 START WITH 1  NOMAXVALUE  NOCYCLE
  2  ;

Sequence created.

SQL> CREATE TABLE java_objects (object_id NUMBER, object_name varchar(128), object_value BLOB DEFAULT empty_blob(), primary key (object_id));

Table created.

SQL> desc table java_objects;
Usage: DESCRIBE [schema.]object[@db_link]
SQL> desc java_objects;
 Name                                      Null?    Type
 ----------------------------------------- -------- ----------------------------
 OBJECT_ID                                 NOT NULL NUMBER
 OBJECT_NAME                                        VARCHAR2(128)
 OBJECT_VALUE                                       BLOB

SQL> select SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, INCREMENT_BY, LAST_NUMBER from  user_sequences;

SEQUENCE_NAME                   MIN_VALUE  MAX_VALUE INCREMENT_BY LAST_NUMBER
------------------------------ ---------- ---------- ------------ -----------
ID_SEQ                                  1 1.0000E+27            1          21
JAVA_OBJECT_SEQUENCE                    1 1.0000E+27            1           1

SQL> commit;

Commit complete.


<< java program execution>>
C:\zmp\book\chapters>java SerializeJavaObjects_Oracle
conn=oracle.jdbc.driver.OracleConnection@6e70c7
[Before Serialization] list=[This is a short string., 1234, Sun Oct 12 20:35:54 PDT 2003]
writeJavaObject: done serializing: java.util.ArrayList
Serialized objectID => 1
readJavaObject: done de-serializing: java.util.ArrayList
[After De-Serialization] list=[This is a short string., 1234, Sun Oct 12 20:35:54 PDT 2003]

C:\zmp\book\chapters>java SerializeJavaObjects_Oracle
conn=oracle.jdbc.driver.OracleConnection@6e70c7
[Before Serialization] list=[This is a short string., 1234, Sun Oct 12 20:35:59 PDT 2003]
writeJavaObject: done serializing: java.util.ArrayList
Serialized objectID => 2
readJavaObject: done de-serializing: java.util.ArrayList
[After De-Serialization] list=[This is a short string., 1234, Sun Oct 12 20:35:59 PDT 2003]

C:\zmp\book\chapters>
<< java program execution>>

SQL> select object_id, object_name from java_objects;

OBJECT_ID   OBJECT_NAME
---------   --------------------
        1   java.util.ArrayList
        2   java.util.ArrayList


****/