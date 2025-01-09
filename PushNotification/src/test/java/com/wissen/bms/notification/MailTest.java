package com.wissen.bms.notification;

import com.wissen.bms.notification.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MailTest {
	
	@Autowired
	private EmailNotificationService emailservice;
	
	@Autowired
	com.wissen.bms.notification.producer.KafkaProducer KafkaProducer;
	
	@PostConstruct
	public void sendmail() {
		
		System.out.println("In the test");
		
		String subject="mailtest";
		
		String body="this is my first mail";
		
		String respondentid="vikalppandey@gmail.com";
		//KafkaProducer.produceMockData();
		
		//emailservice.sendEmail(respondentid, subject, body);		
	}
	

}
