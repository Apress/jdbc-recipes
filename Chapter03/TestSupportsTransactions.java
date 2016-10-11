import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestSupportsTransactions {

    public static boolean supportsTransactions(Connection conn)
        throws SQLException {

        if (conn == null) {
            return false;
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();
        if (dbMetaData == null) {
            // metadata is not supported
            return false;
        }

        return dbMetaData.supportsTransactions();
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            String dbVendor = args[0];
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("--- begin Test ---");
            System.out.println("dbVendor="+dbVendor);
            System.out.println("conn="+conn);
            System.out.println("Transaction Support:"+
               supportsTransactions(conn));
            System.out.println("--- end of Test ---");
       }
    catch(Exception e){
        e.printStackTrace();
        System.exit(1);
    }
    finally {
        DatabaseUtil.close(conn);
        }
    }
}