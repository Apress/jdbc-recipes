import java.io.*;
import java.sql.*;

import jcb.db.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class InsertTextFileToMySQL {

    private static final String INSERT_TEXT_FILE =
        "insert into DataFiles(id, fileName, fileBody) values (?, ?, ?)";

    private static String trimArgument(String s) {
        if ((s == null) || (s.length() == 0)) {
            return s;
        }
        else {
            return s.trim();
        }
    }

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

        if ((args == null) || (args.length != 3)) {
            System.err.println("Usage: java InsertTextFileToMySQL <id> <name> <textfile>");
            System.exit(0);
        }

        String id = trimArgument(args[0]);
        String name = trimArgument(args[1]);
        String textFile = trimArgument(args[2]);

        FileInputStream fis  = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            File file = new File(textFile);
            fis = new FileInputStream(file);
            pstmt = conn.prepareStatement(INSERT_TEXT_FILE);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setAsciiStream(3, fis,(int)file.length());
            pstmt.executeUpdate();
            conn.commit();
        }
        catch (Exception e) {
          System.err.println("Error: " + e.getMessage());
          e.printStackTrace();
        }
        finally {
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(fis);
            DatabaseUtil.close(conn);
        }
    }

}
