import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 * This class displays a Blob object in a JFrame
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class BlobSelect extends JPanel {

   // look and feel constants
   public static final String METAL_LOOK_AND_FEEL =
      "javax.swing.plaf.metal.MetalLookAndFeel";

   public static void main(String args[]) throws Exception {
      UIManager.setLookAndFeel(METAL_LOOK_AND_FEEL) ;
      JFrame frame = new JFrame("Blob Demo for MySQL Database");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });

      Connection conn = null;
      String dbVendor = args[0]; // { "mysql", "oracle" }
      int id = Integer.parseInt(args[1]);
      conn = VeryBasicConnectionManager.getConnection(dbVendor);
      frame.setContentPane(new BlobSelect(id, conn)) ;
      frame.pack();
      frame.setVisible(true);
   }


/**
 * Extract and return the BLOB object.
 * @param id the primary key to the BLOB object.
 * @param conn Connection object.
 */
 public static byte[] getBLOB(int id, Connection conn)
    throws Exception {
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    String query = "SELECT photo FROM MyPictures WHERE id = ?" ;
    try {
       pstmt = conn.prepareStatement(query) ;
       pstmt.setInt(1, id);
       rs = pstmt.executeQuery();
       rs.next();
       Blob blob = rs.getBlob("photo");
       // materialize BLOB onto client
       return blob.getBytes(1, (int)blob.length());
    }
    finally {
       DatabaseUtil.close(rs);
       DatabaseUtil.close(pstmt);
       DatabaseUtil.close(conn);
    }
}


/**
 * Constructor to display BLOB object.
 * @param id the primary key to the MyPictures table
 * @param conn Connection object.
 */
 public BlobSelect(int id, Connection conn)
    throws Exception {
    // materialize BLOB onto client
    ImageIcon icon = new ImageIcon(getBLOB(id, conn)) ;
    JLabel photoLabel = new JLabel(icon) ;
    setLayout(new GridLayout(1, 1));
    add(photoLabel);
 }

}