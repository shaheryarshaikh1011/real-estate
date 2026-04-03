package addproperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/addproperty")
public class AddPropertyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ✅ GET PARAMETERS
            String title = request.getParameter("title");
            String city = request.getParameter("city");
            String bedroomsStr = request.getParameter("bedrooms");
            String bathroomsStr = request.getParameter("bathrooms");
            String areaStr = request.getParameter("area_sqft");
            String priceStr = request.getParameter("price");
            String imageUrl = request.getParameter("image_url");

            // 🔍 DEBUG PRINT
            System.out.println("---- DEBUG DATA ----");
            System.out.println("title: " + title);
            System.out.println("city: " + city);
            System.out.println("bedrooms: " + bedroomsStr);
            System.out.println("bathrooms: " + bathroomsStr);
            System.out.println("area_sqft: " + areaStr);
            System.out.println("price: " + priceStr);
            System.out.println("image_url: " + imageUrl);
            System.out.println("--------------------");

            // ✅ FIELD-WISE VALIDATION (VERY CLEAR ERRORS)
            if (title == null || title.trim().isEmpty()) {
                response.getWriter().println("Error: Title is required!");
                return;
            }

            if (city == null || city.trim().isEmpty()) {
                response.getWriter().println("Error: City is required!");
                return;
            }

            if (bedroomsStr == null || bedroomsStr.trim().isEmpty()) {
                response.getWriter().println("Error: Bedrooms are required!");
                return;
            }

            if (bathroomsStr == null || bathroomsStr.trim().isEmpty()) {
                response.getWriter().println("Error: Bathrooms are required!");
                return;
            }

            if (areaStr == null || areaStr.trim().isEmpty()) {
                response.getWriter().println("Error: Area is required!");
                return;
            }

            if (priceStr == null || priceStr.trim().isEmpty()) {
                response.getWriter().println("Error: Price is required!");
                return;
            }

            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                response.getWriter().println("Error: Image URL is required!");
                return;
            }

            // ✅ CONVERT VALUES
            int bedrooms, bathrooms, area;
            double price;

            try {
                bedrooms = Integer.parseInt(bedroomsStr);
                bathrooms = Integer.parseInt(bathroomsStr);
                area = Integer.parseInt(areaStr);
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                response.getWriter().println("Error: Invalid number format!");
                return;
            }

            // ✅ DB CONNECTION
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // ✅ SQL (MATCHES YOUR TABLE)
            String sql = "INSERT INTO properties (title, city, bedrooms, bathrooms, area_sqft, price, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, title);
            pst.setString(2, city);
            pst.setInt(3, bedrooms);
            pst.setInt(4, bathrooms);
            pst.setInt(5, area);
            pst.setDouble(6, price);
            pst.setString(7, imageUrl);
            
            

            int row = pst.executeUpdate();

            if (row > 0) {
                response.getWriter().println("✅ Property Added Successfully!");
            } else {
                response.getWriter().println("❌ Failed to add property!");
            }

            pst.close();
            conn.close();

        }
        
        
        catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}