package com.nullterminators.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** This class contains all the startup logic for our employee management application. */
@SpringBootApplication
public class EmployeeManagementApplication {

  public static void main(String[] args) {

    SpringApplication.run(EmployeeManagementApplication.class, args);
  }
}
