
import React from "react";

const FleetSummary = ({ data }) => {
  const { totalVehicles, activeVehicles, vehiclesWithIssues } = data;

  return (
    <div className="row">
      {[
        { label: "Total Vehicles", value: totalVehicles },
        { label: "Active Vehicles", value: activeVehicles },
        { label: "Vehicles with Issues", value: vehiclesWithIssues },
      ].map((item, index) => (
        <div className="col-md-4 mb-4" key={index}>
          <div className="card text-center">
            <div className="card-body">
              <h5 className="card-title">{item.label}</h5>
              <h2 className="card-text text-primary">{item.value}</h2>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default FleetSummary;


