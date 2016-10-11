import java.io.*;
import java.sql.*;
import java.text.*;

import jcb.db.*;
import jcb.meta.*;

import oracle.jdbc.driver.*;
import oracle.sql.BLOB;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class InsertBLOB_Oracle {
   Connection conn;

   public InsertBLOB_Oracle() throws Exception {
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      conn = DriverManager.getConnection(
       "jdbc:oracle:thin:@localhost:1521:caspian", "mp", "mp2");
   }

   public static void main(String[] args)
      throws Exception {
      if (args.length != 2) {
         System.out.println("usage: java InsertBLOB_Oracle <id> <binary-file>");
         System.exit(0);
      }

      String id = args[0].trim();
      String binaryFileName = args[1].trim();
      new InsertBLOB_Oracle().process(id, binaryFileName);
   }

  public void process(String id, String binaryFileName)
     throws Exception {
     int             rows      = 0;
     FileInputStream fin       = null;
     OutputStream    out       = null;
     ResultSet       rs        = null;
     Statement       stmt      = null;
     oracle.sql.BLOB photo     = null;

     try {
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
        rows = stmt.executeUpdate("insert into my_pictures"+
          "(id, photo ) values ('"+id+"', empty_blob() )");
        System.out.println(rows + " rows inserted");

        // now retrieve the BLOB locator
        rs = stmt.executeQuery("select photo from  "+
          "my_pictures where id = '"+id+ "' for update nowait");
        rs.next();
        photo = ((OracleResultSet)rs).getBLOB(1);

        // Now, we have the BLOB locator, store the photo
        File binaryFile = new File(binaryFileName);
        fin = new FileInputStream(binaryFile);
        out = photo.getBinaryOutputStream();
        // Get the optimal buffer size from the BLOB
        byte[] buffer = new byte[photo.getBufferSize()];
        int length = 0;
        while ((length = fin.read(buffer)) != -1) {
          out.write(buffer, 0, length);
        }

        // you've got to close the output stream before
        // you commit, or the changes are lost!
        out.close();
        fin.close();
        conn.commit();
    }
    finally {
      DatabaseUtil.close(rs);
      DatabaseUtil.close(stmt);
    }
  }

   protected void finalize() throws Throwable {
      DatabaseUtil.close(conn);
      super.finalize();
   }
}