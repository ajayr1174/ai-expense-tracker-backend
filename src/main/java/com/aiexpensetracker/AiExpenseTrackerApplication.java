package com.aiexpensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AiExpenseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiExpenseTrackerApplication.class, args);
    }

}
