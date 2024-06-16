#glavni img
#FROM maven:3.9-amazoncorretto-21 AS build

#WORKDIR /app

#COPY pom.xml .
#RUN mvn dependency:go-offline

#COPY src ./src

#RUN mvn clean package -DskipTests

#pokretanje

#FROM maven:3.9-amazoncorretto-21

#WORKDIR /app

#COPY --from=build /app/target/store-0.0.1-SNAPSHOT.jar app.jar

#EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "app.jar"]


FROM maven:3.8.4 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]