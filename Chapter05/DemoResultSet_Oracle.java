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
 public class DemoResultSet_Oracle {

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
            System.out.println("------DemoResultSet_Oracle begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            //
            // prepare query
            //
            String query = "select id, name, age from employees";

            //
            // create a statement
            //
            stmt = conn.createStatement();


            //
            // execute query and return result as a ResultSet
            //
            rs = stmt.executeQuery(query);

            //
            // extract data from the ResultSet
            //
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                int age = rs.getInt(3);
                System.out.println("id="+id);
                System.out.println("name="+name);
                System.out.println("age="+age);
                System.out.println("---------------");
            }
            System.out.println("------DemoResultSet_Oracle end---------");
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