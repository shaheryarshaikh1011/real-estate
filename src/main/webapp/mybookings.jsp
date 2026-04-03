<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Bookings</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
  background: linear-gradient(rgba(0,0,0,0.8), rgba(0,0,0,0.8)),
  url("https://images.unsplash.com/photo-1600596542815-ffad4c1539a9");
  background-size: cover;
  background-position: center;
  min-height: 100vh;
  color: #fff;
}

.booking-card {
  background: rgba(255,255,255,0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
}

.booking-img {
  height: 180px;
  object-fit: cover;
  border-radius: 10px 10px 0 0;
}

.badge-pending   { background-color: #f39c12; }
.badge-confirmed { background-color: #27ae60; }
.badge-cancelled { background-color: #e74c3c; }
</style>
</head>

<body>

<nav class="navbar navbar-dark bg-black px-4">
  <a class="navbar-brand">&#128203; My Bookings</a>
  <div>
    <a href="viewproperties" class="btn btn-light btn-sm me-2">Dashboard</a>
    <a href="login.html" class="btn btn-danger btn-sm">Logout</a>
  </div>
</nav>

<div class="container mt-4">

  <h2 class="mb-4">My Bookings</h2>

  <%
    List<Map<String, String>> bookings =
        (List<Map<String, String>>) request.getAttribute("bookings");

    if (bookings != null && !bookings.isEmpty()) {
  %>

  <div class="row g-4">
  <%
      for (Map<String, String> b : bookings) {
        String status = b.get("status") != null ? b.get("status") : "pending";
        String badgeClass = "badge-pending";
        if ("confirmed".equalsIgnoreCase(status)) badgeClass = "badge-confirmed";
        else if ("cancelled".equalsIgnoreCase(status)) badgeClass = "badge-cancelled";
  %>
    <div class="col-md-4">
      <div class="card booking-card text-white">
        <img src="<%= b.get("image_url") %>" class="booking-img" alt="Property Image">
        <div class="card-body">
          <h5 class="card-title"><%= b.get("title") %></h5>
          <p class="mb-1">&#128205; <%= b.get("city") %></p>
          <p class="mb-1">&#127968; <%= b.get("bedrooms") %> BHK</p>
          <p class="mb-1">&#8377; <%= b.get("price") %></p>
          <p class="mb-1">&#128197; <%= b.get("booking_date") %></p>
          <span class="badge <%= badgeClass %> mt-2"><%= status.toUpperCase() %></span>
        </div>
      </div>
    </div>
  <%
      }
  %>
  </div>

  <%
    } else {
  %>
  <div class="text-center mt-5">
    <p class="fs-5">You have no bookings yet.</p>
    <a href="viewproperties" class="btn btn-primary mt-2">Browse Properties</a>
  </div>
  <%
    }
  %>

</div>

</body>
</html>
