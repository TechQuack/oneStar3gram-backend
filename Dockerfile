FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY ./pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine AS prod
WORKDIR /app
ENV PATH="$PATH:/sbin"
RUN apk add maven
COPY --from=build /app/target/oneStar3gram-0.0.1-SNAPSHOT.war app.war
ENTRYPOINT ["java","-war","/app/app.war"]

FROM build AS dev
WORKDIR /app
COPY . .
RUN keytool -importcert -noprompt -trustcacerts -alias proxy-onestar3gram -file certs/cert.pem -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit
ENTRYPOINT mvn spring-boot:run