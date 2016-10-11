import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * This class provides the following features:
 *   1) how to serialize a Java object to the MySQL database.
 *   2) how to de-serialize a Java object from the MySQL database.
 *
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
class SerializeJavaObjects_MySQL {

    static final String WRITE_OBJECT_SQL =
        "INSERT INTO java_objects(object_name, object_value) VALUES (?, ?)";
    static final String READ_OBJECT_SQL =
        "SELECT object_value FROM java_objects WHERE object_id = ?";

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        //String url = "jdbc:mysql://localhost/tiger";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * This method writes a Java object to an MySQL database (serialization).
     */
    public static long writeJavaObject(Connection conn, Object object)
        throws Exception {

        String className = object.getClass().getName();
        PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL);

        // set input parameters
        pstmt.setString(1, className);
        pstmt.setObject(2, object);
        pstmt.executeUpdate();

        // get the generated key for the object_id
        ResultSet rs = pstmt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        close(rs);
        close(pstmt);
        System.out.println("writeJavaObject: done serializing: " + className);
        return id;
    }

    public static Object readJavaObject(Connection conn, long id)
        throws Exception {

        PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        Object object = rs.getObject(1);
        String className = object.getClass().getName();

        close(rs);
        close(pstmt);
        System.out.println("readJavaObject: done de-serializing: " + className);
        return object;
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


mysql>
    CREATE TABLE java_objects (
    object_id INT AUTO_INCREMENT,
    object_name varchar(128),
    object_value BLOB,
    primary key (object_id));

Query OK, 0 rows affected (0.09 sec)

mysql> desc java_objects;
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| object_id    | int(11)      |      | PRI | NULL    | auto_increment |
| object_name  | varchar(128) | YES  |     | NULL    |                |
| object_value | blob         | YES  |     | NULL    |                |
+--------------+--------------+------+-----+---------+----------------+
3 rows in set (0.04 sec)

mysql>


<< java program execution>>
C:\zmp\book\chapters>javac SerializeJavaObjects_MySQL.java

C:\zmp\book\chapters>java SerializeJavaObjects_MySQL
conn=com.mysql.jdbc.Connection@cd2c3c
[Before Serialization] list=[This is a short string., 1234, Sun Oct 12 21:16:20 PDT 2003]
writeJavaObject: done serializing: java.util.ArrayList
Serialized objectID => 1
readJavaObject: done de-serializing: java.util.ArrayList
[After De-Serialization] list=[This is a short string., 1234, Sun Oct 12 21:16:20 PDT 2003]

C:\zmp\book\chapters>java SerializeJavaObjects_MySQL
conn=com.mysql.jdbc.Connection@cd2c3c
[Before Serialization] list=[This is a short string., 1234, Sun Oct 12 21:16:30 PDT 2003]
writeJavaObject: done serializing: java.util.ArrayList
Serialized objectID => 2
readJavaObject: done de-serializing: java.util.ArrayList
[After De-Serialization] list=[This is a short string., 1234, Sun Oct 12 21:16:30 PDT 2003]

C:\zmp\book\chapters>
<< java program execution>>

SQL> select object_id, object_name from java_objects;

mysql> select object_id, object_name from java_objects;
+-----------+---------------------+
| object_id | object_name         |
+-----------+---------------------+
|         1 | java.util.ArrayList |
|         2 | java.util.ArrayList |
+-----------+---------------------+
2 rows in set (0.02 sec)

mysql>
****/