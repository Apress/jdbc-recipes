import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class RasingCustomSqlWarning {

   public static void main(String[] args) {
      if (args.length != 1) {
        System.out.printout("usage: RasingCustomSqlWarning ");
        System.exit(1);
      }

      // args[0] denotes the badge number
      String badgeNumber = args[0];
      try {
          // get the salary
          int salary = getEmployeeSalary(badgeNumber);

          // all is OK. we got the salary
          System.out.println("Got all data from database.");
          System.out.println("emp. badge number="+badgeNumber);
          System.out.println("emp. salary="+salary);
      }
      catch(SQLException e) {
          // printout root SQLException
          System.err.println("An SQL exception occurred: " + e);
          e.printStackTrace();

          // get all chained SQLExceptions
          while((e = e.getNextException()) != null) {
             System.err.println("Contained reason: " + e);
          }
      }
   }

   private static int getEmployeeSalary(String badgeNumber)
       throws SQLException {
       // Status flag resulting from database data should be
       // created from normal business rules in a live situation.
       boolean somethingWrongHappened = false;
       int salary = getEmployeeSalaryFromDatabase(badgeNumber);
       if (salary > 200000) {
            somethingWrongHappened = true;
       }

       if(somethingWrongHappened) {
           // Create two custom SQL Warnings
           SQLWarning rootWarning =
             new SQLWarning("Employee Salary Business rules not properly regarded");
           SQLWarning containedWarning =
             new SQLWarning("Salary over $200,000.");

           // Chain the warnings
           rootWarning.setNextWarning(containedWarning);

           // Notify the caller of the warnings
           throw rootWarning;
       }
   }

   private static int getEmployeeSalaryFromDatabase(String badgeNumber)
       throws SQLException {
       // return the salary of employee with
       // the badge number = badgeNumber
   }
}
