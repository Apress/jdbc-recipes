import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class Demo_PreparedStatement_SetBinaryStream {

    public static void main(String[] args) {
        // set up input parameters from command line:
        String dbVendor = args[0]; // { "mysql", "oracle" }
        String id = args[1];
        String smallFileName = args[2];
        String largeFileName = args[3];
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Demo_PreparedStatement_setBinaryStream begin--");
            // get a database Connection object
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare small binary stream
            File smallFile = new File(smallFileName);
            int smallFileLength = (int) smallFile.length();
            InputStream smallStream = (InputStream) new FileInputStream(smallFile);

            // prepare large binary stream
            File largeFile = new File(largeFileName);
            int largeFileLength = (int) largeFile.length();
            InputStream largeStream = (InputStream) new FileInputStream(largeFile);

            // prepare SQL query
            String query = "insert into binary_table" +
               "(id, raw_column, long_raw_column) values(?, ?, ?)";

            // begin transaction
            conn.setAutoCommit(false);

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setBinaryStream(2, smallStream, smallFileLength);
            pstmt.setBinaryStream(3, largeStream, largeFileLength);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);

            // end transaction
            conn.commit();
            System.out.println("--Demo_PreparedStatement_setBinaryStream end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }
