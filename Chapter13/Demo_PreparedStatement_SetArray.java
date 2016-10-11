import java.util.*;
import java.io.*;
import java.sql.*;

import oracle.sql.ArrayDescriptor;
import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class Demo_PreparedStatement_SetArray {

    public static void main(String[] args) {
        String dbVendor = args[0]; // { "mysql", "oracle" }
        Connection conn = null;
        PreparedStatement pstmt = null;
        java.sql.Array sqlArray = null;
        try {
            System.out.println("--Demo_PreparedStatement_SetArray begin--");

            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // For oracle you need an array descriptor specifying
            // the type of the array and a connection to the database
            // the first parameter must match with the SQL ARRAY type created
            ArrayDescriptor arrayDescriptor =
               ArrayDescriptor.createDescriptor("CHAR_ARRAY", conn);
            // then obtain an Array filled with the content below
            String[] content = { "v1", "v2", "v3", "v4" };
            sqlArray= new oracle.sql.ARRAY(arrayDescriptor, conn, content);

            // prepare query
            String query = "insert into CHAR_ARRAY_TABLE(id, array) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            // set input parameters to PreparedStatement object
            // the order of setting input paramerters is not important
            pstmt.setString(1, "id300");
            pstmt.setArray(2, sqlArray);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_SetArray end--");
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
