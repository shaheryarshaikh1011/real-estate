package searchproperty;

import java.io.IOException;
import java.sql.*;
import java.util.*;

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

        try {
            String city = request.getParameter("city");
            String minPriceStr = request.getParameter("min_price");
            String maxPriceStr = request.getParameter("max_price");
            String bedroomsStr = request.getParameter("bedrooms");
            String bathroomsStr = request.getParameter("bathrooms");

            // Build parameterized query — no string concatenation of user input
            StringBuilder sql = new StringBuilder("SELECT * FROM properties WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (city != null && !city.isEmpty()) {
                sql.append(" AND city=?");
                params.add(city);
            }
            if (minPriceStr != null && !minPriceStr.isEmpty()) {
                sql.append(" AND price >= ?");
                params.add(Double.parseDouble(minPriceStr));
            }
            if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                sql.append(" AND price <= ?");
                params.add(Double.parseDouble(maxPriceStr));
            }
            if (bedroomsStr != null && !bedroomsStr.isEmpty()) {
                sql.append(" AND bedrooms >= ?");
                params.add(Integer.parseInt(bedroomsStr));
            }
            if (bathroomsStr != null && !bathroomsStr.isEmpty()) {
                sql.append(" AND bathrooms >= ?");
                params.add(Integer.parseInt(bathroomsStr));
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement pst = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pst.executeQuery();

            List<Map<String, String>> results = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> prop = new HashMap<>();
                prop.put("id", rs.getString("id"));
                prop.put("title", rs.getString("title"));
                prop.put("city", rs.getString("city"));
                prop.put("bedrooms", rs.getString("bedrooms"));
                prop.put("bathrooms", rs.getString("bathrooms"));
                prop.put("area_sqft", rs.getString("area_sqft"));
                prop.put("price", rs.getString("price"));
                prop.put("image_url", rs.getString("image_url"));
                results.add(prop);
            }

            rs.close();
            pst.close();
            conn.close();

            request.setAttribute("results", results);
            request.getRequestDispatcher("search_result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}