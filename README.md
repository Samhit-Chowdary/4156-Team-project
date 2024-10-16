# 4156-Team-project
This is the repository for the team project of the team Null Terminators.

Team Members:

Abhilash Ganga (ag4797), Ajit Sharma Kasturi (ak5055), Hamsitha Challagundla (hc3540), Madhura Chatterjee (mc5470),  Samhit Chowdary Bhogavalli (sb4845)


## Dependencies

In order to build and use our service you must install the following:

* Maven 3.9.9: https://maven.apache.org/download.cgi
* JDK 17: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
* IDE: VSCode / IntelliJ

## Testing

All our unit tests for this project are located in the directory "src/test". Run `mvn test` to run the tests.

## Style Check

Command: `mvn checkstyle:check`

## Debugging

Static bug analyzer used: PMD (https://pmd.github.io)

Steps followed:
- Download pmd
- Follow steps under "Quick Start" in https://pmd.github.io
- Run: pmd check -f text -R rulesets/java/quickstart.xml -d /path-to-directory-with-files

## Endpoints

This section describes the endpoints that our service provides, as well as their inputs and outputs.

### Employee Profile Management (/employeeProfile):

#### GET/getAllEmployees, GET/

* Returns a String containing the list of all existing employee profiles.

#### GET/employeeProfile/{id}

* Expected Input Parameters: id (int)
* Expected Output: Details of the specified employee (String)
* Returns the employee profile by id
* Upon Success: HTTP 200 Status Code is returned along with details of the employee profile in the response body.
* Upon Failure: HTTP 404 Status Code is returned along with appropriate message in the response body.

#### POST/employeeProfile/createNewEmployee

* Expected Input Parameters: employeeProfile (EmployeeProfile)
* Expected Output: Success or failure message
* Creates new employee profile
* Upon Success: HTTP 200 Status Code is returned along with "Employee profile created successfully." in the response body.
* Upon Failure: HTTP 404 Status Code is returned along with appropriate message in the response body.

#### DELETE/employeeProfile/{id}

* Expected Input Parameters: id (int)
* Expected Output: Success or failure message
* Deletes existing employee profile
* Upon Success: HTTP 200 Status Code is returned along with "Employee profile successfully deleted." in the response body.
* Upon Failure: HTTP 404 Status Code is returned along with appropriate message in the response body.