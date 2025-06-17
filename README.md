# VynqTalk Server (Back-end)

This is the back-end for VynqTalk, a chat application built with Spring Boot and Java. It provides RESTful APIs for messaging, user authentication, and data management, using MySQL as the database.
Features

REST APIs for real-time messaging
User authentication with Spring Security
Database integration with MySQL

Prerequisites

Java (JDK 21 or higher)
Maven (v3.8 or higher)
PostgreSQL
A running Postgresql server

Setup Instructions

Clone the repository:
git clone <https://github.com/goal651/vynqtalk-server.git>
cd vynqtalk-server

Set up PostgreSQL database:

Create a database named vynqtalk:CREATE DATABASE vynqtalk;

Update the application.properties file in src/main/resources with your MySQL credentials:spring.datasource.url=jdbc:postgresql://localhost:3306/vynqtalk
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update

Build the project:
mvn clean install

Run the application:
mvn spring-boot:run

The server will start at <http://localhost:8080>.

Troubleshooting

Ensure PostgreSQL is running and accessible with the provided credentials.
Check application.properties for correct database settings.
Verify Java and Maven versions with java -version and mvn -version.

Contributing
Contributions are welcome! Fork the repo, submit pull requests, or report issues.
Support
If you like this project, please give it a ⭐ on GitHub!
