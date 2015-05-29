
package pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 
 */

public class ItemSelectServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String item;
        if ("shirt".equals(request.getParameter("radio"))) {
            item = "シャツ";
        } else {
            item = "ジーンズ";
        }

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String driverURL = "jdbc:derby://localhost:1527/shohin0";
            Connection con = DriverManager.getConnection(driverURL, "db", "db");

            Statement stmt = con.createStatement();

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            String sql = "select * from B_SHOHIN where SHOHIN_NAME ='" + item + "'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Map<String, Object> record = new HashMap<String, Object>();
                record.put("id", new Integer(rs.getInt("SHOHIN_ID")));
                record.put("name", rs.getString("SHOHIN_NAME"));
                record.put("color", rs.getString("SHOHIN_COLOR"));
                record.put("size", rs.getString("SHOHIN_SIZE"));
                record.put("price", new Integer(rs.getInt("SHOHIN_PRICE")));
                list.add(record);
            }
            rs.close();

            stmt.close();
            con.close();

            request.setAttribute("data", list);

            RequestDispatcher rd;
            if ("シャツ".equals(item)) {
                rd = request.getRequestDispatcher("/topsSelect.jsp");
            } else {
                request.setAttribute("itm", item);
                rd = request.getRequestDispatcher("/bottomsSize.jsp");
            }
            rd.forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
