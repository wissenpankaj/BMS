package com.wissen.bms.reportingAPI.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class SwaggerJsonExporter {

    private static final String SWAGGER_URL = "http://localhost:8080/api-docs";

    @EventListener(ApplicationReadyEvent.class)
    public void exportSwaggerJson() {
        RestTemplate restTemplate = new RestTemplate();
        String swaggerJson = null;

        try {
            swaggerJson = restTemplate.getForObject(SWAGGER_URL, String.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch Swagger JSON: " + e.getMessage());
            return;
        }

        // Specify the file location
        File file = new File("ReportingAPI/API_Documentation.json");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(swaggerJson);
            System.out.println("Swagger JSON file generated successfully at: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
