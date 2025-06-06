FROM maven:3.8.6-eclipse-temurin-17 AS build

# Build arguments for Maven
ARG MAVEN_OPTS="-Dmaven.repo.local=/usr/share/maven/ref/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
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
RUN mvn $MAVEN_CLI_OPTS clean package -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true

# Runtime stage
FROM tomcat:9.0-jre17-temurin

# Add build arguments for runtime
ARG TOMCAT_VERSION=9.0
ARG TOMCAT_HOME=/usr/local/tomcat

WORKDIR $TOMCAT_HOME

# Remove default Tomcat webapps and optimize Tomcat
RUN rm -rf webapps/* && \
    rm -rf work/* && \
    rm -rf temp/* && \
    mkdir -p webapps/ROOT

# Copy the WAR file to Tomcat's webapps directory
COPY --from=build /app/target/*.war webapps/ROOT.war

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.security.egd=file:/dev/./urandom"
ENV CATALINA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Create a non-root user
RUN groupadd -r tomcat && \
    useradd -r -g tomcat tomcat && \
    chown -R tomcat:tomcat $TOMCAT_HOME

USER tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"] 