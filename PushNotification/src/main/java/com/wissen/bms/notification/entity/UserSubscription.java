package com.wissen.bms.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "user_subscription")
public class UserSubscription {
	
	@Id
	private String vehicleId;
	
	private String notificationType;
	
	private String deviceToken;
	
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

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setToken(String token) {
		this.deviceToken = token;
	}
	public String getToken (){ return
		this.deviceToken ;
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
				+ deviceToken + ", email_Id=" + email_Id + "]";
	}

		
	
	

}
