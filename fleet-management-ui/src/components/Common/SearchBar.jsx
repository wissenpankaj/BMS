
import React, { useState } from "react";
import "./css/SearchBar.css";

const SearchBar = ({ onSearch, placeholder }) => {
    const [query, setQuery] = useState("");

    const handleChange = (e) => {
        const value = e.target.value;
        setQuery(value);
        onSearch(value);
    };

    return (
        <div className="search-bar">
            <input
                type="text"
                placeholder={placeholder || "Search..."}
                value={query}
                onChange={handleChange}
            />
        </div>
    );
};

export default SearchBar;

/* Usage Example:
import SearchBar from "./SearchBar";

const MyComponent = () => {
    const handleSearch = (query) => {
        console.log("Searching for:", query);
    };

    return <SearchBar onSearch={handleSearch} placeholder="Search Batteries..." />;
};
*/
