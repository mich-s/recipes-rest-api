# Recipes REST API
## Requirements
```
Java 8, Gradle 4, MySQL 5
```
## Setup
### Clone the repository
`git clone https://github.com/mich-s/recipes-rest-api.git`
### Enter your database credentials
1.  _build.gradle_ - Flyway build script
```
flyway {
	url = 'jdbc:mysql://localhost:3306/recipes?serverTimezone=UTC&useSSL=false&createDatabaseIfNotExist=true'
	user = ''
	password = ''
}
```
2.   _src/main/resources/persistence-mysql.properties_
```
jdbc.url=jdbc:mysql://localhost:3306/recipes?serverTimezone=UTC&useSSL=false
jdbc.user=
jdbc.password=
```
### Database migration
Open a command line and type:
```
gradlew flywayMigrate
```

To check status of data migration:
```
gradlew flywayInfo
```

## Run the app with _gretty_ plugin
```
gradlew appRun
```
## Test API
There are several tools to test REST APIs, e.g. `cURL`, `Postman`. 
I've created Recipes REST Client which is designed to call Recipes REST API.
### [Recipes REST Client](https://github.com/mich-s/recipes-client )

## API Endpoints
| HTTP method | endpoint | action |
|:---:|:---:|:---:|
| GET | /recipes-api/recipes | list recipes |
| GET | /recipes-api/recipes/{id} | read a single recipe |
| POST | /recipes-api/recipes | create a new recipe |
| PUT | /recipes-api/recipes/{id} | update recipe |
| DELETE | /recipes-api/recipes | delete recipe | 


## Running the tests
```
gradlew test
```
## Built With

* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc) - web framework
* [Gradle](https://gradle.org/) - build tool
* [Hibernate](http://hibernate.org/) - ORM mapping tool
* [Flyway](https://flywaydb.org/) - database migration tool
* [MySQL](https://www.mysql.com/) - RDBMS
* [Gretty](https://github.com/akhikhl/gretty) - gradle plugin for running web-apps on jetty and tomcat
* [jackson-databind](https://github.com/FasterXML/jackson-databind) - data-binding package for Jackson
* tests:
	* [Spring TestContext Framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-framework) - testing framework
	* [JUnit 5](https://junit.org/junit5/) - testing framework
	* [Mockito](https://site.mockito.org/) - mocking framework
	* [hamcrest](http://hamcrest.org/) - matcher framework
	* [jsonPath](https://github.com/json-path/JsonPath) - a Java DSL for reading JSON documents
	* [H2](http://www.h2database.com/html/main.html) - in-memory database
