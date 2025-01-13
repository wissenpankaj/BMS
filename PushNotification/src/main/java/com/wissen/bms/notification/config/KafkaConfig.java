package com.wissen.bms.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class KafkaConfig {
 
    @Bean
    public RecordMessageConverter messageConverter() {
        return new JsonMessageConverter();
    }
}