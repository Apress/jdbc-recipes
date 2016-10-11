import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Map;
import java.util.List;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DbUtils_UseMap_MySQL {

    public static void main(String[] args) {

        Connection conn = null;
        String jdbcURL = "jdbc:mysql://localhost/octopus";
        String jdbcDriver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "root";

        try {
            DbUtils.loadDriver(jdbcDriver);
            conn = DriverManager.getConnection(jdbcURL, user, password);

            QueryRunner qRunner = new QueryRunner();
            System.out.println("DbUtils_UseMap_MySQL: begin using MapListHandler...");

            List mapList = (List) qRunner.query(conn,
                "select id, name from animals_table",
                new MapListHandler());

            for (int i = 0; i < mapList.size(); i++) {
                Map map = (Map) mapList.get(i);
                System.out.println("id=" + map.get("id"));
                System.out.println("name=" + map.get("name"));
                System.out.println("-----------------");
            }

            System.out.println("DbUtils_UseMap_MySQL: end.");

        }
        catch (SQLException e) {
            // handle the exception
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(conn);
        }
    }
}