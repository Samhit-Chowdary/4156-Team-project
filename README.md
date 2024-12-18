# 4156-Team-project
This is the repository for the team project of the team Null Terminators.

Team Members:

Abhilash Ganga (ag4797), Ajit Sharma Kasturi (ak5055), Hamsitha Challagundla (hc3540), Madhura Chatterjee (mc5470),  Samhit Chowdary Bhogavalli (sb4845)

## Viewing the Client Repository
Please use the following link to view the repository relevant to the client: https://github.com/Samhit-Chowdary/4156-Team-project-client

This service is deployed and can be accessed using the following link: http://104.197.175.33:8080.

## Dependencies

In order to build and use our service you must install the following:

* Maven 3.9.9: https://maven.apache.org/download.cgi
* JDK 17: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
* IDE: VSCode / IntelliJ

## Testing

All our unit tests for this project are located in the directory "src/test". Run `mvn test` to run the tests.

## Style Check

Command: `mvn checkstyle:check`

## Static Code Analysis

To generate the static code analysis report, run the following command: 

```bash
mvn pmd:check
```
You can find the report at `target/pmd.xml`

We used the following plugin for pmd static bug analyzer.
```declarative
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.25.0</version>
    <configuration>
        <rulesets> <ruleset>/rulesets/java/maven-pmd-plugin-default.xml</ruleset> </rulesets>
    </configuration>
</plugin>
```

## CI Pipeline
The CI pipeline is defined in the `.github/workflows/main.yml` file, which automatically builds the project and runs various tests, including:

* Style Check using Checkstyle.
* Static Code Analysis using PMD.
* Branch Coverage using Jacoco.
* API Testing using Postman and Newman.

## CI Build Report

The CI build reports can be found here: https://github.com/Samhit-Chowdary/4156-Team-project/actions
(One of the build reports can be found here: https://github.com/Samhit-Chowdary/4156-Team-project/actions/runs/12201880494/job/34041277308)

## JIRA Board

The JIRA board for the project can be found here: https://null-terminators.atlassian.net/jira/software/projects/SCRUM/boards/1

## Endpoints

This section describes the endpoints that our service provides, as well as their inputs and outputs.

### Authentication:

All API's listed below except for registerCompany are protected by basic authentication. the client (Company) must provide a valid username and password to access the API's and each client can only access data which they have created and cannot access other clients data.

### Company Management:

POST /registerCompany
* Expected Input Parameters: company (Company) {username, password, name, address, state}
* Expected Output: Success or failure message
* Registers a new company with given details.
* Upon Success: HTTP 201 Status Code is returned along with "Company is registered successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with appropriate message in the response body.

POST /company/changePassword
* Expected Input Parameters: new password (String)
* Expected Output: Success or failure message
* Changes the password of the company.
* Upon Success: HTTP 200 Status Code is returned along with "Password changed successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with appropriate message in the response body.

### Employee Profile Management (/employeeProfile):

#### GET /getAllEmployees, GET/

* Returns a String containing the list of all existing employee profiles.

#### GET /employeeProfile/{id}

* Expected Input Parameters: id (int)
* Expected Output: Details of the specified employee (String)
* Returns the employee profile by id
* Upon Success: HTTP 200 Status Code is returned along with details of the employee profile in the response body.
* Upon Failure: HTTP 404 Status Code is returned along with appropriate message in the response body.

#### POST /employeeProfile/createNewEmployee

* Expected Input Parameters: employeeProfile (EmployeeProfile)
* Expected Output: Success or failure message
* Creates new employee profile
* Upon Success: HTTP 200 Status Code is returned along with "Employee profile created successfully." in the response body.
* Upon Failure: HTTP 404 Status Code is returned along with appropriate message in the response body.

#### PATCH /employeeProfile/{id}/updateName

* Expected Input Parameters: employee id (int), employee's name (String)
* Expected Output: Success or failure message
* Updates name of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee name updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### PATCH /employeeProfile/{id}/updateEmailId

* Expected Input Parameters: employee id (int), employee's email id (String)
* Expected Output: Success or failure message
* Updates email-Id of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee email-id updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### PATCH /employeeProfile/{id}/updateDesignation

* Expected Input Parameters: employee id (int), employee's email id (String)
* Expected Output: Success or failure message
* Updates designation of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee designation updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### PATCH /employeeProfile/{id}/updatePhoneNumber

* Expected Input Parameters: employee id (int), employee's phone number (String)
* Expected Output: Success or failure message
* Updates phone number of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee phone number updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### PATCH /employeeProfile/{id}/updateBaseSalary

* Expected Input Parameters: employee id (int), employee's Base Salary (int)
* Expected Output: Success or failure message
* Updates phone number of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee base salary updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### PATCH /employeeProfile/{id}/updateEmergencyContact

* Expected Input Parameters: employee id (int), employee's Emergency Contact (string)
* Expected Output: Success or failure message
* Updates phone number of an existing employee
* Upon Success: HTTP 200 Status Code is returned along with "Employee emergency contact updated successfully." in the response body.
* Upon Failure: HTTP 400 Status Code is returned along with "Employee not found." in the response body.

#### DELETE /employeeProfile/{id}

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
* Query Parameter: startDate & endDate (YYYY-MM-DD)
* Expected Output: List of TimeOff records or error message
* Retrieves time-off requests for a specific employee within a specified date range
* Upon Success: HTTP 200 Status Code is returned along with the list of time-off records in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist, no time-off requests are found in the specified range, or if validation fails.

#### POST /timeoff/create

* Expected Input - Request body: timeOff (TimeOff) 
* Expected Output: Success or failure message
* Creates a new time-off request for a specific employee
* Upon Success: HTTP 201 Status Code is returned along with the created time-off request in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or any other validation error occurs.

#### PUT /timeoff/{employeeId}/{timeOffId}/update-status
* Expected Input Parameters: employeeId (Integer), timeOffId (Integer), action (String) (approve/reject or cancel)
* Query Parameter: action (String) (approve/reject or cancel)
* Expected Output: Success or failure message
* Updates the status of a specific time-off request
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist, the time-off request is not found, or if the action is not applicable.

#### DELETE /timeoff/{employeeId}/{timeOffId}
* Expected Input Parameters: employeeId (Integer), timeOffId (Integer)
* Expected Output: Success or failure message
* Deletes a specific time-off request for an employee
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or the time-off request is not found. HTTP 400 Status Code is returned if the `timeOffId` is invalid (e.g., null or non-positive).

### Payroll Management (/payroll):

#### GET /payroll/{employeeId}

* Expected Input Parameters: employeeId (Integer)
* Expected Output: List of Payroll records or error message
* Retrieves all payroll records for a specific employee which includes the salary, deductions and net pay
* Upon Success: HTTP 200 Status Code is returned along with the list of payroll records in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or no records are found.

#### PATCH /payroll/{employeeId}/markPaid

* Expected Input Parameters: employeeId (Integer), month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Marks a specific payroll record as paid for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if payroll for the month and year does not exist or if the flag is already set and HTTP 400 Status Code if month and year are missing or not in the correct format.

#### PATCH /payroll/{employeeId}/markUnpaid

* Expected Input Parameters: employeeId (Integer), month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Marks a specific payroll record as unpaid for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if payroll for the month and year does not exist or if the flag is already not set and HTTP 400 Status Code if month and year are missing or not in the correct format.

#### PATCH /payroll/{employeeId}/adjustSalary

* Expected Input Parameters: employeeId (Integer), month (Integer) in MM, year (Integer) in YYYY, salary (Integer) in request body (JSON format).
* Expected Output: Success or failure message
* Adjusts the salary for a specific employee for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if payroll for the month and year does not exist and HTTP 400 Status Code if month, year and salary are missing or not in the correct format.

#### PATCH /payroll/{employeeId}/adjustDay

* Expected Input Parameters: employeeId (Integer), day (Integer) in DD, month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Adjusts the payment day for a specific employee for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if payroll for the month and year does not exist and HTTP 400 Status Code if day, month, and year are missing or in the correct format.

#### POST /payroll/{employeeId}/addPayroll

* Expected Input Parameters: employeeId (Integer), day (Integer) in DD, month (Integer) in MM, year (Integer) in YYYY and salary (Integer) in request body (JSON format).
* Expected Output: Success or failure message
* Creates a new payroll record for a specific employee
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist and HTTP 409 Status Code if the payroll for the month and year already exists and HTTP 400 Status Code day, month, year and salary are missing or not in the correct format.

#### DELETE /payroll/{employeeId}/deletePayroll

* Expected Input Parameters: employeeId (Integer), month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Deletes a specific payroll record for a specific employee
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if payroll for the month and year does not exist and HTTP 400 Status Code if day, month, and year are missing or in the correct format.

#### POST /payroll/generatePayroll

* Expected Input Parameters: month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Generates payroll records for all employees in the company for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 400 Status Code is returned if month and year are missing or not in the correct format.

#### DELETE /payroll/deletePayroll

* Expected Input Parameters: month (Integer) in MM, year (Integer) in YYYY in request body (JSON format).
* Expected Output: Success or failure message
* Deletes all payroll records for all employees in the company for a given month and year
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 400 Status Code is returned if month and year are missing or not in the correct format.

### Employee Hierarchy (/api/employee-hierarchy)

#### POST /addEdge/{supervisorId}/{employeeId}

* Expected Input Parameters: supervisorId (Integer), employeeId (Integer)
* Expected Output: Success or failure message
* Adds a new edge in the employee hierarchy
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist and HTTP 400 Status Code if the supervisor is not an employee and HTTP 409 Status Code if the edge already exists.

#### GET /subordinates/{fromEmployeeId}

* Expected Input Parameters: fromEmployeeId (Integer)
* Expected Output: List of subordinates of the employee or error message
* Retrieves all subordinates of a specific employee
* Upon Success: HTTP 200 Status Code is returned along with the list of subordinates in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist.

#### GET /supervisor/{toEmployeeId}

* Expected Input Parameters: toEmployeeId (Integer)
* Expected Output: Supervisor of the employee or error message
* Retrieves the supervisor of a specific employee
* Upon Success: HTTP 200 Status Code is returned along with the supervisor in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist or if the employee has no supervisor.

#### GET /tree/{employeeId}

* Expected Input Parameters: employeeId (Integer)
* Expected Output: The tree of employees with the specified employee as the root or error message
* Retrieves the tree of employees with the specified employee as the root
* Upon Success: HTTP 200 Status Code is returned along with the tree in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist.

#### DELETE /removeEdge/{employeeId}

* Expected Input Parameters: employeeId (Integer)
* Expected Output: Success or failure message
* Deletes the edge from a specific employee to their supervisor
* Upon Success: HTTP 200 Status Code is returned along with a success message in the response body.
* Upon Failure: HTTP 404 Status Code is returned if the employee does not exist and HTTP 400 Status Code if the employee is not an employee and HTTP 409 Status Code if the edge does not exist.

## Internal Integration Tests

This section provides details on the internal integration tests in the `com.nullterminators.project.integration.internal` package. These tests ensure the controller integrates correctly with its dependencies, including repositories and services.
Unlike **unit tests**, which test individual components in isolation, **internal integration tests** validate how different components work together.
Our internal  integration tests are:
1. **Controller-Centric**: The tests directly call the controller methods and validate their responses.
2. **Dependency Interaction**: The tests mock the behavior of repositories (`EmployeeHierarchyRepository`, `CompanyEmployeesRepository`, `PayrollRepository`, etc) to simulate database interactions.
3. **Focus on Logical Flow**: These tests check if the controller's business logic integrates correctly with mocked dependencies, ensuring the controller's endpoints handle input, invoke the correct service or repository methods, and return expected responses.
 
### Our internal integration tests:
#### 1. **`testGetSubordinatesSuccess`**
- **Purpose**: Validates that the `getSubordinates` endpoint successfully retrieves a supervisor's subordinates.
- **Integration Points**:
    - **`EmployeeHierarchyRepository`**: Simulates fetching subordinates by supervisor ID.
- **Setup**:
    - Mock data for subordinates is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains the correct list of subordinates.
    - The number of subordinates matches the expected value.

#### 2. **`testAddEmployeeSupervisorEdgeSuccess`**
- **Purpose**: Verifies that an edge between an employee and a supervisor is successfully created when valid data is provided.
- **Integration Points**:
    - **`EmployeeHierarchyRepository`**: Checks for existing supervisors and subtree data to prevent cycles.
- **Setup**:
    - Mocks simulate no existing supervisor for the employee and no cycle in the hierarchy.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Edge added successfully."`

#### 3. **`testAddEmployeeSupervisorEdgeFailureCycle`**
- **Purpose**: Ensures the system prevents adding an edge that would create a cycle in the hierarchy.
- **Integration Points**:
    - **`EmployeeHierarchyRepository`**: Simulates a condition where a cycle would occur if the edge is added.
- **Setup**:
    - Mock repository returns a subtree indicating a cycle.
- **Assertions**:
    - Response status code is `400 Bad Request`.
    - The response body contains the error message: `"Operation failed: Employee already has a supervisor, or adding this edge would create a cycle."`

#### 4. **`testGetSubordinatesFailure`**
- **Purpose**: Validates that the `getSubordinates` endpoint correctly handles a scenario where the supervisor ID does not exist.
- **Integration Points**:
    - **`EmployeeHierarchyRepository`**: Simulates a case where no subordinates are found for the given supervisor ID.
- **Setup**:
    - Mock repository returns an empty result for the nonexistent supervisor ID.
- **Assertions**:
    - Response status code is `404 Not Found`.
    - The response body contains the error message: `"Employee with ID 999 not found"`

#### 5. **`testGetPayrollByEmployeeIdSuccess`**
- **Purpose**: Validates that the `getPayrollByEmployeeId` endpoint successfully retrieves list of  payrolls.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates fetching payroll by employee ID.
    - **'CompanyEmployeesRepository'**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for payroll is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains the correct payroll information.

#### 6. **`testMarkAsPaidSuccess`**
- **Purpose**: Validates that the `markAsPaid` endpoint successfully marks a specific payroll record as paid.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates updating a specific payroll record to mark it as paid.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for a paid payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Attribute was updated successfully"`

#### 7. **`testMarkAsUnpaidAlreadyCompleted`**
- **Purpose**: Validates that the `markAsUnpaid` endpoint correctly handles a scenario where the payroll record is already marked as unpaid.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates a case where the payroll record is already marked as unpaid.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for an already unpaid payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `400 Bad Request`.
    - The response body contains the error message: `"Employee has already been marked as Not Paid"`

#### 8. **`testAdjustSalarySuccess`**
- **Purpose**: Validates that the `adjustSalary` endpoint successfully adjusts the salary of a specific payroll record.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates updating a specific payroll record to adjust its salary.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for an adjusted salary payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Attribute was updated successfully"`

#### 9. **`testAdjustDaySuccess`**
- **Purpose**: Validates that the `adjustDay` endpoint successfully adjusts the payment day of a specific payroll record.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates updating a specific payroll record to adjust its payment day.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for an adjusted payment day payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Attribute was updated successfully"`

#### 10. **`testDeletePayrollByEmployeeIdSuccess`**
- **Purpose**: Validates that the `deletePayrollByEmployeeId` endpoint successfully deletes a specific payroll record.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates deleting a specific payroll record.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for a deleted payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Attribute was deleted successfully"`

#### 11. **`testAddPayrollByEmployeeIdInvalidFormat`**
- **Purpose**: Validates that the `addPayrollByEmployeeId` endpoint correctly handles invalid input format.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates a case where the input format is invalid.
    - **`CompanyEmployeesRepository`**: Simulates verification of employee existence in company by employee ID.
- **Setup**:
    - Mock data for an invalid input format is given as input.
- **Assertions**:
    - Response status code is `400 Bad Request`.
    - The response body contains the error message: `"Invalid input format for payroll data"`

#### 12. **`testDeletePayroll`**
- **Purpose**: Validates that the `deletePayroll` endpoint successfully deletes all payroll records for month and year.
- **Integration Points**:
    - **`PayrollRepository`**: Simulates deleting payroll records.
    - **`CompanyEmployeesRepository`**: Simulates getting list of employees in company.
- **Setup**:
    - Mock data for a deleted payroll record is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.
    - The response body contains a success message: `"Payroll for this month and year has been deleted"`

#### 13. **`testCreateCompanySuccess`**
- **Purpose**: Validates that the `createCompany` endpoint successfully creates a new company.
- **Integration Points**:
    - **`CompanyRepository`**: Simulates creating a new company.
- **Setup**:
    - Mock data for a new company is returned from the repository.
- **Assertions**:
    - Response status code is `201 CREATED`.

#### 14. **`testCreateCompanyFailure`**
- **Purpose**: Validates that the `createCompany` endpoint correctly handles a scenario where the company already exists.
- **Integration Points**:
    - **`CompanyRepository`**: Simulates a case where the company already exists.
- **Setup**:
    - Mock data for an existing company is given as input.
- **Assertions**:
    - Response status code is `400 Bad Request`.

#### 15. **`testChangePasswordSuccess`**
- **Purpose**: Validates that the `changePassword` endpoint successfully changes the password of a company.
- **Integration Points**:
    - **`CompanyRepository`**: Simulates changing the password of a company.
- **Setup**:
    - Mock data for a company with a new password is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.

#### 16. **`testChangePasswordFailure`**
- **Purpose**: Validates that the `changePassword` endpoint correctly handles a scenario where the company does not exist.
- **Integration Points**:
    - **`CompanyRepository`**: Simulates a case where the company does not exist.
- **Setup**:
    - Mock data for a non-existent company is given as input.
- **Assertions**:
    - Response status code is `400 Bad Request`.

#### 17. **`getAllEmployeesSuccessTest`**
- **Purpose**: Validates that the `getAllEmployees` endpoint successfully returns a list of employees.
- **Integration Points**:
    - **`CompanyEmployeesRepository`**: Simulates getting a list of employees in a company.
    - **`EmployeeRepository`**: Simulates getting a list of employees.
- **Setup**:
    - Mock data for a list of employees is returned from the repository.
- **Assertions**:
    - Response status code is `200 OK`.

## External Integration Tests

This section provides details on the internal integration tests in the `com.nullterminators.project.integration.external` package. These tests ensure the calls to the repositories are handled correctly and integrates correctly.
Our external integration tests directly call the repository methods and validate their responses.

### Our external integration tests:

#### 1. **`testFindAllByEmployeeIdOrderByPaymentDateDesc`**
- **Purpose**: Validates that the `findAllByEmployeeIdOrderByPaymentDateDesc` method correctly retrieves payroll records for a specific employee.
- **Assertions**:
    - The response contains the correct payroll information.

#### 2. **`testFindByEmployeeIdPaymentMonthAndYear`**
- **Purpose**: Validates that the `findByEmployeeIdPaymentMonthAndYear` method correctly retrieves a specific payroll record.
- **Assertions**:
    - The response contains the correct payroll information.

#### 3. **`testSave`**
- **Purpose**: Validates that the `save` method correctly saves a new payroll record.
- **Assertions**:
    - Checks if the payroll record is saved successfully.

#### 4. **`testDelete`**
- **Purpose**: Validates that the `delete` method correctly deletes a payroll record.
- **Assertions**:
    - Checks if the payroll record is deleted successfully.

#### 5. **`testFindByUserName`**
- **Purpose**: Validates that the `findByUserName` method correctly retrieves a company record.
- **Assertions**:
    - Checks if the company record is retrieved successfully.

#### 6. **`testFindAllByCompanyUsernameAndEmployeeId`**
- **Purpose**: Validates that the `findAllByCompanyUsernameAndEmployeeId` method correctly retrieves company employees.
- **Assertions**:
    - Checks if the employee is in the company.

#### 7. **`testFindAllByEmployeeId`**
- **Purpose**: Validates that the `findAllByEmployeeId` method correctly retrieves company employees by employee ID.
- **Assertions**:
    - Checks if the list of employees is retrieved successfully.

#### 8. **`testFindAllByCompanyUsername`**
- **Purpose**: Validates that the `findAllByCompanyUsername` method correctly retrieves company employees by company username.
- **Assertions**:
    - Checks if the list of employees is retrieved successfully.

## An Initial Note to Developers
If you intend to use this service, start by creating a company via the `/registerCompany` endpoint. Then, use that company's credentials to access the service endpoints. To segregate your data across environments (e.g., sandbox, dev, and prod), create a separate company for each environment. The service will handle the segregation for you.

Using the provided endpoints as outlined to integrate the service into your client. For detailed instructions on deploying both the client and the service, please refer to the client GitHub repository.

## Postman Testing
The Postman Collection is located in the `postman` folder.

## Reporting

#### Style Check Report
After the style check command is run, the report is generated in the `target/site` folder as html file.

![Style Check Report](reports/checkstyle.png)

#### Branch Coverage Report
After the test coverage command is run, the report is generated in the `target/site/jaCoCo` folder as html file.
This project has 86% branch coverage.

![Branch Coverage Report](reports/branchcoverage.png)

#### PMD Report
After the PMD check command is run, the report is generated in the `target/report` folder as html file.

![PMD Report](reports/pmd.png)
