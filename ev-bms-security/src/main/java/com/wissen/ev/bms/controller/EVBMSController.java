package com.wissen.ev.bms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1")
@RestController
public class EVBMSController {

	@GetMapping("/helloUser")
	public String helloUser()
	{
		
		return "helloUser";
	}
	
	@GetMapping("/helloAdmin")
	public String helloAdmin()
	{
		
		return "helloAdmin";
	}
	
}
