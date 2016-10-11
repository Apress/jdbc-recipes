import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class Create_Table_All_Types_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "scott";
        String password = "tiger";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {

        System.out.println("Create_Table_All_Types_Oracle(): begin");
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = getConnection();

            // create an varray type
            pstmt = conn.prepareStatement("CREATE TYPE varray_type is VARRAY(5) OF VARCHAR(10)");
            pstmt.executeUpdate();


            // Create an OBJECT type
            pstmt = conn.prepareStatement("CREATE TYPE oracle_object is OBJECT(column_string VARCHAR(128), column_integer INTEGER)");
            pstmt.executeUpdate();

            StringBuffer allTypesTable = new StringBuffer("CREATE TABLE oracle_all_types(");
            //                    Column Name          Oracle Type          Java Type
            allTypesTable.append("column_short           SMALLINT, ");          // short
            allTypesTable.append("column_int             INTEGER, ");           // int
            allTypesTable.append("column_float           REAL, ");              // float; can also be NUMBER
            allTypesTable.append("column_double          DOUBLE PRECISION, ");  // double; can also be FLOAT or NUMBER
            allTypesTable.append("column_bigdecimal      DECIMAL(13,0), ");     // BigDecimal
            allTypesTable.append("column_string          VARCHAR2(254), ");     // String; can also be CHAR(n)
            allTypesTable.append("column_characterstream LONG, ");              // CharacterStream or AsciiStream
            allTypesTable.append("column_bytes           RAW(2000), ");         // byte[]; can also be LONG RAW(n)
            allTypesTable.append("column_binarystream    RAW(2000), ");         // BinaryStream; can also be LONG RAW(n)
            allTypesTable.append("column_timestamp       DATE, ");              // Timestamp
            allTypesTable.append("column_clob            CLOB, ");              // Clob
            allTypesTable.append("column_blob            BLOB, ");              // Blob; can also be BFILE
            allTypesTable.append("column_bfile           BFILE, ");             // oracle.sql.BFILE
            allTypesTable.append("column_array           varray_type, ");       // oracle.sql.ARRAY
            allTypesTable.append("column_object          oracle_object)");      // oracle.sql.OBJECT
            pstmt.executeUpdate(allTypesTable.toString());
            // when you are at this point, creation of table ok.

            System.out.println("Create_Table_All_Types_Oracle(): end");
        }
        catch (Exception e) {
            // creation of table failed.
            // handle the exception
            e.printStackTrace();
        }
    }
}
