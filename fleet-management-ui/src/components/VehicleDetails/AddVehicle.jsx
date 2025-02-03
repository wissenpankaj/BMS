import React, { useState } from "react";
import "./css/AddVehicle.css"; // Import CSS file for styling

function AddVehicle() {
  const [formData, setFormData] = useState({
    vehicles_id: "",
    make: "",
    model: "",
    vehicles_type: "",
    battery_id: "",
  });

  const [errors, setErrors] = useState({});
  const [responseMessage, setResponseMessage] = useState("");

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
      const response = await fetch("/vehicle/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Failed to add vehicle. Please try again.");
      }

      setResponseMessage("Vehicle added successfully!");
      setFormData({ vehicles_id: "", make: "", model: "", vehicles_type: "", battery_id: "" });
      setErrors({});
    } catch (error) {
      setResponseMessage(error.message);
    }
  };

  return (
    <div className="container">
      <h2>Add New Vehicle</h2>
      {responseMessage && <p className="message">{responseMessage}</p>}

      <form onSubmit={handleSubmit}>
        {["vehicles_id", "make", "model", "vehicles_type", "battery_id"].map((field) => (
          <div key={field} className="form-group">
            <label>{field.replace("_", " ").toUpperCase()}</label>
            <input
              type="text"
              name={field}
              value={formData[field]}
              onChange={handleChange}
              className={errors[field] ? "error-input" : ""}
            />
            {errors[field] && <p className="error-message">{errors[field]}</p>}
          </div>
        ))}

        <button type="submit" className="submit-button">Submit</button>
      </form>
    </div>
  );
}

export default AddVehicle;
