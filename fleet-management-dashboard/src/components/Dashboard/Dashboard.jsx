
import React from "react";
import FleetSummary from "./FleetSummary";
import BatteryHealthOverview from "./BatteryHealthOverview";
import AlertsNotifications from "./AlertsNotifications";
import PerformanceMetrics from "./PerformanceMetrics";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();

  const fleetSummaryData = {
    totalVehicles: 100,
    activeVehicles: 85,
    vehiclesWithIssues: 15,
  };

  const batteryHealthData = [
    { name: "Healthy", value: 70 },
    { name: "Warning", value: 20 },
    { name: "Faulty", value: 10 },
  ];

  const allVehiclesPerformance = [
      {
        vehicleId: "V123",
        performanceMetrics: [
          { name: "Jan", avgBatteryLife: 80, avgDischargeRate: 2.5, avgTemp: 35 },
          { name: "Feb", avgBatteryLife: 78, avgDischargeRate: 2.6, avgTemp: 34 },
          { name: "Mar", avgBatteryLife: 79, avgDischargeRate: 2.7, avgTemp: 36 },
          { name: "Apr", avgBatteryLife: 81, avgDischargeRate: 2.5, avgTemp: 35 },
        ],
      },
      {
        vehicleId: "V124",
        performanceMetrics: [
          { name: "Jan", avgBatteryLife: 75, avgDischargeRate: 2.8, avgTemp: 37 },
          { name: "Feb", avgBatteryLife: 74, avgDischargeRate: 2.9, avgTemp: 38 },
          { name: "Mar", avgBatteryLife: 76, avgDischargeRate: 2.7, avgTemp: 36 },
          { name: "Apr", avgBatteryLife: 77, avgDischargeRate: 2.6, avgTemp: 35 },
        ],
      },
    ];



  const alerts = [
    { message: "Battery ID 123 overheating detected.", time: "10 mins ago" },
    { message: "Battery ID 456 rapid discharge detected.", time: "1 hour ago" },
  ];
  return (
    <div className="container my-4">
      <h2 className="mb-4">Dashboard Overview</h2>
      <FleetSummary data={fleetSummaryData} />
      <div className="row">
        <div className="col-md-6">
          <BatteryHealthOverview data={batteryHealthData} />
        </div>

      </div>
      {/* <PerformanceMetrics data={performanceMetrics} /> */}
      <PerformanceMetrics data={allVehiclesPerformance} />
      <button
        className="btn btn-primary mt-4"
        onClick={() => navigate("/faulty-batteries")}
      >
        View Faulty Batteries
      </button>
    </div>
  );
};

export default Dashboard;

