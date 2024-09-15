# transit-fare-calculator
***
## Prerequisites
- Java 11 or higher.
- Maven 3.8.5 or higher (if you prefer to use a local Maven installation).
- Clone/download the Github repository provided in Code section.
- Run the project from the main folder /transit-fare-calculator to ensure access to the source resources.


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

## Repo
[Github](https://github.com/mullaivendhan-ariaputhri/transit-fare-calculator)


## Assumptions
- **Input validation:** The input file is correctly formatted, with no leading or trailing spaces or tab characters, and includes all necessary data. Any improperly formatted records may result in errors.
- **Unique Payment mode:** The passenger uses the same credit card for both tap-on and tap-off.
- **Unique Tap-On/Off Pairing:** Pairing is done by matching PAN and ordered by time. Each Tap-On event and consecutive Tap-Off event will be considered as a completed trip.
- **Chronological Order:** Tap-On events precede Tap-Off events for the same trip when ordered by time for the same PAN.
- **Single Active Trip / Passenger:** A passenger can only have one active trip at a time, meaning a Tap-On must be followed by a Tap-Off before another Tap-On for the same card.
- **Handling of Orphan Tap-Offs:** Records with a Tap-Off but no corresponding Tap-On are deemed invalid. These entries are included in the output file (trips.csv) with the status as "INVALID".

## Tech Trade-offs due to time constraints
- Batch or parallel processing.
- Validation of individual input fields.
- Scalability to handle larger datasets or high volumes.
- Comprehensive input and output validation.
- Robust exception handling.


