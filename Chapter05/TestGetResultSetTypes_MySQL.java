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
 public class TestGetResultSetTypes_MySQL {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        //String url = "jdbc:mysql://localhost/octopus";
        String url = "jdbc:mysql://localhost/tiger";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {

        Connection conn = null;
        try {
            System.out.println("------TestGetResultSetTypes_MySQL begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            String resultSetTypes =
                DatabaseMetaDataTool.getAvailableResultSetTypes(conn);
            System.out.println(resultSetTypes);
            System.out.println("------TestGetResultSetTypes_MySQL end---------");
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