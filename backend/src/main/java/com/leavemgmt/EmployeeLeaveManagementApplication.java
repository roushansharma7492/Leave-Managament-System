package com.leavemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * EmployeeLeaveManagementApplication - Main Spring Boot Application
 */
@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "com.leavemgmt")
public class EmployeeLeaveManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeLeaveManagementApplication.class, args);
    }
}
