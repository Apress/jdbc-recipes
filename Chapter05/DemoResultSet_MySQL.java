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
 public class DemoResultSet_MySQL {

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
            System.out.println("------DemoResultSet_MySQL begin---------");

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
            System.out.println("------DemoResultSet_MySQL end---------");
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