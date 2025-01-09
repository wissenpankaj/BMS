import pandas as pd
import numpy as np
from datetime import datetime
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor, RandomForestClassifier
from sklearn.metrics import mean_squared_error, classification_report
import sqlite3  # Replace with your actual DB connector
import time
import schedule  # Library for periodic task scheduling

# Step 1: Connect to the database and fetch data
def fetch_data_from_db(db_path, table_name):
    conn = sqlite3.connect(db_path)  # Replace with your actual DB connection
    query = f"SELECT * FROM {table_name}"
    data = pd.read_sql_query(query, conn)
    conn.close()
    return data

# Step 2: Preprocess data
def preprocess_data(data):
    # Convert dates to datetime format
    data["init_date"] = pd.to_datetime(data["init_date"])
    data["fault_date"] = pd.to_datetime(data["fault_date"])
    
    # Calculate lifespan
    data["lifespan_days"] = (data["fault_date"] - data["init_date"]).dt.days

    # Encode risk_level as numerical
    risk_level_mapping = {"low": 0, "medium": 1, "high": 2}
    data["risk_level_encoded"] = data["risk_level"].map(risk_level_mapping)

    # Drop non-numeric or irrelevant columns
    features = [
        "voltage", "current", "soc", "temperature",
        "internal_resistance", "cycle_count",
        "energy_throughput", "charging_time", "risk_level_encoded"
    ]
    target = "lifespan_days"
    return data[features], data[target]

# Step 3: Train models
def train_models(X, y):
    # Split data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # Train regression model
    regressor = RandomForestRegressor(random_state=42)
    regressor.fit(X_train, y_train)
    y_pred = regressor.predict(X_test)
    print("Regression Model RMSE:", np.sqrt(mean_squared_error(y_test, y_pred)))

    # Train classification model
    classifier = RandomForestClassifier(random_state=42)
    classifier.fit(X_train, X["risk_level_encoded"])
    y_class_pred = classifier.predict(X_test)
    print("Classification Report:\n", classification_report(X_test["risk_level_encoded"], y_class_pred))

    return regressor, classifier

# Step 4: Predict and save results
def save_predictions_to_db(data, regressor, db_path, table_name):
    data["predicted_lifespan"] = regressor.predict(data)
    data["recommended_replacement_time"] = data["init_date"] + pd.to_timedelta(data["predicted_lifespan"], unit="d")
    
    conn = sqlite3.connect(db_path)  # Replace with your actual DB connection
    data.to_sql(table_name, conn, if_exists="replace", index=False)
    conn.close()

# Main workflow
def main_workflow():
    db_path = "battery_data.db"  # Replace with actual DB path
    table_name = "battery_telemetry"

    # Fetch and process data
    data = fetch_data_from_db(db_path, table_name)
    X, y = preprocess_data(data)

    # Train models
    regressor, classifier = train_models(X, y)

    # Save predictions back to the database
    save_predictions_to_db(data, regressor, db_path, "battery_predictions")

# Schedule task at user-defined intervals
def schedule_task(interval_seconds):
    print(f"Scheduling the task to run every {interval_seconds} seconds.")
    schedule.every(interval_seconds).seconds.do(main_workflow)

    while True:
        schedule.run_pending()
        time.sleep(1)

# User-defined frequency (e.g., every 3600 seconds = 1 hour)
user_defined_interval = 3600  # Replace with your desired interval in seconds
schedule_task(user_defined_interval)
