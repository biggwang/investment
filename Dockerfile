FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/investment.jar
COPY ${JAR_FILE} investment.jar
ENTRYPOINT ["java","-jar", "investment.jar"]