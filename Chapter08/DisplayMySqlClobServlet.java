import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jcb.db.DatabaseUtil;


/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DisplayMySqlClobServlet extends HttpServlet {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        System.out.println("-- DisplayMySqlClobServlet begin --");

        Clob fileAsCLOB = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String id = request.getParameter("id").trim();
        String query = "select fileBody from DataFiles where id = "+id;
        ServletOutputStream out = response.getOutputStream();

        // all responses will be in text/html format
        response.setContentType("text/html");

        try {
          conn = getConnection();
        }
        catch(Exception e) {
            out.println("<html><head><title>CLOB Example</title></head>");
            out.println("<body><h4>Database Connection Problem.</h4>");
            out.println("<h5>"+e.getMessage()+"</h5></body></html>");
            return;
        }

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                fileAsCLOB = rs.getClob(1);
            }
            else {
                out.println("<html><head><title>CLOB Example</title></head>");
                out.println("<body><h4>No file found for id="+id+"</h4></body></html>");
                return;
            }

            // Materialize the CLOB as a String object (get the whole clob).
            long length = fileAsCLOB.length();
            // note that the first character is at position 1
            String fileAsString = fileAsCLOB.getSubString(1, (int) length);

            // write it for display
            out.println(fileAsString);
            System.out.println("CLOB writing done.");
        }
        catch (SQLException e) {
            out.println("<html><head><title>Error: CLOB Example</title></head>");
            out.println("<body><h4>Error="+e.getMessage()+"</h4></body></html>");
            return;
        }
        finally {
          DatabaseUtil.close(rs);
          DatabaseUtil.close(stmt);
          DatabaseUtil.close(conn);
        }
        System.out.println("*** DisplayMySqlClobServlet end----------------");
    }

    public void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
    }
}