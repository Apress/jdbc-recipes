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
 public class InsertCustomType2_Oracle {
   public static Connection getConnection() throws Exception {
      String driver = "oracle.jdbc.driver.OracleDriver";
      String url = "jdbc:oracle:thin:@localhost:1521:caspian";
      String username = "scott";
      String password = "tiger";
      Class.forName(driver);    // load Oracle driver
      return DriverManager.getConnection(url, username, password);
   }

   public static void main(String[] args) {
      System.out.println("--- InsertCustomType2_Oracle begin ---");
      if (args.length != 5) {
         System.out.println("usage: java InsertCustomType2_Oracle "+
            "<id> <isbn> <title> <author> <edition>");
         System.exit(1);
      }

      String id = args[0];
      String isbn = args[1];
      String title = args[2];
      String author = args[3];
      int edition = Integer.parseInt(args[4]);

      // create the Book object
      Book book = new Book(isbn, title, author, edition);
      book.print();

      Connection conn = null;
      PreparedStatement pstmt = null;
      try {
         conn = getConnection();
         // create type map
         java.util.Map map = conn.getTypeMap();
         System.out.println("map="+map);
         map.put("SCOTT.BOOK", Class.forName("Book"));
         System.out.println("map="+map);

         String insert =
           "insert into book_table(ID, BOOK_OBJECT) values(?, ?)";
         pstmt = conn.prepareStatement(insert);
         pstmt.setString(1, id);
         pstmt.setObject(2, book);
         pstmt.executeUpdate();
         System.out.println("--- InsertCustomType2_Oracle end ---");
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