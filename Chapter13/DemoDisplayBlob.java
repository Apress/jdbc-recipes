import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 * This class displays blob objects in a JFrame
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DemoDisplayBlob extends JPanel {
    /**
     * Constructor to display BLOB object.
     * @param dbVendor database vendor
     * @param id the primary key to the MyPictures table
     */
     public DemoDisplayBlob(String dbVendor, String id) throws Exception {
        // materialize BLOB onto client
        java.sql.Blob blob = getBlob(dbVendor, id);
        byte[] data = blob.getBytes(1, (int)blob.length());

        // add blob to frame
        setLayout(new GridLayout(1, 1));
        JLabel label = new JLabel(new ImageIcon(data)) ;
        add(label);
    }

    /**
     * Extract and return the BLOB object.
     * @param dbVendor database vendor
     * @param id the primary key to the BLOB object.
     */
     public static java.sql.Blob getBlob(String dbVendor, String id)
        throws Exception {
        Connection conn = null ;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String query = "SELECT blob_column FROM blob_table WHERE id = ?";

        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            pstmt = conn.prepareStatement(query) ;
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            // materialize binary data onto client
            java.sql.Blob blob = rs.getBlob(1);
            return blob;
        }
        finally {
            DatabaseUtil.close(rs);
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }

    public static void main(String args[]) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ;
        JFrame frame = new JFrame("Blob Demo for Oracle Database");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        String dbVendor = args[0]; // { "mysql", "oracle" }
        String id = args[1];
        frame.setContentPane(new DemoDisplayBlob(dbVendir, id)) ;
        frame.pack();
        frame.setVisible(true);
    }
}
