package com.battery_management.model;

public class Vehicle {
    private String time;
    private String id;         // Tag: Id
    private String name;       // Tag: name
    private String status;     // Field: Status
    private String issues;     // Field: Issues

    // Getters and setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
               "time='" + time + '\'' +
               ", id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", status='" + status + '\'' +
               ", issues='" + issues + '\'' +
               '}';
    }
}
