import React from "react";
import { useNavigate } from "react-router-dom";

const FaultSummary = () => {
  const navigate = useNavigate();

  const faultSummaryData = [
    { faultType: "Overheating", count: 2 },
    { faultType: "Rapid Discharge", count: 2 },
  ];

  return (
    <div className="container my-4">
      <h2>Fault Summary</h2>
      <button
        className="btn btn-secondary mb-3"
        onClick={() => navigate("/faulty-batteries")}
      >
        Back to Faulty Batteries
      </button>
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Fault Type</th>
            <th>Count</th>
          </tr>
        </thead>
        <tbody>
          {faultSummaryData.map((fault, index) => (
            <tr key={index}>
              <td>{fault.faultType}</td>
              <td>{fault.count}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default FaultSummary;
