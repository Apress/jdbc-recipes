     /**
      *
      * @author Mahmoud Parsian
      * @email  admin@jdbccookbook.com
      *
      */
     public class Test {
       public static void main(String[] args) {
          int x = 1;
          int y = 0;

          System.out.println("try block starts");
          try {
              System.out.println("before division");
              int z = x/y; // division by 0
              System.out.println("after division");
          }
          catch (ArithmeticException ae)  {
              // catch clause below handles the ArithmeticException
              // generated by the division by zero. This exception must be
              // listed before Exception (because it is a subclass of Exception).
              System.out.println("--- attempt to divide by 0: handle exception");
              ae.printStackTrace(); // print the details of exception
              // do whatever here to handle the exception
              System.out.println("--- end to handle the exception");
          }
          catch(Exception e) {
              // handle the other exceptions
              e.printStackTrace();
              System.out.println(e.getMessage());
          }
          System.out.println("catch block ends");
        }
    }
