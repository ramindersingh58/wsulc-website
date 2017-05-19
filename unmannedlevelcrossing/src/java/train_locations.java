/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class train_locations extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String lat, lng, ti, ln;
            lat = request.getParameter("lat");
            lng = request.getParameter("lng");
            ti = request.getParameter("ti");
            ln = request.getParameter("ln");
            System.out.println("from servlet" + lat + lng + ti + ln);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing", "root", "9JarTnyxny");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("select * from train_data where train_id='" + ti + "'");
            if (rs.next()) //Row Found
            {
                Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2 = stmt1.executeQuery("select * from train_locations");

                rs2.next();
                //Insert New Record
                rs2.moveToInsertRow();

                rs2.updateString("lat", lat);
                rs2.updateString("lng", lng);
                rs2.updateString("train_id", ti);
                rs2.updateString("loc_name", ln);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(cal.getTime());
                rs2.updateString("time", time);

                rs2.insertRow();
                out.println(""+time);

            } else {
                out.println("failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
/*
if (rs.next()) //Row Found
            {
                Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2 = stmt1.executeQuery("select * from train_locations where train_id='" + ti + "' order by dateandtime");

                rs2.last();
                int total = rs2.getRow();
                rs2.beforeFirst();
                if (total == 5) {
                    rs2.next();
                    rs2.updateString("lat", lat);
                    rs2.updateString("lng", lng);
                    rs2.updateString("train_id", ti);
                    rs2.updateString("loc_name", ln);
                    rs2.updateRow();
                    out.println("success");
                } else {//total==0 or total<5
                    rs2.next();
                    //Insert New Record
                    rs2.moveToInsertRow();

                    rs2.updateString("lat", lat);
                    rs2.updateString("lng", lng);
                    rs2.updateString("train_id", ti);
                    rs2.updateString("loc_name", ln);

                    rs2.insertRow();
                    out.println("success");
                }
                
            } else {
                out.println("failed");
            }
*/
