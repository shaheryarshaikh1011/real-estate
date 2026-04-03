<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
body {
  background-color: #0f0f1a;
  color: #eee;
  min-height: 100vh;
}
.section-card {
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 15px;
  padding: 30px;
  margin-bottom: 30px;
}
.table { color: #eee; }
.badge-pending   { background-color: #f39c12; color: #000; }
.badge-confirmed { background-color: #27ae60; }
.badge-cancelled { background-color: #e74c3c; }
</style>
</head>
<body>

<nav class="navbar navbar-dark bg-black px-4">
  <a class="navbar-brand">&#9881; Admin Panel</a>
  <div>
    <span class="text-light me-3">Welcome, <%= session.getAttribute("adminName") %></span>
    <a href="admin-login.html" class="btn btn-danger btn-sm">Logout</a>
  </div>
</nav>

<div class="container mt-4">

  <%
    String msg = request.getParameter("msg");
    if (msg != null && !msg.isEmpty()) {
  %>
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    <%= msg %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
  </div>
  <% } %>

  <!-- ADD PROPERTY -->
  <div class="section-card">
    <h4 class="mb-4">&#127968; Add New Property</h4>
    <form action="addproperty" method="post">
      <div class="row g-3">
        <div class="col-md-6">
          <input type="text" name="title" class="form-control bg-dark text-white border-secondary" placeholder="Property Title" required>
        </div>
        <div class="col-md-6">
          <input type="text" name="city" class="form-control bg-dark text-white border-secondary" placeholder="City" required>
        </div>
        <div class="col-md-3">
          <input type="number" name="bedrooms" class="form-control bg-dark text-white border-secondary" placeholder="Bedrooms" min="1" required>
        </div>
        <div class="col-md-3">
          <input type="number" name="bathrooms" class="form-control bg-dark text-white border-secondary" placeholder="Bathrooms" min="1" required>
        </div>
        <div class="col-md-3">
          <input type="number" name="area_sqft" class="form-control bg-dark text-white border-secondary" placeholder="Area (sqft)" min="1" required>
        </div>
        <div class="col-md-3">
          <input type="number" step="0.01" name="price" class="form-control bg-dark text-white border-secondary" placeholder="Price (&#8377;)" min="0" required>
        </div>
        <div class="col-12">
          <input type="url" name="image_url" class="form-control bg-dark text-white border-secondary" placeholder="Image URL" required>
        </div>
        <div class="col-12">
          <button type="submit" class="btn btn-success px-4">Add Property</button>
        </div>
      </div>
    </form>
  </div>

  <!-- ALL BOOKINGS -->
  <div class="section-card">
    <h4 class="mb-4">&#128203; All Bookings</h4>
    <%
      List<Map<String, String>> bookings =
          (List<Map<String, String>>) request.getAttribute("bookings");

      if (bookings != null && !bookings.isEmpty()) {
    %>
    <div class="table-responsive">
      <table class="table table-dark table-striped table-bordered align-middle">
        <thead class="table-secondary text-dark">
          <tr>
            <th>#</th>
            <th>Property</th>
            <th>City</th>
            <th>Price (&#8377;)</th>
            <th>Buyer</th>
            <th>Email</th>
            <th>Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        <%
          for (Map<String, String> b : bookings) {
            String status = b.get("status") != null ? b.get("status") : "pending";
            String badgeClass = "badge-pending";
            if ("confirmed".equalsIgnoreCase(status)) badgeClass = "badge-confirmed";
            else if ("cancelled".equalsIgnoreCase(status)) badgeClass = "badge-cancelled";
        %>
          <tr>
            <td><%= b.get("id") %></td>
            <td><%= b.get("title") %></td>
            <td><%= b.get("city") %></td>
            <td><%= b.get("price") %></td>
            <td><%= b.get("buyer_name") %></td>
            <td><%= b.get("buyer_email") %></td>
            <td><%= b.get("booking_date") %></td>
            <td><span class="badge <%= badgeClass %>"><%= status.toUpperCase() %></span></td>
            <td>
              <% if (!"confirmed".equalsIgnoreCase(status)) { %>
              <form action="confirmbooking" method="post">
                <input type="hidden" name="booking_id" value="<%= b.get("id") %>">
                <button type="submit" class="btn btn-success btn-sm">Confirm</button>
              </form>
              <% } else { %>
              <span class="text-success fw-bold">&#10003; Done</span>
              <% } %>
            </td>
          </tr>
        <%
          }
        %>
        </tbody>
      </table>
    </div>
    <%
      } else {
    %>
    <p class="text-muted">No bookings yet.</p>
    <%
      }
    %>
  </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
