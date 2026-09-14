package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EducationmanagementApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                EducationmanagementApplication.class,
                args
        );
    }
}