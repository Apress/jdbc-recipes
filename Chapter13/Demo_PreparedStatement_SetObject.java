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

public class Demo_PreparedStatement_SetObject {

    public static void main(String[] args) {

        System.out.println("-- Demo_PreparedStatement_SetObject begin--");
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        try {
            String dbVendor = args[0]; // { "mysql", "oracle" }
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // table column names
            String [] columnNames = {"id", "name", "content", "date_created"};

            // inputValues contains the data to put in the database
            Object [] inputValues = new Object[columnNames.length];


            // fill input values
            inputValues[0] = new java.math.BigDecimal(100);
            inputValues[1] = new String("Alex Taylor");
            inputValues[2] = new String("This is my resume.");
            inputValues[3] = new Timestamp ( (new java.util.Date()).getTime() );

            // prepare blob object from an existing binary column
            String insert =  "insert into resume (id, name, content, date_created)"+
                             "values(?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insert);

            // set input parameter values
            pstmt.setObject(1, inputValues[0]);
            pstmt.setObject(2, inputValues[1]);
            pstmt.setObject(3, inputValues[2]);
            pstmt.setObject(4, inputValues[3]);

            // execute SQL INSERT statement
            pstmt.executeUpdate();

            // now retrieve the inserted record from db
            String query =
              "select id, name, content, date_created from resume where id=?";

            // create PrepareStatement object
            pstmt2 = conn.prepareStatement(query);
            pstmt2.setObject(1, inputValues[0]);
            rs = pstmt2.executeQuery();
            Object [] outputValues = new Object[columnNames.length];
            if (rs.next()) {
                // outputValues contains the data retrieved from the database
                for ( int i = 0; i < columnNames.length; i++) {
                    outputValues[i] = rs.getObject(i+1);
                }
            }


            //
            // display retrieved data
            //
            if (dbVendor.equalsIgnoreCase("oracle")) {
                System.out.println("id="+
                    ((java.math.BigDecimal) outputValues[0]).toString());
                System.out.println("name="+ ((String) outputValues[1]));
                System.out.println("content="+
                    ((Clob) outputValues[2]));
                System.out.println("date_created="+
                    ((java.sql.Date) outputValues[3]).toString());
            }
            else if (dbVendor.equalsIgnoreCase("mysql")) {
                System.out.println("id="+ ((Long) outputValues[0]).toString());
                System.out.println("name="+ ((String) outputValues[1]));
                System.out.println("content="+
                    ((String) outputValues[2]));
                System.out.println("date_created="+
                    ((java.sql.Timestamp) outputValues[3]).toString());
            }

            System.out.println("---------------");
            System.out.println("-- Demo_PreparedStatement_SetObject end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(pstmt2);
            DatabaseUtil.close(conn);
        }
    }
}
