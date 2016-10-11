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
 public class Demo_DataTruncation_1
    extends VeryBasicConnectionManager {

    public static void displayError(DataTruncation dataTruncation) {
        if ( dataTruncation != null ) {
            System.out.println("Data truncation error: ");
            System.out.println(dataTruncation.getDataSize() +
              " bytes should have been ");
            if (dataTruncation.getRead()) {
                System.out.println("Read (Error:) ");
            }
            else {
                System.out.println("Written (Error:) ");
            }
            System.out.println(dataTruncation.getTransferSize() +
              " number of bytes of data actually transferred.");
        }
    }

    public static void displayError(SQLWarning warning) {
        while ( warning != null ) {
            if ( warning instanceof DataTruncation ) {
                displayError((DataTruncation) warning);
            }
            else {
                System.out.println(" Warning: " + warning.getMessage());
            }
            warning = warning.getNextWarning();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            System.out.println("-- Demo_DataTruncation_1 begin --");
            // args[0] = dbVendor = {“mysql”, “oracle” }
            conn = VeryBasicConnectionManager.getConnection(args[0]); //  args[0] = dbVendor
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // create Statement object
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM animals_table");
            displayError(stmt.getWarnings());
            // try to write more data for the name column.
            stmt.executeUpdate("INSERT INTO animals_table(id, name)"+
                               "VALUES(111, 'ginger123456789')");
            displayError(stmt.getWarnings());

            System.out.println("-- Demo_DataTruncation_1 end --");
        }
        catch (DataTruncation dt) {
            System.out.println("-- got DataTruncation exception --");
            displayError(dt);
            System.out.println("-- printStackTrace --");
            dt.printStackTrace();
            System.exit(1);
        }
        catch (SQLException se) {
            System.out.println("-- got SQLException exception --");
            System.out.println("Database error message: "+se.getMessage());
            System.exit(1);
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
