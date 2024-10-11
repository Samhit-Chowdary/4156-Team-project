package com.nullterminators.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/** This class contains all the startup logic for our employee management application. */
@EnableJpaRepositories(basePackages = "com.nullterminators.project.repository")
@EntityScan(basePackages = "com.nullterminators.project.model")
@SpringBootApplication
public class EmployeeManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmployeeManagementApplication.class, args);
  }
}
