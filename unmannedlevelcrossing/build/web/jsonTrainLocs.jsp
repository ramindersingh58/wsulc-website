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
    ResultSet rs = stmt.executeQuery("select * from train_data");

    while (rs.next()) {

        int ti = rs.getInt("train_id");
        String tn = rs.getString("train_name");
        Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs1 = stmt1.executeQuery("select * from train_locations where train_id='" + ti + "' order by time desc limit 1");

        if (rs1.next()) {
            flag=1;
            String lat, lng;
            lat = rs1.getString("lat");
            lng = rs1.getString("lng");

            JSONObject obj = new JSONObject();
            obj.put("train_id", ti);
            obj.put("train_name", tn);
            obj.put("lat", lat);
            obj.put("lng", lng);
            jsonArr.add(obj);
        }
    }
    if (flag == 1) {
        JSONObject obj = new JSONObject();
        obj.put("Trains", jsonArr);
        out.println(obj.toJSONString());
    } else {
        out.println("No Train Info");
    }

%>