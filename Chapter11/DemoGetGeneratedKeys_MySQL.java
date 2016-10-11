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
 public class DemoGetGeneratedKeys_MySQL {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";

        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("------DemoGetGeneratedKeys_MySQL begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            //
            // create a statement
            //
            stmt = conn.createStatement();


            //
            // insert a  record into the animals_table
            //
            stmt.executeUpdate("insert into animals_table (name) values('tiger11')");

            rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                 ResultSetMetaData rsMetaData = rs.getMetaData();
                 int columnCount = rsMetaData.getColumnCount();

                 for (int i = 1; i <= columnCount; i++) {
                     String key = rs.getString(i);
                     System.out.println("key " + i + " is " + key);
                 }
            }

            System.out.println("------DemoGetGeneratedKeys_MySQL end---------");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
}