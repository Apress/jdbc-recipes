package jcb.util;

import java.sql.*;

/**
 * DatabaseUtil provides some basic methods for handling odd jdbc
 * tasks such as closing a ResultSet, PreparedStatement, ...
 *
 *
 * <p>
 * Date: October 2, 2002
 * <p>
 * @author Mahmoud Parsian
 * <p>
 * @since 1.4
 * <p>
 */
public class DatabaseUtil {

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
    }


    public static void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
    }


    public static void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        }
        catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
    }


    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
    }

    public static java.sql.Date getJavaSqlDate() {

        java.util.Date javaUtilDate = new java.util.Date();
        return new java.sql.Date(javaUtilDate.getTime());
    }


    public static String getTrimmedString(ResultSet resultSet, int index)
        throws SQLException {

        String value = resultSet.getString(index);

        if (value != null) {
            value = value.trim();
        }

        return value;
    }


    public static String getTrimmedString(ResultSet resultSet, String columnName)
        throws SQLException {

        String value = resultSet.getString(columnName);

        if (value != null) {
            value = value.trim();
        }

        return value;
    }

}
