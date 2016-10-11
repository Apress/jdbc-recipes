/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class TestJDBCDriverInstallation_Oracle {

    public static void main(String[] args) {
        System.out.println("-- TestJDBCDriverInstallation_Oracle begin --");

        // Test a JDBC Driver Installation
        try {
            String className = "oracle.jdbc.driver.OracleDriver";
            Class driverObject = Class.forName(className);
            System.out.println("driverObject="+driverObject);
            System.out.println("your installation of JDBC Driver OK.");
        }
        catch(Exception e) {
            // your installation of JDBC Driver Failed
            System.out.println("Failed: JDBC Driver Error: "+e.getMessage());
        }

        System.out.println("-- TestJDBCDriverInstallation_Oracle end --");
    }
}
