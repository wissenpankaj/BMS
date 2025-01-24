import React from 'react';
import './css/Header.css';

const Header = () => {
  return (
    <header className="header">
      <div className="logo">
        <h1>Fleet Management</h1>
      </div>
      <nav>
        <ul>
        <li><a href="/dashboard">Dashboard</a></li>
        <li><a href="/faulty-batteries">Faulty Batteries</a></li>
        <li><a href="/reports">Reports</a></li>
        <li><a href="/settings">Settings</a></li>
        <li><a href="/maintenance-logs">Maintenance Logs</a></li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
