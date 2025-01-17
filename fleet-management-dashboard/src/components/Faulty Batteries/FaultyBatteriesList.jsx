import React from "react";
import { useNavigate } from "react-router-dom";

const FaultyBatteriesLists = () => {
  const navigate = useNavigate();

  const faultyBatteries = [
    { vehicleId: "V123", batteryId: "B001", faultType: "Overheating" },
    { vehicleId: "V124", batteryId: "B002", faultType: "Rapid Discharge" },
    { vehicleId: "V125", batteryId: "B003", faultType: "Overheating" },
    { vehicleId: "V126", batteryId: "B004", faultType: "Rapid Discharge" },
  ];

  return (
    <div className="container my-4">
      <h2>Faulty Batteries Overview</h2>
      <button
        className="btn btn-secondary mb-3"
        onClick={() => navigate("/fault-summary")}
      >
        View Fault Summary
      </button>
      <button
        className="btn btn-secondary mb-3"
        onClick={() => navigate("/")}
      >
        Go to Dashboard
      </button>
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Vehicle ID</th>
            <th>Battery ID</th>
            <th>Fault Type</th>
          </tr>
        </thead>
        <tbody>
          {faultyBatteries.map((battery, index) => (
            <tr key={index}>
              <td>{battery.vehicleId}</td>
              <td>{battery.batteryId}</td>
              <td>{battery.faultType}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default FaultyBatteriesLists;
