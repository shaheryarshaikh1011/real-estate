package admin;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AdminRegisterServlet")
public class AdminRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "INSERT INTO users (fullname, email, password, user_type) VALUES (?, ?, ?, 'admin')";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, fullname);
            pst.setString(2, email);
            pst.setString(3, password);

            int row = pst.executeUpdate();
            pst.close();
            conn.close();

            if (row > 0) {
                response.sendRedirect("admin-login.html?registered=1");
            } else {
                response.sendRedirect("admin-register.html?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-register.html?error=1");
        }
    }
}
