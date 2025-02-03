import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./css/PurchaseOrder.css";

const PurchaseOrder = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const salesOrder = location.state || {};  // ✅ Prevents crash if location.state is undefined

  console.log("Received Sales Order:", location.state);

  // Generate auto-filled fields
  const today = new Date().toISOString().split("T")[0]; // Current date
  const expectedDelivery = new Date();
  expectedDelivery.setDate(expectedDelivery.getDate() + 3);
  const expectedDate = expectedDelivery.toISOString().split("T")[0];

  const [formData, setFormData] = useState({
    purchaseOrderId: `PO-${Date.now()}`,
    salesOrderId: salesOrder.salesOrderId || "",  // ✅ Safely access data
    batteryType: salesOrder.batteryType || "",
    quantity: salesOrder.quantity || "",
    supplierId: "",
    orderDate: today,
    expectedDeliveryDate: expectedDate,
    updatedAt: today,
    status: "pending",
  });

  useEffect(() => {
    // Fetch suppliers from API
    fetch("/api/suppliers")
      .then((res) => res.json())
      .then((data) => setSuppliers(data))
      .catch((err) => console.error("Error fetching suppliers:", err));
  }, []);

  const [suppliers, setSuppliers] = useState([]);

  // Handle form input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.salesOrderId || !formData.supplierId) {
      alert("Please select a Supplier.");
      return;
    }

    try {
      const response = await fetch("/api/purchase-orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (!response.ok) throw new Error("Failed to create Purchase Order");

      alert("Purchase Order Created Successfully!");
      navigate("/purchase-order-history");
    } catch (error) {
      console.error("Error creating purchase order:", error);
    }
  };

  return (
    <div className="purchase-order-container">
      <h2>Create Purchase Order</h2>
      {formData.salesOrderId ? (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Purchase Order ID</label>
            <input type="text" name="purchaseOrderId" value={formData.purchaseOrderId} readOnly />
          </div>

          <div className="form-group">
            <label>Sales Order ID</label>
            <input type="text" name="salesOrderId" value={formData.salesOrderId} readOnly />
          </div>

          <div className="form-group">
            <label>Supplier</label>
            <select name="supplierId" value={formData.supplierId} onChange={handleChange} required>
              <option value="">Select Supplier</option>
              {suppliers.map((supplier) => (
                <option key={supplier.supplierId} value={supplier.supplierId}>
                  {supplier.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Battery Type</label>
            <input type="text" name="batteryType" value={formData.batteryType} readOnly />
          </div>

          <div className="form-group">
            <label>Quantity</label>
            <input type="number" name="quantity" value={formData.quantity} onChange={handleChange} required />
          </div>

          <div className="form-group">
            <label>Order Date</label>
            <input type="date" name="orderDate" value={formData.orderDate} readOnly />
          </div>

          <div className="form-group">
            <label>Expected Delivery Date</label>
            <input type="date" name="expectedDeliveryDate" value={formData.expectedDeliveryDate} readOnly />
          </div>

          <button type="submit" className="submit-button">Submit Purchase Order</button>
        </form>
      ) : (
        <p className="error-message">Error: Sales Order not found. Please select a valid sales order.</p>
      )}
    </div>
  );
};

export default PurchaseOrder;
