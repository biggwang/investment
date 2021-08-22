FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/investment.jar
COPY ${JAR_FILE} investment.jar
ENV	USE_PROFILE dev
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-jar", "investment.jar"]