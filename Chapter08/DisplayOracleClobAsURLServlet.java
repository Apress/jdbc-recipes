import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jcb.db.DatabaseUtil;
import jcb.util.IOUtil;
import jcb.util.RandomGUID;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DisplayOracleClobAsURLServlet extends HttpServlet {

    // directory where clob data will be placed as files.
    private static final String CLOB_DIRECTORY =
        "c:/tomcat/webapps/octopus/clobDir";

    // CLOB_DIRECTORY as a URL
    private static final String CLOB_URL =
        "http://localhost:8000/octopus/clobDir";

    private static final String CLOB_FILE_PREFIX = "/clob-";

    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "scott";
        String password = "tiger";
        //String password = "tiger222"; // wrong password
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    private static String getClobAsURL(Clob Clob) throws Exception {

        InputStream in = null;
        FileOutputStream out = null;
        try {

            if (Clob == null) {
                return null;
            }

            // get a random GUID for Clob filename
            String guid = RandomGUID.getGUID();
            String ClobFile = CLOB_DIRECTORY + CLOB_FILE_PREFIX + guid;

            in = Clob.getAsciiStream();
            if (in == null) {
                return null;
            }

            out = new FileOutputStream(ClobFile);
            int length = (int) Clob.length();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();

            return CLOB_URL + CLOB_FILE_PREFIX + guid;
        }
        finally {
          IOUtil.close(in);
          IOUtil.close(out);
        }

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        Clob clob = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String id = request.getParameter("id").trim();
        String query = "select fileBody from DataFiles where  id = "+id;
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("text/html");
        out.println("<html><head><title>DisplayOracleClobAsURLServlet</title></head>");

        try {
          conn = getConnection();
        }
        catch(Exception e) {
            out.println("<body><h4>Database Connection Problem.</h4>");
            out.println("<h4>"+e.getMessage()+"</h4></body></html>");
            return;
        }

        try {
          stmt = conn.createStatement();
          rs = stmt.executeQuery(query);

          if (rs.next()) {
            clob = rs.getClob(1);
            out.println("<body><h3>file id="+id+"</h3>"+
                        getClobAsURL(clob)+"</body></html>");
          }
          else {
            out.println("<body><h4>No File found for id="+id+"</h4></body></html>");
            return;
          }
        }
        catch (Exception e) {
            out.println("<body><h4>Error="+e.getMessage()+"</h4></body></html>");
            return;
        }
        finally {
          DatabaseUtil.close(rs);
          DatabaseUtil.close(stmt);
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
