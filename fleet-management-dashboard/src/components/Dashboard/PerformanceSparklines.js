import React from "react";
import { LineChart, Line, ResponsiveContainer } from "recharts";

const PerformanceSparklines = ({ data }) => {
  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title">Performance Trends</h5>
        <div className="row">
          {data.map((vehicle) => (
            <div className="col-md-4" key={vehicle.vehicleId}>
              <h6>Vehicle {vehicle.vehicleId}</h6>
              <ResponsiveContainer width="100%" height={50}>
                <LineChart data={vehicle.performanceMetrics}>
                  <Line type="monotone" dataKey="avgBatteryLife" stroke="#4caf50" />
                </LineChart>
              </ResponsiveContainer>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default PerformanceSparklines;
