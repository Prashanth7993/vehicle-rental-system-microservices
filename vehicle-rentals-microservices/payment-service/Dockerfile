FROM maven AS build 

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests


FROM openjdk:17-oracle


COPY --from=build /app/target/payment-service.jar payment-service.jar

CMD ["java","-jar","payment-service.jar"]
