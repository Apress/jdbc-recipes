import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DbUtils_UseBean_MySQL {

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
            System.out.println("DbUtils_UseBean_MySQL: begin using BeanListHandler...");

            List beans = (List) qRunner.query(conn,
                "select id, name from animals_table",
                new BeanListHandler(AnimalBean.class));

            for (int i = 0; i < beans.size(); i++) {
                AnimalBean bean = (AnimalBean) beans.get(i);
                bean.print();
            }
            System.out.println("DbUtils_UseBean_MySQL: end.");

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