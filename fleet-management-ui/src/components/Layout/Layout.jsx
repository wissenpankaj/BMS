import React from 'react';
import Header from './Header';
import Sidebar from './Sidebar';
import Footer from './Footer';
import './css/Layout.css';

const Layout = ({ children }) => {
  return (
    <div className="layout">
      <Header />
      <div className="main-content">
        {/* <Sidebar /> */}
        <div className="content">{children}</div>
      </div>
      <Footer />
    </div>
  );
};

export default Layout;
