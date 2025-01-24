import React from 'react';
import './css/Sidebar.css';

const Sidebar = () => {
  return (
    <aside className="sidebar">
      <ul>
        <li><a href="/dashboard">Dashboard</a></li>
        <li><a href="/faulty-batteries">Faulty Batteries</a></li>
        <li><a href="/reports">Reports</a></li>
        <li><a href="/settings">Settings</a></li>
        <li><a href="/maintenance-logs">Maintenance Logs</a></li>
      </ul>
    </aside>
  );
};

export default Sidebar;
