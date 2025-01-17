import React from "react";
import { useParams } from "react-router-dom";
import "./BatteryDetailsPage.css";
//will replace with api
const batteryData = [
  {
    id: "B001",
    vehicleId: "VEH001",
    manufacturer: "Tesla",
    model: "PowerPack 2.0",
    installationDate: "2023-06-15",
    healthStatus: "Healthy",
    faultType: null,
  },
  {
    id: "B002",
    vehicleId: "VEH002",
    manufacturer: "Panasonic",
    model: "Li-Ion 18650",
    installationDate: "2022-12-10",
    healthStatus: "Faulty",
    faultType: "Overheating",
  },
  {
    id: "B003",
    vehicleId: "VEH003",
    manufacturer: "LG Chem",
    model: "RESU 10H",
    installationDate: "2023-01-20",
    healthStatus: "Warning",
    faultType: "Rapid Discharge",
  },
];

const BatteryDetailsPage = () => {
  const { batteryId } = useParams();

 
  const battery = batteryData.find((b) => b.id === batteryId);

  if (!battery) {
    return <p>Battery not found.</p>;
  }

  return (
    <div className="battery-details">
      <h1>Battery Details</h1>
      <div className="details-card">
        <p><strong>Manufacturer:</strong> {battery.manufacturer}</p>
        <p><strong>Model:</strong> {battery.model}</p>
        <p><strong>Installation Date:</strong> {new Date(battery.installationDate).toLocaleDateString()}</p>
        <p><strong>Battery ID:</strong> {battery.id}</p>
        <p><strong>Vehicle ID:</strong> {battery.vehicleId}</p>
        <p><strong>Health Status:</strong> {battery.healthStatus}</p>
        <p><strong>Fault Type:</strong> {battery.faultType || "None"}</p>
      </div>
    </div>
  );
};

export default BatteryDetailsPage;
