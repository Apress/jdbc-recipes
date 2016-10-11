import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 * This class displays binary objects in a JFrame
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DemoDisplayBinary extends JPanel {
    /**
     * Constructor to display BLOB object.
     * @param dbVendor database vendor.
     * @param id the primary key to the MyPictures table
     */
     public DemoDisplayBinary(String dbVendor, String id) throws Exception {
        // materialize BLOB onto client
        Object[] binaryData = getBinaryData(dbVendor, id);
        setLayout(new GridLayout(1, 2));
        ImageIcon icon1 = new ImageIcon((byte[])binaryData[0]) ;
        JLabel photoLabel1 = new JLabel(icon1) ;
        add(photoLabel1);
        ImageIcon icon2 = new ImageIcon((byte[])binaryData[1]) ;
        JLabel photoLabel2 = new JLabel(icon2) ;
        add(photoLabel2);
    }

    /**
     * Extract and return the BLOB object.
     * @param dbVendor database vendor.
     * @param id the primary key to the BLOB object.
     */
     public static Object[] getBinaryData(String dbVendor, String id)
        throws Exception {
        Connection conn = null ;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String query = "SELECT raw_column, long_raw_column "+
                         "FROM binary_table WHERE id = ?";
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            Object[] results = new Object[2];
            pstmt = conn.prepareStatement(query) ;
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            // materialize binary data onto client
            results[0] = rs.getBytes("RAW_COLUMN");
            results[1] = rs.getBytes("LONG_RAW_COLUMN");
            return results;
        }
        finally {
            DatabaseUtil.close(rs);
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }

    public static void main(String args[]) throws Exception {
        String dbVendor = args[0]; // { "mysql", "oracle" }
        String id = args[1];
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ;
        JFrame frame = new JFrame("Binary Demo for" + dbVendor + " Database");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setContentPane(new DemoDisplayBinary(dbVendor, id)) ;
        frame.pack();
        frame.setVisible(true);
    }
}
