package admin;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/admindashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");

        if (adminId == null) {
            response.sendRedirect("admin-login.html");
            return;
        }

        List<Map<String, String>> bookings = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT b.id, b.booking_date, b.status, " +
                         "p.title, p.city, p.price, " +
                         "u.fullname AS buyer_name, u.email AS buyer_email " +
                         "FROM bookings b " +
                         "JOIN properties p ON b.property_id = p.id " +
                         "JOIN users u ON b.user_id = u.id " +
                         "ORDER BY b.id DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Map<String, String> booking = new HashMap<>();
                booking.put("id", rs.getString("id"));
                booking.put("title", rs.getString("title"));
                booking.put("city", rs.getString("city"));
                booking.put("price", rs.getString("price"));
                booking.put("buyer_name", rs.getString("buyer_name"));
                booking.put("buyer_email", rs.getString("buyer_email"));
                booking.put("booking_date", rs.getString("booking_date"));
                booking.put("status", rs.getString("status"));
                bookings.add(booking);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }
}
