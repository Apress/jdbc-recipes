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
 public class Demo_Statement_AddBatch_Oracle {

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "mp";
        String password = "mp2";
        //String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
        //String username = "octopus";
        //String password = "octopus";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void checkUpdateCounts(int[] updateCounts) {
        for (int i=0; i<updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                // Successfully executed; the number represents number of affected rows
                System.out.println("Successfully executed; updateCount="+updateCounts[i]);
            }
            else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                System.out.println("Successfully executed; updateCount=Statement.SUCCESS_NO_INFO");
            }
            else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                // Failed to execute
                System.out.println("Failed to execute; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        try {
            System.out.println("------Demo_Statement_AddBatch_Oracle begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // Disable auto-commit
            conn.setAutoCommit(false);

            // create Statement object
            stmt = conn.createStatement();
            stmt.addBatch("DELETE FROM animals_table");
            stmt.addBatch("INSERT INTO animals_table(id, name) VALUES(111, 'ginger')");
            stmt.addBatch("INSERT INTO animals_table(id, name) VALUES(222, 'lola')");
            stmt.addBatch("INSERT INTO animals_table(id, name) VALUES(333, 'freddy')");


            // Execute the batch
            int[] updateCounts = stmt.executeBatch();

            // all statements were successfully executed.
            // updateCounts contains one element for each
            // batched statement. The updateCounts[i] contains
            // the number of rows affected by that statement.
            checkUpdateCounts(updateCounts);

            // since there were no errors, commit
            conn.commit();
        }
        catch (BatchUpdateException e) {
            // Not all of the statements were successfully executed
            int[] updateCounts = e.getUpdateCounts();

            // Some databases will continue to execute after one fails.
            // If so, updateCounts.length will equal the number of batched statements.
            // If not, updateCounts.length will equal the number of successfully executed statements
            checkUpdateCounts(updateCounts);

            // Either commit the successfully executed statements or rollback the entire batch
            try {
                conn.rollback();
            }
            catch (Exception e2) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
}