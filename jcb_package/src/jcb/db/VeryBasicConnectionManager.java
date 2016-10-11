package jcb.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class provides very basic connection management
 * for demonstration purposes only. In production environment,
 * you should replace these with production quality "connection
 * pool management" functionality (such as Apache's Excalibur).
 */
public class VeryBasicConnectionManager {

    static final String DB_VENDOR_MYSQL  = "mysql";
    static final String DB_VENDOR_ORACLE = "oracle";

    public static Connection getConnection_MySQL() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection_Oracle() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "scott";
        String password = "tiger";
        Class.forName(driver);  // load Oracle driver
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection(String dbVendor)
        throws Exception {
        if (dbVendor.equalsIgnoreCase(DB_VENDOR_MYSQL)) {
            return getConnection_MySQL();
        }
        else if (dbVendor.equalsIgnoreCase(DB_VENDOR_ORACLE)) {
            return getConnection_Oracle();
        }
        //else if (dbVendor.equalsIgnoreCase("XXX")) {
        //    return getConnection_XXX();
        //}
        else {
            throw new Exception("unknown db vendor");
        }
    }
}