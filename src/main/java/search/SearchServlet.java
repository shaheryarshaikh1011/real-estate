package search;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String city = request.getParameter("city");
        String transactionType = request.getParameter("transaction_type");
        String propertyType = request.getParameter("property_type");
        String minPriceStr = request.getParameter("min_price");
        String maxPriceStr = request.getParameter("max_price");

        double minPrice = minPriceStr != null && !minPriceStr.isEmpty() ? Double.parseDouble(minPriceStr) : 0;
        double maxPrice = maxPriceStr != null && !maxPriceStr.isEmpty() ? Double.parseDouble(maxPriceStr) : Double.MAX_VALUE;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM properties WHERE 1=1";
            if (city != null && !city.isEmpty()) sql += " AND city LIKE ?";
            if (transactionType != null && !transactionType.isEmpty()) sql += " AND transaction_type=?";
            if (propertyType != null && !propertyType.isEmpty()) sql += " AND property_type=?";
            sql += " AND price BETWEEN ? AND ?";

            PreparedStatement pst = conn.prepareStatement(sql);

            int index = 1;
            if (city != null && !city.isEmpty()) pst.setString(index++, "%" + city + "%");
            if (transactionType != null && !transactionType.isEmpty()) pst.setString(index++, transactionType);
            if (propertyType != null && !propertyType.isEmpty()) pst.setString(index++, propertyType);
            pst.setDouble(index++, minPrice);
            pst.setDouble(index++, maxPrice);

            ResultSet rs = pst.executeQuery();

            response.setContentType("text/html");
            response.getWriter().println("<h2 class='text-center'>Search Results</h2>");
            while (rs.next()) {
                response.getWriter().println(
                    "<div style='border:1px solid #ccc; margin:10px; padding:10px;'>"
                    + "<h3>" + rs.getString("title") + "</h3>"
                    + "<p>City: " + rs.getString("city") + "</p>"
                    + "<p>Type: " + rs.getString("property_type") + " | " + rs.getString("transaction_type") + "</p>"
                    + "<p>Price: ₹" + rs.getDouble("price") + "</p>"
                    + "<p>Description: " + rs.getString("description") + "</p>"
                    + "</div>"
                );
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
