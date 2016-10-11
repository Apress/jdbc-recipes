              import java.util.*;
              import java.io.*;
              import java.sql.*;

              import jcb.db.VeryBasicConnectionManager;
              import jcb.util.DatabaseUtil;

              /**
               *
               * @author Mahmoud Parsian
               * @email  admin@jdbccookbook.com
               *
               */
               public class GetGeneratedKeys {
                  public static void main(String[] args) {
                      ResultSet rs = null;
                      Connection conn = null;
                      PreparedStatement pstmt = null;
                      try {
                          System.out.println("--GetGeneratedKeys begin--");
                          String dbVendor = args[0]; // database vendor
                          String name = args[1];     // animal name
                          conn = VeryBasicConnectionManager.getConnection(dbVendor);
                          System.out.println("conn="+conn);

                          // insert a  record into the animals_table using PreparedStatement
                          // note that the SQL INSERT is different for each vendor
                          String insert = null;
                          if (dbVendor.equalsIgnoreCase("mysql")) {
                              insert = "insert into animals_table(name) values(?)";
                          }
                          else if (dbVendor.equalsIgnoreCase("oracle")) {
                              insert = "insert into animals_table(id, name) "+
                                       "values(ANIMAL_ID_SEQ.nextval, ?)";
                          }

                          pstmt = conn.prepareStatement(insert);  // create a PreparedStatement
                          pstmt.setString(1, name);               // set input values
                          pstmt.executeUpdate();                  // insert the record

                         if (dbVendor.equalsIgnoreCase("mysql")) {
                             rs = stmt.getGeneratedKeys();
                          }
                          else if (dbVendor.equalsIgnoreCase("oracle")) {
                             rs = stmt.executeQuery("select ANIMAL_ID_SEQ.currval from dual");
                          }

                          while (rs.next()) {
                               ResultSetMetaData rsMetaData = rs.getMetaData();
                               int columnCount = rsMetaData.getColumnCount();
                               for (int i = 1; i <= columnCount; i++) {
                                   String key = rs.getString(i);
                                   System.out.println("key " + i + " is " + key);
                               }
                          }
                          System.out.println("--GetGeneratedKeys end--");
                      }
                      catch(Exception e){
                          e.printStackTrace();
                          System.exit(1);
                      }
                      finally {
                          // release database resources
                          DatabaseUtil.close(rs);
                          DatabaseUtil.close(pstmt);
                          DatabaseUtil.close(conn);
                      }
                  }
              }
