import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./css/SalesOrderList.css";

const SalesOrderList = () => {
  const [salesOrders, setSalesOrders] = useState([]);
  const [selectedAction, setSelectedAction] = useState({});
  const navigate = useNavigate();
// Dummy Data
useEffect(() => {
    const dummySalesOrders = [
      {
        salesOrderId: "SO001",
        stationId: "ST1001",
        batteryType: "Lithium-Ion",
        quantity: 10,
        orderDate: "2024-01-15",
        expectedDeliveryDate: "2024-01-20",
        status: "pending",
      },
      {
        salesOrderId: "SO002",
        stationId: "ST1002",
        batteryType: "Lead-Acid",
        quantity: 5,
        orderDate: "2024-01-16",
        expectedDeliveryDate: "2024-01-21",
        status: "approved",
      },
      {
        salesOrderId: "SO003",
        stationId: "ST1003",
        batteryType: "Nickel-Cadmium",
        quantity: 8,
        orderDate: "2024-01-17",
        expectedDeliveryDate: "2024-01-22",
        status: "completed",
      },
    ];
    setSalesOrders(dummySalesOrders);
  }, []);

//   useEffect(() => {
//     // Fetch sales orders from API
//     fetch("/api/sales-orders")
//       .then((res) => res.json())
//       .then((data) => setSalesOrders(data))
//       .catch((err) => console.error("Error fetching sales orders:", err));
//   }, []);

  // Handle action selection (Approve, Reject, Complete)
  const handleAction = (salesOrderId, action) => {
    fetch(`/api/sales-orders/${salesOrderId}/update`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status: action }),
    })
      .then((res) => res.json())
      .then((updatedOrder) => {
        setSalesOrders((prevOrders) =>
          prevOrders.map((order) =>
            order.salesOrderId === salesOrderId ? updatedOrder : order
          )
        );
        setSelectedAction({ ...selectedAction, [salesOrderId]: false });
      })
      .catch((err) => console.error("Error updating order:", err));
  };

  // Navigate to Purchase Order Page with sales order details
  const handlePlaceOrder = (order) => {
    if (order.status === "approved") {
      navigate("/inventory/purchase-order", { state:  order  });
    }
  };

  return (
    <div className="sales-order-container">
      <h2>Sales Orders</h2>
      <table className="sales-order-table">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Station ID</th>
            <th>Battery Type</th>
            <th>Quantity</th>
            <th>Order Date</th>
            <th>Expected Delivery</th>
            <th>Status</th>
            <th>Actions</th>
            <th>Place Order</th>
          </tr>
        </thead>
        <tbody>
          {salesOrders.map((order) => (
            <tr key={order.salesOrderId}>
              <td>{order.salesOrderId}</td>
              <td>{order.stationId}</td>
              <td>{order.batteryType}</td>
              <td>{order.quantity}</td>
              <td>{order.orderDate}</td>
              <td>{order.expectedDeliveryDate}</td>
              <td className={`status ${order.status}`}>{order.status}</td>
              <td>
                <div className="dropdown">
                  <button
                    className="action-button"
                    onClick={() =>
                      setSelectedAction({
                        ...selectedAction,
                        [order.salesOrderId]: !selectedAction[order.salesOrderId],
                      })
                    }
                  >
                    Action ▼
                  </button>
                  {selectedAction[order.salesOrderId] && (
                    <ul className="dropdown-menu">
                      <li onClick={() => handleAction(order.salesOrderId, "approved")}>Approve</li>
                      <li onClick={() => handleAction(order.salesOrderId, "rejected")}>Reject</li>
                      <li onClick={() => handleAction(order.salesOrderId, "completed")}>Complete</li>
                    </ul>
                  )}
                </div>
              </td>
              <td>
                <button
                  className={`place-order-button ${order.status === "approved" ? "" : "disabled"}`}
                  onClick={() => handlePlaceOrder(order)}
                  disabled={order.status !== "approved"}
                >
                  Place Order
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default SalesOrderList;
