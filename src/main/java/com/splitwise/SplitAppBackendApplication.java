package com.splitwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SplitAppBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SplitAppBackendApplication.class, args);
    }
}
