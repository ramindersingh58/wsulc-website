<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.sql.*" %>
<%
    //JDBC CODE HERE
    int flag = 0;
    JSONArray jsonArr = new JSONArray();
    double mindist = 1000.0;
    JSONObject obj = new JSONObject();
    String clat, clon;
    clat = request.getParameter("lat");
    clon = request.getParameter("lon");
    System.out.println("from android" + clat + clon);
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing", "root", "9JarTnyxny");

    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("select * from level_crossing");

    while (rs.next()) {

        int li = rs.getInt("level_id");
        String ln = rs.getString("level_name");

        String lat, lng;
        lat = rs.getString("lat");
        lng = rs.getString("lng");
        double llat = Double.parseDouble(lat);
        double llon = Double.parseDouble(lng);//1
        double vlat = Double.parseDouble(clat);
        double vlon = Double.parseDouble(clon);//2
        double pi = 3.14159265358979323846;
        double theta, dist;
        theta = llon - vlon;
        dist = Math.sin((llat * pi / 180)) * Math.sin(vlat * pi / 180) + Math.cos(llat * pi / 180) * Math.cos(vlat * pi / 180) * Math.cos(theta * pi / 180);
        dist = Math.acos(dist);
        dist = dist * 180 / pi;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        System.out.println(" " + llat + " " + llon + " " + vlat + " " + vlon + " " + dist);
        if (dist < mindist) {
            flag = 1;
            mindist = dist;
            obj.put("level_id", li);
            obj.put("level_name", ln);
            obj.put("lat", lat);
            obj.put("lng", lng);
        }
    }
    if (flag == 1) {
        jsonArr.add(obj);
        JSONObject obj2 = new JSONObject();
        obj2.put("Levels", jsonArr);
        out.println(obj2.toJSONString());
    } else {
        out.println("No Level Nearby");
    }

%>