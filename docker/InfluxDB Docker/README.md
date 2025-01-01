# InfluxDB Docker Setup and Initialization

This repository provides the necessary configuration to run InfluxDB in a Docker container, along with a custom initialization script that sets up the schema for your InfluxDB bucket. Please follow the steps below to get started.

## Prerequisites

Ensure you have the following installed on your machine:

- **Docker**: [Download Docker](https://www.docker.com/get-started)
- **Docker Compose**: [Download Docker Compose](https://docs.docker.com/compose/install/)
- **Bash**: This guide assumes you're running the script in a Bash-compatible terminal (e.g., Git Bash, WSL, or Linux/macOS terminal).

## Setup Instructions

### 1. Clone the Repository

Clone the repository to your local machine if you haven't already:

```bash
git clone https://github.com/wissenpankaj/BMS.git
cd your-repository-folder/docker/InfluxDB Docker
```

### 2. Prepare the Docker Environment

Make sure Docker is running on your machine.

### 3. Run the Initialization Script

After ensuring Docker is up and running, simply execute the provided `main.sh` script to bring up the InfluxDB container and initialize the schema.

```bash
chmod +x main.sh
./main.sh
```

The script will perform the following actions:

- Start the Docker containers as defined in `docker-compose.yml`.
- Wait for InfluxDB to initialize.
- Execute the `scripts.sh` script to set up the required schema for your InfluxDB bucket.

### 4. Verify the Setup

Once the script completes successfully, you can verify that InfluxDB is running and the schema has been created:

- Open the Docker Desktop or run the following command to check running containers:

```bash
docker ps
```

- You can also check InfluxDB by accessing the InfluxDB UI in your browser at `http://localhost:8086`.

### 5. Additional Configuration (Optional)

If you need to modify the configuration or initialize other schemas, you can edit the `init.sh` script accordingly.

---

## Troubleshooting

If you encounter any issues, please ensure:

- Docker and Docker Compose are properly installed.
- The `docker-compose.yml` file and `init.sh` script are in the same directory.
- InfluxDB is running without errors. You can check the container logs using:

```bash
docker logs influxdb
```