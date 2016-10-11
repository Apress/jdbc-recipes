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
 public class GetColumnNamesFromResultSet_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "mp";
        String password = "mp2";

        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static String getColumnNames(ResultSet rs)
        throws SQLException {
        if (rs == null) {
            return null;
        }

        // get result set meta data
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        StringBuffer columnNames = new StringBuffer("<columnNames>");

        // get the column names; column indexes start from 1
        for (int i=1; i<numberOfColumns+1; i++) {
            String columnName = rsMetaData.getColumnName(i);
            // Get the name of the column's table name
            String tableName = rsMetaData.getTableName(i);
            columnNames.append("<column name=\""+columnName+"\" table=\""+tableName+"\"/>");
        }
        columnNames.append("</columnNames>");
        return columnNames.toString();
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("------GetColumnNamesFromResultSet_Oracle begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            //
            // prepare query
            //
            String query = "select id, name, age from employees";

            //
            // create a statement
            //
            stmt = conn.createStatement();


            //
            // execute query and return result as a ResultSet
            //
            rs = stmt.executeQuery(query);

            //
            // get the column names from the ResultSet
            //
            String columnNames = getColumnNames(rs);
            System.out.println(columnNames);
            System.out.println("------GetColumnNamesFromResultSet_Oracle end---------");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
}