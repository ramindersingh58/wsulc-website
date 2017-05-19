
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Raminder
 */
public class VehicleUserLogIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JSONArray jsonArr = new JSONArray();
            String un_ei, pw;
            un_ei = request.getParameter("un_ei");
            pw = request.getParameter("pw");

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing", "root", "9JarTnyxny");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("select * from vehicle_user where (username='" + un_ei + "' or email_id='" + un_ei + "') and password='" + pw + "'");
            if (rs.next()) //Row Found
            {
                String pn, ei, un;
                pn = rs.getString("personname");
                ei = rs.getString("email_id");
                un = rs.getString("username");
                JSONObject obj = new JSONObject();
                obj.put("pn", pn);
                obj.put("ei", ei);
                obj.put("un", un);
                jsonArr.add(obj);
                JSONObject obj2 = new JSONObject();
                obj2.put("success", jsonArr);
                out.println(obj2.toJSONString());

            } else {
                out.println("invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }

    }

}
