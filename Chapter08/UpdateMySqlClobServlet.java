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
 public class UpdateMySqlClobServlet extends HttpServlet {

    static final String UPDATE_CLOB =
        "update datafiles set filebody=? where id=?";

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

        String fileContent = null;
        Connection conn = null;

        String id = trimParameter(request.getParameter("id"));
        String fileAsURL = trimParameter(request.getParameter("file"));
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        out.println("<html><head><title>UpdateMySqlClobServlet</title></head>");

        try {
          conn = getConnection();
          fileContent = getClobsContentAsString(fileAsURL);
          updateCLOB(conn, id, fileContent);
          out.println("<body><h4>OK: updated an existing record with id="+id+"</h4></body></html>");
        }
        catch(Exception e) {
          e.printStackTrace();
          out.println("<body><h4>Error: "+e.getMessage()+"</h4></body></html>");
        }
    }

    public void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
        doGet(request, response);
    }

    public void updateCLOB(Connection conn, String id, String fileContent)
        throws Exception {

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(UPDATE_CLOB);
            pstmt.setString(1, fileContent);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        }
        finally {
            DatabaseUtil.close(pstmt);
        }
    }

    public static String  getClobsContentAsString(String urlAsString)
        throws Exception {
        InputStream content = null;
        try {
            java.net.URL url = new java.net.URL(urlAsString);
            java.net.URLConnection urlConn = url.openConnection();
            urlConn.connect();
            content = urlConn.getInputStream();
            return IOUtil.inputStreamToString(content);
        }
        finally {
            IOUtil.close(content);
        }
    }

    private static String trimParameter(String s) {
        if ((s == null) || (s.length() == 0)) {
            return s;
        }
        else {
            return s.trim();
        }
    }
}