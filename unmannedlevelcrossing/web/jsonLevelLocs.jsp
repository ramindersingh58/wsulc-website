<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.sql.*" %>
<%
    //JDBC CODE HERE
    int flag = 0;
    JSONArray jsonArr = new JSONArray();
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://mysql31238-ulcws.cloud.cms500.com/unmannedlevelcrossing", "root", "9JarTnyxny");

    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("select * from level_crossing");

    while (rs.next()) {
        flag=1;

        int li = rs.getInt("level_id");
        String ln = rs.getString("level_name");

        String lat, lng;
        lat = rs.getString("lat");
        lng = rs.getString("lng");

        JSONObject obj = new JSONObject();
        obj.put("level_id", li);
        obj.put("level_name", ln);
        obj.put("lat", lat);
        obj.put("lng", lng);

        jsonArr.add(obj);

    }
    if (flag == 1) {
        JSONObject obj = new JSONObject();
        obj.put("Levels", jsonArr);
        out.println(obj.toJSONString());
    } else {
        out.println("No Level Info");
    }

%>