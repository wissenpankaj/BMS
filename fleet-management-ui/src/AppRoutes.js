import React from 'react';
import { Route, Routes } from 'react-router-dom';  // Import Routes from react-router-dom
import DashboardPage from './pages/DashboardPage';
import FaultyBatteriesPage from './pages/FaultyBatteriesPage';
import VehicleList from './components/VehicleDetails/VehicleList';
import AddVehicle from './components/VehicleDetails/AddVehicle';
import UpdateVehicle from './components/VehicleDetails/UpdateVehicle';
import SalesOrderList from './components/InventoryManagement/SalesOrderList';
import PurchaseOrder from './components/InventoryManagement/PurchaseOrder';

function AppRoutes() {
  return (
    // <Router>
      <Routes>  {/* Use Routes from react-router-dom */}
        <Route path="/dashboard" element={<DashboardPage />} />  {/* Use element instead of component */}
        <Route path="/faulty-batteries" element={<FaultyBatteriesPage />} />
        <Route path="/vehicle/vehicle-list" element={<VehicleList/>} />
        <Route path="/vehicle/add-vehicle" element={<AddVehicle/>} />
        <Route path="/vehicle/update-vehicle" element={<UpdateVehicle/>} />
        <Route path="/inventory/sales-orders" element={<SalesOrderList/>} />
        <Route path="/inventory/purchase-order" element={<PurchaseOrder/>} />

        {/* Add more routes as needed */}
      </Routes>
    // </Router>
  );
}

export default AppRoutes;  {/* Export Routes.js as AppRoutes */}
