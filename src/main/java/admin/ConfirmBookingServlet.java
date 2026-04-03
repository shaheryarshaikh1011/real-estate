package admin;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/confirmbooking")
public class ConfirmBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");

        if (adminId == null) {
            response.sendRedirect("admin-login.html");
            return;
        }

        try {
            int bookingId = Integer.parseInt(request.getParameter("booking_id"));

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "UPDATE bookings SET status='confirmed' WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, bookingId);
            pst.executeUpdate();
            pst.close();
            conn.close();

            response.sendRedirect("admindashboard?msg=Booking+Confirmed+Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admindashboard?msg=Error+confirming+booking");
        }
    }
}
