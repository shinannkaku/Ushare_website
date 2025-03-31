package java.servlets;

public class LineLoginServlet {
    import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

@WebServlet("/line-login-callback")
public class LineLoginServlet extends HttpServlet {
    private static final String CLIENT_ID = "2007150725";
    private static final String CLIENT_SECRET = "dc1cfc79260730bad2d46b2ddb5b5db4";
    private static final String REDIRECT_URI = "http://localhost:8080/line-login-callback";
    private static final String TOKEN_URL = "https://api.line.me/oauth2/v2.1/token";
    private static final String PROFILE_URL = "https://api.line.me/v2/profile";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String returnedState = request.getParameter("state");
        HttpSession session = request.getSession();
        String storedState = (String) session.getAttribute("oauthState");

        // 驗證 state，防止 CSRF 攻擊
        if (storedState == null || !storedState.equals(returnedState)) {
            response.sendRedirect("error.html"); // 錯誤處理
            return;
        }

        // 用 code 換取 access token
        String accessToken = getAccessToken(code);
        if (accessToken == null) {
            response.sendRedirect("error.html");
            return;
        }

        // 用 access token 取得用戶資訊
        JSONObject userProfile = getUserProfile(accessToken);

        // 存入 Session，讓前端可以顯示用戶資料
        session.setAttribute("userProfile", userProfile);
        response.sendRedirect("welcome.jsp");
    }

    private String getAccessToken(String code) throws IOException {
        URL url = new URL(TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String postData = "grant_type=authorization_code"
                + "&code=" + URLEncoder.encode(code, "UTF-8")
                + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject json = new JSONObject(response.toString());
            return json.getString("access_token");
        }
    }

    private JSONObject getUserProfile(String accessToken) throws IOException {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return new JSONObject(response.toString());
        }
    }
}

}
