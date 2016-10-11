import java.sql.*;
import java.util.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestCreateConnectionWithProperties_MySQL {

    public static final String DATABASE_USER = "user";
    public static final String DATABASE_PASSWORD = "password";
    public static final String MYSQL_AUTO_RECONNECT = "autoReconnect";
    public static final String MYSQL_MAX_RECONNECTS = "maxReconnects";

    /**
     * Create MySQL Connection... which will live for a long time
     */
    public static Connection getConnection() throws Exception {

        String driver = "org.gjt.mm.mysql.Driver";
        // load the driver
        Class.forName(driver);
        String dbURL = "jdbc:mysql://localhost/tiger";
        String dbUsername = "root";
        String dbPassword = "root";

        //
        // these are properties that get passed
        // to DriverManager.getConnection(...)
        //
        java.util.Properties connProperties = new java.util.Properties();
        connProperties.put(DATABASE_USER, dbUsername);
        connProperties.put(DATABASE_PASSWORD, dbPassword);

        //
        // set additional connection properties:
        // if connection stales, then make automatically
        // reconnect; make it alive again;
        // if connection stales, then try for reconnection;
        //
        connProperties.put(MYSQL_AUTO_RECONNECT, "true");
        connProperties.put(MYSQL_MAX_RECONNECTS, "4");
        Connection conn = DriverManager.getConnection(dbURL, connProperties);
        return conn;
    }


    public static void main(String[] args) {

        Connection conn = null;
        try {
            System.out.println("-- TestCreateConnection_MySQL begin --");

            //
            // get connection to an Oracle database
            //
            conn = getConnection();
            System.out.println("conn="+conn);

            // use connection ...
            System.out.println("-- TestCreateConnection_MySQL end --");
        }
        catch(Exception e){
            // handle the exception
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            //
            // release database resources
            //
            try { conn.close(); } catch(Exception ignore) { }
        }
    }
}