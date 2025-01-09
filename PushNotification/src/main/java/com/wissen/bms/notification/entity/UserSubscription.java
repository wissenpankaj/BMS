package com.wissen.bms.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "user_subscription")
public class UserSubscription {
	
	@Id
	private String vehicleId;
	
	private String notificationType;
	
	private String token;
	
	private String email_Id;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail_Id() {
		return email_Id;
	}

	public void setEmail_Id(String email_Id) {
		this.email_Id = email_Id;
	}

	@Override
	public String toString() {
		return "UserSubscription [vehicleId=" + vehicleId + ", notificationType=" + notificationType + ", token="
				+ token + ", email_Id=" + email_Id + "]";
	}

		
	
	

}
