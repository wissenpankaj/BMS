import React from 'react';
import Layout from './components/Layout/Layout';  // Import Layout component

import AppRoutes from './AppRoutes';  //Import Routes
import './styles/global.css';  // Global styles
import './styles/variables.css'; // Variables for colors, fonts, etc.
import './styles/mixins.css';    // Reusable mixins

// Import FaultyBatteryList
import FaultyBatteryList from './components/FaultyBatteries/FaultyBatteryList';
import { useSdk } from './hooks/useSdk';

function App() {
  useSdk();
  return (
    // <div>
    //   <Layout>
    //     <AppRoutes />
    //   </Layout>
    // </div>
    <></>
  );
}

export default App;
