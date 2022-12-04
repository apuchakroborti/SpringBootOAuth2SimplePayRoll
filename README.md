# payroll-management-system
This is a Spring Boot Application for Payroll Management System

In this project I will show how to implement authorization using oauth2 from scratch:- \
SpringBoot
Flyway
Lombok
OAuth2

Steps to follow: 
1. Install MySQL \
1.1. Create user and give permission to it:- \
$ CREATE USER 'apu'@'localhost' IDENTIFIED BY 'tigerit'; \
$ create database testpayroll; \
$ GRANT ALL PRIVILEGES ON testpayroll.* TO 'apu'@'localhost'; 

2. Now to you have to create the necessary tables listed in the migration scripts from 1 to 6. 
If you run this project the tables will be created automatically. 

3. Now to you have to insert some initial data into some tables: \
3.1. Insert two roles into the oauth_authority table authority name like ADMIN and USER \
3.2. Create an ADMIN user by inserting data into the oauth_user table \
username: admin@gmail.com \
password: The hashed password of '1234' by using the Bcrypt password encoder algorithm of strength 8. \
You can get it online and use any one of them. \
3.3. Insert data into the oauth_client_details tables. Insert rows based on the different types of client. The attributes are 
CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY
Here the client secret must be hashed. You can use the Bcrypt password encoder algorithm of strength 4. 

4. BUILD and RUN the Project: \
BUILD: $ mvn clean package \
Then run the project by using the below command: \
$ mvn spring-boot:run 

5. Access and Refresh Token generation: \
URL: http://127.0.0.1:9000/service-api/oauth/token \
Request Method: POST \
Headers: 
Content-Type: application/x-www-form-urlencoded \
Accept: application/json \
Authorization: Basic {{$token}} \
Here, $token: 'dGVzdC13ZWJhcHAtcnc6dGVzdC13ZWJhcHAtcnctMTIzNA==' \
How to make token: Base64Encoded string of 'test-webapp-rw:test-webapp-rw-1234' \
Body: 
grant_type: password \
username: YOUR_USERNAME (here, admin@gmail.com) \
password: YOUR_PASSWORD (1234) 
You will get the access and refresh token here

6. Now You can call the Employee related crud services:
HEADERS:
Content-Type: application/json
Authorization: Bearer {{access_token}} //from step 5

6.1 To enroll a new employee: \
POST http://127.0.0.1:9000/service-api/api/employee \
6.2 To get an employee info by id: \
GET http://127.0.0.1:9000/service-api/api/employee/{id}  
6.3 To get paginated employees by search criteria: \
GET http://127.0.0.1:9000/service-api/employee \
6.4 To update a employee: \
PUT http://127.0.0.1:9000/service-api/api/employee/{id}  
6.5 To delete a employee by id: \
DELETE http://127.0.0.1:9000/service-api/api/employee/{id} \
6.6 To update password: \
POST http://127.0.0.1:9000/service-api/api/employee/update-password \
6.7 To reset password by username: \
POST http://127.0.0.1:9000/service-api/api/employee/reset-password 

7. Update employee's current salary: Only admin user can do this
7.1 To insert a new salary for an employee: \
POST http://127.0.0.1:9000/service-api/api/salary \
7.2 To get all salary information by search criteria: \
GET http://127.0.0.1:9000/service-api/api/salary  
7.3 To get current salary of an employee by employeeId: \
GET http://127.0.0.1:9000/service-api/api/salary/{employeeId} \

Google-Signin:
Go to the url: https://console.cloud.google.com/ \
Dashboard --> APIs and Services --> Credentials --> Create Credentials --> OAuth Client ID --> \
OAuth consent screen --> External

Application Type --> Web Application \
Name --> [SimplePayroll] \
Urls --> http://localhost:9000 \
Authorized redirect URIs --> \
URIs --> http://localhost:9000/login/oauth2/code/google --> Create

We can see the message
OAuth client created
Your client ID: <<copy from https://console.cloud.google.com credentials>> 
Your Client Secret: <<copy from https://console.cloud.google.com credentials>> 



