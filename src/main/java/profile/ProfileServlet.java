package profile;


import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // ❌ Not logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // ✅ Get user details
            String userSql = "SELECT * FROM users WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(userSql);
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                request.setAttribute("name", rs.getString("fullname"));
                request.setAttribute("email", rs.getString("email"));
                request.setAttribute("userType", rs.getString("user_type"));
            }

            // ✅ Get bookings
            String bookingSql = "SELECT b.*, p.title, p.city, p.price " +
                                "FROM bookings b JOIN properties p ON b.property_id=p.id " +
                                "WHERE b.user_id=?";
            PreparedStatement pst2 = conn.prepareStatement(bookingSql);
            pst2.setInt(1, userId);

            ResultSet rs2 = pst2.executeQuery();

            request.setAttribute("bookings", rs2);

            // ✅ Forward to JSP
            request.getRequestDispatcher("profile.html").forward(request, response);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}