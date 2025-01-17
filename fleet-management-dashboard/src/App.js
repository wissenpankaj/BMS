import './App.css';
import Dashboard from './components/Dashboard/Dashboard';
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FaultyBatteriesLists from './components/Faulty Batteries/FaultyBatteriesList';
import FaultSummary from './components/Faulty Batteries/FaultSummary';
import BatteryDetailsPage from "./components/Battery Details/BatteryDetailsPage";

function App() {
  return (
    <div className="App">
     {/* <Dashboard/> */}
     <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/faulty-batteries" element={<FaultyBatteriesLists />} />
        <Route path="/fault-summary" element={<FaultSummary />} />
        <Route path="/battery/:batteryId" element={<BatteryDetailsPage />} />
      </Routes>
    </Router>
        
    </div>
  );
}

export default App;
