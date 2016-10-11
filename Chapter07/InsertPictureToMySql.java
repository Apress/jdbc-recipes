import java.io.*;
import java.sql.*;
import jcb.util.DatabaseUtil;

 /**
  *
  * @author Mahmoud Parsian
  * @email  admin@jdbccookbook.com
  *
  */
 public class InsertPictureToMySql {
   String INSERT_PICTURE =
     "insert into MyPictures(id, name, photo) values (?, ?, ?)";
   Connection conn = null;

   /**
    * constructor
    */
   public InsertPictureToMySql() throws SQLException {
       DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
       conn = DriverManager.getConnection(
           "jdbc:mysql://localhost/octopus", "root", "root");
   }
   public static void main(String[] args)
      throws Exception, IOException, SQLException {
      if ((args == null) || (args.length != 3)) {
         System.err.println("Usage: java InsertPictureToMySql <id> <name> <photo>");
         System.exit(0);
      }
      String id = DatabaseUtil.trimArgument(args[0]);
      String name = DatabaseUtil.trimArgument(args[1]);
      String photo = DatabaseUtil.trimArgument(args[2]);
      new InsertPictureToMySql().insert(id, name, photo);
   }
   public void insert(String id, String name, String photo)
      throws IOException, SQLException {
      FileInputStream fis  = null;
      PreparedStatement ps = null;
      try {
         // begin transaction
         conn.setAutoCommit(false);

         File file = new File(photo);
         fis = new FileInputStream(file);
         ps = conn.prepareStatement(INSERT_PICTURE);
         ps.setString(1, id);
         ps.setString(2, name);
         ps.setBinaryStream(3, fis,(int)file.length());
         ps.executeUpdate();

         // end transaction
         conn.commit();
      }
      finally {
         DatabaseUtil.close(ps);
         DatabaseUtil.close(fis);
      }
   }
   protected void finalize() throws Throwable {
      DatabaseUtil.close(conn);
      super.finalize();
   }
}
