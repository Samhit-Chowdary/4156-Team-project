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

### Timeoff Records Management (/timeoff):

#### GET /timeoff/{employeeId}

* Expected Input Parameters: employeeId (Integer)
* Expected Output: List of TimeOff records or error message
* Retrieves all time-off requests for a specific employee
* Upon Success: HTTP 200 Status Code is returned along with the list of time-off records in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or no time-off records are found.

#### GET /timeoff/{employeeId}/range

* Expected Input Parameters: employeeId (Integer), startDate (String), endDate (String)
* Expected Output: List of TimeOff records or error message
* Retrieves time-off requests for a specific employee within a specified date range
* Upon Success: HTTP 200 Status Code is returned along with the list of time-off records in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist, no time-off requests are found in the specified range, or if validation fails.

#### POST /timeoff/create

* Expected Input Parameters: timeOff (TimeOff)
* Expected Output: Success or failure message
* Creates a new time-off request for a specific employee
* Upon Success: HTTP 201 Status Code is returned along with the created time-off request in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or any other validation error occurs.

#### PUT /timeoff/{employeeId}/{timeOffId}/update-status

* Expected Input Parameters: employeeId (Integer), timeOffId (Integer), action (String)
* Expected Output: Success or failure message
* Updates the status of a specific time-off request
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist, the time-off request is not found, or if the action is not applicable.
