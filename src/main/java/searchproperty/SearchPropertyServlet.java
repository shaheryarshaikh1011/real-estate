package searchproperty;

import java.io.IOException;
import java.sql.*;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/searchproperty")
public class SearchPropertyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        try {
            // ✅ GET PARAMETERS
            String city = request.getParameter("city");
            String transactionType = request.getParameter("transaction_type");
            String propertyType = request.getParameter("property_type");
            String minPriceStr = request.getParameter("min_price");
            String maxPriceStr = request.getParameter("max_price");
            String bedroomsStr = request.getParameter("bedrooms");
            String bathroomsStr = request.getParameter("bathrooms");

            // 🔍 DEBUG
            System.out.println("city: " + city);
            System.out.println("transaction: " + transactionType);
            System.out.println("type: " + propertyType);
            System.out.println("minPrice: " + minPriceStr);
            System.out.println("maxPrice: " + maxPriceStr);
            System.out.println("bedrooms: " + bedroomsStr);
            System.out.println("bathrooms: " + bathroomsStr);

            // ✅ BASE QUERY
            String sql = "SELECT * FROM properties WHERE city=?";

            // OPTIONAL FILTERS
            if (minPriceStr != null && !minPriceStr.isEmpty()) {
                sql += " AND price >= " + minPriceStr;
            }

            if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                sql += " AND price <= " + maxPriceStr;
            }

            if (bedroomsStr != null && !bedroomsStr.isEmpty()) {
                sql += " AND bedrooms >= " + bedroomsStr;
            }

            if (bathroomsStr != null && !bathroomsStr.isEmpty()) {
                sql += " AND bathrooms >= " + bathroomsStr;
            }

            // ✅ DB CONNECTION
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, city);

            ResultSet rs = pst.executeQuery();

            // ✅ OUTPUT HTML
            response.sendRedirect("search_result.html?msg=Booked Successfully");

            while (rs.next()) {
                response.getWriter().println("<div style='border:1px solid #ccc; padding:15px; margin:10px;'>");

                response.getWriter().println("<h3>" + rs.getString("title") + "</h3>");
                response.getWriter().println("<p>City: " + rs.getString("city") + "</p>");
                response.getWriter().println("<p>Bedrooms: " + rs.getInt("bedrooms") + "</p>");
                response.getWriter().println("<p>Bathrooms: " + rs.getInt("bathrooms") + "</p>");
                response.getWriter().println("<p>Area: " + rs.getInt("area_sqft") + " sqft</p>");
                response.getWriter().println("<p>Price: ₹" + rs.getDouble("price") + "</p>");
                response.getWriter().println("<img src='" + rs.getString("image_url") + "' width='200'>");

                response.getWriter().println("</div>");
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