FROM maven:3.9.6-eclipse-temurin-21 AS build

# Build arguments for Maven
ARG MAVEN_OPTS="-Xmx512m -Xms256m -Dmaven.repo.local=/usr/share/maven/ref/repository"
ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version"

WORKDIR /app

# Copy pom files
COPY pom.xml .

# Download dependencies
RUN mvn $MAVEN_CLI_OPTS dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn $MAVEN_CLI_OPTS clean package \
    -DskipTests \
    -Dmaven.compiler.source=21 \
    -Dmaven.compiler.target=21 \
    -Dmaven.compiler.release=21

# Runtime stage
FROM tomcat:10.1-jdk21-temurin

# Set environment variables
ENV JAVA_OPTS="-Xmx192m -Xms96m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+DisableExplicitGC -Djava.security.egd=file:/dev/./urandom"
ENV CATALINA_OPTS="-Xms96m -Xmx192m -XX:+UseG1GC"

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to Tomcat's webapps directory
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Create a script to wait for database and check application startup
RUN echo '#!/bin/sh\n\
echo "Waiting for database connection..."\n\
for i in $(seq 1 30); do\n\
  if nc -z $DATABASE_HOST $DATABASE_PORT; then\n\
    echo "Database is ready!"\n\
    break\n\
  fi\n\
  echo "Waiting for database... attempt $i of 30"\n\
  sleep 2\n\
done\n\
# Start Tomcat\n\
catalina.sh run &\n\
# Wait for application to start\n\
for i in $(seq 1 30); do\n\
  if curl -s http://localhost:8080/actuator/health > /dev/null; then\n\
    echo "Application is ready!"\n\
    break\n\
  fi\n\
  echo "Waiting for application... attempt $i of 30"\n\
  sleep 2\n\
done\n\
# Keep container running\n\
tail -f /usr/local/tomcat/logs/catalina.out' > /usr/local/tomcat/bin/start.sh && \
chmod +x /usr/local/tomcat/bin/start.sh

EXPOSE 8080
CMD ["/usr/local/tomcat/bin/start.sh"] 