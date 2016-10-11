import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.db.VeryBasicConnectionManager;
import jcb.util.DatabaseUtil;

public class DemoGetGeneratedKeys
    extends VeryBasicConnectionManager {

    public static void main(String[] args) {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        try {
            System.out.println("--DemoGetGeneratedKeys begin--");
            String dbVendor = args[0]; // database vendor
            conn = getConnection(dbVendor);
            System.out.println("conn="+conn);

            // create a statement
            stmt = conn.createStatement();


            // insert a  record into the animals_table
            // note that the SQL INSERT is different for each vendor
            String insert = null;
            if (dbVendor.equalsIgnoreCase("mysql")) {
                insert = "insert into animals_table(name) "+
                         "values('tiger11')";
            }
            else if (dbVendor.equalsIgnoreCase("oracle")) {
                insert = "insert into animals_table(id, name) "+
                         "values(ANIMAL_ID_SEQ.nextval, 'tiger11')";
            }

            stmt.executeUpdate(insert);

            if (dbVendor.equalsIgnoreCase("mysql")) {
               rs = stmt.getGeneratedKeys();
           }
            else if (dbVendor.equalsIgnoreCase("oracle")) {
               rs = stmt.executeQuery("select ANIMAL_ID_SEQ.currval from dual");
            }

            while (rs.next()) {
                 ResultSetMetaData rsMetaData = rs.getMetaData();
                 int columnCount = rsMetaData.getColumnCount();
                 for (int i = 1; i <= columnCount; i++) {
                     String key = rs.getString(i);
                     System.out.println("key " + i + " is " + key);
                 }
            }
            System.out.println("--DemoGetGeneratedKeys end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
}