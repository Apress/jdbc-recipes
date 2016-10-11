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
 public class Select_Records_Using_PreparedStatement {

    public static void main(String[] args) {
        String dbVendor = args[0]; // { “mysql”, “oracle” }
        int deptNumber = Integer.parseInt(args[1]);

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Select_Records_… begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("deptNumber="+ deptNumber);
            System.out.println("---------------");

            // prepare query
            String query = "select DEPT_NUM, DEPT_NAME, DEPT_LOC " +
                    "from DEPT where DEPT_NUM > ?";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setInt(1, deptNumber); // set input parameter
            // execute query and return result as a ResultSet
            rs = pstmt.executeQuery();

            // extract data from the ResultSet
            while (rs.next()) {
                int dbDeptNumber = rs.getInt(1);
                String dbDeptName = rs.getString(2);
                String dbDeptLocation = rs.getString(3);
                System.out.println(dbDeptNumber +"\t"+ dbDeptName +
                      "\t"+ dbDeptLocation);
            }
            System.out.println("---------------");
            System.out.println("--Select_Records_… end--");
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
