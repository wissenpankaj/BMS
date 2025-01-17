
import React from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";

const COLORS = ["#4caf50", "#ff9800", "#f44336"];

const BatteryHealthOverview = ({ data }) => {
  // Calculate Overall Health
  const overallHealth = data.reduce((max, current) => (current.value > max.value ? current : max), data[0]).name;

  return (
    <div className="card mb-4">
      <div className="card-body">
        <h5 className="card-title">Battery Health Overview</h5>
        <h6 className="text-muted mb-3">
          Overall Health: <span className="text-primary">{overallHealth}</span>
        </h6>
        <div style={{ width: "100%", height: "200px" }}>
          <ResponsiveContainer>
            <PieChart>
              <Pie
                data={data}
                cx="50%"
                cy="50%"
                label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)})`}
                outerRadius={80}
                dataKey="value"
              >
                {data.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default BatteryHealthOverview;
