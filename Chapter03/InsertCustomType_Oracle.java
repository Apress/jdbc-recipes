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
 public class InsertCustomType_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "scott";
        String password = "tiger";
        Class.forName(driver);  // load Oracle driver
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        System.out.println("--- InsertCustomType_Oracle begin ---");
        if (args.length != 5) {
            System.out.println("usage: java InsertCustomType_Oracle "+
               "<id> <isbn> <tilte> <author> <edition>");
            System.exit(1);
        }

        String id = args[0];
        String isbn = args[1];
        String title = args[2];
        String author = args[3];
        int edition = Integer.parseInt(args[4]);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            String insert =
               "insert into book_table values(?, BOOK(?, ?, ?, ?))";
            pstmt = conn.prepareStatement(insert);
            pstmt.setString(1, id);
            pstmt.setString(2, isbn);
            pstmt.setString(3, title);
            pstmt.setString(4, author);
            pstmt.setInt(5, edition);
            pstmt.executeUpdate();
            System.out.println("--- InsertCustomType_Oracle end ---");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }
}