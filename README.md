[![Build Status](https://travis-ci.com/aniewie/person-api.svg?branch=master)](https://travis-ci.com/aniewie/person-api)
# person-api 

Simple RESTful API, which provides a service for storing, updating, retrieving and deleting Person entities

## Run

This is a Spring Boot/Java 8/Maven application. Clone the project and run `./mvnw spring-boot:run` from the main folder to quickly run the application in an exploded form [(as descibed here)](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html#using-boot-running-with-the-maven-plugin).
You can also build a jar with `./mvnw package` and run it on Java:

`java -jar target/person-api-0.0.1-SNAPSHOT.jar`

The application runs on an embedded Tomcat Web Server, which by default listens on 8080 port. 

## Authenticate

Authentication is required for all the `person` endpoints and the application expects a token generated by itself. 
The application provides an additional endpoint `/token`, from where the token can be retrieved. The `/token` endpoint is protected with Basic Authentication.
The are 2 (hardcoded) user accounts:

* `user:password` - role USER
* `admin:password` - roles USER, ADMIN.

You can use a browser to get a token, using credentials of a chosen user. The output in the browser will be the generated token.

To authenticate for any `/person` methods pass a valid token in `Authorization: Bearer ` header (a token without `Bearer ` prefix will also be accepted).
A token expires in 10 minutes.

## Play

Enpoints for Person resources retrieval and manipulation:
* Retrieve all person entities (packed in a wrapper object) GET http://localhost:8080/person
* Create a new Person POST http://localhost:8080/person - only ADMIN role
* Delete a Person DELETE http://localhost:8080/person/{id} - only ADMIN role
* Retrieve a Person GET http://localhost:8080/person/{id} 
* Update a Person PUT http://localhost:8080/person/{id} - only ADMIN role
* Partially update a Person PATCH http://localhost:8080/person/{id}

You can list and test all available methods using Swagger UI: `http://localhost:8080/swagger-ui.html` or simply go to `http://localhost:8080`.
You can authenticate in Swagger UI by clicking any of the padlock icons and pasting you valid token in the input field.

## Test

You can run integration tests with Maven command: `./mvnw verify`
