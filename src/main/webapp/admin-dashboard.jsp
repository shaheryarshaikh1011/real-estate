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

/* Form fields — light background so text is visible */
.prop-input {
  background-color: #1e1e2e;
  border: 1px solid #444;
  color: #fff;
  border-radius: 8px;
  padding: 10px 14px;
  width: 100%;
  font-size: 14px;
  transition: border-color 0.2s;
}
.prop-input::placeholder { color: #888; }
.prop-input:focus {
  outline: none;
  border-color: #27ae60;
  box-shadow: 0 0 0 3px rgba(39,174,96,0.2);
  background-color: #1e1e2e;
  color: #fff;
}
.prop-input.is-invalid { border-color: #e74c3c !important; }
.prop-label {
  font-size: 13px;
  color: #aaa;
  margin-bottom: 5px;
  display: block;
}
.field-error {
  color: #e74c3c;
  font-size: 12px;
  margin-top: 4px;
  display: none;
}
#imgPreview {
  max-height: 140px;
  border-radius: 8px;
  border: 1px solid #444;
  display: none;
  margin-top: 8px;
}
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
    <form action="addproperty" method="post" id="addPropertyForm" novalidate>
      <div class="row g-3">

        <div class="col-md-6">
          <label class="prop-label">Property Title *</label>
          <input type="text" name="title" id="title" class="prop-input" placeholder="e.g. Vasai Villa">
          <div class="field-error" id="titleErr">Title is required.</div>
        </div>

        <div class="col-md-6">
          <label class="prop-label">City *</label>
          <input type="text" name="city" id="city" class="prop-input" placeholder="e.g. Mumbai">
          <div class="field-error" id="cityErr">City is required.</div>
        </div>

        <div class="col-md-3">
          <label class="prop-label">Bedrooms *</label>
          <input type="number" name="bedrooms" id="bedrooms" class="prop-input" placeholder="e.g. 3" min="1">
          <div class="field-error" id="bedroomsErr">Enter a valid number (min 1).</div>
        </div>

        <div class="col-md-3">
          <label class="prop-label">Bathrooms *</label>
          <input type="number" name="bathrooms" id="bathrooms" class="prop-input" placeholder="e.g. 2" min="1">
          <div class="field-error" id="bathroomsErr">Enter a valid number (min 1).</div>
        </div>

        <div class="col-md-3">
          <label class="prop-label">Area (sqft) *</label>
          <input type="number" name="area_sqft" id="area_sqft" class="prop-input" placeholder="e.g. 1200" min="1">
          <div class="field-error" id="areaErr">Enter a valid area (min 1).</div>
        </div>

        <div class="col-md-3">
          <label class="prop-label">Price (&#8377;) *</label>
          <input type="number" step="0.01" name="price" id="price" class="prop-input" placeholder="e.g. 25000" min="0">
          <div class="field-error" id="priceErr">Enter a valid price (min 0).</div>
        </div>

        <div class="col-12">
          <label class="prop-label">Image URL *</label>
          <input type="text" name="image_url" id="image_url" class="prop-input" placeholder="https://...">
          <div class="field-error" id="imageErr">Enter a valid image URL starting with http.</div>
          <img id="imgPreview" src="" alt="Image Preview">
        </div>

        <div class="col-12 mt-2">
          <button type="submit" class="btn btn-success px-4">Add Property</button>
          <button type="reset" class="btn btn-outline-secondary px-4 ms-2" onclick="resetForm()">Clear</button>
        </div>

      </div>
    </form>
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
<script>
// Image URL live preview
document.getElementById("image_url").addEventListener("input", function () {
  const preview = document.getElementById("imgPreview");
  const val = this.value.trim();
  if (val.startsWith("http")) {
    preview.src = val;
    preview.style.display = "block";
    preview.onerror = () => { preview.style.display = "none"; };
  } else {
    preview.style.display = "none";
  }
});

// Form validation
document.getElementById("addPropertyForm").addEventListener("submit", function (e) {
  let valid = true;

  function check(id, errId, condition) {
    const el = document.getElementById(id);
    const err = document.getElementById(errId);
    if (condition) {
      el.classList.add("is-invalid");
      err.style.display = "block";
      valid = false;
    } else {
      el.classList.remove("is-invalid");
      err.style.display = "none";
    }
  }

  check("title",     "titleErr",    !document.getElementById("title").value.trim());
  check("city",      "cityErr",     !document.getElementById("city").value.trim());
  check("bedrooms",  "bedroomsErr", !document.getElementById("bedrooms").value || parseInt(document.getElementById("bedrooms").value) < 1);
  check("bathrooms", "bathroomsErr",!document.getElementById("bathrooms").value || parseInt(document.getElementById("bathrooms").value) < 1);
  check("area_sqft", "areaErr",     !document.getElementById("area_sqft").value || parseInt(document.getElementById("area_sqft").value) < 1);
  check("price",     "priceErr",    document.getElementById("price").value === "" || parseFloat(document.getElementById("price").value) < 0);
  const imgVal = document.getElementById("image_url").value.trim();
  check("image_url", "imageErr",    !imgVal || !imgVal.startsWith("http"));

  if (!valid) e.preventDefault();
});

function resetForm() {
  document.querySelectorAll(".prop-input").forEach(el => el.classList.remove("is-invalid"));
  document.querySelectorAll(".field-error").forEach(el => el.style.display = "none");
  document.getElementById("imgPreview").style.display = "none";
}
</script>
