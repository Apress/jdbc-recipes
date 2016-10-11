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
 public class DemoGetGeneratedKeys_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "mp";
        String password = "mp2";

        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("------DemoGetGeneratedKeys_Oracle begin---------");

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
            stmt.executeUpdate("insert into animals_table(id, name) values(ANIMAL_ID_SEQ.nextval, 'rabbit')");

            rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                 ResultSetMetaData rsMetaData = rs.getMetaData();
                 int columnCount = rsMetaData.getColumnCount();

                 for (int i = 1; i <= columnCount; i++) {
                     String key = rs.getString(i);
                     System.out.println("key " + i + " is " + key);
                 }
            }

            System.out.println("------DemoGetGeneratedKeys_Oracle end---------");
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