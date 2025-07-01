# VynqTalk Server (Back-end)

This is the back-end for VynqTalk, a chat application built with Spring Boot and Java. It provides RESTful APIs for messaging, user authentication, and data management.

## Features

- REST APIs for real-time messaging
- WebSocket support for real-time communication
- User authentication with Spring Security
- PostgreSQL database integration
- Spring Data JPA for data persistence
- Spring Boot Actuator for monitoring

## Prerequisites

- Java (JDK 21 or higher)
- Maven (v3.8 or higher)
- PostgreSQL 14+ (or compatible version)
- Git

## Setup Instructions
1. Clone the repository:

```bash
git clone https://github.com/goal651/vynqtalk-server.git
cd vynqtalk-server
```

2. Set up PostgreSQL database:

```sql
CREATE DATABASE vynqtalk;
```

3. Create a `.env` file with the following content      :

```properties
PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/vynqtalk
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
```

4. Build and run the project:

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The server will start at http://localhost:8080.

## API Documentation

The API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator endpoints: http://localhost:8080/actuator

## Security

The application uses Spring Security for:
- JWT-based authentication
- Role-based access control
- CSRF protection
- XSS protection
- Rate limiting

## Troubleshooting

- Ensure PostgreSQL is running and accessible with the provided credentials
- Verify database connection settings in `application.properties`
- Check Java and Maven versions with `java -version` and `mvn -version`

## Contributing
Contributions are welcome! Fork the repo, submit pull requests, or report issues.

## Support
If you like this project, please give it a ⭐ on GitHub!
