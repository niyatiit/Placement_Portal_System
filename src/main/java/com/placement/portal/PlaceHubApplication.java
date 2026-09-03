package com.placement.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlaceHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlaceHubApplication.class, args);
        System.out.println("Server is running ...");
    }
}
