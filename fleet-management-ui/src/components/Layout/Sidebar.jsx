import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './css/Sidebar.css';

const Sidebar = () => {
  const [isVehicleMenuOpen, setIsVehicleMenuOpen] = useState(false);
  const [isInventoryMenuOpen, setIsInventoryMenuOpen] = useState(false);

  return (
    <aside className="sidebar">
      {/* Project Name */}
      <div className="sidebar-header">
        <h2>EV Fleet Management</h2>
      </div>

      {/* Main Menu Items */}
      <ul>
        <li><Link to="/dashboard">Dashboard</Link></li>
        <li><Link to="/faulty-batteries">Faulty Batteries</Link></li>
        
        {/* Vehicle Management Menu */}
        <li>
          <button className="menu-toggle" onClick={() => setIsVehicleMenuOpen(!isVehicleMenuOpen)}>
            Vehicle Management
          </button>
          {isVehicleMenuOpen && (
            <ul className="submenu">
              <li><Link to="/vehicle/vehicle-list">Get All Vehicle</Link></li>
              <li><Link to="/vehicle/add-vehicle">Add New Vehicle</Link></li>
              <li><Link to="/vehicle/update-vehicle">Update Vehicle</Link></li>
            </ul>
          )}
        </li>

        {/* Inventory Management Menu */}
        <li>
          <button className="menu-toggle" onClick={() => setIsInventoryMenuOpen(!isInventoryMenuOpen)}>
            Inventory Management
          </button>
          {isInventoryMenuOpen && (
            <ul className="submenu">
              <li><Link to="/inventory/sales-orders">Sales Orders</Link></li>
              <li><Link to="/inventory/purchase-history">Purchase Order History</Link></li>
              <li><Link to="/inventory/battery-stock">Battery Stock</Link></li>
            </ul>
          )}
        </li>

        <li><Link to="/reports">Reports</Link></li>
        <li><Link to="/settings">Settings</Link></li>
        <li><Link to="/maintenance-logs">Maintenance Logs</Link></li>
      </ul>
    </aside>
  );
};

export default Sidebar;
