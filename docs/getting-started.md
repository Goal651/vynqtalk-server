# Getting Started

Welcome to vynqtalk-server! This guide will help you set up and run the server locally.

## Prerequisites

- Java 21+
- Maven 3.6+
- (Optional) Docker

## Clone the Repository

```sh
git clone https://github.com/goal651/vynqtalk-server.git
cd vynqtalk-server
```

## Build the Project

```sh
mvn clean package
```

## Run the Server

```sh
java -jar target/vynqtalk-server-1.0.0.jar
```

The server will start at [http://localhost:8080](http://localhost:8080)

## Configuration

- Edit `src/main/resources/application.properties` to customize settings (database, ports, etc.)
- Use environment variables for sensitive data (e.g., DB credentials)

## Useful Commands

- Run tests: `mvn test`
- Build Docker image: `docker build -t vynqtalk-server .`

## Need Help?

Check the [API Reference](api-reference.md) or open an issue on GitHub.
