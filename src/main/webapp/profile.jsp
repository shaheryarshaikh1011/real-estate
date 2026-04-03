<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Profile</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
  background: linear-gradient(rgba(0,0,0,0.85), rgba(0,0,0,0.85)),
  url("https://images.unsplash.com/photo-1600585154526-990dced4db0d");
  background-size: cover;
  color: white;
}

.profile-card {
  background: rgba(255,255,255,0.1);
  padding: 30px;
  border-radius: 20px;
}

.booking-card {
  background: rgba(255,255,255,0.1);
  padding: 15px;
  margin: 10px 0;
  border-radius: 10px;
}
</style>
</head>

<body>

<nav class="navbar navbar-dark bg-black">
  <div class="container-fluid">
    <a class="navbar-brand">&#128100; Profile</a>
    <div>
      <a href="viewproperties" class="btn btn-light">Dashboard</a>
      <a href="login.html" class="btn btn-danger">Logout</a>
    </div>
  </div>
</nav>

<div class="container mt-5">

  <!-- Profile -->
  <div class="profile-card text-center">
    <h3><%= request.getAttribute("name") %></h3>
    <p>&#128231; <%= request.getAttribute("email") %></p>
    <p>Role: <%= request.getAttribute("userType") %></p>
  </div>

  <!-- Bookings -->
  <h4 class="mt-4">My Bookings</h4>

  <%
    List<Map<String, String>> bookings =
        (List<Map<String, String>>) request.getAttribute("bookings");

    if (bookings != null && !bookings.isEmpty()) {
        for (Map<String, String> b : bookings) {
  %>
  <div class="booking-card">
    &#127968; <%= b.get("title") %> - <%= b.get("city") %><br>
    &#128176; &#8377;<%= b.get("price") %><br>
    &#128197; <%= b.get("booking_date") %><br>
    Status: <%= b.get("status") %>
  </div>
  <%
        }
    } else {
  %>
  <p class="mt-3">No bookings yet.</p>
  <%
    }
  %>

</div>

</body>
</html>
