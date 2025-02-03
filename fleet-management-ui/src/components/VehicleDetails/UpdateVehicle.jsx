import React, { useState, useEffect } from "react";
import "./css/UpdateVehicle.css"; // Import CSS file for styling

function UpdateVehicle() {
  const [formData, setFormData] = useState({
    vehicles_id: "",
    make: "",
    model: "",
    vehicles_type: "",
    battery_id: "",
  });

  const [errors, setErrors] = useState({});
  const [responseMessage, setResponseMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Handle input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Validate form before submission
  const validateForm = () => {
    let newErrors = {};
    if (!formData.vehicles_id.trim()) newErrors.vehicles_id = "Vehicle ID is required.";
    if (!formData.make.trim()) newErrors.make = "Make is required.";
    if (!formData.model.trim()) newErrors.model = "Model is required.";
    if (!formData.vehicles_type.trim()) newErrors.vehicles_type = "Vehicle type is required.";
    if (!formData.battery_id.trim()) newErrors.battery_id = "Battery ID is required.";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const response = await fetch("/vehicle/update", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Failed to update vehicle. Please try again.");
      }

      setResponseMessage("Vehicle updated successfully!");
      setFormData({ vehicles_id: "", make: "", model: "", vehicles_type: "", battery_id: "" });
      setErrors({});
    } catch (error) {
      setResponseMessage(error.message);
    }
  };

  // Fetch vehicle details based on Vehicle ID
  const fetchVehicleDetails = async (vehicleId) => {
    if (!vehicleId.trim()) return;

    setIsLoading(true);
    try {
      const response = await fetch(`/vehicle/get/${vehicleId}`);
      const data = await response.json();

      if (data) {
        setFormData({
          vehicles_id: data.vehicles_id,
          make: data.make,
          model: data.model,
          vehicles_type: data.vehicles_type,
          battery_id: data.battery_id,
        });
        setErrors({});
      } else {
        setErrors({ vehicles_id: "Vehicle ID not found" });
        setFormData({
          vehicles_id: vehicleId,
          make: "",
          model: "",
          vehicles_type: "",
          battery_id: "",
        });
      }
    } catch (error) {
      setErrors({ vehicles_id: "Failed to fetch vehicle details. Please try again." });
    } finally {
      setIsLoading(false);
    }
  };

  // Handle Vehicle ID input change and auto-fetch details
  const handleVehicleIdChange = (e) => {
    const { value } = e.target;
    setFormData({ ...formData, vehicles_id: value });
    if (value.trim()) {
      fetchVehicleDetails(value);
    } else {
      setFormData({
        vehicles_id: "",
        make: "",
        model: "",
        vehicles_type: "",
        battery_id: "",
      });
      setErrors({});
    }
  };

  return (
    <div className="container">
      <h2>Update Vehicle</h2>
      {responseMessage && <p className="message">{responseMessage}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Vehicle ID</label>
          <input
            type="text"
            name="vehicles_id"
            value={formData.vehicles_id}
            onChange={handleVehicleIdChange}
            className={errors.vehicles_id ? "error-input" : ""}
          />
          {errors.vehicles_id && <p className="error-message">{errors.vehicles_id}</p>}
        </div>

        {["make", "model", "vehicles_type", "battery_id"].map((field) => (
          <div key={field} className="form-group">
            <label>{field.replace("_", " ").toUpperCase()}</label>
            <input
              type="text"
              name={field}
              value={formData[field]}
              onChange={handleChange}
              disabled={isLoading || !formData.vehicles_id.trim()}
              className={errors[field] ? "error-input" : ""}
            />
            {errors[field] && <p className="error-message">{errors[field]}</p>}
          </div>
        ))}

        <button type="submit" className="submit-button" disabled={isLoading}>
          {isLoading ? "Loading..." : "Update Vehicle"}
        </button>
      </form>
    </div>
  );
}

export default UpdateVehicle;
