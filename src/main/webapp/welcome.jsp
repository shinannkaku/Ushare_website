<%@ page import="org.json.JSONObject" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>歡迎使用 circolle</title>
</head>
<body>
    <h2>LINE 登入成功！</h2>
    <%
        JSONObject userProfile = (JSONObject) session.getAttribute("userProfile");
        if (userProfile != null) {
    %>
        <p>用戶名稱：<%= userProfile.getString("displayName") %></p>
        <img src="<%= userProfile.getString("pictureUrl") %>" width="100">
    <%
        } else {
    %>
        <p>未能取得用戶資料</p>
    <%
        }
    %>
</body>
</html>
