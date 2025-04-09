<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String clientId = "2007150725";
    String redirectUri = "http://localhost:8080/line-login-callback";
    String state = UUID.randomUUID().toString();
    session.setAttribute("oauthState", state);
%>

<html>
<head>
    <title>Circolle 登入</title>
</head>
<body>
    <h2>使用 LINE 登入</h2>
    <a href="https://access.line.me/oauth2/v2.1/authorize?response_type=code
    &client_id=2007150725
    &redirect_uri=http://localhost:8080/line-login-callback
    &state=隨機字串
    &scope=profile%20openid%20email">
        <img src="https://upload.wikimedia.org/wikipedia/commons/4/41/LINE_logo.svg" alt="使用 LINE 登入" width="150">
    </a>
</body>
</html>


