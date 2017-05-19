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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raminder
 */
public class VehicleUserSignUp extends HttpServlet {

   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String un,pw,pn,ei;
            un=request.getParameter("un");
            pw=request.getParameter("pw");
            pn=request.getParameter("pn");
            ei=request.getParameter("ei");
            System.out.println(un+pw+pn+ei);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing","root","9JarTnyxny");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("select * from vehicle_user where username='" + un + "' or email_id='"+ ei +"'");
            if (rs.next()) //Row Found
            {
                out.println("UserName/Email Id already registered");
            } else {
                //Insert New Record
                rs.moveToInsertRow();

                rs.updateString("username", un);
                rs.updateString("password", pw);
                rs.updateString("personname", pn);
                rs.updateString("email_id",ei);
                //rs.updateString("contact", cn);
               // rs.updateString("photo", photopath);

                rs.insertRow();
                out.println("success");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();;
        }
    }

    

  

}
