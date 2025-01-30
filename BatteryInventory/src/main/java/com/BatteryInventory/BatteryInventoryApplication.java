package com.BatteryInventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BatteryInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatteryInventoryApplication.class, args);
	}

}
