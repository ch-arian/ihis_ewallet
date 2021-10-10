# IHIS E-Wallet Application
This project is for users to retrieve, check, and transfer e-wallet dollars

## Table of Contents
- Tech Stack Used
- Environment Setup
- Walk Through
	
## Tech Stack Used
-	Spring Boot
-	Docker
-	DynamoDB

## Environment Setup
### PreRequisites	
-	Java 8
-	Docker
-   Maven

### Setting up
1. Build the application by executing following command in CMD `mvn clean install`
2. Dockerise and run the application by executing following command `docker-compose -f .\docker\docker-compose-dep.yml up -d`
3. Access Swagger by visting http://localhost:8080/swagger-ui.html
NOTE: You will be prompt to login after u visit the page. You can press cancel and ignore it.

## Walk Through
- This application is stateless and uses JWT for authentication.
- First, execute **/register** to register new user.
- After that, execute **/login** to login.
- You may execute **/viewBalance** and **/viewTransaction** to view your details
- You can also register new user to transfer ewallet dollar using API **/transfer**