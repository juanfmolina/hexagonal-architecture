# Applying Hexagonal Architecture

This project is an implementation based on a Bank solution to calculate taxes given a list of operations

## Requirements

Install Java 11.0.11
Maven 3.9.0

## Installation

Use the maven tool to compile and create the jar

```bash
cd hexagonal-architecture
mvn clean compile install
```

Now you will have the artifact can be run using java in **target/hexagonal-architecture.jar**
## Testing

Use the maven tool to test the different test in the project

```bash
cd hexagonal-architecture
mvn clean test
```

## Run

Use the java tool to run the Java Artifact

```bash
java -jar target/hexagonal-architecture.jar com.bank.Application
```


## Architectural and Design decisions
* project was developed under Hexagonal Architecture to prevent coupling between the domain and the technical aspects
* The policy and specification patterns were used to define the business logic
* Transformer pattern was used to transform outer object (like Data Transfer Object) to domain objects 

## Frameworks used
* Lombok to easy the coding process
* Jackson to parse string Json representations to Java Objects


