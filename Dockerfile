FROM maven:3.9.6-eclipse-temurin-21 AS build

# Build arguments for Maven
ARG MAVEN_OPTS="-Xmx2048m -Xms1024m -Dmaven.repo.local=/usr/share/maven/ref/repository"
ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version"

WORKDIR /app

# Copy pom files
COPY pom.xml .

# Download dependencies
RUN mvn $MAVEN_CLI_OPTS dependency:go-offline

# Copy source code
COPY src ./src

# Build the application with debug output
RUN mvn $MAVEN_CLI_OPTS clean package \
    -DskipTests \
    -Dmaven.compiler.source=21 \
    -Dmaven.compiler.target=21 \
    -Dmaven.compiler.release=21 \
    -X

# Runtime stage
FROM tomcat:10.1-jdk21-temurin

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.security.egd=file:/dev/./urandom"
ENV CATALINA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to Tomcat's webapps directory
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"] 