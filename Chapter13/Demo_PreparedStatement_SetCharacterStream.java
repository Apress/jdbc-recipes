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
 public class Demo_PreparedStatement_SetCharacterStream {

    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetCharacterStream begin--");
         // read inputs from command line
        String dbVendor = args[0];
        String fileName = args[1];
        Reader fileReader = null;
        long fileLength = 0;
        try {
            File file = new File(fileName);
            fileLength = file.length();
            fileReader = (Reader) new BufferedReader(new FileReader(file));
            System.out.println("fileName="+fileName);
            System.out.println("fileLength="+fileLength);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // get a database connection object
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // begin transaction
            conn.setAutoCommit(false);

            // prepare SQL query for inserting a new row using SetCharacterStream()
            String query = "insert into char_stream_table" +
                 " (id, char_stream_column) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, fileName);
            pstmt.setCharacterStream(2, fileReader, (int)fileLength);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);

            // end transaction
            conn.commit();
            System.out.println("--Demo_PreparedStatement_SetCharacterStream end--");
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
}
