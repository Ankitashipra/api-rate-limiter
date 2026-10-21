# API Rate Limiter 

A small Java project to learn how API rate limiting works. It implements the Fixed Window Rate Limiting algorithm and demonstrates how to limit requests independently for different clients.

This is a learning project focused on understanding the algorithm, writing tests, and practicing core Java concepts. It is not a production-ready API server.

## Learning Goals

By building this project, I am learning:

- How rate limiting protects services from excessive requests.
- How the fixed window algorithm works.
- How to maintain separate request counts for different clients.
- How Java 'Map' and objects can represent client state.
- How to use 'Duration', 'Instant', and 'Clock' to handle time.
- How 'synchronized' helps protect shared state from concurrent access.
- How to write automated tests using JUnit 5 and Maven.

## How It Works

The rate limiter allows a configured number of requests per client during a fixed time window.


Each client has an independent counter. Reaching the limit for one client does not consume another client's quota.

### Algorithm

1. Receive a client ID.
2. Find or create that client's window state.
3. Check whether the current window has expired.
4. Reset the counter if the window has expired.
5. Reject the request if the limit has already been reached.
6. Otherwise, increment the counter and accept the request.

## Tech Stack

- **Java 21**
- **Maven** — build and dependency management
- **JUnit 5** — automated testing

## Project Structure

```text
api-rate-limiter/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── ratelimiter/
    │           ├── WindowState.java
    │           ├── FixedWindowRateLimiter.java
    │           └── Main.java
    └── test/
        └── java/
            └── ratelimiter/
                └── FixedWindowRateLimiterTest.java
```

## Requirements

Install:

- JDK 21 or compatible Java 21 runtime
- Apache Maven 3.9 or compatible Maven version


## Run the Project

Clone the repository after publishing it to GitHub, or open the existing project folder.

### 1. Run automated tests


mvn test

### 2. Build the project


mvn clean package


### 3. Run the demo

java -cp target/classes ratelimiter.Main


The demo sends sample requests using different client IDs and prints whether each request is accepted or rejected.


## Purpose

The purpose of this project is to build a clear understanding of rate limiting through implementation and testing. The emphasis is on learning the algorithm, understanding design trade-offs, and being able to explain the code.
