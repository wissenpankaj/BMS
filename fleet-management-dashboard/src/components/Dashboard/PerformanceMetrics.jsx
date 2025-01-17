import React from "react";

const PerformanceMetricsTable = ({ data }) => {
  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title">Performance Metrics Table</h5>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>Vehicle ID</th>
              <th>Month</th>
              <th>Battery Life (%)</th>
              <th>Discharge Rate</th>
              <th>Temperature (°C)</th>
            </tr>
          </thead>
          <tbody>
            {data.map((vehicle) =>
              vehicle.performanceMetrics.map((metric, index) => (
                <tr key={`${vehicle.vehicleId}-${index}`}>
                  <td>{vehicle.vehicleId}</td>
                  <td>{metric.name}</td>
                  <td>{metric.avgBatteryLife}</td>
                  <td>{metric.avgDischargeRate}</td>
                  <td>{metric.avgTemp}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );



}




export default PerformanceMetricsTable;
