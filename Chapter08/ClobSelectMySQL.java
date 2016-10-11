   import javax.swing.*;
   import java.awt.*;
   import java.awt.event.*;
   import java.sql.*;

   import jcb.db.*;

   /**
    * This class displays a Clob object in a JFrame
    *
    *
    * @author Mahmoud Parsian
    * @email  admin@jdbccookbook.com
    *
    */
   public class ClobSelectMySQL extends JPanel {

       // look and feel constants
       public static final String MOTIF_LOOK_AND_FEEL =
           "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

       public static final String WINDOWS_LOOK_AND_FEEL =
           "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

       public static final String METAL_LOOK_AND_FEEL =
           "javax.swing.plaf.metal.MetalLookAndFeel";


       /**
        * Get a connection object.
        */
       public static Connection getConnection() throws Exception {
           String driver = "org.gjt.mm.mysql.Driver";
           String url = "jdbc:mysql://localhost/octopus";
           String username = "root";
           String password = "root";
           Class.forName(driver);  // load MySQL driver
           Connection conn = DriverManager.getConnection(url, username, password);
           return conn;
       }


       /**
        * Extract and return the CLOB object as String.
        * @param id the primary key to the CLOB object.
        */
        public static String getCLOB(int id) throws Exception {
           Connection conn = null ;
           ResultSet rs = null;
           PreparedStatement pstmt = null;
           String query = "SELECT fileBody FROM DataFiles WHERE id = ?" ;

           try {
               conn = getConnection();
               pstmt = conn.prepareStatement(query) ;
               pstmt.setInt(1, id);
               rs = pstmt.executeQuery();
               rs.next();
               Clob clob = rs.getClob(1);
               // materialize CLOB onto client
               String wholeClob = clob.getSubString(1, (int) clob.length());
               return wholeClob;
           }
           finally {
               DatabaseUtil.close(rs);
               DatabaseUtil.close(pstmt);
               DatabaseUtil.close(conn);
           }
       }


       /**
        * Constructor to display CLOB object.
        * @param id the primary key to the DataFiles table
        */
        public ClobSelectMySql(int id) throws Exception {
           setLayout(new GridLayout(1, 1));
           add(new TextArea(getCLOB(id), 3, 10));
       }


       public static void main(String args[]) throws Exception {
           int id = Integer.parseInt(args[0]);
           UIManager.setLookAndFeel(METAL_LOOK_AND_FEEL) ;
           JFrame frame = new JFrame("CLOB Demo for MySQL Database. id="+id);
           frame.addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                   System.exit(0);
               }
           });

           frame.setContentPane(new ClobSelectMySql(id)) ;
           frame.pack();
           frame.setVisible(true);
       }
}