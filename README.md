# User Service

Spring boot application that provides an API for making operation with authorization:
- create user
- login
- check permissions

## Configuration

1. login to Mysql server (change credentials in [application.yml](src/main/resources/application.yml))
2. create DB schema named **user_db**
3. setup environment variables: APP_JWT_SECRET and APP_JWT_EXPIRATION_MILLISECONDS e.g.
   APP_JWT_SECRET=daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb;APP_JWT_EXPIRATION_MILLISECONDS=6000000

## Service Requirements

Requires:
- a running [Eureka registry](https://github.com/Volodymyr2907/vine-registration-service);
- a running Mysql DB;

## How to run

1. run [docker-compose](src/main/resources/docker/docker-compose.yml) file for MySql and RabbitMq
2. ```bash mvn clean install```
3. run [Eureka registry](https://github.com/Volodymyr2907/vine-registration-service);
4. run [user service](src/main/java/com/mentorship/userservice/UserServiceApplication.java)