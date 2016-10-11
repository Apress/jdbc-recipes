import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class CreateEmployeeTableMySQL {

  private static final String EMPLOYEE_TABLE =
        "create table MyEmployees3 ( " +
        "   id INT PRIMARY KEY, " +
        "   firstName VARCHAR(20), " +
        "   lastName VARCHAR(20), " +
        "   title VARCHAR(20), " +
        "   salary INT " +
        ")";

    /**
     * Create a MySQL Connection...
     */
    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }


    public static void main(String args[]) {

        Connection conn = null;
        Statement stmt = null;
        try {
          conn = getConnection();
          stmt = conn.createStatement();
          stmt.executeUpdate(EMPLOYEE_TABLE);
          stmt.executeUpdate("insert into MyEmployees3(id, firstName) values(100, 'Alex')");
          stmt.executeUpdate("insert into MyEmployees3(id, firstName) values(200, 'Mary')");
          System.out.println("CreateEmployeeTableMySQL: main(): table created.");
        }
        catch( ClassNotFoundException e ) {
          System.out.println("error: failed to load MySQL driver.");
          e.printStackTrace();
        }
        catch( SQLException e ) {
          System.out.println("error: failed to create a connection object.");
          e.printStackTrace();
        }
        catch( Exception e ) {
          System.out.println("other error:");
          e.printStackTrace();
        }
        finally {
            // close resources
            close(stmt);
            close(conn);
        }
    }

    public static void close(java.sql.Connection conn) {
        if (conn != null) {
            try {
                if (conn.isClosed()) {
                    return;
                }
                else {
                    conn.close();
                }
            }
            catch(Exception e) {
                // ignore
            }
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
        }
    }


}