FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:9.0-jre17-temurin
WORKDIR /app

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to Tomcat's webapps directory
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080
CMD ["catalina.sh", "run"] 