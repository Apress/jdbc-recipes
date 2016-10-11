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
 public class InsertClobToMySqlServlet extends HttpServlet {

    static final String INSERT_CLOB =
        "insert into datafiles(id, filename, filebody) values (?, ?, ?)";

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
        String name = trimParameter(request.getParameter("name"));
        String fileAsURL = trimParameter(request.getParameter("file"));
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        out.println("<html><head><title>InsertClobToMySqlServlet</title></head>");

        try {
          conn = getConnection();
          fileContent = getClobsContentAsString(fileAsURL);
          insertCLOB(conn, id, name, fileContent);
          out.println("<body><h4>OK: inserted a new record with id="+id+"</h4></body></html>");
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

    public void insertCLOB(Connection conn, String id, String name, String fileContent)
        throws Exception {

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(INSERT_CLOB);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, fileContent);
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
