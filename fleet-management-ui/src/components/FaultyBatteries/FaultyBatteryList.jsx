import React from "react";
import { useFetchData } from "../../hooks/useFetchData";
import { getBatteryFaults } from "../../services/faultyBatteryService";

const FaultyBatteryList = () => {
  
  const token = "your-auth-token"; // Replace with your actual token

  const { data, loading, error } = useFetchData(() => getBatteryFaults(token), [token]);

  if (loading) return <div>Loading battery faults...</div>;
  if (error) return <div>Error fetching battery faults: {error.message}</div>;

  return (
    <div>
      <h1>Battery Faults</h1>
      <table border="1" cellPadding="10" style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr>
            <th>Fault ID</th>
            <th>GPS</th>
            <th>Vehicle ID</th>
            <th>Battery ID</th>
            <th>Fault Reason</th>
            <th>Recommendation</th>
            <th>Time</th>
            <th>Level</th>
            <th>Risk</th>
          </tr>
        </thead>
        <tbody>
          {data.map((fault) => (
            <tr key={fault.faultId}>
              <td>{fault.faultId}</td>
              <td>{fault.gps}</td>
              <td>{fault.vehicleId}</td>
              <td>{fault.batteryId}</td>
              <td>{fault.faultReason}</td>
              <td>{fault.recommendation}</td>
              <td>{new Date(fault.time).toLocaleString()}</td>
              <td>{fault.level}</td>
              <td>{fault.risk}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default FaultyBatteryList;
