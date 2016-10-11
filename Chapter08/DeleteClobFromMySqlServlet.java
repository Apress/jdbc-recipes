import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jcb.db.DatabaseUtil;
import jcb.util.IOUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DeleteClobFromMySqlServlet extends HttpServlet {

    private static final String DELETE_CLOB_RECORD =
        "delete from DataFiles where id = ?";

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        String id = request.getParameter("id").trim();
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("text/html");
        out.println("<html><head><title>Delete CLOB Record</title></head>");

        try {
          conn = getConnection();
          pstmt = conn.prepareStatement(DELETE_CLOB_RECORD);
          pstmt.setString(1, id);
          pstmt.executeUpdate();
          out.println("<body><h4>deleted CLOB record with id="+id+"</h4></body></html>");
        }
        catch (Exception e) {
            out.println("<body><h4>Error="+e.getMessage()+"</h4></body></html>");
        }
        finally {
          DatabaseUtil.close(pstmt);
          DatabaseUtil.close(conn);
        }
    }

    public void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
    }

}
