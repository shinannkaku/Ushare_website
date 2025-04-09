package java.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.UUID;

public class LineLoginServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 生成隨機的 state 字串
        String state = UUID.randomUUID().toString();

        // 建立 LINE 登入 URL
        String loginUrl = "https://access.line.me/oauth2/v2.1/authorize?response_type=code" +
                          "&client_id=2007150725" +
                          "&redirect_uri=http://localhost:8080/line-login-callback" +
                          "&state=" + state + 
                          "&scope=profile%20openid%20email";

        // 將登入 URL 重定向到 LINE 登入頁面
        response.sendRedirect(loginUrl);
    }
    
}
