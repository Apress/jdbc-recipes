import java.io.*;
import java.sql.*;
import java.text.*;
import jcb.util.DatabaseUtil;

// add these imports for access to the required Oracle classes
import oracle.jdbc.driver.*;
import oracle.sql.BLOB;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class InsertPictureToOracle {
   Connection conn;

   public InsertPictureToOracle() throws SQLException {
       DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
       conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@mparsian:1521:scorpian", "octopus", "octopus");
   }

  public static void main(String[] args)
     throws Exception, IOException, SQLException {
     if ((args == null) || (args.length != 3)) {
        System.err.println("Usage: java InsertPictureToOracle <id> <name> <photo>");
        System.exit(0);
     }
     String id = DatabaseUtil.trimArgument(args[0]);
     String name = DatabaseUtil.trimArgument(args[1]);
     String photo = DatabaseUtil.trimArgument(args[2]);
     new InsertPictureToOracle().insert(id, name, photo);
   }
   public void insert(String id, String name, String photo)
      throws Exception, IOException, SQLException {
      int             rows       = 0;
      FileInputStream fin        = null;
      OutputStream    out        = null;
      ResultSet       result     = null;
      Statement       stmt       = null;
      oracle.sql.BLOB oracleBlob = null;

      try {
         conn.setAutoCommit(false);
         stmt = conn.createStatement();
         result = stmt.executeQuery("select id from MyPictures where id  = "+ id);
         while (result.next()) {
            rows++;
         }

         if (rows > 1) {
            System.err.println("Too many rows!");
            System.exit(1);
         }

         result.close();
         result = null;
         if (rows == 0) {
            System.out.println("This creates the LOB locators");
            rows = stmt.executeUpdate("insert into MyPictures "+
              "(id, name, photo ) values ("+id+", '"+ name +"', empty_blob() )");
            System.out.println(rows + " rows inserted");
            // Now retrieve the locator
            rows = 0;
            result = stmt.executeQuery("select photo from  MyPictures "+
                      "where  id = "+id+ " for update nowait");
            result.next();
            oracleBlob = ((OracleResultSet)result).getBLOB(1);
            result.close();
            result = null;
         }
         stmt.close();
         stmt = null;
         // Now that you have the locator, store the photo
         File binaryFile = new File(photo);
         fin = new FileInputStream(binaryFile);
         out = oracleBlob.getBinaryOutputStream();
         // Get the optimal buffer size from the BLOB
         byte[] buffer = new byte[oracleBlob.getBufferSize()];
         int length = 0;
         while ((length = fin.read(buffer)) != -1) {
            out.write(buffer, 0, length);
         }
         // You have to close the output stream before
         // you commit, or the changes are lost!
         out.close();
         out = null;
         fin.close();
         fin = null;
         conn.commit();
      }
      finally {
         DatabaseUti.close(result);
         DatabaseUti.close(stmt);
         DatabaseUti.close(out);
         DatabaseUti.close(fin);
      }
   }
   protected void finalize() throws Throwable {
       DatabaseUti.close(conn);
       super.finalize();
   }
}
