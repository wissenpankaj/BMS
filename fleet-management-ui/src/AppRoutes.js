import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';  // Import Routes from react-router-dom
import DashboardPage from './pages/DashboardPage';
import FaultyBatteriesPage from './pages/FaultyBatteriesPage';

function AppRoutes() {
  return (
    <Router>
      <Routes>  {/* Use Routes from react-router-dom */}
        <Route path="/dashboard" element={<DashboardPage />} />  {/* Use element instead of component */}
        <Route path="/faulty-batteries" element={<FaultyBatteriesPage />} />
        {/* Add more routes as needed */}
      </Routes>
    </Router>
  );
}

export default AppRoutes;  {/* Export Routes.js as AppRoutes */}
