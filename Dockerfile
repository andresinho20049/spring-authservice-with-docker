########### Stage BUILD ###########
FROM maven:3.6.3-jdk-8-slim as builder

# Define Workdir
WORKDIR /opt/app
#Copy pom.xml to container
COPY pom.xml ./

RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean
RUN mvn install package -DskipTests

########### Stage RUN ###########
FROM maven:3.6.3-jdk-8-slim

# Define Workdir
WORKDIR /opt/app

# Copy target from build stage
COPY --from=builder /opt/app/target/AuthService.war ./
ENTRYPOINT ["java", "-jar", "./AuthService.war"]
