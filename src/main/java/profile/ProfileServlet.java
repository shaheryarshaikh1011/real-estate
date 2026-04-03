package profile;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Get user details
            String userSql = "SELECT * FROM users WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(userSql);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                request.setAttribute("name", rs.getString("fullname"));
                request.setAttribute("email", rs.getString("email"));
                request.setAttribute("userType", rs.getString("user_type"));
            }
            rs.close();
            pst.close();

            // Get bookings — collect into a List before closing the connection
            List<Map<String, String>> bookings = new ArrayList<>();
            String bookingSql = "SELECT b.booking_date, b.status, p.title, p.city, p.price " +
                                "FROM bookings b JOIN properties p ON b.property_id=p.id " +
                                "WHERE b.user_id=?";
            PreparedStatement pst2 = conn.prepareStatement(bookingSql);
            pst2.setInt(1, userId);
            ResultSet rs2 = pst2.executeQuery();
            while (rs2.next()) {
                Map<String, String> booking = new HashMap<>();
                booking.put("title", rs2.getString("title"));
                booking.put("city", rs2.getString("city"));
                booking.put("price", rs2.getString("price"));
                booking.put("booking_date", rs2.getString("booking_date"));
                booking.put("status", rs2.getString("status"));
                bookings.add(booking);
            }
            rs2.close();
            pst2.close();
            conn.close();

            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}