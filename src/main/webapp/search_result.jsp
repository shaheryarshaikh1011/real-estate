<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Search Results</title>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  color: white;
  background:
    linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
    url('https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1470&q=80') no-repeat center center fixed;
  background-size: cover;
}

nav {
  background: rgba(0,0,0,0.9);
  color: white;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
}

nav a {
  color: white;
  text-decoration: none;
  margin-left: 15px;
}

nav a:hover {
  text-decoration: underline;
}

.container {
  padding: 40px;
}

.property-card {
  background: rgba(30, 30, 30, 0.95);
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.5);
  margin-bottom: 20px;
  transition: 0.3s;
}

.property-card:hover {
  transform: translateY(-5px);
}

.property-card h3 {
  margin-top: 0;
  color: #27ae60;
}

.property-card a.btn-book {
  display: inline-block;
  padding: 10px 20px;
  background: #27ae60;
  color: white;
  border-radius: 5px;
  text-decoration: none;
}

.property-card a.btn-book:hover {
  background: #219150;
}
</style>
</head>

<body>

<nav>
  <div><strong>RealEstate</strong></div>
  <div>
    <a href="viewproperties">Dashboard</a>
    <a href="login.html">Logout</a>
  </div>
</nav>

<div class="container">

  <h2>Search Results</h2>

  <%
    List<Map<String, String>> results =
        (List<Map<String, String>>) request.getAttribute("results");

    if (results != null && !results.isEmpty()) {
        for (Map<String, String> p : results) {
  %>

  <div class="property-card">
    <h3><%= p.get("title") %></h3>
    <p>Location: <%= p.get("city") %></p>
    <p>BHK: <%= p.get("bedrooms") %> BHK</p>
    <p>Price: &#8377;<%= p.get("price") %></p>
    <a href="bookproperty.jsp?property_id=<%= p.get("id") %>" class="btn-book">Book Now</a>
  </div>

  <%
        }
    } else {
  %>
  <p>No properties found matching your criteria.</p>
  <%
    }
  %>

</div>

</body>
</html>
