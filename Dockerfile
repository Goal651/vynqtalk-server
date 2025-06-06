FROM maven:3.9.6-eclipse-temurin-21 AS build

# Build arguments for Maven
ARG MAVEN_OPTS="-Xmx2048m -Xms1024m -XX:MaxPermSize=512m -Dmaven.repo.local=/usr/share/maven/ref/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version -DinstallAtEnd=true -DdeployAtEnd=true"

WORKDIR /app

# Copy only pom files first to cache dependencies
COPY pom.xml .
COPY */pom.xml ./
RUN mkdir -p target && \
    for f in $(find . -name "pom.xml"); do \
        mkdir -p $(dirname $f)/src/main/java; \
        touch $(dirname $f)/src/main/java/.gitkeep; \
    done

# Download dependencies
RUN mvn $MAVEN_CLI_OPTS dependency:go-offline

# Copy source code
COPY src ./src

# Build the application with optimized settings
RUN mvn $MAVEN_CLI_OPTS clean package \
    -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.compiler.source=21 \
    -Dmaven.compiler.target=21 \
    -Dmaven.compiler.release=21

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