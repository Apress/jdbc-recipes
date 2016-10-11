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
 public class Demo_Statement_GetObjectValues_Oracle {

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

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("------Demo_Statement_GetObjectValues_Oracle begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // create Statement object
            stmt = conn.createStatement();

            // Select rows from the employee table
            // note that emp is an EMPLOYEE_TYPE object
            rs = stmt.executeQuery("SELECT emp, age FROM employee");


            // Get the OBJECT values from each row
             while (rs.next()) {

                // Get the EMPLOYEE_TYPE value from the first column emp
                oracle.sql.STRUCT emp = (oracle.sql.STRUCT) rs.getObject(1);

                // Get the emp values from each row
                Object[] empValues = emp.getAttributes();

                // Get the  values of emp
                String name = (String) empValues[0];
                java.math.BigDecimal badgeNumber = (java.math.BigDecimal) empValues[1];

                // Get the age from the second column employee of the row
                int age = rs.getInt(2);

                System.out.println("name="+name);
                System.out.println("badgeNumber="+badgeNumber);
                System.out.println("age="+age);
                System.out.println("----------------------");
            }
        }
        catch (Exception e) {
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