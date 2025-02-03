import React, { useState } from 'react';
import './css/Header.css';  // Import CSS file

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <header className="header">
      {/* Logo and Down Arrow */}
      <div className="user-info" onClick={() => setIsMenuOpen(!isMenuOpen)}>
        <img src="/path-to-your-logo.png" alt="User Logo" className="user-logo" />
        <span className="down-arrow">▼</span>
      </div>

      {/* Profile/Logout Dropdown */}
      {isMenuOpen && (
        <div className="dropdown-menu">
          <ul>
            <li><a href="/profile">Profile</a></li>
            <li><a href="/logout">Logout</a></li>
            <li><a href="/signin">Sign In</a></li>
          </ul>
        </div>
      )}
    </header>
  );
};

export default Header;
