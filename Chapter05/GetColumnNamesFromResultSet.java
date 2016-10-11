import java.sql.*;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class GetColumnNamesFromResultSet {

    public static String getColumnNames(ResultSet rs)
        throws SQLException {
        if (rs == null) {
            return null;
        }

        // get result set metadata
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        StringBuffer columnNames = new StringBuffer("<columnNames>");

        // get the column names; column indexes start from 1
        for (int i=1; i<numberOfColumns+1; i++) {
            String columnName = rsMetaData.getColumnName(i);
            // Get the name of the column's table name
            String tableName = rsMetaData.getTableName(i);
            columnNames.append("<column name=\""+columnName+
               "\" table=\""+tableName+"\"/>");
        }
        columnNames.append("</columnNames>");
        return columnNames.toString();
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String dbVendor = args[0];  // database vendor
        try {
            System.out.println("--GetColumnNamesFromResultSet begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "select id, name, age from employees";

            // create a statement
            stmt = conn.createStatement();

            // execute query and return result as a ResultSet
            rs = stmt.executeQuery(query);

            // get the column names from the ResultSet
            String columnNames = getColumnNames(rs);
            System.out.println(columnNames);
            System.out.println("--GetColumnNamesFromResultSet end--");
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
