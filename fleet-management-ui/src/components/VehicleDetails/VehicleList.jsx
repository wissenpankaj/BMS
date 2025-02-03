
import React, { useState, useEffect } from "react";
import SearchBar from "../Common/SearchBar";
import "./css/VehicleList.css";
import fetchData from "../../hooks/useFetchData"; // Importing custom hook
import vehicleService from "../../services/vehicleService"; // Importing API service

const VehicleList = () => {
    const [vehicles, setVehicles] = useState([]);
    const [filteredVehicles, setFilteredVehicles] = useState([]);

    // // Fetch vehicle data from API
    // useEffect(() => {
    //     const fetchVehicles = async () => {
    //         try {
    //             const data = await fetchData(vehicleService.getAllVehicles);
    //             setVehicles(data);
    //             setFilteredVehicles(data);
    //         } catch (error) {
    //             console.error("Error fetching vehicles:", error);
    //         }
    //     };
    //     fetchVehicles();
    // }, []);

   

    // Dummy Data (Replace with API call later)
    useEffect(() => {
        const dummyVehicles = [
            { vehicles_id: "V1", make: "Toyota", model: "Corolla", vehicles_type: "Sedan", battery_id: "B123" },
            { vehicles_id: "V2", make: "Tesla", model: "Model 3", vehicles_type: "Electric", battery_id: "B456" },
            { vehicles_id: "V3", make: "Ford", model: "F-150", vehicles_type: "Truck", battery_id: "B789" },
        ];
        setVehicles(dummyVehicles);
        setFilteredVehicles(dummyVehicles);
    }, []);

    // Handle search filtering
    const handleSearch = (query) => {
        const filtered = vehicles.filter(vehicle =>
            Object.values(vehicle).some(value =>
                value.toLowerCase().includes(query.toLowerCase())
            )
        );
        setFilteredVehicles(filtered);
    };

    return (
        <div className="vehicle-list">
            <h2>All Vehicles</h2>
            <SearchBar onSearch={handleSearch} />
            <table>
                <thead>
                    <tr>
                        <th>Vehicle ID</th>
                        <th>Make</th>
                        <th>Model</th>
                        <th>Type</th>
                        <th>Battery ID</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredVehicles.map(vehicle => (
                        <tr key={vehicle.vehicles_id}>
                            <td>{vehicle.vehicles_id}</td>
                            <td>{vehicle.make}</td>
                            <td>{vehicle.model}</td>
                            <td>{vehicle.vehicles_type}</td>
                            <td>{vehicle.battery_id}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default VehicleList;