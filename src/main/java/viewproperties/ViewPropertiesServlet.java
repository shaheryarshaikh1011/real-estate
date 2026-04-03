package viewproperties;


import java.io.IOException;
import java.sql.*;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/viewproperties")
public class ViewPropertiesServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, String>> properties = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM properties";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Map<String, String> property = new HashMap<>();

                property.put("id", rs.getString("id"));
                property.put("title", rs.getString("title"));
                property.put("city", rs.getString("city"));
                property.put("bedrooms", rs.getString("bedrooms"));
                property.put("price", rs.getString("price"));
                property.put("image_url", rs.getString("image_url"));

                properties.add(property);
            }

            request.setAttribute("properties", properties);

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("buyer-dashboard.jsp").forward(request, response);
    }
}