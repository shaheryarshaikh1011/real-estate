package admin;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM users WHERE email=? AND password=? AND user_type='admin'";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("adminId", rs.getInt("id"));
                session.setAttribute("adminName", rs.getString("fullname"));
                response.sendRedirect("admindashboard");
            } else {
                response.sendRedirect("admin-login.html?error=1");
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
