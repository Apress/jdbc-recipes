import java.sql.*;
/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class SmallTestMySQL {

  private static final String EMPLOYEE_TABLE =
        "create table employees_table ( " +
        "   id INT PRIMARY KEY, " +
        "   name VARCHAR(20))";

  public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";

        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
  }

  public static void main(String args[]) throws Exception {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      // get a database connection
      conn = getConnection();

      // create table
      stmt = conn.createStatement();
      stmt.executeUpdate(EMPLOYEE_TABLE);
      System.out.println("CreateEmployeeTableMySql: main(): table created.");

      // insert couple of records
      stmt.executeUpdate("insert into employees_table(id, name) values('100', 'alex')");
      stmt.executeUpdate("insert into employees_table(id, name) values('200', 'mary')");

      // get all employee records from the database
      rs = stmt.executeQuery("select id, name from employees_table");
      while (rs.next()) {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        System.out.println("id = "+id+"\t name = "+ name);
      }
    }
    catch( Exception e ) {
      // handle the exception
      e.printStackTrace();
    }
    finally {
        // close database resource not needed
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch(Exception e) { // ignore
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch(Exception e) { // ignore
        }

        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch(Exception e) { // ignore
        }
    }
  }
}
