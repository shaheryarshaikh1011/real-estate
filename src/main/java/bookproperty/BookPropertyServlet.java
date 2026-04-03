package bookproperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dbconnection.DBConnection;

@WebServlet("/bookproperty")
public class BookPropertyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Get session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // ❌ If not logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            // ✅ Get form data
            String propertyIdStr = request.getParameter("property_id");
            String bookingDateStr = request.getParameter("booking_date");

            														

            // ✅ Convert values
            int propertyId = Integer.parseInt(propertyIdStr);
            Date bookingDate = Date.valueOf(bookingDateStr);

            // ✅ Get DB connection
            Connection con = DBConnection.getConnection();

            if (con == null) {
                response.getWriter().println("Database connection failed!");
                return;
            }

            // ✅ Correct SQL (matches your table)
            String sql = "INSERT INTO bookings (property_id, user_id, booking_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, propertyId);     // property_id
            ps.setInt(2, userId);         // user_id
            ps.setDate(3, bookingDate);   // booking_date from form
            ps.setString(4, "pending");   // ENUM value (must be lowercase)

            // ✅ Execute
            int row = ps.executeUpdate();

            if (row > 0) {
                response.sendRedirect("buyerDashboard.html?msg=Booked Successfully");
            } else {
                response.getWriter().println("Booking Failed!");
            }

            // ✅ Close
            ps.close();
            con.close();

        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid Property ID!");
        } catch (IllegalArgumentException e) {
            response.getWriter().println("Invalid Date Format!");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}