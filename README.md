# Lotto Project

**Lotto Project** is a Spring Boot-based backend web application implementing a lottery game. Users can provide six unique numbers (within the range 1-99) to generate a unique lottery ticket identified by a hash. The system generates winning numbers weekly, validates user submissions, and announces results. It uses NoSQL database (MongoDB) for tickets and results repositories.

---

## Features

- **Number Submission**: Users can submit six unique numbers to generate a ticket.
- **Weekly Draws**: Winning numbers are generated automatically every week.
- **Result Checking**: Users can retrieve results using their unique ticket hash.
- **Validation**: Ensures that submitted numbers are exactly six and within the range of 1–99.
- **Data Persistence**: Uses MongoDB to store tickets, winning numbers, and results.
- **Integration Tests**: Includes tests with Testcontainers and WireMock for validation.
- **API Documentation**: Exposes REST API endpoints documented via Swagger.

---

## Architecture

The application employs a modular monolith hexagonal architecture with separate responsibilities, including:
- **Number Receiver**: Handles user inputs and ticket generation.
- **Numbers Generator**: Generates random winning numbers within the specified range.
- **Result Announcer**: Matches user tickets with winning numbers and calculates results.
- **Result Checker**: Enables users to check results using their ticket hash.

## Tech Stack

- **Java 17**
- **Apache Maven**
- **Spring Boot**
- **MongoDB**
- **Docker**
- **JUnit, Testcontainers**
- **Swagger for API Documentation**

## Rest-API Endpoints

Base URL: http://localhost:8080

| Endpoint           | Method | Request                  | Response | Description                 |  
|--------------------|--------|--------------------------|----------|-----------------------------|
| `/inputNumbers`    |  POST  | RequestBody (requestDto) | JSON     | Submit six unique numbers             |
| `/results/{id}`    |   GET  | PathVariable (id)        | JSON     | Check results using the ticket hash   |

