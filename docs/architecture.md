# Architecture Overview

vynqtalk-server is a modular, scalable chat server built with Java and Spring Boot.

## Main Components

- **Controllers:** Handle HTTP and WebSocket requests
- **Services:** Business logic and data processing
- **Repositories:** Data access layer (JPA/Hibernate)
- **DTOs:** Data transfer objects for API communication
- **WebSocket Handlers:** Real-time messaging
- **Security:** JWT-based authentication and authorization

## High-Level Diagram

``` bash
[Client]
   |
[REST API / WebSocket]
   |
[Controllers]
   |
[Services]
   |
[Repositories]
   |
[Database]
```

## Technologies Used

- Java 21
- Spring Boot
- Spring Security
- WebSocket
- JPA/Hibernate
- Maven

For more details, see the source code or other docs in this folder.
