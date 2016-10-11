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
 public class TestAccessExcel {

    public static Connection getConnection() throws Exception {
        String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String url = "jdbc:odbc:excelDB";
        String username = "";
        String password = "";
        Class.forName(driver);  // load JDBC-ODBC driver
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String args[]) {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            String excelQuery = "select * from [Sheet1$]";
            rs=stmt.executeQuery(excelQuery);

            while(rs.next()){
                System.out.println(rs.getString("BadgeNumber")+
                  " "+ rs.getString("FirstName")+" "+
                  rs.getString("LastName"));
            }
        }
        catch (Exception e){
            // handle the exception
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
 }