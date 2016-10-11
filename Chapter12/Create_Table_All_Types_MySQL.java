import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class Create_Table_All_Types_MySQL {

    private static final String CD_RECORD =
        "insert into cd(id, title, artist, tracks) values(null, ?, ?, ?)";

    public static Connection getConnection() throws Exception {

        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";

        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {

        System.out.println("Create_Table_All_Types_MySQL(): begin");
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {

            StringBuffer allTypesTable = new StringBuffer("CREATE TABLE mysql_all_types(");
            //                    Column Name          MySQL Type          Java Type
            allTypesTable.append("column_boolean       BOOL, ");              // boolean
            allTypesTable.append("column_byte          TINYINT, ");           // byte
            allTypesTable.append("column_short         SMALLINT, ");          // short
            allTypesTable.append("column_int           INTEGER, ");           // int
            allTypesTable.append("column_long          BIGINT, ");            // long
            allTypesTable.append("column_float         FLOAT, ");             // float
            allTypesTable.append("column_double        DOUBLE PRECISION, ");  // double
            allTypesTable.append("column_bigdecimal    DECIMAL(13,0), ");     // BigDecimal
            allTypesTable.append("column_string        VARCHAR(254), ");      // String
            allTypesTable.append("column_date          DATE, ");              // Date
            allTypesTable.append("column_time          TIME, ");              // Time
            allTypesTable.append("column_timestamp     TIMESTAMP, ");         // Timestamp
            allTypesTable.append("column_asciistream1  TINYTEXT, ");          // Clob (&lt; 2^8 bytes)
            allTypesTable.append("column_asciistream2  TEXT, ");              // Clob (&lt; 2^16 bytes)
            allTypesTable.append("column_asciistream3  MEDIUMTEXT, ");        // Clob (&lt; 2^24 bytes)
            allTypesTable.append("column_asciistream4  LONGTEXT, ");          // Clob (&lt; 2^32 bytes)
            allTypesTable.append("column_blob1         TINYBLOB, ");          // Blob (&lt; 2^8 bytes)
            allTypesTable.append("column_blob2         BLOB, ");              // Blob (&lt; 2^16 bytes)
            allTypesTable.append("column_blob3         MEDIUMBLOB, ");        // Blob (&lt; 2^24 bytes)
            allTypesTable.append("column_blob4         LONGBLOB)");           // Blob (&lt; 2^32 bytes)

            conn = getConnection();
            pstmt = conn.prepareStatement(allTypesTable.toString());
            pstmt.executeUpdate();
            // creation of table ok.

            System.out.println("Create_Table_All_Types_MySQL(): end");
        }
        catch (Exception e) {
            // creation of table failed.
            // handle the exception
            e.printStackTrace();
        }
    }
}
