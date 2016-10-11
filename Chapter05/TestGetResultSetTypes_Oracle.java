import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.db.*;
import jcb.meta.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestGetResultSetTypes_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        //String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        //String username = "mp";
        //String password = "mp2";
        String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
        String username = "octopus";
        String password = "octopus";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {

        Connection conn = null;
        try {
            System.out.println("------TestGetResultSetTypes_Oracle begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            String resultSetTypes =
                DatabaseMetaDataTool.getAvailableResultSetTypes(conn);
            System.out.println(resultSetTypes);
            System.out.println("------TestGetResultSetTypes_Oracle end---------");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(conn);
        }
    }

}