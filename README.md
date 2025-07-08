# VynqTalk Server 🚀

**VynqTalk Server** is the powerful back-end for VynqTalk, a modern chat application built with Spring Boot and Java. It provides robust RESTful APIs, real-time messaging, secure authentication, and seamless data management.

---

## ✨ Features

- ⚡ **Real-time Messaging** (REST APIs & WebSocket)
- 🔒 **Secure Authentication** (JWT, Spring Security)
- 🗄️ **PostgreSQL Integration** (with Spring Data JPA)
- 📊 **Monitoring** (Spring Boot Actuator)
- 🛡️ **Advanced Security** (CSRF, XSS, Rate Limiting)
- 📚 **Comprehensive API Docs** (Swagger UI)

---

## 🚀 Quick Start

### 1. Clone the Repository

```bash
# Copy this command
git clone https://github.com/goal651/vynqtalk-server.git
cd vynqtalk-server
```

### 2. Set Up PostgreSQL

```sql
-- Copy and run in your PostgreSQL client
CREATE DATABASE vynqtalk;
```

### 3. Configure Environment

Create a `.env` file in the project root:

```properties
# Copy and edit with your credentials
PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/vynqtalk
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
```

### 4. Build & Run

```bash
# Copy and run in your terminal
./mvnw clean install
./mvnw spring-boot:run
```

The server will start at: [http://localhost:8080](http://localhost:8080)

---

## 📖 API Documentation

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Actuator Endpoints:** [http://localhost:8080/actuator](http://localhost:8080/actuator)

---

## 🔐 Security Highlights

- JWT-based authentication
- Role-based access control
- CSRF & XSS protection
- Rate limiting

---

## 🛠️ Troubleshooting

- Ensure PostgreSQL is running and accessible
- Verify credentials in `.env` and `application.properties`
- Check Java & Maven versions:
  ```bash
  java -version
  mvn -version
  ```

---

## 🤝 Contributing

Contributions are welcome!  
Fork the repo, submit pull requests, or report issues.

---

## 💬 Support

If you like this project, please give it a ⭐ on GitHub!

---
