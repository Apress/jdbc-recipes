import java.sql.*;

import jcb.db.VeryBasicConnectionManager;
import jcb.meta.DatabaseMetaDataTool;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestGetResultSetTypes {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            System.out.println("--TestGetResultSetTypes begin--");
            String dbVendor = args[0]; // { "mysql", "oracle" }
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            String resultSetTypes =
                DatabaseMetaDataTool.getAvailableResultSetTypes(conn);
            System.out.println(resultSetTypes);
            System.out.println("--TestGetResultSetTypes end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(conn);
        }
    }
}
