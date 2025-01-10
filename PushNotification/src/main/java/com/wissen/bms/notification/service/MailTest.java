package com.wissen.bms.notification.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import jakarta.annotation.PostConstruct;
 
@Service
public class MailTest {
	@Autowired
	private EmailNotificationService emailservice;

	@PostConstruct
	public void sendmail() {
		System.out.println("In the test");
		String subject="mailtest";
		String body="this is my first mail";
		String respondentid="vikappandey@gmail.com";
		//KafkaProducer.produceMockData();
		emailservice.sendHtmlEmail(respondentid, subject, body);		
	}

 
}