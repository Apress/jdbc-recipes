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
 public class Demo_PreparedStatement_AddBatch
    extends VeryBasicConnectionManager {

    public static void checkUpdateCounts(int[] updateCounts) {
        for (int i=0; i<updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                // Successfully executed; the number
                // represents number of affected rows
                System.out.println("OK; updateCount="+updateCounts[i]);
            }
            else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of
                // affected rows not available
                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
            }
            else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                // Failed to execute
                System.out.println("Failure; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }

    public static void main(String[] args) {
        String query = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Demo_PreparedStatement_AddBatch begin--");
            conn = getConnection(args[0]); // args[0] = dbVendor
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // Disable auto-commit
            conn.setAutoCommit(false);

            // prepare query
            query = "insert into add_batch_table(string_column, int_column) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);

            // add the first batch
            pstmt.setString(1, "id-1");
            pstmt.setInt(2, 100);
            pstmt.addBatch();

            // add the second batch
            pstmt.setString(1, "id-2");
            pstmt.setInt(2, 200);
            pstmt.addBatch();

            // add the third batch
            pstmt.setString(1, "id-3");
            pstmt.setInt(2, 300);
            pstmt.addBatch();

            // execute the batch
            int[] updateCounts = pstmt.executeBatch();

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

            // Some databases will continue to execute after one
            // fails. If so, updateCounts.length will equal the
            // number of batched statements. If not, updateCounts.length
            // will equal the number of successfully executed statements
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
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }
}