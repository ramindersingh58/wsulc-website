/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Double.isNaN;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Raminder
 */
public class jsonTrainLocs2 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //JDBC CODE HERE
            int flag = 0;
            double mindist = 1000.0;
            JSONObject obj = new JSONObject();
            String clat, clon;
            clat = request.getParameter("lat");
            clon = request.getParameter("lon");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime = sdf.format(cal.getTime());
            System.out.println("train android" + clat + clon + currentTime);
            String beforeTime = subtractTimes(currentTime, "00:02:00");
            String afterTime = addTimes(currentTime, "00:02:00");
            System.out.println("after adding/subtracting time" + beforeTime + "and" + afterTime);
            JSONArray jsonArr = new JSONArray();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing", "root", "9JarTnyxny");

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("select * from train_data");

            while (rs.next()) {
                int ti = rs.getInt("train_id");
                String tn = rs.getString("train_name");
                int delayinmins = rs.getInt("delayinmins");
                String delayedBeforeTime = null, delayedAfterTime = null;
                if (delayinmins > 0) {
                    int hours = delayinmins / 60;
                    int mins = delayinmins % 60;
                    String delayedTime = String.format("%02d:%02d:%02d", hours, mins,0);
                    delayedBeforeTime = subtractTimes(beforeTime, delayedTime);
                    delayedAfterTime = subtractTimes(afterTime, delayedTime);
                } else {
                    delayinmins = -delayinmins;
                    int hours = delayinmins / 60;
                    int mins = delayinmins % 60;
                    String delayedTime = String.format("%02d:%02d:%02d", hours, mins,0);
                    delayedBeforeTime = addTimes(beforeTime, delayedTime);
                    delayedAfterTime = addTimes(afterTime, delayedTime);
                }
                System.out.println("..............................\n"+ti+"::"+delayinmins+"::"+delayedBeforeTime+" "+delayedAfterTime);

                Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                ResultSet rs1 = stmt1.executeQuery("select * from train_locations where train_id='" + ti + "' and time between '" + delayedBeforeTime + "' and '" + delayedAfterTime + "'");
                rs1.last();
                int total = (rs1.getRow() / 2) - 1;
                rs1.beforeFirst();
                System.out.println(total + "total rows ");
                while (total > 0) {
                    rs1.next();
                    total--;
                }
                if (rs1.next()) {

                    String lat, lng,time;
                    lat = rs1.getString("lat");
                    lng = rs1.getString("lng");
                    time=rs1.getString("time");
                    double tlat = Double.parseDouble(lat);
                    double tlon = Double.parseDouble(lng);//1
                    double vlat = Double.parseDouble(clat);
                    double vlon = Double.parseDouble(clon);//2
                    double pi = 3.14159265358979323846;
                    double theta, dist;
                    theta = tlon - vlon;
                    dist = Math.sin((tlat * pi / 180)) * Math.sin(vlat * pi / 180) + Math.cos(tlat * pi / 180) * Math.cos(vlat * pi / 180) * Math.cos(theta * pi / 180);
                    dist = Math.acos(dist);
                    dist = dist * 180 / pi;
                    dist = dist * 60 * 1.1515;
                    dist = dist * 1.609344;
                    if (dist < mindist) {
                        flag = 1;
                        mindist = dist;
                        obj.put("train_id", ti);
                        obj.put("train_name", tn);
                        obj.put("lat", lat);
                        obj.put("lng", lng);
                        obj.put("time", time);
                    }

                }
            }
            if (flag == 1) {
                jsonArr.add(obj);
                JSONObject obj2 = new JSONObject();
                obj2.put("Trains", jsonArr);
                out.println(obj2.toJSONString());
            } else {
                out.println("No Train Nearby");
            }

        } catch (SQLException ex) {
            Logger.getLogger(jsonTrainLocs2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(jsonTrainLocs2.class.getName()).log(Level.SEVERE, null, ex);
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

//    private String addTimes(String start, String end) {
//
//        int times[] = new int[3];
//        String times1[] = start.split(":");
//        String times2[] = end.split(":");
//        int time1[] = new int[3], time2[] = new int[3];
//
//        for (int i = 0; i < 3; i++) {
//            time1[i] = (isNaN(parseInt(times1[i]))) ? 0 : parseInt(times1[i]);
//            time2[i] = (isNaN(parseInt(times2[i]))) ? 0 : parseInt(times2[i]);
//            times[i] = time1[i] + time2[i];
//        }
//
//        int seconds = times[2];
//        int minutes = times[1];
//        int hours = times[0];
//
//        if (seconds % 60 == 0) {
//            hours += seconds / 60;
//        }
//
//        if (minutes % 60 == 0) {
//            int res = minutes / 60;
//            hours += res;
//            minutes = minutes - (60 * res);
//        }
//
//        return hours + ":" + minutes + ":" + seconds;
//    }
    private String subtractTimes(String s1, String s2) {

        LocalTime t1 = LocalTime.parse(s1);
        LocalTime t2 = LocalTime.parse(s2);

        Duration d = Duration.between(t2, t1);
        //System.out.println(d); //PT-1H
        long hours = d.toHours();
        int minutes = (int) (d.toMinutes() % 60);
        int secs = (int) (d.getSeconds() % 60);
        return String.format("%02d:%02d:%02d", hours, minutes,secs);
    }

    private String addTimes(String time1, String time2) {
        String date3=null;
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            
            Date date1 = timeFormat.parse(time1);
            Date date2 = timeFormat.parse(time2);
            
            long sum = date1.getTime() + date2.getTime();
            
            date3 = timeFormat.format(new Date(sum));
            
        } catch (ParseException ex) {
            Logger.getLogger(jsonTrainLocs2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return date3;
    }
}
