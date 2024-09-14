# transit-fare-calculator
***
## Prerequisites
- Java 11 or higher
***
# Using Maven Wrapper
## Build and Run
To build the application, run:
```bash
./mvnw clean package
```

To run the application:
```bash
java -jar target/transit-fare-calculator.jar
```
***

# Without Maven Wrapper
If you prefer to use local Maven installation, make sure you have Maven 3.8.5 or higher installed. Build the project with:
```bash
mvn clean package
```

To run the application:
```bash
java -jar target/transit-fare-calculator.jar
```
***

# References
#### To add Maven Wrapper to the Project
```bash
mvn wrapper:wrapper
```
#### To build the application using the Maven Wrapper, run:
```bash
./mvnw clean package
```

#### Repo
[Github](https://github.com/mullaivendhan-ariaputhri/transit-fare-calculator)
