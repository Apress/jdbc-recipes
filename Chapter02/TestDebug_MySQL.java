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
public class TestDebug_MySQL {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

    public static int countRows(Connection conn, String tableName)
        throws SQLException {
        // select the number of rows in the table
        Statement stmt = null;
        ResultSet rs = null;
        int rowCount = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM "+ tableName);
            // get the number of rows from the result set
            rs.next();
            rowCount = rs.getInt(1);
        }
        finally {
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
        }

        return rowCount;
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            System.out.println("------TestDebug_MySQL begin---------");
            PrintWriter pw = new PrintWriter(new FileOutputStream("mysql_debug.txt"));
            DriverManager.setLogWriter(pw);
            conn = getConnection();
            String tableName = args[0];
            System.out.println("tableName="+tableName);
            System.out.println("conn="+conn);
            System.out.println("rowCount="+countRows(conn, tableName));
            System.out.println("------TestDebug_MySQL end---------");
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