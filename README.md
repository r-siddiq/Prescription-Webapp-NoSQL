# Prescription Webapp NoSQL

This is a Spring Boot web application that manages prescriptions using a NoSQL database, specifically MongoDB.

## Features

The application is a web-based system for handling prescriptions, including:

  * **Doctor Management**: Functionality to manage doctor information.
  * **Patient Management**: Functionality to create and update patient records.
  * **Prescription Management**: Functionality to create and fill prescriptions.
  * **Data Models**: The application includes data models for `Doctor`, `Patient`, `Prescription`, `Drug`, and `Pharmacy`.
  * **Data Population**: A JavaScript file is included to populate the database with initial data.
  * **User Interface**: The application uses Thymeleaf to render HTML pages for various views such as `doctor_register`, `patient_create`, `prescription_fill`, etc..

-----

## Dependencies

The project is built with Maven. Key dependencies include:

  * **Spring Boot Starter Data MongoDB**: For interacting with the MongoDB database.
  * **Spring Boot Starter Web**: For building the web application.
  * **Spring Boot Starter Thymeleaf**: For server-side rendering of web pages.
  * **Spring Boot DevTools**: For development-time features like automatic restarts.
  * **Java 17**: The project requires Java 17 to run.

-----

## Prerequisites

To run this application, you must have the following installed:

  * Java Development Kit (JDK) 17 or higher
  * Maven
  * MongoDB running on your local machine.

## Getting Started

1.  **Clone the repository**: Clone this project from its source.
2.  **Configure MongoDB**: The application expects a MongoDB instance running on `localhost` at port `27017` with a database named `test`.
3.  **Run the application**: Navigate to the project directory and run the main application file.

-----

## Build and Run

You can build the project using Maven:

```bash
./mvnw clean package
```

To run the application, use the generated JAR file:

```bash
java -jar dist/prescription-webapp-NoSQL-1.0.0-SNAPSHOT.jar
```

-----

## Project Structure

This project follows a standard Maven project structure. The key directories and files are:

  * `src/main/java`: Contains the Java source code.
  * `src/main/resources`: Contains configuration files and web resources.
  * `pom.xml`: The project's configuration file.
  * `dist/`: The directory where compiled code and the runnable JAR file are generated. This directory should only contain the final JAR file after a successful build.
