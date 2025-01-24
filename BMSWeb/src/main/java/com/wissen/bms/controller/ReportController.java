package com.wissen.bms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    private  RestTemplate restTemplate;
    @Value("${Api.reportingapi.url}")
    String apiUrl = "http://localhost:8081/api/faultlogs/all";

    public ReportController() {

    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/report")
    public String getReports(Authentication authentication, Model model) {
        // URL of the `report-api` (adjust the port or path as needed)
        String accessToken="Bearer";
//        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
//            accessToken = jwtAuthToken.getToken().getTokenValue();
//        } else
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            accessToken = oidcUser.getIdToken().getTokenValue(); // ID token
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Fetch reports from `report-api`


        ResponseEntity< List> reports = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        // Add the reports to the model for rendering
        model.addAttribute("reports", reports.getBody());
        return "report";
    }
}

